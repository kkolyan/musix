package com.nplekhanov.musix;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public interface Musix {
    void addUser(String userId, String fullName, String photoUrl);

    Collection<User> getDefaultBand();
    User getUser(String userId);

    void addOpinion(String userId, String track, Opinion opinion);

    Collection<OpinionTrack> getTracksWithOpinions();

    List<Map.Entry<String,Opinion>> getOrderedTracks(String userId);

    void increaseTrackOrder(String userId, String track, int step);

    void increaseTrackOrderOfDesired(String userId, int step);

    void increaseTrackOrderOfAll(String userId, int step);
}
