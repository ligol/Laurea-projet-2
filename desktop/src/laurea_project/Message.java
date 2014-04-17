package laurea_project;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Message")
public class Message {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String message;
	@DatabaseField
	private Contacts contact;
	@DatabaseField
	private Boolean me;
	@DatabaseField
	private Boolean read = false;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "contact_id")
	private Contacts contacts;

	public Message() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Contacts getContacts() {
		return contacts;
	}

	public void setContacts(Contacts contacts) {
		this.contacts = contacts;
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
		return contact.getNickname() + ": " + message;
	}

}
