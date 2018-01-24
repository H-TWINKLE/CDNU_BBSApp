package com.twinkle.cdnubbs.user;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
    private String content;
    private User user;
    private Post post;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
