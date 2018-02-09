package com.fanw.fanwsocialapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fanw on 2018/2/5.
 */

public class Video implements Serializable {

    /*
    {
            "sizeHD": 12901,
            "videoTopic": {
                "ename": "T1462429838217",
                "tname": "饭（重新注册）爱豆",
                "alias": "饭爱豆直击明星最新娱乐资讯",
                "topic_icons": "http://img2.cache.netease.com/m/newsapp/topic_icons/T1462429838217.png",
                "tid": "T1462429838217"
            },
            "mp4Hd_url": "http://flv3.bn.netease.com/videolib3/1802/07/miAFq6045/HD/miAFq6045-mobile.mp4",
            "description": "梁朝伟拍《欧洲攻略》险烧伤 唐嫣与其合作既期待又紧张",
            "title": "梁朝伟拍《欧洲攻略》险烧伤 唐嫣与其合作既期待又紧张",
            "mp4_url": "http://flv3.bn.netease.com/videolib3/1802/07/miAFq6045/SD/miAFq6045-mobile.mp4",
            "vid": "VJ94BRVNE",
            "cover": "http://vimg1.ws.126.net/image/snapshot/2018/2/N/F/VJ94BRVNF.jpg",
            "sizeSHD": 22234,
            "playersize": 1,
            "ptime": "2018-02-07 15:02:24",
            "m3u8_url": "http://flv.bn.netease.com/videolib3/1802/07/miAFq6045/SD/movie_index.m3u8",
            "topicImg": "http://vimg1.ws.126.net/image/snapshot/2016/4/A/M/VBKLOLMAM.jpg",
            "votecount": 0,
            "length": 183,
            "videosource": "新媒体",
            "m3u8Hd_url": "http://flv.bn.netease.com/videolib3/1802/07/miAFq6045/HD/movie_index.m3u8",
            "sizeSD": 7960,
            "topicSid": "VBKLOLMAK",
            "playCount": 0,
            "replyCount": 0,
            "replyBoard": "video_bbs",
            "replyid": "J94BRVNE050835RB",
            "topicName": "饭爱豆",
            "sectiontitle": "",
            "topicDesc": "饭爱豆直击明星最新娱乐资讯"
        }
    * */
    private int sizeHD;
    private VideoTopic videoTopic;
    private String mp4Hd_url;//hdurl
    private String description;
    private String title;
    private String mp4_url;//sdurl
    private String vid;
    private String cover;//video image
    private int sizeSHD;
    private int playersize;
    private String ptime;
    private String m3u8_url;
    private String topicImg;
    private int votecount;
    private int length;
    private String videosource;
    private String m3u8Hd_url;
    private int sizeSD;
    private String topicSid;
    private int playCount;
    private int replyCount;
    private String replyBoard;
    private String replyid;
    private String topicName;
    private String sectiontitle;
    private String topicDesc;

    public int getSizeHD() {
        return sizeHD;
    }

    public void setSizeHD(int sizeHD) {
        this.sizeHD = sizeHD;
    }

    public VideoTopic getVideoTopic() {
        return videoTopic;
    }

    public void setVideoTopic(VideoTopic videoTopic) {
        this.videoTopic = videoTopic;
    }

    public String getMp4Hd_url() {
        return mp4Hd_url;
    }

    public void setMp4Hd_url(String mp4Hd_url) {
        this.mp4Hd_url = mp4Hd_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMp4_url() {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getSizeSHD() {
        return sizeSHD;
    }

    public void setSizeSHD(int sizeSHD) {
        this.sizeSHD = sizeSHD;
    }

    public int getPlayersize() {
        return playersize;
    }

    public void setPlayersize(int playersize) {
        this.playersize = playersize;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getM3u8_url() {
        return m3u8_url;
    }

    public void setM3u8_url(String m3u8_url) {
        this.m3u8_url = m3u8_url;
    }

    public String getTopicImg() {
        return topicImg;
    }

    public void setTopicImg(String topicImg) {
        this.topicImg = topicImg;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getVideosource() {
        return videosource;
    }

    public void setVideosource(String videosource) {
        this.videosource = videosource;
    }

    public String getM3u8Hd_url() {
        return m3u8Hd_url;
    }

    public void setM3u8Hd_url(String m3u8Hd_url) {
        this.m3u8Hd_url = m3u8Hd_url;
    }

    public int getSizeSD() {
        return sizeSD;
    }

    public void setSizeSD(int sizeSD) {
        this.sizeSD = sizeSD;
    }

    public String getTopicSid() {
        return topicSid;
    }

    public void setTopicSid(String topicSid) {
        this.topicSid = topicSid;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getReplyBoard() {
        return replyBoard;
    }

    public void setReplyBoard(String replyBoard) {
        this.replyBoard = replyBoard;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSectiontitle() {
        return sectiontitle;
    }

    public void setSectiontitle(String sectiontitle) {
        this.sectiontitle = sectiontitle;
    }

    public String getTopicDesc() {
        return topicDesc;
    }

    public void setTopicDesc(String topicDesc) {
        this.topicDesc = topicDesc;
    }
}
