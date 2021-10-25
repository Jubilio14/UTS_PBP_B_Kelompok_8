package com.medicare.prohealthymedicare.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.medicare.prohealthymedicare.R;
import com.medicare.prohealthymedicare.model.DataDokterModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataDokterAdapter extends RecyclerView.Adapter<DataDokterAdapter.DataDokterViewHolder> {


    private ArrayList<DataDokterModels> dataList;
    private Dialog dialog;


    public interface Dialog {
        void onClick(int position, String nama, String hari, String jam, String cp);
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }


    public DataDokterAdapter(ArrayList<DataDokterModels> dataList) {
        this.dataList = dataList;
    }

    @Override
    public DataDokterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_datadokter, parent, false);
        return new DataDokterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataDokterViewHolder holder, int position) {
        holder.nama.setText("Nama Dokter : " + dataList.get(position).getNama());
        holder.jenis.setText("Jenis Praktik : " + dataList.get(position).getJenispraktik());
        holder.hari.setText("Hari Praktik : " + dataList.get(position).getHaripraktik());
        holder.jam.setText("Jam Praktik : " + dataList.get(position).getJampraktik());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.onClick(holder.getLayoutPosition(), dataList.get(position).getNama(), dataList.get(position).getHaripraktik(), dataList.get(position).getJampraktik(), dataList.get(position).getCp());
                }
            }
        });

        Picasso.get().load(dataList.get(position).getFoto()).into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class DataDokterViewHolder extends RecyclerView.ViewHolder {
        private TextView nama, jenis, hari, jam;
        private ImageView foto;

        public DataDokterViewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.txtnamadokter);
            jenis = (TextView) itemView.findViewById(R.id.txtjenis);
            hari = (TextView) itemView.findViewById(R.id.txthari);
            jam = (TextView) itemView.findViewById(R.id.txtjam);
            foto = (ImageView) itemView.findViewById(R.id.foto);


        }
    }
}