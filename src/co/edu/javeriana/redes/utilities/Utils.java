package co.edu.javeriana.redes.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import co.edu.javeriana.redes.interfaz.ConsolePanel;

public class Utils {
	private static final String octet = "([0-9]|[1-9][0-9]|(1[0-9][0-9]|2[0-4][0-9]|25[0-5]))"; // Acepts numbers that are only between [0 - 255]
	public static final String validAddress = octet + "." + octet + "." + octet + "." + octet;
	public static String fileName = "./devices.bin";
	
	public static String byteArrayToHexString(byte[] array, String separator) {
		String byteArray = "";
		int n = 1;
		for (byte b : array) {
			String bt = "" + Integer.toHexString(b & 0xff);
			byteArray += ((bt.length() == 1) ? "0" : "" ) + bt;
			byteArray += ((n < array.length) ? separator : "");
			n++;
		}
		return byteArray;
	}
	
	public static String byteArrayToDecString(byte[] array, String separator) {
		String byteArray = "";
		int n = 1;
		for (byte b : array) {
			byteArray += Integer.toString(b & 0xff);
			byteArray += ((n < array.length) ? separator : "");
			n++;
		}
		return byteArray;
	}
	
	public static void saveObject(String fileName, Object object) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			ObjectOutputStream output = new ObjectOutputStream(fos);
			output.writeObject(object);
			output.close();
		} catch (Exception evento) {
			ConsolePanel.print("Error saving the object [" + evento + "]");
		}
	}
	
	public static Object readObject(String fileName) {
		Object object = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream input = new ObjectInputStream(fis);
				object = input.readObject();
				input.close();
			}
		} catch (Exception evento) {
			ConsolePanel.print("Error reading the file '" + fileName + "' [" + evento + "]");
		}
		return object;
	}
	
	public static PrintWriter writeFile(File archivo) {
		try {
			FileWriter file = new FileWriter(archivo);
			PrintWriter printWriter = new PrintWriter(file);
			return printWriter;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String convertirGregorianFecha(GregorianCalendar fecha) {
		int ddia = fecha.get(Calendar.DATE);
		int mmes = fecha.get(Calendar.MONTH) + 1;
		String anio = "" + fecha.get(Calendar.YEAR);
		int hhora = fecha.get(Calendar.HOUR_OF_DAY);
		int mminuto = fecha.get(Calendar.MINUTE);
		int ssegundo = fecha.get(Calendar.SECOND);
		
		String dia = ((ddia < 10) ? "0" : "" ) + ddia;
		String mes = ((mmes < 10) ? "0" : "" ) + mmes;
		String hora = ((hhora < 10) ? "0" : "" ) + hhora;
		String minuto = ((mminuto < 10) ? "0" : "" ) + mminuto;
		String segundo = ((ssegundo < 10) ? "0" : "" ) + ssegundo;
		
		
		String fechaS = anio + "-" + mes + "-" + dia + " - " + hora + "." + minuto + "." + segundo;
		return fechaS;
	}
}
