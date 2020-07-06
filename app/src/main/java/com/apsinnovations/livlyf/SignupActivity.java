package com.apsinnovations.livlyf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.apsinnovations.livlyf.models.User;
import com.apsinnovations.livlyf.utils.PrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;


public class SignupActivity extends AppCompatActivity {
    ImageView imgDP;
    TextInputEditText txtName,txtEmail, txtPass;
    Button btnSignup;
    TextView txtLogin;
    ProgressBar progressBar;
    StorageReference storageReference;
    private final int TAKE_IMAGE_CODE = 101;
    private final int PICK_IMAGE_REQUEST = 102;
    private static final String TAG = "SignupActivity";
    boolean flag;
    boolean DPflag=false;
    User user;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.setFirstTimeLaunch(false);
        setContentView(R.layout.activity_signup);
        imgDP = findViewById(R.id.SA_imgDP);
        txtName = findViewById(R.id.SA_txtName);
        txtEmail = findViewById(R.id.SA_txtEmail);
        txtPass = findViewById(R.id.SA_txtPass);
        btnSignup = findViewById(R.id.SA_btnSignup);
        progressBar = findViewById(R.id.SA_progress);
        txtLogin = findViewById(R.id.SA_txtLogin);
        progressBar.setVisibility(View.GONE);
        user = new User();

        storageReference= FirebaseStorage.getInstance().getReference();

        imgDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createProfile() {
            user.setName(txtName.getText().toString().trim());
            user.setEmail(txtEmail.getText().toString().trim());
            user.setPassword(txtPass.getText().toString().trim());

            flag=true;

        if (!(Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches())){
            flag=false;
            txtEmail.setText("");
            txtEmail.setFocusable(true);
            txtEmail.setError("Invalid Email");

        }if(user.getPassword().length()<8){
            flag=false;
            txtPass.setText("");
            txtPass.setFocusable(true);
            txtPass.setError("Invalid Password");
            Toast.makeText(getApplicationContext(),"Password Length should be greater than 8",Toast.LENGTH_SHORT).show();

        }if(user.getName().isEmpty()){
            flag=false;
            txtName.setText("");
            txtName.setFocusable(true);
            txtName.setError("Invalid Name");
        }

        if(flag){
            progressBar.setVisibility(View.VISIBLE);
            registerUserInFirebase();
        }
    }

    private void registerUserInFirebase() {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if(authResult.getUser()!=null) {
                            firebaseUser=authResult.getUser();
                            saveUserInFirebase();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(getApplicationContext(),"User Already Registered! Please choose Login",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getApplicationContext(), "Something Went Wrong !!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveUserInFirebase() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users")
                .document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if(DPflag) {
                            BitmapDrawable bitmapDrawable= (BitmapDrawable) imgDP.getDrawable();
                            Bitmap bitmap=bitmapDrawable.getBitmap();
                            UploadtoStorage(bitmap);
                        }else{
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder picDialog = new AlertDialog.Builder(this);
        picDialog.setTitle("Select");
        String[] picDialogItems = {
                "Choose from Gallery",
                "Capture from Camera" };
        picDialog.setItems(picDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                switch(which){
                    case 0:
                        SelectfromGallery();
                        break;
                    case 1:
                        ClickfromCamera();
                        break;
                }
            }
        });
        picDialog.show();
    }

    private void ClickfromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }

    private void SelectfromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            if (resultCode == RESULT_OK && data!=null && data.getExtras()!=null) {
                    Uri filePath = data.getData();
                    Picasso.get().load(filePath).fit().centerInside().into(imgDP);
                    DPflag=true;
            }else{
                Toast.makeText(getApplicationContext(),"Please Try Again..",Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            Uri filePath = data.getData();
            Picasso.get().load(filePath).fit().centerInside().into(imgDP);
            DPflag=true;
        }
    }

    private void UploadtoStorage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        final StorageReference saveRef = storageReference.child("usersDP").child(firebaseUser.getUid() + ".jpeg");

        saveRef.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(saveRef);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e.getCause());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void getDownloadUrl(StorageReference storageReference) {
        storageReference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: " + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        assert user != null;
        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(SignupActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Profile Image Upload Error. Try Again...", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}