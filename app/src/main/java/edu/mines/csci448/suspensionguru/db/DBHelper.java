package edu.mines.csci448.suspensionguru.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "suspensionguru.db";
    private static DBHelper theInstance = null;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (theInstance == null)
            theInstance = new DBHelper(context);
        return theInstance;
    }

    public static void initDB(Context context) {
        // Open the Database Once (may take a very long time)
        SQLiteOpenHelper helper = new DBHelper(context);
        new AsyncTask<SQLiteOpenHelper, Void, Void>() {
            @Override
            protected Void doInBackground(final SQLiteOpenHelper helpers[]) {
                SQLiteDatabase db = null;
                for (SQLiteOpenHelper helper : helpers)
                    try {
                        db = helper.getWritableDatabase();
                    } finally {
                        if (db != null) db.close();
                    }
                return null;
            }
        }.execute(helper);
    }

    public static void closeDB(Context context) {
        // Close the Database (may take a very long time)
        SQLiteOpenHelper helper = getInstance(context);
        helper.getWritableDatabase().close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Tables
        db.execSQL(DBSchema.VehicleTable.SQL_CREATE_TABLE);
        db.execSQL(DBSchema.SetupTable.SQL_CREATE_TABLE);
        db.execSQL(DBSchema.SuspensionTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do Nothing
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        // Enable Foreign Key Support
        if (!db.isReadOnly())
            db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
