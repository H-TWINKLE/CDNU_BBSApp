package com.twinkle.cdnubbs.user;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;


public class Post extends BmobObject {

    private String title;
    private String content;
    private User author;
    private String img;
    private Integer hot;
    private Integer commentnum;

    private Integer praisenum;
    private  BmobRelation praise;
    private List<Integer> acttype;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

    public Integer getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(Integer praisenum) {
        this.praisenum = praisenum;
    }

    public BmobRelation getPraise() {
        return praise;
    }

    public void setPraise(BmobRelation praise) {
        this.praise = praise;
    }

    public List<Integer> getActtype() {
        return acttype;
    }

    public void setActtype(List<Integer> acttype) {
        this.acttype = acttype;
    }

    @Override
    public String toString() {
        return "title:"+getTitle()+"\nauthor:"+getAuthor().getName()+"\npic:"+getAuthor().getPic()
                +"\ntime:"+getCreatedAt()+"\nimg:"+getImg()+"\ncontent:"+getContent();
    }
}
