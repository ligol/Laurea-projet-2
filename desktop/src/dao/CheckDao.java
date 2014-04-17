package dao;

import java.sql.SQLException;

import laurea_project.Check;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
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

		QueryBuilder<Check, String> qBuilder = checkDao.queryBuilder();
		qBuilder.orderBy("isFirstLaunch", false);
		Check check = checkDao.queryForFirst(qBuilder.prepare());
		System.out.println("Check si first launch : " + check.isFirstLaunch());

		return check.isFirstLaunch();
	}

	public void performDBUpdate(ConnectionSource connectionSource, Check update)
			throws SQLException {
		checkDao = DaoManager.createDao(connectionSource, Check.class);

		checkDao.update(update);
	}

	public Check performDBSelect(ConnectionSource connectionSource)
			throws SQLException {
		checkDao = DaoManager.createDao(connectionSource, Check.class);

		Check select = checkDao.queryForId("Id");

		return select;
	}

}
