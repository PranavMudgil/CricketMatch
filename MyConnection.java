import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

	private static String dbName = "sys";
	private static String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;
	private static String dbUsername = "root";
	private static String dbPassword = "root";

	private static Connection conn = null;

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		} catch (ClassNotFoundException | SQLException e) {

			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
