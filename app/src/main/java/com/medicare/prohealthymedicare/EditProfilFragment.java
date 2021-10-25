package com.medicare.prohealthymedicare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.medicare.prohealthymedicare.database.AppDatabase;
import com.medicare.prohealthymedicare.database.entity.UserEntity;
import com.medicare.prohealthymedicare.databinding.FragmentEditProfilBinding;
import com.medicare.prohealthymedicare.session.Session;
import com.medicare.prohealthymedicare.ui.ProfileFragment;

import java.io.File;


public class EditProfilFragment extends Fragment {

        FragmentEditProfilBinding binding;
    private AppDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profil, container, false);
        binding.getLifecycleOwner();
        database = AppDatabase.getInstance(requireContext());

        String foto = Session.getIsFoto(requireContext());

        binding.edtfirstname.setText(Session.getIsFirstname(requireContext()));
        binding.edtlastname.setText(Session.getIsLastName(requireContext()));
        binding.edtpassword.setText(Session.getIsPassword(requireContext()));
        binding.edtusername.setText(Session.getIsUsername(requireContext()));
        File imgFile = new  File(foto);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            binding.profileImage.setImageBitmap(myBitmap);

        }


        binding.btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = binding.edtusername.getText().toString().trim();
                String firstname = binding.edtfirstname.getText().toString().trim();
                String lastname = binding.edtlastname.getText().toString().trim();
                String password = binding.edtpassword.getText().toString().trim();

                if (binding.edtusername.getText().toString().trim().equals(Session.getIsUsername(requireContext()))){
                    database.userDao().updateakun(firstname,lastname,username,password);
                    Session.setIsFirstname(requireContext(),username);
                    Session.setIsLastName(requireContext(),lastname);
                    Session.setIsUsername(requireContext(),username);
                    Session.setIsPassword(requireContext(),password);

                    Snackbar.make(v, "Update berhasil ", Snackbar.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment_activity_customer, new ProfileFragment())
                            .commit();

                }else {
                    UserEntity cekuser = database.userDao().cekuser(username);
                    if (cekuser!=null){
                        Snackbar.make(v, "Username terdaftar ", Snackbar.LENGTH_LONG).show();
                    }else {
                        database.userDao().updateakun(firstname,lastname,username,password);
                        Session.setIsFirstname(requireContext(),username);
                        Session.setIsLastName(requireContext(),lastname);
                        Session.setIsUsername(requireContext(),username);
                        Session.setIsPassword(requireContext(),password);
                        Snackbar.make(v, "Update berhasil ", Snackbar.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_activity_customer, new ProfileFragment())
                                .commit();

                    }
                }
            }
        });

        binding.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_customer, new ProfileFragment())
                        .commit();

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}