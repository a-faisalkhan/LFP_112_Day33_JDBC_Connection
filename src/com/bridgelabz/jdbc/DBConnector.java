package com.bridgelabz.jdbc;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DBConnector {

	public static String JDBC_STR = "jdbc:mysql://localhost:3306/payroll_service";
	public static String USERNAME = "darpan";
	public static String PASSWORD = "darpan";

	static Connection connection;

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(
					"JDBC Driver class is not found");
			e.printStackTrace();
		}
		listDrivers();
		try {
			connection = DriverManager.getConnection(
					JDBC_STR, USERNAME, PASSWORD);
			System.out
					.println("Connection is established.");
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(
					"Connection can not established.");
			return null;
		}
	}

	public static void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void listDrivers() {
		Enumeration<Driver> drivers = DriverManager
				.getDrivers();

		while (drivers.hasMoreElements()) {

			Driver driver = drivers.nextElement();
			System.out.println(driver.getClass().getName());
		}
	}
}
