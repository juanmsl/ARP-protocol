package co.edu.javeriana.redes.interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import co.edu.javeriana.redes.negocio.ARP;
import net.miginfocom.swing.MigLayout;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton btnEnviarArp;
	private JSpinner t_timeout;
	private JTextField direccionIP;
	private JTextField mascara;
	private JSpinner msnToPendiente;
	private JSpinner msnToInactivo;
	private JSpinner tmToOut;
	private JRadioButton rdbtnAutomatica;
	private JRadioButton rdbtnManual;
	private JButton btnDetener;
	private JTextField red;
	private JTextField host;
	
	public ControlPanel(MainAPP mainAPP) {
		ButtonGroup bg = new ButtonGroup();
		setBackground(Color.DARK_GRAY);
		setLayout(new MigLayout("", "[grow][][][][][grow]", "[][][][][][][]"));
		
		JLabel lblProtocoloArp = new JLabel("Protocolo ARP");
		lblProtocoloArp.setForeground(SystemColor.inactiveCaptionBorder);
		lblProtocoloArp.setHorizontalAlignment(SwingConstants.CENTER);
		lblProtocoloArp.setFont(new Font("Tahoma", Font.BOLD, 20));
		add(lblProtocoloArp, "cell 1 0,growx");
		
		JLabel lblTimeoutParaQue = new JLabel("Timeout para que la maquina responda ");
		lblTimeoutParaQue.setForeground(SystemColor.inactiveCaptionBorder);
		lblTimeoutParaQue.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblTimeoutParaQue, "flowx,cell 3 0 2 1");
		
		JLabel lblDireccinIp = new JLabel("Direcci\u00F3n IP:");
		lblDireccinIp.setForeground(SystemColor.inactiveCaptionBorder);
		lblDireccinIp.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblDireccinIp, "flowx,cell 1 1");
		
		JLabel lblDireccinDeRed = new JLabel("Direcci\u00F3n de red:");
		lblDireccinDeRed.setForeground(Color.WHITE);
		lblDireccinDeRed.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblDireccinDeRed, "flowx,cell 1 2");
		
		Component rigidArea = Box.createRigidArea(new Dimension(40, 20));
		add(rigidArea, "cell 2 3");
		
		JLabel lblElDispositivoPasara = new JLabel("El dispositivo pasara al estado pendiente si no responde");
		lblElDispositivoPasara.setForeground(SystemColor.inactiveCaptionBorder);
		lblElDispositivoPasara.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(lblElDispositivoPasara, "flowx,cell 1 4,alignx left");
		
		JLabel lblIdentificarDispositivosDe = new JLabel("Identificar dispositivos de la red de manera");
		lblIdentificarDispositivosDe.setForeground(SystemColor.inactiveCaptionBorder);
		lblIdentificarDispositivosDe.setFont(new Font("Tahoma", Font.BOLD, 12));
		add(lblIdentificarDispositivosDe, "cell 3 4 2 1,alignx center");
		
		JLabel lblDespuesPasaraAl = new JLabel("Despues pasara al estado inactivo si no responde");
		lblDespuesPasaraAl.setForeground(SystemColor.inactiveCaptionBorder);
		lblDespuesPasaraAl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(lblDespuesPasaraAl, "flowx,cell 1 5,alignx left");
		
		rdbtnManual = new JRadioButton("Manual");
		rdbtnManual.setEnabled(false);
		rdbtnManual.setFocusable(false);
		rdbtnManual.setFocusPainted(false);
		rdbtnManual.setRolloverEnabled(false);
		rdbtnManual.setRequestFocusEnabled(false);
		rdbtnManual.setForeground(SystemColor.inactiveCaptionBorder);
		rdbtnManual.setToolTipText("Se enviara la rafaga de mensajes ARP cada vez que el usuario desee enviarla");
		rdbtnManual.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnManual.setOpaque(false);
		rdbtnManual.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				mainAPP.stopAutomaticARP();
			}
		});
		
		rdbtnAutomatica = new JRadioButton("Automatica");
		rdbtnAutomatica.setFocusable(false);
		rdbtnAutomatica.setFocusPainted(false);
		rdbtnAutomatica.setRolloverEnabled(false);
		rdbtnAutomatica.setRequestFocusEnabled(false);
		rdbtnAutomatica.setForeground(SystemColor.inactiveCaptionBorder);
		rdbtnAutomatica.setToolTipText("Se enviara la rafaga de mensajes ARP automatoicamente cada 10 segundos");
		rdbtnAutomatica.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnAutomatica.setOpaque(false);
		rdbtnAutomatica.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnAutomatica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evento) {
				ARP.CONTINUAR = true;
				mainAPP.sendAutomaticARP();
				rdbtnManual.setEnabled(true);
				rdbtnAutomatica.setEnabled(false);
				btnEnviarArp.setVisible(false);
			}
		});
		bg.add(rdbtnAutomatica);
		add(rdbtnAutomatica, "cell 3 5,growx");
		rdbtnManual.setSelected(true);
		bg.add(rdbtnManual);
		add(rdbtnManual, "cell 4 5,growx");
		
		JLabel lblElDispositivoSe = new JLabel("El dispositivo se eliminara si dura inactivo");
		lblElDispositivoSe.setForeground(SystemColor.inactiveCaptionBorder);
		lblElDispositivoSe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(lblElDispositivoSe, "flowx,cell 1 6,alignx left");
		
		msnToPendiente = new JSpinner();
		msnToPendiente.setForeground(SystemColor.inactiveCaptionBorder);
		msnToPendiente.setFont(new Font("Tahoma", Font.PLAIN, 12));
		msnToPendiente.setBorder(null);
		msnToPendiente.setModel(new SpinnerNumberModel(3, 1, 100, 1));
		add(msnToPendiente, "cell 1 4,alignx left");
		
		JLabel lblMensajesConsecutivos = new JLabel("mensajes consecutivos");
		lblMensajesConsecutivos.setForeground(SystemColor.inactiveCaptionBorder);
		lblMensajesConsecutivos.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(lblMensajesConsecutivos, "cell 1 4");
		
		msnToInactivo = new JSpinner();
		msnToInactivo.setForeground(SystemColor.inactiveCaptionBorder);
		msnToInactivo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		msnToInactivo.setBorder(null);
		msnToInactivo.setModel(new SpinnerNumberModel(2, 1, 100, 1));
		add(msnToInactivo, "cell 1 5,alignx left");
		
		JLabel lblMensajesConsecutivos_1 = new JLabel("mensajes consecutivos");
		lblMensajesConsecutivos_1.setForeground(SystemColor.inactiveCaptionBorder);
		lblMensajesConsecutivos_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(lblMensajesConsecutivos_1, "cell 1 5");
		
		tmToOut = new JSpinner();
		tmToOut.setForeground(SystemColor.inactiveCaptionBorder);
		tmToOut.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tmToOut.setBorder(null);
		tmToOut.setModel(new SpinnerNumberModel(15, 1, 60, 1));
		add(tmToOut, "cell 1 6,alignx left");
		
		JLabel lblSegundos = new JLabel("segundos seguidos");
		lblSegundos.setForeground(SystemColor.inactiveCaptionBorder);
		lblSegundos.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(lblSegundos, "cell 1 6");
		
		direccionIP = new JTextField(ARP.IP.getHostAddress());
		direccionIP.setForeground(SystemColor.inactiveCaptionBorder);
		direccionIP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		direccionIP.setOpaque(false);
		direccionIP.setEditable(false);
		direccionIP.setBorder(new EmptyBorder(0, 5, 0, 0));
		add(direccionIP, "cell 1 1,growx");
		direccionIP.setColumns(8);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(50);
		add(horizontalStrut_1, "cell 1 1");
		
		JLabel lblMascaraDeRed = new JLabel("Mascara de red:");
		lblMascaraDeRed.setForeground(SystemColor.inactiveCaptionBorder);
		lblMascaraDeRed.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblMascaraDeRed, "cell 1 1");
		
		mascara = new JTextField(ARP.MASK.getHostAddress() + " /" + ARP.MASK_SIZE);
		mascara.setForeground(SystemColor.inactiveCaptionBorder);
		mascara.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mascara.setOpaque(false);
		mascara.setEditable(false);
		mascara.setBorder(new EmptyBorder(0, 5, 0, 0));
		add(mascara, "cell 1 1,growx");
		mascara.setColumns(10);
		
		t_timeout = new JSpinner();
		t_timeout.setForeground(SystemColor.inactiveCaptionBorder);
		t_timeout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		t_timeout.setBorder(null);
		t_timeout.setModel(new SpinnerNumberModel(1500, 100, 10000, 100));
		add(t_timeout, "flowx,cell 4 1");
		
		JLabel lblMs = new JLabel("ms");
		lblMs.setForeground(SystemColor.inactiveCaptionBorder);
		lblMs.setFont(new Font("Tahoma", Font.PLAIN, 12));
		add(lblMs, "cell 4 1,growx");
		
		btnDetener = new JButton("Detener");
		btnDetener.setFocusable(false);
		btnDetener.setFocusTraversalKeysEnabled(false);
		btnDetener.setFocusPainted(false);
		btnDetener.setRolloverEnabled(false);
		btnDetener.setRequestFocusEnabled(false);
		btnDetener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ARP.CONTINUAR = false;
				btnDetener.setEnabled(false);
			}
		});
		btnDetener.setVisible(false);
		
		btnEnviarArp = new JButton("Enviar ARP");
		btnEnviarArp.setFocusable(false);
		btnEnviarArp.setFocusTraversalKeysEnabled(false);
		btnEnviarArp.setFocusPainted(false);
		btnEnviarArp.setRolloverEnabled(false);
		btnEnviarArp.setRequestFocusEnabled(false);
		btnEnviarArp.setIcon(new ImageIcon(new ImageIcon(ConsolePanel.class.getResource("../resources/send.png")).getImage()));
		btnEnviarArp.setForeground(Color.DARK_GRAY);
		btnEnviarArp.setToolTipText("Se enviara una rafaga de mensajes ARP a las IP de su red de area local y se guardaran los dispositivos que respondan con su direcci\u00F3n MAC");
		btnEnviarArp.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnEnviarArp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ARP.CONTINUAR = true;
				rdbtnAutomatica.setEnabled(false);
				rdbtnManual.setEnabled(false);
				mainAPP.sendARP();
				btnDetener.setVisible(true);
				btnDetener.setEnabled(true);
				btnEnviarArp.setVisible(false);
			}
		});
		add(btnEnviarArp, "cell 3 6,growx");
		btnDetener.setIcon(new ImageIcon(new ImageIcon(ConsolePanel.class.getResource("../resources/stop.png")).getImage()));
		btnDetener.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnDetener.setForeground(Color.DARK_GRAY);
		add(btnDetener, "cell 4 6,growx");
		
		red = new JTextField(ARP.SUBNET.getHostAddress());
		red.setBorder(new EmptyBorder(0, 5, 0, 0));
		red.setEditable(false);
		red.setForeground(Color.WHITE);
		red.setOpaque(false);
		red.setFont(new Font("Tahoma", Font.PLAIN, 14));
		add(red, "cell 1 2");
		red.setColumns(8);
		
		Component horizontalStrut = Box.createHorizontalStrut(23);
		add(horizontalStrut, "cell 1 2");
		
		JLabel lblHost = new JLabel("Maquinas en Host:");
		lblHost.setForeground(Color.WHITE);
		lblHost.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblHost, "cell 1 2");
		
		host = new JTextField("" + ARP.HOST);
		host.setEditable(false);
		host.setForeground(Color.WHITE);
		host.setFont(new Font("Tahoma", Font.PLAIN, 14));
		host.setOpaque(false);
		host.setBorder(new EmptyBorder(0, 5, 0, 0));
		add(host, "cell 1 2");
		host.setColumns(5);
	}

	public int getMsnToPendiente() {
		return (int) this.msnToPendiente.getValue();
	}
	
	public int getMsnToInactivo() {
		return (int) this.msnToInactivo.getValue();
	}
	
	public int getTmToOut() {
		return (int) this.tmToOut.getValue();
	}
	
	public int getTimeout() {
		return (int) this.t_timeout.getValue();
	}

	public void enableButton() {
		btnDetener.setVisible(false);
		btnEnviarArp.setVisible(true);
		rdbtnAutomatica.setEnabled(true);
		rdbtnManual.setEnabled(false);
	}
}
