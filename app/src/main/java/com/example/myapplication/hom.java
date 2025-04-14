package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.example.myapplication.Models.Absen;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class hom extends AppCompatActivity {

    private TextView tvTotalHadir, tvTotalTidakHadir;
    private PieChart pieChart;
    private DatabaseReference databaseRef;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom);

        // Inisialisasi Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("absen");

        // Inisialisasi UI
        initializeUIComponents();
        setupBottomNavigation();
        loadAttendanceData();  // Pastikan ini dipanggil
    }

    private void initializeUIComponents() {
        tvTotalHadir = findViewById(R.id.tvTotalHadir);
        tvTotalTidakHadir = findViewById(R.id.tvTotalTidakHadir);
        pieChart = findViewById(R.id.pieChart);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Konfigurasi PieChart
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_absen) {
                startActivity(new Intent(hom.this, AbsenActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(hom.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    // Metode yang sebelumnya hilang
    private void loadAttendanceData() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int[] counts = countAttendance(snapshot);
                updateUI(counts[0], counts[1]);
                updatePieChart(counts[0], counts[1]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Gagal memuat data: " + error.getMessage());
            }
        });
    }

    private int[] countAttendance(DataSnapshot snapshot) {
        int hadir = 0;
        int tidakHadir = 0;

        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            try {
                Absen absen = childSnapshot.getValue(Absen.class);
                if (absen != null && absen.getStatus() != null) {
                    switch (absen.getStatus().toLowerCase()) {
                        case "hadir":
                            hadir++;
                            break;
                        case "tidak hadir":
                            tidakHadir++;
                            break;
                        default:
                            Log.w("UnknownStatus", "Status tidak dikenali: " + absen.getStatus());
                    }
                }
            } catch (Exception e) {
                Log.e("DataError", "Error parsing data: " + e.getMessage());
            }
        }
        return new int[]{hadir, tidakHadir};
    }

    private void updateUI(int hadir, int tidakHadir) {
        tvTotalHadir.setText(String.valueOf(hadir));
        tvTotalTidakHadir.setText(String.valueOf(tidakHadir));
    }

    private void updatePieChart(int hadir, int tidakHadir) {
        List<PieEntry> entries = new ArrayList<>();
        if (hadir > 0) entries.add(new PieEntry(hadir, "Hadir"));
        if (tidakHadir > 0) entries.add(new PieEntry(tidakHadir, "Tidak Hadir"));

        if (!entries.isEmpty()) {
            PieDataSet dataSet = new PieDataSet(entries, "Statistik Kehadiran");
            dataSet.setColors(
                    Color.parseColor("#4CAF50"),  // Hijau
                    Color.parseColor("#F44336")   // Merah
            );
            dataSet.setValueTextSize(12f);
            dataSet.setValueTextColor(Color.WHITE);

            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.animateY(1000);
            pieChart.invalidate();
        } else {
            pieChart.clear();
            pieChart.setNoDataText("Tidak ada data kehadiran");
        }
    }
}