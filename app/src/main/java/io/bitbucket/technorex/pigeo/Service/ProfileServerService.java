package io.bitbucket.technorex.pigeo.Service;

import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Domain.UserCount;
import io.bitbucket.technorex.pigeo.Repository.ProfileRepository;
import io.bitbucket.technorex.pigeo.Repository.ServerProfileRepository;

import java.util.List;

public class ProfileServerService {
        private ServerProfileRepository profileRepository;
        public ProfileServerService(){
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
        public void getUserCount(UserCount userCount, ProfileRepository.OnResultListener<UserCount> resultListener){
            profileRepository.userCount(userCount,resultListener);
        }
        public void incrementUser(UserCount userCount){profileRepository.incrementUser(userCount);}
}
