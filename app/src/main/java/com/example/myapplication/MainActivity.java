package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextInputEditText emailUser, passwordUser;
    CheckBox checkBoxes;
    Button btLogin;
    TextView forgotPass, signUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailUser = findViewById(R.id.email);
        passwordUser = findViewById(R.id.password);
        checkBoxes = findViewById(R.id.checkboxes);
        btLogin = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUp);

        btLogin.setOnClickListener(loginView -> {
            String email = String.valueOf(emailUser.getText()).trim();
            String password = String.valueOf(passwordUser.getText()).trim();

            // Basic validation
            if (email.isEmpty()) {
                emailUser.setError("Email is required");
                return;
            }

            if (password.isEmpty()) {
                passwordUser.setError("Password is required");
                return;
            }

            // Firebase authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Login Gagal: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Handle sign up text click - using a different parameter name
        signUp.setOnClickListener(signUpView -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
        });

        // You can also add forgot password functionality
        forgotPass.setOnClickListener(forgotView -> {
            // Implement forgot password logic here
        });


    }
}