package com.zdjf.qqh.data.entity;

import java.io.Serializable;

public class BaseBean implements Serializable {
    /**
     * 状态码
     */
    private int status;
    /**
     * 错误描述
     */
    private String des;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}