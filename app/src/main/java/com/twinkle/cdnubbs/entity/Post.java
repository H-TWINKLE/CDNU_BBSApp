package com.twinkle.cdnubbs.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;


public class Post extends BmobObject {

    private String title;          //1
    private String content;           //4
    private User author;         //2
    private List<String> img;         //6
    private Integer hot;         //5
    private Integer commentnum;           //3
    private String local;        //7
    private Integer praisenum;        //9
    private BmobRelation praise;         //8
    private List<Integer> acttype;         //10

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

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

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
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
        return "title:" + getTitle() + "\nauthor:" + getAuthor().getName() + "\npic:" + getAuthor().getPic()
                + "\ntime:" + getCreatedAt() + "\nimg:" + getImg() + "\ncontent:" + getContent();
    }
}
