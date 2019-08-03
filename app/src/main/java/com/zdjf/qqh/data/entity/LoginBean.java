package com.zdjf.qqh.data.entity;

/**
 * 登陆实体
 */
public class LoginBean extends BaseBean {
    private String uid;
    private String token;
    private String version;
    private UserBean userVo;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public UserBean getUserVo() {
        return userVo;
    }

    public void setUserVo(UserBean userVo) {
        this.userVo = userVo;
    }
}
