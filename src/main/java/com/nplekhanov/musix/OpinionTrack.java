package com.nplekhanov.musix;

import java.util.Map;

/**
 * Created by nplekhanov on 5/13/2017.
 */
public class OpinionTrack {
    private String track;
    private Map<String,Opinion> opinionByUser;
    private Map<User, Long> ratedWithinGroup;
    private long positionInsideGroup;

    public long getPositionInsideGroup() {
        return positionInsideGroup;
    }

    public void setPositionInsideGroup(long positionInsideGroup) {
        this.positionInsideGroup = positionInsideGroup;
    }

    public Map<User, Long> getRatedWithinGroup() {
        return ratedWithinGroup;
    }

    public void setRatedWithinGroup(Map<User, Long> ratedWithinGroup) {
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
