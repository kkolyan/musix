package com.nplekhanov.musix;

/**
 * Created by nplekhanov on 2/19/2017.
 */
public class RatingDetail {
    private String userId;
    private Role role;
    private Rating rating;

    public RatingDetail(String userId, Role role, Rating rating) {
        this.userId = userId;
        this.role = role;
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public Rating getRating() {
        return rating;
    }
}
