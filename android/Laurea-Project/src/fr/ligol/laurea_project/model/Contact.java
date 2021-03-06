package fr.ligol.laurea_project.model;

import java.util.List;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Contact extends SugarRecord<Contact> {
    private String name;
    private String hisHash;
    private String hisPublicKey;
    @Ignore
    private boolean isConnected = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHisHash() {
        return hisHash.replace("\n", "");
    }

    public void setHisHash(String hisHash) {
        this.hisHash = hisHash;
    }

    public String getHisPublicKey() {
        return hisPublicKey;
    }

    public void setHisPublicKey(String hisPublicKey) {
        this.hisPublicKey = hisPublicKey;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public List<Message> getMessages() {
        return Message.find(Message.class, "contact = ?", this.id.toString());
    }

    public boolean isAllMessageRead() {
        for (Message m : getMessages()) {
            if (m.isRead() == false) {
                return false;
            }
        }
        return true;
    }
}
