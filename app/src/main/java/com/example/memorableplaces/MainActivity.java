package com.example.memorableplaces;

import android.content.Intent;
import android.location.Address;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;

import java.util.*;

public class MainActivity extends AppCompatActivity {
    ListView places;
    static ArrayList<String> listOfPlaces;
    static ArrayList<LatLng> locations;
    static ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locations=new ArrayList<>();
          listOfPlaces=new ArrayList<>();
        listOfPlaces.add("Add the places");
        arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listOfPlaces);
        places=findViewById(R.id.places);
        places.setAdapter(arrayAdapter);
        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent=new Intent(getApplicationContext(),PlacesSelect.class);
               intent.putExtra("placeNumber",position);
               startActivity(intent);
            }
        });
    }
}