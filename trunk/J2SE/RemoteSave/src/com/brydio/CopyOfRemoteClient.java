package com.brydio;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

public class CopyOfRemoteClient {
	
	private static String ip;
	private static String port;

	public static void main(String[] args) {
		ip = args[0];
		port = args[1];
		try {
			Calculator c = (Calculator) Naming.lookup("//"+ip+":"+port+"/helloObj");
			for (int i = 100; i < 200; i++) {
				c.setState(i);
				Thread.sleep(1);
			}
		} catch (MalformedURLException murle) {
			System.out.println();
			System.out.println("MalformedURLException");
			System.out.println(murle);
		} catch (RemoteException re) {
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (java.lang.ArithmeticException ae) {
			System.out.println();
			System.out.println("java.lang.ArithmeticException");
			System.out.println(ae);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}