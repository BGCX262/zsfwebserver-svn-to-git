package test.client.gui.component;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import test.client.Telnet;


public class LoginFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4512407986082820182L;
	
	public JLabel uName;
	public JTextField userName;
	public JLabel uPWD;
	public JPasswordField password;
	public JButton login;
	public JButton regist;
	
	public LoginFrame(String title) {
		super(title);
		
		uName = new JLabel("用户名：");
		userName = new JTextField(10);
		uPWD = new JLabel("密码：");
		password = new JPasswordField(10);
		login = new JButton("登录");
		regist = new JButton("注册");
		
		initialization();
	}

	private void initialization() {
		setLayout(new GridLayout(3, 1));
		
		JPanel name = new JPanel(true);
		name.setLayout(new FlowLayout(FlowLayout.CENTER));
		name.add(uName);
		name.add(userName);
		
		JPanel pwd = new JPanel(true);
		pwd.setLayout(new FlowLayout(FlowLayout.CENTER));
		pwd.add(uPWD);
		pwd.add(password);
		
		JPanel btns = new JPanel(true);
		btns.setLayout(new FlowLayout(FlowLayout.CENTER));
		btns.add(login);
		btns.add(regist);
		
		add(name);
		add(pwd);
		add(btns);
		
		login.addActionListener(this);
		regist.addActionListener(this);
		
		pack();
		setLocation(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn.getText().equals("登录")) {
			Telnet.session.write("2," + userName.getText() + "," + new String(password.getPassword()));
			
		} else if (btn.getText().equals("注册")) {
			Telnet.session.write("1," + userName.getText() + "," + new String(password.getPassword()));
		}
	}

}
