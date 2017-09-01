package co.edu.javeriana.redes.interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import co.edu.javeriana.redes.negocio.ARP;
import co.edu.javeriana.redes.negocio.Device;
import co.edu.javeriana.redes.negocio.DeviceManager;
import net.miginfocom.swing.MigLayout;

public class MainAPP extends JFrame {
	private static final long serialVersionUID = 1L;
	private JSplitPane contentPane;
	private ControlPanel controlPanel;
	private DeviceManager deviceManager;
	private DeviceMainPanel deviceMainPanel;
	
	public static void main(String[] args) {
		try {
			InetAddress[] addrs = Inet4Address.getAllByName(Inet4Address.getLocalHost().getHostAddress());
			for (InetAddress inetAddress : addrs) {
				System.out.println(inetAddress.getHostAddress());
			}
			if(ARP.ENABLE) {
				MainAPP dialog = new MainAPP();
				dialog.setVisible(true);
			} else {
				System.out.println("Something goes wrong connectig to network...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MainAPP() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception evento) {
			evento.printStackTrace();
		}
		contentPane = new JSplitPane();
		contentPane.setEnabled(false);
		contentPane.setOneTouchExpandable(true);
		contentPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.setDividerSize(10);
		JButton buttonUp = (JButton) ((BasicSplitPaneUI) contentPane.getUI()).getDivider().getComponent(0);
		JButton buttonDown = (JButton) ((BasicSplitPaneUI) contentPane.getUI()).getDivider().getComponent(1);
		buttonDown.setVisible(false);
		buttonDown.setToolTipText("Mostrar");
		buttonUp.setToolTipText("Ocultar");
		buttonUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonUp.setVisible(false);
				buttonDown.setVisible(true);
			}
		});
		buttonDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonUp.setVisible(true);
				buttonDown.setVisible(false);
			}
		});
		
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Protocolo ARP");
		setFont(new Font("Tahoma", Font.PLAIN, 12));
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(1300, 700));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		controlPanel = new ControlPanel(this);
		contentPane.setLeftComponent(controlPanel);
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		contentPane.setRightComponent(panel);
		panel.setLayout(new MigLayout("", "[][grow]", "[grow]"));
		deviceMainPanel = new DeviceMainPanel();
		panel.add(deviceMainPanel, "cell 0 0,grow");
		ConsolePanel consolePanel = new ConsolePanel();
		panel.add(consolePanel, "cell 1 0,grow");
		deviceManager = new DeviceManager(this);
	}
	
	public void addDevice(Device device) {
		this.deviceMainPanel.addDevice(device);
	}
	
	public void sendARP() {
		this.deviceManager.sendARP();
	}

	public void sendAutomaticARP() {
		this.deviceManager.sendAutomaticARP();
	}

	public void stopAutomaticARP() {
		this.deviceManager.stopAutomaticARP();
	}

	public ControlPanel getControlPanel() {
		return this.controlPanel;
	}

	public boolean containsDevice(Device device) {
		return this.deviceMainPanel.containsDevice(device);
	}

	public void removeDevice(Device device) {
		this.deviceMainPanel.removeDevice(device);
	}

	public void enableButton() {
		this.controlPanel.enableButton();
	}

	public int getTimeout() {
		return this.controlPanel.getTimeout();
	}
}
