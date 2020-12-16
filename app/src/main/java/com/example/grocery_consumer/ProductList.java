package com.example.grocery_consumer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProductList extends MainActivity {
    private RecyclerView recyclerView;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    public static  ArrayList<String> itemno = new ArrayList<>();
    public static  ArrayList<String> itemname = new ArrayList<>();
    static ArrayList<String> images = new ArrayList<>();
    static ArrayList<String> prices = new ArrayList<>();
    private ProductAdapter adapter;
    public static FirebaseFirestore firebaseFirestore;
    public static  CollectionReference collectionReference;
public String storeid,userId,cartstoreid;
static Button addtocart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
initwidgets();

        ProductAdapter.imageurl.clear();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        getSharedPreference();

        storeid = intent.getExtras().get("storeid").toString();
        getproducts();

    }
public void initwidgets(){
    recyclerView = findViewById(R.id.recyclerview);
    addtocart = findViewById(R.id.addtocart);
    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    recyclerView.setHasFixedSize(true);
}

    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
        cartstoreid = sharedPreferences.getString("cartstoreid", "");

    }

    private void getproducts() {
                   collectionReference = firebaseFirestore.collection("stores").document(storeid).collection("products");
        Query query =collectionReference.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();
         adapter = new ProductAdapter(options,cartstoreid,storeid,userId);
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
