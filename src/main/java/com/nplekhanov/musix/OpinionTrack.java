package com.nplekhanov.musix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nplekhanov on 5/13/2017.
 */
public class OpinionTrack {
    private String track;
    private Map<String,Opinion> opinionByUser;

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public Map<String, Opinion> getOpinionByUser() {
        return opinionByUser;
    }

    public void setOpinionByUser(Map<String, Opinion> opinionByUser) {
        this.opinionByUser = opinionByUser;
    }

    public static Collection<OpinionTrack> aggregate(Collection<User> users) {
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
