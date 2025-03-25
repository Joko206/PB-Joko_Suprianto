package com.example.myapplication;

import android.util.Log;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.myapplication.Models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private TextView usernameTextView, emailTextView, nimTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize TextViews
        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        nimTextView = findViewById(R.id.nim);

        // Check if the user is authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get the UID of the current user
            Log.d("HomeActivity", "Logged in as: " + userId); // Log for debugging
            fetchUserData(userId); // Fetch user data using the UID
        } else {
            Log.e("HomeActivity", "User is not logged in");
            Toast.makeText(HomeActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchUserData(String userId) {
        // Log the userId to make sure it is correct
        Log.d("HomeActivity", "Fetching data for userId: " + userId);

        // Fetch user data from the Realtime Database
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                    if (userDetails != null) {
                        usernameTextView.setText(userDetails.getUsername());
                        emailTextView.setText(userDetails.getEmail());
                        nimTextView.setText(userDetails.getNim());
                    } else {
                        Log.e("HomeActivity", "UserDetails is null");
                        Toast.makeText(HomeActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("HomeActivity", "User not found in the database");
                    Toast.makeText(HomeActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("HomeActivity", "Failed to load data: " + databaseError.getMessage());
                Toast.makeText(HomeActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
