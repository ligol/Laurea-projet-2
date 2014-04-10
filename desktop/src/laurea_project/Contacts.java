package laurea_project;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Contacts")
public class Contacts {

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String nickname;
	@DatabaseField
	private String hash;
	@DatabaseField
	private String publickey;

	public Contacts() {

	}

	public Contacts(int id, String nickname, String hash, String publickey) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.hash = hash;
		this.publickey = publickey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPublickey() {
		return publickey;
	}

	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	@Override
	public String toString() {
		return nickname + " - " + hash + " - " + publickey;
	}
}
