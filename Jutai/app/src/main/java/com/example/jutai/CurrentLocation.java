package com.example.jutai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocation extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    String addressText;
    Button backbtn;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentLocationMarker;

    private Button getLocationButton;
    private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        getLocationButton = findViewById(R.id.get_location_button);
        locationText = findViewById(R.id.location_text);
        backbtn = findViewById(R.id.backbtn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
            }
        });

        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null && mMap != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                            // Get address using Geocoder
                            addressText = "Unknown location";
                            Geocoder geocoder = new Geocoder(CurrentLocation.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address address = addresses.get(0);
                                    addressText = address.getAddressLine(0); // Full address
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String latLngText = "Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude();
                            String fullInfo = addressText + "\n" + latLngText;

                            // Add or update marker
                            if (currentLocationMarker != null) {
                                currentLocationMarker.remove();
                            }

                            currentLocationMarker = mMap.addMarker(new MarkerOptions()
                                    .position(currentLatLng)
                                    .title("Your Location")
                                    .snippet(latLngText)); // Address is shown in TextView, coords in snippet

                            currentLocationMarker.showInfoWindow();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

                            // Update TextView
                            locationText.setText("📍 " + fullInfo);
                        } else {
                            Toast.makeText(CurrentLocation.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("location", addressText);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}