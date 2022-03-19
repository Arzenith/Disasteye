package com.example.disasteye;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.disasteye.databinding.ActivityMapsBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    SearchView searchView;



    private ConstraintLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout headerLayout;
    private ImageView swiper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        headerLayout = findViewById(R.id.header_layout);
        swiper = findViewById(R.id.swiper);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //Sets max/min to bottom sheet scroll.
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setPeekHeight(450);
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    if (bottomSheetBehavior.getPeekHeight() == 450) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehavior.setPeekHeight(80);
                        mMap.getUiSettings().setScrollGesturesEnabled(false);
                    } else if (bottomSheetBehavior.getPeekHeight() == 80) {
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                    }

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });



        //request will recieve a URL and gather data from the API!
        String link = "https://eonet.gsfc.nasa.gov/api/v3/events";
        HTTPRequest request = new HTTPRequest();
        request.execute(link);

        //R.id.idSearchView is found in the activity_maps.xml  -- Searches given location
        //More on searchView doc: https://abhiandroid.com/ui/searchview#SearchView_Methods_In_Android
        searchView = (SearchView) findViewById(R.id.idSearchView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        //OnQueryTextListener() -- call backs to changed made in query text: https://developer.android.com/reference/android/widget/SearchView.OnQueryTextListener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String locationName = searchView.getQuery().toString();
                List<Address> addresses = null;

                if (locationName.equals("") || locationName != null) {
                    // Create geocoder obj -- takes address and finds location: https://developer.android.com/reference/android/location/Geocoder
                    Geocoder geocoder = new Geocoder(MapsActivity.this);

                    //Given locationName, it will gecode the location on map, and adds to addressList
                    try {
                        addresses = geocoder.getFromLocationName(locationName, 1);
                    } catch (Exception except) {
                        except.printStackTrace();
                    }

                    //Get location, from the first position listed in addressList:
                    Address address = addresses.get(0);
                    Log.d("address", addresses.get(0) + " and " + address);

                    // Add location's coordinates:
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    mMap.clear();
                    // Add marker to pos.
                    mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
                    // Move to pos.
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
                //Returns false to let search view perform default action:
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        final MaterialToolbar TopAppBar = (MaterialToolbar) findViewById(R.id.topAppBar);
        //void setSupportActionBar(TopAppBar);
        TopAppBar.setNavigationIcon(R.drawable.ic_menu_24);
        TopAppBar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MapsActivity.this, "SideBar Clicked", Toast.LENGTH_LONG).show();
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                        //drawer.openDrawer(Gravity.START);
                    }
                }
        );

        //bottomNav
        /*
        bottomNav = findViewById(R.id.bottomSheetButton);

        bottomNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.show();
            }
        });

         */
    }


    /*
    navigationView.setNavigationItemSelectedListener { menuItem ->
        // Handle menu item selected
        menuItem.isChecked = true;
        drawerLayout.close();
    }
     */


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
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                System.out.println(mMap.getCameraPosition().target.latitude);
                System.out.println(mMap.getCameraPosition().target.longitude);
            }
        });

        //Setting default location. Features to include:
        //1.Need to check if gps is enabled. -- Request to enable if not.
        //2. Update google maps, showing user location.

    }


}