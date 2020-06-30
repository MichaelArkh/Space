package com.example.michaelarkhangelskiy_final.ui.notifications;

import com.google.android.gms.maps.model.LatLng;

public class ISS {
    private String[] people;
    private LatLng location;

    public ISS() {}

    public ISS(String[] people, LatLng location){
        this.people = people;
        this.location = location;
    }

    public String[] getPeople() {
        return people;
    }

    public void setPeople(String[] people) {
        this.people = people;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
