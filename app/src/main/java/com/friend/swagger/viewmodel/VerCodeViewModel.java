package com.friend.swagger.viewmodel;

import com.friend.swagger.entity.VerCode;
import com.friend.swagger.repository.VerCodeRepository;
import androidx.lifecycle.ViewModel;

/**
 * @Author ZhangWenXuan
 * @Date 2020-02-26 14:39
 **/
public class VerCodeViewModel extends ViewModel {
    private VerCodeRepository verCodeRepository;
    private VerCode verCode;
    public void init(){
        verCodeRepository = VerCodeRepository.getInstance();
    }

    public VerCode getVerCode() {
        verCode = verCodeRepository.getVerCode();
        return verCode;
    }

    /**
     * 请求验证码
     * @param phone
     */
    public void postVerCode(String phone) {
        verCodeRepository.postCode(phone);
    }
}
