package com.medicare.prohealthymedicare;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.google.android.material.snackbar.Snackbar;
import com.medicare.prohealthymedicare.database.AppDatabase;
import com.medicare.prohealthymedicare.database.entity.DokterEntity;
import com.medicare.prohealthymedicare.databinding.FragmentRegistrasiBinding;
import com.medicare.prohealthymedicare.session.Session;
import com.medicare.prohealthymedicare.ui.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;


public class RegistrasiFragment extends Fragment {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat hariformatter;
    String CHANNEL_ID="10001";

    AppDatabase database;
    String jenis,namadokter;
    FragmentRegistrasiBinding binding;
    private String[] Item = {"Mata","Telinga"};
    private String hari;
    private String jam;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_registrasi,container,false);
        binding.getLifecycleOwner();
        database = AppDatabase.getInstance(requireContext());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        hariformatter = new SimpleDateFormat("EEEE", Locale.US);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Mata");
        arrayList.add("Telinga");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, arrayList);

        //Memasukan Adapter pada Spinner
        binding.spnpraktik.setAdapter(arrayAdapter);

        binding.spnpraktik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 jenis = adapterView.getItemAtPosition(i).toString();

                 if (jenis.equals("Mata")){
                     namadokter = "Dr. Supri";

                 }
                 if (jenis.equals("Telinga")){
                     namadokter = "Dr. Tatan";
                 }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        binding.edttanggalpraktik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });


        binding.btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_customer, new HomeFragment())
                        .commit();
            }
        });

        binding.btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar(v);
            }
        });

        return  binding.getRoot();

    }

    private void daftar(View v){

        Log.d("tester",namadokter+hari+jenis+Session.getIsFirstname(requireContext())+jam);
        String keluhan = binding.edtkeluhan.getText().toString().trim();
        if (namadokter!=null && hari!=null && jenis!=null &&jam!=null && !keluhan.isEmpty() ){

            DokterEntity cekdokter = database.dokterDao().cekdokter(Session.getIsUsername(requireContext()), jenis);
            if (cekdokter!=null){
                Snackbar.make(v, "Sudah pernah daftar", Snackbar.LENGTH_LONG).show();
            }else {

                final int min = 1;
                final int max = 20;
                final int random = new Random().nextInt((max - min) + 1) + min;
                database.dokterDao().insertdokter(namadokter,hari,jenis,Session.getIsUsername(requireContext()),jam,random,keluhan);
                notifikasi("Berhasil daftar Dokter"+ jenis, "Healthy Medicare");
                Snackbar.make(v, "Berhasil daftar", Snackbar.LENGTH_LONG).show();

            }


        }else {
            Snackbar.make(v, "Lengkapi semua kolom", Snackbar.LENGTH_LONG).show();

        }

/*
        DokterEntity cekdokter = database.dokterDao().cekdokter(Session.getIsUsername(requireContext()), jenis);
        if (cekdokter!=null){
            Snackbar.make(v, "Sudah pernah daftar", Snackbar.LENGTH_LONG).show();
        }else {
            database.dokterDao().insertcart(namadokter,hari,jenis,Session.getIsUsername(requireContext()),jam);
            Snackbar.make(v, "Registrasi berhasil", Snackbar.LENGTH_LONG).show();

        }
*/

    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();


        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                hari = hariformatter.format(newDate.getTime());
                jam = dateFormatter.format(newDate.getTime());
                binding.edttanggalpraktik.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

    public void notifikasi(String pesan, String pengirim)
    {
        String notification_title = pengirim;
        String notification_message = pesan;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(requireActivity())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message);
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(), 0, intent, 0);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        requireActivity(),
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = (int) System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) requireActivity().getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);

            mBuilder.setChannelId(CHANNEL_ID);
            mNotifyMgr.createNotificationChannel(notificationChannel);
        }
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}