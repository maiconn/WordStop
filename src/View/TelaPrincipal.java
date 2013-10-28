package View;


import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import Control.Usuarios.ControlaUsuarios;
import View.Usuario.EsqueceuSenhaOuLogin;
import View.Usuario.novoUsuario;

public class TelaPrincipal extends Objetos{
	private static final long serialVersionUID = 1L;
	private JMenuBar menubar;
	private JMenu arquivo;
	private JMenuItem sair, criar_novaConta, logoff;
	private JFrame frame;
	private JPanel panelTelaLogin, panelLogin, PanelPrincipal;
	private ImageIcon imageIcon, imageIconTelaInicial;
	private Image icon;
	private JLabel planoFundo, nomeUser, senhaUser;
	private JTextField tfNomeUser;
	private JPasswordField pfSenhaUser;
	private JButton Entrar, esqueceu;
	@SuppressWarnings("unchecked")
	private ArrayList getUserSenha;
	private ControlaUsuarios controlaUsers;
	private Connection conexao;
	private TelaInicial telaInicial;
	private getHour pegaHora;
	private JSeparator separador;
	

	public TelaPrincipal(){
		//Criando Frame
		frame = new JFrame("WordStop | The Game");

		//Criando Panel Principal
		PanelPrincipal = new JPanel();
		PanelPrincipal.setLayout(null);

		//Criando Ícone
		imageIcon = new ImageIcon("src/images/WordStopIcon.png");
		icon = imageIcon.getImage();

		//Criando Imagem TelaPrincipal
		imageIconTelaInicial = new ImageIcon("src/images/TelaInicial.jpg");
		planoFundo = new JLabel(imageIconTelaInicial);
		planoFundo.setBounds(0,0,imageIconTelaInicial.getIconWidth(),imageIconTelaInicial.getIconHeight());
		panelTelaLogin = new JPanel();
		panelTelaLogin.setVisible(true);
		panelTelaLogin.setLayout(null);
		panelTelaLogin.setBounds(0,0, 800, 583);
		PanelPrincipal.add(panelTelaLogin);

		//Contruindo Menus
		menubar = new JMenuBar();
		//Arquivo
		arquivo = new JMenu("Arquivo");		
		criar_novaConta = new JMenuItem("Criar Nova Conta");
		criar_novaConta.addActionListener(this);
		separador = new JSeparator();
		sair = new JMenuItem("Sair");
		sair.addActionListener(this);
		logoff = new JMenuItem("Logoff");
		logoff.setEnabled(false);
		logoff.addActionListener(this);		
		arquivo.add(criar_novaConta);
		arquivo.add(separador);
		arquivo.add(logoff);
		arquivo.add(sair);

		menubar.add(arquivo);

		//Componentes
		//Instanciando Tela Inicial
		telaInicial = new TelaInicial();
		
		//BUTTON ENTRAR
		
		//PanelVisible(2);

		//LOGIN
		panelLogin = new JPanel();
		panelLogin.setLayout(null);
		panelLogin.setBounds(180,180,350,160);
		panelLogin.setBorder(BorderFactory.createTitledBorder("Login"));
		panelTelaLogin.add(panelLogin);

		

		nomeUser = newJLabelPadrao("Nome de Usuário", 15, 25, 230, 25);
		panelLogin.add(nomeUser);

		tfNomeUser = newJTextFieldPadrao(170, 22, 150, 28);
		panelLogin.add(tfNomeUser);

		senhaUser = newJLabelPadrao("Senha", 115, 65, 120, 25);
		panelLogin.add(senhaUser);

		pfSenhaUser = new JPasswordField();
		pfSenhaUser.setBounds(170,62,150,28);
		panelLogin.add(pfSenhaUser);

		Entrar = newJButton("Entrar", 127, 100, 110, 38);
		panelLogin.add(Entrar);		

		esqueceu = new JButton("Esqueceu sua senha ou login");
		esqueceu.setBounds(83, 138, 200, 20);
		esqueceu.addActionListener(this);
		panelLogin.add(esqueceu);
		

		

		//Set BackGround PanelTelaLogin
		panelTelaLogin.add(planoFundo);

		//SetFrame	
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				fechaFrame();
			}
		});
		frame.setJMenuBar(menubar);
		frame.add(PanelPrincipal);			
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(800,578);
		frame.setVisible(true);		
		frame.setIconImage(icon);
		frame.setLocationRelativeTo(null);
	}
	@SuppressWarnings("deprecation")
	public void Entrar(){
		ConexaoOracle con = new ConexaoOracle();
		try {
			conexao = con.getConnection();
			controlaUsers = new ControlaUsuarios(conexao);			
			if (controlaUsers.isUserCadastrado(tfNomeUser.getText()) == true){
				getUserSenha = controlaUsers.getUserSenha(tfNomeUser.getText());
				if (pfSenhaUser.getText().equals(getUserSenha.get(1))){
					
					
					telaInicial.TelaIniciall(tfNomeUser.getText());		
					PanelPrincipal.add(telaInicial.panelPrincipal);
					
					
					
					//Msg Boas Vindas
					pegaHora = new getHour();
					criar_novaConta.setVisible(false);
					separador.setVisible(false);
					PanelVisible(2);
					pegaHora.msgBoasVindas(getUserSenha.get(2).toString());
				} else {
					newJOPWarningMessage("Usuário ou Senha Inválido!", "Logon");
				}
			} else {
				newJOPWarningMessage("Usuário ou Senha Inválido!", "Logon");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conexao.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}


	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == criar_novaConta){
			new novoUsuario();
		}
		if (evento.getSource() == Entrar){
			logoff.setEnabled(true);
			Entrar();
		}
		if (evento.getSource() == esqueceu){
			new EsqueceuSenhaOuLogin();
		}
		if (evento.getSource() == logoff){
			if (newJOPConfirmDialogYes_No("Deseja realmente sair de "+tfNomeUser.getText()+"?", "Logoff")==JOptionPane.YES_OPTION){
				newJOPInfMsg("Até Logo "+getUserSenha.get(2)+".", "Bye bye!");
				tfNomeUser.setText("");
				pfSenhaUser.setText("");
				PanelVisible(1);
				logoff.setEnabled(false);	
				criar_novaConta.setVisible(true);
			}					
		}
		if (evento.getSource() == sair){
			if (newJOPConfirmDialogYes_No("Deseja Realmente Sair do WordStop?", "Sair?") == JOptionPane.YES_OPTION){
				System.exit(0);
			}			
		}

	}
	
	//Gerencia Panels
	public void PanelVisible(int panel){
		/* 1 - panelLogin
		 * 2 - panelTelaInicio*/
		if (panel == 1){
			telaInicial.panelPrincipal.setVisible(false);
			panelTelaLogin.setVisible(true);
		}
		if (panel == 2){
			panelTelaLogin.setVisible(false);
			telaInicial.panelPrincipal.setVisible(true);			
		}
	}
	public JPanel getPanelPrincipal() {
		PanelPrincipal.setVisible(false);
		return PanelPrincipal;
	}
	public void fechaFrame(){
		if (newJOPConfirmDialogYes_No("Deseja Realmente Sair do WordStop?", "Sair") == JOptionPane.YES_OPTION){
			System.exit(JFrame.EXIT_ON_CLOSE);
		} else {
			frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		}
	}
}
