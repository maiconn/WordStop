package View.Usuario;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Control.Usuarios.ControlaUsuarios;
import Model.Usuario;
import View.Objetos;
import View.ConexaoOracle;

public class novoUsuario extends Objetos{
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel titulo, nome, sobrenome,sexo, data_nascimento, email, pergunta_secreta, 
			resposta_secreta, Cidade, Estado, Pais, login, senha, confirmar_senha,
			im_NovoJogador, obgNome, obgSobrenome, obgSexo, obgData_Nascimento, obgEmail, 
			obgPergunta,obgResposta, obgCidade, obgEstado, obgPais, obgLogin;
	private ImageIcon img_NovoJogador;
	private JTextField tfNome, tfSobrenome, tfEmail, tfResposta_secreta, tfCidade,
			tfEstado, tfPais, tfLogin, tfPerguntaSecreta;
	private JPasswordField pfSenha, pfConfirmar_senha;
	private JComboBox combo_sexo, combo_pergunta_secreta, combo_dia, combo_mes, combo_ano;
	private JButton Concluir;
	private Connection conexao;
	private ConexaoOracle getConnection;
	private ControlaUsuarios controlaUsers;

	public novoUsuario(){
		getConnection = new ConexaoOracle();
		try {
			conexao = getConnection.getConnection();
			controlaUsers = new ControlaUsuarios(conexao);
			grafico();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	public void grafico(){
		//Foto do Login
		img_NovoJogador = new ImageIcon("src/images/novoJogador.jpg");
		im_NovoJogador = new JLabel(img_NovoJogador);
		im_NovoJogador.setBounds(200,260,img_NovoJogador.getIconWidth(), img_NovoJogador.getIconWidth());
		panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(true);
		panel.setBackground(Color.white);

		//Construindo Componentes		
		titulo = newJLabelTitulo("Cadastro de Usuários", 167, 5, 280, 25);
		panel.add(titulo);

		nome = newJLabelPadrao("Nome", 144, 60, 100, 25);
		panel.add(nome);
		
		obgNome = newJLabelObrigatoria(134, 60, 100, 25);
		panel.add(obgNome);

		tfNome = newJTextFieldPadrao(190, 58, 150, 28);
		panel.add(tfNome);

		sobrenome = newJLabelPadrao("Sobrenome", 95, 100, 130, 25);
		panel.add(sobrenome);
		
		obgSobrenome = newJLabelObrigatoria(85, 100, 130, 25);
		panel.add(obgSobrenome);

		tfSobrenome = newJTextFieldPadrao(190, 98, 300, 28);
		panel.add(tfSobrenome);

		sexo = newJLabelPadrao("Sexo", 144, 140, 100, 25);
		panel.add(sexo);
		
		obgSexo = newJLabelObrigatoria(134, 140, 100, 25);
		panel.add(obgSexo);

		combo_sexo = newJComboBox(190, 137, 115, 28);
		combo_sexo.addItem("Selecione");
		combo_sexo.addItem("Masculino");
		combo_sexo.addItem("Feminino");
		panel.add(combo_sexo);

		data_nascimento = newJLabelPadrao("Data de Nascimento", 5, 180, 230, 25);
		panel.add(data_nascimento);
		
		obgData_Nascimento = newJLabelObrigatoria(-2, 180, 230, 25);
		panel.add(obgData_Nascimento);

		JLabel dia = new JLabel("dia");
		dia.setBounds(200, 178, 60, 28);
		panel.add(dia);

		combo_dia = newJComboBox(220, 178, 53, 28);
		combo_dia.addItem("");
		for (int i=1 ; i<=31 ; i++){
			combo_dia.addItem(i);
		}
		panel.add(combo_dia);

		JLabel mes = new JLabel("mês");
		mes.setBounds(290, 178, 60, 28);
		panel.add(mes);

		combo_mes = newJComboBox(320, 178, 53, 28);
		combo_mes.addItem("");
		for (int i=1 ; i<=12 ; i++){
			combo_mes.addItem(i);
		}
		combo_mes.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getDiasMes(Integer.parseInt(combo_mes.getSelectedItem().toString()));
			}
		});
		panel.add(combo_mes);

		JLabel ano = new JLabel("ano");
		ano.setBounds(390, 178, 60, 28);
		panel.add(ano);

		combo_ano = newJComboBox(415, 178, 70, 28);
		combo_ano.addItem("");
		for (int i=1900 ; i<=2005 ; i++){
			combo_ano.addItem(i);
		}
		panel.add(combo_ano);

		email = newJLabelPadrao("E-Mail", 126, 220, 100, 25);
		panel.add(email);
		
		obgEmail = newJLabelObrigatoria(116, 220, 100, 25);
		panel.add(obgEmail);

