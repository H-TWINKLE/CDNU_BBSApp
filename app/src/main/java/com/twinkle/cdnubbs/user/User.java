package com.twinkle.cdnubbs.user;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {

    private String name;
    private String time;
    private String pic;
    private String cdnu_id;
    private int level;
    private String pass;
    private String cdnu_pass;

    public String getCdnu_pass() {
        return cdnu_pass;
    }

    public void setCdnu_pass(String cdnu_pass) {
        this.cdnu_pass = cdnu_pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCdnu_id() {
        return cdnu_id;
    }

    public void setCdnu_id(String cdnu_id) {
        this.cdnu_id = cdnu_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
