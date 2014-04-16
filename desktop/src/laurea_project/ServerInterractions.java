package laurea_project;

import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.SocketIO;


public class ServerInterractions {
	private SocketIO	socket;
	
	public void connect() {
        try {
            socket = new SocketIO("http://ligol.fr:8080/");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block                                                                                                                              
            e.printStackTrace();
        }
        /*	    JSONObject userInfo = new JSONObject();
	    JSONObject userFollow = new JSONObject();

	    socket.connect();

	    socket.emit("userInfo", userInfo.toString());
	    socket.emit("userFollow", userFollow.toString()); */
	}

	
	public void disconnect() {

	}
}
