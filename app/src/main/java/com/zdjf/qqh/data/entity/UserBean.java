package com.zdjf.qqh.data.entity;

/**
 * 用户实体
 */
public class UserBean {
    private String nickName;
    private String phone;
    private String imageIcon;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }

    public String getDecryptPhone() {
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length());
    }
}
