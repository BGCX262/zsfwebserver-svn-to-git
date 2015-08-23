package test.client.handler;

import javax.swing.JOptionPane;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import test.client.Telnet;
import test.client.gui.component.LoginFrame;
import test.client.gui.component.SimpleFrame;


public class SimpleConnectorHandler implements IoHandler {
	
	public SimpleFrame frame;
	public LoginFrame login;

	public void sessionCreated(IoSession session) throws Exception {
		Telnet.session = session;
	}

	public void sessionOpened(IoSession session) throws Exception {
		frame = new SimpleFrame("客户端");
		login = new LoginFrame("登录");
		
		login.setVisible(true);
	}

	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("session is closed..");
		System.exit(0);
	}

	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {}

	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {}

	public void messageReceived(IoSession session, Object message) throws Exception {
		int index = message.toString().indexOf(",");
		String cmd = message.toString().substring(0, index);
		String content = message.toString().substring(index+1);
		if (cmd.equals("4")) {	// regist
			JOptionPane.showMessageDialog(login, content.equals("0") ? "注册失败" : "注册成功");
			
		} else if (cmd.equals("5")) {	// login
			boolean flag = content.equals("0");
			JOptionPane.showMessageDialog(login, flag ? "登录失败" : "登录成功");
			if (!flag) {
				login.setVisible(false);
				frame.setVisible(true);
				
				/*for (int i = 0; i < 100; i++) {
					Telnet.session.write("3," + i);
				}*/
			}
			
		} else if (cmd.equals("6")) {	// say
			frame.msgs.append(content + "\r\n");
			frame.msgs.setCaretPosition(frame.msgs.getText().length());
		}
	}

	public void messageSent(IoSession session, Object message) throws Exception {}

}
