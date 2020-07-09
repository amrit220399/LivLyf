package com.apsinnovations.livlyf.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    ImageView imgDP;
    TextInputEditText txtname, txtEmail, txtPass;
    Button btnUpdate, btnEdit;
    boolean flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imgDP = view.findViewById(R.id.ProF_imgDP);
        txtname = view.findViewById(R.id.ProF_etxtName);
        txtEmail = view.findViewById(R.id.ProF_etxtEmail);
        txtPass = view.findViewById(R.id.ProF_etxtPass);
        btnUpdate = view.findViewById(R.id.btnUpdateProfile);
        btnEdit = view.findViewById(R.id.btn_EditProfile);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        fetchProfile();
        fetchDP();
        return view;
    }

    private void editProfile() {
        btnEdit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
        txtname.setEnabled(true);
        txtPass.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPass.setTransformationMethod(null);
    }

    private void updateProfile() {
        btnUpdate.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
        txtname.setEnabled(false);
        txtPass.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        User user = new User();
        user.setName(txtname.getText().toString().trim());
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
            Toast.makeText(getActivity(), "Password Length should be greater than 8", Toast.LENGTH_SHORT).show();

        }
        if (user.getName().isEmpty()) {
            flag = false;
            txtname.setText("");
            txtname.setFocusable(true);
            txtname.setError("Invalid Name");
        }

        if (flag) {
            updateUser(user);
        }
    }

    private void updateUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Profile Updated Successfully",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProfile() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                txtname.setText(user.getName());
                txtEmail.setText(user.getEmail());
                txtPass.setText(user.getPassword());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void fetchDP() {
        Uri uri = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();
        Picasso.get().load(uri).fit().into(imgDP);
    }
}