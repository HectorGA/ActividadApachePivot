package dad.recetapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataBase {
	
	private static final ResourceBundle CONFIG = ResourceBundle.getBundle(DataBase.class.getPackage().getName() + ".database");
	//	Reflecci�n JAVA, es cuando puedes acceder a la informaci�n del objeto, 
	//	algo as� como los metadatos de los objetos.	
	private static Connection connection = null;
	
	//	Creamos un construcctor privado para que no se puedea crear una estancia del objeto DataBase
	private DataBase(){}
	
	public static Connection getConnection() throws SQLException{
		if(connection == null || connection.isClosed()){
			connection = connect();
		}
		return connection;
	}
	
	private static final void registerDriver(){
		try {
			Class.forName(CONFIG.getString("db.driver.classname"));
		} catch (ClassNotFoundException e) {
			System.err.println("Error al cargar el driver JDBC");
		}
	}
	
	public static Connection connect(String url, String username, String password) throws SQLException{
		registerDriver();
		return DriverManager.getConnection(url, username, password);
	}
	
	public static Connection connect() throws SQLException {
		return connect(CONFIG.getString("db.url"), CONFIG.getString("db.username"), CONFIG.getString("db.password"));
	}
	
	public static void disconnect(Connection connection) throws SQLException {
		if(connection != null && !connection.isClosed()){
			connection.close();
		}
	}
	
	public static void disconnect() throws SQLException{
		disconnect(connection);
		connection = null;
	}
	
	/**
	 * Abre una conexi�n con la base de datos usando los par�metros del fichero
	 * de propiedades "database.properties
	 * @return Conex�on abierta
	 * @throws SQLExeption En caso de no poder abrir la conexi�n por alg�n motivo.
	 */
	public static Boolean test(){
		Boolean testOk = false;
		try {
			Connection c = DataBase.getConnection();
			DataBase.disconnect(c);
			testOk = true;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return testOk;
	}
}
