package View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

public class getHour {
	GregorianCalendar hour;
	SimpleDateFormat formato;
	static String data;
	Date agora;
	
	public String getHourNow(){
		hour = new GregorianCalendar();
		formato = new SimpleDateFormat("HH");
		return data = formato.format(hour.getTime());		
	}
	
	public void msgBoasVindas(String jogador){
		int hour = Integer.parseInt(getHourNow());
		if (hour<=05){
			JOptionPane.showMessageDialog(null, "Boa Madrugada "+jogador+", tenha um excelente jogo.", "Bem Vindo!", JOptionPane.INFORMATION_MESSAGE);
		}
		if (hour>=06 && hour<=11){
			JOptionPane.showMessageDialog(null, "Bom Dia "+jogador+", tenha um excelente jogo.", "Bem Vindo!", JOptionPane.INFORMATION_MESSAGE);
		}
		if (hour>=12 && hour<=17){
			JOptionPane.showMessageDialog(null, "Boa Tarde "+jogador+", tenha um excelente jogo.", "Bem Vindo!", JOptionPane.INFORMATION_MESSAGE);
		}
		if (hour>=18 && hour<=23){
			JOptionPane.showMessageDialog(null, "Boa Noite "+jogador+", tenha um excelente jogo.", "Bem Vindo!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	

}
