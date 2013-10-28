package DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Usuario;

public class DBUsers extends DBCenter{
	Connection conexao;

	public DBUsers(Connection conexao) {
		super(conexao);
		this.conexao = conexao;
	}
	
	public int getProxIdUser () throws SQLException{
		return getProxId("SELECT MAX(ID) FROM TABLE_USERS","Erro ao Selecionar Ids do Banco de dados");
	}
	
	public boolean isUserCadastrado (String login) throws SQLException{
		return isCadastrado("SELECT LOGIN FROM TABLE_USERS WHERE LOGIN='"+login+"'", "Erro ao Selecionar usuários do BD.");
	}
	
	public boolean CadastraNewUser(Usuario user) throws SQLException{
		try{
		PreparedStatement ps = conexao.prepareStatement("INSERT INTO TABLE_USERS (ID, NOME, SOBRENOME, " +
				"SEXO, DATA_NASCIMENTO, EMAIL, CIDADE, ESTADO, PAIS, PERGUNTA_SECRETA, " +
				"RESPOSTA_SECRETA, LOGIN, SENHA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setInt(1, getProxIdUser());
		ps.setString(2, user.getNome());
		ps.setString(3, user.getSobrenome());
		ps.setString(4, user.getSexo());
		ps.setString(5, user.getData_nascimento());
		ps.setString(6, user.getEmail());
		ps.setString(7, user.getCidade());
		ps.setString(8, user.getEstado());
		ps.setString(9, user.getPais());
		ps.setString(10, user.getPergunta_secreta());
		ps.setString(11, user.getResposta_secreta());
		ps.setString(12, user.getLogin());
		ps.setString(13, user.getSenha());
		ps.execute();
		return true;
		} catch (Exception e){
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getPergResp(String user) throws SQLException{
		return getSecreta("SELECT PERGUNTA_SECRETA, RESPOSTA_SECRETA FROM TABLE_USERS WHERE LOGIN='"+user+"'", "Erro no BD");
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getPergResp4User(String email) throws SQLException{
		return getSecreta("SELECT PERGUNTA_SECRETA, RESPOSTA_SECRETA FROM TABLE_USERS WHERE EMAIL='"+email+"'", "Erro no BD");
	}
	
	public boolean mudarSenha(String login, String senha) throws SQLException{
		
		try {
			PreparedStatement ps = conexao.prepareStatement("UPDATE TABLE_USERS SET SENHA='"+senha+"' WHERE LOGIN='"+login+"'");
			ps.execute();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean verificaEmailData(String email, String data) throws SQLException{
		return isCadastrado("SELECT * FROM TABLE_USERS WHERE EMAIL='"+email+"' AND DATA_NASCIMENTO='"+data+"'", "Erro ao pegar dados do BD");
	}
	
	public boolean mudarLogin(String email, String login) throws SQLException{
		
		try {
			PreparedStatement ps = conexao.prepareStatement("UPDATE TABLE_USERS SET LOGIN='"+login+"' WHERE EMAIL='"+email+"'");
			ps.execute();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList getLoginSenha(String login) throws SQLException{
		return getLoginSenhaAndUser("SELECT LOGIN, SENHA, NOME FROM TABLE_USERS WHERE LOGIN='"+login+"'", "Erro ao pegar dados do banco de dados.");
	}
	
}
