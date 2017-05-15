package com.nplekhanov.musix;

import java.util.*;
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
                    x.setRating(1);
                });
                return true;
            }
            int i = 0;
            for (Opinion opinion: db.indexedUsers().get(userId).getOpinionByTrack().values()) {
                fixRating(opinion);
            }
            if (i > 0) {
                return true;
            }
            return false;
        });
        return repository.read(db -> {
            Comparator<Map.Entry<String, Opinion>> cmp = Comparator.comparing(x -> x.getValue().getAttitude().ordinal());
            cmp = cmp.thenComparing(x -> -x.getValue().getRating());
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
            fixRating(opinion);
            return true;
        });
    }

    @Override
    public void increaseTrackOrderOfDesired(String userId, int step) {
        repository.change(db -> {
            User user = db.indexedUsers().get(userId);
            for (Opinion o: user.getOpinionByTrack().values()) {
                if (o.getAttitude() == Attitude.DESIRED) {
                    o.setRating(o.getRating() + step);
                    fixRating(o);
                }
            }
            return true;
        });
    }

    @Override
    public void increaseTrackOrderOfAll(String userId, int step) {
        repository.change(db -> {
            User user = db.indexedUsers().get(userId);
            for (Opinion o: user.getOpinionByTrack().values()) {
                if (o.getAttitude() == Attitude.DESIRED || o.getAttitude() == Attitude.ACCEPTABLE) {
                    o.setRating(o.getRating() + step);
                    fixRating(o);
                }
            }
            return true;
        });
    }

    private void fixRating(Opinion o) {
        if (o.getRating() < 0) {
            o.setRating(0);
        }
        if (o.getRating() > 5) {
            o.setRating(5);
        }
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
            for (User user: users) {
                ot.getRatedWithinGroup().put(user, user.getOpinionByTrack().getOrDefault(ot.getTrack(), new Opinion()).getRating());
            }
        }
        Map<OpinionTrack, Collection<OpinionTrack>> subGroups = new TreeMap<>((o1, o2) -> {
            LongSummaryStatistics stat = new LongSummaryStatistics();
            for (User user: users) {
                Map<String, Opinion> obt = user.getOpinionByTrack();
                long c1 = obt.getOrDefault(o1.getTrack(), new Opinion()).getRating();
                long c2 = obt.getOrDefault(o2.getTrack(), new Opinion()).getRating();
                long diff = c2 - c1;
                if (diff != 0) {
                    diff = diff / Math.abs(diff);
                }
                stat.accept(diff);
            }
            if (stat.getAverage() > 0) {
                return 1;
            }
            if (stat.getAverage() < 0) {
                return -1;
            }
            return 0;
        });
        for (OpinionTrack track: tracks) {
            subGroups.put(track, new ArrayList<>());
        }
        for (OpinionTrack track: tracks) {
            subGroups.get(track).add(track);
        }
        int i = 0;
        for (Collection<OpinionTrack> subGroup: subGroups.values()) {
            i ++;
            for (OpinionTrack ot: subGroup) {
                ot.setPositionInsideGroup(i);
            }
        }
        return subGroups.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
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
