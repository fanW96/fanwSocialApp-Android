package com.fanw.fanwsocialapp.model;

import java.io.Serializable;

/**
 * Created by fanw on 2018/2/7.
 */

public class VideoTopic implements Serializable {
    private String ename;
    private String tname;
    private String alias;
    private String topic_icons;
    private String tid;

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTopic_icons() {
        return topic_icons;
    }

    public void setTopic_icons(String topic_icons) {
        this.topic_icons = topic_icons;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
