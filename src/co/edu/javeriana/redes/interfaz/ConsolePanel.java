package co.edu.javeriana.redes.interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.GregorianCalendar;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import co.edu.javeriana.redes.utilities.Utils;

public class ConsolePanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private static JTextArea console;
	private JMenuItem mntmLiberarScroll;
	private JMenuItem mntmGuardarLog;

	public ConsolePanel() {
		
		console = new JTextArea();
		console.setEditable(false);
		console.setText("");
		console.setBackground(new Color(0, 0, 128));
		console.setForeground(SystemColor.inactiveCaptionBorder);
		DefaultCaret caret = ((DefaultCaret) console.getCaret());
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		setViewportView(console);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(console, popupMenu);
		
		mntmGuardarLog = new JMenuItem("Guardar log");
		mntmGuardarLog.setIcon(new ImageIcon(new ImageIcon(ConsolePanel.class.getResource("../resources/save.png")).getImage()));
		mntmGuardarLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConsolePanel.guardarLog();
			}
		});
		popupMenu.add(mntmGuardarLog);
		
		mntmLiberarScroll = new JMenuItem("Liberar scroll");
		mntmLiberarScroll.setIcon(new ImageIcon(new ImageIcon(ConsolePanel.class.getResource("../resources/unlocked.png")).getImage()));
		mntmLiberarScroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
				mntmLiberarScroll.setVisible(false);
			}
		});
		popupMenu.add(mntmLiberarScroll);
	}
	
	public static void guardarLog() {
		String text = console.getText();
		String fileName = "Log-[" + Utils.convertirGregorianFecha(new GregorianCalendar()) + "].log";
		PrintWriter output = Utils.writeFile(new File(fileName));
		output.write(text);
		output.close();
		ConsolePanel.print("Saving log " + fileName);
	}
	
	public static void print(Object object) {
		console.append(object.toString() + "\n");
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
