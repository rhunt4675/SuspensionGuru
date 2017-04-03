package edu.mines.csci448.suspensionguru.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.mines.csci448.suspensionguru.db.DBHelper;
import edu.mines.csci448.suspensionguru.db.DBSchema;

public class Vehicle {
    private Integer _id;
    private String _name;
    private String _purpose;

    public Vehicle(String name) {
        this(null, name, "");
    }

    private Vehicle(Integer id, String name, String purpose) {
        _id = id;
        _name = name;
        _purpose = purpose;
    }

    private ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put(DBSchema.VehicleTable.Cols.NAME, _name);
        result.put(DBSchema.VehicleTable.Cols.PURPOSE, _purpose);
        return result;
    }

    public static List<Vehicle> getVehicles(Context context) {
        // Local Variables
        List<Vehicle> results = new ArrayList<>();
        Cursor cursor = null;

        // Acquire a DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();

        try {
            // Query Vehicles
            cursor = db.query(DBSchema.VehicleTable.NAME,/* Table Name */
                    null,                   /* Table Columns */
                    null,                   /* Selection */
                    null,                   /* Selection Args */
                    null,                   /* Group By */
                    null,                   /* Having */
                    null                    /* Order By */);

            // Parse Results
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String purpose = cursor.getString(2);

                results.add(new Vehicle(id, name, purpose));
            }

            // Return results
            return results;

        } finally {
            // Free Resources
            if (cursor != null)
                cursor.close();
        }
    }

    public void saveVehicle(Context context) {
        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        // Null ID determines saved/unsaved
        if (_id == null) {

            // Create new record
            _id = (int) db.insert(DBSchema.VehicleTable.NAME,       /* Table Name */
                        null,                           /* Null Column Hack */
                        getContentValues()              /* Content Values */);

        } else {

            // Update existing record
            db.update(DBSchema.VehicleTable.NAME,       /* Table Name */
                        getContentValues(),             /* Content Values */
                        DBSchema.VehicleTable.Cols.ID + "=?",   /* Selection Clause */
                        new String[]{ String.valueOf(_id) }     /* Selection Arguments */ );
        }
    }

    public void deleteVehicle(Context context) {
        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        // Delete Entry
        db.delete(DBSchema.VehicleTable.NAME,           /* Table Name */
                DBSchema.VehicleTable.Cols.ID + "=?",   /* Selection Clause */
                new String[]{ String.valueOf(_id)}      /* Selection Arguments */);
    }

    int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getPurpose() {
        return _purpose;
    }

    public void setPurpose(String purpose) {
        _purpose = purpose;
    }
}
