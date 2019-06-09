package de.fhws.smartdisplay.database;

public class SettingsTable {
    private static final String CONSTRAINT_PRIMARY_KEY = "PRIMARY KEY";
    private static final String FOREIGN_KEY = "FOREIGN KEY";
    private static final String REFERENCES = "REFERENCES";

    private static final String COLUMN_TYPE_TEXT = "TEXT";
    private static final String COLUMN_TYPE_INTEGER = "INTEGER";

    public static final String TABLE_NAME = "settings";

    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_EINS = "eins";
    public static final String COLUMN_NAME_ZWEI = "zwei";
    public static final String COLUMN_NAME_DREI = "drei";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME +
                    "(" +
                    COLUMN_NAME_ID     + " " + COLUMN_TYPE_INTEGER     + " " + CONSTRAINT_PRIMARY_KEY + "," +
                    COLUMN_NAME_EINS     + " " + COLUMN_TYPE_INTEGER     + "," +
                    COLUMN_NAME_ZWEI     + " " + COLUMN_TYPE_INTEGER     + "," +
                    COLUMN_NAME_DREI     + " " + COLUMN_TYPE_TEXT     +
                    ");";
}
