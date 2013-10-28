package SocketConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import View.Jogo.Chat;
import View.Jogo.Conferencia;
import View.Jogo.IniciarJogo;
import View.Jogo.Jogar;
import View.Jogo.ListaJogadores;
import View.Jogo.Resultado;
import View.Jogo.TelaJogo;

public class Server{
	private ServerSocket Server;
	private static ArrayList<PrintWriter> Write4Jogadores;
	private boolean conectou;
	public static ArrayList<String> listaJogadores = new ArrayList<String>();
	public static ArrayList<String> preparados = new ArrayList<String>();
	public static ArrayList<String> letrasSelecionadas = new ArrayList<String>();
	public static ArrayList<String> tiposSelecionados = new ArrayList<String>();
	public static ArrayList<String> TiposJogadores = new ArrayList<String>();
	public static ArrayList<String> TiposServidor = new ArrayList<String>();
	public static ArrayList<Boolean> ConfererirRespostas = new ArrayList<Boolean>();
	public static Iterator<String> proxTipo;
	public static int PontosRodadaJogador[] = new int[8];
	public static int PontosTotalJogador[] = new int[8];
	public static int RespostaValidaJogador[] = new int[8];
	public static String RespostaDosJogadores[] = new String[8];
	public static String quant_rodadas;
	public static String Nick, porta;
	public static int desistiu;
	public static int rodadaAtual;
	public static int prontosPconferencia;
	public static int Confererido;

	//GET SET SE A CONEXÃO DEU CERTO
	public boolean isConectou() {
		return conectou;
	}
	public void setConectou(boolean conectou) {
		this.conectou = conectou;
	}

	@SuppressWarnings("static-access")
	public void ConnectServer(String porta, String Nick){
		this.Nick = Nick;
		this.porta = porta;
		try {
			//Abrir o ServerSocket
			Server = new ServerSocket(Integer.parseInt(porta));		

			//Thread Aceitar a conexão e tratar o jogador
			Thread aceitar = new Thread(new Accepting());
			aceitar.start();

			//Cria ArrayList que adicinará os Clientes para formar uma comunicação entre todos
			Write4Jogadores = new ArrayList<PrintWriter>();

			//setar se a conexao deu certo
			setConectou(true);

			listaJogadores.add(Nick);
		} catch (NumberFormatException e) {

			JOptionPane.showMessageDialog(null, "Digite uma porta válida!", "Alerta", JOptionPane.INFORMATION_MESSAGE);
			//setar se a conexao nao deu errado
			setConectou(false);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Não foi possível conectar com a porta "+porta+".", "Erro de Conexao", JOptionPane.ERROR_MESSAGE);
			//setar se a conexao nao deu errado
			setConectou(false);

		}
	}

	//Trata Conexão
	public class Accepting implements Runnable{

		@Override
		public void run() {
			//Espera Iniciar A Interface
			while (!TelaJogo.TudoOk){

			}
			//Adiciona O Nick do Servidor Na Lista de nicks
			ListaJogadores.getModeloListaJogadores().addElement(Nick);

			//MSG DE BOAS VINDAS AO SERVIDOR
			Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"Conectado na porta "+porta+".\n");
			Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"Bem Vindo Ao WordStop...\n");

