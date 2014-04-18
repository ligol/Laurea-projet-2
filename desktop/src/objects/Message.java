package objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Message")
public class Message {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String message;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "contact_id")
	private Contacts contact;
	@DatabaseField
	private Boolean me;
	@DatabaseField
	private Boolean read = false;

	public Message() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Contacts getContact() {
		return contact;
	}

	public void setContact(Contacts contact) {
		this.contact = contact;
	}

	public Boolean getMe() {
		return me;
	}

	public void setMe(Boolean me) {
		this.me = me;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	@Override
	public String toString() {
		if (getMe() == true) return "Me: " + message;
		else return contact.getNickname() + ": " + message;
	}

}
