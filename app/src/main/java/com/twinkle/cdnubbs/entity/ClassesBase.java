package com.twinkle.cdnubbs.entity;

public class ClassesBase {

    private int title;
    private int pic;

    public ClassesBase() {}

    public ClassesBase(int title, int pic) {
        this.title = title;
        this.pic = pic;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
