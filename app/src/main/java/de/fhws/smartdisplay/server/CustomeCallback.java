package de.fhws.smartdisplay.server;

public interface CustomeCallback<T> {
    void onResponse(T value);
    void onFailure();
}