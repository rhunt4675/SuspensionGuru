package edu.mines.csci448.suspensionguru.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.mines.csci448.suspensionguru.db.DBHelper;
import edu.mines.csci448.suspensionguru.db.DBSchema;

public class SuspensionDimension {
    private Integer _id;
    private Integer _vehicleId;
    private Integer _setupId;

    private Double _upperFrameX;
    private Double _upperFrameY;
    private Double _upperFrameZ;
    private Double _upperAxleX;
    private Double _upperAxleY;
    private Double _upperAxleZ;

    private Double _lowerFrameX;

    private Double _lowerFrameY;
    private Double _lowerFrameZ;
    private Double _lowerAxleX;
    private Double _lowerAxleY;
    private Double _lowerAxleZ;

    private SuspensionDimension(Integer vehicleId, Integer setupId) {
        _vehicleId = vehicleId;
        _setupId = setupId;
    }

    private ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put(DBSchema.SuspensionTable.Cols.ID, _id);
        result.put(DBSchema.SuspensionTable.Cols.VEHICLE_ID, _vehicleId);
        result.put(DBSchema.SuspensionTable.Cols.SETUP_ID, _setupId);
        result.put(DBSchema.SuspensionTable.Cols.UPPER_FRAME_X, _upperFrameX);
        result.put(DBSchema.SuspensionTable.Cols.UPPER_FRAME_Y, _upperFrameY);
        result.put(DBSchema.SuspensionTable.Cols.UPPER_FRAME_Z, _upperFrameZ);
        result.put(DBSchema.SuspensionTable.Cols.UPPER_AXLE_X, _upperAxleX);
        result.put(DBSchema.SuspensionTable.Cols.UPPER_AXLE_Y, _upperAxleY);
        result.put(DBSchema.SuspensionTable.Cols.UPPER_AXLE_Z, _upperAxleZ);
        result.put(DBSchema.SuspensionTable.Cols.LOWER_FRAME_X, _lowerFrameX);
        result.put(DBSchema.SuspensionTable.Cols.LOWER_FRAME_Y, _lowerFrameY);
        result.put(DBSchema.SuspensionTable.Cols.LOWER_FRAME_Z, _lowerFrameZ);
        result.put(DBSchema.SuspensionTable.Cols.LOWER_AXLE_X, _lowerAxleX);
        result.put(DBSchema.SuspensionTable.Cols.LOWER_AXLE_Y, _lowerAxleY);
        result.put(DBSchema.SuspensionTable.Cols.LOWER_AXLE_Z, _lowerAxleZ);
        return result;
    }

    public static SuspensionDimension getSuspensionDimension(Vehicle vehicle, Setup setup, Context context) {
        // Local Variables
        Cursor cursor = null;

        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();

        try {
            // Query SuspensionDimensions
            cursor = db.query(DBSchema.SuspensionTable.NAME,    /* Table Name */
                    null,                                       /* Selection Columns */
                    DBSchema.SuspensionTable.Cols.VEHICLE_ID + "=? AND "
                        + DBSchema.SuspensionTable.Cols.SETUP_ID + "=?",
                                                                /* Selection */
                    new String[]{ String.valueOf(vehicle.getId()),
                        String.valueOf(setup.getId())},
                                                                /* Selection Arguments */
                    null,                                       /* Group By */
                    null,                                       /* Having */
                    null                                        /* Order By */ );

            // Parse Results
            SuspensionDimension dimension = new SuspensionDimension(vehicle.getId(), setup.getId());
            if (cursor.moveToNext()) {
                if (!cursor.isNull(0)) dimension.setId(cursor.getInt(0));
                if (!cursor.isNull(3)) dimension.setUpperFrameX(cursor.getDouble(3));
                if (!cursor.isNull(4)) dimension.setUpperFrameY(cursor.getDouble(4));
                if (!cursor.isNull(5)) dimension.setUpperFrameZ(cursor.getDouble(5));
                if (!cursor.isNull(6)) dimension.setUpperAxleX(cursor.getDouble(6));
                if (!cursor.isNull(7)) dimension.setUpperAxleY(cursor.getDouble(7));
                if (!cursor.isNull(8)) dimension.setUpperAxleZ(cursor.getDouble(8));
                if (!cursor.isNull(9)) dimension.setLowerFrameX(cursor.getDouble(9));
                if (!cursor.isNull(10)) dimension.setLowerFrameY(cursor.getDouble(10));
                if (!cursor.isNull(11)) dimension.setLowerFrameZ(cursor.getDouble(11));
                if (!cursor.isNull(12)) dimension.setLowerAxleX(cursor.getDouble(12));
                if (!cursor.isNull(13)) dimension.setLowerAxleY(cursor.getDouble(13));
                if (!cursor.isNull(14)) dimension.setLowerAxleZ(cursor.getDouble(14));
            }

            return dimension;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void saveSuspensionDimension(Context context) {
        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        // Null ID determines saved/unsaved
        if (_id == null) {

            // Set _id
            _id = 1;

            // Create new record
            db.insert(DBSchema.SuspensionTable.NAME,     /* Table Name */
                    null,                           /* Null Column Hack */
                    getContentValues()              /* Content Values */);

        } else {

            // Update existing record
            db.update(DBSchema.SuspensionTable.NAME,                            /* Table Name */
                    getContentValues(),                                         /* Content Values */
                    DBSchema.SuspensionTable.Cols.VEHICLE_ID + "=? AND "
                            + DBSchema.SuspensionTable.Cols.SETUP_ID + "=?",    /* Selection */
                    new String[]{ String.valueOf(_vehicleId),
                            String.valueOf(_setupId)}                            /* Selection Arguments */);
        }
    }

    public void deleteSuspensionDimension(Context context) {
        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        // Delete Entry
        db.delete(DBSchema.SetupTable.NAME,                                  /* Table Name */
                DBSchema.SuspensionTable.Cols.VEHICLE_ID + "=? AND "
                        + DBSchema.SuspensionTable.Cols.SETUP_ID + "=?",    /* Selection */
                new String[]{ String.valueOf(_vehicleId),
                        String.valueOf(_setupId)}                            /* Selection Arguments */);
    }

    public Integer getId() {
        return _id;
    }

    private void setId(Integer id) {
        _id = id;
    }

    public Double getUpperFrameX() {
        return _upperFrameX;
    }

    public void setUpperFrameX(Double upperFrameX) {
        _upperFrameX = upperFrameX;
    }

    public Double getUpperFrameY() {
        return _upperFrameY;
    }

    public void setUpperFrameY(Double upperFrameY) {
        _upperFrameY = upperFrameY;
    }

    public Double getUpperFrameZ() {
        return _upperFrameZ;
    }

    public void setUpperFrameZ(Double upperFrameZ) {
        _upperFrameZ = upperFrameZ;
    }

    public Double getUpperAxleX() {
        return _upperAxleX;
    }

    public void setUpperAxleX(Double upperAxleX) {
        _upperAxleX = upperAxleX;
    }

    public Double getUpperAxleY() {
        return _upperAxleY;
    }

    public void setUpperAxleY(Double upperAxleY) {
        _upperAxleY = upperAxleY;
    }

    public Double getUpperAxleZ() {
        return _upperAxleZ;
    }

    public void setUpperAxleZ(Double upperAxleZ) {
        _upperAxleZ = upperAxleZ;
    }

    public Double getLowerFrameX() {
        return _lowerFrameX;
    }

    public void setLowerFrameX(Double lowerFrameX) {
        _lowerFrameX = lowerFrameX;
    }

    public Double getLowerFrameY() {
        return _lowerFrameY;
    }

    public void setLowerFrameY(Double lowerFrameY) {
        _lowerFrameY = lowerFrameY;
    }

    public Double getLowerFrameZ() {
        return _lowerFrameZ;
    }

    public void setLowerFrameZ(Double lowerFrameZ) {
        _lowerFrameZ = lowerFrameZ;
    }

    public Double getLowerAxleX() {
        return _lowerAxleX;
    }

    public void setLowerAxleX(Double lowerAxleX) {
        _lowerAxleX = lowerAxleX;
    }

    public Double getLowerAxleY() {
        return _lowerAxleY;
    }

    public void setLowerAxleY(Double lowerAxleY) {
        _lowerAxleY = lowerAxleY;
    }

    public Double getLowerAxleZ() {
        return _lowerAxleZ;
    }

    public void setLowerAxleZ(Double lowerAxleZ) {
        _lowerAxleZ = lowerAxleZ;
    }
}
