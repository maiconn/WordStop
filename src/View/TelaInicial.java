package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import SocketConnection.Jogador;
import SocketConnection.Server;
import View.Jogo.TelaJogo;

public class TelaInicial extends Objetos{
	private static final long serialVersionUID = 1L;
	public JPanel panelPrincipal, panelInicio;
	private JLabel infoUser, usuario, getfotoTelaInicio, Selecionar, portaCriar, nickCriar,
	nickEntrar, portaEntrar, ipEntrar;
	private ImageIcon fotoTelaInicioJogo;
	private JTextField tfPortaCriar, tfNickCriar, tfNickEntrar, tfPortaEntrar, tfIpEntrar;
	private JList EntrarOuCriar;
	private JScrollPane barra;
	private JPanel panelCriar, panelEntrar;
	private JButton Criar, Entrar;
	private TelaJogo telaJogo;
	private Server server = new Server();
	private Jogador jogador = new Jogador();

	public void TelaIniciall(String user){
		// SET PANELPRINCIPAL
		panelPrincipal = new JPanel();
		panelPrincipal.setLayout(null);
		panelPrincipal.setOpaque(true);
		panelPrincipal.setBounds(0,0,800,578);

		// SET PANELINICIO
		panelInicio = new JPanel();
		panelInicio.setLayout(null);
		panelInicio.setOpaque(true);
		panelInicio.setBackground(Color.white);
		panelInicio.setBounds(0,0,800,578);
		//SET BACKGROUND
		fotoTelaInicioJogo = new ImageIcon("src/images/TelaInicioJogo.jpg");
		getfotoTelaInicio = new JLabel(fotoTelaInicioJogo);
		getfotoTelaInicio.setBounds(0,0,panelInicio.getWidth(), panelInicio.getHeight());

		//InitComponents		
		//Componentes do jogo
		infoUser = new JLabel("logado com");
		infoUser.setFont(new Font("Arial", Font.ITALIC, 11));
		infoUser.setForeground(Color.white);
		infoUser.setBounds(550,11,200,25);
		panelInicio.add(infoUser);


		usuario = newJLabelPadrao(user, 620,10,200,25);
		usuario.setForeground(Color.red);
		panelInicio.add(usuario);

		Selecionar = newJLabelTitulo("Selecione Opção", 300,230,200,42);
		Selecionar.setForeground(Color.RED);
		panelInicio.add(Selecionar);


		//Criar ou entrar em um jogo
		String opcoes[] = {"Criar Novo Jogo", "Entrar Em Um Jogo"};
		EntrarOuCriar = new JList(opcoes);
		EntrarOuCriar.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent clique) {
				if (clique.getClickCount() == 2){
					if (EntrarOuCriar.getSelectedIndex() == 0){
						panelCriar.setVisible(true);
					} else {
						panelCriar.setVisible(false);
					}

					if (EntrarOuCriar.getSelectedIndex() == 1){
						panelEntrar.setVisible(true);
					} else {
						panelEntrar.setVisible(false);
					}
				}				
			}
		});
		barra = new JScrollPane(EntrarOuCriar);
		EntrarOuCriar.setSelectionMode(0);
		barra.setBounds(340,275,120,42);
		panelInicio.add(barra);	

		//Cria Novo Jogo
		panelCriar = new JPanel();
		panelCriar.setLayout(null);
		panelCriar.setBounds(0,190,235,210);
		panelCriar.setBorder(BorderFactory.createTitledBorder("Criar"));
		panelCriar.setBackground(Color.white);
		panelCriar.setVisible(false);
		panelInicio.add(panelCriar);

		nickCriar = newJLabelPadrao("NickName", 15,50,100,25);
		panelCriar.add(nickCriar);

		tfNickCriar = newJTextFieldPadrao(100,48,120,28);
		tfNickCriar.setText(user);
		panelCriar.add(tfNickCriar);

		portaCriar = newJLabelPadrao("Porta", 45,90,100,25);
		panelCriar.add(portaCriar);

		tfPortaCriar = newJTextFieldPadrao(100,88,90,28);
		tfPortaCriar.setText("123");
		panelCriar.add(tfPortaCriar);

		Criar = newJButton("Criar Novo Jogo", 30,160,170,30);
		panelCriar.add(Criar);		

		//Entrar Em Um Jogo
		panelEntrar = new JPanel();
		panelEntrar.setLayout(null);
		panelEntrar.setBounds(565,190,230,210);
		panelEntrar.setBorder(BorderFactory.createTitledBorder("Entrar"));
		panelEntrar.setBackground(Color.white);
		panelEntrar.setVisible(false);
		panelInicio.add(panelEntrar);

		nickEntrar = newJLabelPadrao("NickName", 5,30,100,25);
		panelEntrar.add(nickEntrar);

		tfNickEntrar = newJTextFieldPadrao(90,28,120,28);
		tfNickEntrar.setText(user);
		panelEntrar.add(tfNickEntrar);

		ipEntrar = newJLabelPadrao("Ip", 63,70,100,25);
		panelEntrar.add(ipEntrar);

		tfIpEntrar = newJTextFieldPadrao(90,68,135,28);
		tfIpEntrar.setText("127.0.0.1");
		panelEntrar.add(tfIpEntrar);

		portaEntrar = newJLabelPadrao("Porta", 35,110,100,25);
		panelEntrar.add(portaEntrar);

		tfPortaEntrar = newJTextFieldPadrao(90,108,90,28);
		tfPortaEntrar.setText("123");
		panelEntrar.add(tfPortaEntrar);

		Entrar = newJButton("Entrar No Jogo", 30,160,170,30);
		panelEntrar.add(Entrar);

		panelInicio.add(getfotoTelaInicio);
		panelPrincipal.add(panelInicio);		
	}
	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == Criar){
			server.ConnectServer(tfPortaCriar.getText(), tfNickCriar.getText());
			if (server.isConectou() == true){
				telaJogo = new TelaJogo();
				telaJogo.TelaJogoInit(1, tfNickCriar.getText());
				panelInicio.setVisible(false);
				panelPrincipal.add(telaJogo.panelTelaJogo);
			}


		}

		if (evento.getSource() == Entrar){
			jogador.InitConexaoJogador(tfIpEntrar.getText(), Integer.parseInt(tfPortaEntrar.getText()), tfNickEntrar.getText());
			while (!Jogador.EntraOrNo){
				System.out.println("Esperando Verificar Nick");
			}
			if (jogador.isConnected() == true){
				telaJogo = new TelaJogo();
				telaJogo.TelaJogoInit(2, tfNickEntrar.getText());
				panelInicio.setVisible(false);
				panelPrincipal.add(telaJogo.panelTelaJogo);
			}
		}

	}

}
