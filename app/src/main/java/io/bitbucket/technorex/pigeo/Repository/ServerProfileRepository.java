package io.bitbucket.technorex.pigeo.Repository;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Domain.UserCount;

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
        saveProfile(profile);
    }

    private void saveProfile(Profile profile) {
        firebaseFirestore.collection(DB_COLLECTION_NAME)
                .document("User"+ profile.getId())
                .set(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("***-----Server--->>>", "Update successful!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("***-----Server--->>>", "Update failed!");
                    }
                });
    }

    @Override
    public void reset() {

    }

    public void userCount(final UserCount userCount,final OnResultListener<UserCount> resultListener){
        firebaseFirestore
                .collection("UserCount")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        UserCount dummy = null;
                        for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            dummy= document.toObject(UserCount.class);
                            break;
                        }
                        resultListener.onResult(dummy);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(LOG_TAG, "Failed to retrieve profile count.", e);
                    }
                });
    }

    public void incrementUser(UserCount userCount){
        userCount.setNumber(userCount.getNumber()+1);
        firebaseFirestore.collection("UserCount")
                .document("UserNumber")
                .set(userCount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("***-----Server--->>>", "user count Update successful!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("***-----Server--->>>", "user count Update failed!");
                    }
                });
    }
}
