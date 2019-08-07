package com.leuters.qqh.net;

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

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoanService {
    /**
     * 首页头部信息
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/pub/homepageInfo")
    Observable<HomeBean> getHomeData(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 产品列表
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/prod/list")
    Observable<HomeBean> getHomeProductList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 更新检测
     *
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/pub/sysUpdate")
    Observable<UploadBean> sysUpdated(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 贷款大全排序标签
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/prod/sortLabelList")
    Observable<CompleteBean> getLoanSortLabelList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 贷款大全
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/prod/list")
    Observable<CompleteBean> getLoanProductList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 首页类型页面-广告
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/pub/getAd")
    Observable<HomeTypeProductBean> getHomeTypeAdList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 首页类型页面-产品列表
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/prod/list")
    Observable<HomeTypeProductBean> getHomeTypeProductList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 获取验证码
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/pub/sendSMS")
    Observable<BaseBean> getSendSms(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 登陆
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/login")
    Observable<LoginBean> login(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 登出
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/logout")
    Observable<LoginBean> logout(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 查询用户信息
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/getUserInfo")
    Observable<LoginBean> getUserInfo(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 获取客服信息
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/getCusServ")
    Observable<ServiceBean> service(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 上传头像
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/UploadHeadIcon")
    Observable<BaseBean> uploadHeadIcon(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 用户点击统计
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("api/app/fast2/user/toStatistics")
    Observable<StatisticsBean> toStatistics(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 统计产品时长
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("api/app/fast2/user/toStayTime")
    Observable<BaseBean> toStayTime(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 修改昵称
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/editNickName")
    Observable<BaseBean> editNickname(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 申请记录
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/getProductAccessList")
    Observable<MyLoanRecordBean> getMyLoanRecordList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 申请记录-推荐列表
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/prod/list")
    Observable<MyLoanRecordBean> getMyRecommendProductList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 获取消息中心
     */
    @Headers({"Content-type:application/json;charset=UTF-8", "platform:ANDROID"})
    @POST("app/user/getPushMessage")
    Observable<MessageCenterBean> getMessageCenterList(@Header("source") String channel, @Header("uniqueNo") String uniqueNo, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

}
