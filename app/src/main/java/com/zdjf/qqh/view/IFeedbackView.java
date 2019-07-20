package com.zdjf.qqh.view;

public interface IFeedbackView extends IBaseView {
    void submitSuccess();

    void uploadSuccess(String picLink);

    void uploadFailed();
}
