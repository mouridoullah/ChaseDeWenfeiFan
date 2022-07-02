package driverJDBC;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppJDBC {

	private static DbConnection connection;

	private static void createGraphDataBase(Connection connection, String query) {

		try (Statement statement = connection.createStatement()) {
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void deleteDataBase(Connection connection) {
		// cleanup
		try (Statement statement = connection.createStatement()) {
			statement.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static ResultSet executeQuery(Connection connection, String query) {
		ResultSet result = null;
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			result = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void main(String[] args) throws SQLException {

		connection = DbConnection.getInstance();

		// create graph
		String query1 = "CREATE (:User {id:1,a:1,b:2}), (:User {id:2,c:3,d:4}), " +
						"(:User {id:1,c:3,d:4})<-[:rel]-(:User {id:3,c:3,d:4})-[:rel]->(:User {id:2,a:10,b:2})";
		createGraphDataBase(connection.getConnection(), query1);

		// query
		String query2 = "MATCH (x:User), (y:User) " + "WHERE NOT (y)-[]-() " + "RETURN y ";
				
				
//"MATCH (x:User), (y:User) " + 
//"WHERE NOT (y)-[]-() " + 
//"AND x.id = y.id AND x.a IS NOT NULL AND y.c IS NOT NULL " + 
//"SET x.c = y.c, x.d = y.d " + 
//"DELETE y " + 
//"RETURN x";
	

		ResultSet result = executeQuery(connection.getConnection(), query2);

		if (result.next()) {
			System.out.println(result.getString("x.id"));
		}
		result.close();

		deleteDataBase(connection.getConnection());

		connection.getConnection().close();
	}

}
