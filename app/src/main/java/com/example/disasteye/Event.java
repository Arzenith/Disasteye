package com.example.disasteye;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Event {
    public LatLng coords;
    public String title;
    public String disasterType;
    public Marker marker;
    public String date;


    public Event (LatLng coords, String title, String disasterType, String date)
    {
        this.coords = coords;
        this.title = title;
        this.disasterType = disasterType;
        this.date = date;
    }

    public Event (LatLng coords, String title, String disasterType, String date, Marker marker)
    {
        this.coords = coords;
        this.title = title;
        this.disasterType = disasterType;
        this.date = date;
        this.marker = marker;
    }

    public void printEvent()
    {
        System.out.println(this.title + " " + this.disasterType + " " + this.coords);
    }

    @Override public String toString() {
        return "Title: " + title + " Disaster: " + disasterType +  " Coordinates: " + coords +"\n" + " Date: " + date;
    }

    public String formattedCoordinates(){

        String coordinates = coords.toString();
        String formattedCoords = coordinates.replaceFirst("lat/lng: ", "");

        return formattedCoords;
    }

    public String formattedDate(){
        String newDate =date;
        String[] arr = newDate.split("[-]",0);
        String month = "January";

        switch(arr[1]){
            case "01":
                month = "January";
                break;
            case "02":
                month = "February";
                break;
            case "03":
                month = "March";
                break;
            case "04":
                month = "April";
                break;
            case "05":
                month = "May";
                break;
            case "06":
                month = "June";
                break;
            case "07":
                month = "July";
                break;
            case "08":
                month = "August";
                break;
            case "09":
                month = "September";
                break;
            case "10":
                month = "October";
                break;
            case "11":
                month = "November";
                break;
            case "12":
                month = "December";
                break;
        }

        String date = arr[2].substring(0,2);

        String formattedDate = month + " " + date + ", " + arr[0];

        return formattedDate;

    }
}
