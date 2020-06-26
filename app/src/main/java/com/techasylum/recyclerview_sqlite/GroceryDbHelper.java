package com.techasylum.recyclerview_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.techasylum.recyclerview_sqlite.GroceryContract.*;

public class GroceryDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="my_grocery";
    public static final int VERSION=1;
    SQLiteDatabase db;
    public GroceryDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GROCERY_TABLE= "CREATE TABLE " +
                GroceryEntry.TABLE_NAME + " (" +
                GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GroceryEntry.COLUMN_AMOUNT + " TEXT NOT NULL, " +
                GroceryEntry.COLUMN_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_GROCERY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +GroceryEntry.TABLE_NAME);
        onCreate(db);

    }




}
