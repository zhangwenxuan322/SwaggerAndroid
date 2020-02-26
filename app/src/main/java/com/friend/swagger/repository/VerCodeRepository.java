package com.friend.swagger.repository;

import com.friend.swagger.api.VerCodeApi;
import com.friend.swagger.common.Constant;
import com.friend.swagger.entity.VerCode;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-26 14:39
 **/
public class VerCodeRepository {
    private VerCodeApi verCodeApi;
    private VerCode verCode;
    private static VerCodeRepository verCodeRepository;

    public static VerCodeRepository getInstance(){
        if (verCodeRepository == null){
            verCodeRepository = new VerCodeRepository();
        }
        return verCodeRepository;
    }

    private VerCodeRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.localUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        verCodeApi = retrofit.create(VerCodeApi.class);
    }

    public VerCode getVerCode() {
        return verCode;
    }

    /**
     * 请求验证码
     * @param phone
     */
    public void postCode(String phone) {
        Call<VerCode> call = verCodeApi.getCode(phone);
        call.enqueue(new Callback<VerCode>() {
            @Override
            public void onResponse(Call<VerCode> call, Response<VerCode> response) {
                if (response.isSuccessful() && response.body() != null) {
                    verCode = response.body();
                }
            }

            @Override
            public void onFailure(Call<VerCode> call, Throwable t) {
                verCode = new VerCode();
            }
        });
    }
}
