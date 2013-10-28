package View;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.MaskFormatter;

public abstract class Objetos extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;

	public JLabel newJLabelPadrao(String titulo, int x, int y, int w, int h){
		JLabel label = new JLabel(titulo);
		label.setBounds(x,y,w,h);
		label.setFont(new Font("Powderfinger Type", Font.TYPE1_FONT, 15));
		label.setForeground(new Color(81,81,81));
		return label;
	}
	
	public JLabel newJLabelObrigatoria(int x, int y, int w, int h){
		JLabel label = new JLabel("*");
		label.setBounds(x,y,w,h);
		label.setFont(new Font("Powderfinger Type", Font.TYPE1_FONT, 20));
		label.setForeground(Color.RED);
		label.setToolTipText("Campo Obrigatório");
		label.setVisible(false);
		return label;
	}
	
	public JLabel newJLabelTitulo(String titulo, int x, int y, int w, int h){
		JLabel label = new JLabel(titulo);
		label.setBounds(x,y,w,h);
		label.setFont(new Font("Powderfinger Type", Font.BOLD, 20));
		label.setForeground(new Color(0,0,0));
		return label;
	}
	
	public JTextField newJTextFieldPadrao(int x, int y, int w, int h){
		JTextField tf = new JTextField();
		tf.setBounds(x,y,w,h);
		tf.setFont(new Font("Powderfinger Type", Font.PLAIN, 13));
		return tf;
	}
	
	public JToggleButton newJToggleButtonLetras(String titulo, final int x, final int y){
		final JToggleButton button = new JToggleButton(titulo, false);
		button.setBounds(x,y,50,50);
		button.setFont(new Font("Powderfinger Smudged", Font.PLAIN, 22));
		button.addActionListener(this);
		return button;
	}
	public JPanel newJPanel(int x, int y, int w, int h){
		JPanel panel = new JPanel();
		panel.setBounds(x, y, w, h);
		panel.setLayout(null);
		return panel;
	}
	public JButton newJButton(String titulo, int x, int y, int w, int h){
		JButton button = new JButton(titulo);
		button.setBounds(x,y,w,h);
		button.setFont(new Font("Powderfinger Type", Font.BOLD, 13));
		button.addActionListener(this);
		return button;
	}
	public JFormattedTextField newJFormattedTextField(String mascara, int x, int y, int w, int h){
		JFormattedTextField ftf = null;
		try {
			MaskFormatter mask = new MaskFormatter(mascara);
			ftf = new JFormattedTextField(mask);
			ftf.setBounds(x,y,w,h);
			ftf.setFont(new Font("Powderfinger Type", Font.TYPE1_FONT, 15));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ftf;
	}
	public JComboBox newJComboBox(int x, int y, int w, int h){
		JComboBox combo = new JComboBox();
		combo.setBounds(x,y,w,h);
		combo.setFont(new Font("Powderfinger Type", Font.TYPE1_FONT, 13));
		return combo;
	}
	
	//Constrói Msgs Diversas
	public void newJOPWarningMessage(String msg, String titulo){
		JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.WARNING_MESSAGE);
	}
	
	public void newJOPErrorMsg(String msg, String titulo){
		JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.ERROR_MESSAGE);
	}
	
	public void newJOPInfMsg(String msg, String titulo){
		JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void newJOPQuestionMsg(String msg, String titulo){
		JOptionPane.showMessageDialog(null, msg, titulo, JOptionPane.QUESTION_MESSAGE);
	}
	
	public int newJOPConfirmDialogYes_No(String msg, String titulo){
		if (JOptionPane.showConfirmDialog(null, msg, titulo, JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){;
		return JOptionPane.YES_OPTION;
	    } else {
	    	return JOptionPane.NO_OPTION;
	    }
	
	}
	
	public int newJOPConfirmDialogYes_No_Cancel(String msg, String titulo){
		if (JOptionPane.showConfirmDialog(null, msg, titulo, JOptionPane.YES_NO_CANCEL_OPTION)==JOptionPane.YES_OPTION){;
		return JOptionPane.YES_OPTION;
	    } else {
	    	return JOptionPane.NO_OPTION;
	    }
	}	
	
	public JLabel JogadorPreparado (int x, int y){
		JLabel label = new JLabel("OK!");
		label.setForeground(Color.red);
		label.setBounds(x,y,40,40);
		label.setFont(new Font("Arial", Font.BOLD, 11));
		label.setVisible(false);
		return label;
	}

}
