package io.bitbucket.technorex.pigeo.Domain;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {
    private String contactName;
    private String contactNumber;
    private String id;
    private String checker="no";

    public Contact() {}

    public Contact(String contactName, String contactNumber, String id) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.id = id;
    }

    public Contact(String contactName, String contactNumber, String id, String checker) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.id = id;
        this.checker = checker;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return Objects.equals(getContactName(), contact.getContactName()) &&
                Objects.equals(getContactNumber(), contact.getContactNumber()) &&
                Objects.equals(getId(), contact.getId()) &&
                Objects.equals(getChecker(), contact.getChecker());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContactName(), getContactNumber(), getId(), getChecker());
    }

    @NonNull
    @Override
    public String toString() {
        return "Contact{" +
                "contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", id='" + id + '\'' +
                ", checker='" + checker + '\'' +
                '}';
    }
}
