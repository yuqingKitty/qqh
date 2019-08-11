package com.leuters.qqh.data.entity;

import java.io.Serializable;

public class UploadBean extends BaseBean implements Serializable {
    private SysNoticeBean sysNoticeVO;
    private String srcURL;
    //是否强制更新（1是 0否）
    private int appEnForce;
    private String title;
    private String name;

    public UploadBean(String srcURL, int appEnForce, String title, String name) {
        this.srcURL = srcURL;
        this.appEnForce = appEnForce;
        this.title = title;
        this.name = name;
    }

    /**
     * 系统提示
     */
    public static class SysNoticeBean implements Serializable {
        private String title;
        private String noticeName;
        private String srcURL;
        private String buttonText;

        public SysNoticeBean(String srcURL) {
            this.srcURL = srcURL;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNoticeName() {
            return noticeName;
        }

        public void setNoticeName(String noticeName) {
            this.noticeName = noticeName;
        }

        public String getSrcURL() {
            return srcURL;
        }

        public void setSrcURL(String srcURL) {
            this.srcURL = srcURL;
        }

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            this.buttonText = buttonText;
        }
    }

    public String getSrcURL() {
        return srcURL;
    }

    public void setSrcURL(String srcURL) {
        this.srcURL = srcURL;
    }

    public int getAppEnForce() {
        return appEnForce;
    }

    public void setAppEnForce(int appEnForce) {
        this.appEnForce = appEnForce;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SysNoticeBean getSysNoticeVO() {
        return sysNoticeVO;
    }

    public void setSysNoticeVO(SysNoticeBean sysNoticeVO) {
        this.sysNoticeVO = sysNoticeVO;
    }
}

