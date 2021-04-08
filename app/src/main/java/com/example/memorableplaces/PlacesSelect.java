package com.example.memorableplaces;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.os.Parcelable;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.*;
import java.util.*;
public class PlacesSelect extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Intent intent;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                currLocation(lastLocation,"Your position");
            }
    }

    public void currLocation(Location location, String title){
        if(location!=null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,12));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_select);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
         intent=getIntent();
         if(intent.getIntExtra("placeNumber",0)==0)
         {
             locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
             locationListener=new LocationListener() {
                 @Override
                 public void onLocationChanged(@NonNull Location location) {
                     currLocation(location,"Current Position");
                 }
             };
             mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                 @Override
                 public void onMapLongClick(LatLng latLng) {
                     MarkerOptions markerOptions = new MarkerOptions();
                     markerOptions.position(latLng);
                     mMap.addMarker(markerOptions);
                     Toast.makeText(PlacesSelect.this, "Location Saved", Toast.LENGTH_SHORT).show();
                     Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
                     try {
                         List<Address> list=geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                         String address="";
                         if(list.get(0).getThoroughfare()!=null) {
                             if (list.get(0).getSubThoroughfare() != null)
                                 address += list.get(0).getSubThoroughfare();
                             address += list.get(0).getThoroughfare();
                         }
                         else {
                             Date date = new Date();
                             address+=date.toString();
                             address+=" ";
                             long millis=System.currentTimeMillis();
                             address+=String.valueOf(millis);
                         }
                         MainActivity.listOfPlaces.add(address);
                         MainActivity.locations.add(latLng);
                         MainActivity.arrayAdapter.notifyDataSetChanged();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             });
             if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                 ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
             else {
                 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                 Location lastLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                 currLocation(lastLocation,"Your position");
             }
         }
         else
         {
             Location selectedLocation=new Location(LocationManager.GPS_PROVIDER);
             selectedLocation.setLatitude(MainActivity.locations.get(intent.getIntExtra("placeNumber",0)-1).latitude);
             selectedLocation.setLongitude(MainActivity.locations.get(intent.getIntExtra("placeNumber",0)-1).longitude);
             currLocation(selectedLocation,"favourite position");
         }

    }
}