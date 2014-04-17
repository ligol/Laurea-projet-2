package utils;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import java.sql.SQLException;

import laurea_project.Contacts;
import laurea_project.Message;
import listener.OnContactChatListener;
import listener.OnContactListListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import dao.ContactsDao;
import dao.MessageDao;

public class SocketIOCallback implements IOCallback {
	private OnContactListListener newListListener;
	private OnContactChatListener newChatListener;
	private static ConnectionSource connectionSourceContacts;
	private static ConnectionSource connectionSourceMessages;
	private static ContactsDao cd;
	private static MessageDao mess;

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
					Contacts contact = cd.performDBfind(connectionSourceContacts, o.getString("sender"));
					m.setContact(contact);
					m.setMe(false);
					m.setMessage(o.getString("content"));

					// Save the message in the DB
					connectionSourceMessages = new JdbcConnectionSource("jdbc:sqlite:messagesdb");
					mess = new MessageDao(connectionSourceMessages);
					mess.performDBInsert(connectionSourceMessages, m);
					connectionSourceMessages.close();

					if (newChatListener != null) {
						newChatListener.onMessage();
					}
					newListListener.newDisconnetion(o.getString("user"),
							o.getBoolean("state"));
				} catch (JSONException | SQLException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
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

	public static ConnectionSource getConnectionSourceContacts() {
		return connectionSourceContacts;
	}

	public static void setConnectionSourceContacts(ConnectionSource connectionSourceContacts) {
		SocketIOCallback.connectionSourceContacts = connectionSourceContacts;
	}

	public ContactsDao getConactsDao() {
		return cd;
	}

	public static void setContactsDao(ContactsDao cd) {
		SocketIOCallback.cd = cd;
	}

	public static MessageDao getMess() {
		return mess;
	}

	public static void setMess(MessageDao mess) {
		SocketIOCallback.mess = mess;
	}

}
