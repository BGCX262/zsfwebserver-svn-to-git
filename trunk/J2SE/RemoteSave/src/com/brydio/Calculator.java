package com.brydio;

import java.rmi.Remote;

public interface Calculator extends Remote {

	public long add(long a, long b) throws java.rmi.RemoteException;

	public long sub(long a, long b) throws java.rmi.RemoteException;

	public long mul(long a, long b) throws java.rmi.RemoteException;

	public long div(long a, long b) throws java.rmi.RemoteException;

	public int getState() throws java.rmi.RemoteException;
	public void setState(int state) throws java.rmi.RemoteException;
}