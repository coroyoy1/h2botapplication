package com.google.group.h2botapplication.waterstation;

import com.google.group.h2botapplication.*;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.group.h2botapplication.R;
import com.google.group.h2botapplication.setterandgettermodels.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

public class WaterStationDocumentVersion2Activity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView image1, image2, image3, image4, image5, image6;
    Button button1, button2, button3, button4, button5, button6, buttonlogout, submitToFirebase;
    EditText businessName, businessStartTime, businessEndTime, businessDeliveryFeePerGal, businessMinNoCapacity, businessTelNo;

    Boolean isClick1=false, isClick2=false, isClick3=false, isClick4=false, isClick5=false, isClick6=false;
    Intent intent;
    Uri uri1,uri2,uri3,uri4,uri5,uri6;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;

    private RadioGroup radioGroup;
    private RadioButton radioButton, radioButtonv, radioButtonvv;
    String radioCheck = "";
    String businessFreeOrNoText = "";
    String businessDeliveryService = "";

    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_station_document_version2);

        intent = new Intent();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        radioGroup = findViewById(R.id.radioAllSD);


        image1 = (ImageView)findViewById(R.id.permit1);
        image2 = (ImageView)findViewById(R.id.permit2);
        image3 = (ImageView)findViewById(R.id.permit3);
        image4 = (ImageView)findViewById(R.id.permit4);
        image5 = (ImageView)findViewById(R.id.permit5);
        image6 = (ImageView)findViewById(R.id.permit6);

        button1 = (Button)findViewById(R.id.permitButton1);
        button2 = (Button)findViewById(R.id.permitButton2);
        button3 = (Button)findViewById(R.id.permitButton3);
        button4 = (Button)findViewById(R.id.permitButton4);
        button5 = (Button)findViewById(R.id.permitButton5);
        button6 = (Button)findViewById(R.id.permitButton6);
        buttonlogout = (Button)findViewById(R.id.logoutButton);
        submitToFirebase = (Button)findViewById(R.id.submitButton);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        buttonlogout.setOnClickListener(this);
        submitToFirebase.setOnClickListener(this);


        //Edittext
        businessName = findViewById(R.id.stationNameSD);
        businessStartTime = findViewById(R.id.stationBusinesshoursSD);
        businessEndTime = findViewById(R.id.stationBusinesshoursSDEnd);
        businessDeliveryFeePerGal = findViewById(R.id.stationDeliverySD);
        businessMinNoCapacity = findViewById(R.id.stationCapacitySD);
        businessTelNo = findViewById(R.id.stationTelephoneySD);



        radioButton = findViewById(R.id.radioYes);
        radioButtonv = findViewById(R.id.radioNo);
        radioButtonvv = findViewById(R.id.radioFree);
        radioButton.setChecked(true);

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                businessDeliveryFeePerGal.setVisibility(View.VISIBLE);
                businessFreeOrNoText = "not";
                businessDeliveryService = "active";
            }
        });
        radioButtonv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessDeliveryFeePerGal.setVisibility(View.INVISIBLE);
                businessFreeOrNoText = "not";
                businessDeliveryService = "inactive";
            }
        });
        radioButtonvv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                businessFreeOrNoText = "free";
                businessDeliveryService = "active";
            }
        });
        if(radioButton.isChecked())
        {
            radioCheck = "true";
        }
        else if(radioButtonv.isChecked())
        {
            radioCheck = "false";
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                uri1 = data.getData();uri2 = data.getData();uri3 = data.getData();uri4 = data.getData();uri5 = data.getData();uri6 = data.getData();
                if(isClick1)
                {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri1);
                        image1.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(isClick2)
                {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri2);
                        image2.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(isClick3)
                    {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri3);
                        image3.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(isClick4)
                {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri4);
                        image4.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(isClick5)
                {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri5);
                        image5.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(isClick6)
                {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri6);
                        image6.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            Toast.makeText(WaterStationDocumentVersion2Activity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }



    private void uploadData(final String businessNameTextGET,
               final String businessStartTimeTextGET,
               final String businessEndTimeTextGET,
               final String businessDeliveryFeePerGalTextGet,
               final String businessMinNoCapacityTextGET,
               final String businessTelNoTextGET)
    {
        if(uri1 != null)
        {
            String currentUser = mAuth.getCurrentUser().getUid();
                StorageReference ref = storageReference.child("users_documents").child(currentUser).child(currentUser+"/"+"station_business_permit");
                ref.putFile(uri1)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> result1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String addOne = uri.toString();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("station_business_permit", addOne);
                                        firebaseFirestore.collection("User_WS_Docs_File").document(mAuth.getUid()).set(data);
                                    }
                                });
                            }
                        });
            }
        if(uri2 != null)
        {
            String currentUser = mAuth.getCurrentUser().getUid();
            StorageReference ref = storageReference.child("users_documents").child(currentUser+"/"+"station_sanitary_permit_picture");
            ref.putFile(uri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> result1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String addOne = uri.toString();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("station_sanitary_permit", addOne);
                                    firebaseFirestore.collection("User_WS_Docs_File").document(mAuth.getUid()).set(data);
                                }
                            });
                        }
                    });
        }
        if(uri3 != null)
        {
            String currentUser = mAuth.getCurrentUser().getUid();
            StorageReference ref = storageReference.child("users_documents").child(currentUser+"/"+"station_fire_protection_picture");
            ref.putFile(uri3)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> result1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String addOne = uri.toString();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("station_fire_protection", addOne);
                                    firebaseFirestore.collection("User_WS_Docs_File").document(mAuth.getUid()).set(data);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
        if(uri4 != null)
        {
            String currentUser = mAuth.getCurrentUser().getUid();
            StorageReference ref = storageReference.child("users_documents").child(currentUser+"/"+"station_real_property_taxes_picture");
            ref.putFile(uri4)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> result1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String addOne = uri.toString();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("station_real_property_taxes", addOne);
                                    firebaseFirestore.collection("User_WS_Docs_File").document(mAuth.getUid()).set(data);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
        if(uri5 != null)
        {
            String currentUser = mAuth.getCurrentUser().getUid();
            StorageReference ref = storageReference.child("users_documents").child(currentUser+"/"+"station_occupancy_permit_picture");
            ref.putFile(uri5)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> result1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String addOne = uri.toString();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("station_occupancy_permit", addOne);
                                    firebaseFirestore.collection("User_WS_Docs_File").document(mAuth.getUid()).set(data);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
        if(uri6 != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final String currentUser = mAuth.getCurrentUser().getUid();
            StorageReference ref = storageReference.child("users_documents").child(currentUser+"/"+"station_physico_chem_permit_picture");
            ref.putFile(uri6)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> result1 = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result1.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String addOne = uri.toString();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("station_physico_chem_permit", addOne);
                                    data.put("status", "inactive");
                                    firebaseFirestore.collection("User_WS_Docs_File").document(mAuth.getUid()).set(data);
                                }
                            });
                            UserWSBusinessInfoFile userWSBusinessInfoFile = new UserWSBusinessInfoFile(currentUser, businessNameTextGET, businessStartTimeTextGET, businessEndTimeTextGET, businessDeliveryService, businessFreeOrNoText, businessDeliveryFeePerGalTextGet, businessMinNoCapacityTextGET, businessTelNoTextGET, "active");
                            firebaseFirestore.collection("User_WS_Business_Info_File").document(currentUser).set(userWSBusinessInfoFile)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            showMessages("Successfully save");
                                            progressDialog.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showMessages("Error to save");
                                            progressDialog.dismiss();
                                        }
                                    });
                            progressDialog.dismiss();
                            Toast.makeText(WaterStationDocumentVersion2Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            //passToNextAct();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(WaterStationDocumentVersion2Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void showMessages(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public void openGallery()
    {
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.permitButton1:
                isClick1=true;isClick2=false;isClick3=false;isClick4=false;isClick5=false;isClick6=false;
                openGallery();
                break;
            case R.id.permitButton2:
                isClick2=true;isClick1=false;isClick3=false;isClick4=false;isClick5=false;isClick6=false;
                openGallery();
                break;
            case R.id.permitButton3:
                isClick3=true;isClick2=false;isClick1=false;isClick4=false;isClick5=false;isClick6=false;
                openGallery();
                break;
            case R.id.permitButton4:
                isClick4=true;isClick2=false;isClick3=false;isClick1=false;isClick5=false;isClick6=false;
                openGallery();
                break;
            case R.id.permitButton5:
                isClick5=true;isClick2=false;isClick3=false;isClick4=false;isClick1=false;isClick6=false;
                openGallery();
                break;
            case R.id.permitButton6:
                isClick6=true;isClick2=false;isClick3=false;isClick4=false;isClick5=false;isClick1=false;
                openGallery();
                break;
            case R.id.submitButton:
                checkingAddPhoto();
                stringData();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                finish();
                startActivity(new Intent(WaterStationDocumentVersion2Activity.this, LoginActivity.class));
                break;
            default:
                Toast.makeText(WaterStationDocumentVersion2Activity.this, "There is not such thing on app", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void passToNextAct()
    {
        Intent passIntent = new Intent(WaterStationDocumentVersion2Activity.this, MerchantAccessVerification.class);
        startActivity(passIntent);
    }
    public void stringData()
    {
        String businessNameText = businessName.getText().toString();
        String businessStartTimeText = businessStartTime.getText().toString();
        String businessEndTimeText = businessEndTime.getText().toString();
        String businessDeliveryFeePerGalText = businessDeliveryFeePerGal.getText().toString();
        String businessMinNoCapacityText = businessMinNoCapacity.getText().toString();
        String businessTelNoText = businessTelNo.getText().toString();

        uploadData(businessNameText,
                businessStartTimeText,
                businessEndTimeText,
                businessDeliveryFeePerGalText,
                businessMinNoCapacityText,
                businessTelNoText);
    }
    public void checkingAddPhoto()
    {
        if(image1.getDrawable() == null
        && image2.getDrawable() == null
        && image3.getDrawable() == null
        && image4.getDrawable() == null
        && image5.getDrawable() == null
        && image6.getDrawable() == null)
        {
            showMessages("All document should be attached!");
            return;
        }
    }
}
