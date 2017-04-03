package edu.mines.csci448.suspensionguru.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.mines.csci448.suspensionguru.db.DBHelper;
import edu.mines.csci448.suspensionguru.db.DBSchema;

public class Setup {
    /* Member Variables */
    private Integer _id;
    private String _name;
    private String _driver;
    private Double _targetSpeed;

    private String _locationName;
    private String _locationDescription;
    private Double _locationLatitude;
    private Double _locationLongitude;

    private Double _tirePressure;
    private Double _tireDiameter;
    private Double _tireRollingDiameter;
    private Double _tireWidth;

    private Double  _massVehicle;
    private Double _massFrontUnsprung;
    private Double _massBackUnsprung;

    private Double _centerOfGravityX;
    private Double _centerOfGravityY;
    private Double _centerOfGravityHeight;

    public Setup(String name) {
        this(null, name);
    }

    private Setup(Integer id, String name) {
        _id = id;
        _name = name;
    }

    private ContentValues getContentValues() {
        ContentValues result = new ContentValues();
        result.put(DBSchema.SetupTable.Cols.NAME, _name);
        result.put(DBSchema.SetupTable.Cols.DRIVER, _driver);
        result.put(DBSchema.SetupTable.Cols.TARGET_SPEED, _targetSpeed);
        result.put(DBSchema.SetupTable.Cols.LOCATION_NAME, _locationName);
        result.put(DBSchema.SetupTable.Cols.LOCATION_DESCRIPTION, _locationDescription);
        result.put(DBSchema.SetupTable.Cols.LOCATION_LATITUDE, _locationLatitude);
        result.put(DBSchema.SetupTable.Cols.LOCATION_LONGITUDE, _locationLongitude);
        result.put(DBSchema.SetupTable.Cols.TIRE_PRESSURE, _tirePressure);
        result.put(DBSchema.SetupTable.Cols.TIRE_DIAMETER, _tireDiameter);
        result.put(DBSchema.SetupTable.Cols.TIRE_ROLLING_DIAMETER, _tireRollingDiameter);
        result.put(DBSchema.SetupTable.Cols.TIRE_WIDTH, _tireWidth);
        result.put(DBSchema.SetupTable.Cols.MASS_VEHICLE, _massVehicle);
        result.put(DBSchema.SetupTable.Cols.MASS_FRONT_UNSPRUNG, _massFrontUnsprung);
        result.put(DBSchema.SetupTable.Cols.MASS_BACK_UNSPRUNG, _massBackUnsprung);
        result.put(DBSchema.SetupTable.Cols.COG_X, _centerOfGravityX);
        result.put(DBSchema.SetupTable.Cols.COG_Y, _centerOfGravityY);
        result.put(DBSchema.SetupTable.Cols.COG_HEIGHT, _centerOfGravityHeight);
        return result;
    }

    public static List<Setup> getSetups(Context context) {
        // Local Variables
        List<Setup> result = new ArrayList<>();
        Cursor cursor = null;

        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();

        try {
            // Query Setups
            cursor = db.query(DBSchema.SetupTable.NAME,         /* Table Name */
                                null,                           /* Selection Columns */
                                null,                           /* Selection */
                                null,                           /* Selection Arguments */
                                null,                           /* Group By */
                                null,                           /* Having */
                                null                            /* Order By */ );

            // Loop through Results
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);

                // Store Setup Entry
                Setup setup = new Setup(id, name);
                result.add(setup);

                // Apply Additional Data
                if (!cursor.isNull(2)) setup.setDriver(cursor.getString(2));
                if (!cursor.isNull(3)) setup.setTargetSpeed(cursor.getDouble(3));
                if (!cursor.isNull(4)) setup.setLocationName(cursor.getString(4));
                if (!cursor.isNull(5)) setup.setLocationDescription(cursor.getString(5));
                if (!cursor.isNull(6)) setup.setLocationLatitude(cursor.getDouble(6));
                if (!cursor.isNull(7)) setup.setLocationLongitude(cursor.getDouble(7));
                if (!cursor.isNull(8)) setup.setTirePressure(cursor.getDouble(8));
                if (!cursor.isNull(9)) setup.setTireDiameter(cursor.getDouble(9));
                if (!cursor.isNull(10)) setup.setTireRollingDiameter(cursor.getDouble(10));
                if (!cursor.isNull(11)) setup.setTireWidth(cursor.getDouble(11));
                if (!cursor.isNull(12)) setup.setMassVehicle(cursor.getDouble(12));
                if (!cursor.isNull(13)) setup.setMassFrontUnsprung(cursor.getDouble(13));
                if (!cursor.isNull(14)) setup.setMassBackUnsprung(cursor.getDouble(14));
                if (!cursor.isNull(15)) setup.setCenterOfGravityX(cursor.getDouble(15));
                if (!cursor.isNull(16)) setup.setCenterOfGravityY(cursor.getDouble(16));
                if (!cursor.isNull(17)) setup.setCenterOfGravityHeight(cursor.getDouble(17));
            }

