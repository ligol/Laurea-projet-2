package laurea_project;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import objects.Contacts;
import objects.Message;

import com.j256.ormlite.dao.Dao;

public class ChatWindow extends JFrame {

	private Dao<Message, Integer> messageDao;
	private JPanel contentPane;
	private JTextArea chatHistory;
	private JTextField message;
	private static Contacts contact;

	public ChatWindow(Frame parentFrame, Contacts selectedContact, Dao<Message, Integer> messDao) {
		super(selectedContact.getNickname());
		setResizable(false);
		setContact(selectedContact);
		setMessageDao(messDao);

		setSize(400, 300);
		setLocationRelativeTo(parentFrame);	// window center
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{325, 15};
		gbl_contentPane.rowHeights = new int[]{200, 20};
		gbl_contentPane.columnWeights = new double[]{0.0};
		gbl_contentPane.rowWeights = new double[]{Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		chatHistory = new JTextArea();
		chatHistory.setFocusable(false);
		chatHistory.setFocusTraversalKeysEnabled(false);
		chatHistory.setRequestFocusEnabled(false);
		chatHistory.setEditable(false);
		GridBagConstraints gbc_chat = new GridBagConstraints();
		gbc_chat.gridwidth = 2;
		gbc_chat.fill = GridBagConstraints.BOTH;
		gbc_chat.insets = new Insets(0, 5, 5, 5);
		gbc_chat.gridx = 0;
		gbc_chat.gridy = 0;
		gbc_chat.insets = new Insets(0, 5, 0, 0);
		contentPane.add(chatHistory, gbc_chat);

		message = new JTextField();
		GridBagConstraints gbc_message = new GridBagConstraints();
		gbc_message.fill = GridBagConstraints.HORIZONTAL;
		gbc_message.insets = new Insets(0, 0, 0, 5);
		gbc_message.gridx = 0;
		gbc_message.gridy = 1;
		contentPane.add(message, gbc_message);
		message.requestFocus();

		JButton send = new JButton("Send");
		GridBagConstraints gbc_send = new GridBagConstraints();
		gbc_send.gridx = 1;
		gbc_send.gridy = 1;
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// Create the Message Object
				Message messageSent = new Message();
				messageSent.setMessage(message.getText());
				messageSent.setMe(true);
				messageSent.setRead(true);
				messageSent.setContact(contact);

				// Add the message in the chat history
				if (chatHistory.getText().length() > 0) chatHistory.insert("\n", chatHistory.getText().length());
				chatHistory.insert("Me: " + message.getText(), chatHistory.getText().length());

				try {
					messageDao.create(messageSent);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				message.setText(""); // reset the message entry				
			}
		});
		contentPane.add(send, gbc_send);

		List<Message> results;
		try {
			results = messageDao.queryBuilder().where().eq("contact_id", contact.getId()).query();
			for (Message result : results) {
				if (chatHistory.getText().length() > 0) chatHistory.insert("\n", chatHistory.getText().length());
				chatHistory.insert(result.toString(), chatHistory.getText().length());				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	public void setMessageDao(Dao<Message, Integer> mess) {
		this.messageDao = mess;
	}

	public void setContact(Contacts contact) {
		ChatWindow.contact = contact;
	}

}
