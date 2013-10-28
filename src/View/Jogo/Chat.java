package View.Jogo;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import SocketConnection.Jogador;
import SocketConnection.Server;
import View.Objetos;

public class Chat extends Objetos{
	private static final long serialVersionUID = 1L;
	JPanel panelChat;
	private static JTextArea msgs_chat;
	private JScrollPane barra_msg_chat;
	private JTextField tfMandarMsg;
	private JButton EnviarMsg;
	private int Serv1Jogador2;
	private String Nick;

	public void InitChat(int Serv1Jogador2, String Nick){
		this.Nick = Nick;
		this.Serv1Jogador2 = Serv1Jogador2;
		panelChat = new JPanel();
		panelChat.setLayout(null);
		panelChat.setBackground(Color.gray);
		panelChat.setBorder(BorderFactory.createTitledBorder("Chat"));
		panelChat.setBounds(10,310,585,208);

		setMsgs_chat(new JTextArea());
		getMsgs_chat().setWrapStyleWord(true);
		getMsgs_chat().setLineWrap(true);
		getMsgs_chat().setEditable(false);
		barra_msg_chat = new JScrollPane(getMsgs_chat());
		barra_msg_chat.setBounds(15,20,555,140);
		panelChat.add(barra_msg_chat);		

		tfMandarMsg = newJTextFieldPadrao(15,160,450,28);
		panelChat.add(tfMandarMsg);

		EnviarMsg = newJButton("Enviar", 470,160,100,40);
		panelChat.add(EnviarMsg);
	}



	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == EnviarMsg){
			if (Serv1Jogador2 == 2){
				//Manda a msg para o PrintWriter saida... (getSaida)
				if (!tfMandarMsg.getText().equals("")){
					Jogador.getSaida().println("/msg");
					Jogador.getSaida().println(Nick+": "+tfMandarMsg.getText()+"\n");
					//Seta o JTextField como nulo para escrever outra msg
					tfMandarMsg.setText("");
				}
			} else {
				if (!tfMandarMsg.getText().equals("")){
					Server.mandaPtodosJogadores("/msg");
					Server.mandaPtodosJogadores(Nick+": "+tfMandarMsg.getText()+"\n");
					Chat.getMsgs_chat().setText(Chat.getMsgs_chat().getText()+Nick+": "+tfMandarMsg.getText()+"\n");
					tfMandarMsg.setText("");
				}
			}
		}

	}



	public void setMsgs_chat(JTextArea msgs_chat) {
		Chat.msgs_chat = msgs_chat;
	}

	public static JTextArea getMsgs_chat() {
		return msgs_chat;
	}


}
