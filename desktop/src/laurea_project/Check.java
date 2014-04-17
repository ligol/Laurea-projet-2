package laurea_project;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Check")
public class Check {

	@DatabaseField(id = true)
	private String id = "Id";
	@DatabaseField
	private Boolean isFirstLaunch = true;
	@DatabaseField
	private String priv;
	@DatabaseField
	private String pub;

	public Check() {
		
	}
	
	public Check(Boolean isFirstLaunch, String priv, String pub) {
		this.isFirstLaunch = isFirstLaunch;
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

	public boolean isFirstLaunch() {
		return isFirstLaunch;
	}

	public void setFirstLaunch(boolean isFirstLaunch) {
		this.isFirstLaunch = isFirstLaunch;
	}

}
