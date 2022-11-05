package com.loadapp.load.api;

public class Api {

    private static final String HOST = "http://api.chucard.com";
    //验证服务是否存活
    public static final String CHECK_SERVER_ALIVE = HOST + "/api/v1/account/monitor_live";
    //验证手机号码是否注册
    public static final String CHECK_MOBILE = HOST + "/api/v1/account/check_mobile";
    //发送短信验证码(获取验证码)
    public static final String GET_SMS_CODE = HOST + "/api/v1/account/send_sms_otp";
    //验证验证码
    public static final String CHECK_SMS_CODE = HOST + "/api/v1/account/check_otp_code";
    //注册账号
    public static final String REGISTER = HOST + "/api/v1/account/register";
    //登录账号
    public static final String LOGIN = HOST + "/api/v1/account/login";
    //登出账号
    public static final String LOGOUT = HOST + "/api/v1/account/logout";
    //修改密码
    public static final String MODIFY_PSD = HOST + "/api/v1/account/modify_password";
    //上传fcm token
    public static final String UPLOAD_FCM_TOKEN = HOST + "/api/v1/account/upload_fcmtoken";

}
