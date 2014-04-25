package utils;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import listener.OnContactChatListener;
import listener.OnContactListListener;
import objects.Check;
import objects.Contacts;
import objects.Message;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;

public class SocketIOCallback implements IOCallback {
	private OnContactListListener newListListener;
	private List<OnContactChatListener> newChatListener = new ArrayList<OnContactChatListener>();
	private static Dao<Contacts, Integer> contactsDao;
	private static Dao<Message, Integer> messageDao;
	private static Dao<Check, String> checkDao;

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
					newListListener.onConnection(o.getString("user"), o.getBoolean("state"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case "disconnected":
			if (newListListener != null) {
				try {
					JSONObject o = new JSONObject((String) param[0]);
					newListListener.onDisconnetion(o.getString("user"),
							o.getBoolean("state"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case "message":
			try {
				JSONObject o = new JSONObject((String) param[0]);
				Message m = new Message();

				List<Contacts> contact = contactsDao.queryBuilder().where().eq("hash", o.getString("sender")).query();

				m.setContact(contact.get(0));
				m.setMe(false);
				m.setMessage(RSAUtils.decrypt(checkDao, o.getString("content")));

				messageDao.create(m);

				if (newChatListener != null) {
					for (OnContactChatListener chat : newChatListener) {
						chat.onMessage(m);
					}
				}
				if (newListListener != null) {
					newListListener.onMessage();
				}
			} catch (JSONException | SQLException e) {
				e.printStackTrace();
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

	public void setNewChatListener(OnContactChatListener newChatListener) {
		this.newChatListener.add(newChatListener);
	}

	public Dao<Contacts, Integer> getContactsDao() {
		return contactsDao;
	}

	public static void setContactsDao(Dao<Contacts, Integer> cd) {
		SocketIOCallback.contactsDao = cd;
	}

	public static Dao<Message, Integer> getMessageDao() {
		return messageDao;
	}

	public static void setMessageDao(Dao<Message, Integer> mess) {
		SocketIOCallback.messageDao = mess;
	}

	public static Dao<Check, String> getCheckDao() {
		return checkDao;
	}

	public static void setCheckDao(Dao<Check, String> checkDao) {
		SocketIOCallback.checkDao = checkDao;
	}

}
