package com.lk.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
    public static boolean checkVerifyCode(HttpServletRequest request){
        String verifyCodeExpected = ((String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY)).toLowerCase();
        String verifyCodeActul = (HttpServletRequestUtil.getString(request,"verifyCodeActul")).toLowerCase();
        if (verifyCodeActul == null || !verifyCodeActul.equals(verifyCodeExpected)){
            return true;
        }
        return false;
    }
}
