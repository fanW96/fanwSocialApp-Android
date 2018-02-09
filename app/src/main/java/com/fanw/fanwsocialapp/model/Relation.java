package com.fanw.fanwsocialapp.model;

import java.io.Serializable;

/**
 * Created by fanw on 2018/2/9.
 */

public class Relation implements Serializable {

    private int relation_id;
    private User up;
    private User fan;

    public String toFansString() {
        return "Relation{" +
                "relation_id=" + relation_id +
                ", fan=" + fan.toString() +
                '}';
    }

    public String toUpsString() {
        return "Relation{" +
                "relation_id=" + relation_id +
                ", up=" + up.toString() +
                '}';
    }

    public int getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(int relation_id) {
        this.relation_id = relation_id;
    }

    public User getUp() {
        return up;
    }

    public void setUp(User up) {
        this.up = up;
    }

    public User getFan() {
        return fan;
    }

    public void setFan(User fan) {
        this.fan = fan;
    }
}
