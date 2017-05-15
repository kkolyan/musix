package com.nplekhanov.musix;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class RepositoryUsingMusix implements Musix {

    private Repository repository;

    public RepositoryUsingMusix(Repository repository) {
        this.repository = repository;
    }

    public void addUser(String userId, String fullName, String photoUrl) {
        repository.change(db -> {
            User user = db.indexedUsers().get(userId);
            if (user == null) {
                user = new User();
                user.setUid(userId);
                db.indexedUsers().put(userId, user);
                user.setOpinionByTrack(new HashMap<>());
            }
            if (fullName != null && !fullName.trim().isEmpty() && !fullName.equals("null null")) {
                user.setFullName(fullName);
            }
            if (photoUrl != null && !photoUrl.isEmpty()) {
                user.setPhotoUrl(photoUrl);
            }
            return true;
        });
    }

    @Override
    public Collection<User> getDefaultBand() {
        return repository.read(db -> db.indexedBands().get(db.getDefaultBand()).getMembers().stream()
                .map(x -> db.indexedUsers().get(x))
                .collect(Collectors.toList()));
    }

    public User getUser(String userId) {
        return repository.read(db -> db.indexedUsers().get(userId));
    }

    @Override
    public void addOpinion(String userId, String track, Opinion opinion) {
        repository.change(db -> {
            User user = db.indexedUsers().get(userId);
            user.getOpinionByTrack().put(track.trim(), opinion);
            return true;
        });
    }

    public void init() {
    }

    @Override
    public Collection<OpinionTrack> getTracksWithOpinions() {

        Collection<User> users = getDefaultBand();
        Collection<OpinionTrack> tracks = aggregateTracksWithOpinions(users);
        return sortTracksWithOpinions(tracks, users);
    }

    @Override
    public List<Map.Entry<String, Opinion>> getOrderedTracks(String userId) {
        repository.change(db -> {
            Set<Long> ratings = db.indexedUsers().get(userId).getOpinionByTrack().values().stream()
                    .map(Opinion::getRating)
                    .distinct()
                    .collect(Collectors.toSet());
            if (ratings.size() == 1 && ratings.iterator().next().equals(0L)) {
                db.indexedUsers().get(userId).getOpinionByTrack().values().forEach(x -> {
                    x.setRating(100 - x.getAttitude().ordinal() * 100);
                });
                return true;
            }
            return false;
        });
        return repository.read(db -> {
            Comparator<Map.Entry<String, Opinion>> cmp = Comparator.comparing(x -> -x.getValue().getRating());
            cmp = cmp.thenComparing(Map.Entry::getKey);
            return db.indexedUsers().get(userId).getOpinionByTrack().entrySet().stream()
                    .sorted(cmp)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public void increaseTrackOrder(String userId, String track, int step) {
        repository.change(db -> {
            User user = db.indexedUsers().get(userId);
            Opinion opinion = user.getOpinionByTrack().get(track);
            opinion.setRating(opinion.getRating() + step);
            return true;
        });
    }

    private Collection<OpinionTrack> sortTracksWithOpinions(Collection<OpinionTrack> tracks, Collection<User> users) {

        Map<Long, List<OpinionTrack>> groups = tracks.stream()
                .collect(Collectors.groupingBy(x -> {
                    Map<Attitude, Long> countByAttitude = x.getOpinionByUser().values().stream()
                            .collect(Collectors.groupingBy(Opinion::getAttitude,
                                    Collectors.counting()));
                    long order = 100 * -countByAttitude.getOrDefault(Attitude.DESIRED, 0L);
                    order += 100 * countByAttitude.getOrDefault(Attitude.UNACCEPTABLE, 0L);
                    if (countByAttitude.getOrDefault(Attitude.UNACCEPTABLE, 0L) > 0) {
                        order += 90000;
                    }
                    if (x.getOpinionByUser().size() != users.size()) {
                        order -= 100000;
                    }

                    return order;
                }));

        return new TreeMap<>(groups).values().stream()
                .map(x -> sortGroup(x, users))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<OpinionTrack> sortGroup(List<OpinionTrack> tracks, Collection<User> users) {
        for (OpinionTrack ot: tracks) {
            ot.setRatedWithinGroup(new HashMap<>());
        }
        for (User user: users) {
            for (OpinionTrack ot: tracks) {
                Opinion o = user.getOpinionByTrack().get(ot.getTrack());
                ot.getRatedWithinGroup().put(user, o == null ? 0L : o.getRating());
            }
            long sum = tracks.stream().mapToLong(x -> x.getRatedWithinGroup().get(user)).sum();
            if (sum > 0) {
                for (OpinionTrack ot: tracks) {
                    long normalized = 100 * ot.getRatedWithinGroup().get(user) / sum;
                    ot.getRatedWithinGroup().put(user, normalized);
                }
            }
        }
        tracks.sort(Comparator.comparing(x -> -x.getRatedWithinGroup().values().stream().mapToLong(Long::longValue).sum()));
        return tracks;
    }

    private Collection<OpinionTrack> aggregateTracksWithOpinions(Collection<User> users) {
        Map<String,OpinionTrack> map = new HashMap<>();
        for (User user: users) {
            user.getOpinionByTrack().forEach((track, op) -> {
                OpinionTrack ot = map.compute(track, (k, v) -> {
                    if (v != null) {
                        return v;
                    }
                    OpinionTrack t = new OpinionTrack();
                    t.setTrack(k);
                    t.setOpinionByUser(new HashMap<>());
                    return t;
                });
                ot.getOpinionByUser().put(user.getUid(), user.getOpinionByTrack().get(track));
            });
        }
        return map.values();
    }
}
