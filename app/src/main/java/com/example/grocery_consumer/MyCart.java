package com.example.grocery_consumer;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCart extends AppCompatActivity implements PaymentResultListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences;
    FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private RecyclerView recyclerView;
    RelativeLayout itemlayout;
    private ImageView shopimageIv;
    public static TextView shopnameTv,detailsTv,cartvalueTv,totalTv,methodTv,discountTv,cartemptyTv,oldcartTv;
    static Date date = setDate();
    private String shname,description,orderid,image;;
    private Button placeorderBTn;
    static String storeid ="",userId,username,phone;
    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> itemno = new ArrayList<>();
    private TextView gtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        getSupportActionBar().hide();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getSharedPreference();
        recyclerView = findViewById(R.id.cartview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        initwidgets();
        init();
        cartemptyTv.setVisibility(View.INVISIBLE);
        itemlayout.setVisibility(View.INVISIBLE);
        oldcartTv.setVisibility(View.INVISIBLE);
        getCartProducts();


    }

    public void init(){
        oldcartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PreviousOrders.class));
            }
        });
            placeorderBTn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startPayment();
                }

            });

        }
    public void startPayment() {
        float grandtotal = 0;
        grandtotal = Float.parseFloat(totalTv.getText().toString())*100;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_NoNpIryvAqtM1p");
        /**
         * Instantiate Checkout
         */


        /**
         * Set your logo here
         */
//        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Grocery");
            options.put("description", "Customer : "+userId );
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", grandtotal+"");//pass amount in currency subunits
            options.put("prefill.email", "samplemail@sample.com");
            options.put("prefill.contact","9988774455");
            checkout.open(activity, options);
        } catch(Exception e) {
//            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


        public void setOldcart(){
            collectionReference = firebaseFirestore.collection("customers").document(userId).collection("oldcart");
            Map<String, Object> products = new HashMap<>();
            products.put("name", name);
            products.put("itemno", itemno);
            products.put("orderid",orderid);
            products.put("date", date);
            products.put("image", images);
            products.put("storename",shname);
            products.put("description",description);
            products.put("price", prices);
            products.put("status", "order placed");
            collectionReference.document().set(products);
        }

    public void setPlaceorder(){
        collectionReference = firebaseFirestore.collection("stores").document(storeid).collection("order");
        Map<String, Object> products = new HashMap<>();
        products.put("name", name);
        products.put("itemno", itemno);
        products.put("orderid",orderid);
        products.put("date", date);
        products.put("image", images);
        products.put("customername",username);
        products.put("phone",phone);
        products.put("price", prices);
        products.put("status", "order placed");
        collectionReference.document().set(products);
    }

    public static Date setDate(){
        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        Date today = start.getTime();
        return today;
    }

    private void initwidgets(){
        shopnameTv = findViewById(R.id.shopname);
        detailsTv = findViewById(R.id.details);
        cartvalueTv = findViewById(R.id.cartvalue);
        discountTv = findViewById(R.id.discount);
        totalTv = findViewById(R.id.total);
        methodTv = findViewById(R.id.payment);
        shopimageIv = findViewById(R.id.shopimage);
        placeorderBTn = findViewById(R.id.placeorder);
        itemlayout = findViewById(R.id.itemlayout);
        cartemptyTv = findViewById(R.id.cartempty);
        oldcartTv = findViewById(R.id.oldcart);
    }

    private void getCartProducts(){

        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
        collectionReference.document("cart").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        orderid = document.getId();
                        name = (ArrayList<String>) document.get("name");
                        storeid = document.get("storeid").toString();   //first time to fetch when no values in cart
                        itemno = (ArrayList<String>) document.get("itemno");
                        prices = (ArrayList<String>) document.get("price");
                        images = (ArrayList<String>) document.get("image");
                        itemlayout.setVisibility(View.VISIBLE);
                        cartemptyTv.setVisibility(View.INVISIBLE);
                        setStorename(storeid);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cart Empty.", Toast.LENGTH_LONG).show();
                        cartemptyTv.setVisibility(View.VISIBLE);
                        oldcartTv.setVisibility(View.VISIBLE);
                    }
                    CartAdapter adapter = new CartAdapter(userId,storeid,itemno,name, prices, images);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Cart Empty.", Toast.LENGTH_LONG).show();
                    cartemptyTv.setVisibility(View.VISIBLE);
                    oldcartTv.setVisibility(View.VISIBLE);
                }
                }


        });
           }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setStorename(String storeid){

        firebaseFirestore.collection("stores").document(storeid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document =task.getResult();
                    shname = (String) document.get("storename");
                    description = document.get("category").toString()+("\n")+document.get("location").toString()+("\n")+document.get("phone").toString();
                    image = document.get("storeimage").toString();
                    Picasso.get().load(image).into(shopimageIv);
                    shopnameTv.setText(shname);
                    detailsTv.setText(description);

                }
            }
        });
    }

    public void getSharedPreference(){

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
        storeid = sharedPreferences.getString("storeid","");
        username = sharedPreferences.getString("username", "");
        phone = sharedPreferences.getString("phone", "");
    }

    @Override
    public void onPaymentSuccess(String s) {

        setOldcart();
        setPlaceorder();
        collectionReference = firebaseFirestore.collection("customers").document(userId).collection("cart");
        collectionReference.document("cart")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        ProductAdapter.prdtname.clear();
        ProductAdapter.prdtnum.clear();
        ProductAdapter.prdtimages.clear();
        ProductAdapter.prdtprices.clear();
        ProductAdapter.flag1=0;
        startActivity(new Intent(getApplicationContext(), OrderPlaced.class));
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
    }
}