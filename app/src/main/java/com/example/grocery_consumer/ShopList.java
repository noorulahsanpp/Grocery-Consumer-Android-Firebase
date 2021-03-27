package com.example.grocery_consumer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ShopList extends ActionBarActivity {
    static ShopList activityA;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    private ShopAdapter adapter;
    public static FirebaseFirestore firebaseFirestore;
    public static CollectionReference collectionReference;
    private FirebaseAuth mAuth;
    private ArrayList<Integer> cImages = new ArrayList<>();
    private ArrayList<String> cNames = new ArrayList<>();
    RecyclerView recyclerView;
    String userID;
    private Boolean exit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(ShopList.this, UserLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        activityA = this;
        userID = mAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        collectionReference = firebaseFirestore.collection("stores");
        recyclerView = findViewById(R.id.recyclerview1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCategory();
        getproducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBarActivity.getquantity();
        ShopAdapter.shopid.clear();
    }
    public static ShopList getInstance(){
        return   activityA;
    }

 private void getproducts() {

        Query query = collectionReference.orderBy("storename", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Shops> options = new FirestoreRecyclerOptions.Builder<Shops>()
                .setQuery(query, Shops.class)
                .build();

       adapter = new ShopAdapter(options);
       recyclerView.setAdapter(adapter);
    }

    private void getCategory(){

            cNames.add("General Store");
            cNames.add("Vegetables and Fruits");
            cNames.add("Electronics and Home Appliances");
            cNames.add("Groceries");
            cNames.add("Fish and Meat");
            cNames.add("Bakery");
            cNames.add("Restaurent");

        cImages.add(R.drawable.ic_action_store);
        cImages.add(R.drawable.ic_action_store);
        cImages.add(R.drawable.ic_action_electronics);
        cImages.add(R.drawable.ic_action_grocery);
        cImages.add(R.drawable.ic_action_store);
        cImages.add(R.drawable.ic_action_bakery);
        cImages.add(R.drawable.ic_action_restaurent);

        RecyclerView recyclerView1 = findViewById(R.id.category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(cNames,cImages);
        recyclerView1.setAdapter(categoryAdapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }

}