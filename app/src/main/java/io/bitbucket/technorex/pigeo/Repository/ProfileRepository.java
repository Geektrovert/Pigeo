package io.bitbucket.technorex.pigeo.Repository;

import io.bitbucket.technorex.pigeo.Domain.Profile;

import java.util.List;

public interface ProfileRepository {

    Profile retrieveProfile();

    List<Profile> getProfiles();

    void updateProfile(Profile profile);

    void retrieveProfile(String email,OnResultListener<Profile> resultListener);

    interface OnResultListener<T> {

        void onResult(T data);

    }
}
