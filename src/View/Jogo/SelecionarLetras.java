package View.Jogo;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import SocketConnection.Server;
import View.Objetos;

public class SelecionarLetras extends Objetos{
	private static final long serialVersionUID = 1L;
	public static JPanel panelSelecionarLetras;
	public static JToggleButton A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,X,W,Y,Z;
	
	public void InitSelecionarLetras(){
		panelSelecionarLetras = new JPanel();
		panelSelecionarLetras.setLayout(null);
		panelSelecionarLetras.setBorder(BorderFactory.createTitledBorder("Letras Válidas"));
		panelSelecionarLetras.setBounds(420,7,370,225);
		
		//LETRAS
		//primeira fileira
		A = newJToggleButtonLetras("a", 10, 20);
		panelSelecionarLetras.add(A);

		B = newJToggleButtonLetras("b", 60, 20);
		panelSelecionarLetras.add(B);

		C = newJToggleButtonLetras("c", 110, 20);
		panelSelecionarLetras.add(C);

		D = newJToggleButtonLetras("d", 160, 20);
		panelSelecionarLetras.add(D);

		E = newJToggleButtonLetras("e", 210, 20);
		panelSelecionarLetras.add(E);

		F = newJToggleButtonLetras("f", 260, 20);
		panelSelecionarLetras.add(F);

		G = newJToggleButtonLetras("g", 310, 20);
		panelSelecionarLetras.add(G);

		//segunda fileira
		H = newJToggleButtonLetras("h", 10, 70);
		panelSelecionarLetras.add(H);

		I = newJToggleButtonLetras("i", 60, 70);
		panelSelecionarLetras.add(I);

		J = newJToggleButtonLetras("j", 110, 70);
		panelSelecionarLetras.add(J);

		K = newJToggleButtonLetras("k", 160, 70);
		panelSelecionarLetras.add(K);

		L = newJToggleButtonLetras("l", 210, 70);
		panelSelecionarLetras.add(L);

		M = newJToggleButtonLetras("m", 260, 70);
		panelSelecionarLetras.add(M);

		N = newJToggleButtonLetras("n", 310, 70);
		panelSelecionarLetras.add(N);

		//terceira fileira
		O = newJToggleButtonLetras("o", 10, 120);
		panelSelecionarLetras.add(O);

		P = newJToggleButtonLetras("p", 60, 120);
		panelSelecionarLetras.add(P);

		Q = newJToggleButtonLetras("q", 110, 120);
		panelSelecionarLetras.add(Q);

		R = newJToggleButtonLetras("r", 160, 120);
		panelSelecionarLetras.add(R);

		S = newJToggleButtonLetras("s", 210, 120);
		panelSelecionarLetras.add(S);

		T = newJToggleButtonLetras("t", 260, 120);
		panelSelecionarLetras.add(T);

		U = newJToggleButtonLetras("u", 310, 120);
		panelSelecionarLetras.add(U);

		//quarta fileira
		V = newJToggleButtonLetras("v", 10, 170);
		panelSelecionarLetras.add(V);

		W = newJToggleButtonLetras("w", 60, 170);
		panelSelecionarLetras.add(W);

		X = newJToggleButtonLetras("x", 110, 170);
		panelSelecionarLetras.add(X);

		Y = newJToggleButtonLetras("y", 160, 170);
		panelSelecionarLetras.add(Y);

		Z = newJToggleButtonLetras("z", 210, 170);
		panelSelecionarLetras.add(Z);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JToggleButton button = (JToggleButton) e.getSource();
		verificaClick(button, button.getX(), button.getY());		
	}
	

	public JToggleButton verificaClick(JToggleButton Button, int x , int y){
		if (Button.isSelected()){
			Button.setFont(new Font("Powderfinger Smudged", Font.BOLD, 30));
			Button.setText(Button.getText().toUpperCase());
			Button.setBounds(x,y,52,52);	
			
			//Adiciona A letra selecionada na ArrayList e manda para os jogadores
			Server.letrasSelecionadas.add(Button.getText());
			Server.mandaPtodosJogadores("/letraSelecionada");
			Server.mandaPtodosJogadores(Button.getText());
		} else {
			Button.setText(Button.getText().toLowerCase());
			Button.setFont(new Font("Powderfinger Smudged", Font.PLAIN, 22));
			Button.setBounds(x,y,50,50);
			//Remove A letra desselecionada na ArrayList e manda para os jogadores
			Server.letrasSelecionadas.remove(Button.getText().toUpperCase());
			Server.mandaPtodosJogadores("/letraSelecionada");
			Server.mandaPtodosJogadores(Button.getText().toUpperCase());
		}
		return Button;
	}

}
