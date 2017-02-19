package com.nplekhanov.musix;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public interface Repository {
    void addUser(String userId, String fullName, String photoUrl);
    Map<String, User> getUsers();
    User getUser(String userId);
    Collection<TrackInfo> getTrackInfoList(String userId);
    void addRating(String userId, String track, Map<Role, Rating> ratings);
    String getDump();
    void setDump(String content);
    List<Composition> calculateChart();
}
