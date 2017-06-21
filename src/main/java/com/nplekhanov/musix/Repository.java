package com.nplekhanov.musix;

import com.nplekhanov.musix.model.Database;

/**
 * Created by nplekhanov on 5/14/2017.
 */
public interface Repository {
    String getDump();

    void change(UsersChange callback);

    <T> T read(UsersRead<T> callback);

    void setDump(String content);

    interface UsersChange {
        boolean tryChange(Database db);
    }

    interface UsersRead<T> {
        T read(Database db);
    }
}
