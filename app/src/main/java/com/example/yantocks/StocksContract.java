package com.example.yantocks;

import android.provider.BaseColumns;

public class StocksContract {
    public static final class StocksEntry implements BaseColumns {
        public static final String TABLE_NAME = "stocks";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_SHORT_NAME = "short_name";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_SECTOR = "sector";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_CLOSE = "close";
        public static final String COLUMN_HIGH = "high";
        public static final String COLUMN_LOW = "low";
        public static final String COLUMN_CHANGE_DAY_FLAT = "change_day_flat";
        public static final String COLUMN_CHANGE_DAY_PERCENT = "change_day_percent";
        public static final String COLUMN_TIME_UPDATE = "time_update";
        public static final String COLUMN_FAVOURITE = "favourite";

        public static final String TYPE_INTEGER = "INTEGER";
        public static final String TYPE_REAL = "REAL";
        public static final String TYPE_TEXT = "TEXT";

        public static final String CREATE_TABLE_COMMAND =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " " + TYPE_INTEGER + " PRIMARY KEY, "
                + COLUMN_SHORT_NAME + " " + TYPE_TEXT + ", "
                + COLUMN_FULL_NAME + " " + TYPE_TEXT + ", "
                + COLUMN_COUNTRY + " " + TYPE_TEXT + ", "
                + COLUMN_SECTOR + " " + TYPE_TEXT + ", "
                + COLUMN_CURRENCY + " " + TYPE_TEXT + ", "
                + COLUMN_CLOSE + " " + TYPE_REAL + ", "
                + COLUMN_HIGH + " " + TYPE_REAL + ", "
                + COLUMN_LOW + " " + TYPE_REAL + ", "
                + COLUMN_CHANGE_DAY_FLAT + " " + TYPE_TEXT + ", "
                + COLUMN_CHANGE_DAY_PERCENT + " " + TYPE_TEXT + ", "
                + COLUMN_TIME_UPDATE + " " + TYPE_TEXT + ", "
                + COLUMN_FAVOURITE + " " + TYPE_INTEGER + ")";
        public static final String DROP_TABLE_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}