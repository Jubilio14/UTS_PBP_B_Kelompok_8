package com.medicare.prohealthymedicare.ui;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.medicare.prohealthymedicare.R;
import com.medicare.prohealthymedicare.adapter.JadwalAdapter;
import com.medicare.prohealthymedicare.database.AppDatabase;
import com.medicare.prohealthymedicare.database.entity.DokterEntity;
import com.medicare.prohealthymedicare.databinding.FragmentJadwalBinding;
import com.medicare.prohealthymedicare.session.Session;

import java.util.ArrayList;
import java.util.List;

public class JadwalFragment extends Fragment {

    FragmentJadwalBinding binding;
    private AppDatabase database;
    private JadwalAdapter orderAdapter;
    private List<DokterEntity> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_jadwal,container,false);
        binding.getLifecycleOwner();

        database = AppDatabase.getInstance(requireContext().getApplicationContext());
        list.clear();
        list.addAll(database.dokterDao().getjadwal(Session.getIsUsername(requireContext())));

        orderAdapter = new JadwalAdapter(requireContext().getApplicationContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext().getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.rvjadwal.setLayoutManager(layoutManager);
        binding.rvjadwal.setAdapter(orderAdapter);

        return  binding.getRoot();
    }
}