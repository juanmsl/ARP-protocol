package co.edu.javeriana.redes.negocio;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import co.edu.javeriana.redes.interfaz.ConsolePanel;
import co.edu.javeriana.redes.utilities.Utils;
import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class ARP {
	private static final byte[] BROADCAST_MSN = new byte[] {(byte)255, (byte)255, (byte)255, (byte)255, (byte)255, (byte)255};
	
	public static boolean CONTINUAR = true;
	
	public static final Inet4Address IP = ARP.getLocalIP();
	public static final Inet4Address MASK = ARP.getMaskNetwork(IP);
	public static final Inet4Address SUBNET = ARP.getSubnetNetwork(IP, MASK);
	public static final NetworkInterface NETWORK_DEVICE = ARP.getNetworkInterfaceFor(IP);
	public static final int MASK_SIZE = ARP.getMaskSize(MASK);
	public static final int HOST = ARP.getNumberOfHostForMask(MASK_SIZE);
	public static final Inet4Address BROADCAST_ADDRESS = ARP.getBroadcastAddress(SUBNET, HOST);
	
	public static final boolean ENABLE = (IP != null && MASK != null && SUBNET != null && NETWORK_DEVICE != null && BROADCAST_ADDRESS != null);

	private static byte[] arp(InetAddress IP, byte[] MAC, NetworkInterface device) throws IOException {
		JpcapCaptor captor = JpcapCaptor.openDevice(device, 2000, false, 3000);
		captor.setFilter("arp", true);
		JpcapSender sender = captor.getJpcapSenderInstance();
		InetAddress srcip = null;
		for (NetworkInterfaceAddress addr : device.addresses) {
			if (addr.address instanceof Inet4Address) {
				srcip = addr.address;
				break;
			}
		}
		ARPPacket arp = new ARPPacket() {
			private static final long serialVersionUID = 1L;

			@Override
			public String toString() {
				return "[" + hardtype + " | " + prototype + " | " + hlen + " | " + plen + " | " + operation + " | " + Utils.byteArrayToHexString(sender_hardaddr, ":") + " | " + Utils.byteArrayToDecString(sender_protoaddr, ".") + " | " + Utils.byteArrayToHexString(target_hardaddr, ":") + " | " + Utils.byteArrayToDecString(target_protoaddr, ".") + "]";
			}
		};
		arp.hardtype = ARPPacket.HARDTYPE_ETHER;
		arp.prototype = ARPPacket.PROTOTYPE_IP;
		arp.hlen = 6;
		arp.plen = 4;
		arp.operation = ARPPacket.ARP_REQUEST;
		arp.sender_hardaddr = device.mac_address;
		arp.sender_protoaddr = srcip.getAddress();
		arp.target_hardaddr = MAC;
		arp.target_protoaddr = IP.getAddress();
		
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac = device.mac_address;
		ether.dst_mac = MAC;
		
		arp.datalink = ether;
		
		ConsolePanel.print(arp);
		sender.sendPacket(arp);
		
		while (true) {
			ARPPacket p = (ARPPacket) captor.getPacket();
			if (p == null) {
				throw new IllegalArgumentException(IP + " didn't respond the ARP packet");
			}
			if (Arrays.equals(p.target_protoaddr, srcip.getAddress())) {
				return p.sender_hardaddr;
			}
		}
	}
	
	public static void getDevicesTo(DeviceManager deviceManager) {	
		ConsolePanel.print("════════════════════════════════════");
		ConsolePanel.print("Starting the ARP to " + IP.getHostName() + ":");
		ConsolePanel.print("  Local IP: " + IP.getHostAddress());
		ConsolePanel.print("      Mask: " + MASK.getHostAddress() + " /" + MASK_SIZE);
		ConsolePanel.print("    Subnet: " + SUBNET.getHostAddress());
		ConsolePanel.print("      Host: " + HOST + " machines");
		ConsolePanel.print(" Broadcast: " + BROADCAST_ADDRESS.getHostAddress());
		ConsolePanel.print("════════════════════════════════════");
		
		int subnet = ARP.convertQuartetToBinaryString(SUBNET.getHostAddress());
		int broadcast = ARP.convertQuartetToBinaryString(BROADCAST_ADDRESS.getHostAddress());
		
		for (int host = subnet + 1; CONTINUAR && host <= broadcast; host++) {
			try {
				int timeout = deviceManager.getTimeout();
				byte[] byte_IP = InetAddress.getByName(ARP.convertIpToQuartet(host)).getAddress();
				InetAddress auxIP = InetAddress.getByAddress(byte_IP);
				ConsolePanel.print("ARP to /" + Utils.byteArrayToDecString(byte_IP, ".") + "...");
				if (auxIP.isReachable(timeout)) {
					byte[] MAC = arp(auxIP, ARP.BROADCAST_MSN, NETWORK_DEVICE);
					deviceManager.addDevice(MAC, auxIP.getAddress());
					ConsolePanel.print("\t" + Utils.byteArrayToDecString(byte_IP, ".") + " : [" + Utils.byteArrayToHexString(MAC, " : ") + "]");
				} else {
					ConsolePanel.print("\t" + Utils.byteArrayToDecString(byte_IP, ".") + ": Didn't respond in " + timeout + "ms");
				}
			} catch (UnknownHostException evento) {
				ConsolePanel.print("\tError getting the IP address [" + evento + "]");
			} catch (IOException evento) {
				ConsolePanel.print("\tError waiting the request [" + evento + "]");
			} catch (IllegalArgumentException evento) {
				ConsolePanel.print("\t[" + evento.getMessage() + "]");
			}
		}
	}

	public static boolean sendARPTo(Device device) {
		InetAddress IP_DEVICE = null;
		try { IP_DEVICE = InetAddress.getByAddress(device.getIp()); }
		catch (UnknownHostException evento1) {
			ConsolePanel.print("\tError getting the local IP address");
			return false;
		}
		byte[] MAC = device.getMac();
		ConsolePanel.print("Checking /" + IP_DEVICE.getHostAddress() + "...");
		try {
			byte[] New_MAC = arp(IP_DEVICE, MAC, NETWORK_DEVICE);
			if(Arrays.equals(New_MAC, MAC)) {
				ConsolePanel.print("\t" + IP_DEVICE.getHostAddress() + ": Is active");
				return true;
			}
			ConsolePanel.print("\t" + IP_DEVICE.getHostAddress() + ": request with another MAC address");
		} catch (UnknownHostException evento) {
			ConsolePanel.print("\tError getting the IP address [" + evento + "]");
		} catch (IOException evento) {
			ConsolePanel.print("\tError waiting the request [" + evento + "]");
		} catch (IllegalArgumentException evento) {
			ConsolePanel.print("\tError [" + evento.getMessage() + "]");
		}
		return false;
	}
	
	private static NetworkInterface getNetworkInterfaceFor(InetAddress ip) {
		try {
			NetworkInterface[] devices = JpcapCaptor.getDeviceList();
			for (NetworkInterface device : devices) {
				for (NetworkInterfaceAddress addr : device.addresses) {
					if (!(addr.address instanceof Inet4Address)) continue;
					byte[] my_IP = ip.getAddress();
					byte[] mask_network = addr.subnet.getAddress();
					byte[] bif = addr.address.getAddress();
					for (int i = 0; i < 4; i++) {
						my_IP[i] = (byte) (my_IP[i] & mask_network[i]);
						bif[i] = (byte) (bif[i] & mask_network[i]);
					}
					if (Arrays.equals(my_IP, bif)) {
						return device;
					}
				}
			}
		} catch (Exception evento) {
			System.out.println("Error getting the devices [" + evento + "]");
		}
		return null;
	}
	
	private static Inet4Address getLocalIP() {
		try {
			return (Inet4Address) Inet4Address.getLocalHost();
		} catch (UnknownHostException evento) {
			System.out.println("Error getting the IP address [" + evento.getMessage() + "]");
		}
		return null;
	}
	
	private static Inet4Address getMaskNetwork(Inet4Address ip) {
		try {
			NetworkInterface[] devices = JpcapCaptor.getDeviceList();
			for (NetworkInterface device : devices) {
				for (NetworkInterfaceAddress addr : device.addresses) {
					if (!(addr.address instanceof Inet4Address)) continue;
					byte[] my_IP = ip.getAddress();
					byte[] mask_network = addr.subnet.getAddress();
					byte[] bif = addr.address.getAddress();
					for (int i = 0; i < 4; i++) {
						my_IP[i] = (byte) (my_IP[i] & mask_network[i]);
						bif[i] = (byte) (bif[i] & mask_network[i]);
					}
					if (Arrays.equals(my_IP, bif)) {
						return (Inet4Address) addr.subnet;
					}
				}
			}
		} catch (NullPointerException evento) {
			System.out.println("Error getting the mask network for IP null address [" + evento.getMessage() + "]");
		} catch (Exception evento) {
			System.out.println("Error getting the mask network [" + evento.getMessage() + "]");
		}
		return null;
	}
	
	private static Inet4Address getSubnetNetwork(Inet4Address ip, Inet4Address mask) {
		try {
			byte[] mask_network = mask.getAddress();
			byte[] my_IP = ip.getAddress();
			for (int i = 0; i < 4; i++) {
				my_IP[i] = (byte) (my_IP[i] & mask_network[i]);
			}
			return (Inet4Address) Inet4Address.getByAddress(my_IP);
		} catch (NullPointerException evento) {
			System.out.println("Error getting the subnet network for IP null address, or MASK null network [" + evento.getMessage() + "]");
		} catch (UnknownHostException evento) {
			System.out.println("Error getting the subnet network [" + evento.getMessage() + "]");
		} catch (Exception evento) {
			System.out.println("Error getting the subnet network [" + evento.getMessage() + "]");
		}
		return null;
	}
	
	private static int getMaskSize(Inet4Address mask) {
		try {
			int bc = -1;
			int ms = ARP.convertQuartetToBinaryString(mask.getHostAddress());
			int host = (int) (Integer.SIZE - (Math.log((bc ^ ms) + 1) / Math.log(2)));
			return host;
		} catch (NullPointerException evento) {
			System.out.println("Error getting the mask size for MASK null network [" + evento.getMessage() + "]");
		} catch (Exception evento) {
			System.out.println("Error getting the mask size [" + evento.getMessage() + "]");
		}
		return -1;
	}
	
	private static int getNumberOfHostForMask(int mask) {
		return (int) Math.pow(2, Integer.SIZE - mask) - 2;
	}
	
	private static Inet4Address getBroadcastAddress(Inet4Address subnet, int host) {
		try {
			int subnet_int = ARP.convertQuartetToBinaryString(subnet.getHostAddress());
			return (Inet4Address) InetAddress.getByName(ARP.convertIpToQuartet(subnet_int + host + 1));
		} catch (NullPointerException evento) {
			System.out.println("Error getting the broadcast address for SUBNET null network [" + evento.getMessage() + "]");
		} catch (Exception evento) {
			System.out.println("Error getting the broadcast address [" + evento.getMessage() + "]");
		}
		return null;
	}
	
	private static int convertQuartetToBinaryString(String ipAddress) {
		String[] ip = ipAddress.split("\\.|/");
		int octet1 = Integer.parseInt(ip[0]);
		int octet2 = Integer.parseInt(ip[1]);
		int octet3 = Integer.parseInt(ip[2]);
		int octet4 = Integer.parseInt(ip[3]);
		int output = octet1;
		output = (output << 8) + octet2;
		output = (output << 8) + octet3;
		output = (output << 8) + octet4;
		return output;
	}
	
	private static String convertIpToQuartet(int ipAddress) {
		int octet1 = (ipAddress >> 24) & 255;
		int octet2 = (ipAddress >> 16) & 255;
		int octet3 = (ipAddress >> 8) & 255;
		int octet4 = ipAddress & 255;
		return octet1 + "." + octet2 + "." + octet3 + "." + octet4;
	}
}
