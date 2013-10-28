package View.Usuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Control.Usuarios.ControlaUsuarios;
import View.ConexaoOracle;
import View.Objetos;

public class EsqueceuSenhaOuLogin extends Objetos{
	private static final long serialVersionUID = 1L;
	private ConexaoOracle getConection;
	private Connection conexao;
	private ControlaUsuarios controlaUsers;
	private JPanel panel, panelSenha, panelUsuario;
	private JTextArea info;
	private JComboBox SelectOption, combo_dia, combo_mes, combo_ano;
	private JLabel nomeUser, perguntaSecreta, RespostaSecreta, RespostaSecretaUser, PerguntaSecretaUser,
			data_nascimento, email;
	private JTextField tfNomeUser, tfPerguntaSecreta, tfRespostaSecreta, tfEmail, tfPerguntaSecretaUser,
			tfRespSecretUser;
	private JButton Checar, RecuperarSenha, ChecarUser, Recuperar_User;
	@SuppressWarnings("unchecked")
	private ArrayList getPerguntaResp = new ArrayList();
	
	public EsqueceuSenhaOuLogin(){
		getConection = new ConexaoOracle();
		try {
			conexao = getConection.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		controlaUsers = new ControlaUsuarios(conexao);
		graficos();
	}
	public void graficos(){
		//InitComponentes
		panel = new JPanel();
		panel.setLayout(null);
		
		//PanelInfo
		info = new JTextArea();
		info.setBorder(BorderFactory.createEtchedBorder());
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setText("Para fazer a recuperação de conta, selecione a opção desejada e prossiga.\n" +
				"\nObs: \n1 - Para recuperar a senha, basta saber a sua resposta secreta.\n" +
				"2 - Para recuperar o nome de usuário, precisará saber o e-mail cadastrado, informar data de nascimento" +
				" e a resposta secreta.");
		info.setBounds(10,10,495,113);
		info.setEnabled(false);
		info.setEditable(false);
		info.setOpaque(true);
		panel.add(info);
		
		SelectOption = newJComboBox(100,130,300,28);
		SelectOption.addItem("Selecione Opção Desejada");
		SelectOption.addItem("Recuperar Senha");
		SelectOption.addItem("Recuperar Nome de Usuário");
		SelectOption.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (SelectOption.getSelectedIndex()==1){
					panelSenha.setVisible(true);
				} else {
					panelSenha.setVisible(false);
				}	
				
				if (SelectOption.getSelectedIndex()==2){
					panelUsuario.setVisible(true);
				} else {
					panelUsuario.setVisible(false);
				}
			}
		});
		panel.add(SelectOption);
		
		//PANEL RECUPERAR SENHA
		panelSenha = newJPanel(10,165,495,230);
		panelSenha.setBorder(BorderFactory.createTitledBorder("Recuperar Senha"));
		panel.add(panelSenha);
		
		nomeUser = newJLabelPadrao("Nome de Usuário", 88,20,200,25);
		panelSenha.add(nomeUser);
		
		tfNomeUser = newJTextFieldPadrao(243,18,150,28);
		panelSenha.add(tfNomeUser);
		
		Checar = newJButton("Checar",200,50,90,30);
		panelSenha.add(Checar);
		
		perguntaSecreta = newJLabelPadrao("Pergunta Secreta", 10, 100, 200, 25);
		panelSenha.add(perguntaSecreta);
		
		tfPerguntaSecreta = newJTextFieldPadrao(175, 98, 310, 28);
		tfPerguntaSecreta.setEnabled(false);
		panelSenha.add(tfPerguntaSecreta);
		
		RespostaSecreta = newJLabelPadrao("Resposta Secreta", 10, 140, 200, 25);
		panelSenha.add(RespostaSecreta);
		
		tfRespostaSecreta = newJTextFieldPadrao(175, 138, 310, 28);
		tfRespostaSecreta.setEnabled(false);
		panelSenha.add(tfRespostaSecreta);
		
		RecuperarSenha = newJButton("Recuperar Senha", 150, 185, 200, 30);
		RecuperarSenha.setEnabled(false);
		panelSenha.add(RecuperarSenha);
		
		panelSenha.setVisible(false);
		
		//PANEL PARA RECUPERAR LOGIN
		panelUsuario = newJPanel(10,165,495,250);
		panelUsuario.setBorder(BorderFactory.createTitledBorder("Recuperar Nome de Usuário"));
		panel.add(panelUsuario);
		
		data_nascimento = newJLabelPadrao("Data de Nascimento",20,20,200,25);
		panelUsuario.add(data_nascimento);
		
		JLabel dia = new JLabel("dia");
		dia.setBounds(215,20,200,25);
		panelUsuario.add(dia);
		
		combo_dia = newJComboBox(235,18,53,28);
		combo_dia.addItem("");
		for (int i=1 ; i<=31 ; i++){
			combo_dia.addItem(i);
		}
		panelUsuario.add(combo_dia);
		
		JLabel mes = new JLabel("mês");
		mes.setBounds(292,20,200,25);
		panelUsuario.add(mes);
		
		combo_mes = newJComboBox(320,18,53,28);
		combo_mes.addItem("");
		for (int i=1 ; i<=12 ; i++){
			combo_mes.addItem(i);
		}
		panelUsuario.add(combo_mes);
		
		JLabel ano = new JLabel("ano");
		ano.setBounds(375,20,200,25);
		panelUsuario.add(ano);

		combo_ano = newJComboBox(399, 18, 70, 28);
		combo_ano.addItem("");
		for (int i=1900 ; i<=2005 ; i++){
			combo_ano.addItem(i);
		}
		panelUsuario.add(combo_ano);
		
		email = newJLabelPadrao("E-mail", 120, 60, 100, 25);
		panelUsuario.add(email);
		
		tfEmail = newJTextFieldPadrao(185, 58, 200, 28);
		panelUsuario.add(tfEmail);
		
		ChecarUser = newJButton("Checar", 200,90,90,30);
		panelUsuario.add(ChecarUser);
		
		PerguntaSecretaUser = newJLabelPadrao("Pergunta Secreta", 10, 130, 200, 25);
		panelUsuario.add(PerguntaSecretaUser);
		
		tfPerguntaSecretaUser = newJTextFieldPadrao(175, 128, 310, 28);
		tfPerguntaSecretaUser.setEnabled(false);
		panelUsuario.add(tfPerguntaSecretaUser);
		
		RespostaSecretaUser = newJLabelPadrao("Resposta Secreta", 10, 170, 200, 25);
		panelUsuario.add(RespostaSecretaUser);
		
		tfRespSecretUser = newJTextFieldPadrao(175, 168, 310, 28);
		tfRespSecretUser.setEnabled(false);
		panelUsuario.add(tfRespSecretUser);
		
		Recuperar_User = newJButton("Recuperar Usuário", 150, 210, 200, 30);
		Recuperar_User.setEnabled(false);
		panelUsuario.add(Recuperar_User);
		
		panelUsuario.setVisible(false);
		
		//Fechar a conexao se o JDialog fechar
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					conexao.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}				
			}
		});
		
		//Set JDialog
		add(panel);
		setTitle("Recuperar Conta");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(520,450);				
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == Checar){
			if (!tfNomeUser.getText().trim().equals("")){
				try {
					if (controlaUsers.isUserCadastrado(tfNomeUser.getText())){
						tfRespostaSecreta.setEnabled(true);
						RecuperarSenha.setEnabled(true);
						tfNomeUser.setEnabled(false);
						Checar.setEnabled(false);
						
						
						getPerguntaResp = controlaUsers.getPerguntaResposta(tfNomeUser.getText());
						tfPerguntaSecreta.setText(getPerguntaResp.get(0).toString());						
					} else {
						newJOPWarningMessage("Usuário Inválido!", "Warning");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				newJOPInfMsg("Digite um nome de usuário!", "Message");
			}
		}
		
		if (evento.getSource() == RecuperarSenha){
			if (tfRespostaSecreta.getText().equals(getPerguntaResp.get(1))){
				new MudarSenha(conexao, tfNomeUser.getText());
				dispose();
				try {
					conexao.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}	
			} else {
				newJOPWarningMessage("Resposta Inválida!", "Warning");
			}
		}
		if (evento.getSource() == ChecarUser){
			String date = combo_dia.getSelectedItem().toString()+"/"+combo_mes.getSelectedItem().toString()+"/"+combo_ano.getSelectedItem().toString();
			if (combo_dia.getSelectedIndex() != 0 && combo_mes.getSelectedIndex() != 0 && combo_ano.getSelectedIndex() != 0){
				if (!tfEmail.getText().trim().equals("")){
					try {
						if (controlaUsers.verificaEmailDataNasc(tfEmail.getText(), date)){
							tfRespSecretUser.setEnabled(true);
							Recuperar_User.setEnabled(true);
							getPerguntaResp = controlaUsers.getPerguntaRespostaforUser(tfEmail.getText());
							tfPerguntaSecretaUser.setText(getPerguntaResp.get(0).toString());
						} else {
							newJOPInfMsg("E-mail ou data de nascimento não conferem!", "Message");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					newJOPInfMsg("E-mail Inválido!", "Message");
				}
			}else {
				newJOPInfMsg("Data Inválida!", "Message");
			}
			
		}
		if (evento.getSource() == Recuperar_User){
			if (tfRespSecretUser.getText().equals(getPerguntaResp.get(1))){
				String login = JOptionPane.showInputDialog(null, "Digite novo nome de usuário.");
				while(login.trim().equals("")){
					newJOPWarningMessage("Digite um nome de usuário!", "Warning");
					login = JOptionPane.showInputDialog(null, "Digite novamente o nome usuário.");
				}
				newJOPInfMsg("Usuário Modificado Com Sucesso!", "Usuário Modificado");
				try {
					controlaUsers.ModificaLogin(login, tfEmail.getText());
					conexao.close();
					dispose();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				newJOPWarningMessage("Resposta Inválida!", "Warning");
			}
		}
		
	}


}
