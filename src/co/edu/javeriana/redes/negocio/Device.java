package co.edu.javeriana.redes.negocio;

import java.io.Serializable;
import java.util.Arrays;

import co.edu.javeriana.redes.interfaz.ConsolePanel;
import co.edu.javeriana.redes.interfaz.DevicePanel;

/**Esta es la clase del objeto Device.java
 */
public class Device implements Serializable {
	/** serialVersionUID serialVersionUID del objeto Device.java
	 */
	private static final long serialVersionUID = 1L;
	/**Este es el enumerado de los tipos del objeto Device.java
	 */
	public enum Type {
		Desconocido, Computador, Consola, IPhone, Portatil, Celular, Tablet, Impresora, Servidor, Fax, Reloj, Router, Television;
		
		public String getName() {
			return this.name();
		}
	}
	/**Este es el enumerado de los estados del objeto Device.java
	 */
	public enum State {
		ACTIVO, PENDIENTE, INACTIVO;
		
		public String getName() {
			return this.name();
		}
	}
	/** mac mac del objeto Device.java
	 */
	private byte mac[];
	/** ip ip del objeto Device.java
	 */
	private byte ip[];
	/** tipo tipo del objeto Device.java
	 */
	private Type tipo;
	/** estado estado del objeto Device.java
	 */
	private transient State estado;
	/** icon icon del objeto Device.java
	 */
	private String icon;
	/** devicePanel devicePanel del objeto Device.java
	 */
	private transient DevicePanel devicePanel;
	/** deviceManager deviceManager del objeto Device.java
	 */
	private transient DeviceManager deviceManager;
	/** countMSN countMSN del objeto Device.java
	 */
	private transient long countMSN;
	/** failedMSN failedMSN del objeto Device.java
	 */
	private transient long failedMSN;
	/** requestMSN requestMSN del objeto Device.java
	 */
	private transient long requestMSN;
	/** countPendiente countPendiente del objeto Device.java
	 */
	private transient long countPendiente;
	/** countInactivo countInactivo del objeto Device.java
	 */
	private transient long countInactivo;
	/** cambio cambio del objeto Device.java
	 */
	private transient String cambio;
	/** checking checking del objeto Device.java
	 */
	private transient Thread automaticChecking;
	/** waitToRemove waitToRemove del objeto Device.java
	 */
	private transient Thread waitToRemove;
	/** isChecking isChecking del objeto Device.java
	 */
	private transient boolean isChecking;
	
	/**Constructor de la clase Device.java
	 * @param mac Dirección mac del dispositivo
	 * @param ip Dirección ip del dispositivo
	 * @param estado Estado del dispositivo
	 * @throws Exception Error sobre la longitud de bytes sobre la dirección mac o la dirección ip
	 */
	public Device(byte[] mac, byte[] ip, State estado, DeviceManager deviceManager) {
		this.mac = mac;
		this.ip = ip;
		this.tipo = Type.Desconocido;
		this.estado = estado;
		this.devicePanel = null;
		this.resetCounts();
		this.deviceManager = deviceManager;
		this.icon = this.tipo.getName();
		this.automaticChecking = null;
		this.isChecking = false;
		this.waitToRemove = null;
	}

	/**Este metodo retorna el valor del atributo mac
	 * @return El valor de mac
	 */
	public byte[] getMac() {
		return this.mac;
	}

	/**Este metodo modifica el valor del atributo mac
	 * @param mac mac a cambiar
	 */
	public void setMac(byte[] mac) {
		this.mac = mac;
		if(this.devicePanel != null) {
			this.devicePanel.drawMac();
		}
	}

	/**Este metodo retorna el valor del atributo ip
	 * @return El valor de ip
	 */
	public byte[] getIp() {
		return this.ip;
	}

	/**Este metodo modifica el valor del atributo ip
	 * @param ip ip a cambiar
	 */
	public void setIp(byte[] ip) {
		this.ip = ip;
		if(this.devicePanel != null) {
			this.devicePanel.drawIp();
		}
	}

	/**Este metodo retorna el valor del atributo tipo
	 * @return El valor de tipo
	 */
	public Type getTipo() {
		return this.tipo;
	}

	/**Este metodo modifica el valor del atributo tipo
	 * @param tipo tipo a cambiar
	 */
	public void setTipo(Type tipo) {
		this.tipo = tipo;
		this.icon = this.tipo.getName();
		this.deviceManager.saveDevices();
		if(this.devicePanel != null) {
			this.devicePanel.drawIcon();
		}
	}

	/**Este metodo retorna el valor del atributo estado
	 * @return El valor de estado
	 */
	public State getEstado() {
		return this.estado;
	}

	/**Este metodo modifica el valor del atributo estado
	 * @param estado estado a cambiar
	 */
	public void setEstado(State estado) {
		this.estado = estado;
		if(this.devicePanel != null) {
			this.devicePanel.drawEstado();
		}
	}

	/**Este metodo retorna el valor del atributo icon
	 * @return El valor de icon
	 */
	public String getIcon() {
		return this.icon;
	}

	/**Este metodo retorna el valor del atributo devicePanel
	 * @return El valor de devicePanel
	 */
	public DevicePanel getDevicePanel() {
		return this.devicePanel;
	}

	/**Este metodo modifica el valor del atributo devicePanel
	 * @param devicePanel devicePanel a cambiar
	 */
	public void setDevicePanel(DevicePanel devicePanel) {
		this.devicePanel = devicePanel;
	}

	/**Este metodo retorna el valor del atributo countMSN
	 * @return El valor de countMSN
	 */
	public long getCountMSN() {
		return this.countMSN;
	}

