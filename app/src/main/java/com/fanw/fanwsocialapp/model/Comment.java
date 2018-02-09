package com.fanw.fanwsocialapp.model;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by fanw on 2018/2/9.
 */

public class Comment implements Serializable {
    private int comment_id;
    private Essay essay;
    private User user;
    private String comment_content;
    private Date comment_date;

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }
}
