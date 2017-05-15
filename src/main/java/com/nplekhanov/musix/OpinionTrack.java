package com.nplekhanov.musix;

import java.util.Map;

/**
 * Created by nplekhanov on 5/13/2017.
 */
public class OpinionTrack {
    private String track;
    private Map<String,Opinion> opinionByUser;
    private Map<User, Double> ratedWithinGroup;

    public Map<User, Double> getRatedWithinGroup() {
        return ratedWithinGroup;
    }

    public void setRatedWithinGroup(Map<User, Double> ratedWithinGroup) {
        this.ratedWithinGroup = ratedWithinGroup;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public Map<String, Opinion> getOpinionByUser() {
        return opinionByUser;
    }

    public void setOpinionByUser(Map<String, Opinion> opinionByUser) {
        this.opinionByUser = opinionByUser;
    }
}
