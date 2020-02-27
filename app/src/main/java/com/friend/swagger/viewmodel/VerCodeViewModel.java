package com.friend.swagger.viewmodel;

import com.friend.swagger.entity.VerCode;
import com.friend.swagger.repository.VerCodeRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-26 14:39
 **/
public class VerCodeViewModel extends ViewModel {
    private VerCodeRepository verCodeRepository;
    private LiveData<VerCode> verCode;
    public void init(){
        verCodeRepository = VerCodeRepository.getInstance();
    }

    public LiveData<VerCode> getVerCode(String phone) {
        verCodeRepository.getCode(phone);
        verCode = verCodeRepository.getVerCode();
        return verCode;
    }
}
