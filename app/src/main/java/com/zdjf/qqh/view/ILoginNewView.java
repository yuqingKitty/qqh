package com.zdjf.qqh.view;

public interface ILoginNewView extends IBaseView {
    void getLoginSmsSuccess();

    void loginSuccess();

    void loginFailed(String dec);
}