package com.brydio;

import java.rmi.server.UnicastRemoteObject;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {

	private static final long serialVersionUID = -3701460066566430211L;

	// 这个实现必须有一个显式的构造函数，并且要抛出一个RemoteException异常
	public CalculatorImpl() throws java.rmi.RemoteException {
		super();
	}

	public long add(long a, long b) throws java.rmi.RemoteException {
		return a + b;
	}

	public long sub(long a, long b) throws java.rmi.RemoteException {
		return a - b;
	}

	public long mul(long a, long b) throws java.rmi.RemoteException {
		return a * b;
	}

	public long div(long a, long b) throws java.rmi.RemoteException {
		return a / b;
	}
}