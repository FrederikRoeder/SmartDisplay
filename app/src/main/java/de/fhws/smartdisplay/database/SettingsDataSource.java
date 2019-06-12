package de.fhws.smartdisplay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
            SettingsTable.COLUMN_NAME_NOTIFICATION_SET,
            SettingsTable.COLUMN_NAME_NAME
    };

    private SettingsData cursorToSettingsData(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(SettingsTable.COLUMN_NAME_ID);
        int notificationSetIndex = cursor.getColumnIndex(SettingsTable.COLUMN_NAME_NOTIFICATION_SET);
        int nameIndex = cursor.getColumnIndex(SettingsTable.COLUMN_NAME_NAME);

        long id = cursor.getLong(idIndex);
        int notificationSetInt = cursor.getInt(notificationSetIndex);
        String name = cursor.getString(nameIndex);

        boolean notificationSet = false;
        if(notificationSetInt > 0) {
            notificationSet = true;
        }

        SettingsData settings = new SettingsData(id, notificationSet, name);

        return settings;
    }


    public List<SettingsData> getAllSettings() {
        List<SettingsData> settingsList = new ArrayList<>();
        open();
        Cursor cursor = database.query(SettingsTable.TABLE_NAME,
                columns, null, null, null, null, SettingsTable.COLUMN_NAME_ID);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                SettingsData settings;
                while (!cursor.isAfterLast()) {
                    settings = cursorToSettingsData(cursor);
                    settingsList.add(settings);
                    cursor.moveToNext();
                }
            }
        }
        cursor.close();
        close();
        return settingsList;
    }

    public SettingsData findById(Long id) {
        open();
        Cursor cursor = database.query(SettingsTable.TABLE_NAME,
                columns, SettingsTable.COLUMN_NAME_ID + "=?",
                new String[] {""+id}, null, null, null);

        SettingsData settings = null;
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                settings = cursorToSettingsData(cursor);
            }
        }
        cursor.close();
        close();
        return settings;
    }

    public SettingsData create(SettingsData settingsWithoutId) {
        open();
        int notificationSetInt = 0;
        if(settingsWithoutId.isNotificationSet()) {
            notificationSetInt = 1;
        }

        ContentValues values = new ContentValues();
        values.put(SettingsTable.COLUMN_NAME_NOTIFICATION_SET, notificationSetInt);
        values.put(SettingsTable.COLUMN_NAME_NAME, settingsWithoutId.getName());

        long insertId = database.insert(SettingsTable.TABLE_NAME, null, values);

        Cursor cursor = database.query(SettingsTable.TABLE_NAME,
                columns, SettingsTable.COLUMN_NAME_ID + "=?",
                new String[] {""+insertId}, null, null, null);

        SettingsData settings = null;
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                settings = cursorToSettingsData(cursor);
            }
        }
        cursor.close();
        close();
        return settings;
    }

    public void update(SettingsData settingsWithId) {
        open();
        int notificationSetInt = 0;
        if(settingsWithId.isNotificationSet()) {
            notificationSetInt = 1;
        }

        ContentValues values = new ContentValues();
        values.put(SettingsTable.COLUMN_NAME_NOTIFICATION_SET, notificationSetInt);
        values.put(SettingsTable.COLUMN_NAME_NAME, settingsWithId.getName());

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

    public void deleteAll() {
        List<SettingsData> settingsList = getAllSettings();
        open();
        for(SettingsData s : settingsList) {
            database.delete(SettingsTable.TABLE_NAME,
                    SettingsTable.COLUMN_NAME_ID + "=?",
                    new String[] {""+s.getId()});
        }
        close();
    }
}
