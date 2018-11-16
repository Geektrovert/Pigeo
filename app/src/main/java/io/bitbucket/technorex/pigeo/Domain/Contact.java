package io.bitbucket.technorex.pigeo.Domain;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable {
    private String contactName;
    private String contactNumber;
    private int id;

    public Contact(String contactName, String contactNumber, int id) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.id = id;
    }

    public Contact(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return id == contact.id &&
                Objects.equals(contactName, contact.contactName) &&
                Objects.equals(contactNumber, contact.contactNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(contactName, contactNumber, id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Contact{" +
                "contactName='" + contactName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", id=" + id +
                '}';
    }
}
