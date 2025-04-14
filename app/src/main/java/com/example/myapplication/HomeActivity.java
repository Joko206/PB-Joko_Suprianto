package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvNim;
    private BottomNavigationView bottomNav;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi UI components
        tvUsername = findViewById(R.id.username);
        tvEmail = findViewById(R.id.email);
        tvNim = findViewById(R.id.nim);
        bottomNav = findViewById(R.id.bottom_navigation);

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://pb-jokosuprianto-default-rtdb.firebaseio.com");
        mDatabase = database.getReference("Users");

        // Setup Bottom Navigation
        setupBottomNavigation();

        // Load user data
        loadUserData();
    }

    private void setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                // Refresh halaman jika sudah di Home
                recreate();
                return true;
            } else if (itemId == R.id.nav_absen) {
                startActivity(new Intent(HomeActivity.this, AbsenActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_home) {
                startActivity(new Intent(HomeActivity.this, hom.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();
        Log.d("HomeActivity", "Mengambil data untuk user ID: " + userId);

        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil data langsung dari snapshot
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String nim = dataSnapshot.child("nim").getValue(String.class);

                    // Update UI di main thread
                    runOnUiThread(() -> {
                        tvUsername.setText(username != null ? username : "-");
                        tvEmail.setText(email != null ? email : "-");
                        tvNim.setText(nim != null ? nim : "-");
                    });

                    // Debug: Tampilkan data di log
                    Log.d("HomeActivity",
                            "\nUsername: " + username +
                                    "\nEmail: " + email +
                                    "\nNIM: " + nim
                    );
                } else {
                    Log.e("HomeActivity", "Data tidak ditemukan di path: Users/" + userId);
                    Toast.makeText(HomeActivity.this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("HomeActivity", "Error: " + databaseError.getMessage());
                Toast.makeText(HomeActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}