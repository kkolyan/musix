package com.nplekhanov.musix.model;

import java.util.Map;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class User {
    private String uid;
    private String fullName;
    private String photoUrl;

    private Map<String, Opinion> opinionByTrack;

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

    public Map<String, Opinion> getOpinionByTrack() {
        return opinionByTrack;
    }

    public void setOpinionByTrack(Map<String, Opinion> opinionByTrack) {
        this.opinionByTrack = opinionByTrack;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
