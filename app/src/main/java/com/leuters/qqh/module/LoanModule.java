package com.leuters.qqh.module;

import android.app.Activity;

import com.leuters.qqh.application.BaseApplication;
import com.leuters.qqh.data.entity.BaseBean;
import com.leuters.qqh.data.entity.CompleteBean;
import com.leuters.qqh.data.entity.HomeBean;
import com.leuters.qqh.data.entity.HomeTypeProductBean;
import com.leuters.qqh.data.entity.LoginBean;
import com.leuters.qqh.data.entity.MessageCenterBean;
import com.leuters.qqh.data.entity.MyLoanRecordBean;
import com.leuters.qqh.data.entity.ServiceBean;
import com.leuters.qqh.data.entity.StatisticsBean;
import com.leuters.qqh.data.entity.UploadBean;
import com.leuters.qqh.data.entity.VerifyUserTokenBean;
import com.leuters.qqh.net.LoanService;
import com.leuters.qqh.net.MainFactory;
import com.leuters.qqh.utils.GsonUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.leuters.qqh.utils.Tools;

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
    private String uid;
    private String token;
    private String uniqueNo;

    public LoanModule(Activity context) {
        rxAppCompatActivity = (RxAppCompatActivity) context;
    }

    /**
     * 获取首页头部信息
     */
    public void getHomeData(Map<String, Object> params, Observer observer) {
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<HomeBean> homeObservable = LOAN_SERVICE.getHomeData(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
        homeObservable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    // 产品列表
    public void getHomeProductList(Map<String, Object> params, Observer observer) {
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<HomeBean> homeObservable = LOAN_SERVICE.getHomeProductList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<CompleteBean> loanObservable = LOAN_SERVICE.getLoanSortLabelList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
        loanObservable.subscribeOn(Schedulers.io())
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<CompleteBean> loanObservable = LOAN_SERVICE.getLoanProductList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
        loanObservable.subscribeOn(Schedulers.io())
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<BaseBean> observable = LOAN_SERVICE.getSendSms(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<LoginBean> observable = LOAN_SERVICE.login(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<LoginBean> observable = LOAN_SERVICE.logout(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<LoginBean> observable = LOAN_SERVICE.getUserInfo(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
    public void service(Map<String, Object> params, Observer observer) {
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<ServiceBean> observable = LOAN_SERVICE.service(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<BaseBean> observable = LOAN_SERVICE.uploadHeadIcon(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
    public void toStatisticClickProduct(Map<String, Object> params, Observer observer) {
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<StatisticsBean> observable = LOAN_SERVICE.toStatisticClickProduct(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<BaseBean> observable = LOAN_SERVICE.toStayTime(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<UploadBean> observable = LOAN_SERVICE.sysUpdated(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<BaseBean> observable = LOAN_SERVICE.editNickname(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<MyLoanRecordBean> observable = LOAN_SERVICE.getMyLoanRecordList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<MyLoanRecordBean> observable = LOAN_SERVICE.getMyRecommendProductList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<MessageCenterBean> observable = LOAN_SERVICE.getMessageCenterList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<HomeTypeProductBean> observable = LOAN_SERVICE.getHomeTypeAdList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
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
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<HomeTypeProductBean> observable = LOAN_SERVICE.getHomeTypeProductList(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * token验证
     * @param params
     * @param observer
     */
    public void getTokenState(Map<String, Object> params, Observer observer) {
        uid = BaseApplication.getUserId(rxAppCompatActivity);
        token = BaseApplication.getToken(rxAppCompatActivity);
        uniqueNo = Tools.getDeviceId();
        Observable<VerifyUserTokenBean> observable = LOAN_SERVICE.getTokenState(BaseApplication.CHANNEL, uniqueNo, uid, token, getRequestBody(params));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

}
