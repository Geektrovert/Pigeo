package io.bitbucket.technorex.pigeo.Repository;

import io.bitbucket.technorex.pigeo.Domain.Contact;

import java.util.List;

public interface ContactRepository {

    List<Contact> listContacts();

    void addContact(Contact contact);

    void deleteContact(Contact contact);

    void updateContact(Contact contact);
}
