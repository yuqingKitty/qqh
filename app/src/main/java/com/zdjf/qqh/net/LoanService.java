package com.zdjf.qqh.net;

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
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/pub/homepageInfo")
    Observable<HomeBean> getHomeData(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 产品列表
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/prod/list")
    Observable<HomeBean> getHomeProductList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 更新检测
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/pub/sysUpdate")
    Observable<UploadBean> sysUpdated(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 贷款大全排序标签
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/prod/sortLabelList")
    Observable<CompleteBean> getLoanSortLabelList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 贷款大全
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/prod/list")
    Observable<CompleteBean> getLoanProductList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 获取验证码
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/pub/sendSMS")
    Observable<BaseBean> getSendSms(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 登陆
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/user/login")
    Observable<LoginBean> login(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 登出
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/user/logout")
    Observable<LoginBean> logout(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 查询用户信息
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("app/user/getUserInfo")
    Observable<LoginBean> getUserInfo(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 获取客服信息
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/cs")
    Observable<ServiceBean> service(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token);

    /**
     * 上传头像
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/UploadHeadIcon")
    Observable<BaseBean> uploadHeadIcon(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 用户点击统计
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/toStatistics")
    Observable<StatisticsBean> toStatistics(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 统计产品时长
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/toStayTime")
    Observable<BaseBean> toStayTime(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 修改昵称
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/editNickname")
    Observable<BaseBean> editNickname(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 申请记录
     * @param channel
     * @param userId
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/myLoanRecordList")
    Observable<MyLoanRecordBean> getMyLoanRecordList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 申请记录-推荐列表
     * @param channel
     * @param userId
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/myRecommendProductList")
    Observable<MyLoanRecordBean> getMyRecommendProductList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 获取消息中心
     * @param channel
     * @param userId
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/getMessageList")
    Observable<MessageCenterBean> getMessageCenterList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);


    /**
     * 首页类型页面-广告
     * @param channel
     * @param userId
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/getHomeTypeAdList")
    Observable<HomeTypeProductBean> getHomeTypeAdList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

    /**
     * 首页类型页面-产品列表
     * @param channel
     * @param userId
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/getHomeTypeProductList")
    Observable<HomeTypeProductBean> getHomeTypeProductList(@Header("source") String channel, @Header("uid") String userId, @Header("token") String token, @Body RequestBody body);

}
