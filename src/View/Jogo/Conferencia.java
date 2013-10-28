package View.Jogo;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import SocketConnection.Jogador;
import SocketConnection.Server;
import View.Objetos;

public class Conferencia extends Objetos{
	private static final long serialVersionUID = 1L;
	public static JPanel panelConferencia;
	public static JCheckBox c0, c1, c2, c3, c4, c5, c6, c7;
	private JLabel info_tipo, info_rodada, info_valida, info_letra;
	public static JLabel tipo, rodada, letra;
	private JTextField Confere_user0, Confere_user1, Confere_user2, Confere_user3, Confere_user4,
	Confere_user5, Confere_user6, Confere_user7;
	private JCheckBox valida0, valida1, valida2, valida3, valida4, valida5, valida6, valida7, exemplo;
	public static Component[] Confere_users = new Component[8];
	public static Component[] Valida = new Component[8];
	public static JButton EnviarConferencia;
	private int Servidor1User2;


	public void InitConferencia(int Servidor1User2){
		this.Servidor1User2 = Servidor1User2;
		InitConf();
	}
	public void InitConf(){
		panelConferencia = new JPanel();
		panelConferencia.setLayout(null);
		panelConferencia.setBorder(BorderFactory.createTitledBorder("Conferência"));
		panelConferencia.setBounds(10,10,775,290);

		info_rodada = newJLabelPadrao("Rodada", 15, 20, 100, 25);
		panelConferencia.add(info_rodada);

		rodada = newJLabelTitulo("1", 80, 18, 50, 25);
		panelConferencia.add(rodada);

		info_letra = newJLabelPadrao("Letra:", 115, 20, 100, 25);
		panelConferencia.add(info_letra);

		letra = newJLabelTitulo("A", 176, 18, 50, 25);
		panelConferencia.add(letra);

		info_tipo = newJLabelPadrao("Tipo:", 220, 20, 200, 25);
		panelConferencia.add(info_tipo);

		tipo = newJLabelTitulo("Nome", 275, 18, 200, 25);
		panelConferencia.add(tipo);

		info_valida = newJLabelPadrao("*Para respostas válidas selecione", 370, 220, 340, 25);
		panelConferencia.add(info_valida);

		exemplo = new JCheckBox();
		exemplo.setSelected(true);
		exemplo.setBounds(710, 220, 50, 25);
		exemplo.setEnabled(false);
		panelConferencia.add(exemplo);



		Confere_user0 = newJTextFieldPadrao(110, 58, 120, 28);
		Confere_users[0] = Confere_user0;

		valida0 = new JCheckBox();
		valida0.setBounds(90, 58, 25, 28);
		Valida[0] = valida0;

		Confere_user1 = newJTextFieldPadrao(110, 88, 120, 28);
		Confere_users[1] = Confere_user1;

		valida1 = new JCheckBox();
		valida1.setBounds(90, 88, 25, 28);
		Valida[1] = valida1;

		Confere_user2 = newJTextFieldPadrao(110, 118, 120, 28);
		Confere_users[2] = Confere_user2;

		valida2 = new JCheckBox();
		valida2.setBounds(90, 118, 25, 28);
		Valida[2] = valida2;

		Confere_user3 = newJTextFieldPadrao(110, 148, 120, 28);
		Confere_users[3] = Confere_user3;

		valida3 = new JCheckBox();
		valida3.setBounds(90, 148, 25, 28);
		Valida[3] = valida3;

		Confere_user4 = newJTextFieldPadrao(110, 178, 120, 28);
		Confere_users[4] = Confere_user4;

		valida4 = new JCheckBox();
		valida4.setBounds(90, 178, 25, 28);
		Valida[4] = valida4;

		Confere_user5 = newJTextFieldPadrao(110, 208, 120, 28);
		Confere_users[5] = Confere_user5;

		valida5 = new JCheckBox();
		valida5.setBounds(90, 208, 25, 28);
		Valida[5] = valida5;

		Confere_user6 = newJTextFieldPadrao(110, 238, 120, 28);
		Confere_users[6] = Confere_user6;

		valida6 = new JCheckBox();
		valida6.setBounds(90, 238, 25, 28);
		Valida[6] = valida6;

		Confere_user7 = newJTextFieldPadrao(270, 58, 120, 28);
		Confere_users[7] = Confere_user7;

		valida7 = new JCheckBox();
		valida7.setBounds(250, 58, 25, 28);
		Valida[7] = valida7;

		for (int i = 0 ; i<Confere_users.length ; i++){
			//ADICIONA TODOS OS TEXTFIELDS PARA RESPOSTAS DE USUÁRIOS NO PANEL
			panelConferencia.add(Confere_users[i]);
			//NÃO SERÁ POSSÍVEL EDITAR OS TF...
			((JTextComponent) Confere_users[i]).setEditable(false);
			//TODOS OS TF DE CONFERENCIA INVISÍVEIS...
			Confere_users[i].setVisible(false);

			//ADICIONA OS CHECKBOX DE RESPOSTAS VÁLIDAS
			panelConferencia.add(Valida[i]);
			//DEIXA ELES TODOS SELECIONADOS
			((AbstractButton) Valida[i]).setSelected(true);
			//TODOS OS CHECKBOX INVISÍVEIS...
			Valida[i].setVisible(false);
		}


		EnviarConferencia = newJButton("Enviar Conferência", 300, 250, 200, 28);
		panelConferencia.add(EnviarConferencia);
	}

