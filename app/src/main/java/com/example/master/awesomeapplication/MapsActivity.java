package com.example.master.awesomeapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLngPosition;
    private ArrayList<User> users = new ArrayList<User>();
    private String myEmail;
    private String myPassword;
    FirebaseAuth auth;
    LatLng myposition;
    private int HARDCODED_DISTANCE = 1000;
    private static final int REQUEST_FINE_LOCATION = 101;
    private DatabaseReference databaseReference;
    // Creating a marker object
    Marker marker;
    List<Marker> markersList;
    MarkerOptions markerOptions = new MarkerOptions();
    String containKey;


    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    UserInformation userInfo;


    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;

    //
    //methods
    //
    //onCreate - override, gets the map
    //onMapReady - gets current location, calls retrieveUser
    //retrieveUsers - gets the list of users passed from the calling intent
    //vibrate - method used to vibrate when a user gets close - to be called from specific code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       myposition=new LatLng(0,0);

        startLocationUpdates();
        auth = FirebaseAuth.getInstance();
        //retrieve current user info
        myEmail = getIntent().getStringExtra("currentEmail");
        myPassword = getIntent().getStringExtra("currentPassword");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        markersList = new ArrayList<>();
        getUsers();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is whe6re we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);

            // Google API Zoom In and Out button
            final UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setCompassEnabled(true);
            uiSettings.setZoomControlsEnabled(true);
            //getUsers();
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_FINE_LOCATION);


    }



    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        // You can now create a LatLng Object for use with maps
        latLngPosition = new LatLng(location.getLatitude(), location.getLongitude());


        // removing the marker
//        if(marker!=null)
//        {
//            marker.remove(); // removing the marker
//            marker=null;
//        }

        //Getting position of user
//        markerOptions.position(latLngPosition);
//        markerOptions.title("My Location");
//
//        //adding marker to the map
//        marker =  mMap.addMarker(markerOptions);
//        markersList.add(marker);
        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();

         userInfo = new UserInformation(email, latLngPosition.latitude, latLngPosition.longitude);

        databaseReference.child(user.getUid()).setValue(userInfo);

    }

    //This function gets a list of users from the database
    private void getUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //allUsers.clear();
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserInformation userInfo = userSnapshot.getValue(UserInformation.class);
                    if (userInfo.getLongitude() == 0 || userInfo.getLatitude() == 0) {
                        return;
                    }
                    placeMarker(new LatLng(userInfo.getLatitude(), userInfo.getLongitude()), data.iterator().next().getKey(), userInfo.getName());

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public double calculateDistance(LatLng startP, LatLng endP) {

        double distance = 0;
        Location locationA = new Location("A");
        locationA.setLatitude(startP.latitude);
        locationA.setLongitude(startP.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(endP.latitude);
        locationB.setLongitude(endP.longitude);
        distance = locationA.distanceTo(locationB);
        return distance;
    }

    public void vibrate() {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 150 milliseconds
        v.vibrate(150);
    }

    // Pops out notification that someone is close to you
    public void showNotification(LatLng la) {
        if (calculateDistance(myposition, la) < 20 && calculateDistance(myposition, la) != 0) {
            Toast.makeText(this, "Someone is Close to you", Toast.LENGTH_LONG).show();
            vibrate();
        } else return;
    }

    //This method places markers on the map based on the database records
    public void placeMarker(LatLng ll, String key, String name) {
        Marker holder = null;

            for (Marker m : markersList) {

                if (m.getTag().toString().equals(key)) {

                    m.remove();
                    holder = m;
                    break;
                }
            }

        markersList.remove(holder);


//adding a marker
        MarkerOptions options;

             
              
        if(auth.getCurrentUser().getUid().equals(key)){
            myposition = ll;
                   options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(name);
        }
       else{
             options = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title(name);
             showNotification(ll);
        }

        marker = mMap.addMarker(options);


        marker.setTag(key);


        containKey = marker.getTitle();

        markersList.add(marker);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
