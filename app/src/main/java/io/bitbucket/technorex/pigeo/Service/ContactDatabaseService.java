package io.bitbucket.technorex.pigeo.Service;

import android.content.Context;
import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.Repository.ContactRepository;
import io.bitbucket.technorex.pigeo.Repository.DatabaseContactRepository;

import java.util.List;

public class ContactDatabaseService {
    private DatabaseContactRepository contactRepository;

    public ContactDatabaseService(Context context){
        contactRepository=new DatabaseContactRepository(context);
    }

    public List<Contact> retrieveContacts(){
        return contactRepository.listContacts();
    }

    public void addContact(Contact contact){
        contactRepository.addContact(contact);
    }

    public void updateContact(Contact contact){
        contactRepository.updateContact(contact);
    }

    public void deleteContact(Contact contact){
        contactRepository.deleteContact(contact);
    }

    public List<Contact> getAllContacts(){
        return contactRepository.getAllContacts();
    }

    public void addToAllContacts(List<Contact> contacts){
        contactRepository.addToAllContacts(contacts);
    }

    public void addToContacts(List<Contact> contacts) {contactRepository.addToContacts(contacts);}

    public void resetAllContacts() {contactRepository.resetAllContacts();}

    public void resetContacts() {contactRepository.resetContacts();}

    public void updateContacts(){contactRepository.updateContacts();}
}
