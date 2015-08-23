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
package net.sf.cindy.util;

import java.util.Date;

import net.sf.cindy.buffer.DefaultBufferPool;
import net.sf.cindy.session.dispatcher.DefaultDispatcher;
import net.sf.cindy.session.nio.DatagramChannelSession;
import net.sf.cindy.session.nio.NonBlockingSessionAcceptor;
import net.sf.cindy.session.nio.PipeSession;
import net.sf.cindy.session.nio.SocketChannelSession;

/**
 * Cindy configuration.
 * 
 * @author <a href="chenrui@gmail.com">Roger Chen</a>
 * @version $id$
 */
public class Configuration {
	private static final FileListener defaultProperties = FileListener.getDefaultInstance();
	/*static{
		FileListener.startListening();
	}*/
	
	public static boolean reload(){
		return defaultProperties.reload();
	}
	
	public static String get(String key) {
		return defaultProperties.getProperty(key, null);
	}

	public static String get(String key, String defaultValue) {
		return defaultProperties.getProperty(key, defaultValue);
	}

	public static int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(get(key, Integer.toString(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		try {
			return Boolean.valueOf(get(key, Boolean.toString(defaultValue)));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	// Global

	public static boolean isEnableJmx() {
		return getBoolean("enableJmx", false);
	}

	public static boolean isDisableInnerException() {
		return getBoolean("disableInnerException", false);
	}

	// Buffer

	public static String getBufferPool() {
		return get("buffer.bufferPool", DefaultBufferPool.class.getName());
	}

	public static boolean isUseDirectBuffer() {
		return getBoolean("buffer.useDirectBuffer", false);
	}

	public static boolean isUseLinkedBuffer() {
		return getBoolean("buffer.useLinkedBuffer", false);
	}

	// Dispatcher

	public static String getDispatcher() {
		return get("dispatcher", DefaultDispatcher.class.getName());
	}

	public static int getDispatcherConcurrent() {
		return getInt("dispatcher.concurrent", 1);
	}

	public static int getDispatcherCapacity() {
		return getInt("dispatcher.capacity", 1000);
	}

	public static int getDispatcherKeepAliveTime() {
		return getInt("dispatcher.KeepAliveTime", 5000);
	}

	// Session
	public static int[] getSocketListeningPort() {
		String str = get("session.socket.port", "80");
		String[] portsStr = str.split(",");
		int[] ports = new int[portsStr.length];
		for(int i = 0; i < portsStr.length; i++){
			ports[i] = Integer.valueOf(portsStr[i]);
		}
		return ports;
	}
	
	public static void main(String[] args) {
		getSocketListeningPort();
		getSocketListeningPort();
	}

	public static int getSessionTimeout() {
		return getInt("session.timeout", 0);
	}

	public static int getRecvBufferSize() {
		return getInt("session.recvBufferSize", -1);
	}

	public static int getSendBufferSize() {
		return getInt("session.sendBufferSize", -1);
	}

	public static boolean isSocketKeepAlive() {
		return getBoolean("session.keepAlive", false);
	}

	public static boolean isReuseSessionAddress() {
		return getBoolean("session.reuseAddress", false);
	}

	public static boolean isTcpNoDelay() {
		return getBoolean("session.tcpNoDelay", true);
	}

	public static int getSoLinger() {
		return getInt("session.soLinger", -1);
	}

	public static int getReadPacketSize() {
		return getInt("session.readPacketSize", 8192);
	}

	public static int getWritePacketSize() {
		// NIO channel do not handle WSAENOBUFS, so we can't direct write the
		// whole packet to channel.
		return getInt("session.writePacketSize", 1024 * 1024);
	}

	public static String getTcpSession() {
		return get("session.type.tcp", SocketChannelSession.class.getName());
	}

	public static String getUdpSession() {
		return get("session.type.udp", DatagramChannelSession.class.getName());
	}

	public static String getPipeSession() {
		return get("session.type.pipe", PipeSession.class.getName());
	}

	// connectFlow
	public static boolean isConnectFlow() {
		return getBoolean("connectFlow.enable", false);
	}

	public static int geClearTimeAlternation() {
		return getInt("connectFlow.clearTime", 60000);
	}

	public static int getMaxForceoutCount() {
		return getInt("connectFlow.maxForceoutCount", 10000);
	}

	public static String getPrivilege() {
		return get("connectFlow.privilege", null);
	}

	// PackageFlow
	public static boolean isPackageFlow() {
		return getBoolean("packageFlow.enable", false);
	}

	public static int geAlternationTime() {
		return getInt("packageFlow.alternationTime", 10000);
	}

	public static int getMaxExceptionCount() {
		return getInt("packageFlow.maxExceptionCount", 20);
	}

	public static int getPackageLength() {
		return getInt("packageFlow.packageLength", 2028);
	}

	public static int getInterval() {
		return getInt("packageFlow.interval", 600);
	}

	public static String getFileSession() {
		return get("session.type.file");
	}

	// Acceptor

	public static String getTcpAcceptor() {
		return get("acceptor.type.tcp", NonBlockingSessionAcceptor.class.getName());
	}

	public static int getAcceptorBacklog() {
		return getInt("acceptor.backlog", 4096);
	}

	public static boolean isReuseAcceptorAddress() {
		return getBoolean("acceptor.reuseAddress", false);
	}

	public static int getTimerCorePoolSize() {
		return getInt("timer.corePoolSize", 1);
	}

	public static String getServiceName() {
		return get("system.serviceName", "SmileCindy-Pro-3.0 on " + new Date());
	}

	public static long getServerStopTime() {
		return getInt("server.stop.time", 60000);
	}

	public static boolean isEnableCDP() {
		return getBoolean("enableCDP", true);
	}

	public static int getServerConslePort() {
		return getInt("server.consle.listen.port", 800);
	}
}
