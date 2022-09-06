package com.pds.socialmediahub.model;

/**
 * Created by Anon on 14,June,2019
 */
public class Conversation {

    private String message;
    private long time;
    private boolean isDeleted;

    public Conversation() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
