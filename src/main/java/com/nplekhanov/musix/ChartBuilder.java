package com.nplekhanov.musix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by nplekhanov on 2/19/2017.
 */
public class ChartBuilder {
    public static Composition compose(String track, Map<Role, Map<Rating, Set<String>>> ratingsForUsers, Map<String, User> users) {
        Collection<RatingDetail> scoreDetails = new ArrayList<>();

        Set<String> usersRated = new TreeSet<>();
        int score = 0;
        for (User user: users.values()) {
            int listenScore = 0;
            int playScore = 0;

            Map<Role, Rating> ratingMap = user.getRatingByTrack().get(track);
            if (ratingMap != null) {
                usersRated.add(user.getUid());
                for (Role role: ratingMap.keySet()) {
                    Rating rating = ratingMap.get(role);
                    if (role == Role.LISTEN) {
                        switch (rating) {
                            case LIKE: listenScore += 1; break;
                            case VERY_LIKE: listenScore += 2; break;
                            case DISLIKE: listenScore += -2; break;
                        }
                    } else {
                        switch (rating) {
                            case LIKE: playScore = Math.max(playScore, 1); break;
                            case VERY_LIKE: playScore   = Math.max(playScore, 2); break;
                        }
                    }
                    if ((rating == Rating.DISLIKE && role == Role.LISTEN) || EnumSet.of(Rating.LIKE, Rating.VERY_LIKE).contains(rating)) {
                        scoreDetails.add(new RatingDetail(user.getUid(), role, rating));
                    }
                }
            }
            score += listenScore + playScore;
        }

        Map<Role, Set<String>> userIdsByRole = ratingsForUsers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        x -> x.getValue().entrySet().stream()
                                .filter(y -> EnumSet.of(Rating.NEUTRAL, Rating.LIKE, Rating.VERY_LIKE).contains(y.getKey()))
                                .flatMap(y -> y.getValue().stream())
                                .collect(Collectors.toSet())
                ));
        return new Composition(track, score, scoreDetails, userIdsByRole, usersRated);
    }

}
