package dao;

import java.sql.SQLException;

import laurea_project.Message;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class MessageDao extends BaseDaoImpl<Message, Integer> implements MessageDaoInterface {
	Dao<Message, Integer> messageDao;

	public MessageDao(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, Message.class);
	}

	public void performDBInsert(ConnectionSource connectionSource, Message message) throws SQLException {
		messageDao = DaoManager.createDao(connectionSource, Message.class);
		// create table
		TableUtils.createTableIfNotExists(connectionSource, Message.class);

		messageDao.create(message);
	}
}
