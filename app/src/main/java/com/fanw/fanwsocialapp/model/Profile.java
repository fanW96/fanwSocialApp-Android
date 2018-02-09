package com.fanw.fanwsocialapp.model;

/**
 * Created by fanw on 2018/2/9.
 */

public class Profile {

    private int profile_id;
    private String profile_sign;
    private String profile_location;
    private boolean profile_sex;
    private User user;//user_id的外键约束

    @Override
    public String toString() {
        return "Profile{" +
                "profile_id=" + profile_id +
                ", profile_sign='" + profile_sign + '\'' +
                ", profile_location='" + profile_location + '\'' +
                ", profile_sex=" + profile_sex +
                ", user=" + user.toString() +
                '}';
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }

    public String getProfile_sign() {
        return profile_sign;
    }

    public void setProfile_sign(String profile_sign) {
        this.profile_sign = profile_sign;
    }

    public String getProfile_location() {
        return profile_location;
    }

    public void setProfile_location(String profile_location) {
        this.profile_location = profile_location;
    }

    public boolean isProfile_sex() {
        return profile_sex;
    }

    public void setProfile_sex(boolean profile_sex) {
        this.profile_sex = profile_sex;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
