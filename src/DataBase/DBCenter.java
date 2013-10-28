package DataBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBCenter {
	Connection conexao;
	public DBCenter(Connection conexao){
		this.conexao = conexao;
	}
	
	public int getProxId(String sql, String msg) throws SQLException{
		try {
			ResultSet rs = conexao.prepareStatement(sql).executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;
		} catch (SQLException ex) {
			throw new SQLException(msg);
		}
	}
	
	public boolean isCadastrado(String sql, String msg) throws SQLException{
		ResultSet rs;
		try {
			rs = conexao.prepareStatement(sql).executeQuery();
			if (rs.next()){
				return true;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new SQLException(msg);
		}		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getSecreta(String sql, String msg) throws SQLException{
		ResultSet rs;
		ArrayList resultado = new ArrayList();
		try {
			rs = conexao.prepareStatement(sql).executeQuery();
			while(rs.next()){
				resultado.add(rs.getString(1));
				resultado.add(rs.getString(2));
			}
			return resultado;
		} catch (SQLException e) {
			throw new SQLException(msg);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getLoginSenhaAndUser(String sql, String msg) throws SQLException{
		ResultSet rs;
		ArrayList resultado = new ArrayList();
		try {
			rs = conexao.prepareStatement(sql).executeQuery();
			while(rs.next()){
				resultado.add(rs.getString(1));
				resultado.add(rs.getString(2));
				resultado.add(rs.getString(3));
			}
			return resultado;
		} catch (SQLException e) {
			throw new SQLException(msg);
		}
		
	}

}
