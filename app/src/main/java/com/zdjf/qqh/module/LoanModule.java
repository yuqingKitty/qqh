package com.zdjf.qqh.module;

import android.app.Activity;

import com.zdjf.qqh.application.BaseApplication;
import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.data.entity.CompleteBean;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.data.entity.HomeTypeProductBean;
import com.zdjf.qqh.data.entity.LoginBean;
import com.zdjf.qqh.data.entity.MessageCenterBean;
import com.zdjf.qqh.data.entity.MyLoanRecordBean;
import com.zdjf.qqh.data.entity.ProtocolBean;
import com.zdjf.qqh.data.entity.ServiceBean;
import com.zdjf.qqh.data.entity.StatisticsBean;
import com.zdjf.qqh.data.entity.UploadBean;
import com.zdjf.qqh.net.LoanService;
import com.zdjf.qqh.net.MainFactory;
import com.zdjf.qqh.utils.GsonUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoanModule {
    public static final LoanService LOAN_SERVICE = MainFactory.getComicServiceInstance();
    private RxAppCompatActivity rxAppCompatActivity;

    public LoanModule(Activity context) {
        rxAppCompatActivity = (RxAppCompatActivity) context;
    }

    /**
     * 获取首页头部信息
     */
    public void getHomeData(Map<String, Object> params, Observer observer) {
        Observable<HomeBean> homeObservable = LOAN_SERVICE.getHomeData(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        homeObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    // 产品列表
    public void getHomeProductList(Map<String, Object> params, Observer observer) {
        Observable<HomeBean> homeObservable = LOAN_SERVICE.getHomeProductList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        homeObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 贷款大全排序标签
     *
     * @param params
     * @param observer
     */
    public void getLoanSortLabelList(Map<String, Object> params, Observer observer) {
        Observable<CompleteBean> homeObservable = LOAN_SERVICE.getLoanSortLabelList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        homeObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 贷款大全
     *
     * @param params
     * @param observer
     */
    public void getLoanProductList(Map<String, Object> params, Observer observer) {
        Observable<CompleteBean> homeObservable = LOAN_SERVICE.getLoanProductList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        homeObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 注册
     *
     * @param observer
     */
    public void userRegister(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> registerObservable = LOAN_SERVICE.register(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        registerObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 获取验证码
     *
     * @param params
     * @param observer
     */
    public void sendSms(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> observable = LOAN_SERVICE.getSendSms(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 登陆
     *
     * @param params
     * @param observer
     */
    public void userLogin(Map<String, Object> params, Observer observer) {
        Observable<LoginBean> observable = LOAN_SERVICE.login(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 登出
     *
     * @param params
     * @param observer
     */
    public void logout(Map<String, Object> params, Observer observer) {
        Observable<LoginBean> observable = LOAN_SERVICE.logout(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 用户信息
     *
     * @param params
     * @param observer
     */
    public void getUserInfo(Map<String, Object> params, Observer observer) {
        Observable<LoginBean> observable = LOAN_SERVICE.getUserInfo(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 修改密码
     *
     * @param params
     * @param observer
     */
    public void changePwd(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> observable = LOAN_SERVICE.editPassword(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 重置密码
     *
     * @param params
     * @param observer
     */
    public void resetPassword(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> observable = LOAN_SERVICE.resetPassword(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 获取协议
     *
     * @param params
     * @param observer
     */
    public void cms(Map<String, Object> params, Observer observer) {
        Observable<ProtocolBean> observable = LOAN_SERVICE.cms(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 客服信息
     *
     * @param observer
     */
    public void service(Observer observer) {
        Observable<ServiceBean> observable = LOAN_SERVICE.service(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 上传头像
     *
     * @param observer
     */
    public void uploadHead(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> observable = LOAN_SERVICE.uploadHeadIcon(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 提交反馈
     *
     * @param observer
     */
    public void submitFeedBack(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> observable = LOAN_SERVICE.feedBack(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 用户点击统计
     *
     * @param params
     * @param observer
     */
    public void toStatistics(Map<String, Object> params, Observer observer) {
        Observable<StatisticsBean> observable = LOAN_SERVICE.toStatistics(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 统计时长
     *
     * @param params
     * @param observer
     */
    public void toStayTime(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> observable = LOAN_SERVICE.toStayTime(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 更新检测
     *
     * @param params
     * @param observer
     */
    public void sysUpdated(Map<String, Object> params, Observer observer) {
        Observable<UploadBean> observable = LOAN_SERVICE.sysUpdated(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 修改用户名
     *
     * @param params
     * @param observer
     */
    public void setNickname(Map<String, Object> params, Observer observer) {
        Observable<BaseBean> observable = LOAN_SERVICE.editNickname(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    private static RequestBody getRequestBody(Map<String, Object> params) {
        return RequestBody.create(MediaType.parse("Content-Type, application/json"), GsonUtil.mapToJson(params));
    }

    /**
     * 申请记录
     * @param params
     * @param observer
     */
    public void getMyLoanRecordList(Map<String, Object> params, Observer observer) {
        Observable<MyLoanRecordBean> observable = LOAN_SERVICE.getMyLoanRecordList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 申请记录-推荐列表
     * @param params
     * @param observer
     */
    public void getMyRecommendProductList(Map<String, Object> params, Observer observer) {
        Observable<MyLoanRecordBean> observable = LOAN_SERVICE.getMyRecommendProductList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 消息中心
     * @param params
     * @param observer
     */
    public void getMessageCenterList(Map<String, Object> params, Observer observer) {
        Observable<MessageCenterBean> observable = LOAN_SERVICE.getMessageCenterList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 首页类型页面-广告
     * @param params
     * @param observer
     */
    public void getHomeTypeAdList(Map<String, Object> params, Observer observer) {
        Observable<HomeTypeProductBean> observable = LOAN_SERVICE.getHomeTypeAdList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * 首页类型页面-产品列表
     * @param params
     * @param observer
     */
    public void getHomeTypeProductList(Map<String, Object> params, Observer observer) {
        Observable<HomeTypeProductBean> observable = LOAN_SERVICE.getHomeTypeProductList(BaseApplication.CHANNEL, BaseApplication.getUserId(rxAppCompatActivity), getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

}
