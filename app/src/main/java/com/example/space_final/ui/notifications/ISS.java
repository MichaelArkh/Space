package com.example.space_final.ui.notifications;

import com.google.android.gms.maps.model.LatLng;

/**
 * The type Iss object.
 */
public class ISS {
    private String[] people;
    private LatLng location;

    /**
     * Instantiates a new Iss.
     */
    public ISS() {}

    /**
     * Instantiates a new Iss.
     *
     * @param people   the people
     * @param location the location
     */
    public ISS(String[] people, LatLng location){
        this.people = people;
        this.location = location;
    }

    /**
     * Get people string [ ].
     *
     * @return the string [ ]
     */
    public String[] getPeople() {
        return people;
    }

    /**
     * Sets people.
     *
     * @param people the people
     */
    public void setPeople(String[] people) {
        this.people = people;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public LatLng getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(LatLng location) {
        this.location = location;
    }
}
