package io.bitbucket.technorex.pigeo.Domain;

import android.support.annotation.NonNull;

import java.util.Objects;

public class CustomLocation {
    private double latitude,longitude;
    public CustomLocation(){

    }

    public CustomLocation(double latiude, double longitude) {
        this.latitude = latiude;
        this.longitude = longitude;
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
        if (!(o instanceof CustomLocation)) return false;
        CustomLocation that = (CustomLocation) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 &&
                Double.compare(that.getLongitude(), getLongitude()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }

    @NonNull
    @Override
    public String toString() {
        return "CustomLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
