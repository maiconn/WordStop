package View.Jogo;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import View.Objetos;

public class TelaJogo extends Objetos{
	private static final long serialVersionUID = 1L;
	public JPanel panelTelaJogo;
	private ImageIcon imageFundo;
	private JLabel imageFundoLabel;
	public static boolean TudoOk = false, TudoOkJogador = false;
	private Chat chat = new Chat();
	private ListaJogadores listaJogadores = new ListaJogadores();
	private SelecionarLetras selecionarLetras = new SelecionarLetras();
	private ConfigJogo configJogo = new ConfigJogo();
	private IniciarJogo iniciarJogo = new IniciarJogo();
	private Jogar jogar = new Jogar();
	private Conferencia conferencia = new Conferencia();
	private Resultado resultado = new Resultado();
	private int Servidor1User2;
	private String Nick;



	public void TelaJogoInit(int Servidor1User2, String Nick){
		this.Nick = Nick;
		this.Servidor1User2 = Servidor1User2;
		if (Servidor1User2 == 1){
			InitServer();
		} else {
			InitJogador();
		}	
	}

	public void InitServer(){
		//SET PANEL TELA JOGO
		panelTelaJogo = new JPanel();
		panelTelaJogo.setLayout(null);
		panelTelaJogo.setBounds(0,0,800,578);
		panelTelaJogo.setOpaque(true);
		panelTelaJogo.setBackground(Color.white);

		//SET IMAGEM FUNDO
		imageFundo = new ImageIcon("src/images/WordStop_Jogo.jpg");
		imageFundoLabel = new JLabel(imageFundo);
		imageFundoLabel.setBounds(0,0,panelTelaJogo.getWidth(),panelTelaJogo.getHeight());

		//InitComponents
		//PANEL JOGAR
		if (Servidor1User2==1){
			jogar.InitJogo(1, Nick);
			Jogar.panelJogoIniciado.setVisible(false);
			panelTelaJogo.add(Jogar.panelJogoIniciado);
		} else {
			jogar.InitJogo(2, Nick);
			Jogar.panelJogoIniciado.setVisible(false);
			panelTelaJogo.add(Jogar.panelJogoIniciado);
		}
		
		//PANEL CONFERÊNCIA
		if (Servidor1User2==1){
			conferencia.InitConferencia(1);
			Conferencia.panelConferencia.setVisible(false);
			panelTelaJogo.add(Conferencia.panelConferencia);
		} else {
			conferencia.InitConferencia(2);
			Conferencia.panelConferencia.setVisible(false);
			panelTelaJogo.add(Conferencia.panelConferencia);
		}
		
		//PANEL RESULTADO
		if (Servidor1User2==1){
			resultado.InitResultado(1);
			Resultado.panelResultado.setVisible(false);
			panelTelaJogo.add(Resultado.panelResultado);
		} else {
			resultado.InitResultado(2);
			Resultado.panelResultado.setVisible(false);
			panelTelaJogo.add(Resultado.panelResultado);
		}
		
		

		//PANEL CHAT
		if (Servidor1User2==1){
			chat.InitChat(1, Nick);
			panelTelaJogo.add(chat.panelChat);
		} else {
			chat.InitChat(2, Nick);
			panelTelaJogo.add(chat.panelChat);
		}

		//PANEL JOGADORES ONLINE
		listaJogadores.InitListaJogadores();
		panelTelaJogo.add(listaJogadores.panelJogadoresOnline);

		//PANEL SELECIONAR LETRAS
		selecionarLetras.InitSelecionarLetras();
		SelecionarLetras.panelSelecionarLetras.setVisible(true);
		panelTelaJogo.add(SelecionarLetras.panelSelecionarLetras);

		//PANEL CONFIG JOGO
		if (Servidor1User2==1){
			configJogo.InitConfigJogo(1);
			ConfigJogo.panelConfigJogo.setVisible(true);
			panelTelaJogo.add(ConfigJogo.panelConfigJogo);
		} else {
			configJogo.InitConfigJogo(2);
			panelTelaJogo.add(ConfigJogo.panelConfigJogo);
		}


		//PANEL INICIAR JOGO
		if (Servidor1User2 == 1){
			iniciarJogo.InitIniciarJogo(1, Nick);
			IniciarJogo.panelIniciarJogo.setVisible(true);
			panelTelaJogo.add(IniciarJogo.panelIniciarJogo);
		} else {
			iniciarJogo.InitIniciarJogo(2, Nick);
			panelTelaJogo.add(IniciarJogo.panelIniciarJogo);
		}

		//ADD IMAGEM FUNDO NO JPANEL
		panelTelaJogo.add(imageFundoLabel);

		TudoOk = true;
	}

	public void InitJogador(){
		InitServer();

		//DESABILITAR PANEL SELECIONAR LETRAS
		Component[] SelecionarLetras = View.Jogo.SelecionarLetras.panelSelecionarLetras.getComponents();
		for (int i =0 ; i<SelecionarLetras.length ; i++){
			SelecionarLetras[i].setEnabled(false);
		}

		//DESABILITAR PANEL CONFIGURAÇÕES
		Component[] Configuracoes = ConfigJogo.panelConfigJogo.getComponents();
		for (int i =0 ; i<Configuracoes.length ; i++){
			Configuracoes[i].setEnabled(false);
		}
		configJogo.listaTipos.setEnabled(false);

		//BOTAO INICIAR JOGO INVISIVEL
		iniciarJogo.IniciarJogo.setVisible(false);
		
		//BOTAO DO RESULTADO INVISIVEL
		Resultado.ProximaRodada.setVisible(false);

		TudoOkJogador=true;
	}

	@Override
	public void actionPerformed(ActionEvent evento) {

	}

}
