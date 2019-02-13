package com.google.group.h2botapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.group.h2botapplication.customer.CustomerMainActivity;
import com.google.group.h2botapplication.deliveryman.DeliveryManDocumentActivity;
import com.google.group.h2botapplication.deliveryman.DeliveryManMainActivity;
import com.google.group.h2botapplication.tpa.TPAffiliateMainActivity;
import com.google.group.h2botapplication.waterdealer.WaterPeddlerDocumentActivity;
import com.google.group.h2botapplication.waterdealer.WaterPeddlerHomeActivity;
import com.google.group.h2botapplication.waterstation.MerchantAccessVerification;
import com.google.group.h2botapplication.waterstation.WaterStationDocumentVersion2Activity;
import com.google.group.h2botapplication.waterstation.WaterStationMainActivity;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView register;
    EditText emailAddress, passwordType;
    Button loginNow;
    String RegisteredUserID;
    ProgressDialog progressDialog;
    String documentVerify;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
 //   private FirebaseDatabase databaseConnection;
 //   private DatabaseReference refConnection;
 //   FirebaseUser currentUser;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        databaseConnection = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        emailAddress = findViewById(R.id.usernameEditText);
        passwordType = findViewById(R.id.passwordEditText);
        register = findViewById(R.id.registerAccount);
        loginNow = findViewById(R.id.logInBtn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    if(!(LoginActivity.this).isFinishing())
                    {
                        progressDialog.show();
                    }
                    userTypeLogin();
                }
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, zCreateAccountOptionUserTypeActivity.class);
                startActivity(intent);
            }
        });
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInNow();
//                tempLogin();
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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signInNow()
    {
        String email = emailAddress.getText().toString();
        String password = passwordType.getText().toString();

        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "Please check your email address or password.", Toast.LENGTH_SHORT).show();
            //("Please check your email address or password.");
        }
        else
        {
            if(!(LoginActivity.this).isFinishing())
            {
                progressDialog.show();
            }
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    finish();
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(LoginActivity.this, "Please check your internet connection or credentials.", Toast.LENGTH_SHORT).show();
                      //  showMessages("Please check your internet connection or credentials.");
                    }
                    else {
                            userTypeLogin();
                        }
                    }
            });
        }
    }

    private void userTypeLogin()
    {
        FirebaseUser userHERE = FirebaseAuth.getInstance().getCurrentUser();
        RegisteredUserID = userHERE.getUid();
        //db.collection("User_File").document(RegisteredUserID);
            db.collection("User_File").whereEqualTo("user_getUID", RegisteredUserID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    documentVerify = (String) document.get("user_status");
                                    String userType = (String) document.get("user_type");
                                    if (userType.equals("Customer")) {
                                        Toast.makeText(LoginActivity.this, RegisteredUserID + " => " + userType, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
                                        finish();
                                    }
                                    else if(userType.equals("Water Station")){

                    if(documentVerify.equals("inactive"))
                    {
                        startActivity(new Intent(LoginActivity.this, WaterStationDocumentVersion2Activity.class));
                        finish();
                    }
                    else if(documentVerify.equals("unconfirmed"))
                    {
                        startActivity(new Intent(LoginActivity.this, MerchantAccessVerification.class));
                    }
                    else if(documentVerify.equals("active"))
                    {
                        startActivity(new Intent(LoginActivity.this, WaterStationMainActivity.class));
                        finish();
                    }
                                        Toast.makeText(LoginActivity.this, "Successfully logged-in as Station Owner", Toast.LENGTH_SHORT).show();
                   // showMessages("Successfully logged-in as Station Owner");
                }
                else if(userType.equals("Delivery Man"))
                {
                    if(documentVerify.equals("inactive"))
                    {
                        finish();
                        startActivity(new Intent(LoginActivity.this, DeliveryManDocumentActivity.class));
                    }
                    else if(documentVerify.equals("active"))
                    {
                        finish();
                        startActivity(new Intent(LoginActivity.this, DeliveryManMainActivity.class));
                    }
                    Toast.makeText(LoginActivity.this, "Successfully logged-in as Delivery Man", Toast.LENGTH_SHORT).show();
                    //showMessages("Successfully logged-in as Delivery Man");
                }
                else if(userType.equals("Water Dealer"))
                {
                    if(documentVerify.equals("inactive"))
                    {
                        startActivity(new Intent(LoginActivity.this, WaterPeddlerDocumentActivity.class));
                        Toast.makeText(LoginActivity.this, "Water Dealer Not Verified", Toast.LENGTH_SHORT).show();
                    //   showMessages("Water Dealer Not Verified");
                    }
                    else if(documentVerify.equals("active"))
                    {
                        startActivity(new Intent(LoginActivity.this, WaterPeddlerHomeActivity.class));
                        Toast.makeText(LoginActivity.this, "Successfully logged-in as Water Dealer", Toast.LENGTH_SHORT).show();
                    }
                    //showMessages("Successfully logged-in as Water Dealer");
                }
                else if(userType.equals("Third Party Affiliate"))
                {
                    if(documentVerify.equals("inactive"))
                    {
                        Toast.makeText(LoginActivity.this, "Your registration is still on process. Please wait for the confirmation that will be sent through SMS.", Toast.LENGTH_SHORT).show();
                        //showMessages("Your registration is still on process. Please wait for the confirmation that will be sent through SMS.");
                    }
                    else if(documentVerify.equals("active"))
                    {
                        startActivity(new Intent(LoginActivity.this, TPAffiliateMainActivity.class));
                        Toast.makeText(LoginActivity.this, "Successfully logged-in as Third Party Affiliate", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, " => ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Toast.makeText(LoginActivity.this, "DocumentSnapshot data: "+document.getData(), Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(LoginActivity.this, "wala", Toast.LENGTH_SHORT).show();
//                    }
//            }
//        });
//        db.collection("User_File").document(RegisteredUserID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void (DocumentSnapshot documentSnapshot) {
//                String userType = (String) documentSnapshot.get("user_type");
//                if(userType.equals("Customer"))
//                {
//                    startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
//                    finish();
//                }
//                else
//                {
//                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        })
//        ;
//        refConnection = FirebaseDatabase.getInstance().getReference().child("User_File").child(RegisteredUserID);
//        refConnection.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String userType = dataSnapshot.child("user_type").getValue().toString();
//                if (userType.equals("Customer")) {
//                    //Temporary Output
//                    startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
////                    showMessages("Successfully logged-in as Customer");
//                    finish();
//                }
//                else if(userType.equals("Water Station")){
//                    String documentVerify = dataSnapshot.child("user_status").getValue().toString();
//                    if(documentVerify.equals("inactive"))
//                    {
//                        startActivity(new Intent(LoginActivity.this, WaterStationDocumentVersion2Activity.class));
//                        finish();
//                    }
//                    else if(documentVerify.equals("unconfirmed"))
//                    {
//                        startActivity(new Intent(LoginActivity.this, MerchantAccessVerification.class));
//                    }
//                    else if(documentVerify.equals("active"))
//                    {
//                        startActivity(new Intent(LoginActivity.this, WaterStationMainActivity.class));
//                        finish();
//                    }
//                    showMessages("Successfully logged-in as Station Owner");
//                }
//                else if(userType.equals("Delivery Man"))
//                {
//                    String documentVerify = dataSnapshot.child("user_status").getValue().toString();
//                    if(documentVerify.equals("inactive"))
//                    {
//                        finish();
//                        startActivity(new Intent(LoginActivity.this, DeliveryManDocumentActivity.class));
//                    }
//                    else if(documentVerify.equals("active"))
//                    {
//                        finish();
//                        startActivity(new Intent(LoginActivity.this, DeliveryManMainActivity.class));
//                    }
//                    showMessages("Successfully logged-in as Delivery Man");
//                }
//                else if(userType.equals("Water Dealer"))
//                {
//                    String documentVerify = dataSnapshot.child("user_status").getValue().toString();
//                    if(documentVerify.equals("inactive"))
//                    {
//                        startActivity(new Intent(LoginActivity.this, WaterPeddlerDocumentActivity.class));
//                        showMessages("Water Dealer Not Verified");
//                    }
//                    else if(documentVerify.equals("active"))
//                    {
//                        startActivity(new Intent(LoginActivity.this, WaterPeddlerHomeActivity.class));
//                        showMessages("Water Dealer Verified");
//                    }
//                    showMessages("Successfully logged-in as Water Dealer");
//                }
//                else if(userType.equals("Third Party Affiliate"))
//                {
//                    String documentVerify = dataSnapshot.child("user_status").getValue().toString();
//                    if(documentVerify.equals("inactive"))
//                    {
//                        showMessages("Your registration is still on process. Please wait for the confirmation that will be sent through SMS.");
//                    }
//                    else if(documentVerify.equals("active"))
//                    {
//                        startActivity(new Intent(LoginActivity.this, TPAffiliateMainActivity.class));
//                        showMessages("Successfully logged-in as Third Party Affiliate");
//                        finish();
//                    }
//                }
//                else
//                {
//                    showMessages("Failed to Login");
//                    return;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//
//    private void showMessages(String s)
//    {
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
//    }

//    public void tempLogin()
//    {
//        String email = emailAddress.getText().toString().trim();
//        if(email.equals("customer"))
//        {
//            startActivity(new Intent(LoginActivity.this, CustomerMainActivity.class));
//        }
//        else if(email.equals("dealer"))
//        {
//            startActivity(new Intent(LoginActivity.this, WaterPeddlerHomeActivity.class));
//        }
//        else if(email.equals("station"))
//        {
//            startActivity(new Intent(LoginActivity.this, WaterStationMainActivity.class));
//        }
//        else if(email.equals("delivery"))
//        {
//            startActivity(new Intent(LoginActivity.this, DeliveryManMainActivity.class));
//        }
//        else if(email.equals("tpa"))
//        {
//            startActivity(new Intent(LoginActivity.this, TPAffiliateMainActivity.class));
//        }
//        else
//        {
//            showMessages("error");
//        }
//    }
}
    }