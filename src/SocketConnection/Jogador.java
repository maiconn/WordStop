package SocketConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import View.Jogo.Chat;
import View.Jogo.Conferencia;
import View.Jogo.ConfigJogo;
import View.Jogo.IniciarJogo;
import View.Jogo.Jogar;
import View.Jogo.ListaJogadores;
import View.Jogo.Resultado;
import View.Jogo.SelecionarLetras;
import View.Jogo.TelaJogo;

public class Jogador {
	Socket conexaoJogador;
	BufferedReader recebeMsg;
	InputStreamReader escreveMsg;
	private static PrintStream saida;
	private boolean Connected;
	public static String Nick;
	public static boolean EntraOrNo = false;
	//Lista de todos os nomes dos jogadores que estão atualmente jogando
	public static ArrayList<String> listaNicks = new ArrayList<String>();
	// Letras que estão selecionadas;
	public static ArrayList<String> letrasSelecionadas = new ArrayList<String>();
	// Os tipos que estão selecionados
	public static ArrayList<String> tiposSelecionados = new ArrayList<String>();

	// TIPOS DO JOGADOR, OU SEJA, O QUE O JOGADOR DIGITOU NO JOGO
	public static ArrayList<String> TiposJogador = new ArrayList<String>();

	// TIPOS QUE O JOGADOR IRÁ CONFERIR PARA VER SE REALMENTE SÃO RESPOSTAS VERDADEIRAS
	public static ArrayList<String> TiposConferencia = new ArrayList<String>();

	//A ARRAYLIST BOOLEAN QUE DETERMINA SE AS RESPOSTAS SAO CERTAS OU NÃO, QUE SERÁ ENVIADA PARA O SERVIDOR
	public static ArrayList<Boolean> Conferido = new ArrayList<Boolean>();

	//GetSet
	//Connected
	public void setConnected(boolean connected) {
		this.Connected = connected;
	}
	public boolean isConnected() {
		return Connected;
	}
	//saida
	public void setSaida(PrintStream saida) {
		Jogador.saida = saida;
	}
	public static PrintStream getSaida() {
		return saida;
	}

