package com.munifahsan.youthspaceapp.EditProfile.pres;

import android.util.Log;

import com.google.gson.internal.$Gson$Preconditions;
import com.munifahsan.youthspaceapp.EditProfile.EditProfileEvent;
import com.munifahsan.youthspaceapp.EditProfile.repo.EditProfileRepo;
import com.munifahsan.youthspaceapp.EditProfile.repo.EditProfileRepoInt;
import com.munifahsan.youthspaceapp.EditProfile.view.EditProfileViewInt;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.munifahsan.youthspaceapp.EditProfile.EditProfileEvent.onError;
import static com.munifahsan.youthspaceapp.EditProfile.EditProfileEvent.onSuccess;

public class EditProfilePres implements EditProfilePresInt {
    private EditProfileViewInt mEditProfileView;
    private EditProfileRepoInt mEditProfileRepoInt;
    private EventBus mEventBus;

    public EditProfilePres(EditProfileViewInt mEditProfileView) {
        this.mEditProfileView = mEditProfileView;
        mEditProfileRepoInt = new EditProfileRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Subscribe
    public void onEventMainThread(EditProfileEvent event){
        switch (event.getEventType()){
            case onSuccess:
                onActSuccess(event.getImageUrl(), event.getEmail(), event.getNama(), event.getNo_tlp(), event.getGender());
                Log.d("nama in getEvent : " , event.getImageUrl());
                break;
            case onError:
                onActError(event.getErrorMessage());
                break;
        }
    }

    @Override
    public void onCreate(){
        mEventBus.register(this);
    }

    @Override
    public void onDestroy(){
        mEditProfileView = null;
        mEventBus.unregister(this);
    }

    private void onActError(String error) {
        mEditProfileView.showMessage(error);
    }

    private void onActSuccess(String imageUrl, String email, String nama, String noTlp, String gender) {
        mEditProfileView.hideShimmer();
        mEditProfileView.setInputEnabled(true);

        if (imageUrl.equals("")){
            mEditProfileView.showTextPhoto();
            mEditProfileView.setTextPhoto(String.valueOf(nama.charAt(0)));
        } else {
            mEditProfileView.hideTextPhoto();
            mEditProfileView.setProfileImage(imageUrl);
        }

        if (isEmailValid(email)){
            mEditProfileView.setEmailEnabled(false);
        } else {
//            mEditProfileView.onEmailOnClick(true);
            mEditProfileView.setEmailEnabled(true);
        }
        mEditProfileView.setEmail(email);

        if (!nama.equals("")){
            mEditProfileView.setNama(nama);
            mEditProfileView.showMessage(nama);
        } else {
            mEditProfileView.setNama("-");
        }

        if (!noTlp.equals("")){
            mEditProfileView.setNoTlp(noTlp);
        } else {
            mEditProfileView.setNoTlp("-");
        }

        if (!gender.equals("")){
            mEditProfileView.setGender(gender);
        }  //mEditProfileView.setGender("-");

    }

    @Override
    public void getData(){
        mEditProfileView.showShimmer();

        mEditProfileRepoInt.getData();
    }

    @Override
    public void updateProfileData(String imageUrl, String email, String nama, String noTlp, String gender){
        if (isEmailValid(email)){
            mEditProfileRepoInt.updateData(imageUrl, email, nama, noTlp, gender);
        } else {
            mEditProfileRepoInt.updateData(imageUrl, null, nama, noTlp, gender);
        }
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
