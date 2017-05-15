package com.nplekhanov.musix;

/**
 * Created by nplekhanov on 5/13/2017.
 */
public class Opinion {
    private Attitude attitude;
    private boolean notEnoughSkills;
    private String comment;
    private long rating;

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public Attitude getAttitude() {
        return attitude;
    }

    public void setAttitude(Attitude attitude) {
        this.attitude = attitude;
    }

    public boolean isNotEnoughSkills() {
        return notEnoughSkills;
    }

    public void setNotEnoughSkills(boolean notEnoughSkills) {
        this.notEnoughSkills = notEnoughSkills;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
