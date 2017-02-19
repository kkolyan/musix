package com.nplekhanov.musix;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by nplekhanov on 2/19/2017.
 */
public class Composition {
    private final String track;
    private final int rating;
    private final Collection<RatingDetail> ratingDetails;
    private final Map<Role, Set<String>> userIdsByRole;
    private final Set<String> usersRated;

    public Composition(String track, int rating, Collection<RatingDetail> ratingDetails, Map<Role, Set<String>> userIdsByRole, Set<String> usersRated) {
        this.track = track;
        this.rating = rating;
        this.ratingDetails = ratingDetails;
        this.userIdsByRole = userIdsByRole;
        this.usersRated = usersRated;
    }

    public Collection<RatingDetail> getRatingDetails() {
        return ratingDetails;
    }

    public String getTrack() {
        return track;
    }

    public int getRating() {
        return rating;
    }

    public Map<Role, Set<String>> getUserIdsByRole() {
        return userIdsByRole;
    }

    public Set<String> getUsersRated() {
        return usersRated;
    }
}
