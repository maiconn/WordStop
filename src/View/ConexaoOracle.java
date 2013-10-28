package View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoOracle {
	public Connection getConnection() throws SQLException{
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "System", "123");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			throw new SQLException("Não foi possível conectar com o banco de dados!");
		}		
	}

}
