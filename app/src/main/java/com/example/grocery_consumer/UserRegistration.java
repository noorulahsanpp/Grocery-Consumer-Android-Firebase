package com.example.grocery_consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UserRegistration extends AppCompatActivity {


    private static final String TAG = "UserRegistration";
    private static final int REQUEST_LOCATION = 1;

    private ImageView photo;
    private EditText phoneET, nameET, locationET;
    private Button signUpBtn, currentLocationBTN;
    private ProgressDialog progressDialog;
    private TextView register;
    private String uName = "", uLocation = "", saveCurrentDate, saveCurrentTime, downloadImageUrl;
    private String phoneNo, userId;


    private Uri imageUri;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageRef;
    private FirebaseUser user;

    private String latitudeLOC, longitudeLOC;
    private LocationManager locationManager;

    private boolean gps_Enabled = false;
    private boolean network_Enabled = false;

    private FusedLocationProviderClient fusedLocationProviderClient;

    Geocoder geocoder;
    List<Address> getAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_registration);

        progressDialog = new ProgressDialog(this);
        initWidgets();

        phoneNo = getIntent().getStringExtra("phoneNo");
        phoneET.setText(phoneNo);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        userId = mAuth.getUid();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Registering ");
                progressDialog.setMessage("Please wait ");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                getData();
                if (validate(uName,uLocation)) {
                    saveData();
                }

            }
        });
        currentLocationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(UserRegistration.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(UserRegistration.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(UserRegistration.this,
                            Locale.getDefault());

                    try {
                        latitudeLOC = ""+location.getLatitude();
                        longitudeLOC = ""+location.getLongitude();
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        locationET.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e);
            }
        });
    }




    public void saveData() {
        try {
            DocumentReference customer = firebaseFirestore.collection("customers").document(userId + "");
            DocumentReference location = firebaseFirestore.collection("customercoordinates").document(userId+"");
            Map<String, Object> customertinfo = new HashMap<>();
            Map<String, Object> locationinfo = new HashMap<>();
            customertinfo.put("userid", userId);
            customertinfo.put("location", uLocation);
            customertinfo.put("phone", phoneNo);
            customertinfo.put("username", uName);
            customer.set(customertinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//                    startActivity(new Intent(UserRegistration.this, home.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserRegistration.this, "Something went wrong. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            });
            locationinfo.put("altitude", latitudeLOC);
            locationinfo.put("longitude", longitudeLOC);
            location.set(locationinfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(UserRegistration.this, UserLogin.class));
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "addProduct: " + e);
        }
    }

    public Boolean validate(String name, String location) {
        if (TextUtils.isEmpty(name)) {
            nameET.setError("Input name");
            nameET.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(location)) {
            locationET.setError("Input Location");
            locationET.requestFocus();
            return false;
        }
        return true;
    }

    private void initWidgets() {
        Log.d(TAG, "initWidgets: Initialising widgets");
        signUpBtn = findViewById(R.id.button4);
        nameET = findViewById(R.id.name);
        locationET = findViewById(R.id.location);
        phoneET = findViewById(R.id.phone);
        phoneET.setEnabled(false);
        currentLocationBTN = findViewById(R.id.button9);
    }

    public void getData() {
        uName = nameET.getText().toString().trim();
        uLocation = locationET.getText().toString().trim();



}


    }
