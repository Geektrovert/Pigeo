package io.bitbucket.technorex.pigeo.Repository;

import io.bitbucket.technorex.pigeo.Domain.Profile;

public interface ProfileRepository {
    Profile retrieveProfile();

    void updateProfile(Profile profile);

}
