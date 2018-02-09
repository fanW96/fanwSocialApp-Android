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
}