	public void actionPerformed(ActionEvent evento) {
		//SE O SERVIDOR CLICAR EM ENVIAR CONFERÊNCIA;
		if (Servidor1User2 == 1){

			//Conferindo CheckBox's do servidor
			for (int i = 0 ; i<Server.listaJogadores.size() ; i++){
				if (((AbstractButton) Valida[i]).isSelected()){
					Server.ConfererirRespostas.add(true);
				} else {
					Server.ConfererirRespostas.add(false);
				}
			}

			//Acrescenta +1 na lista dos que já conferiram
			Server.Confererido = Server.Confererido+1;
			
			//Da um Ok para o Gráfico
			ListaJogadores.JogPreparado[0].setVisible(true);
			
			//Manda para todos o envio da Conferencia [seta um OK! no server... na interface dos Jogadores]
			Server.mandaPtodosJogadores("/preparado");
			Server.mandaPtodosJogadores(Server.Nick);
			
			//Adiciona na ArrayList os preparados
			Server.preparados.add(Server.Nick);
			
			
			
			EnviarConferencia.setEnabled(false);
			for (int i = 0 ; i<Server.listaJogadores.size() ; i++){
				Valida[i].setEnabled(false);
			}

			//SE o server for o último a conferir
			if (Server.Confererido == Server.listaJogadores.size()){
				Server.ConfereEPontua();
				if (Server.proxTipo.hasNext()){
					Server.ProxTipo();
				} else {
					Server.SomaEnviaEmostra();
				}
			}
		}

		//SE O JOGADOR CLICAR EM ENVIAR CONFERÊNCIA;
		else {

			//Adiciona na ArrayList A Conferencia
			for (int i = 0 ; i<Jogador.listaNicks.size() ; i++){
				if (((AbstractButton) Valida[i]).isSelected()){
					Jogador.Conferido.add(true);
				} else {
					Jogador.Conferido.add(false);
				}
			}

			//Mandando Conferencia para o servidor
			for (int i = 0 ; i<Jogador.Conferido.size() ; i++){
				Jogador.getSaida().println("/Conferindo");
				Jogador.getSaida().println(Jogador.Nick);
				Jogador.getSaida().println(Jogador.Conferido.get(i));
			}
			
			//Esvazia a ArrayList de tipos Conferidos para efetuar uma nova conferência
			Jogador.Conferido.clear();
			
			//Manda um OK para o grafico
			Jogador.getSaida().println("/preparado");
			Jogador.getSaida().println(Jogador.Nick);
			
			

			//diz para o servidor que já enviou sua conferencia
			Jogador.getSaida().println("/ConferenciaPronta");			
			
			
			EnviarConferencia.setEnabled(false);
			for (int i = 0 ; i<Jogador.listaNicks.size() ; i++){
				Valida[i].setEnabled(false);
			}
		}

	}

}
