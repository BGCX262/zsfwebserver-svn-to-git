/*
 * Copyright 2004-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.cindy.session.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.SessionType;
import net.sf.cindy.session.AbstractSessionAcceptor;
import net.sf.cindy.util.ChannelUtils;
import net.sf.cindy.util.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Non-blocking session acceptor.
 * 
 * @author <a href="chenrui@gmail.com">Roger Chen</a>
 * @version $id$
 */
public class NonBlockingSessionAcceptor extends AbstractSessionAcceptor {
	
	private static final Log log = LogFactory.getLog(NonBlockingSessionAcceptor.class);
    private final AtomicInteger counter = new AtomicInteger();

    private final ServerSocketChannelSession session = new ServerSocketChannelSession() {

        protected void buildSession(SocketChannel sc) {
            // Auto���һ��������ΪĬ�Ϸ������˿�
        	counter.incrementAndGet();
            try {
            	// ���õ�ǰsocket��һЩ����
                setSocketOptions(sc.socket());
                // ��ǰ���Ӷ�Ӧ��session�࣬ͨ��attachment��ʽ����ĵ�ǰͨ�����У������socket����ʱ�䶼��Ч
                SocketChannelSession session = new SocketChannelSession();
                
                session.setChannel(sc);
                // ��Ҫ��ͨ��attachment��ʽ����ĵ�ǰͨ�����к����þ���Ӧ�õ�handle
                sessionAccepted(session);
            } catch (Throwable e) {
                exceptionCaught(e);
            }
        }
    };

    public NonBlockingSessionAcceptor() {
        session.setSessionHandler(new SessionHandlerAdapter() {

            public void exceptionCaught(Session session, Throwable cause) {
                NonBlockingSessionAcceptor.this.exceptionCaught(cause);
            }
        });
    }

    public SessionType getSessionType() {
        return SessionType.TCP;
    }

    public boolean isStarted() {
        return session.isStarted();
    }

    public SocketAddress getListenAddress() {
        if (isStarted())
            return session.getSocket().getLocalSocketAddress();
        return super.getListenAddress();
    }

    public int getListenPort() {
        if (isStarted())
            return session.getSocket().getLocalPort();
        return super.getListenPort();
    }

    public synchronized void start() {
        if (getAcceptorHandler() == null)
            throw new IllegalStateException("acceptor handler is null");
        if (isStarted())
            return;

        ServerSocketChannel channel = null;
        try {
            channel = ServerSocketChannel.open();
            setServerSocketOptions(channel.socket());
            counter.set(0);
            session.setChannel(channel);
            session.start().complete();
            showStartMessage(channel);
        } catch (IOException e) {
            ChannelUtils.close(channel);
            exceptionCaught(e);
            if (e instanceof BindException) {
				System.exit(0);
			}
        }
    }

    private void showStartMessage(ServerSocketChannel channel) {
    	log.info("Starting " + Configuration.getServiceName());
    	log.info("server listening to localhost:" + channel.socket().getLocalPort());
    	log.info("");
    	StringBuffer buffer;
    	Map<String, String> getenv = System.getenv();
    	Properties properties = System.getProperties();
		if (log.isDebugEnabled()) {
			Iterator<String> ite = getenv.keySet().iterator();
			while(ite.hasNext()) {
				String next = ite.next();
				log.debug(next + " = " +getenv.get(next));
			}
			Iterator<Object> proIte = properties.keySet().iterator();
			while(proIte.hasNext()) {
				Object next = proIte.next();
				log.debug(next + " = " +properties.get(next));
			}
		} else {
			buffer = new StringBuffer();
			buffer.append(getenv.get("COMPUTERNAME"));
			buffer.append(".");
			buffer.append(properties.get("user.name"));
			log.info(buffer);
			
			buffer = new StringBuffer();
	    	buffer.append(properties.get("os.name"));
			String space = " ";
			String comma = ", ";
			buffer.append(space);
			buffer.append(properties.get("os.version"));
			buffer.append(space);
			buffer.append(properties.get("os.arch"));
			log.info(buffer);
			
			buffer = new StringBuffer();
			buffer.append(properties.get("java.runtime.name"));
			buffer.append(space);
			buffer.append(properties.get("java.runtime.version"));
			buffer.append(comma);
			buffer.append(properties.get("sun.jnu.encoding"));
			buffer.append(comma);
			buffer.append(properties.get("user.language"));
			log.info(buffer);
			
			buffer = new StringBuffer();
			buffer.append(properties.get("java.vm.name"));
			buffer.append(space);
			buffer.append(properties.get("java.vm.version"));
			buffer.append(comma);
			buffer.append(properties.get("sun.arch.data.model"));
			buffer.append(comma);
			buffer.append(properties.get("java.vm.info"));
			buffer.append(comma);
			buffer.append(properties.get("java.vm.specification.vendor"));
			log.info(buffer);
		}
		log.info("");
	}

	public int getAcceptedCount() {
        return counter.get();
    }

    public synchronized void close() {
        session.close().complete();
    }
}
