package com.example.mapbasics;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Button btnOpenMap;
    SupportMapFragment mapFragment;

    private GoogleMap mMap;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private PermissionManager permissionManager;
    private LocationManager locationManager;

    int MAP_TYPE_NONE = GoogleMap.MAP_TYPE_NONE;
    int MAP_TYPE_NORMAL = GoogleMap.MAP_TYPE_NORMAL;
    int MAP_TYPE_SATELLITE = GoogleMap.MAP_TYPE_SATELLITE;
    int MAP_TYPE_HYBRID = GoogleMap.MAP_TYPE_HYBRID;
    int MAP_TYPE_TERRAIN = GoogleMap.MAP_TYPE_TERRAIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        btnOpenMap = findViewById(R.id.btnOpenMap);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        permissionManager = PermissionManager.getInstance(this);
        locationManager = LocationManager.getInstance(this);

        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!permissionManager.checkPermissions(permissions)) {
                    permissionManager.askPermissions(MapsActivity.this, permissions, 100);
                } else {
                    if (locationManager.isLocationEnabled()) {
                        Log.d("TAG", "1");
                        mapFragment.getMapAsync(MapsActivity.this);
                    } else {
                        Log.d("TAG", "2");
                        locationManager.createLocationRequest();
                    }
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setupMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissionManager.handlePermissionResult(MapsActivity.this, 100,
                permissions,
                grantResults)) {

            if (locationManager.isLocationEnabled()) {
                mapFragment.getMapAsync(MapsActivity.this);
            } else {
                locationManager.createLocationRequest();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setupMap() {
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setMapType(MAP_TYPE_HYBRID);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(false);
        mMap.setTrafficEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
}