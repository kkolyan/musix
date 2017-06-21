package com.nplekhanov.musix.model.tournament;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by nplekhanov on 21/06/2017.
 */
public class Tournament {
    private String name;
    private List<Tour> tours = new ArrayList<>();
    private Collection<String> listedTracks = new ArrayList<>();

    public Collection<String> getListedTracks() {
        return listedTracks;
    }

    public void setListedTracks(Collection<String> listedTracks) {
        this.listedTracks = listedTracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }
}
