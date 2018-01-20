package com.twinkle.cdnubbs.java.utils;


public interface Init {

    public static final String now_time = String.valueOf(System.currentTimeMillis());
    public static final String[] lv_info = new String[]{"我的消息", "我的评论", "我的帖子", "我的收藏", "我的赞"};
    public static final String[] lv_admin = new String[]{"头像", "账户", "昵称", "电话号码", "注册时间", "等级", "修改密码"};
    public static final String ok = "确定";
    public static final String cancel = "取消";
    public static final String ExitApp = "ExitApp";
    public static final String UpdateInfo = "UpdateInfo";
    public static final String new_admin = "新用户";
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final String timeout = "处理超时，请稍后再试";
}
