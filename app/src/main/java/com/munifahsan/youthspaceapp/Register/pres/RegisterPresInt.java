package com.munifahsan.youthspaceapp.Register.pres;

import com.facebook.AccessToken;

public interface RegisterPresInt {
    void onCreate();

    void onDestroy();

    void doEmailRegister(String email, String pass, String confirmPass);

    void doGoogleSignIn(String token);

    void doFacebookSignIn(AccessToken accessToken);

    Boolean isValidForm(String email, String pass, String confirmPass);
}
