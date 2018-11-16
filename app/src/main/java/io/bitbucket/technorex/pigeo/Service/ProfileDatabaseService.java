package io.bitbucket.technorex.pigeo.Service;

import android.content.Context;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Repository.DatabaseProfileRepository;
import io.bitbucket.technorex.pigeo.Repository.ProfileRepository;

public class ProfileDatabaseService {
    private ProfileRepository profileRepository;
    public ProfileDatabaseService(Context context){
        profileRepository=new DatabaseProfileRepository(context);
    }
    public Profile retrieveProfile(){
        return profileRepository.retrieveProfile();
    }
    public void updateProfile(Profile profile){
        profileRepository.updateProfile(profile);
    }
}
