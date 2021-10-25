package com.medicare.prohealthymedicare;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.medicare.prohealthymedicare.adapter.DataDokterAdapter;
import com.medicare.prohealthymedicare.databinding.FragmentDataDokterBinding;
import com.medicare.prohealthymedicare.model.DataDokterModels;

import java.util.ArrayList;


public class DataDokterFragment extends Fragment {
    private FragmentDataDokterBinding binding;
    private DataDokterAdapter adapter;
    private Dialog customDialog;
    private ArrayList<DataDokterModels> dokterModelsArrayList;

    String namadokter;
    private TextView txtnama,txthari,txtjam,txtcp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_data_dokter,container,false);

        addData();

        adapter = new DataDokterAdapter(dokterModelsArrayList);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());

        binding.rvdokter.setLayoutManager(layoutManager);

        binding.rvdokter.setAdapter(adapter);


        adapter.setDialog(new DataDokterAdapter.Dialog() {
            @Override
            public void onClick(int position, String nama, String hari, String jam, String cp) {
                initCustomDialog(nama,hari,jam,cp);

                customDialog.show();
            }


        });



        return binding.getRoot();
    }

    void addData(){
        dokterModelsArrayList = new ArrayList<>();
        dokterModelsArrayList.add(new DataDokterModels("Dr. Supri","Mata","Senin,Rabu,Sabtu","07.00 - 13.00",Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +R.drawable.fotodoktermata).toString(),"081234567898"));
        dokterModelsArrayList.add(new DataDokterModels("Dr. Tatan   ","Telinga","Selasa,Kamis,Jumat","08.00 - 13.00",Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +R.drawable.fotodoktertelinga).toString(),"081264553828"));

    }

    private void initCustomDialog(String namadokter,String hari, String jam , String cp){
        customDialog = new Dialog(requireContext());
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        customDialog.setContentView(R.layout.showdatadokter);
        customDialog.setCancelable(true);

        txtnama = customDialog.findViewById(R.id.txtnama);
        txthari = customDialog.findViewById(R.id.txthari);
        txtjam = customDialog.findViewById(R.id.txtjam);
        txtcp = customDialog.findViewById(R.id.txtcp);
        txtnama.setText("Dokter : "+ namadokter);
        txthari.setText("Hari : "+ hari);
        txtjam.setText("Jam : "+ jam);
        txtcp.setText("Cp : "+ cp);
    }


}