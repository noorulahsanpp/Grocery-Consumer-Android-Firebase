package com.example.grocery_consumer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PreviousOrders extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    String userId,username,phone;
    TextView noOrderTv;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore firebaseFirestore;
    public static CollectionReference collectionReference;
    private PreviousAdapter previousAdapter;
    RecyclerView rvItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);
        getSharedPreference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       rvItem = findViewById(R.id.rv_item);
        rvItem.setLayoutManager(layoutManager);
        noOrderTv = findViewById(R.id.noOrders);
        noOrderTv.setVisibility(View.INVISIBLE);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("oldcart");
        getPreviousOrder();
    }

    public void getPreviousOrder(){

       Query query =collectionReference.orderBy("date", Query.Direction.ASCENDING);
       query.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                noOrderTv.setVisibility(View.VISIBLE);
            }
        });
        FirestoreRecyclerOptions<PreviousItem> options = new FirestoreRecyclerOptions.Builder<PreviousItem>()
                .setQuery(query, PreviousItem.class)
                .build();
        previousAdapter = new PreviousAdapter(options);
        rvItem.setAdapter(previousAdapter);
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
        previousAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        previousAdapter.stopListening();
    }
}
