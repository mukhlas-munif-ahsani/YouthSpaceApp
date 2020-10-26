package com.munifahsan.youthspaceapp.Register.pres;

import com.facebook.AccessToken;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;
import com.munifahsan.youthspaceapp.Register.RegisterEvent;
import com.munifahsan.youthspaceapp.Register.repo.RegisterRepo;
import com.munifahsan.youthspaceapp.Register.repo.RegisterRepoInt;
import com.munifahsan.youthspaceapp.Register.view.RegisterViewInt;

import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.munifahsan.youthspaceapp.Register.RegisterEvent.onError;
import static com.munifahsan.youthspaceapp.Register.RegisterEvent.onSuccess;

public class RegisterPres implements RegisterPresInt {
    private RegisterViewInt mRegisterViewInt;
    private EventBus mEventBus;
    private RegisterRepoInt mRegisterRepoInt;

    public RegisterPres(RegisterViewInt registerViewInt) {
        mRegisterViewInt = registerViewInt;
        mRegisterRepoInt = new RegisterRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Override
    public void onCreate() {
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mRegisterViewInt = null;
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onEventMainThread(RegisterEvent event) {
        switch (event.getEventType()) {
            case onSuccess:
                onRegisterSuccess();
                break;
            case onError:
                onRegisterError(event.getErrorMessage());
                break;
        }
    }

    private void onRegisterError(String errorMessage) {
        mRegisterViewInt.hideProgress();
        mRegisterViewInt.setInputEnabled(true);
        mRegisterViewInt.showMessage(errorMessage);
    }

    private void onRegisterSuccess() {
        mRegisterViewInt.navigateToMain();
    }

    @Override
    public void doEmailRegister(String email, String pass, String confirmPass) {
        mRegisterViewInt.showProgress();
        mRegisterViewInt.setInputEnabled(false);

        mRegisterRepoInt.doEmailRegister(email, pass);
    }

    @Override
    public void doGoogleSignIn(String token){
        mRegisterViewInt.showProgress();
        mRegisterViewInt.setInputEnabled(false);

        mRegisterRepoInt.doGoogleSignIn(token);
    }

    @Override
    public void doFacebookSignIn(AccessToken accessToken){
        mRegisterViewInt.showProgress();
        mRegisterViewInt.setInputEnabled(false);

        mRegisterRepoInt.doFacebookSignIn(accessToken);
    }

    @Override
    public Boolean isValidForm(String email, String pass, String confirmPass) {
        boolean isValid = true;
        if (email.isEmpty()){
            isValid = false;
            mRegisterViewInt.setEmailLayoutError("Email tidak boleh kosong");
        }

        if (!email.isEmpty() && !isEmailValid(email)){
            isValid = false;
            mRegisterViewInt.setEmailLayoutError("Email tidak valid");
        }

        if (pass.isEmpty()){
            isValid = false;
            mRegisterViewInt.setPassLayoutError("Password tidak boleh kosong");
        }

        if (confirmPass.isEmpty()){
            isValid = false;
            mRegisterViewInt.setConfirmPassLayoutError("Confirm password tidak boleh kosong");
        }

        if (!pass.isEmpty() && !confirmPass.isEmpty() && !confirmPass.equals(pass)){
            isValid = false;
            mRegisterViewInt.setConfirmPassLayoutError("Confirm password tidak sama");
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
