package co.edu.javeriana.redes.interfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import co.edu.javeriana.redes.negocio.Device;
import net.miginfocom.swing.MigLayout;

public class DeviceMainPanel extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private static long COUNT = 0;
	private JPanel panelDevice;

	public DeviceMainPanel() {
		setWheelScrollingEnabled(false);
		panelDevice = new JPanel();
		panelDevice.setBackground(new Color(248, 248, 255));
		panelDevice.setLayout(new MigLayout("", "[grow]", "[]"));
		setViewportView(panelDevice);
		setMinimumSize(new Dimension(710, 200));
	}

	public void addDevice(Device device) {
		DevicePanel panel = new DevicePanel(device);
		panelDevice.add(panel, "cell 0 " + COUNT++ + ",grow");
	}

	public boolean containsDevice(Device device) {
		for(Component c : this.panelDevice.getComponents()) {
			if(c instanceof DevicePanel) {
				DevicePanel devicePanel = (DevicePanel) c;
				if(Arrays.equals(devicePanel.getDevice().getMac(), device.getMac()) && Arrays.equals(devicePanel.getDevice().getIp(), device.getIp())) {
					return true;
				}
			}
		}
		return false;
	}

	public void removeDevice(Device device) {
		for(Component c : this.panelDevice.getComponents()) {
			if(c instanceof DevicePanel) {
				DevicePanel devicePanel = (DevicePanel) c;
				if(Arrays.equals(devicePanel.getDevice().getMac(), device.getMac()) && Arrays.equals(devicePanel.getDevice().getIp(), device.getIp())) {
					device.setDevicePanel(null);
					panelDevice.remove(devicePanel);
					panelDevice.repaint();
				}
			}
		}
	}
}
