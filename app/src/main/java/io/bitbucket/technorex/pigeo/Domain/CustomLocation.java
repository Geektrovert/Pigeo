package io.bitbucket.technorex.pigeo.Domain;

import android.support.annotation.NonNull;

import java.util.Objects;

public class CustomLocation {
    private String latitude,longitude;
    public CustomLocation(){

    }

    public CustomLocation(String latiude, String longitude) {
        this.latitude = latiude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomLocation)) return false;
        CustomLocation that = (CustomLocation) o;
        return Objects.equals(getLatitude(), that.getLatitude()) &&
                Objects.equals(getLongitude(), that.getLongitude());
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
