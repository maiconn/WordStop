package Control.Usuarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import DataBase.DBUsers;
import Model.Usuario;

public class ControlaUsuarios {
	private DBUsers dataUsers;
	
	public ControlaUsuarios(Connection conexao){
		dataUsers = new DBUsers(conexao);
	}
	
	public int getProxIDUser() throws SQLException{
		return dataUsers.getProxIdUser();
	}
	
	public boolean isUserCadastrado(String login) throws SQLException{
		return dataUsers.isUserCadastrado(login);
	}
	
	public boolean newUser(Usuario user) throws SQLException{
		return dataUsers.CadastraNewUser(user);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getPerguntaResposta(String usuario) throws SQLException{
		return dataUsers.getPergResp(usuario);
	}
	
	public boolean ModificaSenha(String login, String senha) throws SQLException{
		return dataUsers.mudarSenha(login, senha);
	}
	
	public boolean verificaEmailDataNasc(String email, String data) throws SQLException{
		return dataUsers.verificaEmailData(email, data);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getPerguntaRespostaforUser(String email) throws SQLException{
		return dataUsers.getPergResp4User(email);
	}
	
	public boolean ModificaLogin(String login, String email) throws SQLException{
		return dataUsers.mudarLogin(email, login);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getUserSenha(String login) throws SQLException{
		return dataUsers.getLoginSenha(login);
	}

}
