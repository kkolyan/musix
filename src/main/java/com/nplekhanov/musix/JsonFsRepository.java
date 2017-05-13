package com.nplekhanov.musix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class JsonFsRepository implements Repository {

    private File file = new File(System.getProperty("user.home"), "musix.json");

    public void addUser(String userId, String fullName, String photoUrl) {
        change(db -> {
            User user = db.indexedUsers().get(userId);
            if (user == null) {
                user = new User();
                user.setUid(userId);
                db.indexedUsers().put(userId, user);
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
        return read(Database::indexedUsers);
    }

    public User getUser(String userId) {
        return read(db -> db.indexedUsers().get(userId));
    }

    private NavigableSet<String> getTracks() {
        return read(db -> db.getUsers().stream()
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
        return read(db -> {
            Map<String, Set<String>> usersByTrack = db.getUsers().stream()
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
        change(db -> {
            User user = db.indexedUsers().get(userId);
            user.getRatingByTrack().put(track.trim(), ratings);
            return true;
        });
    }

    public void init() {
    }

    private interface UsersChange {
        boolean tryChange(Database db);
    }

    private interface UsersRead<T> {
        T read(Database db);
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
    public List<Composition> calculateChart(String chartName) {
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
        Chart chart = getCharts().get(chartName);
        return read(db -> {
            Band band = db.indexedBands().get(db.getDefaultBand());
            Collection<User> members = db.getUsers().stream()
                    .filter(user -> band.getMembers().contains(user.getUid()))
                    .collect(Collectors.toList());
            Map<String, List<UserTrackRating>> ratingsByTrack = members.stream()
                    .flatMap(user -> user.getRatingByTrack().entrySet().stream()
                            .map(rating -> new UserTrackRating(user.getUid(), rating.getKey(), rating.getValue())))
                    .collect(Collectors.groupingBy(x -> x.track));

            List<Composition> compositions = new ArrayList<>();

            ratingsByTrack.forEach((track, trackRatings) -> {
                if (!chart.getTracks().contains(track)) {
                    return;
                }
                Map<Role, Map<Rating, Set<String>>> ratingsForUsers = trackRatings.stream()
                        .flatMap(x -> x.ratings.entrySet().stream().map(y -> new UserRoleRating(x.userId, y.getKey(), y.getValue())))
                        .collect(
                                Collectors.groupingBy(x -> x.role,
                                        Collectors.groupingBy(x -> x.rating,
                                                Collectors.mapping(x -> x.userId,
                                                        Collectors.toSet()))));

                Composition c = ChartBuilder.compose(track, ratingsForUsers, members.stream().collect(Collectors.toMap(User::getUid, x -> x)));
                compositions.add(c);
            });
            compositions.sort(Comparator.comparing(x -> -1.0 * x.getRating() / x.getUsersRated().size()));
            return compositions;
        });
    }

    @Override
    public Map<String, Chart> getCharts() {
        Map<String, Chart> charts = new TreeMap<>();
        Chart defaultChart = new Chart();
        defaultChart.setName("");
        defaultChart.setTracks(getTracks());
        charts.put("", defaultChart);

        read(Database::getCharts)
                .forEach(chart -> charts.put(chart.getName(), chart));
        return charts;
    }

    @Override
    public void fixChart(String chartName) {
        Chart chart = new Chart();
        chart.setName(chartName);
        chart.setTracks(getTracks());
        change(db -> {
            if (db.getCharts().stream().anyMatch(x -> x.getName().equals(chartName))) {
                throw new IllegalStateException("chart with such name already exists");
            }
            db.getCharts().add(chart);
            return true;
        });
    }

    private synchronized void change(UsersChange callback) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Database db;
            try {
                db = objectMapper.readValue(file, Database.class);
            } catch (FileNotFoundException e) {
                db = new Database();
                db.setUsers(new ArrayList<>());
                db.setCharts(new ArrayList<>());
            }

            db.getUsers().forEach(this::fixTrackNames);

            boolean modified = callback.tryChange(db);

            if (modified) {
                db.setUsers(db.indexedUsers().values());
                // to avoid inconsistent write serialization and write are split
                String json = objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true).writeValueAsString(db);
                FileUtils.write(file, json, "UTF-8");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private synchronized <T> T read(UsersRead<T> callback) {
        Database db;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = FileUtils.readFileToString(file, "UTF-8");
            db = objectMapper.readValue(json, Database.class);

        } catch (FileNotFoundException e) {
            db = new Database();
            db.setUsers(new ArrayList<>());
            db.setCharts(new ArrayList<>());
        } catch (IOException e) {
            throw new IllegalStateException(e);

        }
        return callback.read(db);
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
