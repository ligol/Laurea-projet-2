package fr.ligol.laurea_project.util;

import fr.ligol.laurea_project.listener.OnNewConnectionListener;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SocketIOCallback implements IOCallback {
    private OnNewConnectionListener newConnectionListener;

    @Override
    public void on(String event, IOAcknowledge arg1, Object... param) {
        // TODO Auto-generated method stub
        Log.d("test", event);
        switch (event) {
        case "connected":
            if (newConnectionListener != null) {
                try {
                    JSONObject o = new JSONObject((String) param[0]);
                    newConnectionListener.newConnection(o.getString("user"),
                            o.getBoolean("state"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
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

    public OnNewConnectionListener getNewConnectionListener() {
        return newConnectionListener;
    }

    public void setNewConnectionListener(
            OnNewConnectionListener newConnectionListener) {
        this.newConnectionListener = newConnectionListener;
    }

}
