package com.zdjf.qqh.data.entity;

/**
 * 登陆实体
 */
public class LoginBean extends BaseBean {
    private String userId;
    private String token;
    private String version;
    private UserBean userInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public UserBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserBean userInfo) {
        this.userInfo = userInfo;
    }
}
