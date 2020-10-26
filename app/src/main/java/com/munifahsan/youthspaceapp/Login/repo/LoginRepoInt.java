package com.munifahsan.youthspaceapp.Login.repo;

import com.facebook.AccessToken;

public interface LoginRepoInt {
    void doEmailLogin(String email, String pass);

    void doGoogleSignIn(String idToken);

    void doFacebookSignIn(AccessToken accessToken);
}
