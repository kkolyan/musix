package com.nplekhanov.musix;

import java.util.Map;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class User {
    private String uid;
    private String fullName;
    private String photoUrl;
    private Map<String, Map<Role, Rating>> ratingByTrack;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<String, Map<Role, Rating>> getRatingByTrack() {
        return ratingByTrack;
    }

    public void setRatingByTrack(Map<String, Map<Role, Rating>> ratingByTrack) {
        this.ratingByTrack = ratingByTrack;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
