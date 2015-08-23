package com.brydio;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RemoteServer {
	
	private static String ip;
	private static String port;
	static CalculatorImpl ttt;
	
	static {
		try {
			ttt = new CalculatorImpl();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public RemoteServer() {
		try {
			// 启动注册表
			LocateRegistry.createRegistry(Integer.valueOf(port).intValue());
			// 奖名称绑定到对象
			Naming.rebind("//"+ip+":"+port+"/helloObj", ttt);

			System.out.println("RMI服务器正在运行。。。。。。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ip = args[0];
		port = args[1];
		RemoteServer remoteServer = new RemoteServer();
		remoteServer.new TestThread().start();
	}

	int count = 0;
	class TestThread extends Thread {
		public void run() {
			while (true) {
				System.out.println(ttt.getState());
				if (count++ > 200)
					break;
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}