			try {
				//LOOP PRINCIPAL
				while(true){					
					//aceita o cliente
					Socket conexaoJogador = Server.accept();

					InputStreamReader escreveMsg = new InputStreamReader(conexaoJogador.getInputStream());
					BufferedReader entrada = new BufferedReader(escreveMsg);

					//Cria um PrintWriter que irá se conectar com o cliente Aceitado
					PrintWriter getClient = new PrintWriter(conexaoJogador.getOutputStream());



					//VERIFICA NICKNAME
					boolean Entrar = false;
					String msg = entrada.readLine();
					String NickName = null;
					if (msg.equals("/nick")){
						msg = entrada.readLine();
						if (VerificaNick(msg)){
							getClient.println("/YESnick");
							getClient.flush();
							Entrar = true;								
							ListaJogadores.getModeloListaJogadores().addElement(msg);
							//AVISA TODOS E AO SERVIDOR QUE O JOGADOR CONECTOU
							mandaPtodosJogadores("/msg");
							NickName = msg;
							mandaPtodosJogadores("["+NickName+" entrou no WordStop]");
							Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"["+NickName+" entrou no WordStop]\n");
							mandaPtodosJogadores("/nick");
							mandaPtodosJogadores(msg);
						} else {
							getClient.println("/NOnick");
							getClient.flush();
							conexaoJogador.close();
						}
					}

					if (Entrar){
						//Adiciona o PrintWriter do cliente na Arraylist cada vez que aceitar um novo cliente					
						Write4Jogadores.add(getClient);




						//Manda Os Nicks
						MandaTodosNicks(getClient);

						//Manda Jogadores Preparados
						for (int i = 0 ; i<preparados.size() ; i++){
							getClient.println("/preparado");
							getClient.println(preparados.get(i));
							getClient.flush();
						}

						//Manda Letras Selecionadas
						for (int i = 0 ; i<letrasSelecionadas.size() ; i++){
							getClient.println("/letraSelecionada");
							getClient.println(letrasSelecionadas.get(i));
							getClient.flush();
						}

						//Manda Tipos Adicionados
						for (int i = 0 ; i<tiposSelecionados.size() ; i++){
							getClient.println("/tipoADD");
							getClient.println(tiposSelecionados.get(i));
							getClient.flush();
						}

						//Manda quantidade de rodadas
						if (quant_rodadas != null){
							int rodada = Integer.parseInt(quant_rodadas)-1;
							getClient.println("/rodada");
							getClient.println(rodada);
							getClient.flush();
						}


						//MSG DE BOAS VINDAS PARA O CLIENTE
						getClient.println("/msg");
						getClient.println("Conexão Sincronizada.");
						getClient.println("/msg");
						getClient.println("Bem Vindo Ao WordStop...");
						getClient.flush();

						//Inicia a Thread para tratar esta nova conexão!
						Thread trataCliente = new Thread(new TrataJogador(conexaoJogador, NickName));
						trataCliente.start();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}	

	//Trata cada jogador que entrar
	public class TrataJogador implements Runnable{
		private Socket sock;
		private InputStreamReader escreveMsg;
		private BufferedReader entrada;
		private String NickName;

		public TrataJogador(Socket conexaoJogador, String NickName){
			//diz que o Socket que ele recebeu como parâmetro é igual a o socket de classe
			this.sock = conexaoJogador;
			this.NickName = NickName;
		}

		public void run() {
			try {

				//Criando Conversores para Receber uma Mensagem do Jogador
				escreveMsg = new InputStreamReader(sock.getInputStream());
				entrada = new BufferedReader(escreveMsg);


				String Command = entrada.readLine();
				//Loop Para Receber Msg
				while (Command != null){
					VerificaCommand(Command);
					Command = entrada.readLine();
				}

			} catch (IOException e) {	
				//JOGADOR SAIU
				//Desseleciona todos os (preparados) e desseleciona os checkbox's
				ListaJogadores.JogPreparado[0].setVisible(false);
				IniciarJogo.getCbPreparado().setSelected(false);
				for (int b = 0 ; b<listaJogadores.size() ; b++){
					ListaJogadores.JogPreparado[b].setVisible(false);
				}

				//Retira O jogador da Interface
				for (int i = 0 ; i<listaJogadores.size() ; i++){
					//se o jogador desconectar
					if (listaJogadores.get(i).equals(NickName)){
						//remove da lista de Jogadores
						listaJogadores.remove(i);
						//Notifica a saida do jogador
						mandaPtodosJogadores("/msg");
						mandaPtodosJogadores("["+NickName+" Saiu Do WordStop]");
						//Remove da Interface
						mandaPtodosJogadores("/removeUser");
						mandaPtodosJogadores(NickName);
						Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"["+NickName+" saiu do Wordstop]\n");						
						ListaJogadores.getModeloListaJogadores().removeElement(NickName);
						if (preparados.size() != 0){
							preparados.remove(0);
						}
					}
				}
			}

		}

		//Verifica com o comando para executar a ação de determinado comando recebido
		public void VerificaCommand(String Command) throws IOException{
			String Acao;
			//se o comando for igual a /msg
			if (Command.equals("/msg")){
				//Recebe a msg
				Acao = entrada.readLine();

				//Envia para a classe onde tem a interface JTextArea do servidor
				Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+Acao+"\n");
				Chat.getMsgs_chat().setCaretPosition(Chat.getMsgs_chat().getDocument().getLength());

				//Manda o comando
				mandaPtodosJogadores("/msg");	
				//Envia msg Para Todos
				mandaPtodosJogadores(Acao);	
			}
			//se o comando for igual a /nick
			if (Command.equals("/nick")){
				//Recebe o Nick
				Acao = entrada.readLine();

				//Verificar Nick
				if (VerificaNick(Acao)==true){
					mandaPtodosJogadores("/nick");
					mandaPtodosJogadores(Acao);	
					ListaJogadores.getModeloListaJogadores().addElement(Acao);
				} 

			}
			//checkbox Preparado do jogador que Selecionar
			if (Command.equals("/preparado")){
				Acao = entrada.readLine();
				mandaPtodosJogadores("/preparado");
				mandaPtodosJogadores(Acao);
				for (int i = 0 ; i<listaJogadores.size() ;i++){
					if (listaJogadores.get(i).equals(Acao)){
						ListaJogadores.JogPreparado[i].setVisible(true);
						preparados.add(Acao);
					}
				}
			}
			//checkbox Preparado do jogador que desselecionar
			if (Command.equals("/despreparado")){
				Acao = entrada.readLine();
				mandaPtodosJogadores("/despreparado");
				mandaPtodosJogadores(Acao);
				for (int i = 0 ; i<listaJogadores.size() ;i++){
					if (listaJogadores.get(i).equals(Acao)){
						ListaJogadores.JogPreparado[i].setVisible(false);
						preparados.remove(Acao);
					}
				}
			}
			//COMANDO /STOP
			if (Command.equals("/STOP")){
				//Adiciona os tipos do servidor em uma arraylist separada dos tipos do cliente
				for (int i = 0 ; i<tiposSelecionados.size() ; i++){
					TiposServidor.add(((JTextComponent) Jogar.tf_Tipos[i]).getText());
				}

				prontosPconferencia = prontosPconferencia+1;

				mandaPtodosJogadores("/STOP");				
				IniciarJogo.IniciaLabelsAndTfTIPOS();				
			}

			//COMANDO /desistir
			if (Command.equals("/desistir")){
				desistiu = desistiu+1;
				if (desistiu == listaJogadores.size()){					
					//manda msg para todos avisando a desistencia de todos
					mandaPtodosJogadores("/msg");
					mandaPtodosJogadores("[Todos desistiram da rodada!]\n");

					//MANDA UM STOP PARA TODOS
					mandaPtodosJogadores("/STOP");

					//IMPLEMENTA NA INFERFACE DO SERVER
					Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"[Todos desistiram da rodada!]\n");
					IniciarJogo.IniciaLabelsAndTfTIPOS();
					Jogar.Conferir();
				} else {
					System.out.println(desistiu);
					System.out.println(listaJogadores.size());
				}
			}
			//COMANDO /EnviandoConferencia
			if (Command.equals("/EnviandoConferencia")){
				Acao = entrada.readLine();
				for (int i = 0 ; i<listaJogadores.size() ; i++){
					if (listaJogadores.get(i).equals(Acao)){
						Acao = entrada.readLine();
						if (Acao.equals("")){
							RespostaDosJogadores[i] = "";
						} else {
							RespostaDosJogadores[i] = Acao;
						}
					}
				}			
			}

