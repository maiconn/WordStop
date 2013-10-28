package View.Jogo;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import View.Objetos;

public class ListaJogadores extends Objetos{
	private static final long serialVersionUID = 1L;
	JPanel panelJogadoresOnline;
	private static DefaultListModel modeloListaJogadores;
	public static Component[] JogPreparado = new Component[8];
	private JLabel JP0, JP1, JP2, JP3, JP4, JP5, JP6, JP7;
	private JScrollPane barra_JogadoresOnline;
	private JList listaJogadoresOnline;
	
	public void InitListaJogadores(){
		panelJogadoresOnline = new JPanel();
		panelJogadoresOnline.setLayout(null);
		panelJogadoresOnline.setBackground(Color.lightGray);
		panelJogadoresOnline.setBorder(BorderFactory.createTitledBorder("Jogadores"));
		panelJogadoresOnline.setBounds(600,310,185,208);
		
		setModeloListaJogadores(new DefaultListModel());
		
		//Preparados...
		JP0 = JogadorPreparado(15,13);
		JogPreparado[0] = JP0;
		
		JP1 = JogadorPreparado(15,31);
		JogPreparado[1] = JP1;
		
		JP2 = JogadorPreparado(15,50);
		JogPreparado[2] = JP2;
		
		JP3 = JogadorPreparado(15,67);
		JogPreparado[3] = JP3;
		
		JP4 = JogadorPreparado(15,85);
		JogPreparado[4] = JP4;
		
		JP5 = JogadorPreparado(15,102);
		JogPreparado[5] = JP5;
		
		JP6 = JogadorPreparado(15,120);
		JogPreparado[6] = JP6;
		
		JP7 = JogadorPreparado(15,139);
		JogPreparado[7] = JP7;

		for (int i = 0 ; i< JogPreparado.length ; i++){
			panelJogadoresOnline.add(JogPreparado[i]);
		}
		
		
		listaJogadoresOnline = new JList(getModeloListaJogadores());
		listaJogadoresOnline.setSelectionMode(0);
		barra_JogadoresOnline = new JScrollPane(listaJogadoresOnline);
		barra_JogadoresOnline.setBounds(40,20,135,183);
		panelJogadoresOnline.add(barra_JogadoresOnline);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		
	}
	
	
	
	//GET SET...
	public void setModeloListaJogadores(DefaultListModel modeloListaJogadores) {
		ListaJogadores.modeloListaJogadores = modeloListaJogadores;
	}

	public static DefaultListModel getModeloListaJogadores() {
		return modeloListaJogadores;
	}

}