	/**Este metodo retorna el valor del atributo failedMSN
	 * @return El valor de failedMSN
	 */
	public long getFailedMSN() {
		return this.failedMSN;
	}

	/**Este metodo modifica el valor del atributo failedMSN
	 * @param failedMSN failedMSN a cambiar
	 */
	public void setFailedMSN(long failedMSN) {
		this.failedMSN = failedMSN;
		this.countMSN++;
		if(this.devicePanel != null) {
			this.devicePanel.drawPerdidos();
		}
	}

	/**Este metodo retorna el valor del atributo requestMSN
	 * @return El valor de requestMSN
	 */
	public long getRequestMSN() {
		return this.requestMSN;
	}

	/**Este metodo modifica el valor del atributo requestMSN
	 * @param requestMSN requestMSN a cambiar
	 */
	public void setRequestMSN(long requestMSN) {
		this.requestMSN = requestMSN;
		this.countMSN++;
		if(this.devicePanel != null) {
			this.devicePanel.drawRespondidos();
		}
	}

	/**Este metodo modifica el valor del atributo deviceManager
	 * @param deviceManager deviceManager a cambiar
	 */
	public void setDeviceManager(DeviceManager deviceManager) {
		this.deviceManager = deviceManager;
	}

	/**Este metodo resetea la cuenta de los mensajes
	 */
	public void resetCounts() {
		this.countMSN = 1;
		this.failedMSN = 0;
		this.requestMSN = 1;
		this.countInactivo = 0;
		this.countPendiente = 0;
		this.cambio = "";
	}

	/**Este metodo retorna el valor del atributo cambio
	 * @return El valor de cambio
	 */
	public String getCambio() {
		return this.cambio;
	}

	/**Este metodo modifica el valor del atributo cambio
	 * @param cambio cambio a cambiar
	 */
	public void setCambio(String cambio) {
		this.cambio = cambio;
		if(this.devicePanel != null) {
			this.devicePanel.drawCambio();
		}
	}
	
	/**Este metodo retorna el valor del atributo countPendiente
	 * @return El valor de countPendiente
	 */
	public long getCountPendiente() {
		return this.countPendiente;
	}

	/**Este metodo modifica el valor del atributo countPendiente
	 * @param countPendiente countPendiente a cambiar
	 */
	public void setCountPendiente(long countPendiente) {
		this.countPendiente = countPendiente;
	}

	/**Este metodo retorna el valor del atributo countInactivo
	 * @return El valor de countInactivo
	 */
	public long getCountInactivo() {
		return this.countInactivo;
	}

	/**Este metodo modifica el valor del atributo countInactivo
	 * @param countInactivo countInactivo a cambiar
	 */
	public void setCountInactivo(long countInactivo) {
		this.countInactivo = countInactivo;
	}

	/**Este metodo retorna el valor del atributo isChecking
	 * @return El valor de isChecking
	 */
	public boolean isChecking() {
		return this.isChecking;
	}

	/**Este metodo modifica el valor del atributo isChecking
	 * @param isChecking isChecking a cambiar
	 */
	public void setChecking(boolean isChecking) {
		this.isChecking = isChecking;
	}

	/**Este metodo retorna el valor del atributo checking
	 * @return El valor de checking
	 */
	public Thread getChecking() {
		return this.automaticChecking;
	}
	
	/**Este metodo inicia el checkeo automatico del dispositivo
	 */
	public void startChecking() {
		this.automaticChecking = new Thread(new Runnable() {
			public void run() {
				while(isChecking) {
					deviceManager.checkDevice(Arrays.toString(mac) + Arrays.toString(ip));
				}
				
				automaticChecking = null;
				enable();
			}
		});
		setChecking(true);
		this.automaticChecking.start();
	}
	
	/**Este metodo elimina el dispositivo de la interfaz grafica si dura tmToOut segundos inactivo
	 * @param tmToOut Tiempo en segundos para que el dispositivo se elimine
	 */
	public void countToRemoveDevice(final int tmToOut) {
		waitToRemove = new Thread(new Runnable() {
			public void run() {
				try {
					int t = tmToOut;
					while(t-- > 0 && getEstado() == State.INACTIVO) {
						Thread.sleep(1000);
						if(getEstado() == State.INACTIVO) {
							setCambio("" + ((t == 0) ? "Bye!" : (t + "s")));
						}
					}
					Thread.sleep(200);
					if(getEstado() == State.INACTIVO) {
						isChecking = false;
						ConsolePanel.print("Removing device...");
						deviceManager.removeDevice(Device.this);
						ConsolePanel.print("Device Removed");
					}
				} catch (InterruptedException evento) {
					ConsolePanel.print("Eliminación interrumpida");
				} finally {
					waitToRemove = null;
				}
			}
		});
		waitToRemove.start();
	}

	/**Este metodo detiene el chequeo automatico
	 */
	public void stopAutomaticChecking() {
		this.isChecking = false;
		if(this.waitToRemove != null) {
			this.waitToRemove.interrupt();
		}
		if(this.automaticChecking != null) {
			this.automaticChecking.interrupt();
		}
	}

	/**Este metodo habilita el boton de enviar del panel del dispositivo
	 */
	public void enable() {
		if(this.devicePanel != null) {
			this.devicePanel.enableButton();
		}
	}

	public void check() {
		Thread check = new Thread(new Runnable() {
			public void run() {
				deviceManager.checkDevice(Arrays.toString(mac) + Arrays.toString(ip));
			}
		});
		check.start();
	}
}
