package com.munifahsan.youthspaceapp.EditProfile.view;

import com.munifahsan.youthspaceapp.R;

import butterknife.OnClick;

public interface EditProfileViewInt {

//    void onEmailOnClick(boolean showMessage);

    void showTextPhoto();

    void hideTextPhoto();

    void setTextPhoto(String msg);

    void setProfileImage(String url);

    void setEmail(String email);

    void setNama(String nama);

    void setNoTlp(String noTlp);

    void setGender(String gender);

    void setEmailError(String msg);

    void setNamaError(String msg);

    void setNoTlpError(String msg);

    void setGenderError(String msg);

    void setEmailEnabled(boolean enabled);

    void setInputEnabled(boolean enabled);

    void showShimmer();

    void hideShimmer();

    void showMessage(String msg);
}
