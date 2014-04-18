package objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Check")
public class Check {

	@DatabaseField(id = true)
	private String id = "Keys";
	@DatabaseField
	private String priv;
	@DatabaseField
	private String pub;

	public Check() {

	}

	public Check(String priv, String pub) {
		this.priv = priv;
		this.pub = pub;
	}

	public String getPriv() {
		return priv;
	}

	public void setPriv(String priv) {
		this.priv = priv;
	}

	public String getPub() {
		return pub;
	}

	public void setPub(String pub) {
		this.pub = pub;
	}

}
