package com.google.group.h2botapplication;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.group.h2botapplication.SetterAndGetterModels.*;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Button addPhoto, signUp;
    ImageView imageView;


    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri uri;
    Boolean clickable=false;

    EditText firstNameRegister, lastNameRegister, addressRegister, contactRegister, emailRegister, passwordRegister;
    ProgressDialog progressDialog;
    TextView headerTitle;
    FirebaseUser currentUser;
    String uidString;
    String userType;

    FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance();
        String s = getIntent().getStringExtra("TextValue");
        headerTitle = findViewById(R.id.headerRegister);
        headerTitle.setText(s);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        firstNameRegister = (EditText) findViewById(R.id.RegisterFullName);
        lastNameRegister = (EditText) findViewById(R.id.RegisterLastName);
        addressRegister = (EditText) findViewById(R.id.RegisterAddress);
        contactRegister = (EditText) findViewById(R.id.RegisterContact);
        emailRegister = (EditText) findViewById(R.id.RegisterEmailAddress);
        passwordRegister = (EditText) findViewById(R.id.RegisterPassword);


        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);


        imageView = findViewById(R.id.RegisterPhoto);
        addPhoto = findViewById(R.id.addPhotoRegister);
        signUp = findViewById(R.id.RegisterSignUp);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickable=true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(RegisterActivity.this).isFinishing()) {
                    progressDialog.show();
                }
                String firstNameString = firstNameRegister.getText().toString();
                String lastNameString = lastNameRegister.getText().toString();
                String addressString = addressRegister.getText().toString();
                String contactNoString = contactRegister.getText().toString();
                String emailAddressString = emailRegister.getText().toString();
                String passwordString = passwordRegister.getText().toString();

                if(passwordString.isEmpty() || firstNameString.isEmpty() || lastNameString.isEmpty() || addressString.isEmpty() || contactNoString.isEmpty() || emailAddressString.isEmpty() || imageView.getDrawable() == null)
                {
                    showMessage("Some fields are missing");
                    progressDialog.dismiss();
                }
                else
                {
                    CreateAccount(uri);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                uri = data.getData();

                if(clickable) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            Toast.makeText(RegisterActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }

    private void CreateAccount(final Uri uri)
    {
        mAuth.createUserWithEmailAndPassword(emailRegister.getText().toString(), passwordRegister.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        userType = "Customer";
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                         uidString = firebaseUser.getUid();

                        StorageReference mStorage = FirebaseStorage.getInstance().getReference("users_photos").child(uidString);
                        mStorage.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String stringUri = uri.toString();
                                        UserFile userFile;
                                        if(userType.equals("Customer"))
                                        {
                                            userFile = new UserFile(uidString,
                                                    stringUri,
                                                    firstNameRegister.getText().toString(),
                                                    lastNameRegister.getText().toString(),
                                                    addressRegister.getText().toString(),
                                                    contactRegister.getText().toString(),
                                                    userType,
                                                    "active",
                                                    emailRegister.getText().toString());
                                        }
                                        else
                                        {
                                            userFile = new UserFile(uidString,
                                                    stringUri,
                                                    firstNameRegister.getText().toString(),
                                                    lastNameRegister.getText().toString(),
                                                    addressRegister.getText().toString(),
                                                    contactRegister.getText().toString(),
                                                    userType,
                                                    "inactive",
                                                    emailRegister.getText().toString());
                                        }
                                        firebaseFirestore.collection("User_File").document(uidString).set(userFile)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        showMessage("Successfully Added");
                                                        progressDialog.dismiss();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        showMessage("Error to register");
                                                        progressDialog.dismiss();
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        showMessage("Please connect to Internet Connection");
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getApplication().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void passToNextActivity()
    {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
