
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MyConnection {

	private static String dbName = Messages.getString("MyConnection.0"); 
	private static String dbUrl = Messages.getString("MyConnection.1") + dbName; 
	private static String dbUsername = Messages.getString("MyConnection.2"); 
	private static String dbPassword = Messages.getString("MyConnection.3"); 

	private static Connection conn = null;

	public static Connection getConnection() {
		if(conn !=null) {
			return conn;
		}
		try {
			Class.forName(Messages.getString("MyConnection.4")); //$NON-NLS-1$
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
