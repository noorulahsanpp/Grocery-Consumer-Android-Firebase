package com.example.grocery_consumer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ActionBarActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    String username, phone;
    static FirebaseFirestore firebaseFirestore;
    static  TextView textCartItemCount;
    static int mCartItemCount;
    static ArrayList quantity = new ArrayList();
    static String cartstoreid,userID;
    static CollectionReference collectionReference;
    public static DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Purchase");
        setContentView(R.layout.badgelayout);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(ActionBarActivity.this, UserLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount =actionView.findViewById(R.id.cart_badge);
        textCartItemCount.setVisibility(View.INVISIBLE);
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        getUserDetails();

        return true;
    }

    public void setSharedPreferences() {

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userID);
        editor.putString("username", username);
        editor.putString("phone", phone);
        editor.commit();
    }


    public void getUserDetails() {

        documentReference = firebaseFirestore.collection("customers").document(userID);
        documentReference.collection("cart").document("cart").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {                                                             //gets the storeid in cart
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        cartstoreid = (String) document.get("storeid");
                        setSharedPreferences();
                        ProductAdapter.cartstoreid = cartstoreid;
                    }
                    else{
                        ProductAdapter.cartstoreid = "";                      //set the cartstoreid in productadapter on each loads
                    }
                }
            }
        });
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    username = (String) document.get("username");
                    phone = (String) document.get("phone");
                    setSharedPreferences();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getquantity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, MyCart.class));
            return true;
        }
        else if(item.getItemId() == R.id.logout)
        {
            logout();
        }
        else if(item.getItemId() == R.id.location){
            startActivity(new Intent(this, MapsActivity.class));
        }
        else if(item.getItemId() == R.id.previous){
            startActivity(new Intent(this, PreviousOrders.class));
        }
        return super.onOptionsItemSelected(item);
    }


    public static void getquantity(){

        collectionReference= firebaseFirestore.collection("customers").document(userID).collection("cart");
        collectionReference.document("cart").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                quantity =(ArrayList<String>) document.get("itemno");
                }
            else{
                quantity.clear();
                mCartItemCount = 0;
            }}
                mCartItemCount = 0;
            for(int i = 0;i<quantity.size();i++){
                String n = (String) quantity.get(i);
                mCartItemCount = mCartItemCount + Integer.parseInt(n);
            }
            setupBadge();

                    }
        });
    }

    public static void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                    textCartItemCount.setVisibility(View.INVISIBLE);
            } else {
                textCartItemCount.setVisibility(View.VISIBLE);
                textCartItemCount.setText(String.valueOf(mCartItemCount));

            }
        }
    }

    private void logout(){

        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), UserLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        return;
    }


}