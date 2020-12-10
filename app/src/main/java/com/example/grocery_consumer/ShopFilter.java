package com.example.grocery_consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ShopFilter extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    private ShopAdapter adapter;
    String category;
    public static FirebaseFirestore firebaseFirestore;
    public static CollectionReference collectionReference;
    static   int flag = 0;
    private ArrayList<String> mNames = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_filter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();


        recyclerView = findViewById(R.id.filterview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.setHasFixedSize(true);
        category= intent.getExtras().get("category").toString();

getproducts();

    }
    private void getproducts() {
        collectionReference = firebaseFirestore.collection("stores");
        Query query = collectionReference.whereEqualTo("category",category);
        FirestoreRecyclerOptions<Stores> options = new FirestoreRecyclerOptions.Builder<Stores>()
                .setQuery(query, Stores.class)
                .build();

        adapter = new ShopAdapter(options);
        recyclerView.setAdapter(adapter);
//             setSharedPreferences();
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

}