package com.example.grocery_consumer;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    String userId;
    SharedPreferences sharedPreferences;
    LatLng myLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSharedPreference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("customercoordinates").document(userId+"").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        GeoPoint geoPoint = document.getGeoPoint("position");
                        myLatLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                    }
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        firebaseFirestore.collection("coordinates").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    GeoPoint geoPoint = document.getGeoPoint("position");
                    LatLng lng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                    LatLng kerala = new LatLng(10.8505, 76.2711);
                    mMap.addMarker(new MarkerOptions().position(lng).title("Grocery Stores"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 10));
//                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
//                    CameraPosition cameraPosition = new CameraPosition.Builder()
//                            .target(kerala )      // Sets the center of the map to Mountain View
//                            .zoom(10)                   // Sets the zoom
//                            .bearing(90)                // Sets the orientation of the camera to east
//                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                            .build();                   // Creates a CameraPosition from the builder
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });
    }
    public void getSharedPreference(){
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        userId = sharedPreferences.getString("userid", "");
    }
}