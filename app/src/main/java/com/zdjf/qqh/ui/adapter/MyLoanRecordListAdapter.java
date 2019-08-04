package com.zdjf.qqh.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zdjf.qqh.R;
import com.zdjf.qqh.data.entity.MyLoanRecordBean;
import com.zdjf.qqh.utils.FormatUtil;
import com.zdjf.qqh.utils.GlideImageLoader;

import java.util.List;

public class MyLoanRecordListAdapter extends BaseAdapter<MyLoanRecordBean.MyLoanBean> {
    private Context mContext;

    public MyLoanRecordListAdapter(Context context, @Nullable List<MyLoanRecordBean.MyLoanBean> data) {
        super(R.layout.adapter_my_loan_record, data);
        mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, MyLoanRecordBean.MyLoanBean item) {
        helper.setText(R.id.tv_loan_name, item.name)
                .setText(R.id.tv_loan_time, FormatUtil.long2StrDate(item.createDate));
        GlideImageLoader.setImg(mContext, item.logoUrl, (ImageView) helper.getView(R.id.iv_loan_icon),
                R.mipmap.icon_product_default, R.mipmap.icon_product_default);
    }

}