			//COMANDO /ProntoPconferencia
			if (Command.equals("/ProntoPconferencia")){
				prontosPconferencia = prontosPconferencia+1;

				//Condição que aguarda até todos estiverem prontos
				if (prontosPconferencia == listaJogadores.size()){

					//se tiver um proximo tipo
					if (proxTipo.hasNext()){
						String tipoAtual = proxTipo.next();

						mandaPtodosJogadores("/ProntoPconferencia.LetraDaRodada");
						mandaPtodosJogadores(Jogar.letraSelecinadaParaRodada.getText());

						//Manda A Rodada Atual
						mandaPtodosJogadores("/ProntoPconferencia.Rodada");
						mandaPtodosJogadores(Jogar.rodada.getText());

						//Manda O Tipo Atual
						mandaPtodosJogadores("/ProntoPconferencia.Tipo");
						mandaPtodosJogadores(tipoAtual);

						//Adiciona o Tipo do servidor.. primeira posição na array TIposJogadores
						TiposJogadores.add(TiposServidor.get(0));

						//Adiciona as Respostas Dos Jogadores
						for (int i  = 1 ; i<listaJogadores.size() ; i++){
							TiposJogadores.add(RespostaDosJogadores[i]);	
						}

						//Manda todos Os Tipos Para Todos Os Jogadores
						for (int i = 0; i<TiposJogadores.size() ; i++){
							//manda para todos os jogadores a conferencia do primeiro tipo
							mandaPtodosJogadores("/RecebeConferencia");
							mandaPtodosJogadores(TiposJogadores.get(i));
						}


						//Manda a msg De pronto para todos
						mandaPtodosJogadores("/CONFERIR");


						//Implementa na interface a Letra Selecionada
						Conferencia.letra.setText(Jogar.letraSelecinadaParaRodada.getText());

						//Implementa na interface a Rodada
						Conferencia.rodada.setText(Jogar.rodada.getText());

						//Implementa na interface O Tipo Atual
						Conferencia.tipo.setText(tipoAtual);

						//Implementa as Respostas na Interface Gráfica
						for (int i = 0 ; i<listaJogadores.size() ; i++){
							((JTextComponent) Conferencia.Confere_users[i]).setText(TiposJogadores.get(i));
							if (((JTextComponent) Conferencia.Confere_users[i]).getText().equals("")){
								((AbstractButton) Conferencia.Valida[i]).setSelected(false);
							}
						}

						//Remove o tipo no Server da Array Tipos do servidor
						TiposServidor.remove(0);

						//Inicia o panelConferencia
						Jogar.Conferir();

						//zera o contador de prontos conferencias
						prontosPconferencia = 0;
					} else {

					}
				} 
			}

