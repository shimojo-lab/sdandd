package GameUI;

import java.io.IOException;

import java.net.DatagramPacket;

import java.net.DatagramSocket;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MulticastPublisher extends Thread {
	private DatagramSocket socket;
	private InetAddress group;
	private int port;
	private boolean newMsg=false;
	private String netMsg;
	private String confFile;
	private MMLogger log;
	MMUI ref;
	String playerName;


	public  MulticastPublisher(int port, String multicastIP) throws SocketException, UnknownHostException {
		group= InetAddress.getByName(multicastIP);
		socket = new DatagramSocket();
		netMsg="";
		this.port=port;
		log = new MMLogger();

	}

	public synchronized void setNewMsg (String x) {
		netMsg=x;
		newMsg=true;
		notifyAll();
	}

	public boolean isNewMsg() {
		return newMsg;
	}
	public synchronized void forceNewMsg () {
		
		newMsg=true;
		notifyAll();
	}

	public void run() {
		if(ref!=null)
			 playerName=ref.aPlayer.getPlayerName();
		String logmsg;
		int counter=0;
		Long initTime;
		while(true) {

			synchronized(this) {

				try {
					if (!isNewMsg()) { 
						wait();
					}
					else {
						initTime=System.nanoTime();
						multicast(netMsg+";"+(initTime.toString())+"; send by ; " +playerName +";");
						initTime=System.nanoTime();
						//log.doLogging(initTime.toString()+"; send by ; " +playerName +";"+netMsg);
						System.err.println("publisher send "+ netMsg);
						try {
							sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						newMsg=false;
					}

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void multicast(String multicastMessage) throws IOException {
		DatagramPacket packet = new DatagramPacket(multicastMessage.getBytes(), multicastMessage.getBytes().length, group, port);
		socket.send(packet);
	}

	public String getConfFile() {
		return confFile;
	}

	public void setrefMMUI(MMUI mmui) {
		this.ref=mmui;
	}
	public void setConfFile(String confFile) {
		//System.out.println(confFile);
		this.confFile = confFile;
	}
}