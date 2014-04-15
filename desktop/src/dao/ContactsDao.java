package dao;

import java.sql.SQLException;
import java.util.List;

import laurea_project.Contacts;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ContactsDao extends BaseDaoImpl<Contacts, String> implements ContactsDaoInterface {
	Dao<Contacts, String> contactsDao;

	public ContactsDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Contacts.class);
	}

	public void performDBInsert(ConnectionSource connectionSource, Contacts contact)
			throws SQLException {
		contactsDao = DaoManager.createDao(connectionSource, Contacts.class);
		// create table
		TableUtils.createTableIfNotExists(connectionSource, Contacts.class);

		// save objects to DB
		contactsDao.create(contact);

		// retrieve all objects from DB
		List<Contacts> list = contactsDao.queryForAll();
		System.out.println("*******List of objects saved in DB*******");
		for (Contacts contacts : list) {
			System.out.println(contacts);
		}
	}

	public List<Contacts> performDBSelect(ConnectionSource connectionSource)
			throws SQLException {
		contactsDao = DaoManager.createDao(connectionSource, Contacts.class);
		// create table
		TableUtils.createTableIfNotExists(connectionSource, Contacts.class);

		// select objects from DB
		List<Contacts> contactList = contactsDao.queryForAll();

		return contactList;
	}

}
