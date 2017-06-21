package com.nplekhanov.musix.model;

import com.nplekhanov.musix.model.tournament.Tournament;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by nplekhanov on 21/02/2017.
 */
public class Database {
    private Collection<User> users;
    private transient Map<String, User> indexedUsers;
    private transient Map<String, Band> indexedBands;
    private Collection<Band> bands;
    private String defaultBand;
    private List<Tournament> tournaments = new ArrayList<>();

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public Map<String,User> indexedUsers() {
        if (indexedUsers == null) {
            indexedUsers = users.stream()
                    .collect(Collectors.toMap(User::getUid, x -> x));
        }
        return indexedUsers;
    }

    public Map<String,Band> indexedBands() {
        if (indexedBands == null) {
            indexedBands = bands.stream()
                    .collect(Collectors.toMap(Band::getName, x -> x));
        }
        return indexedBands;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Band> getBands() {
        return bands;
    }

    public void setBands(Collection<Band> bands) {
        this.bands = bands;
    }

    public String getDefaultBand() {
        return defaultBand;
    }

    public void setDefaultBand(String defaultBand) {
        this.defaultBand = defaultBand;
    }
}
