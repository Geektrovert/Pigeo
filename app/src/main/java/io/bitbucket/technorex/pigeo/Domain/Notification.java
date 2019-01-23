package io.bitbucket.technorex.pigeo.Domain;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Notification implements Serializable {

    private String name;
    private String contactNumber;
    private double latitude;
    private double longitude;

    public Notification() {}

    public Notification(String name, String contactNumber, double latitude, double longitude) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0 &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getContactNumber(), that.getContactNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContactNumber(), getLatitude(), getLongitude());
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
