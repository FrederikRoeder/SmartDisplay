package de.fhws.smartdisplay.database;

public class SettingsData {

    private Long id;
    private boolean notificationSet = false;
    private String name = "";

    public SettingsData() {};

    public SettingsData(Long id, boolean notificationSet, String name) {
        this.id = id;
        this.notificationSet = notificationSet;
        this.name = name;
    }

    public SettingsData(boolean notificationSet, String name) {
        this.name = name;
    }

    public SettingsData(boolean notificationSet) {
        this.notificationSet = notificationSet;
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

    public boolean isNotificationSet() {return notificationSet;}

    public void setNotificationSet(boolean notificationSet) {this.notificationSet = notificationSet;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return super.toString();
    }
}
