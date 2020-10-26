package com.munifahsan.youthspaceapp.Account.pres;

import com.munifahsan.youthspaceapp.Account.AccountPageEvent;
import com.munifahsan.youthspaceapp.Account.repo.AccountPageRepo;
import com.munifahsan.youthspaceapp.Account.repo.AccountPageRepoInt;
import com.munifahsan.youthspaceapp.Account.view.AccountViewInt;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

import static com.munifahsan.youthspaceapp.Account.AccountPageEvent.onError;
import static com.munifahsan.youthspaceapp.Account.AccountPageEvent.onSuccess;

public class AccountPagePres implements AccountPagePresInt{

    private AccountViewInt mAccountPageView;
    private AccountPageRepoInt mAccountRepoInt;
    private EventBus mEventBus;

    public AccountPagePres(AccountViewInt mAccountPageView) {
        this.mAccountPageView = mAccountPageView;
        mAccountRepoInt = new AccountPageRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Subscribe
    public void onEventMainThread(AccountPageEvent event){
        switch (event.getEventType()){
            case onSuccess:
                onGetDataSuccess(event.getImageUrl(), event.getNama(), event.getEmail(), event.getNo_tlp(), event.getGender());
                break;
            case onError:
                onGetDataError(event.getErrorMessage());
                break;
        }
    }



    @Override
    public void onCreate(){
        mEventBus.register(this);
    }

    @Override
    public void onDestroy(){
        mAccountPageView = null;
        mEventBus.unregister(this);
    }

    @Override
    public void getProfileData(){
        mAccountPageView.showShimmer();

        mAccountRepoInt.getProfileDataFirebase();
    }

    private void onGetDataError(String errorMessage) {

    }

    public void onGetDataSuccess(String imageUrl, String nama, String email, String noTlp, String gender){
        mAccountPageView.hideShimmer();


        if (imageUrl.equals("")){
            mAccountPageView.showTextPhoto();
            mAccountPageView.setTextPhoto(String.valueOf(nama.charAt(0)));
        } else {
            mAccountPageView.hideTextPhoto();
            mAccountPageView.setProfileImage(imageUrl);
        }

        if (!nama.equals("")){
            mAccountPageView.setNama(nama);
        } else {
            mAccountPageView.setNama("-");
        }

        if (!email.equals("")){
            mAccountPageView.setEmail(email);
        } else {
            mAccountPageView.setEmail("-");
        }

        if (!noTlp.equals("")){
            mAccountPageView.setNoTlp(noTlp);
        } else {
            mAccountPageView.setNoTlp("-");
        }

        if (!gender.equals("")){
            mAccountPageView.setGender(gender);
        } else {
            mAccountPageView.setGender("-");
        }
    }
}
