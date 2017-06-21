package com.nplekhanov.musix.model.tournament;

import java.util.Collection;

/**
 * Created by nplekhanov on 21/06/2017.
 */
public class TournamentStatus {
    private String name;
    private int tour;
    private Collection<String> listedTracks;
    private Collection<String> selectedTracks;
    private int tracksToSelect;
    private boolean finished;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTour() {
        return tour;
    }

    public void setTour(int tour) {
        this.tour = tour;
    }

    public Collection<String> getListedTracks() {
        return listedTracks;
    }

    public void setListedTracks(Collection<String> listedTracks) {
        this.listedTracks = listedTracks;
    }

    public Collection<String> getSelectedTracks() {
        return selectedTracks;
    }

    public void setSelectedTracks(Collection<String> selectedTracks) {
        this.selectedTracks = selectedTracks;
    }

    public int getTracksToSelect() {
        return tracksToSelect;
    }

    public void setTracksToSelect(int tracksToSelect) {
        this.tracksToSelect = tracksToSelect;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
