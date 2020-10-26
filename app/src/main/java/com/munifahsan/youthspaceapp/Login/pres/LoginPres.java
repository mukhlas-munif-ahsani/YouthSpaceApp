package com.munifahsan.youthspaceapp.Login.pres;

import com.facebook.AccessToken;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;
import com.munifahsan.youthspaceapp.Login.LoginEvent;
import com.munifahsan.youthspaceapp.Login.repo.LoginRepo;
import com.munifahsan.youthspaceapp.Login.repo.LoginRepoInt;
import com.munifahsan.youthspaceapp.Login.view.LoginViewInt;
import com.munifahsan.youthspaceapp.Register.RegisterEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.munifahsan.youthspaceapp.Register.RegisterEvent.onError;
import static com.munifahsan.youthspaceapp.Register.RegisterEvent.onSuccess;

public class LoginPres implements LoginPresInt{
    private LoginViewInt mLoginViewInt;
    private LoginRepoInt mLoginRepoInt;
    private GreenRobotEventBus mEventBus;

    public LoginPres(LoginViewInt mLoginViewInt) {
        this.mLoginViewInt = mLoginViewInt;
        mLoginRepoInt = new LoginRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Override
    public void onCreate(){
        mEventBus.register(this);
    }

    @Override
    public void onDestroy(){
        mLoginViewInt = null;
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getEventType()) {
            case onSuccess:
                onLoginSuccess();
                break;
            case onError:
                onLoginError(event.getErrorMessage());
                break;
        }
    }

    private void onLoginSuccess() {
        mLoginViewInt.navigateToMain();
    }

    private void onLoginError(String message){
        mLoginViewInt.setInputEnabled(true);
        mLoginViewInt.hideProgress();

        mLoginViewInt.showMessage(message);
    }

    @Override
    public void doEmailLogin(String email, String pass){
        mLoginViewInt.showProgress();
        mLoginViewInt.setInputEnabled(false);

        mLoginRepoInt.doEmailLogin(email, pass);
    }

    @Override
    public void doGoogleSignIn(String idToken) {
        mLoginViewInt.setInputEnabled(false);

        mLoginRepoInt.doGoogleSignIn(idToken);
    }

    @Override
    public void doFacebookSignIn(AccessToken accessToken){
        mLoginViewInt.setInputEnabled(false);

        mLoginRepoInt.doFacebookSignIn(accessToken);
    }

    @Override
    public Boolean isValidForm(String email, String pass) {
        boolean isValid = true;
        if (email.isEmpty()){
            isValid = false;
            mLoginViewInt.setEmailLayoutError("Email tidak boleh kosong");
        }

        if (!email.isEmpty() && !isEmailValid(email)){
            isValid = false;
            mLoginViewInt.setEmailLayoutError("Email tidak valid");
        }

        if (pass.isEmpty()){
            isValid = false;
            mLoginViewInt.setPassLayoutError("Password tidak boleh kosong");
        }

        return isValid;
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