	//Inicia a Conexão
	public void InitConexaoJogador(String ip, int porta, String Nick){
		//Este nick = aquele nick...
		Jogador.Nick = Nick;
		try {
			//Inicia o Socket
			conexaoJogador = new Socket(ip,porta);

			//Cria Objetos de Entrada E Saída de Dados
			setSaida(new PrintStream(conexaoJogador.getOutputStream()));

			//Manda o Nick
			saida.println("/nick");
			saida.println(Nick);

			//Inicia Thread para receber Msgs
			Thread msgRecebidas = new Thread(new TrataMSGRecebida());
			msgRecebidas.start();


		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "Não foi possível conectar com o ip "+ip+". \nErro: "+e, "Alerta", JOptionPane.ERROR_MESSAGE);
			EntraOrNo=true;
			setConnected(false);
		} catch (IOException a) {
			JOptionPane.showMessageDialog(null, "Não foi possível conectar com a porta "+porta+". \nErro: "+a, "Alerta", JOptionPane.ERROR_MESSAGE);
			EntraOrNo=true;
			// Connected False caso o Socket Não Conectar
			setConnected(false);
		}
	}




	//Thread para receber msgs
	public class TrataMSGRecebida implements Runnable{
		public void run() {
			try {	

				//Criando objetos de comunicação
				escreveMsg = new InputStreamReader(conexaoJogador.getInputStream());
				recebeMsg = new BufferedReader(escreveMsg);

				//Recebe Comandos!
				String Command = recebeMsg.readLine();
				while (Command != null){
					RecebeComando(Command);
					Command = recebeMsg.readLine();
				}				

			} catch (IOException e) {

			}

		}
		public void RecebeComando(String Command) throws IOException{
			String Acao;
			if (Command.equals("/msg")){
				Acao = recebeMsg.readLine();
				Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+Acao+"\n");
				Chat.getMsgs_chat().setCaretPosition(Chat.getMsgs_chat().getDocument().getLength());
			}
			if (Command.equals("/nick")){
				Acao = recebeMsg.readLine();
				ListaJogadores.getModeloListaJogadores().addElement(Acao);	
				listaNicks.add(Acao);
			}
			if (Command.equals("/NOnick")){
				//Nick Não Aceito
				//Fexa a Conexao e Espera um Novo Nick
				conexaoJogador.close();
				setConnected(false);
				EntraOrNo = true;	
				JOptionPane.showMessageDialog(null, "Entre Com Outro NickName!");
				EntraOrNo = false;
			}
			if (Command.equals("/YESnick")){
				//Nick Aceito
				setConnected(true);
				EntraOrNo = true;	
				//Espera a Interface Entrar
				while (!TelaJogo.TudoOkJogador){

				}
			}
			if (Command.equals("/removeUser")){
				for(int i = 0 ; i<listaNicks.size() ; i++){
					ListaJogadores.JogPreparado[i].setVisible(false);
				}
				IniciarJogo.getCbPreparado().setSelected(false);
				Acao = recebeMsg.readLine();
				ListaJogadores.getModeloListaJogadores().removeElement(Acao);
				listaNicks.remove(Acao);
			}
			if (Command.equals("/preparado")){
				Acao = recebeMsg.readLine();
				for (int i = 0 ; i<listaNicks.size() ;i++){
					if (listaNicks.get(i).equals(Acao)){
						ListaJogadores.JogPreparado[i].setVisible(true);
					}
				}
			}
			if (Command.equals("/despreparado")){
				Acao = recebeMsg.readLine();
				for (int i = 0 ; i<listaNicks.size() ;i++){
					if (listaNicks.get(i).equals(Acao)){
						ListaJogadores.JogPreparado[i].setVisible(false);
					}
				}
			}
			if (Command.equals("/rodada")){
				Acao = recebeMsg.readLine();
				ConfigJogo.rodadas.setSelectedIndex(Integer.parseInt(Acao));
			}
			if (Command.equals("/letraSelecionada")){
				Acao = recebeMsg.readLine();
				VerificaBotaoSelecionado(Acao);
			}
			if (Command.equals("/tipoADD")){
				Acao = recebeMsg.readLine();
				ConfigJogo.modeloListaTipos.addElement(Acao);
				tiposSelecionados.add(Acao);
			}
			if (Command.equals("/tipoREMOVE")){
				Acao = recebeMsg.readLine();
				ConfigJogo.modeloListaTipos.removeElement(Acao);
				tiposSelecionados.remove(Acao);
			}
			if (Command.equals("/IniciarJogo")){
				//Somente chat e panel do jogo Setados Visiveis;
				ConfigJogo.panelConfigJogo.setVisible(false);
				IniciarJogo.panelIniciarJogo.setVisible(false);
				SelecionarLetras.panelSelecionarLetras.setVisible(false);
				Resultado.panelResultado.setVisible(false);
				Conferencia.panelConferencia.setVisible(false);
				Jogar.panelJogoIniciado.setVisible(true);

				//Seta os tipos nos labels e nos tf...
				for (int i = 0 ; i<Jogador.tiposSelecionados.size() ; i++){
					Jogar.Label_Tipos[i].setVisible(true);
					((JLabel) Jogar.Label_Tipos[i]).setText(Jogador.tiposSelecionados.get(i));
					Jogar.tf_Tipos[i].setVisible(true);
				}
			}
			if (Command.equals("/LetraRodada")){
				Acao = recebeMsg.readLine();
				Jogar.letraSelecinadaParaRodada.setText(Acao);
			}
			
			if (Command.equals("/RodadaAtual")){
				Acao = recebeMsg.readLine();
				Jogar.rodada.setText(Acao);
				Conferencia.rodada.setText(Acao);
			}
			if (Command.equals("/QtdRodadas")){
				Acao = recebeMsg.readLine();
				Jogar.qnd_rodadas.setText(Acao);
			}
			if (Command.equals("/STOP")){
				//Adicionar TF dos Tipos Digitados(Classe Jogar) na ArrayList TiposJogador
				for (int i =0 ; i<tiposSelecionados.size() ; i++){
					TiposJogador.add(((JTextComponent) Jogar.tf_Tipos[i]).getText());
				}

				//Envia a primeira posição da ArrayList TiposJogador para o servidor
				saida.println("/EnviandoConferencia");
				saida.println(Nick);
				saida.println(TiposJogador.get(0));

				//Remove a posição da ArrayList TiposJogador
				TiposJogador.remove(0);

				//Pronto para conferir
				saida.println("/ProntoPconferencia");		
			}
			if (Command.equals("/todosDespreparados")){
				for (int i = 0 ; i<listaNicks.size() ; i++){
					ListaJogadores.JogPreparado[i].setVisible(false);
				}
			}
			if (Command.equals("/RecebeConferencia")){
				//Adicionar Tipos Na ArrayList
				Acao = recebeMsg.readLine();
				TiposConferencia.add(Acao);
			}
			if (Command.equals("/ProntoPconferencia.LetraDaRodada")){
				Acao = recebeMsg.readLine();
				Conferencia.letra.setText(Acao);
			}
			if (Command.equals("/ProntoPconferencia.Rodada")){
				Acao = recebeMsg.readLine();
				Conferencia.rodada.setText(Acao);
			}
			if (Command.equals("/ProntoPconferencia.Tipo")){
				Acao = recebeMsg.readLine();
				Conferencia.tipo.setText(Acao);
			}
			if (Command.equals("/CONFERIR")){
				for (int i = 0 ; i<TiposConferencia.size() ; i++){
					((JTextComponent) Conferencia.Confere_users[i]).setText(TiposConferencia.get(i));
					if (((JTextComponent) Conferencia.Confere_users[i]).getText().equals("")){
						((AbstractButton) Conferencia.Valida[i]).setSelected(false);
					}
				}

				//Remove Os Tipos Da Array
				TiposConferencia.clear();

				//Implementa o panelConferencia
				Jogar.Conferir();	
			}
			if (Command.equals("/ProxConferencia")){
					Conferencia.EnviarConferencia.setEnabled(true);
					for (int i = 0 ; i<listaNicks.size() ; i++){
						Conferencia.Valida[i].setEnabled(true);
						((AbstractButton) Conferencia.Valida[i]).setSelected(true);
					}
					
					saida.println("/EnviandoConferencia");
					saida.println(Nick);
					saida.println(TiposJogador.get(0));

					TiposJogador.remove(0);

					saida.println("/ProntoPconferencia");
			}
			if (Command.equals("/PontosRodada")){
				Acao = recebeMsg.readLine();
				for (int i =0 ; i<listaNicks.size() ; i++){
					if (Acao.equals(listaNicks.get(i))){
						Acao = recebeMsg.readLine();
						Resultado.PontosRodadaJogador[i].setText(Acao);
					}
				}				
			}
			if (Command.equals("/PontosTotais")){
				Acao = recebeMsg.readLine();
				for (int i =0 ; i<listaNicks.size() ; i++){
					if (Acao.equals(listaNicks.get(i))){
						Acao = recebeMsg.readLine();
						Resultado.PontosTotalJogador[i].setText(Acao);
					}
				}				
			}
			if (Command.equals("/showRESULTADO")){
				for (int i = 0; i<listaNicks.size() ; i++){
					Resultado.Jogadors[i].setText(listaNicks.get(i));
					Resultado.Jogadors[i].setVisible(true);
					Resultado.PontosRodadaJogador[i].setVisible(true);
					Resultado.PontosTotalJogador[i].setVisible(true);
					Resultado.Underlines[i].setVisible(true);
				}
				Resultado.MostraPanelResultado();
			}
			if (Command.equals("/PanelSTART")){
				JOptionPane.showMessageDialog(null,"Rodadas Encerradas!");
				Resultado.PanelsSTARTgame();
			}
			if (Command.equals("/Clear")){
				for (int i = 0 ; i<listaNicks.size() ; i++){
					((JTextComponent) Conferencia.Confere_users[i]).setText("");
				}
				for (int i = 0 ; i<tiposSelecionados.size() ; i++){
					((JTextComponent) Jogar.tf_Tipos[i]).setText("");
				}
				
				//Remove Os Tipos Da Array
				TiposConferencia.clear();				
			}
			if (Command.equals("/habilitarConferencia")){
				Conferencia.EnviarConferencia.setEnabled(true);
				for (int i = 0 ; i<listaNicks.size() ; i++){
					Conferencia.Valida[i].setEnabled(true);
					((AbstractButton) Conferencia.Valida[i]).setSelected(true);
				}
			}
		}

		public void VerificaBotaoSelecionado(String Acao){
			if (Acao.equals("A")){
				if (SelecionarLetras.A.isSelected()){
					SelecionarLetras.A.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.A.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("B")){
				if (SelecionarLetras.B.isSelected()){
					SelecionarLetras.B.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.B.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("C")){
				if (SelecionarLetras.C.isSelected()){
					SelecionarLetras.C.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.C.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("D")){
				if (SelecionarLetras.D.isSelected()){
					SelecionarLetras.D.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.D.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("E")){
				if (SelecionarLetras.E.isSelected()){
					SelecionarLetras.E.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.E.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("F")){
				if (SelecionarLetras.F.isSelected()){
					SelecionarLetras.F.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.F.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("G")){
				if (SelecionarLetras.G.isSelected()){
					SelecionarLetras.G.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.G.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("H")){
				if (SelecionarLetras.H.isSelected()){
					SelecionarLetras.H.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.H.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("I")){
				if (SelecionarLetras.I.isSelected()){
					SelecionarLetras.I.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.I.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("J")){
				if (SelecionarLetras.J.isSelected()){
					SelecionarLetras.J.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.J.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("K")){
				if (SelecionarLetras.K.isSelected()){
					SelecionarLetras.K.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.K.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("L")){
				if (SelecionarLetras.L.isSelected()){
					SelecionarLetras.L.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.L.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("M")){
				if (SelecionarLetras.M.isSelected()){
					SelecionarLetras.M.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.M.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("N")){
				if (SelecionarLetras.N.isSelected()){
					SelecionarLetras.N.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.N.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("O")){
				if (SelecionarLetras.O.isSelected()){
					SelecionarLetras.O.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.O.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("P")){
				if (SelecionarLetras.P.isSelected()){
					SelecionarLetras.P.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.P.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("Q")){
				if (SelecionarLetras.Q.isSelected()){
					SelecionarLetras.Q.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.Q.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("R")){
				if (SelecionarLetras.R.isSelected()){
					SelecionarLetras.R.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.R.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("S")){
				if (SelecionarLetras.S.isSelected()){
					SelecionarLetras.S.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.S.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("T")){
				if (SelecionarLetras.T.isSelected()){
					SelecionarLetras.T.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.T.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("U")){
				if (SelecionarLetras.U.isSelected()){
					SelecionarLetras.U.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.U.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("V")){
				if (SelecionarLetras.V.isSelected()){
					SelecionarLetras.V.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.V.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("W")){
				if (SelecionarLetras.W.isSelected()){
					SelecionarLetras.W.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.W.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("X")){
				if (SelecionarLetras.X.isSelected()){
					SelecionarLetras.X.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.X.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("Y")){
				if (SelecionarLetras.Y.isSelected()){
					SelecionarLetras.Y.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.Y.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}
			if (Acao.equals("Z")){
				if (SelecionarLetras.Z.isSelected()){
					SelecionarLetras.Z.setSelected(false);
					letrasSelecionadas.remove(Acao);
				} else {
					SelecionarLetras.Z.setSelected(true);
					letrasSelecionadas.add(Acao);
				}
			}

		}
	}

}