            // Return results
            return result;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public void saveSetup(Context context) {
        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        // Null ID determines saved/unsaved
        if (_id == null) {

            // Create new record
            _id = (int) db.insert(DBSchema.SetupTable.NAME,     /* Table Name */
                    null,                           /* Null Column Hack */
                    getContentValues()              /* Content Values */);

        } else {

            // Update existing record
            db.update(DBSchema.SetupTable.NAME,       /* Table Name */
                    getContentValues(),               /* Content Values */
                    DBSchema.SetupTable.Cols.ID + "=?",     /* Selection Clause */
                    new String[]{ String.valueOf(_id) }     /* Selection Arguments */ );
        }
    }

    public void deleteSetup(Context context) {
        // Acquire DB Handle
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        // Delete Entry
        db.delete(DBSchema.SetupTable.NAME,             /* Table Name */
                    DBSchema.SetupTable.Cols.ID + "=?", /* Selection Clause */
                    new String[]{ String.valueOf(_id)}  /* Selection Arguments */);
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

    public String getDriver() {
        return _driver;
    }

    public void setDriver(String driver) {
        _driver = driver;
    }

    public Double getTargetSpeed() {
        return _targetSpeed;
    }

    public void setTargetSpeed(Double targetSpeed) {
        _targetSpeed = targetSpeed;
    }

    public String getLocationName() {
        return _locationName;
    }

    public void setLocationName(String locationName) {
        _locationName = locationName;
    }

    public String getLocationDescription() {
        return _locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        _locationDescription = locationDescription;
    }

    public Double getLocationLatitude() {
        return _locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        _locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return _locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        _locationLongitude = locationLongitude;
    }

    public Double getTirePressure() {
        return _tirePressure;
    }

    public void setTirePressure(Double tirePressure) {
        _tirePressure = tirePressure;
    }

    public Double getTireDiameter() {
        return _tireDiameter;
    }

    public void setTireDiameter(Double tireDiameter) {
        _tireDiameter = tireDiameter;
    }

    public Double getTireRollingDiameter() {
        return _tireRollingDiameter;
    }

    public void setTireRollingDiameter(Double tireRollingDiameter) {
        _tireRollingDiameter = tireRollingDiameter;
    }

    public Double getTireWidth() {
        return _tireWidth;
    }

    public void setTireWidth(Double tireWidth) {
        _tireWidth = tireWidth;
    }

    public Double getMassVehicle() {
        return _massVehicle;
    }

    public void setMassVehicle(Double massVehicle) {
        _massVehicle = massVehicle;
    }

    public Double getMassFrontUnsprung() {
        return _massFrontUnsprung;
    }

    public void setMassFrontUnsprung(Double massFrontUnsprung) {
        _massFrontUnsprung = massFrontUnsprung;
    }

    public Double getMassBackUnsprung() {
        return _massBackUnsprung;
    }

    public void setMassBackUnsprung(Double massBackUnsprung) {
        _massBackUnsprung = massBackUnsprung;
    }

    public Double getCenterOfGravityX() {
        return _centerOfGravityX;
    }

    public void setCenterOfGravityX(Double centerOfGravityX) {
        _centerOfGravityX = centerOfGravityX;
    }

    public Double getCenterOfGravityY() {
        return _centerOfGravityY;
    }

    public void setCenterOfGravityY(Double centerOfGravityY) {
        _centerOfGravityY = centerOfGravityY;
    }

    public Double getCenterOfGravityHeight() {
        return _centerOfGravityHeight;
    }

    public void setCenterOfGravityHeight(Double centerOfGravityHeight) {
        _centerOfGravityHeight = centerOfGravityHeight;
    }
}
