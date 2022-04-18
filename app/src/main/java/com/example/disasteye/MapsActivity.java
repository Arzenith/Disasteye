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
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.ImageButton;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
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
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public MapsActivity(){

    }
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    SearchView searchView;

    Marker searchMarker = null;

    ArrayList<Event> eventArray = new ArrayList<>();
    ArrayList<Event> wildfireArray = new ArrayList<>();
    ArrayList<Event> volcanoesArray = new ArrayList<>();
    ArrayList<Event> seaLakeArray = new ArrayList<>();
    ArrayList<Event> stormArray = new ArrayList<>();

    private ConstraintLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout headerLayout;
    private ImageView swiper;
    private ImageButton aboutUs;

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

        HTTPRequest request = new HTTPRequest();
        try {
            request.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //R.id.idSearchView is found in the activity_maps.xml  -- Searches given location
        //More on searchView doc: https://abhiandroid.com/ui/searchview#SearchView_Methods_In_Android
        searchView = (SearchView) findViewById(R.id.idSearchView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //Add the coordinates for all our markers.
        this.eventArray = request.getEvents();

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

                        //Add marker, without creating a duplicate marker from previous search:
                       if(searchMarker != null) {
                           searchMarker.remove();
                       }
                       searchMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(locationName));

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

        //About us button
        aboutUs = (ImageButton) findViewById(R.id.aboutus);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAboutUs();
            }
        });
    }

    //Function to bring up the about us page
    public void openAboutUs(){
        Intent intent = new Intent(this, aboutus.class);
        startActivity(intent);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        try {
        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

                if (!success) {
                    Log.e(null, "Style parsing failed.");
                }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        mMap = googleMap;


        try {
            for (Event e : eventArray) {
                if (e.disasterType.toLowerCase().contains("wildfires")) {
                    Marker temp = mMap.addMarker(new MarkerOptions().position(e.coords).title(e.title).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_wild_fire)));
                    wildfireArray.add(new Event(e.coords, e.title, e.disasterType, temp));
                }
                else if (e.disasterType.toLowerCase().contains("volcanoes")) {
                    Marker temp = mMap.addMarker(new MarkerOptions().position(e.coords).title(e.title).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_volcano)));
                    volcanoesArray.add(new Event(e.coords, e.title, e.disasterType, temp));
                }
                else if(e.disasterType.toLowerCase().contains("sealakeice")){
                    Marker temp = mMap.addMarker(new MarkerOptions().position(e.coords).title(e.title).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_ice)));
                    seaLakeArray.add(new Event(e.coords, e.title, e.disasterType, temp));
                }
                else if(e.disasterType.toLowerCase().contains("severestorms")){
                    Marker temp = mMap.addMarker(new MarkerOptions().position(e.coords).title(e.title).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_storm)));
                    stormArray.add(new Event(e.coords, e.title, e.disasterType, temp));
                }
            }
        }
        catch(Exception exception){
            exception.printStackTrace();
        }

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                System.out.println(mMap.getCameraPosition().target.latitude);
                System.out.println(mMap.getCameraPosition().target.longitude);
            }
        });

    }

    public void toggleVisible(ArrayList<Event> events)
    {
       for(Event e: events)
       {
           e.marker.setVisible(false);
       }
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