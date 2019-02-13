package com.google.group.h2botapplication.waterstation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.group.h2botapplication.LoginActivity;
import com.google.group.h2botapplication.R;

import javax.annotation.Nullable;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

public class MerchantAccessVerification extends AppCompatActivity implements View.OnClickListener {


    Button buttonUpdate, buttonLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_access_verification);

        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("User_File");

        buttonLogout = findViewById(R.id.logoutAV);
        buttonUpdate = findViewById(R.id.updateAV);

        buttonUpdate.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);
    }

    public void checkUserType() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DocumentReference referenceGET = collectionReference.document(firebaseUser.getUid());
        referenceGET.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String userType = documentSnapshot.get("user_type").toString();
                if (userType.equals("Water Station")) {
                    String activateStat = documentSnapshot.get("user_status").toString();
                    if (activateStat.equals("active")) {
                        finish();
                        startActivity(new Intent(MerchantAccessVerification.this, WaterStationMainActivity.class));
                    } else if (activateStat.equals("unconfirmed")) {
                        startActivity(new Intent(MerchantAccessVerification.this, WaterStationDocumentVersion2Activity.class));
                    } else {
                        showMessages("Error to pass intent");
                    }
                }
                //else if (userType.equals("Delivery Man")) {
//                    String activateStat = dataSnapshot.child("status").getValue().toString();
//                    if (activateStat.equals("active")) {
//                        finish();
//                        startActivity(new Intent(MerchantAccessVerification.this, DeliveryManMainActivity.class));
//                    } else if (activateStat.equals("unconfirmed")) {
//                        startActivity(new Intent(MerchantAccessVerification.this, DeliveryManDocumentActivity.class));
//                    } else {
//                        showMessages("Error to pass intent");
//                    }
//                } else if (userType.equals("Third Affiliate")) {
//                    showMessages("Third Affiliate");
//                } else if (userType.equals("Water Dealer")) {
//                    showMessages("Water Dealer");
//                } else {
//                    showMessages("No available user");
//                }
            }
        });
    }

    public void showMessages(String s)
    {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.logoutAV:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(MerchantAccessVerification.this, LoginActivity.class));
                break;
            case R.id.updateAV:
                checkUserType();
                //startActivity(new Intent(MerchantAccessVerification.this, WaterStationDocumentVersion2Activity.class));
                break;
        }
    }
    
}
