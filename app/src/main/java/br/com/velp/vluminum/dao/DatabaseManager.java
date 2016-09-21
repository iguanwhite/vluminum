package br.com.velp.vluminum.dao;

import android.content.Context;

public class DatabaseManager {

    private static DatabaseManager instance;
    private DatabaseHelper databaseHelper;

    private DatabaseManager(Context ctx) {
        databaseHelper = new DatabaseHelper(ctx);
    }

    public static void init(Context ctx) {
        if (null == instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

}
