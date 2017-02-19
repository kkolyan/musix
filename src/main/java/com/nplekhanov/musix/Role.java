package com.nplekhanov.musix;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public enum Role {
    LISTEN(Arrays.asList(
            Rating.DISLIKE,
            Rating.NEUTRAL,
            Rating.LIKE,
            Rating.VERY_LIKE)),
    DRUMS,
    LEAD_VOCAL,
    BASS,
    RHYTM_GUITAR,
    SOLO_GUITAR,
    KEYBOARD;

    private final Rating defaultRating;
    private final Set<Rating> supportedRatings;

    Role() {
        this(Arrays.asList(Rating.values()));
    }


    Role(List<Rating> supportedRatings) {
        this.defaultRating = supportedRatings.get(0);
        this.supportedRatings = Collections.unmodifiableSet(new HashSet<>(supportedRatings));
    }

    public Set<Rating> getSupportedRatings() {
        return supportedRatings;
    }

    public Rating getDefaultRating() {
        return defaultRating;
    }
}