		tfEmail = newJTextFieldPadrao(190, 218, 200, 28);
		panel.add(tfEmail);

		Cidade = newJLabelPadrao("Cidade", 125, 260, 100, 25);
		panel.add(Cidade);
		
		obgCidade = newJLabelObrigatoria(115, 260, 100, 25);
		panel.add(obgCidade);

		tfCidade = newJTextFieldPadrao(190, 258, 150, 28);
		panel.add(tfCidade);

		Estado = newJLabelPadrao("Estado", 125, 300, 100, 25);
		panel.add(Estado);
		
		obgEstado = newJLabelObrigatoria(115, 300, 100, 25);
		panel.add(obgEstado);

		tfEstado = newJTextFieldPadrao(190, 298, 150, 28);
		panel.add(tfEstado);

		Pais = newJLabelPadrao("País", 144, 340, 100, 25);
		panel.add(Pais);
		
		obgPais = newJLabelObrigatoria(134, 340, 100, 25);
		panel.add(obgPais);

		tfPais = newJTextFieldPadrao(190, 338, 150, 28);
		panel.add(tfPais);

		pergunta_secreta = newJLabelPadrao("Pergunta Secreta", 25, 400, 200, 25);
		panel.add(pergunta_secreta);

		combo_pergunta_secreta = newJComboBox(190, 398, 400, 28);
		combo_pergunta_secreta.setToolTipText("A resposta desta Pergunta irá ser usada para recuperar sua senha\n caso perdê-la!");
		combo_pergunta_secreta.addItem("Qual foi a sua melhor professora?");
		combo_pergunta_secreta.addItem("Nome do seu melhor amigo de infância.");
		combo_pergunta_secreta.addItem("Nome da Vó.");
		combo_pergunta_secreta.addItem("Nome do seu animal de Estimação.");
		combo_pergunta_secreta.addItem("Número da sorte.");
		combo_pergunta_secreta.addItem("Outra");
		combo_pergunta_secreta.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (combo_pergunta_secreta.getSelectedIndex() == 5){
					tfPerguntaSecreta.setVisible(true);
				} else {
					tfPerguntaSecreta.setVisible(false);
					obgPergunta.setVisible(false);
				}

			}
		});
		panel.add(combo_pergunta_secreta);

		tfPerguntaSecreta = newJTextFieldPadrao(190, 438, 300, 28);
		tfPerguntaSecreta.setVisible(false);
		panel.add(tfPerguntaSecreta);
		
		obgPergunta = newJLabelObrigatoria(180, 438, 300, 28);
		panel.add(obgPergunta);

		resposta_secreta = newJLabelPadrao("Resposta", 106, 480, 100, 25);
		panel.add(resposta_secreta);
		
		obgResposta = newJLabelObrigatoria(96, 480, 100, 25);
		panel.add(obgResposta);

		tfResposta_secreta = newJTextFieldPadrao(190, 478, 200, 28);
		panel.add(tfResposta_secreta);

		login = newJLabelPadrao("Nome de Usuário", 37, 520, 300, 25);
		panel.add(login);
		
		obgLogin = newJLabelObrigatoria(27, 520, 300, 25);
		panel.add(obgLogin);

		tfLogin = newJTextFieldPadrao(190, 518, 150, 28);
		panel.add(tfLogin);

		senha = newJLabelPadrao("Senha", 137, 560, 100, 25);
		panel.add(senha);

		pfSenha = new JPasswordField();
		pfSenha.setBounds(190, 558, 150, 28);
		panel.add(pfSenha);

		confirmar_senha = newJLabelPadrao("Confirmar Senha", 38, 600, 200, 25);
		panel.add(confirmar_senha);

		pfConfirmar_senha = new JPasswordField();
		pfConfirmar_senha.setBounds(190, 598, 150, 28);
		panel.add(pfConfirmar_senha);

		Concluir = newJButton("Concluir >", 430, 595, 150, 35);
		panel.add(Concluir);

		//Image
		panel.add(im_NovoJogador);
		
		
		//Fechar a conexao se o JDialog fechar
		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					conexao.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});

		//Set JDialog		
		add(panel);
		setTitle("Novo Jogador");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(600,670);				
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);			
	}

	public void getDiasMes(int mes){
		if (mes==1 || mes==3 || mes==5 || mes==7 || mes== 8 || mes == 10 || mes ==12){
			combo_dia.removeAllItems();
			combo_dia.addItem("");
			for (int i=1 ; i<=31 ; i++){
				combo_dia.addItem(i);
			}
		} else if (mes == 2){
			combo_dia.removeAllItems();
			combo_dia.addItem("");
			for (int i=1 ; i<=29 ; i++){
				combo_dia.addItem(i);
			}
		} else {
			combo_dia.removeAllItems();
			combo_dia.addItem("");
			for (int i=1 ; i<=30 ; i++){
				combo_dia.addItem(i);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void Verifica(){
		boolean Cadastrar = true;
		boolean Senha = true;
		String Pergunta = null;
		if (tfNome.getText().trim().equals("")){
			obgNome.setVisible(true);
			Cadastrar = false;
		}else {
			obgNome.setVisible(false);
		}
		
		if (tfSobrenome.getText().trim().equals("")){
			obgSobrenome.setVisible(true);
			Cadastrar = false;
		}else {
			obgSobrenome.setVisible(false);
		}
		
		if (combo_sexo.getSelectedIndex() == 0){
			obgSexo.setVisible(true);
			Cadastrar = false;
		}else {
			obgSexo.setVisible(false);
		}
		
		if (combo_dia.getSelectedIndex() == 0 || combo_mes.getSelectedIndex() == 0 || combo_ano.getSelectedIndex() ==0){
			obgData_Nascimento.setVisible(true);
			Cadastrar = false;
		}else {
			obgData_Nascimento.setVisible(false);
		}
		
		if (tfEmail.getText().trim().equals("")){
			obgEmail.setVisible(true);
			Cadastrar = false;
		}else {
			obgEmail.setVisible(false);
		}
		
		if (tfCidade.getText().trim().equals("")){
			obgCidade.setVisible(true);
			Cadastrar = false;
		}else {
			obgCidade.setVisible(false);
		}
		
		if (tfEstado.getText().trim().equals("")){
			obgEstado.setVisible(true);
			Cadastrar = false;
		}else {
			obgEstado.setVisible(false);
		}
		
		if (tfPais.getText().trim().equals("")){
			obgPais.setVisible(true);
			Cadastrar = false;
		}else {
			obgPais.setVisible(false);
		}
		
		if (combo_pergunta_secreta.getSelectedIndex()==5){
			if (tfPerguntaSecreta.getText().trim().equals("")){
				obgPergunta.setVisible(true);
				Cadastrar = false;
			} else {
				obgPergunta.setVisible(false);
				Pergunta = tfPerguntaSecreta.getText();
			}
		} else {
			Pergunta = combo_pergunta_secreta.getSelectedItem().toString();
		}
		
		if (tfResposta_secreta.getText().trim().equals("")){
			obgResposta.setVisible(true);
			Cadastrar = false;
		}else {
			obgResposta.setVisible(false);
		}
		
		if (tfResposta_secreta.getText().trim().equals("")){
			obgResposta.setVisible(true);
			Cadastrar = false;
		}else {
			obgResposta.setVisible(false);
		}
		
		try {
			if (tfLogin.getText().trim().equals("")){
				obgLogin.setVisible(true);
				Cadastrar = false;
			} else if(controlaUsers.isUserCadastrado(tfLogin.getText())==true){
				newJOPInfMsg("Nome de usuário já cadastrado!", "Login");
				Cadastrar = false;
			} else {
				obgLogin.setVisible(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!pfSenha.getText().equals(pfConfirmar_senha.getText())){
			Senha = false;
			newJOPWarningMessage("Senhas Não Conferem", "Redigitar Senhas");
			pfConfirmar_senha.setText("");
			pfSenha.setText("");
		}
		
		if (Cadastrar && Senha){
			Usuario newUser = new Usuario();
			newUser.setNome(tfNome.getText());
			newUser.setSobrenome(tfSobrenome.getText());
			newUser.setSexo(combo_sexo.getSelectedItem().toString());
			newUser.setData_nascimento(combo_dia.getSelectedItem().toString()+"/"+combo_mes.getSelectedItem().toString()+"/"+combo_ano.getSelectedItem().toString());
			newUser.setEmail(tfEmail.getText());
			newUser.setCidade(tfCidade.getText());
			newUser.setEstado(tfEstado.getText());
			newUser.setPais(tfPais.getText());
			newUser.setPergunta_secreta(Pergunta);
			newUser.setResposta_secreta(tfResposta_secreta.getText());
			newUser.setLogin(tfLogin.getText());
			newUser.setSenha(pfSenha.getText());
			try {
				controlaUsers.newUser(newUser);
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			newJOPInfMsg("Usuário Cadastrado Com Sucesso", "SUCESSO");
			dispose();
			
		} else if (!Cadastrar) {
			newJOPWarningMessage("Preencher Campos * Obrigatórios", "Preencher Campos");
			pfConfirmar_senha.setText("");
			pfSenha.setText("");
		}
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if(evento.getSource() == Concluir){
			Verifica();
		}

	}

}
