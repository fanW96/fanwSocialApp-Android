package com.fanw.fanwsocialapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fanw on 2018/2/9.
 */

public class Essay implements Serializable {
    private int essay_id;
    private int essay_pic_count;
    private String essay_content;
    private User user;
    private int essay_thumbs;
    private String essay_pic_1;
    private String essay_pic_2;
    private String essay_pic_3;
    private Date essay_date;

    public Date getEssay_date() {
        return essay_date;
    }

    public void setEssay_date(Date essay_date) {
        this.essay_date = essay_date;
    }

    public String getEssay_pic_1() {
        return essay_pic_1;
    }

    public void setEssay_pic_1(String essay_pic_1) {
        this.essay_pic_1 = essay_pic_1;
    }

    public String getEssay_pic_2() {
        return essay_pic_2;
    }

    public void setEssay_pic_2(String essay_pic_2) {
        this.essay_pic_2 = essay_pic_2;
    }

    public String getEssay_pic_3() {
        return essay_pic_3;
    }

    public void setEssay_pic_3(String essay_pic_3) {
        this.essay_pic_3 = essay_pic_3;
    }

    public int getEssay_thumbs() {
        return essay_thumbs;
    }

    public void setEssay_thumbs(int essay_thumbs) {
        this.essay_thumbs = essay_thumbs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getEssay_id() {
        return essay_id;
    }

    public void setEssay_id(int essay_id) {
        this.essay_id = essay_id;
    }

    public int getEssay_pic_count() {
        return essay_pic_count;
    }

    public void setEssay_pic_count(int essay_pic_count) {
        this.essay_pic_count = essay_pic_count;
    }

    public String getEssay_content() {
        return essay_content;
    }

    public void setEssay_content(String essay_content) {
        this.essay_content = essay_content;
    }
}
