package test.client.gui.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import test.client.Telnet;


public class SimpleFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 859028873568196374L;
	
	public JTextArea msgs;
	public JTextField msgInput;
	public JButton send;
	public JButton clear;
	
	public SimpleFrame(String title) {
		super(title);
		
		msgs = new JTextArea();
		msgInput = new JTextField();
		send = new JButton("发送");
		clear = new JButton("清除");
		
		initialization();
	}

	private void initialization() {
		setLayout(new BorderLayout());
		
		msgs.setWrapStyleWord(true);
		msgs.setEditable(false);
		add(new JScrollPane(msgs));
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(msgInput);
		JPanel btns = new JPanel();
		btns.setLayout(new FlowLayout(FlowLayout.CENTER));
		btns.add(send);
		btns.add(clear);
		panel.add(btns, BorderLayout.SOUTH);
		add(panel, BorderLayout.SOUTH);
		
		send.addActionListener(this);
		clear.addActionListener(this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				Telnet.session.close(true);
				System.exit(0);
			}
		});
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 300, 800, 600);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btn = (JButton) e.getSource();
		if (btn.getText().equals("发送")) {
			Telnet.session.write("3," + msgInput.getText());
			msgInput.setText("");
			
		} else if (btn.getText().equals("清除")) {
			msgInput.setText("");
			
		}
	}

}
