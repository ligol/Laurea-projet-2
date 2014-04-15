package fr.ligol.laurea_project.model;

import com.orm.SugarRecord;

public class Message extends SugarRecord<Message> {
    private boolean me;
    private boolean read = false;
    private String message;

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
