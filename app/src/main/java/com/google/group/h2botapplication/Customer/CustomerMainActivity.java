package com.google.group.h2botapplication.Customer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.group.h2botapplication.LoginActivity;
import com.google.group.h2botapplication.R;

import java.util.Objects;

public class CustomerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FirebaseAuth mAuth;
    Dialog dialog;
   // public GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        dialog = new Dialog(this);
        drawerLayout = findViewById(R.id.customer_drawer);
        drawerLayout.closeDrawers();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle.syncState();
        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GoogleMapFragment()).commit();
//            navigationView.setCheckedItem(R.id.map);
//            Objects.requireNonNull(getSupportActionBar()).setTitle("Map");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
//            case R.id.map:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GoogleMapFragment()).commit();
//                Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
//                Objects.requireNonNull(getSupportActionBar()).setTitle("Map");
//                break;
//
//            case R.id.my_order:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
//                Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show();
//                Objects.requireNonNull(getSupportActionBar()).setTitle("My Orders");
//                break;
//
//            case R.id.account_settings:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccountSettingFragment()).commit();
//                Toast.makeText(this, "Account Settings", Toast.LENGTH_SHORT).show();
//                Objects.requireNonNull(getSupportActionBar()).setTitle("Account Settings");
//                break;
//
//            case R.id.rate:
////                final Dialog dialog = new Dialog(this);
////                dialog.setContentView(R.layout.ratings_popup);
////                dialog.show();
//                break;
//
//            case R.id.transactions:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerTransactionsFragment()).commit();
//                Toast.makeText(this, "Transactions", Toast.LENGTH_SHORT).show();
//                Objects.requireNonNull(getSupportActionBar()).setTitle("Transactions");
//                break;
        }
        if (menuItem.getItemId() == R.id.logout) {
            logout();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logout() {
        mAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Successfully logged-out", Toast.LENGTH_SHORT).show();
    }

//    public void ShowPopUpAccountSettingUpdate(View view) {
//        Button cancelBtn;
//        Button saveChangesBtn;
//        dialog.setContentView(R.layout.account_settings_popup);
//        cancelBtn = dialog.findViewById(R.id.saveButton);
//        saveChangesBtn = dialog.findViewById(R.id.saveChangesBtn);
//
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(CustomerMainActivity.this, "Temporary", Toast.LENGTH_SHORT).show();
//            }
//        });
//        dialog.show();
//    }
//
//    public void OrderInfoPopup(View view) {
//        dialog.setContentView(R.layout.order_info_popup);
//        dialog.show();
//    }
//
//    public void TransactionInfoPopup(View view) {
//        dialog.setContentView(R.layout.transaction_popup);
//        dialog.show();
//    }
}
