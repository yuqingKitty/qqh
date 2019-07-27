package com.zdjf.qqh.data.entity;

import java.util.List;

/**
 * 消息中心
 */
public class MessageCenterBean extends BaseBean {
    public List<MessageBean> messageList;

    public class MessageBean {
        public String id;
        public String messageIconUrl; // 消息图标URL
        public String messageTitle;  // 消息标题
        public String messageSubTitle; // 消息副标题
        public String messageDesc; // 消息描述
        public int isOfficial; // 是否是官方
    }
}
