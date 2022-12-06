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
    //获取基本信息
    public static final String GET_CONFIG = HOST + "/api/v1/account/base_profile_config";
    //获取客户信息
    public static final String GET_PROFILE = HOST + "/api/v1/account/get_profile";
    //上传客户基本信息
    public static final String UPLOAD_BASE = HOST + "/api/v1/account/upload_base";
    //上传联系人信息
    public static final String UPLOAD_CONTACT = HOST + "/api/v1/account/upload_contact";
    //上传证件相关信息
    public static final String UPLOAD_IDENTITY = HOST + "/api/v1/account/upload_identity";
    //获取银行列表
    public static final String GET_BANK_LIST = HOST + "/api/v1/account/get_bank_list";
    //上传银行账号信息
    public static final String UPLOAD_BANK = HOST + "/api/v1/account/upload_bank";
    //上传客户端信息
    public static final String UPLOAD_CLIENT_INFO = HOST + "/api/v1/account/upload_client_info";
    //产品试算
    public static final String PRODUCT_TRIAL = HOST + "/api/v1/product/trial";
    //产品列表
    public static final String GET_PRODUCT_LIST = HOST + "/api/v1/product/list";
    //获取订单详情
    public static final String GET_ORDER_INFO = HOST + "/api/v1/order/info";
    //验证客户是否可以借贷
    public static final String CHECK_CAN_ORDER = HOST + "/api/v1/order/check";
    //申请订单
    public static final String ORDER_APPLY = HOST + "/api/v1/order/apply";

    public static final String WEB_VIEW_POLICY = HOST + "/html/Privacy.html";

    public static final String WEB_VIEW_TERM = HOST + "/html/terms.html";

}
