package com.lld360.cnc.core;

/**
 * Author: dhc
 * Date: 2016-06-29 13:50
 */
public class Const {

    // Session Key
    public static final String SS_USER = "user";

    // Cookie
    public static final int REMEMBER_ME_TIME = 90 * 24 * 60 * 60;   // 记住帐号过期时间（90天）

    // File belone
    public static final String FILE_USER = "user";

    //third_account type
    public static final int Third_ACCOUNT_TYPE_QQ = 1;//QQ
    public static final int Third_ACCOUNT_TYPE_WEIXIN = 2;//微信

    //third_account agent
    public static final byte Third_ACCOUNT_GENDER_MAN = 1;//男
    public static final byte Third_ACCOUNT_GENDER_WOMAN = 0;//女

    // Page Parameters
    public static final String PAGE_SIZE = "size";
    public static final String PAGE_PAGE = "page";
    public static final String PAGE_TIME = "time";
    public static final String PAGE_LIMIT = "limit";
    public static final String PAGE_OFFSET = "offset";

    // ServletContext Keys
    public static final String CTX_KEY_EXPIRE_TIME_SUFFIX = ":EXPIRE_TIME";
    public static final String CTX_KEY_WXGZH_ACCESS_TOKEN = "WXGZH:ACCESS_TOKEN";
    public static final String CTX_KEY_WXGZH_JSAPI_TICKET = "WXGZH:JSAPI_TICKET";
    public static final String CTX_KEY_CNC_DOWNLOAD = "CNC_DATA_DOWNLOAD";

    // File directory
    public static final String FILE_TEMP_PATH = "temp/";
    public static final String FILE_PREFIX_PATH = "files/";

    // User State
    public static final byte USER_STATE_NORMAL = 1;
    public static final byte USER_STATE_DELETED = -1;

    //Valid Type
    public static final byte SMS_REGIST = 1;    //注册
    public static final byte SMS_RESETPWD = 2;  //找回密码
    public static final byte SMS_FORGOTTPWD = 3;  //忘记密码
    public static final byte SMS_BINDACCOUNT = 4;  //绑定手机号

    public static final String SMS_RESETPASSWORD_TYPE_OK = "OK";  //手机验证成功

    // Doc allow types
    public static final String DOC_ALLOW_TYPES = "pdf,doc,docx,xls,xlsx,ppt,pptx,txt,jpg,jpeg,png,bmp,gif,ico";

    // doc  State
    public static final byte DOC_STATE_NORMAL = 1;//正常
    public static final byte DOC_STATE_LOADING = 2;//处理中
    public static final byte DOC_STATE_DELETE = 9;//删除

    // doc  uploaderType
    public static final byte DOC_UPLOADERTYPE_ADMIN = 1;    //管理员
    public static final byte DOC_UPLOADERTYPE_USER = 2;     //用户

    public static final int DOC_UPLOAD_SCORE = 2;  // 上传文档积分
    public static final int DOC_IMG_PAGES = 50;  // 文档生成图片最大张数

    // System setting keys
    public static final String SETTING_CNC_ARTICLE_LAST_ID = "LAST_ARTICLE_ID_OF_CNCENGINEER";
    public static final String SETTING_CNC_HELP_MPAGE_CONTENT = "CNC_HELP_MPAGE_CONTENT";
    public static final String SETTING_CNC_WXGZH_CXHFYS = "CNC_WXGZH_CXHFYS";


    // User score history type
    public static final byte USER_SCORE_HISTORY_TYPE_UPLOAD = 1;
    public static final byte USER_SCORE_HISTORY_TYPE_DOWNLOAD = 2;
    public static final byte USER_SCORE_HISTORY_TYPE_SALE = 3;
    public static final byte USER_SCORE_HISTORY_TYPE_REGIEST = 4;   //注册
}
