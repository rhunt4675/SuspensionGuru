package edu.mines.csci448.suspensionguru.db;

public class DBSchema {
    public static final class VehicleTable {
        public static final String NAME = "vehicle";

        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String PURPOSE = "purpose";
        }

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + NAME + " ("
                        + Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + Cols.NAME + " TEXT NOT NULL, "
                        + Cols.PURPOSE + " TEXT)";
    }

    public static final class SetupTable {
        public static final String NAME = "setup";

        public static final class Cols {
            /* General */
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String DRIVER = "driver";
            public static final String TARGET_SPEED = "speed";

            /* Location */
            public static final String LOCATION_NAME = "location";
            public static final String LOCATION_DESCRIPTION = "location_description";
            public static final String LOCATION_LATITUDE = "latitude";
            public static final String LOCATION_LONGITUDE = "longitude";

            /* Tires */
            public static final String TIRE_PRESSURE = "pressure";
            public static final String TIRE_DIAMETER = "diameter";
            public static final String TIRE_ROLLING_DIAMETER = "rolling_diameter";
            public static final String TIRE_WIDTH = "width";

            /* Mass */
            public static final String MASS_VEHICLE = "mass";
            public static final String MASS_FRONT_UNSPRUNG = "mass_front";
            public static final String MASS_BACK_UNSPRUNG = "mass_back";

            /* Center of Gravity */
            public static final String COG_X = "cogx";
            public static final String COG_Y = "cogy";
            public static final String COG_HEIGHT = "cogz";
        }

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + NAME + " ("
                        + Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + Cols.NAME + " TEXT NOT NULL, "
                        + Cols.DRIVER + " TEXT, "
                        + Cols.TARGET_SPEED + " REAL, "

                        + Cols.LOCATION_NAME + " TEXT, "
                        + Cols.LOCATION_DESCRIPTION + " TEXT, "
                        + Cols.LOCATION_LATITUDE + " REAL, "
                        + Cols.LOCATION_LONGITUDE + " REAL, "

                        + Cols.TIRE_PRESSURE + " REAL, "
                        + Cols.TIRE_DIAMETER + " REAL, "
                        + Cols.TIRE_ROLLING_DIAMETER + " REAL, "
                        + Cols.TIRE_WIDTH + " REAL, "

                        + Cols.MASS_VEHICLE + " REAL, "
                        + Cols.MASS_FRONT_UNSPRUNG + " REAL, "
                        + Cols.MASS_BACK_UNSPRUNG + " REAL, "

                        + Cols.COG_X + " REAL, "
                        + Cols.COG_Y + " REAL, "
                        + Cols.COG_HEIGHT + " REAL)";
    }

    public static final class SuspensionTable {
        public static final String NAME = "suspension";

        public static final class Cols {
            public static final String ID = "id";
            public static final String VEHICLE_ID = "v_id";
            public static final String SETUP_ID = "s_id";
            public static final String UPPER_FRAME_X = "ufx";
            public static final String UPPER_FRAME_Y = "ufy";
            public static final String UPPER_FRAME_Z = "ufz";
            public static final String UPPER_AXLE_X = "uax";
            public static final String UPPER_AXLE_Y = "uay";
            public static final String UPPER_AXLE_Z = "uaz";
            public static final String LOWER_FRAME_X = "lfx";
            public static final String LOWER_FRAME_Y = "lfy";
            public static final String LOWER_FRAME_Z = "lfz";
            public static final String LOWER_AXLE_X = "lax";
            public static final String LOWER_AXLE_Y = "lay";
            public static final String LOWER_AXLE_Z = "laz";
        }

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + NAME + " ("
                        + Cols.ID + " INTEGER NOT NULL, "
                        + Cols.VEHICLE_ID + " INTEGER NOT NULL, "
                        + Cols.SETUP_ID + " INTEGER NOT NULL, "
                        + Cols.UPPER_FRAME_X + " REAL, "
                        + Cols.UPPER_FRAME_Y + " REAL, "
                        + Cols.UPPER_FRAME_Z + " REAL, "
                        + Cols.UPPER_AXLE_X + " REAL, "
                        + Cols.UPPER_AXLE_Y + " REAL, "
                        + Cols.UPPER_AXLE_Z + " REAL, "
                        + Cols.LOWER_FRAME_X + " REAL, "
                        + Cols.LOWER_FRAME_Y + " REAL, "
                        + Cols.LOWER_FRAME_Z + " REAL, "
                        + Cols.LOWER_AXLE_X + " REAL, "
                        + Cols.LOWER_AXLE_Y + " REAL, "
                        + Cols.LOWER_AXLE_Z + " REAL, "
                        + "PRIMARY KEY (" + Cols.VEHICLE_ID + ", " + Cols.SETUP_ID + ") "
                        + "FOREIGN KEY (" + Cols.VEHICLE_ID + ") REFERENCES " + VehicleTable.NAME + "(" + VehicleTable.Cols.ID + ") ON DELETE CASCADE, "
                        + "FOREIGN KEY (" + Cols.SETUP_ID + ") REFERENCES " + SetupTable.NAME + "(" + SetupTable.Cols.ID + ") ON DELETE CASCADE)";
    }
}
