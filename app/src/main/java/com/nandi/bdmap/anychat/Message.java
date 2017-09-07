package com.nandi.bdmap.anychat;

import java.io.Serializable;

/**
 * Created by ChenPeng on 2017/5/31.
 */

public class Message implements Serializable {
    private String invite_man;  //邀请人
    private String user_name;
    private int invite_userId;  //邀请人id
    private String phone_ids;   //手机IMEI
    private String push_msg;    //推送消息
    private int room_id;        //房间ID
    private String title;       //消息标题

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getInvite_man() {
        return invite_man;
    }

    public void setInvite_man(String invite_man) {
        this.invite_man = invite_man;
    }

    public int getInvite_userId() {
        return invite_userId;
    }

    public void setInvite_userId(int invite_userId) {
        this.invite_userId = invite_userId;
    }

    public String getPhone_ids() {
        return phone_ids;
    }

    public void setPhone_ids(String phone_ids) {
        this.phone_ids = phone_ids;
    }

    public String getPush_msg() {
        return push_msg;
    }

    public void setPush_msg(String push_msg) {
        this.push_msg = push_msg;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
