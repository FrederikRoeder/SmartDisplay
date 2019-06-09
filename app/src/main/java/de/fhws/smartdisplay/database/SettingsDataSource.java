package de.fhws.smartdisplay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SettingsDataSource {

    private SQLiteDatabase database;
    private DbHelper dbHelper;

    public SettingsDataSource(Context context) {
        dbHelper = new DbHelper(context);
    }

    private void open() {
        database = dbHelper.getWritableDatabase();
    }

    private void close() {
        database.close();
    }

    private String[] columns = {
            SettingsTable.COLUMN_NAME_ID,
            SettingsTable.COLUMN_NAME_EINS,
            SettingsTable.COLUMN_NAME_ZWEI,
            SettingsTable.COLUMN_NAME_DREI
    };

    private Settings cursorToSettings(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(SettingsTable.COLUMN_NAME_ID);
        int einsIndex = cursor.getColumnIndex(SettingsTable.COLUMN_NAME_EINS);
        int zweiIndex = cursor.getColumnIndex(SettingsTable.COLUMN_NAME_ZWEI);
        int dreiIndex = cursor.getColumnIndex(SettingsTable.COLUMN_NAME_DREI);

        long id = cursor.getLong(idIndex);
        int einsInt = cursor.getInt(einsIndex);
        int zwei = cursor.getInt(zweiIndex);
        String drei = cursor.getString(dreiIndex);

        boolean eins = false;
        if(einsInt > 0) {
            eins = true;
        }

        Settings settings = new Settings(id, eins, zwei, drei);

        return settings;
    }


    public List<Settings> getAllSettings() {
        List<Settings> settingsList = new ArrayList<>();
        open();
        Cursor cursor = database.query(SettingsTable.TABLE_NAME,
                columns, null, null, null, null, SettingsTable.COLUMN_NAME_ID);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Settings settings;
                while (!cursor.isAfterLast()) {
                    settings = cursorToSettings(cursor);
                    settingsList.add(settings);
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        close();
        return settingsList;
    }

    public Settings findById(Long id) {
        open();
        Cursor cursor = database.query(SettingsTable.TABLE_NAME,
                columns, SettingsTable.COLUMN_NAME_ID + "=?",
                new String[] {""+id}, null, null, null);

        Settings settings = null;
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                settings = cursorToSettings(cursor);
            }
        }
        cursor.close();
        close();
        return settings;
    }

    public Settings create(Settings settingsWithoutId) {
        open();
        int einsInt = 0;
        if(settingsWithoutId.isEins()) {
            einsInt = 1;
        }

        ContentValues values = new ContentValues();
        values.put(SettingsTable.COLUMN_NAME_EINS, einsInt);
        values.put(SettingsTable.COLUMN_NAME_ZWEI, settingsWithoutId.getZwei());
        values.put(SettingsTable.COLUMN_NAME_DREI, settingsWithoutId.getDrei());

        long insertId = database.insert(SettingsTable.TABLE_NAME, null, values);

        Cursor cursor = database.query(SettingsTable.TABLE_NAME,
                columns, SettingsTable.COLUMN_NAME_ID + "=?",
                new String[] {""+insertId}, null, null, null);

        Settings settings = null;
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                settings = cursorToSettings(cursor);
            }
        }
        cursor.close();
        close();
        return settings;
    }

    public void update(Settings settingsWithId) {
        open();
        int einsInt = 0;
        if(settingsWithId.isEins()) {
            einsInt = 1;
        }

        ContentValues values = new ContentValues();
        values.put(SettingsTable.COLUMN_NAME_EINS, einsInt);
        values.put(SettingsTable.COLUMN_NAME_ZWEI, settingsWithId.getZwei());
        values.put(SettingsTable.COLUMN_NAME_DREI, settingsWithId.getDrei());

        database.update(SettingsTable.TABLE_NAME,
                values,
                SettingsTable.COLUMN_NAME_ID + "=?",
                new String[] {""+settingsWithId.getId()});
        close();
    }

    public void delete(Long id) {
        open();
        database.delete(SettingsTable.TABLE_NAME,
                SettingsTable.COLUMN_NAME_ID + "=?",
                new String[] {""+id});
        close();
    }
}
