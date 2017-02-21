package com.nplekhanov.musix;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by nplekhanov on 21/02/2017.
 */
public class Database {
    private Collection<User> users;
    private List<Chart> charts;
    private transient Map<String, User> indexedUsers;

    public Map<String,User> indexedUsers() {
        if (indexedUsers == null) {
            indexedUsers = users.stream()
                    .collect(Collectors.toMap(User::getUid, x -> x));
        }
        return indexedUsers;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public List<Chart> getCharts() {
        return charts;
    }

    public void setCharts(List<Chart> charts) {
        this.charts = charts;
    }
}
