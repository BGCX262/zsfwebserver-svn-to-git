package com.testStrus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloWorld {
	
	public static void main(String[] args) throws SQLException {
		System.out.println("Hello world!");
		
		new com.mysql.jdbc.Driver();
	}
	
	public Connection getConn() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", "root", "root");
	}

}
