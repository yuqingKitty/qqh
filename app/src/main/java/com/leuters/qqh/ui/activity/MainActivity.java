package com.leuters.qqh.ui.activity;

import android.support.annotation.NonNull;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.leuters.qqh.R;
import com.leuters.qqh.data.entity.RxBusMessage;
import com.leuters.qqh.ui.base.BaseFragmentActivity;
import com.leuters.qqh.utils.ToastCompat;
import com.leuters.qqh.utils.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.leuters.qqh.data.commons.Constants.RXBUS_TO_COMPLETE_KEY;

public class MainActivity extends BaseFragmentActivity {
    private long backClickTime;
    /**
     * 首页 导航按钮
     */
    @BindView(R.id.home_btn)
    RadioButton mHomeBtn;
    /**
     * 我的 导航按钮
     */
    @BindView(R.id.mine_btn)
    RadioButton mMineBtn;
    /**
     * 贷款大全 导航按钮
     */
    @BindView(R.id.complete_btn)
    RadioButton mCompleteBtn;

    RxBus rxBus;

    @Override
    protected void initView() {
        fragments = new ArrayList<>();
        fragmentManager = getSupportFragmentManager();
        fragments.add(fragmentManager.findFragmentById(R.id.fm_home));
        fragments.add(fragmentManager.findFragmentById(R.id.fm_com));
        fragments.add(fragmentManager.findFragmentById(R.id.fm_mine));
        selectTab(0);
        mHomeBtn.setChecked(true);

        initRxBus();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @OnCheckedChanged({R.id.home_btn, R.id.mine_btn, R.id.complete_btn})
    void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.home_btn:
                    //点击首页
                    selectTab(0);
                    break;
                case R.id.complete_btn:
                    //点击贷超大全
                    selectTab(1);
                    break;
                case R.id.mine_btn:
                    //点击个人中心
                    selectTab(2);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //点击返回按钮
        if (System.currentTimeMillis() - backClickTime < 2000) {
            //返回
            super.onBackPressed();
        } else {
            ToastCompat.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        }
        backClickTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    private void initRxBus() {
        rxBus = RxBus.getInstanceBus();
        registerRxBus(RxBusMessage.class, new Consumer<RxBusMessage>() {
            @Override
            public void accept(@NonNull RxBusMessage rxBusMessage) {
                //根据事件类型进行处理
                switch (rxBusMessage.getCode()) {
                    case RXBUS_TO_COMPLETE_KEY:
                        mCompleteBtn.setChecked(true);
                        break;
                }
            }
        });
    }

    //注册
    public <T> void registerRxBus(Class<T> eventType, Consumer<T> action) {
        Disposable disposable = rxBus.doSubscribe(eventType, action, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        });
        rxBus.addSubscription(this, disposable);
    }

}
