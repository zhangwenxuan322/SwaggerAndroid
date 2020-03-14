package com.friend.swagger.api;

import com.friend.swagger.entity.UserProfile;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-23 11:21
 **/
public interface UserApi {
    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    @GET("user/phone/{phone}")
    Call<Map<String, Object>> getUserByPhone(@Path("phone") String phone);

    /**
     * 根据swaggerid查询用户信息
     * @param swaggerId
     * @return
     */
    @GET("user/swaggerId/{swaggerId}")
    Call<Map<String, Object>> getUserBySwaggerId(@Path("swaggerId") String swaggerId);

    /**
     * 用户注册
     * @param userProfile
     * @return
     */
    @POST("user")
    Call<Map<String, Object>> userRegister(@Body UserProfile userProfile);

    /**
     * 用户登录
     * @param account
     * @param password
     * @param ip
     * @param place
     * @return
     */
    @GET("user/auth")
    Call<Map<String, Object>> userLogin(@Query("account") String account,
                                        @Query("password") String password,
                                        @Query("ip") String ip,
                                        @Query("place") String place);

    /**
     * 修改密码
     * @param phone
     * @param password
     * @return
     */
    @PUT("user/password")
    Call<Map<String, Object>> changePassword(@Query("phone") String phone,
                                             @Query("password") String password);

    /**
     * 上传用户头像
     * @param file
     * @param filename
     * @return
     */
    @Multipart
    @POST("user/portrait")
    Call<Map<String, Object>> uploadPortrait(@Part MultipartBody.Part file,
                                             @Part("filename") RequestBody filename);

    /**
     * 下载用户头像
     * @param fileName
     * @return
     */
    @GET("user/portrait/{fileName}")
    Call<ResponseBody> downloadPortrait(@Path("fileName") String fileName);
}
