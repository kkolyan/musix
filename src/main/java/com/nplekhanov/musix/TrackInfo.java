package com.nplekhanov.musix;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class TrackInfo {
    private String track;
    private boolean youRated;
    private int ratedUsersCount;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public boolean isYouRated() {
        return youRated;
    }

    public void setYouRated(boolean youRated) {
        this.youRated = youRated;
    }

    public int getRatedUsersCount() {
        return ratedUsersCount;
    }

    public void setRatedUsersCount(int ratedUsersCount) {
        this.ratedUsersCount = ratedUsersCount;
    }
}
