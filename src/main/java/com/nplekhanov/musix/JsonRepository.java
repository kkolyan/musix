package com.nplekhanov.musix;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nplekhanov on 5/14/2017.
 */
public class JsonRepository implements Repository {

    private File file = new File(System.getProperty("user.home"), "musix.json");


    private ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public synchronized void change(UsersChange callback) {
        ObjectMapper objectMapper = createObjectMapper();
        try {
            Database db;
            try {
                db = objectMapper.readValue(file, Database.class);
            } catch (FileNotFoundException e) {
                db = new Database();
                db.setUsers(new ArrayList<>());
            }

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

    @Override
    public synchronized <T> T read(UsersRead<T> callback) {
        Database db;
        ObjectMapper objectMapper = createObjectMapper();
        try {
            String json = FileUtils.readFileToString(file, "UTF-8");
            db = objectMapper.readValue(json, Database.class);

        } catch (FileNotFoundException e) {
            db = new Database();
            db.setUsers(new ArrayList<>());
        } catch (IOException e) {
            throw new IllegalStateException(e);

        }
        return callback.read(db);
    }

    @Override
    public void setDump(String content) {
        try {
            FileUtils.write(file, content, "UTF-8");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public synchronized String getDump() {
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
