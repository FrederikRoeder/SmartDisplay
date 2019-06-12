package de.fhws.smartdisplay.database;

public class SettingsData {

    private Long id;
    private boolean notificationEnabled = false;
    private String name = "";

    public SettingsData() {};

    public SettingsData(Long id, boolean notificationEnabled, String name) {
        this.id = id;
        this.notificationEnabled = notificationEnabled;
        this.name = name;
    }

    public SettingsData(boolean notificationEnabled, String name) {
        this.notificationEnabled = notificationEnabled;
        this.name = name;
    }

    public SettingsData(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public SettingsData(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNotificationEnabled() {return notificationEnabled;}

    public void setNotificationEnabled(boolean notificationEnabled) {this.notificationEnabled = notificationEnabled;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return super.toString();
    }
}
