package dao;

import java.sql.SQLException;

import laurea_project.Check;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class CheckDao extends BaseDaoImpl<Check, String> implements CheckDaoInterface {
	Dao<Check, String> checkDao;

	public CheckDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Check.class);
	}

	public boolean performDBCheck(ConnectionSource connectionSource)
			throws SQLException {
		checkDao = DaoManager.createDao(connectionSource, Check.class);
		// create table
		TableUtils.createTableIfNotExists(connectionSource, Check.class);

		Check check = checkDao.queryForId("Keys");

		if (check == null) return true;
		else return false;
	}

	public void performDBInsert(ConnectionSource connectionSource, Check update)
			throws SQLException {
		checkDao = DaoManager.createDao(connectionSource, Check.class);

		checkDao.create(update);

	}

	public Check performDBSelect(ConnectionSource connectionSource)
			throws SQLException {
		checkDao = DaoManager.createDao(connectionSource, Check.class);

		Check select = checkDao.queryForId("Keys");

		System.out.println("");

		return select;
	}

}
