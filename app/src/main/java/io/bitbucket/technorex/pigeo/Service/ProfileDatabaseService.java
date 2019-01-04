package io.bitbucket.technorex.pigeo.Service;

import android.content.Context;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Repository.DatabaseProfileRepository;
import io.bitbucket.technorex.pigeo.Repository.ProfileRepository;

import java.util.List;

public class ProfileDatabaseService {
    private DatabaseProfileRepository profileRepository;
    public ProfileDatabaseService(Context context){
        profileRepository=new DatabaseProfileRepository(context);
    }
    public Profile retrieveProfile(){
        return profileRepository.retrieveProfile();
    }
    public List<Profile> getProfiles(){return profileRepository.getProfiles();}
    public void updateProfile(Profile profile){
        profileRepository.updateProfile(profile);
    }
    public void reset(){profileRepository.reset();}
    public void addProfile(Profile profile){profileRepository.addProfile(profile);}
}
