package com.twinkle.cdnubbs.utils;


import com.twinkle.cdnubbs.R;

public interface Init {

    public static final String now_time = String.valueOf(System.currentTimeMillis());
    public static final int[] lv_info = new int[]{R.string.mess, R.string.comment, R.string.post, R.string.coll, R.string.praise};
    public static final String[] lv_admin = new String[]{"头像", "账户", "昵称", "电话号码", "注册时间", "等级", "修改密码"};
    public static final String ok = "确定";
    public static final String cancel = "取消";
    public static final String ExitApp = "ExitApp";
    public static final String UpdateInfo = "UpdateInfo";
    public static final String UpdateMess = "UpdateMess";
    public static final String new_admin = "新用户";
    public static final String success_update_pic  = "图片上传成功";
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final String timeout = "处理超时，请稍后再试";
    public static final int[] classes = new int[]{R.drawable.shtc, R.drawable.xxhd, R.drawable.qzzx, R.drawable.qbbk};
    public static final int[] classes_title = new int[]{R.string.shtc,R.string.xxhd,R.string.qzzx,R.string.qbbk};
}
