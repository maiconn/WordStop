package View;

import javax.swing.JOptionPane;

public class exemplo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String testz = JOptionPane.showInputDialog("Digita ai");
		if (testz.startsWith("/nick")){
			JOptionPane.showMessageDialog(null, "Cont�m!");
		}
		StringBuffer teste = new StringBuffer(testz);
		//String teste;
		
		if (teste.indexOf("[001]")==0){
			JOptionPane.showMessageDialog(null, "Cont�m!");
			teste.replace(0, 5, "");
		} else {
			JOptionPane.showMessageDialog(null, "N�o Cont�m!");
		}
		
		teste.toString();
		
		System.out.println(teste);

	}

}
