package com.techasylum.recyclerview_sqlite;

import android.provider.BaseColumns;

public class GroceryContract {
    public GroceryContract() {
    }


    public static final class GroceryEntry implements BaseColumns{

        public static final String TABLE_NAME="groceryList";
        public static final String  COLUMN_NAME="name";
        public static final String  COLUMN_AMOUNT="amount";
        public static final String  COLUMN_TIME="time";
    }
}