package test.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import test.client.handler.SimpleConnectorHandler;

public class Telnet {
	
	public static IoSession session;
	
	public static void main(String[] args) {
		connect();
	}
	
	public static void connect() {

		NioSocketConnector conn = new NioSocketConnector();
		conn.setHandler(new SimpleConnectorHandler());
		
		conn.getFilterChain().addLast("logger", new LoggingFilter());
		conn.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
		
		conn.connect(new InetSocketAddress(12345)).getSession();
	}

}
