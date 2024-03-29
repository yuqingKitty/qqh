package com.zdjf.qqh.net;

import com.zdjf.qqh.data.entity.BaseBean;
import com.zdjf.qqh.data.entity.CompleteProductBean;
import com.zdjf.qqh.data.entity.HomeBean;
import com.zdjf.qqh.data.entity.LoginBean;
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
     * 首页
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/info")
    Observable<HomeBean> getHomeData(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 贷款大全
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/product/getProductList")
    Observable<CompleteProductBean> getLoanProductList(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 产品列表
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/getProductList")
    Observable<HomeBean> getProductList(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 注册
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/register")
    Observable<BaseBean> register(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 获取验证码
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/getSendSms")
    Observable<BaseBean> getSendSms(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 登陆
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/login")
    Observable<LoginBean> login(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 登出
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/logout")
    Observable<LoginBean> logout(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 查询用户信息
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/getUserInfo")
    Observable<LoginBean> getUserInfo(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 修改密码
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/editPassword")
    Observable<BaseBean> editPassword(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 重置密码
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/resetPassword")
    Observable<BaseBean> resetPassword(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 各个协议文案查询
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/cms")
    Observable<ProtocolBean> cms(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 获取客服信息
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/cs")
    Observable<ServiceBean> service(@Header("source") String channel, @Header("user_id") String userId);

    /**
     * 上传头像
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/UploadHeadIcon")
    Observable<BaseBean> uploadHeadIcon(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 提交反馈
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/getFeedBack")
    Observable<BaseBean> feedBack(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 用户点击统计
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/toStatistics")
    Observable<StatisticsBean> toStatistics(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);

    /**
     * 统计产品时长
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/toStayTime")
    Observable<BaseBean> toStayTime(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);


    /**
     * 更新检测
     *
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/pub/sysUpdated")
    Observable<UploadBean> sysUpdated(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);
    /**
     * 修改昵称
     *
     * @param body
     * @return
     */
    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("api/app/fast2/user/editNickname")
    Observable<BaseBean> editNickname(@Header("source") String channel, @Header("user_id") String userId, @Body RequestBody body);
}
