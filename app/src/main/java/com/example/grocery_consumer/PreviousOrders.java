package com.example.grocery_consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PreviousOrders extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    String userId,username,phone;

    SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);
        getSharedPreference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("oldcart");
        RecyclerView rvItem = findViewById(R.id.rv_item);



        Query query =collectionReference.orderBy("date", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        itemAdapter = new ItemAdapter(options,userId);
        rvItem.setLayoutManager(layoutManager);

        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(layoutManager);

    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
        username = sharedPreferences.getString("username", "");
        phone = sharedPreferences.getString("phone", "");
    }
    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }
}
