package com.munifahsan.youthspaceapp.Login.view;

public interface LoginViewInt {
    void showProgress();

    void hideProgress();

    void setEmailLayoutError(String msg);

    void setPassLayoutError(String msg);

    void setInputEnabled(Boolean enabled);

    void navigateToMain();

    void showMessage(String message);
}
