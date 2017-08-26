package com.xiaoluogo.goodtochat.utils;

/**
 * Created by xiaoluogo on 2017/7/23.
 * Email: angel-lwl@126.com
 */
public class Constants {
    //好友请求：未读-未添加->接收到别人发给我的好友添加请求，初始状态
    public static final int STATUS_VERIFY_NONE = 0;
    //好友请求：已读-未添加->点击查看了新朋友，则都变成已读状态
    public static final int STATUS_VERIFY_READED = 2;
    //好友请求：已添加
    public static final int STATUS_VERIFIED = 1;
    //好友请求：拒绝
    public static final int STATUS_VERIFY_REFUSE = 3;
    //好友请求：我发出的好友请求-暂未存储到本地数据库中
    public static final int STATUS_VERIFY_ME_SEND = 4;
    /**
     * 用于意图进入信息页面发送消息标签
     */
    public static final String INFO_PAGER = "info_pager";
    /**
     * 来自左侧菜单的点击进入信息页面
     */
    public static final int MYSELF_INFO_FROM_LEFTMENU = 1;
    /**
     * 来自我的头像的点击进入信息页面
     */
    public static final int MYSELF_INFO_FROM_MYHEADER = 2;
    /**
     * 来自搜索页面的点击进入信息页面
     */
    public static final int FRIEND_INFO_FROM_SEARCH = 3;
    /**
     * 来自好有头像和通讯录的点击进入信息页面
     */
    public static final int FRIEND_INFO_FROM_HEADER = 4;
    /**
     * 来自通知栏的点击进入信息页面
     */
    public static final int FRIEND_INFO_FROM_NOTIFY = 5;
    /**
     * 是否在InfoActivity页面
     */
    public static boolean IS_IN_CHAT_ACTIVITY = false;

    /**
     * 打开相机
     */
    public static final int TAKE_PHOTO = 1;
    /**
     * 相册中选择图片
     */
    public static final int CHOOSE_PHOTO = 2;

    public static final String BASE_WEATHER_URL = "http://apicloud.mob.com/v1/weather/citys?key=";
    public static final String MOB_APPKEY = "1f84fd9ff30ec";

    public static final String WEATHER_URL ="http://apicloud.mob.com/v1/weather/query?key="+MOB_APPKEY+"&city=";
    public static final String WEATHER_CITY = BASE_WEATHER_URL + MOB_APPKEY;
}
