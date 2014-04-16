package laurea_project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatWindow extends JFrame implements ActionListener {

	private JButton		send;
	private JTextArea	message = new JTextArea();
	private JTextField	chat	= new JTextField();

	public ChatWindow(String title) {
		super(title);
		setLayout(new BorderLayout());
		setSize(400, 300);
		setButton();
		setTextField();
		add(message, BorderLayout.CENTER);
		add(chat, BorderLayout.NORTH);
		chat.setPreferredSize(new Dimension(300, 200));
		add(send, BorderLayout.EAST);
	}

	private void setButton() {
		// positionnement du bouton a faire
		send = new JButton("Send");
		send.addActionListener(this);
	}
	
	private void setTextField() {
		chat.setHorizontalAlignment(JTextField.LEFT);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
	}

}
