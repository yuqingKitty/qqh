package com.leuters.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.leuters.qqh.R;
import com.leuters.qqh.data.entity.MessageCenterBean;
import com.leuters.qqh.utils.GlideImageLoader;

import java.util.List;

/**
 * 消息中心适配器
 */
public class MessageCenterAdapter extends BaseAdapter<MessageCenterBean.MessageBean> {
    private Context mContext;

    public MessageCenterAdapter(Context context, @Nullable List<MessageCenterBean.MessageBean> data) {
        super(R.layout.adapter_message_center, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, MessageCenterBean.MessageBean item) {
        helper.setText(R.id.tv_message_title, item.title)
                .setText(R.id.tv_message_label, item.label)
                .setText(R.id.tv_message_sub_title, item.summary)
                .setText(R.id.tv_message_desc, item.content);
        helper.setGone(R.id.tv_message_desc, false);

        GlideImageLoader.setRoundedCorner(mContext, item.iconURL, (ImageView) helper.getView(R.id.iv_message_icon), 10);


        helper.getView(R.id.iv_message_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.getView(R.id.tv_message_desc).getVisibility() == View.VISIBLE) {
                    // 显示到不显示 down
                    helper.setImageResource(R.id.iv_message_arrow, R.mipmap.icon_arrow_right);
                    helper.setGone(R.id.tv_message_desc, false);
                } else {
                    // 不显示到显示 up
                    helper.setImageResource(R.id.iv_message_arrow, R.mipmap.icon_arrow_right);
                    helper.setGone(R.id.tv_message_desc, true);
                }
            }
        });
    }
}
