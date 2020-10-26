package com.munifahsan.youthspaceapp.Register.repo;

import com.facebook.AccessToken;

public interface RegisterRepoInt {
    void doEmailRegister(String email, String pass);

    void doGoogleSignIn(String idToken);

    void doFacebookSignIn(AccessToken accessToken);

}
