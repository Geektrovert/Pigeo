package io.bitbucket.technorex.pigeo.Domain;

import android.support.annotation.NonNull;

import java.util.Objects;

public class Notification {

    private String name, contactNumber;

    public Notification() {}
    public Notification(String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getContactNumber(), that.getContactNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getContactNumber());
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "name='" + name + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
