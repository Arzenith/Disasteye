package com.example.disasteye;

import com.google.android.gms.maps.model.LatLng;

public class Event {
    public LatLng coords;
    public String title;
    public String disasterType;

    Event (LatLng coords, String title, String disasterType)
    {
        this.coords = coords;
        this.title = title;
        this.disasterType = disasterType;
        printEvent();
    }

    public void printEvent()
    {
        System.out.println(this.title + " " + this.disasterType + " " + this.coords);
    }
}
