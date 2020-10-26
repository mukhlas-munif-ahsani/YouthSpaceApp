package com.munifahsan.youthspaceapp.EditProfile.pres;

public interface EditProfilePresInt {
    void onCreate();

    void onDestroy();

    void getData();

    void updateProfileData(String imageUrl, String email, String nama, String noTlp, String gender);
}
