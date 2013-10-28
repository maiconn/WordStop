package View.Jogo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import SocketConnection.Server;
import View.Objetos;

public class ConfigJogo extends Objetos{
	private static final long serialVersionUID = 1L;
	public static JPanel panelConfigJogo;
	private JLabel Tipos, Rodadas;
	private JScrollPane barra_listaTipos;
	public static JComboBox rodadas;
	public static DefaultListModel modeloListaTipos;
	private JButton AdicionarTipo, TipoAleatorio, RemoverTipoSelecionado;
	private JTextField tfTipoAdd;
	JList listaTipos;

	public void InitConfigJogo(int Servidor1User2){
		panelConfigJogo = new JPanel();
		panelConfigJogo.setLayout(null);
		panelConfigJogo.setBorder(BorderFactory.createTitledBorder("Configurações do Jogo"));
		panelConfigJogo.setBounds(10,7,400,300);

		Tipos = newJLabelPadrao("Escolher Tipos",135,16,200,25);
		panelConfigJogo.add(Tipos);

		modeloListaTipos = new DefaultListModel();

		listaTipos = new JList(modeloListaTipos);
		barra_listaTipos = new JScrollPane(listaTipos);
		barra_listaTipos.setBounds(5,40,100,150);
		panelConfigJogo.add(barra_listaTipos);

		tfTipoAdd = newJTextFieldPadrao(5,192,100,28);
		panelConfigJogo.add(tfTipoAdd);

		AdicionarTipo = newJButton("Adicionar Tipo", 110,192,150,27);
		panelConfigJogo.add(AdicionarTipo);

		TipoAleatorio = newJButton("Sugerir Tipo", 110,160,150,25);
		panelConfigJogo.add(TipoAleatorio);

		RemoverTipoSelecionado = newJButton("Remover Tipo Selecionado", 110,45,240,25);
		panelConfigJogo.add(RemoverTipoSelecionado);

		Rodadas = newJLabelPadrao("Quantidade de Rodadas", 100,230,220,25);
		panelConfigJogo.add(Rodadas);
		rodadas = new JComboBox();

		for (int i=1 ; i<16 ; i++){
			rodadas.addItem(i);
		}

		if (Servidor1User2 == 1){
			rodadas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Server.quant_rodadas = rodadas.getSelectedItem().toString();
					Server.mandaPtodosJogadores("/rodada");
					Server.mandaPtodosJogadores(Integer.toString(rodadas.getSelectedIndex()));				
				}
			});
		}

		rodadas.setBounds(183,258,47,28);
		panelConfigJogo.add(rodadas);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == TipoAleatorio){
			Object[] tiposAleatorios = {"Nome", "Carro", "Fruta", "CEP", "Hardware", "Software", "Animal",
					"Cor", "Profissão", "Música", "Banda", "Artista", "Ator/Atriz", "Filme", "MSÉ","Objeto"};
			int Valor = 0 + (int)(Math.random()*15);
			tfTipoAdd.setText(tiposAleatorios[Valor].toString());

		}
		if (evento.getSource() == AdicionarTipo){
			boolean add=true;
			for (int i = 0 ; i<modeloListaTipos.getSize(); i++){				
				if (modeloListaTipos.get(i).equals(tfTipoAdd.getText())){
					add = false;
				}
			}
			if (!tfTipoAdd.getText().trim().equals("")){
				if (add){
					if (modeloListaTipos.getSize()<15){
						modeloListaTipos.addElement(tfTipoAdd.getText());

						//Adiciona Tipo Na ArrayList e manda para os jogadores o tipo
						Server.tiposSelecionados.add(tfTipoAdd.getText());
						Server.mandaPtodosJogadores("/tipoADD");
						Server.mandaPtodosJogadores(tfTipoAdd.getText());

						//Seta o texto com Null para receber outro valor
						tfTipoAdd.setText("");
					} else {
						newJOPInfMsg("No máximo 15 Tipos!", "Mensagem");
					}				
				} else {
					newJOPInfMsg("Este Tipo Já Foi Adicionado à Lista.", "Contém");
				}
			}

		}
		if (evento.getSource() == RemoverTipoSelecionado){
			if (listaTipos.getSelectedIndex() != -1){
				//Remove O tipo da ArrayList e manda para os jogadores o tipo
				Server.tiposSelecionados.remove(listaTipos.getSelectedValue());		
				Server.mandaPtodosJogadores("/tipoREMOVE");
				Server.mandaPtodosJogadores(listaTipos.getSelectedValue().toString());

				//Remove da JList
				modeloListaTipos.remove(listaTipos.getSelectedIndex());						
			}
		}		
	}

}
