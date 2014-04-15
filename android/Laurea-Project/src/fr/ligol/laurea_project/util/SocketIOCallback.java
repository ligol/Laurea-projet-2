package fr.ligol.laurea_project.util;

import fr.ligol.laurea_project.listener.OnContactChatListener;
import fr.ligol.laurea_project.listener.OnContactListListener;
import fr.ligol.laurea_project.model.Contact;
import fr.ligol.laurea_project.model.Message;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SocketIOCallback implements IOCallback {
    private OnContactListListener newListListener;
    private OnContactChatListener newChatListener;

    private SocketIOCallback() {
    }

    private static class SocketIOCallbackHolder {
        private final static SocketIOCallback instance = new SocketIOCallback();
    }

    public static SocketIOCallback getInstance() {
        return SocketIOCallbackHolder.instance;
    }

    @Override
    public void on(String event, IOAcknowledge arg1, Object... param) {
        Log.d("test", event);
        switch (event) {
        case "connected":
            if (newListListener != null) {
                try {
                    JSONObject o = new JSONObject((String) param[0]);
                    newListListener.newConnection(o.getString("user"),
                            o.getBoolean("state"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
        case "disconnected":
            if (newListListener != null) {
                try {
                    JSONObject o = new JSONObject((String) param[0]);
                    newListListener.newDisconnetion(o.getString("user"),
                            o.getBoolean("state"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
        case "message":
            if (newListListener != null) {
                try {
                    JSONObject o = new JSONObject((String) param[0]);
                    Message m = new Message();
                    Contact contact = Contact.find(Contact.class,
                            "hisHash = ?", o.getString("sender")).get(0);
                    m.setContact(contact);
                    m.setMe(false);
                    m.setMessage(o.getString("content"));
                    m.save();
                    if (newChatListener != null) {
                        newChatListener.onMessage();
                    }
                    newListListener.newDisconnetion(o.getString("user"),
                            o.getBoolean("state"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;

        default:
            break;
        }
        for (Object o : param) {
            Log.d("test2", o.toString());
        }
    }

    @Override
    public void onConnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDisconnect() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(SocketIOException arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMessage(String arg0, IOAcknowledge arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMessage(JSONObject arg0, IOAcknowledge arg1) {
        // TODO Auto-generated method stub
    }

    public OnContactListListener getNewConnectionListener() {
        return newListListener;
    }

    public void setNewConnectionListener(
            OnContactListListener newConnectionListener) {
        this.newListListener = newConnectionListener;
    }

    public OnContactChatListener getNewChatListener() {
        return newChatListener;
    }

    public void setNewChatListener(OnContactChatListener newChatListener) {
        this.newChatListener = newChatListener;
    }

}
