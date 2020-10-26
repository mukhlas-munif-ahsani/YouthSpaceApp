package com.munifahsan.youthspaceapp.Account.repo;

public interface AccountPageRepoInt {
    void getProfileDataFirebase();

    void getProfileDataGoogleOrFacebook();

    void checkProfileDataExist();

    Boolean isProfileDataExist();
}