			//COMANDO /Conferindo [RECEBE OS CHECKBOX DOS JOGADORES E ANALIZA SE SÃO TRUE OU FALSE]
			if (Command.equals("/Conferindo")){
				boolean acao;
				Acao = entrada.readLine();

				//Verifica qual jogador Enviou a Conferencia
				for (int i = 0 ; i< listaJogadores.size() ; i++){
					if (listaJogadores.get(i).equals(Acao)){
						Acao = entrada.readLine();

						//Condição para verificar se é true ou false
						if (Acao.equals("true")){
							acao = true;
						} else {
							acao = false;
						}

						//Adiciona a conferencia na arrayList
						ConfererirRespostas.add(acao);
					}
				}					
			}
			//COMANDO /ConferenciaPronta
			if (Command.equals("/ConferenciaPronta")){
				Confererido = Confererido+1;
				if (Confererido == listaJogadores.size()){
					ConfereEPontua();
					if (proxTipo.hasNext()){
						ProxTipo();
					} else {
						SomaEnviaEmostra();
					}
				}
			}
		}

	}

	//Repassa a msg para todos os jogadores conectados ao server
	public static void mandaPtodosJogadores(String msg){
		Iterator<PrintWriter> clientes = Write4Jogadores.iterator();
		while(clientes.hasNext()){
			PrintWriter saida = clientes.next();
			saida.println(msg);
			saida.flush();
		}
	}

	public boolean VerificaNick(String nick){
		boolean add = true;
		for(int i=0 ; i<listaJogadores.size() ; i++){
			if (listaJogadores.get(i).equals(nick)){
				add = false;
			}
		}
		if (add){
			listaJogadores.add(nick);
			return true;
		} else {
			return false;
		}
	}

	public void MandaTodosNicks(PrintWriter write4Jogador){
		for (int i = 0 ; i<listaJogadores.size() ; i++){
			write4Jogador.println("/nick");
			write4Jogador.println(listaJogadores.get(i));
			write4Jogador.flush();
		}
	}
	public static void ConfereEPontua(){
		//Enquanto conter algum lugar na ArrayList ConferirRespostas ele faz o FOR
		while (ConfererirRespostas.size() != 0){
			//CONFERE AS RESPOSTAS DOS JOGADORES
			for (int i = 0 ; i<listaJogadores.size() ; i++){
				//SE FOR VERDADEIRA A RESPOSTA ADICIONA +1 NA [RespostaValidaJogador]
				if (ConfererirRespostas.get(i)== true){
					RespostaValidaJogador[i] = RespostaValidaJogador[i]+1;
				}
			}

			//REMOVE AS RESPOSTAS JÁ CONFERIDAS
			for (int i = 0 ; i<listaJogadores.size() ; i++){
				ConfererirRespostas.remove(0);
			}
		}
		System.out.println("RESPOSTAS VALIDAS");

		for (int i = 0 ; i<listaJogadores.size() ; i++){
			System.out.println(RespostaValidaJogador[i]);
		}

		boolean DEZpontos = true;
		boolean RepostaVerdadeira = true;
		//COMPARA PARA SABER SE A RESPOSTA É VERDADEIRA
		for (int i = 0 ; i<listaJogadores.size() ; i++){

			//PEGA A RESPOSTA DO JOGADOR
			String tf_resposta = TiposJogadores.get(i);

			//Comparação[se a maioria ou a metade optar por resposta válida então a resposta é VERDAIDEIRA... 10 pontos]						
			if (RespostaValidaJogador[i]>=(listaJogadores.size()/2)){

				System.out.println();
				System.out.println("Metade dos Jogadores: "+listaJogadores.size()/2);
				System.out.println("Jogador da Comparação: "+listaJogadores.get(i));
				System.out.println("Resposta: "+TiposJogadores.get(i));


				//compara para saber se a respota do jogador é igual a dos outros jogadores
				System.out.println();
				for (int e = 0 ; e<listaJogadores.size() ; e++){								
					//PEGA A RESPOSTA DOS OUTROS JOGADORES
					String comparacao = TiposJogadores.get(e);


					System.out.println("Comparação: "+comparacao+" e "+tf_resposta);

					//COMPARA PARA VER SE É IGUAL AS RESPOSTAS E DIFERENTE DA RESPOSTA DELE MESMO
					if (tf_resposta.equals(comparacao) && i!=e){

						DEZpontos = false;

					} 
				}
			} else {
				RepostaVerdadeira = false;
			}

			//PONTUAÇÃO
			if (RepostaVerdadeira){
				if (DEZpontos == true){
					PontosRodadaJogador[i] = PontosRodadaJogador[i]+10;
					System.out.println("GANHOU 10 PONTOS");
				} else {
					PontosRodadaJogador[i] = PontosRodadaJogador[i]+5;
					System.out.println("GANHOU 5 PONTOS");
				}
			}	
			DEZpontos = true;
			RepostaVerdadeira = true;
			
			
		}
	}
	public static void ProxTipo(){
		//ZERA O CONFERIDO E ZERA A ARRAY QUE RECEBE AS CONFERÊNCIAS
		Confererido = 0;
		TiposJogadores.clear();

		//ZERA O PRONTOS PARA CONFERENCIA
		prontosPconferencia = 1;

		//ZERA AS RESPOSTAS VÁLIDAS DO JOGADOR
		for (int i = 0 ; i<listaJogadores.size() ; i++){
			RespostaValidaJogador[i] = 0;
		}

		//Manda um /ProxConferencia para iniciar proxima Conferencia
		mandaPtodosJogadores("/ProxConferencia");
		
		//Seta o Conferencia Como Ativo
		Conferencia.EnviarConferencia.setEnabled(true);
		for (int Z = 0 ; Z<listaJogadores.size() ; Z++){
			Conferencia.Valida[Z].setEnabled(true);
			((AbstractButton) Conferencia.Valida[Z]).setSelected(true);
		}

		//Todos tem que estar despreparados
		IniciarJogo.TodosDespreparados();
	}
	public static void SomaEnviaEmostra(){
		//ADICIONA OS PONTOS DA RODADA NOS PONTOS TOTAIS
		for (int i = 0; i<listaJogadores.size() ; i++){
			PontosTotalJogador[i] = PontosTotalJogador[i]+PontosRodadaJogador[i];
		}
		//manda os pontos da rodada e os pontos totais para o jogador
		for (int i = 0 ; i<listaJogadores.size() ; i++){
			//MANDA PARA TODOS
			mandaPtodosJogadores("/PontosRodada");
			mandaPtodosJogadores(listaJogadores.get(i));
			mandaPtodosJogadores(Integer.toString(PontosRodadaJogador[i]));
			mandaPtodosJogadores("/PontosTotais");
			mandaPtodosJogadores(listaJogadores.get(i));
			mandaPtodosJogadores(Integer.toString(PontosTotalJogador[i]));	

			//IMPLEMENTA NA INFERFACE
			Resultado.Jogadors[i].setText(listaJogadores.get(i));
			Resultado.PontosRodadaJogador[i].setText(Integer.toString(PontosRodadaJogador[i]));
			Resultado.PontosTotalJogador[i].setText(Integer.toString(PontosTotalJogador[i]));
		}
		//Inicia as labels com os pontos
		for (int i = 0; i<listaJogadores.size() ; i++){
			Resultado.Jogadors[i].setVisible(true);
			Resultado.PontosRodadaJogador[i].setVisible(true);
			Resultado.PontosTotalJogador[i].setVisible(true);
			Resultado.Underlines[i].setVisible(true);
		}
		//DA UM CLEAR NOS PONTOS DA RODADA
		for (int i = 0 ; i<listaJogadores.size() ; i++){
			PontosRodadaJogador[i] = 0;
		}

		IniciarJogo.TodosDespreparados();

		mandaPtodosJogadores("/showRESULTADO");	
		Resultado.MostraPanelResultado();

	}
	public static void Clear(){
		for (int i = 0 ; i<listaJogadores.size() ; i++){
			((JTextComponent) Conferencia.Confere_users[i]).setText("");
		}
		for (int i = 0 ; i<tiposSelecionados.size() ; i++){
			((JTextComponent) Jogar.tf_Tipos[i]).setText("");
		}
		
		//habilita a conferencia
		Conferencia.EnviarConferencia.setEnabled(true);
		for (int Z = 0 ; Z<listaJogadores.size() ; Z++){
			Conferencia.Valida[Z].setEnabled(true);
			((AbstractButton) Conferencia.Valida[Z]).setSelected(true);
		}
		
		//Manda para o cliente habilitar a conferencia
		mandaPtodosJogadores("/habilitarConferencia");
		
		//ZERA O CONFERIDO E ZERA A ARRAY QUE RECEBE AS CONFERÊNCIAS
		Confererido = 0;
		TiposJogadores.clear();

		//ZERA O PRONTOS PARA CONFERENCIA
		prontosPconferencia = 1;
		
		//Todos tem que estar despreparados
		IniciarJogo.TodosDespreparados();
		
		//Manda um clear para o cliente
		mandaPtodosJogadores("/Clear");
	}
}
