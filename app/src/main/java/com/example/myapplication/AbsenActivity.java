package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.myapplication.Models.Absen;
import com.example.myapplication.adapter.AbsenAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AbsenActivity extends AppCompatActivity {

    private EditText etNama, etTanggal, etWaktu;
    private Spinner spStatus;
    private Button btnSimpan;
    private ListView listViewAbsen;
    private BottomNavigationView bottomNav;
    private DatabaseReference databaseAbsen;
    private List<Absen> absenList;
    private AbsenAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);

        // Inisialisasi Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseAbsen = database.getReference("absen");

        // Inisialisasi UI
        initializeUIComponents();
        setupBottomNavigation();
        setupCRUDOperations();
    }

    private void initializeUIComponents() {
        etNama = findViewById(R.id.etNama);
        etTanggal = findViewById(R.id.etTanggal);
        etWaktu = findViewById(R.id.etWaktu);
        spStatus = findViewById(R.id.spStatus);
        btnSimpan = findViewById(R.id.btnSimpan);
        listViewAbsen = findViewById(R.id.listViewAbsen);
        bottomNav = findViewById(R.id.bottom_navigation);

        absenList = new ArrayList<>();
        adapter = new AbsenAdapter(this, absenList);
        listViewAbsen.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(AbsenActivity.this, hom.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_absen) {
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(AbsenActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.nav_absen);
    }

    private void setupCRUDOperations() {
        btnSimpan.setOnClickListener(v -> simpanAbsen());
        listViewAbsen.setOnItemLongClickListener((parent, view, position, id) -> {
            Absen absen = absenList.get(position);
            databaseAbsen.child(absen.getId()).removeValue();
            Toast.makeText(this, "Data dihapus!", Toast.LENGTH_SHORT).show();
            return true;
        });
        loadDataFromFirebase();
    }

    private void simpanAbsen() {
        String nama = etNama.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String waktu = etWaktu.getText().toString().trim();
        String status = spStatus.getSelectedItem().toString();

        if (nama.isEmpty() || tanggal.isEmpty() || waktu.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua field!", Toast.LENGTH_SHORT).show();
            return;
        }

        String absenId = databaseAbsen.push().getKey();
        Absen absen = new Absen(nama, tanggal, waktu, status);
        absen.setId(absenId);

        if (absenId != null) {
            databaseAbsen.child(absenId).setValue(absen)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Data tersimpan!", Toast.LENGTH_SHORT).show();
                        clearForm();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Gagal menyimpan: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void loadDataFromFirebase() {
        databaseAbsen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                absenList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Absen absen = dataSnapshot.getValue(Absen.class);
                    if (absen != null) {
                        absen.setId(dataSnapshot.getKey());
                        absenList.add(absen);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AbsenActivity.this, "Gagal memuat data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        etNama.setText("");
        etTanggal.setText("");
        etWaktu.setText("");
        spStatus.setSelection(0);
    }
}