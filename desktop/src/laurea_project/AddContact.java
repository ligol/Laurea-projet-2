package laurea_project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class AddContact extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfNickname;
	private JLabel lblNickname;
	private JTextField tfHash;
	private JTextField tfPublicKey;
	private JLabel lblHash;
	private JLabel lblPublicKey;
	private String nickname;
	private String hash;
	private String publickey;
	private JButton okButton;


	/**
	 * Create the dialog.
	 */
	AddContact(Frame parentFrame) {
		super(parentFrame, "Add a new Contact", true);
		setType(Type.POPUP);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parentFrame);	// window center
		setBounds(100, 100, 300, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblNickname = new JLabel("Nickname");
			lblNickname.setHorizontalAlignment(SwingConstants.CENTER);
		}
		contentPanel.setLayout(new GridLayout(0, 2, 0, 0));
		contentPanel.add(lblNickname);
		{
			tfNickname = new JTextField();
			tfNickname.setColumns(10);
		}
		lblNickname.setLabelFor(tfNickname);
		contentPanel.add(tfNickname);
		{
			lblHash = new JLabel("Hash");
			lblHash.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblHash);
		}
		{
			tfHash = new JTextField();
			lblHash.setLabelFor(tfHash);
			contentPanel.add(tfHash);
			tfHash.setColumns(10);
		}
		{
			lblPublicKey = new JLabel("Public key");
			lblPublicKey.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblPublicKey);
		}
		{
			tfPublicKey = new JTextField();
			lblPublicKey.setLabelFor(tfPublicKey);
			contentPanel.add(tfPublicKey);
			tfPublicKey.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.LIGHT_GRAY));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Add");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setVisible(true);
	}

	public String getNickname() {
		return nickname;
	}

	public String getHash() {
		return hash;
	}

	public String getPublicKey() {
		return publickey;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			nickname = tfNickname.getText();
			hash = tfHash.getText();
			publickey = tfPublicKey.getText();
		} else {
			nickname = null;
			hash = null;
			publickey = null;
		}
		setVisible(false);
		dispose();

	}

}