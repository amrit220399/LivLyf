package com.apsinnovations.livlyf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.apsinnovations.livlyf.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText txtEmail, txtPass;
    Button btnLogin;
    TextView txtSignup;
    ProgressBar progressBar;
    boolean flag;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.LA_txtEmail);
        txtPass = findViewById(R.id.LA_txtPass);
        btnLogin = findViewById(R.id.LA_btnLogin);
        progressBar = findViewById(R.id.LA_progress);
        txtSignup = findViewById(R.id.LA_txtSignup);
        progressBar.setVisibility(View.GONE);
        user = new User();

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                checkUserDetails();
            }
        });

    }

    private void checkUserDetails() {
        user.setEmail(txtEmail.getText().toString().trim());
        user.setPassword(txtPass.getText().toString().trim());

        flag = true;
        if (!(Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches())) {
            flag = false;
            txtEmail.setText("");
            txtEmail.setFocusable(true);
            txtEmail.setError("Invalid Email");

        }
        if (user.getPassword().length() < 8) {
            flag = false;
            txtPass.setText("");
            txtPass.setFocusable(true);
            txtPass.setError("Invalid Password");
            Toast.makeText(getApplicationContext(), "Password Length should be greater than 8", Toast.LENGTH_SHORT).show();
        }

        if (flag) {
            progressBar.setVisibility(View.VISIBLE);
            loginUserInFirebase(user.getEmail(), user.getPassword());
        }
    }

    private void loginUserInFirebase(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidUserException) {
                    Toast.makeText(getApplicationContext(), "User Not Found! Please Register first..", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong !!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}