package com.example.disasteye;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Event {
    public LatLng coords;
    public String title;
    public String disasterType;
    public Marker marker;


    public Event (LatLng coords, String title, String disasterType)
    {
        this.coords = coords;
        this.title = title;
        this.disasterType = disasterType;
    }

    public Event (LatLng coords, String title, String disasterType, Marker marker)
    {
        this.coords = coords;
        this.title = title;
        this.disasterType = disasterType;
        this.marker = marker;
    }

    public void printEvent()
    {
        System.out.println(this.title + " " + this.disasterType + " " + this.coords);
    }

    @Override public String toString() {
        return "Title: " + title + " Disaster: " + disasterType +  " Coordinates: " + coords +"\n";
    }
}
