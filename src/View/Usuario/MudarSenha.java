package View.Usuario;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import Control.Usuarios.ControlaUsuarios;
import View.Objetos;

public class MudarSenha extends Objetos{
	private static final long serialVersionUID = 1L;
	private ControlaUsuarios controlaUsers;
	private JPanel panel;
	private JPasswordField pfSenha, pfConfirmarSenha;
	private JLabel senha, confirmar_senha;
	private JButton AlterarSenha;
	private String login;
	private Connection conexao;
	
	public MudarSenha(Connection conexao, String login){
		controlaUsers = new ControlaUsuarios(conexao);
		this.conexao = conexao;
		this.login = login;
		Graficos();
	}
	public void Graficos(){
		panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(BorderFactory.createTitledBorder(""));
		
		senha = newJLabelPadrao("Nova Senha", 67, 20, 200, 25);
		panel.add(senha);
		
		pfSenha = new JPasswordField();
		pfSenha.setBounds(170, 18, 150, 28);
		panel.add(pfSenha);
		
		confirmar_senha = newJLabelPadrao("Confirmar Senha", 18, 60, 200, 25);
		panel.add(confirmar_senha);
		
		pfConfirmarSenha = new JPasswordField();
		pfConfirmarSenha.setBounds(170, 58, 150, 28);
		panel.add(pfConfirmarSenha);
		
		AlterarSenha = newJButton("Alterar Senha", 100, 100, 180, 30);
		panel.add(AlterarSenha);
		
		//Set JDialog		
		add(panel);
		setTitle("Alterar Senha");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(380,170);				
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public void actionPerformed(ActionEvent evento) {
		if (!pfSenha.getText().trim().equals("")){
			if (pfSenha.getText().equals(pfConfirmarSenha.getText())){
				try {
					controlaUsers.ModificaSenha(login, pfSenha.getText());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				newJOPInfMsg("Senha Alterada Com Sucesso!", "Sucessfull");
				dispose();
				try {
					conexao.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				newJOPInfMsg("Senhas Não Conferem!", "Redigitar Senha");
			}
		} else {
			newJOPInfMsg("Digite uma senha!", "Senha");
		}
		
	}		
}
