package com.example.disasteye;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.disasteye.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //R.id.idSearchView is found in the activity_maps.xml  -- Searches given location
        //More on searchView doc: https://abhiandroid.com/ui/searchview#SearchView_Methods_In_Android
        searchView = (SearchView) findViewById(R.id.idSearchView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //OnQueryTextListener() -- call backs to changed made in query text: https://developer.android.com/reference/android/widget/SearchView.OnQueryTextListener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                String locationName = searchView.getQuery().toString();
                ArrayList<Address> addressList = new ArrayList<>();

                if (locationName.equals("") || locationName != null){
                    // Create geocoder obj -- takes address and finds location: https://developer.android.com/reference/android/location/Geocoder
                    Geocoder geocoder = new Geocoder(MapsActivity.this);

                    //Given locationName, it gecodes the location on map, and adds to addressList
                    try { addressList.add((Address) geocoder.getFromLocationName(locationName, 1)); }
                    catch(Exception except){ except.printStackTrace(); }

                    //Get location, from the first position listed in addressList:
                    Address address = null;
                    try{ address = (Address) addressList.get(0); }
                    catch(IndexOutOfBoundsException exception){ exception.printStackTrace();}
                    // Add location's cordinates:
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // Add marker to pos.
                    mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));

                    // Animate camera to pos.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                //Returns false to let search view perform default action:
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}