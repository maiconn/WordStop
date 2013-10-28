package View.Jogo;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import SocketConnection.Jogador;
import SocketConnection.Server;
import View.Objetos;

public class IniciarJogo extends Objetos{
	private static final long serialVersionUID = 1L;
	public static JPanel panelIniciarJogo;
	private static JCheckBox cbPreparado;
	private JLabel preparado;
	JButton IniciarJogo;
	private int Serv1Jogador2;
	private String Nick;

	public void InitIniciarJogo(int Serv1Jogador2, String Nick){
		this.Serv1Jogador2 = Serv1Jogador2;
		this.Nick = Nick;
		panelIniciarJogo = new JPanel();
		panelIniciarJogo.setLayout(null);
		panelIniciarJogo.setBorder(BorderFactory.createTitledBorder("Iniciar Jogo"));
		panelIniciarJogo.setBounds(420,240,369,65);

		preparado = newJLabelPadrao("Preparado!", 30, 25, 120, 25);
		panelIniciarJogo.add(preparado);

		setCbPreparado(new JCheckBox());
		getCbPreparado().setBounds(135, 25, 50, 25);
		getCbPreparado().addActionListener(this);
		panelIniciarJogo.add(getCbPreparado());

		IniciarJogo = newJButton("Iniciar Jogo", 180, 18, 180, 40);
		IniciarJogo.setForeground(Color.RED);
		panelIniciarJogo.add(IniciarJogo);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == getCbPreparado()){
			if (Serv1Jogador2 == 1){
				if (getCbPreparado().isSelected()){
					Server.mandaPtodosJogadores("/preparado");
					Server.mandaPtodosJogadores(Nick);
					ListaJogadores.JogPreparado[0].setVisible(true);
					Server.preparados.add(Nick);
				} else {
					Server.mandaPtodosJogadores("/despreparado");
					Server.mandaPtodosJogadores(Nick);
					ListaJogadores.JogPreparado[0].setVisible(false);
					Server.preparados.remove(Nick);
				}
			}
			if (Serv1Jogador2 == 2){
				if (getCbPreparado().isSelected()){
					Jogador.getSaida().println("/preparado");
					Jogador.getSaida().println(Nick);
				} else {
					Jogador.getSaida().println("/despreparado");
					Jogador.getSaida().println(Nick);
				}
			}
		}
		if (evento.getSource() == IniciarJogo){
			if (Server.listaJogadores.size()>1){
				if (Server.tiposSelecionados.size()>2){
					if (Server.letrasSelecionadas.size()>0){
						if (Server.preparados.size()==Server.listaJogadores.size()){
							InitRodada();
						} else {
							newJOPInfMsg("Todos precisam estar preparados para iniciar o jogo!", "Não Preparados");
						}
					} else {
						newJOPInfMsg("Precisa de no mínimo uma letra selecinada para iniciar o jogo!", "Falta Selecionar Letra");
					}
				} else {
					newJOPInfMsg("Precisa de no mínimo três tipos para iniciar o jogo!", "Faltam Tipos");
				}
			} else {
				newJOPInfMsg("Precisa de no mínimo dois jogadores para iniciar o jogo!", "Faltam Jogadores");
			}
		}

	}

	@SuppressWarnings("static-access")
	public void setCbPreparado(JCheckBox cbPreparado) {
		this.cbPreparado = cbPreparado;
	}

	public static JCheckBox getCbPreparado() {
		return cbPreparado;
	}

	public static void SelecionaLetra4Rodada(){
		int LetraSelecionada = 0 + (int)(Math.random()*Server.letrasSelecionadas.size());
		String letraSelecionada = Server.letrasSelecionadas.get(LetraSelecionada).toString();
		Server.mandaPtodosJogadores("/LetraRodada");
		Server.mandaPtodosJogadores(letraSelecionada);
		
		int qtdRodadas = 1;
		if (Server.quant_rodadas != null){
			qtdRodadas = Integer.parseInt(Server.quant_rodadas);
		}
		if (qtdRodadas<(Server.letrasSelecionadas.size())){
			Server.letrasSelecionadas.remove(LetraSelecionada);	
		}
		Jogar.letraSelecinadaParaRodada.setText(letraSelecionada);
	}
	
	public static void JogoVisivel(){
		ConfigJogo.panelConfigJogo.setVisible(false);
		panelIniciarJogo.setVisible(false);
		SelecionarLetras.panelSelecionarLetras.setVisible(false);
		Resultado.panelResultado.setVisible(false);
		Conferencia.panelConferencia.setVisible(false);
		Jogar.panelJogoIniciado.setVisible(true);
	}
	
	public static void TodosDespreparados(){
		for (int i = 0 ; i<Server.listaJogadores.size() ;i++){
			ListaJogadores.JogPreparado[i].setVisible(false);
			Server.preparados.remove(0);
		}
		Server.mandaPtodosJogadores("/todosDespreparados");
	}
	
	public static void IniciaLabelsAndTfTIPOS(){
		for (int i = 0 ; i<Server.tiposSelecionados.size() ; i++){
			Jogar.Label_Tipos[i].setVisible(true);
			((JLabel) Jogar.Label_Tipos[i]).setText(Server.tiposSelecionados.get(i));
			Jogar.tf_Tipos[i].setVisible(true);
		}
	}
	
	public static void InitRodada(){
		//SETAR PANELS COMO FALSE E DEIXAR SOMENTE O DO JOGO VISIVEL
		JogoVisivel();

		//Mandar para os Jogadores o início do jogo
		Server.mandaPtodosJogadores("/IniciarJogo");

		//Manda Msg De Início de Jogo
		Server.mandaPtodosJogadores("/msg");
		Server.mandaPtodosJogadores("[JOGO INICIADO! Boa Sorte...]\n");
		Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"[JOGO INICIADO! Boa Sorte...]\n");

		//Manda Letra Selecionada Para Os Jogadores e Remove A Letra Da ArrayList caso necessário
		SelecionaLetra4Rodada();

		//Manda Quantidade De Rodadas
		Server.mandaPtodosJogadores("/QtdRodadas");
		if (Server.quant_rodadas != null){
			Server.mandaPtodosJogadores(Server.quant_rodadas);
			Jogar.qnd_rodadas.setText(Server.quant_rodadas);
		} else {
			Server.mandaPtodosJogadores("1");
			Jogar.qnd_rodadas.setText("1");
			Jogar.rodada.setText("1");
		}
		
		//Manda a Rodada Atual
		Server.mandaPtodosJogadores("/ProntoPconferencia.Rodada");
		Server.mandaPtodosJogadores(Jogar.rodada.getText());

		//TODOS DESPREPARADOS PARA COMEÇAR A CONFERÊNCIA APÓS O JOGO
		TodosDespreparados();

		//Seta os tipos nos labels e nos tf...
		IniciaLabelsAndTfTIPOS();
		
		//Dá um iterator na lista de Tipos;
		Server.proxTipo = Server.tiposSelecionados.iterator();
	}
	
	public static void ProxRodada(){
		//SETAR PANELS COMO FALSE E DEIXAR SOMENTE O DO JOGO VISIVEL
		JogoVisivel();

		//Mandar para os Jogadores o início do jogo
		Server.mandaPtodosJogadores("/IniciarJogo");

		//Manda Msg De Início de Jogo
		Server.mandaPtodosJogadores("/msg");
		Server.mandaPtodosJogadores("[JOGO INICIADO! Boa Sorte...]\n");
		Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"[JOGO INICIADO! Boa Sorte...]\n");

		//Manda Letra Selecionada Para Os Jogadores e Remove A Letra Da ArrayList caso necessário
		SelecionaLetra4Rodada();

		//Manda Quantidade De Rodadas
		Server.mandaPtodosJogadores("/QtdRodadas");
		if (Server.quant_rodadas != null){
			Server.mandaPtodosJogadores(Server.quant_rodadas);
			Jogar.qnd_rodadas.setText(Server.quant_rodadas);
		} else {
			Server.mandaPtodosJogadores("1");
			Jogar.qnd_rodadas.setText("1");
			Jogar.rodada.setText("1");
		}
		
		//Manda a Rodada Atual
		Server.mandaPtodosJogadores("/ProntoPconferencia.Rodada");
		Server.mandaPtodosJogadores(Jogar.rodada.getText());

		//Seta os tipos nos labels e nos tf...
		IniciaLabelsAndTfTIPOS();
		
		//Dá um iterator na lista de Tipos;
		Server.proxTipo = Server.tiposSelecionados.iterator();
	}

	
	


}
