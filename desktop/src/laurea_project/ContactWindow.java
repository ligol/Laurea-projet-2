package laurea_project;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import dao.ContactsDao;

public class ContactWindow {

	private JFrame frame;
	private JList<Contacts> list;
	private DefaultListModel<Contacts> dlm;
	private static ContactsDao cd;
	private static ConnectionSource connectionSource;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String databaseUrl = "jdbc:sqlite:contacts.db";
				try {
					connectionSource = new JdbcConnectionSource(databaseUrl);
					cd = new ContactsDao(connectionSource);
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
		frame = new JFrame("Laurea Project");
		frame.setBounds(100, 100, 300, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);

		try {
			List<Contacts> contactList = cd.performDBSelect(connectionSource);
			dlm = new DefaultListModel<Contacts>();
			list = new JList<Contacts>(dlm);
			for (Contacts contact : contactList) {
				dlm.addElement(contact);
			}
			list.setModel(dlm);

			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			scrollPane.setViewportView(list);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		JMenuBar menuBar = new JMenuBar();
		scrollPane.setColumnHeaderView(menuBar);

		JMenu mnFile = new JMenu("Options");
		menuBar.add(mnFile);

		JMenuItem mntmAddANew = new JMenuItem("Add a new contact");
		mntmAddANew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nickname;
				String hash;
				String publickey;
				try {
					AddContact dialog = new AddContact(frame);
					nickname = dialog.getNickname();
					hash = dialog.getHash();
					publickey = dialog.getPublicKey();
					Contacts newContact = new Contacts();
					newContact.setNickname(nickname);
					newContact.setHash(hash);
					newContact.setPublickey(publickey);
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
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
	}
}
