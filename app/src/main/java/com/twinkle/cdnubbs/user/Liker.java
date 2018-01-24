package com.twinkle.cdnubbs.user;

import cn.bmob.v3.BmobObject;


public class Liker extends BmobObject {

    private User liker;
    private User fans;

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }

    public User getFans() {
        return fans;
    }

    public void setFans(User fans) {
        this.fans = fans;
    }
}
