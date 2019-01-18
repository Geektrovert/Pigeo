package io.bitbucket.technorex.pigeo.Repository;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import io.bitbucket.technorex.pigeo.Domain.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerContactRepository implements ContactRepository{
    private static FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private static final String DB_COLLECTION_NAME = "ContactsList";
    private static final String LOG_TAG = "Pigeo";

    private FirebaseFirestore db;

    public ServerContactRepository() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public List<Contact> listContacts() {
        throw new RuntimeException("Wrong method");
    }

    public void listContactsAsync(final ContactRepository.OnResultListener<List<Contact>> resultListener) {
        db.collection(DB_COLLECTION_NAME)
                .document(Objects.requireNonNull(firebaseUser.getEmail()))
                .collection("Contacts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Contact> contacts = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            contacts.add(document.toObject(Contact.class));
                        }

                        resultListener.onResult(contacts);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Failed to retrieve cards.", e);
                    }
                });
    }

    @Override
    public void addContact(Contact contact) {

    }

    public void addContact(Contact contact, String id) {
        saveContact(contact, id);
    }


    @Override
    public void deleteContact(final Contact contact) {
        db.collection(DB_COLLECTION_NAME)
                .document("Contact"+contact.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(LOG_TAG, "Contact deleted successfully from FireStore: " + contact);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Failed to delete contact: " + contact, e);
                    }
                });
    }

    @Override
    public void updateContact(Contact contact) {

    }

    public void updateContact(Contact contact, String id) {
        saveContact(contact, id);
    }

    @Override
    public Contact retrieveContact(int id) {
        return null;
    }

    private void saveContact(final Contact contact, String id) {
        db.collection(DB_COLLECTION_NAME)
                .document(Objects.requireNonNull(firebaseUser.getEmail()))
                .collection("Contacts")
                .document("Contact" + id)
                .set(contact)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(LOG_TAG, "Card saved successfully to FireStore: " + contact);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Failed to save card: " + contact, e);
                    }
                });
    }
}
