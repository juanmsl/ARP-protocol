package co.edu.javeriana.redes.negocio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import co.edu.javeriana.redes.interfaz.ConsolePanel;
import co.edu.javeriana.redes.interfaz.MainAPP;
import co.edu.javeriana.redes.negocio.Device.State;
import co.edu.javeriana.redes.utilities.Utils;

public class DeviceManager {
	private Map<String, Device> devices;
	private MainAPP mainAPP;
	private Thread devicesThread;
	private Thread automaticARP;
	private boolean automaticARPRun;
	
	@SuppressWarnings("unchecked")
	public DeviceManager(MainAPP mainAPP) {
		this.devices = (Map<String, Device>) Utils.readObject(Utils.fileName);
		if(this.devices == null) {
			this.devices = new HashMap<String, Device>();
			ConsolePanel.print("The file " + Utils.fileName + " doesn't exist, new system created!");
		}
		this.mainAPP = mainAPP;
		this.automaticARP = null;
		this.devicesThread = null;
	}
	
	public void addDevice(byte[] mac, byte[] ip) {
		String key = Arrays.toString(mac) + Arrays.toString(ip);
		if(!this.devices.containsKey(key)) {
			Device device = new Device(mac, ip, State.ACTIVO, this);
			this.devices.put(key, device);
			this.mainAPP.addDevice(device);
			this.saveDevices();
		} else {
			Device device = this.devices.get(key);
			if(!this.mainAPP.containsDevice(device)) {
				device.setDeviceManager(this);
				device.resetCounts();
				device.setEstado(State.ACTIVO);
				this.mainAPP.addDevice(device);
			}
		}
	}
	
	public void checkDevice(String key) {
		int msnToPendiente = mainAPP.getControlPanel().getMsnToPendiente();
		int msnToInactivo = mainAPP.getControlPanel().getMsnToInactivo();
		int tmToOut = mainAPP.getControlPanel().getTmToOut();
		
		if(this.devices.containsKey(key)) {
			Device device = this.devices.get(key);
			
			if(ARP.sendARPTo(device)) {
				device.setEstado(State.ACTIVO);
				device.setRequestMSN(device.getRequestMSN() + 1);
				device.setCountPendiente(0);
				device.setCountInactivo(0);
				device.setCambio("" + device.getCountPendiente() + "/" + msnToPendiente);
			} else {
				int cambio = 0;
				device.setFailedMSN(device.getFailedMSN() + 1);
				if(device.getEstado() == State.ACTIVO) {
					device.setCountPendiente(device.getCountPendiente() + 1);
					ConsolePanel.print("\t" + Utils.byteArrayToDecString(device.getIp(), ".") + ": " + device.getCountPendiente() + "/" + msnToPendiente + " to change to 'Pendiente']");
					cambio = 1;
					if(device.getCountPendiente() >= msnToPendiente) {
						device.setCountPendiente(0);
						device.setEstado(State.PENDIENTE);
						ConsolePanel.print("\t" + Utils.byteArrayToDecString(device.getIp(), ".") + ": changing to 'Pendiente']");
						cambio = 2;
					}
				} else if(device.getEstado() == State.PENDIENTE) {
					device.setCountInactivo(device.getCountInactivo() + 1);
					ConsolePanel.print("\t" + Utils.byteArrayToDecString(device.getIp(), ".") + ": " + device.getCountInactivo() + "/" + msnToInactivo + " to change to 'Inactivo']");
					cambio = 2;
					if(device.getCountInactivo() >= msnToInactivo) {
						device.setCountInactivo(0);
						device.setEstado(State.INACTIVO);
						ConsolePanel.print("\t" + Utils.byteArrayToDecString(device.getIp(), ".") + ": changing to 'Inactivo']");
						cambio = 3;
						device.countToRemoveDevice(tmToOut);
					}
				}
				if(cambio == 1) {
					device.setCambio("" + device.getCountPendiente() + "/" + msnToPendiente);
				} else if(cambio == 2) {
					device.setCambio("" + device.getCountInactivo() + "/" + msnToInactivo);
				} else if(cambio == 3) {
					device.setCambio("" + tmToOut + "s");
				}
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException evento) {
				ConsolePanel.print("Checking interrumpido");
			}
		}
	}
	
	public void removeDevice(Device device) {
		mainAPP.removeDevice(device);
	}

	public void sendARP() {
		devicesThread = new Thread(new Runnable() {
			public void run() {
				ARP.getDevicesTo(DeviceManager.this);
				mainAPP.enableButton();
			}
		});
		devicesThread.start();
	}

	public int getTimeout() {
		return this.mainAPP.getTimeout();
	}

	public void sendAutomaticARP() {
		this.automaticARPRun = true;
		this.automaticARP = new Thread(new Runnable() {
			public void run() {
				try {
					ARP.getDevicesTo(DeviceManager.this);
					Thread.sleep(10000);
					if(automaticARPRun) {
						sendAutomaticARP();
					}
				} catch (InterruptedException evento) {
					ConsolePanel.print("Busqueda interrumpida");
				} finally {
					mainAPP.enableButton();
				}
			}
		});
		this.automaticARP.start();
	}

	public void stopAutomaticARP() {
		if(automaticARP != null) {
			ARP.CONTINUAR = false;
			this.automaticARPRun = false;
			automaticARP.interrupt();
		}
	}
	
	public void saveDevices() {
		ConsolePanel.print("Saving devices...");
		Utils.saveObject(Utils.fileName, this.devices);
		ConsolePanel.print("Devices saved");
	}
}
