package com.example.disasteye;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.disasteye.databinding.ActivityMapsBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public MapsActivity(){

    }
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    SearchView searchView;

    ArrayList<LatLng> wildfireArray = new ArrayList<>();
    ArrayList<LatLng> floodArray = new ArrayList<>();
    ArrayList<LatLng> droughtArray = new ArrayList<>();
    ArrayList<LatLng> earthquakeArray = new ArrayList<>();

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
        bottomSheetBehavior.setMaxHeight(1800);
        bottomSheetBehavior.setPeekHeight(200);
        bottomSheetBehavior.setHideable(false);
        headerLayout = findViewById(R.id.header_layout);
        swiper = findViewById(R.id.swiper);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        //Request will receive a URL and gather data from the API!
        String link = "https://eonet.gsfc.nasa.gov/api/v3/events/geojson?category=wildfires";
        HTTPRequest request = new HTTPRequest();
        request.execute(link);

        //R.id.idSearchView is found in the activity_maps.xml  -- Searches given location
        //More on searchView doc: https://abhiandroid.com/ui/searchview#SearchView_Methods_In_Android
        searchView = (SearchView) findViewById(R.id.idSearchView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //Add the coordinates for all our markers.
        ArrayList<Event> eventArray = request.getEvents();
        //System.out.println("EVENTS ARE " + request.getEvents());
        try {
            for (Event e : eventArray) {
                System.out.println("EVENT BEING "+ e);
                if (e.disasterType.contains("wildfires")) {
                    wildfireArray.add(e.coords);
                }
//                else if (e.disasterType.contains("flood")) {
//                    floodArray.add(e.coords);
//                } else if (e.disasterType.contains("drought")) {
//                    droughtArray.add(e.coords);
//                } else if (e.disasterType.contains("earthquake")) {
//                    earthquakeArray.add(e.coords);
//                }
            }
        }
        catch(Exception exception){
            exception.printStackTrace();
        }

        //OnQueryTextListener() -- call backs to changed made in query text: https://developer.android.com/reference/android/widget/SearchView.OnQueryTextListener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String locationName = searchView.getQuery().toString();
                List<Address> addresses = null;

                if (locationName != null) {
                    // Create geocoder obj -- takes address and finds location: https://developer.android.com/reference/android/location/Geocoder
                    Geocoder geocoder = new Geocoder(MapsActivity.this);

                     //Given locationName, it will gecode the location on map, and adds to addressList
                    try {
                        addresses = geocoder.getFromLocationName(locationName, 1);
                    } catch (IOException except) {
                        except.printStackTrace();
                    }

                    //Get location, from the first position listed in addressList:
                   try{
                       Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        // Add marker to pos.
                        mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));
                        // Move to pos.
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }
                   catch(Exception except) {
                       Toast.makeText(MapsActivity.this, "Location not found. Try again.",
                               Toast.LENGTH_SHORT).show();
                       except.printStackTrace();
                    }
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
        TopAppBar.setNavigationIcon(R.drawable.ic_menu_24);
        TopAppBar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                        drawer.openDrawer(Gravity.LEFT);
                    }
                }
        );
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
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            for (LatLng latLng : wildfireArray) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("Wildfire")
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_wild_fire)));
            }
//            for (LatLng latLng : droughtArray) {
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Drought")
//                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_drought)));
//            }
//            for (LatLng latLng : floodArray) {
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Flood")
//                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_flood)));
//            }
//            for (LatLng latLng : earthquakeArray) {
//                mMap.addMarker(new MarkerOptions().position(latLng).title("Earthquake")
//                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_earthquake)));
//            }
        }
        catch(Exception except){
            except.printStackTrace();
        }

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                System.out.println(mMap.getCameraPosition().target.latitude);
                System.out.println(mMap.getCameraPosition().target.longitude);
            }
        });

    }
    //Documentation: https://developers.google.com/android/reference/com/google/android/gms/maps/model/package-summary
    //BitmapDescriptor defines our bitmap image.
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }
}