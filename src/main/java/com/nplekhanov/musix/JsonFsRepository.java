package com.nplekhanov.musix;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class JsonFsRepository implements Repository {

    private File file = new File(System.getProperty("user.home"), "musix.json");

    public void addUser(String userId, String fullName, String photoUrl) {
        change(users -> {
            User user = users.get(userId);
            if (user == null) {
                user = new User();
                user.setUid(userId);
                users.put(userId, user);
                user.setRatingByTrack(new HashMap<>());
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

    public Map<String, User> getUsers() {
        return read(x -> x);
    }

    public User getUser(String userId) {
        return read(users -> users.get(userId));
    }

    private Collection<String> getTracks() {
        return read(users -> users.values().stream()
                .flatMap(x -> x.getRatingByTrack().keySet().stream())
                .distinct()
                .collect(Collectors.toCollection(TreeSet::new)));
    }

    @Override
    public Collection<TrackInfo> getTrackInfoList(String userId) {
        class TrackUser {
            private final String track;
            private final String userId;

            private TrackUser(String track, String userId) {
                this.track = track;
                this.userId = userId;
            }
        }
        return read(users -> {
            Map<String, Set<String>> usersByTrack = users.values().stream()
                    .flatMap(user -> user.getRatingByTrack().keySet().stream().map(track -> new TrackUser(track, user.getUid())))
                    .collect(Collectors.groupingBy(x -> x.track, Collectors.mapping(x -> x.userId, Collectors.toSet())));
            ArrayList<TrackInfo> infos = new ArrayList<>();
            usersByTrack.forEach((track, userIds) -> {
                TrackInfo ti = new TrackInfo();
                ti.setTrack(track);
                ti.setRatedUsersCount(userIds.size());
                ti.setYouRated(userIds.contains(userId));
                infos.add(ti);
            });
            Comparator<TrackInfo> comparing = Comparator.comparing(x -> x.isYouRated() ? 1 : 0);
            comparing = comparing.thenComparing(TrackInfo::getTrack);
            infos.sort(comparing);
            return infos;
        });
    }

    public void addRating(String userId, String track, Map<Role, Rating> ratings) {
        change(users -> {
            User user = users.get(userId);
            user.getRatingByTrack().put(track.trim(), ratings);
            return true;
        });
    }

    public void init() {
    }

    private interface UsersChange {
        boolean tryChange(Map<String, User> users);
    }

    private interface UsersRead<T> {
        T read(Map<String, User> users);
    }

    public synchronized String getDump() {
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setDump(String content) {
        try {
            FileUtils.write(file, content, "UTF-8");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Composition> calculateChart() {
        class UserTrackRating {
            final String userId;
            final String track;
            final Map<Role,Rating> ratings;

            public UserTrackRating(String userId, String track, Map<Role, Rating> ratings) {
                this.userId = userId;
                this.track = track;
                this.ratings = ratings;
            }
        }
        class UserRoleRating {
            final String userId;
            final Role role;
            final Rating rating;

            public UserRoleRating(String userId, Role role, Rating rating) {
                this.userId = userId;
                this.role = role;
                this.rating = rating;
            }
        }
        return read(users -> {
            Map<String, List<UserTrackRating>> ratingsByTrack = users.values().stream()
                    .flatMap(user -> user.getRatingByTrack().entrySet().stream()
                            .map(rating -> new UserTrackRating(user.getUid(), rating.getKey(), rating.getValue())))
                    .collect(Collectors.groupingBy(x -> x.track));

            List<Composition> compositions = new ArrayList<>();

            ratingsByTrack.forEach((track, trackRatings) -> {
                Map<Role, Map<Rating, Set<String>>> ratingsForUsers = trackRatings.stream()
                        .flatMap(x -> x.ratings.entrySet().stream().map(y -> new UserRoleRating(x.userId, y.getKey(), y.getValue())))
                        .collect(
                                Collectors.groupingBy(x -> x.role,
                                        Collectors.groupingBy(x -> x.rating,
                                                Collectors.mapping(x -> x.userId,
                                                        Collectors.toSet()))));

                Composition c = Chart.compose(track, ratingsForUsers, users);
                if (c != null) {
                    compositions.add(c);
                }
            });
            compositions.sort(Comparator.comparing(x -> -1.0 * x.getRating() / x.getUsersRated().size()));
            return compositions;
        });
    }

    private synchronized void change(UsersChange callback) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String,User> usersMap;
            try {
                Collection<User> users = objectMapper.readValue(file, new TypeReference<Collection<User>>() {});
                usersMap = users.stream().collect(Collectors.toMap(User::getUid, x -> x));
            } catch (FileNotFoundException e) {
                usersMap = new HashMap<>();
            }

            usersMap.values().forEach(this::fixTrackNames);

            boolean modified = callback.tryChange(usersMap);

            if (modified) {
                // to avoid inconsistent write serialization and write are split
                String json = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true).writeValueAsString(new ArrayList<>(usersMap.values()));
                FileUtils.write(file, json, "UTF-8");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private synchronized <T> T read(UsersRead<T> callback) {
        Map<String,User> usersMap;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = FileUtils.readFileToString(file, "UTF-8");
            Collection<User> users = objectMapper.readValue(json, new TypeReference<Collection<User>>() {});
            usersMap = users.stream().collect(Collectors.toMap(User::getUid, x -> x));

        } catch (FileNotFoundException e) {
            usersMap = new HashMap<>();
        } catch (IOException e) {
            throw new IllegalStateException(e);

        }
        return callback.read(usersMap);
    }

    private void fixTrackNames(User user) {
        Map<String, Map<Role, Rating>> ratings = user.getRatingByTrack();
        Map<String, Map<Role, Rating>> fixed = new HashMap<>();
        ratings.forEach((track,rating) -> {
            fixed.put(track.trim(), rating);
        });
        user.getRatingByTrack().clear();
        user.getRatingByTrack().putAll(fixed);
    }
}
