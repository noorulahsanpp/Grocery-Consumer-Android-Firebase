package com.example.grocery_consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShopFilter extends AppCompatActivity {

    private ShopAdapter adapter;
    public static FirebaseFirestore firebaseFirestore;
    public static CollectionReference collectionReference;
    RecyclerView recyclerView;
    String category;
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
        FirestoreRecyclerOptions<Shops> options = new FirestoreRecyclerOptions.Builder<Shops>()
                .setQuery(query, Shops.class)
                .build();
        adapter = new ShopAdapter(options);
        recyclerView.setAdapter(adapter);
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