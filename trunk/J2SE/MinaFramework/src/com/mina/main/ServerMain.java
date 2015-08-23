package com.mina.main;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.mina.utils.LogUtil;
import com.mina.utils.session.handler.DefaultSessionHandler;


public class ServerMain {
	
	public static void initTCP() {
		try {
			SocketAcceptor service = new NioSocketAcceptor();
			SocketSessionConfig config = service.getSessionConfig();
			//config.setIdleTime(IdleStatus.BOTH_IDLE, 10);
			config.setMaxReadBufferSize(4096);
			
			if (DefaultSessionHandler.PROTOCOL_TYPE.equals("byte"))
				service.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
			else
				service.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf8"))));
			service.setHandler(new DefaultSessionHandler());
			service.bind(new InetSocketAddress(12345));
			LogUtil.info("Init success!!");
		} catch (Exception e) {
			LogUtil.error("init server failed..", e);
		}
	}
	
	public static void main(String[] args) {
		LogUtil.info("Init MinaFramework..");
		initTCP();
	}

}
