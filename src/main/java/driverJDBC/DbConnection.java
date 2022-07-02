package driverJDBC;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private static DbConnection instance;
	private Connection connection;
	private String url = "jdbc:neo4j:bolt://localhost/";
	private String username = "neo4j";
	private String password = "test";

	private DbConnection() throws SQLException {
		try {
			Class.forName("org.neo4j.jdbc.Driver");
			this.connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException ex) {
			System.out.println("Something is wrong with the DB connection String : " + ex.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static DbConnection getInstance() throws SQLException {
		if (instance == null) {
			instance = new DbConnection();
		} else if (instance.getConnection().isClosed()) {
			instance = new DbConnection();
		}
		return instance;
	}

}