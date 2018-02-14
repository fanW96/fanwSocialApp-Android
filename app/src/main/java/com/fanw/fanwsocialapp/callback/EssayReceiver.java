package com.fanw.fanwsocialapp.callback;

import com.fanw.fanwsocialapp.model.Comment;
import com.fanw.fanwsocialapp.model.Essay;
import com.fanw.fanwsocialapp.model.Profile;
import com.fanw.fanwsocialapp.model.Relation;
import com.fanw.fanwsocialapp.model.User;

import java.io.Serializable;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

/**
 * Created by fanw on 2018/2/9.
 */

public class EssayReceiver implements Serializable {
    private int code;
    private String msg;
    private User user;
    private List<Relation> relationList;
    private int count;
    private Profile profile;
    private Essay essay;
    private List<Essay> essayList;
    private List<Comment> commentList;
    private Comment comment;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Relation> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<Relation> relationList) {
        this.relationList = relationList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Essay getEssay() {
        return essay;
    }

    public void setEssay(Essay essay) {
        this.essay = essay;
    }

    public List<Essay> getEssayList() {
        return essayList;
    }

    public void setEssayList(List<Essay> essayList) {
        this.essayList = essayList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
