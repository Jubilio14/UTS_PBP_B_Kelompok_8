package com.medicare.prohealthymedicare.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.medicare.prohealthymedicare.EditJadwalFragment;
import com.medicare.prohealthymedicare.EditProfilFragment;
import com.medicare.prohealthymedicare.R;
import com.medicare.prohealthymedicare.auth.LoginActivity;
import com.medicare.prohealthymedicare.database.AppDatabase;
import com.medicare.prohealthymedicare.databinding.FragmentProfileBinding;
import com.medicare.prohealthymedicare.session.Session;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class ProfileFragment extends Fragment {

    private static final int IMAGE_CODE_CAPTURE = 10;
    String pathToFile;
    private static final int PERMISSION_CAMERA = 1;
    Uri imageuri;
    private AppDatabase database;
    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.getLifecycleOwner();
        database = AppDatabase.getInstance(requireContext());


        binding.btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.setIsLogin(requireContext(), false);
                Session.setIsFirstname(requireContext(), "");
                Session.setIsLastName(requireContext(), "");
                Session.setIsFoto(requireContext(), "");

                Intent intent = new Intent(requireContext().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();

            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

        binding.edtprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_customer, new EditProfilFragment())
                        .commit();
            }
        });


        return binding.getRoot();

    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

        LinearLayout camera = bottomSheetDialog.findViewById(R.id.btncamera);
        LinearLayout gallery = bottomSheetDialog.findViewById(R.id.btngalery);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });


        bottomSheetDialog.show();
    }


    private void openCamera() {

        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photofile = null;
        photofile = createFotoFile();

        if (photofile != null) {
            pathToFile = photofile.getAbsolutePath();
            Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.medicare.fileprovider", photofile);
            takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(takePic, 1);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                        || getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CAMERA);
                } else {
                    //permisi granted
                    bukakamera();
                }
            }else {
                //system < marshamloow
            }
        }

    }

    private void bukakamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(cameraIntent, IMAGE_CODE_CAPTURE);


    }

    @SuppressLint("QueryPermissionsNeeded")
    private File createFotoFile() {

        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("alfan", "Excep :" + e.toString());
        }

        return image;

    }
    private void opengallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),87);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            binding.profileImage.setImageBitmap(photo);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(requireContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            pathToFile = finalFile.getPath();
            database.userDao().updatefoto(pathToFile,Session.getIsUsername(requireContext()));
            Session.setIsFoto(requireContext(),pathToFile);

        }

        if (requestCode == 87) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(requireContext(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        pathToFile = finalFile.getPath();
                        binding.profileImage.setImageBitmap(bitmap);
                        database.userDao().updatefoto(pathToFile,Session.getIsUsername(requireContext()));
                        Session.setIsFoto(requireContext(),pathToFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, name, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }

        return path;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), "permisi  diijinkan", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(requireContext(), "permisi tidak diijinkan", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        String foto = Session.getIsFoto(requireContext());

        binding.txtfirstname.setText(Session.getIsFirstname(requireContext()));
        binding.txtlastname.setText(Session.getIsLastName(requireContext()));
        File imgFile = new  File(foto);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            binding.profileImage.setImageBitmap(myBitmap);

        }


    }
}