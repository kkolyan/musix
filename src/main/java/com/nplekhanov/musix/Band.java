package com.nplekhanov.musix;

import java.util.Set;

/**
 * Created by nplekhanov on 4/29/2017.
 */
public class Band {
    private String name;
    private Set<String> members;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }
}
