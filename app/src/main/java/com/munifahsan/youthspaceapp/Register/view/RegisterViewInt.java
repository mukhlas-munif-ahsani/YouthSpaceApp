package com.munifahsan.youthspaceapp.Register.view;

public interface RegisterViewInt {
    void setEmailLayoutError(String msg);

    void setPassLayoutError(String msg);

    void setConfirmPassLayoutError(String msg);

    void setInputEnabled(Boolean enabeled);

    void showMessage(String message);

    void navigateToMain();

    void showProgress();

    void hideProgress();
}
