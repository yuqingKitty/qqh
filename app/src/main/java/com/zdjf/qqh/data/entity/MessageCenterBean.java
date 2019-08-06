package com.zdjf.qqh.data.entity;

import java.util.List;

/**
 * 消息中心
 */
public class MessageCenterBean extends BaseBean {
    public List<MessageBean> messageList;

    public class MessageBean {
        public String id;
        public String iconURL; // 消息图标URL
        public String title;  // 消息标题
        public String summary; // 消息副标题
        public String content; // 消息描述
        public String label;
    }
}
