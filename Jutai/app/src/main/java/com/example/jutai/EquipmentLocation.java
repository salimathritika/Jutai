package com.example.jutai;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

public class EquipmentLocation extends FragmentActivity implements OnMapReadyCallback {

    DatabaseHelper myDB;
    private GoogleMap mMap;
    private EditText sourceLocation, destinationLocation;
    private Button drawRouteButton;
    TextView dispDist;

    private final String API_KEY = "Google_Map_API_Key"; // Replace with yours

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_location);

        myDB = new DatabaseHelper(this);
        drawRouteButton = findViewById(R.id.drawRouteButton);
        dispDist = findViewById(R.id.distDisp);

        int equipment_id = getIntent().getIntExtra("equipmentId",0);




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        drawRouteButton.setOnClickListener(v -> {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(EquipmentLocation.this);
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EquipmentLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                return;
            }

            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    String source = location.getLatitude() + "," + location.getLongitude();
                    String destination = myDB.getLocationByEquipmentId(equipment_id);

                    if (!source.isEmpty() && !destination.isEmpty()) {
                        getRouteFromApi(source, destination);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid locations", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Couldn't get current location", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void getRouteFromApi(String source, String destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + source.replace(" ", "+") +
                "&destination=" + destination.replace(" ", "+") +
                "&mode=driving" +
                "&key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(getApplicationContext(), "Failed to connect to API", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                try {
                    JSONObject json = new JSONObject(result);
                    if (!json.getString("status").equals("OK")) {
                        runOnUiThread(() ->
                                Toast.makeText(getApplicationContext(), "No route found", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    JSONObject route = json.getJSONArray("routes").getJSONObject(0);
                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    String encodedPolyline = overviewPolyline.getString("points");
                    List<LatLng> polylinePoints = decodePoly(encodedPolyline);

                    JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
                    String distance = leg.getJSONObject("distance").getString("text");
                    String duration = leg.getJSONObject("duration").getString("text");

                    LatLng startLatLng = new LatLng(
                            leg.getJSONObject("start_location").getDouble("lat"),
                            leg.getJSONObject("start_location").getDouble("lng"));

                    LatLng endLatLng = new LatLng(
                            leg.getJSONObject("end_location").getDouble("lat"),
                            leg.getJSONObject("end_location").getDouble("lng"));

                    runOnUiThread(() -> {
                        mMap.clear();

                        mMap.addMarker(new MarkerOptions().position(startLatLng).title("Current Source"));
                        mMap.addMarker(new MarkerOptions().position(endLatLng).title("Destination:"+destination));

                        PolylineOptions polylineOptions = new PolylineOptions()
                                .addAll(polylinePoints)
                                .width(10)
                                .color(0xFF1E88E5)
                                .geodesic(true);
                        mMap.addPolyline(polylineOptions);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng point : polylinePoints) {
                            builder.include(point);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 150));

                        dispDist.setText("Distance: "+distance);
                        Toast.makeText(getApplicationContext(),
                                "Distance: " + distance + "\nDuration: " + duration,
                                Toast.LENGTH_LONG).show();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() ->
                            Toast.makeText(getApplicationContext(), "Error parsing route data", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new LatLng(lat / 1E5, lng / 1E5));
        }
        return poly;
    }
}