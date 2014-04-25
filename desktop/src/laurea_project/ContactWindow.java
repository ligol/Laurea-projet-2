package laurea_project;

import io.socket.SocketIO;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import objects.Check;
import objects.Contacts;
import objects.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.RSAUtils;
import utils.SocketIOCallback;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ContactWindow {

	private JFrame frame;
	private JList<Contacts> list;
	private DefaultListModel<Contacts> dlm;
	private static Dao<Contacts, Integer> contactsDao;
	private static Dao<Message, Integer> messageDao;
	private static Dao<Check, String> checkDao;
	private static ConnectionSource connectionSource;
	private static ConnectionSource connectionSourceCheck;
	private SocketIO socket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// DB connection
					connectionSource = new JdbcConnectionSource("jdbc:sqlite:data.db");
					connectionSourceCheck = new JdbcConnectionSource("jdbc:sqlite:check");
					setupDataBases();
					SocketIOCallback.setContactsDao(contactsDao);
					SocketIOCallback.setMessageDao(messageDao);
					SocketIOCallback.setCheckDao(checkDao);

					// Create the main window
					ContactWindow window = new ContactWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected static void setupDataBases() throws SQLException {
		contactsDao = DaoManager.createDao(connectionSource, Contacts.class);
		messageDao = DaoManager.createDao(connectionSource, Message.class);
		checkDao = DaoManager.createDao(connectionSourceCheck, Check.class);

		TableUtils.createTableIfNotExists(connectionSource, Contacts.class);
		TableUtils.createTableIfNotExists(connectionSource, Message.class);
		TableUtils.createTableIfNotExists(connectionSourceCheck, Check.class);
	}

	/**
	 * Create the application.
	 */
	public ContactWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		// Check if it is the first launch
		try {
			if (checkDao.idExists("Keys") == false) {
				RSAUtils.generatePrivateKey(checkDao);
			}
		} catch (SQLException e4) {
			e4.printStackTrace();
		}

		// Create the window
		frame = new JFrame("Laurea Project");
		frame.setBounds(100, 100, 300, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);

		try {
			// Get all DB entries
			List<Contacts> contactList = contactsDao.queryForAll();

			dlm = new DefaultListModel<Contacts>();

			// Declare the socket to the server
			socket = new SocketIO("http://ligol.fr:8080/");

			// Initialize the socket to the server
			socket.connect(SocketIOCallback.getInstance());

			// Declare JSON Object in order to send them later to the server
			JSONObject userInfo = new JSONObject();
			JSONObject userFollow = new JSONObject();

			// Set JSON Object
			userInfo.put("id", RSAUtils.getPublicKeyHash(checkDao));

			list = new JList<Contacts>(dlm);
			List<String> id = new ArrayList<String>();

			// Set the JList for the window and the List id for JSON Objects
			for (Contacts contact : contactList) {
				dlm.addElement(contact);	// for the window
				id.add(contact.getHash());	// for JSON Objects
			}
			list.setModel(dlm);
			list.setSelectedIndex(0);

			// Create the double click listener
			MouseListener mouseListener = new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						Contacts selectedItem = list.getSelectedValue();

						if (selectedItem != null) {
							// Create the chat window
							ChatWindow chat = new ChatWindow(frame, selectedItem, messageDao, checkDao, socket);
							chat.setVisible(true);
						}
					}
				}
			};
			list.addMouseListener(mouseListener);
			JSONArray a = new JSONArray(id);
			userFollow.put("id", a);

			// Send to the server
			socket.emit("userInfo", userInfo.toString());
			socket.emit("userFollow", userFollow.toString());

			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			scrollPane.setViewportView(list);
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JMenuBar menuBar = new JMenuBar();
		scrollPane.setColumnHeaderView(menuBar);

		JMenu mnFile = new JMenu("Options");
		menuBar.add(mnFile);

		JMenuItem mntmAddANew = new JMenuItem("Add a new contact");
		mntmAddANew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Get informations of the new contact pop up
					AddContact dialog = new AddContact(frame, checkDao);

					if (dialog.getNickname() != null && dialog.getPublicKey() != null) {
						// Create the new contact object
						Contacts newContact = new Contacts();
						newContact.setNickname(dialog.getNickname());
						newContact.setHash(RSAUtils.getPublicKeyHash(dialog.getPublicKey()));
						newContact.setPublickey(dialog.getPublicKey());

						// Save the new contact in the DB and put it in the list
						contactsDao.create(newContact);
						dlm.addElement(newContact);
						list.setModel(dlm);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnFile.add(mntmAddANew);
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					connectionSource.close();
					connectionSourceCheck.close();
					socket.disconnect();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
	}
}
