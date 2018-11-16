package io.bitbucket.technorex.pigeo.Service;

import android.content.Context;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Repository.ProfileRepository;
import io.bitbucket.technorex.pigeo.Repository.ServerProfileRepository;

import java.util.List;

public class ProfileServerService {
        private ProfileRepository profileRepository;
        public ProfileServerService(Context context){
            profileRepository=new ServerProfileRepository();
        }
        public Profile retrieveProfile(){
            return profileRepository.retrieveProfile();
        }
        public void retrieveProfile(String email, ProfileRepository.OnResultListener<Profile> resultListener) {
            profileRepository.retrieveProfile(email,resultListener);
        }
        public List<Profile> getProfiles(){return profileRepository.getProfiles();}
        public void updateProfile(Profile profile){
            profileRepository.updateProfile(profile);
        }
}
