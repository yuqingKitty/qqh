package com.zdjf.qqh.view;

public interface ILoginView extends IBaseView {
    void loginSuccess();

    void loginFailed(String dec);
}
