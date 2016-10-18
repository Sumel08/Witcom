package com.upiita.witcom.dataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by oscar on 11/10/16.
 */

public class WitcomDataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Witcom.db";
    public static final String ACTIVITIES_TABLE = "CREATE TABLE activities (" +
            "id         TEXT," +
            "activity   TEXT," +
            "id_image   TEXT," +
            "details    TEXT" +
            ");";
    public static final String CONFERENCES_TABLE = "CREATE TABLE conferences (" +
            "id         TEXT," +
            "title      TEXT," +
            "speaker    TEXT," +
            "come       TEXT," +
            "details    TEXT," +
            "notes      TEXT," +
            "id_image   TEXT" +
            ")";
    public static final String IMAGES_TABLE = "CREATE TABLE images (" +
            "id     TEXT," +
            "image  BLOB" +
            ")";
    public static final String OTHER_TABLE = "CREATE TABLE other (" +
            "id         TEXT," +
            "title      TEXT," +
            "second     TEXT," +
            "third      TEXT," +
            "id_image   TEXT" +
            ")";
    public static final String PAPERS_TABLE = "CREATE TABLE papers (" +
            "id         TEXT," +
            "title      TEXT," +
            "author     TEXT," +
            "details    TEXT," +
            "notes      TEXT," +
            "Field6     TEXT" +
            ")";
    public static final String SCHEDULE_TABLE = "CREATE TABLE schedule (" +
            "id             TEXT," +
            "id_activity    TEXT," +
            "date           TEXT," +
            "time           TEXT," +
            "id_details     TEXT" +
            ")";
    public static final String WORKSHOPS_TABLE = "CREATE TABLE workshops (" +
            "id         TEXT," +
            "title      TEXT," +
            "monitor    TEXT," +
            "detail     TEXT," +
            "notes      TEXT," +
            "id_image   TEXT," +
            "place      TEXT," +
            "date      TEXT," +
            "time      TEXT" +
            ")";

    public static final String CITIES_TABLE = "CREATE TABLE cities (" +
            "id     TEXT," +
            "city   TEXT," +
            "id_image  TEXT" +
            ")";

    public static final String PLACES_TABLE = "CREATE TABLE places (" +
            "id            TEXT," +
            "id_city       TEXT," +
            "place         TEXT," +
            "description   TEXT," +
            "address       TEXT," +
            "schedule      TEXT," +
            "extra         TEXT," +
            "latitude      TEXT," +
            "longitude     TEXT," +
            "id_image      TEXT," +
            "cost          TEXT," +
            "telephone     TEXT," +
            "webpage       TEXT" +
            ")";

    public static final String VERSION_TABLE = "CREATE TABLE version (" +
            "info_version   TEXT" +
            ")";

    public WitcomDataBase (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACTIVITIES_TABLE);
        db.execSQL(CONFERENCES_TABLE);
        db.execSQL(IMAGES_TABLE);
        db.execSQL(OTHER_TABLE);
        db.execSQL(PAPERS_TABLE);
        db.execSQL(SCHEDULE_TABLE);
        db.execSQL(WORKSHOPS_TABLE);
        db.execSQL(CITIES_TABLE);
        db.execSQL(PLACES_TABLE);
        db.execSQL(VERSION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
