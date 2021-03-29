package com.example.yantocks;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StocksDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "stocks.db";
    private static final int DB_VERSION = 1;

    public StocksDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StocksContract.StocksEntry.CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(StocksContract.StocksEntry.DROP_TABLE_COMMAND);
        onCreate(db);
    }
}
