package io.bitbucket.technorex.pigeo.Repository;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import io.bitbucket.technorex.pigeo.Domain.Profile;

import java.util.List;

public class ServerProfileRepository implements ProfileRepository {
    private final String DB_COLLECTION_NAME = "Profile";
    private final String LOG_TAG = "Pigeo";
    private FirebaseFirestore firebaseFirestore;

    public ServerProfileRepository(){
        firebaseFirestore=FirebaseFirestore.getInstance();
    }

    @Override
    public Profile retrieveProfile() {
        return null;
    }

    public void retrieveProfile(final String email,final OnResultListener<Profile> resultListener){
        firebaseFirestore
                .collection(DB_COLLECTION_NAME)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Profile dummy=null;
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            dummy = document.toObject(Profile.class);
                            Log.e("--------DummyMail--->>>", dummy.toString());
                            if (dummy.getEmailID().equals(email)) {
                                break;
                            }
                        }
                        resultListener.onResult(dummy);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Failed to retrieve profile.", e);
                    }
                });
    }

    @Override
    public List<Profile> getProfiles() {
        return null;
    }

    @Override
    public void updateProfile(Profile profile) {

    }

    @Override
    public void reset() {

    }
}
