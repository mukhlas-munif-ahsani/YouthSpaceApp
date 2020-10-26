package com.munifahsan.youthspaceapp.EditProfile.repo;

public interface EditProfileRepoInt {
    void getData();

    void updateData(String imageUrl, String email, String nama, String noTlp, String gender);
}
