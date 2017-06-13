package com.jifenke.lepluslive.global.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";

    public static final String APPID = "wx16edfa0dda02edd5";  //线上
    public static final String WEI_XIN_ROOT_URL = "http://www.lepluspay.com"; //线上
    public static final String ORDER_PRINTER_REQUEST_URL = "http://www.lepluslife.com/manage/addReceipt"; //线上

//    public static final String APPID = "wxec4f3a2fb6ee8f06";    //测试
//    public static final String WEI_XIN_ROOT_URL = "http://www.tiegancrm.com";   //测试
//    public static final String ORDER_PRINTER_REQUEST_URL = "http://www.tiegancrm.com/manage/addReceipt"; //测试

    public static final Integer COOKIE_DISABLE_TIME = 604800;

    private Constants() {
    }
}