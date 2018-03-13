package com.dissertation.toaok.utils;

import java.util.EventListener;

/**
 * Created by TOAOK on 2017/10/23.
 */

public class BBMSEvent {

    private String account;
    private String bookinfos;
    private String userinfo;

    public BBMSEvent(String account, String bookinfos, String userinfo) {
        this.account = account;
        this.bookinfos = bookinfos;
        this.userinfo = userinfo;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBookinfos() {
        return bookinfos;
    }

    public void setBookinfos(String bookinfos) {
        this.bookinfos = bookinfos;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public interface OnEventListener extends EventListener {

        void onHandler(BBMSEvent be);

    }
}
