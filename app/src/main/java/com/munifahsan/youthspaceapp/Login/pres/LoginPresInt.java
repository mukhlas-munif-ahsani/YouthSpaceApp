package com.munifahsan.youthspaceapp.Login.pres;

import com.facebook.AccessToken;

public interface LoginPresInt {
    void onCreate();

    void onDestroy();

    void doGoogleSignIn(String idToken);

    void doEmailLogin(String email, String pass);

    void doFacebookSignIn(AccessToken accessToken);

    Boolean isValidForm(String email, String pass);
}
