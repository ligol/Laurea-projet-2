package fr.ligol.laurea_project.model;

import com.orm.SugarRecord;

public class Contact extends SugarRecord<Contact> {
    private String name;
    private String hisHash;
    private String hisPublicKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHisHash() {
        return hisHash;
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
}
