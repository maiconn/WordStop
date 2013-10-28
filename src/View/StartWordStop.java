package View;

import java.awt.Insets;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import ch.randelshofer.quaqua.QuaquaManager;

public class StartWordStop {
	public static void main(String[] args) {  
		try {  
			//SETANDO CONFIGURAÇÕES NO LOOK AND FEEL
			System.setProperty("Quaqua.tabLayoutPolicy","wrap");
			UIManager.put("Panel.opaque", Boolean.TRUE);
			UIManager.put("Quaqua.Component.cellRendererFor", Boolean.TRUE);
			UIManager.put("Component.visualMargin", new Insets(2,2,2,2));
			//SETANDO O LOOK AND FEEL
			UIManager.setLookAndFeel(QuaquaManager.getLookAndFeel());
			
			//Iniciando a TelaPrincipal
			new TelaPrincipal();   


		} catch (Exception e) {  
			JOptionPane.showMessageDialog(null, "Erro Fatal no Look And Feel"+e);  
		}  

	}  
}