package View.Jogo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import SocketConnection.Jogador;
import SocketConnection.Server;
import View.Objetos;

public class Jogar extends Objetos{
	private static final long serialVersionUID = 1L;
	public static JPanel panelJogoIniciado;
	public static JLabel info_rodada, info_letraSelecinadaParaRodada, rodada, letraSelecinadaParaRodada,de, qnd_rodadas;
	private JLabel tipo0, tipo1, tipo2, tipo3, tipo4, tipo5, tipo6, tipo7, tipo8,
	tipo9, tipo10, tipo11, tipo12, tipo13, tipo14;
	private JTextField tfTipo0, tfTipo1, tfTipo2, tfTipo3, tfTipo4, tfTipo5, tfTipo6,
	tfTipo7, tfTipo8, tfTipo9, tfTipo10, tfTipo11, tfTipo12, tfTipo13, tfTipo14;
	public static JButton Stop, desistir_da_rodada;
	public static Component[] Label_Tipos = new Component[15];
	public static Component[] tf_Tipos = new Component[15];
	private static int Servidor1User2;
	private String Nick;

	public void InitJogo(int Servidor1User2, String Nick){
		this.Nick = Nick;
		Jogar.Servidor1User2 = Servidor1User2;
		panelJogoIniciado = new JPanel();
		panelJogoIniciado.setLayout(null);
		panelJogoIniciado.setBorder(BorderFactory.createTitledBorder("Jogar"));
		panelJogoIniciado.setBounds(10,10,775,290);

		info_rodada = newJLabelPadrao("Rodada", 15, 20, 100, 25);
		panelJogoIniciado.add(info_rodada);

		rodada = newJLabelTitulo("1", 80, 18, 50, 25);
		panelJogoIniciado.add(rodada);

		de = newJLabelPadrao("de", 110, 20, 100, 25);
		panelJogoIniciado.add(de);

		qnd_rodadas = newJLabelTitulo("13", 140, 18, 100, 25);
		panelJogoIniciado.add(qnd_rodadas);

		info_letraSelecinadaParaRodada = newJLabelPadrao("Letra:", 220, 20, 200, 25);
		panelJogoIniciado.add(info_letraSelecinadaParaRodada);

		letraSelecinadaParaRodada = newJLabelTitulo("A", 280, 18, 200, 25);
		letraSelecinadaParaRodada.setFont(new Font("Powderfinger Type", Font.BOLD, 25));
		letraSelecinadaParaRodada.setForeground(Color.RED);
		panelJogoIniciado.add(letraSelecinadaParaRodada);


		//PRIMEIRA FILEIRA DE TIPOS
		tipo0 = newJLabelPadrao("tipo0", 5, 60, 110, 25);
		Label_Tipos[0] = tipo0;

		tfTipo0 = newJTextFieldPadrao(110, 58, 120, 28);
		tf_Tipos[0] = tfTipo0;

		tipo3 = newJLabelPadrao("tipo3", 5, 90, 110, 25);
		Label_Tipos[3] = tipo3;

		tfTipo3 = newJTextFieldPadrao(110, 88, 120, 28);
		tf_Tipos[3] = tfTipo3;

		tipo6 = newJLabelPadrao("tipo6", 5, 120, 110, 25);
		Label_Tipos[6] = tipo6;

		tfTipo6 = newJTextFieldPadrao(110, 118, 120, 28);
		tf_Tipos[6] = tfTipo6;

		tipo9 = newJLabelPadrao("tipo9", 5, 150, 110, 25);
		Label_Tipos[9] = tipo9;

		tfTipo9 = newJTextFieldPadrao(110, 148, 120, 28);
		tf_Tipos[9] = tfTipo9;

		tipo12 = newJLabelPadrao("tipo12", 5, 180, 110, 25);
		Label_Tipos[12] = tipo12;

		tfTipo12 = newJTextFieldPadrao(110, 178, 120, 28);
		tf_Tipos[12] = tfTipo12;

		//SEGUNDA FILEIRA DE TIPOS		
		tipo1 = newJLabelPadrao("tipo1", 270, 60, 110, 25);
		Label_Tipos[1] = tipo1;

		tfTipo1 = newJTextFieldPadrao(380, 58, 120, 28);
		tf_Tipos[1] = tfTipo1;

		tipo4 = newJLabelPadrao("tipo4", 270, 90, 110, 25);
		Label_Tipos[4] = tipo4;

		tfTipo4 = newJTextFieldPadrao(380, 88, 120, 28);
		tf_Tipos[4] = tfTipo4;

		tipo7 = newJLabelPadrao("tipo7", 270, 120, 110, 25);
		Label_Tipos[7] = tipo7;

		tfTipo7 = newJTextFieldPadrao(380, 118, 120, 28);
		tf_Tipos[7] = tfTipo7;

		tipo10 = newJLabelPadrao("tipo10", 270, 150, 110, 25);
		Label_Tipos[10] = tipo10;

		tfTipo10 = newJTextFieldPadrao(380, 148, 120, 28);
		tf_Tipos[10] = tfTipo10;

		tipo13 = newJLabelPadrao("tipo13", 270, 180, 110, 25);
		Label_Tipos[13] = tipo13;

		tfTipo13 = newJTextFieldPadrao(380, 178, 120, 28);
		tf_Tipos[13] = tfTipo13;

		//TERCEIRA FILEIRA DE TIPOS
		tipo2 = newJLabelPadrao("tipo2", 530, 60, 110, 25);
		Label_Tipos[2] = tipo2;

		tfTipo2 = newJTextFieldPadrao(640, 58, 120, 28);
		tf_Tipos[2] = tfTipo2;

		tipo5 = newJLabelPadrao("tipo5", 530, 90, 110, 25);
		Label_Tipos[5] = tipo5;

		tfTipo5 = newJTextFieldPadrao(640, 88, 120, 28);
		tf_Tipos[5] = tfTipo5;

		tipo8 = newJLabelPadrao("tipo8", 530, 120, 110, 25);
		Label_Tipos[8] = tipo8;

		tfTipo8 = newJTextFieldPadrao(640, 118, 120, 28);
		tf_Tipos[8] = tfTipo8;	

		tipo11 = newJLabelPadrao("tipo11", 530, 150, 110, 25);
		Label_Tipos[11] = tipo11;

		tfTipo11 = newJTextFieldPadrao(640, 148, 120, 28);
		tf_Tipos[11] = tfTipo11;	

		tipo14 = newJLabelPadrao("tipo14", 530, 180, 110, 25);
		Label_Tipos[14] = tipo14;

		tfTipo14 = newJTextFieldPadrao(640, 178, 120, 28);
		tf_Tipos[14] = tfTipo14;

		//Adicionar as Labels e os tf no objeto[]
		for (int i = 0 ; i<15 ; i++){
			panelJogoIniciado.add(Label_Tipos[i]);
			panelJogoIniciado.add(tf_Tipos[i]);
		}
		//Setar como invisível
		for (int i = 0 ; i<15 ; i++){
			Label_Tipos[i].setVisible(false);
			tf_Tipos[i].setVisible(false);
		}




		//BUTTONS
		Stop = newJButton("STOP", 400, 240, 150, 40);
		Stop.setFont(new Font("Powderfinger Type", Font.BOLD, 20));
		Stop.setForeground(Color.RED);
		panelJogoIniciado.add(Stop);

		desistir_da_rodada = newJButton("Desistir da Rodada", 560, 253, 200, 25);
		panelJogoIniciado.add(desistir_da_rodada);		
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (Servidor1User2 == 1){
			//BOTÃO STOP DO SERV
			if (evento.getSource() == Stop){
				boolean Stop = true;
				//Confere se os TextFields Não Estiverem Vazios
				for (int i = 0 ; i<Server.tiposSelecionados.size() ; i++){
					if (((JTextComponent) Jogar.tf_Tipos[i]).getText().trim().equals("")){
						Stop = false;
					}
				}
				if (Stop){
					
					//Adiciona o primeiro tipo do Servidor Na Array TiposServidor
					for (int i = 0 ; i<Server.tiposSelecionados.size() ; i++){
						Server.TiposServidor.add(((JTextComponent) Jogar.tf_Tipos[i]).getText());
					}
					
					Server.prontosPconferencia = Server.prontosPconferencia+1;
					
					//Manda Para Todos ...
					Server.mandaPtodosJogadores("/msg");
					Server.mandaPtodosJogadores("["+Nick+" pediu Stop!]\n");
					Server.mandaPtodosJogadores("/STOP");

					//Implementa Na Interface Chat...
					Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"["+Nick+" pediu Stop!]\n");
				} else {
					newJOPInfMsg("Você Precisa Completar Todos Os Tipos Antes!", "STOP");
				}

			}

			//BOTÃO DESISTIR DO SERV
			if (evento.getSource() == desistir_da_rodada){
				desistir_da_rodada.setEnabled(false);
				//Manda Para Todos...
				Server.mandaPtodosJogadores("/msg");
				Server.mandaPtodosJogadores("["+Nick+" desistiu da rodada!]\n");
				Server.desistiu = Server.desistiu+1;

				if (Server.desistiu == Server.listaJogadores.size()){
					Server.mandaPtodosJogadores("/STOP");
					
					
					//Chama o Método que irá implementar a interface conferir no servidor
					Conferir();
				}

				//Implementa na Interface...
				Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+"["+Nick+" desistiu da rodada!]\n");				
			}
		}
		//jogador ...
		else {
			//BOTÃO STOP DO JOGADOR
			if (evento.getSource() == Stop){
				//Confere se os TextFields Não Estiverem Vazios
				boolean Stop = true;
				for (int i = 0 ; i<Jogador.tiposSelecionados.size() ; i++){
					if (((JTextComponent) Jogar.tf_Tipos[i]).getText().trim().equals("")){
						Stop = false;
					}
				}
				if (Stop){
					//Manda o STOP para o servidor caso seja Jogador...
					Jogador.getSaida().println("/STOP");
					Jogador.getSaida().println("/msg");
					Jogador.getSaida().println("["+Nick+" pediu Stop!]\n");
				}else {
					newJOPInfMsg("Você Precisa Completar Todos Os Tipos Antes!", "STOP");
				}
			}

			//BOTÃO DESISTIR DO JOGADOR
			if (evento.getSource() == desistir_da_rodada){

				//Manda para o servidor a desistência
				desistir_da_rodada.setEnabled(false);
				Jogador.getSaida().println("/msg");
				Jogador.getSaida().println("["+Nick+" desistiu da rodada!]\n");
				Jogador.getSaida().println("/desistir");
			}

		}

	}
	
	//método para setar os labels e os tf como visíveis;
	public static void Conferir(){
		if (Servidor1User2 == 1){
			//DEIXA OS TF E OS CHECKBOX'S VISIVEIS
			for (int i = 0 ; i<Server.listaJogadores.size() ; i++){
				Conferencia.Confere_users[i].setVisible(true);
				Conferencia.Valida[i].setVisible(true);
			}

			//Implementa o panelConferencia
			panelJogoIniciado.setVisible(false);
			Conferencia.panelConferencia.setVisible(true);
		} else {
			//DEIXA OS TF E OS CHECKBOX'S VISIVEIS
			
			for (int i = 0 ; i<Jogador.listaNicks.size() ; i++){
				Conferencia.Confere_users[i].setVisible(true);
				Conferencia.Valida[i].setVisible(true);
			}

			//Inicia O panelConferencia
			panelJogoIniciado.setVisible(false);
			Conferencia.panelConferencia.setVisible(true);
		}
	}

}
