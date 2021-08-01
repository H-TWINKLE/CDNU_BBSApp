package com.twinkle.cdnubbs.entity;

import cn.bmob.v3.BmobObject;

public class Praise extends BmobObject {
    private Post post;
    private User user;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
