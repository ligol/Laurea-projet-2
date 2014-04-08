package laurea_project;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatWindow extends JFrame implements ActionListener {

	private JButton		send;
	private JTextField	message = new JTextField();
	

	public ChatWindow(String title) {
		super(title);
		setLayout(new FlowLayout());
		setSize(400, 300);
		setButton();
		setTextField();
		add(send);
		send.setSize(100, 100);
		add(message);
	}

	private void setButton() {
		// positionnement du bouton a faire
		send = new JButton("Envoyer");
		send.addActionListener(this);
	}
	
	private void setTextField() {
		message.setSize(100, 50);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		send.setSize(150, 150);
		message.setSize(150, 150);
	}

}
