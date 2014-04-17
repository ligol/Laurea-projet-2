package laurea_project;

import io.socket.SocketIO;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.RSAUtils;
import utils.SocketIOCallback;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import dao.CheckDao;
import dao.ContactsDao;

public class ContactWindow {

	private JFrame frame;
	private JList<Contacts> list;
	private DefaultListModel<Contacts> dlm;
	private static ContactsDao cd;
	private static CheckDao check;
	private static ConnectionSource connectionSource;
	private SocketIO socket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String databaseUrl = "jdbc:sqlite:contacts.db";
				try {
					// DB connection
					connectionSource = new JdbcConnectionSource(databaseUrl);
					cd = new ContactsDao(connectionSource);
					check = new CheckDao(connectionSource);
					SocketIOCallback.setConnectionSource(connectionSource);
					SocketIOCallback.setContactsDao(cd);

					// Create the main window
					ContactWindow window = new ContactWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		boolean isFirstLaunch = true;
		try {
			isFirstLaunch = check.performDBCheck(connectionSource);
		} catch (SQLException e3) {
			e3.printStackTrace();
		}
		if (isFirstLaunch == true) {
			RSAUtils.generatePrivateKey(check, connectionSource);
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
			List<Contacts> contactList = cd.performDBSelect(connectionSource);
			dlm = new DefaultListModel<Contacts>();

			// Declare the socket to the server
			socket = new SocketIO("http://ligol.fr:8080/");

			// Declare JSON Object in order to send them later to the server
			JSONObject userInfo = new JSONObject();
			JSONObject userFollow = new JSONObject();

			// Set JSON Object
			userInfo.put("id", RSAUtils.getPublicKeyHash(check, connectionSource));

			list = new JList<Contacts>(dlm);
			List<String> id = new ArrayList<String>();

			// Set the JList for the window and the List id for JSON Objects
			for (Contacts contact : contactList) {
				dlm.addElement(contact);	// for the window
				id.add(contact.getHash());	// for JSON Objects
			}
			list.setModel(dlm);
			JSONArray a = new JSONArray(id);
			userFollow.put("id", a);

			// Initialize the socket to the server
			socket.connect(SocketIOCallback.getInstance());

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
					AddContact dialog = new AddContact(frame);

					// Create the new contact object
					Contacts newContact = new Contacts();
					newContact.setNickname(dialog.getNickname());
					newContact.setHash(dialog.getHash());
					newContact.setPublickey(dialog.getPublicKey());

					// Save the new contact in the DB and put it in the list
					cd.performDBInsert(connectionSource, newContact);
					dlm.addElement(newContact);
					list.setModel(dlm);
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
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
	}
}
