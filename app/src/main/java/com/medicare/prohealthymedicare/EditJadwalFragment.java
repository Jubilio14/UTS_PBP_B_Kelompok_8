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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.google.android.material.snackbar.Snackbar;
import com.medicare.prohealthymedicare.database.AppDatabase;
import com.medicare.prohealthymedicare.database.entity.DokterEntity;
import com.medicare.prohealthymedicare.databinding.FragmentEditJadwalBinding;
import com.medicare.prohealthymedicare.databinding.FragmentRegistrasiBinding;
import com.medicare.prohealthymedicare.session.Session;
import com.medicare.prohealthymedicare.ui.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.NOTIFICATION_SERVICE;


public class EditJadwalFragment extends Fragment {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat hariformatter;
    String CHANNEL_ID="10001";
    AppDatabase database;
    String jenis,namadokter;
    FragmentEditJadwalBinding binding;
    private String[] Item = {"Mata","Telinga"};
    private String hari;
    private String tanggal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_jadwal,container,false);
        binding.getLifecycleOwner();

        database = AppDatabase.getInstance(requireContext());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        hariformatter = new SimpleDateFormat("EEEE", Locale.US);

        List<DokterEntity> dokter =      database.dokterDao().getjadwal(Session.getIsUsername(requireContext()));

        String[] dokterlist = new String[dokter.size()];

        for (int i =0;i<dokter.size();i++){
            dokterlist[i]= dokter.get(i).jenis;
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, dokterlist);
        //Memasukan Adapter pada Spinner
        binding.spnpraktik.setAdapter(arrayAdapter);

        binding.spnpraktik.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jenis = adapterView.getItemAtPosition(i).toString();
                DokterEntity cekdokter = database.dokterDao().cekdokter(Session.getIsUsername(requireContext()),jenis);

                binding.edttanggalpraktik.setText(cekdokter.jam);
                binding.edtkeluhan.setText(cekdokter.keluhan);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

        binding.edttanggalpraktik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });


        binding.btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtkeluhan = binding.edtkeluhan.getText().toString().trim();
                database.dokterDao().update(binding.edttanggalpraktik.getText().toString(),edtkeluhan,Session.getIsUsername(requireContext()),jenis);
                notifikasi("Berhasil update jadwal dokter"+ jenis, "Healthy Medicare");
                Snackbar.make(v, "Berhasil di update", Snackbar.LENGTH_LONG).show();

            }
        });


        return binding.getRoot();

    }

    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();


        datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                hari = hariformatter.format(newDate.getTime());
                tanggal = dateFormatter.format(newDate.getTime());
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