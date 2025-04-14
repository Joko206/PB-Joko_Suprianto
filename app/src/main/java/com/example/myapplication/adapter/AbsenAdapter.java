package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.myapplication.Models.Absen;
import java.util.List;

public class AbsenAdapter extends ArrayAdapter<Absen> {
    public AbsenAdapter(Context context, List<Absen> absenList) {
        super(context, 0, absenList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Absen absen = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView tvNama = convertView.findViewById(android.R.id.text1);
        TextView tvDetail = convertView.findViewById(android.R.id.text2);

        tvNama.setText(absen.getNama());
        tvDetail.setText(absen.getTanggal() + " | " + absen.getWaktu() + " | " + absen.getStatus());

        return convertView;
    }
}