package com.nplekhanov.musix;

import com.nplekhanov.musix.model.Opinion;
import com.nplekhanov.musix.model.OpinionTrack;
import com.nplekhanov.musix.model.User;
import com.nplekhanov.musix.model.tournament.Tournament;
import com.nplekhanov.musix.model.tournament.TournamentStatus;

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

    boolean isFromDefaultBand(String userId);

    void startTournament(String name);

    void makeChoice(String tournamentName, int tour, Collection<String> tracks);

    Map<String, TournamentStatus> getTournaments();
}
