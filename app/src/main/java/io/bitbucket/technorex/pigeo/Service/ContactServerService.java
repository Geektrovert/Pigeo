package io.bitbucket.technorex.pigeo.Service;

import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.Repository.ContactRepository;
import io.bitbucket.technorex.pigeo.Repository.ServerContactRepository;

import java.util.List;

public class ContactServerService {
    private ServerContactRepository contactRepository;

    public ContactServerService(){
        contactRepository=new ServerContactRepository();
    }

    public void listCardsAsync(ContactRepository.OnResultListener<List<Contact>> resultListener) {
        contactRepository.listContactsAsync(resultListener);
    }

    public void addContact(Contact contact, String id){
        contactRepository.addContact(contact, id);
    }

    public void updateContact(Contact contact, String id){
        contactRepository.updateContact(contact, id);
    }

    public void deleteContact(Contact contact){
        contactRepository.deleteContact(contact);
    }
}
