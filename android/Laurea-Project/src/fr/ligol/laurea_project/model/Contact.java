package fr.ligol.laurea_project.model;

import com.orm.SugarRecord;

public class Contact extends SugarRecord<Contact> {
    private String name;
    private String myPublicKey;
    private String hisPublicKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMyPublicKey() {
        return myPublicKey;
    }

    public void setMyPublicKey(String myPublicKey) {
        this.myPublicKey = myPublicKey;
    }

    public String getHisPublicKey() {
        return hisPublicKey;
    }

    public void setHisPublicKey(String hisPublicKey) {
        this.hisPublicKey = hisPublicKey;
    }
}
