package com.aman_kumar.nodechat.models;

/**
 * Created by aman on 29/03/16.
 */
public class Chat {
    private String msg;
    private int sentBy;
    private String time;

    public Chat(String msg, int sentBy, String time) {
        this.msg = msg;
        this.sentBy = sentBy;
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }
    public int getSentBy() {
        return sentBy;
    }
    public String time() {
        return time;
    }
}
