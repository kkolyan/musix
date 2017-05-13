package com.nplekhanov.musix;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by nplekhanov on 5/13/2017.
 */
public enum ShowMode {
    DESIRED_FIRST, NON_VOTED_FIRST;

    public Collection<OpinionTrack> sort(Collection<OpinionTrack> tracks, String user) {
        if (this == DESIRED_FIRST) {
            return tracks.stream()
                    .sorted(Comparator.comparing(x -> {
                        Map<Attitude, Long> countByAttitude = x.getOpinionByUser().values().stream()
                        .collect(Collectors.groupingBy(Opinion::getAttitude,
                                Collectors.counting()));
                        long order = -countByAttitude.getOrDefault(Attitude.DESIRED, 0L);
                        order += countByAttitude.getOrDefault(Attitude.UNACCEPTABLE, 0L);
                        if (countByAttitude.getOrDefault(Attitude.UNACCEPTABLE, 0L) > 0) {
                            order += 9000;
                        }

                        return order;
            })).collect(Collectors.toList());
        }
        if (this == NON_VOTED_FIRST) {

            return tracks.stream()
                    .sorted(Comparator.comparing(x -> {
                        Map<Attitude, Long> countByAttitude = x.getOpinionByUser().values().stream()
                                .collect(Collectors.groupingBy(Opinion::getAttitude,
                                        Collectors.counting()));
                        long order = -countByAttitude.getOrDefault(Attitude.DESIRED, 0L);
                        order += countByAttitude.getOrDefault(Attitude.UNACCEPTABLE, 0L);
                        if (countByAttitude.getOrDefault(Attitude.UNACCEPTABLE, 0L) > 0) {
                            order += 9000;
                        }
                        if (x.getOpinionByUser().get(user) == null) {
                            order -= 10000;
                        }

                        return order;
                    })).collect(Collectors.toList());
        }
        throw new IllegalStateException(""+this);
    }
}
