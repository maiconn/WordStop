package View.Jogo;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import SocketConnection.Jogador;
import SocketConnection.Server;
import View.Objetos;

public class Resultado extends Objetos{
	private static final long serialVersionUID = 1L;
	public static JPanel panelResultado;
	public static JLabel Jogadors[] = new JLabel[8];
	public static JLabel PontosRodadaJogador[] = new JLabel[8];
	public static JLabel PontosTotalJogador[] = new JLabel[8];
	private JLabel info_ResultadoRodada, info_ResultadoTotal, info_Jogadores, info_preparado;
	public static  JLabel Underlines[] = new JLabel[8];
	public static JCheckBox preparado;
	private int Servidor1User2;
	public static JButton ProximaRodada;

	public void InitResultado(int Servidor1User2){
		this.Servidor1User2 = Servidor1User2;
		panelResultado = new JPanel();
		panelResultado.setLayout(null);
		panelResultado.setBorder(BorderFactory.createTitledBorder("Pontuação"));
		panelResultado.setBounds(10,10,775,290);

		info_Jogadores = newJLabelTitulo("Jogador", 60, 20, 250, 25);
		panelResultado.add(info_Jogadores);

		info_ResultadoRodada = newJLabelTitulo("Resultado da Rodada", 230, 20, 300, 25);
		panelResultado.add(info_ResultadoRodada);

		info_ResultadoTotal  = newJLabelTitulo("Resultado Total", 550, 20, 250, 25);
		panelResultado.add(info_ResultadoTotal);


		int posicaoJogador = 50;
		for (int i = 0 ; i<8 ; i++){
			Jogadors[i] = newJLabelPadrao("Jogador"+i, 60, posicaoJogador, 250, 25);
			Jogadors[i].setForeground(Color.black);
			posicaoJogador = posicaoJogador+23;
			panelResultado.add(Jogadors[i]);
		}

		int posicaoPontosRodada = 50; 
		for (int i = 0 ; i<8 ; i++){
			PontosRodadaJogador[i] = newJLabelPadrao("PontosRodada"+i, 310, posicaoPontosRodada, 300, 25);
			posicaoPontosRodada = posicaoPontosRodada+23;
			PontosRodadaJogador[i].setForeground(Color.blue);
			panelResultado.add(PontosRodadaJogador[i]);
		}

		int posicaoPontosTotal = 50; 
		for (int i = 0 ; i<8 ; i++){
			PontosTotalJogador[i] = newJLabelPadrao("PontosTotal"+i, 620, posicaoPontosTotal, 250, 25);
			posicaoPontosTotal = posicaoPontosTotal+23;
			PontosTotalJogador[i].setForeground(Color.blue);
			panelResultado.add(PontosTotalJogador[i]);
		}

		int posicaoUnderlines = 52; 
		for (int i = 0 ; i<8 ; i++){
			Underlines[i] = newJLabelPadrao("______________________________________" +
					"___________________________" +
					"___________________________________"+i, 60, posicaoUnderlines, 1000, 25);
			Underlines[i].setFont(new Font("Arial", Font.BOLD, 15));
			posicaoUnderlines = posicaoUnderlines+23;
			panelResultado.add(Underlines[i]);
		}

		info_preparado = newJLabelPadrao("Preparado Para Proxima Rodada", 400, 260, 400, 25);
		panelResultado.add(info_preparado);

		preparado = new JCheckBox();
		preparado.addActionListener(this);
		preparado.setBounds(700, 260, 30, 25);
		panelResultado.add(preparado);

		for (int i = 0; i<8 ; i++){
			Jogadors[i].setVisible(false);
			PontosRodadaJogador[i].setVisible(false);
			PontosTotalJogador[i].setVisible(false);
			Underlines[i].setVisible(false);
		}

		ProximaRodada = newJButton("Proxima Rodada", 180, 260, 220, 25);
		panelResultado.add(ProximaRodada);
	}

	public static void MostraPanelResultado(){
		Conferencia.panelConferencia.setVisible(false);
		ConfigJogo.panelConfigJogo.setVisible(false);
		IniciarJogo.panelIniciarJogo.setVisible(false);
		SelecionarLetras.panelSelecionarLetras.setVisible(false);
		panelResultado.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == preparado){
			if (Servidor1User2==1){
				if (preparado.isSelected()){
					Server.mandaPtodosJogadores("/preparado");
					Server.mandaPtodosJogadores(Server.Nick);
					ListaJogadores.JogPreparado[0].setVisible(true);
					Server.preparados.add(Server.Nick);
				} else {
					Server.mandaPtodosJogadores("/despreparado");
					Server.mandaPtodosJogadores(Server.Nick);
					ListaJogadores.JogPreparado[0].setVisible(false);
					Server.preparados.remove(Server.Nick);
				}
			} 
			if (Servidor1User2 == 2){
				if (preparado.isSelected()){
					Jogador.getSaida().println("/preparado");
					Jogador.getSaida().println(Jogador.Nick);
				} else {
					Jogador.getSaida().println("/despreparado");
					Jogador.getSaida().println(Jogador.Nick);
				}
			}
		}
		if (evento.getSource() == ProximaRodada){
			int rodadaAtual = Integer.parseInt(Jogar.rodada.getText());
			int Qtd_rodadas = Integer.parseInt(Jogar.qnd_rodadas.getText());
			if (Server.preparados.size() == Server.listaJogadores.size()){
				if (rodadaAtual < Qtd_rodadas){
					rodadaAtual = rodadaAtual+1;
					Jogar.rodada.setText(Integer.toString(rodadaAtual));
					Conferencia.rodada.setText(Integer.toString(rodadaAtual));
					Server.mandaPtodosJogadores("/RodadaAtual");
					Server.mandaPtodosJogadores(Integer.toString(rodadaAtual));
					Server.Clear();
					Server.mandaPtodosJogadores("/Clear");					
					IniciarJogo.ProxRodada();
				} else {
					newJOPInfMsg("Rodadas Encerradas!", "Rodadas");
					Server.Clear();
					Server.mandaPtodosJogadores("/Clear");
					Server.mandaPtodosJogadores("/PanelSTART");
					PanelsSTARTgame();
				}
			} else {
				newJOPInfMsg("Todos devem estar preparados para a proxima rodada!", "Mensagem");				
			}
		}
	}
	
	public static void PanelsSTARTgame(){
		panelResultado.setVisible(false);
		Conferencia.panelConferencia.setVisible(false);
		Jogar.panelJogoIniciado.setVisible(false);
		IniciarJogo.panelIniciarJogo.setVisible(true);
		ConfigJogo.panelConfigJogo.setVisible(true);
		SelecionarLetras.panelSelecionarLetras.setVisible(true);
	}


}
