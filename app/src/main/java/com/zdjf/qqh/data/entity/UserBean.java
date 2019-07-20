package com.zdjf.qqh.data.entity;

/**
 * 用户实体
 */
public class UserBean {
    private long userId;
    private String nameFast;
    private String phone;
    //脱敏手机号
    private String decryptPhone;
    private String realNameFast;
    private String realNameDecryptFast;
    //身份证
    private String identity;
    //加密身份证号
    private String identityFast;

    private String emailFast;
    private String imageFast;
    private String sex;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNameFast() {
        return nameFast;
    }

    public void setNameFast(String nameFast) {
        this.nameFast = nameFast;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDecryptPhone() {
        return decryptPhone;
    }

    public void setDecryptPhone(String decryptPhone) {
        this.decryptPhone = decryptPhone;
    }

    public String getRealNameFast() {
        return realNameFast;
    }

    public void setRealNameFast(String realNameFast) {
        this.realNameFast = realNameFast;
    }

    public String getRealNameDecryptFast() {
        return realNameDecryptFast;
    }

    public void setRealNameDecryptFast(String realNameDecryptFast) {
        this.realNameDecryptFast = realNameDecryptFast;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentityFast() {
        return identityFast;
    }

    public void setIdentityFast(String identityFast) {
        this.identityFast = identityFast;
    }

    public String getEmailFast() {
        return emailFast;
    }

    public void setEmailFast(String emailFast) {
        this.emailFast = emailFast;
    }

    public String getImageFast() {
        return imageFast;
    }

    public void setImageFast(String imageFast) {
        this.imageFast = imageFast;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
