package com.example.grocery_consumer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProductList extends ActionBarActivity {

    private RecyclerView recyclerView;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    private ProductAdapter adapter;
    public static FirebaseFirestore firebaseFirestore;
    public static  CollectionReference collectionReference;
    public String storeid,userId;
    static Button addtocartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        initwidgets();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        getSharedPreference();
        storeid = intent.getExtras().get("storeid").toString();
        collectionReference = firebaseFirestore.collection("stores").document(storeid).collection("products");
        getproducts();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();       // to set the id of store in cart on each loads
        ProductAdapter.prdtimageurl.clear();
    }


    public void initwidgets(){
    recyclerView = findViewById(R.id.recyclerview);
    addtocartBtn = findViewById(R.id.addtocart);
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    recyclerView.setHasFixedSize(true);
}



    private void getproducts() {
        Query query =collectionReference.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();
         adapter = new ProductAdapter(options,storeid,userId);
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
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");

    }

}
