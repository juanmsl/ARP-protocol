package co.edu.javeriana.redes.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import co.edu.javeriana.redes.negocio.Device;
import co.edu.javeriana.redes.negocio.Device.State;
import co.edu.javeriana.redes.negocio.Device.Type;
import co.edu.javeriana.redes.utilities.Utils;
import net.miginfocom.swing.MigLayout;

public class DevicePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Device device;
	private JLabel mac;
	private JLabel ip;
	private JLabel estado;
	private JLabel icon;
	private JComboBox<Type> comboBox;
	private JLabel msn;
	private JLabel respondidos;
	private JLabel perdidos;
	private Component horizontalStrut;
	private JLabel lblCa;
	private JLabel cambio;
	private JLabel lblChequeo;
	private JRadioButton rdbtnManual;
	private JRadioButton rdbtnAutomatico;
	private JButton btnEnviarArp;
	private Component horizontalStrut_1;

	public DevicePanel(Device device) {
		setBorder(new LineBorder(new Color(64, 64, 64), 3, true));
		this.device = device;
		this.device.setDevicePanel(this);
		setLayout(new BorderLayout(0, 0));
		
		ButtonGroup bg = new ButtonGroup();
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		add(panel, BorderLayout.CENTER);		
		panel.setLayout(new MigLayout("", "[][][][][grow][][]", "[grow][grow][grow][grow]"));
		
		icon = new JLabel("");
		icon.setBorder(new EmptyBorder(8, 8, 8, 8));
		panel.add(icon, "cell 0 0 1 4,alignx center,aligny center");
		
		JLabel lblDispositivo = new JLabel("Dispositivo");
		lblDispositivo.setHorizontalAlignment(SwingConstants.LEFT);
		lblDispositivo.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblDispositivo, "cell 1 0,growx,aligny center");
		
		int index = 0;
		for(Type type: Type.values()) {
			if(type == device.getTipo()) {
				break;
			}
			index++;
		}
		
		comboBox = new JComboBox<Type>(new DefaultComboBoxModel<Type>(Type.values()));
		comboBox.setBorder(new EmptyBorder(0, 5, 0, 0));
		comboBox.setFocusTraversalKeysEnabled(false);
		comboBox.setFocusable(false);
		comboBox.setOpaque(false);
		comboBox.setSelectedIndex(index);
		comboBox.setMaximumRowCount(10);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Type type = (Type) comboBox.getSelectedItem();
				device.setTipo(type);
				drawIcon();
			}
		});
		panel.add(comboBox, "cell 2 0,growx,aligny center");
		
		JLabel lblMensajesEnviados = new JLabel("Mensajes enviados:");
		lblMensajesEnviados.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblMensajesEnviados, "flowx,cell 4 0");
		
		lblChequeo = new JLabel("Chequeo");
		lblChequeo.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblChequeo, "cell 6 0");
		
		JLabel lblDireccinIp = new JLabel("Direcci\u00F3n IP");
		lblDireccinIp.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDireccinIp.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblDireccinIp, "cell 1 1,growx,aligny center");
		
		ip = new JLabel("");
		ip.setBorder(new EmptyBorder(0, 5, 0, 0));
		ip.setHorizontalAlignment(SwingConstants.LEFT);
		ip.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(ip, "cell 2 1,growx,aligny center");
		
		horizontalStrut = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut, "cell 3 1");
		
		JLabel lblMensajesRespondidos = new JLabel("Mensajes respondidos:");
		lblMensajesRespondidos.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblMensajesRespondidos, "flowx,cell 4 1");
		
		horizontalStrut_1 = Box.createHorizontalStrut(20);
		panel.add(horizontalStrut_1, "cell 5 1");
		
		rdbtnManual = new JRadioButton("Manual");
		rdbtnManual.setEnabled(false);
		rdbtnManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				device.stopAutomaticChecking();
			}
		});
		rdbtnManual.setSelected(true);
		rdbtnManual.setFocusPainted(false);
		rdbtnManual.setFocusable(false);
		bg.add(rdbtnManual);
		rdbtnManual.setRolloverEnabled(false);
		rdbtnManual.setRequestFocusEnabled(false);
		panel.add(rdbtnManual, "cell 6 1");
		
		JLabel lblDireccinMac = new JLabel("Direcci\u00F3n MAC");
		lblDireccinMac.setHorizontalAlignment(SwingConstants.LEFT);
		lblDireccinMac.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblDireccinMac, "cell 1 2,growx,aligny center");
		
		mac = new JLabel("");
		mac.setBorder(new EmptyBorder(0, 5, 0, 0));
		mac.setHorizontalAlignment(SwingConstants.LEFT);
		mac.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(mac, "cell 2 2,growx,aligny center");
		
		JLabel lblMensajesPerdidos = new JLabel("Mensajes perdidos:");
		lblMensajesPerdidos.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblMensajesPerdidos, "flowx,cell 4 2");
		
		rdbtnAutomatico = new JRadioButton("Automatico");
		rdbtnAutomatico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnEnviarArp.setVisible(false);
				rdbtnAutomatico.setEnabled(false);
				rdbtnManual.setEnabled(true);
				device.startChecking();
			}
		});
		rdbtnAutomatico.setFocusPainted(false);
		rdbtnAutomatico.setFocusable(false);
		bg.add(rdbtnAutomatico);
		rdbtnAutomatico.setRolloverEnabled(false);
		rdbtnAutomatico.setRequestFocusEnabled(false);
		panel.add(rdbtnAutomatico, "cell 6 2");
		
		JLabel lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEstado.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblEstado, "cell 1 3,growx,aligny center");
		
		estado = new JLabel("");
		estado.setBorder(new EmptyBorder(0, 5, 0, 0));
		estado.setHorizontalAlignment(SwingConstants.LEFT);
		estado.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(estado, "cell 2 3,growx,aligny center");
		
		msn = new JLabel("0");
		msn.setBorder(new EmptyBorder(0, 5, 0, 0));
		msn.setForeground(new Color(0, 0, 0));
		msn.setHorizontalAlignment(SwingConstants.RIGHT);
		msn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(msn, "cell 4 0,growx");
		
		respondidos = new JLabel("0");
		respondidos.setBorder(new EmptyBorder(0, 5, 0, 0));
		respondidos.setForeground(new Color(0, 0, 0));
		respondidos.setHorizontalAlignment(SwingConstants.RIGHT);
		respondidos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(respondidos, "cell 4 1,growx");
		
		perdidos = new JLabel("0");
		perdidos.setBorder(new EmptyBorder(0, 5, 0, 0));
		perdidos.setForeground(new Color(0, 0, 0));
		perdidos.setHorizontalAlignment(SwingConstants.RIGHT);
		perdidos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel.add(perdidos, "cell 4 2,growx");
		
		lblCa = new JLabel("Cambio de estado:");
		lblCa.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblCa, "flowx,cell 4 3");
		
		cambio = new JLabel("");
		cambio.setHorizontalAlignment(SwingConstants.RIGHT);
		cambio.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cambio.setBorder(new EmptyBorder(0, 5, 0, 0));
		panel.add(cambio, "cell 4 3,growx");
		
		btnEnviarArp = new JButton("Enviar ARP");
		btnEnviarArp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				device.check();
			}
		});
		btnEnviarArp.setFocusable(false);
		btnEnviarArp.setFocusTraversalKeysEnabled(false);
		btnEnviarArp.setFocusPainted(false);
		btnEnviarArp.setIcon(new ImageIcon(new ImageIcon(DevicePanel.class.getResource("../resources/send.png")).getImage()));
		btnEnviarArp.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnEnviarArp, "cell 6 3");
		
		drawCambio();
		drawRespondidos();
		drawPerdidos();
		drawIcon();
		drawMac();
		drawIp();
		drawEstado();
	}
	
	public void drawCambio() {
		cambio.setText(device.getCambio());
	}
	
	public void drawRespondidos() {
		msn.setText("" + device.getCountMSN());
		respondidos.setText("" + device.getRequestMSN());
	}
	
	public void drawPerdidos() {
		msn.setText("" + device.getCountMSN());
		perdidos.setText("" + device.getFailedMSN());
	}
	
	public void drawIcon() {
		icon.setIcon(new ImageIcon(new ImageIcon(Device.class.getResource("../resources/" + this.device.getIcon().toLowerCase() + ".png")).getImage()));
	}
	
	public void drawMac() {
		mac.setText(Utils.byteArrayToHexString(device.getMac(), " : "));
	}
	
	public void drawIp() {
		ip.setText(Utils.byteArrayToDecString(device.getIp(), " . "));
	}
	
	public void drawEstado() {
		estado.setText(device.getEstado().getName());
		estado.setIcon(new ImageIcon(new ImageIcon(Device.class.getResource("../resources/" + ((device.getEstado() == State.ACTIVO) ? "active" : ((device.getEstado() == State.PENDIENTE) ? "pending" : "inactive" ) ) + ".png")).getImage()));
	}

	public Device getDevice() {
		return this.device;
	}

	public void enableButton() {
		rdbtnAutomatico.setEnabled(true);
		rdbtnManual.setEnabled(false);
		btnEnviarArp.setVisible(true);
	}
}
