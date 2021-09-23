package GameUI;

import java.io.IOException;

import java.net.DatagramPacket;

import java.net.InetAddress;

import java.net.MulticastSocket;

import java.net.UnknownHostException;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket;
    protected InetAddress multicastAddress;
    protected byte[] buf = new byte[256];
    protected NetworkMessageBuffer networkMsg;
    private MMLogger log;
    String playerName;
    
 
    public MulticastReceiver( int port, String multicastIP,NetworkMessageBuffer networkMsg) throws IOException {
		// TODO Auto-generated constructor stub
    	
    		this.socket=new MulticastSocket(port);
    		this.multicastAddress=InetAddress.getByName(multicastIP);;
		socket.joinGroup(multicastAddress);
		this.networkMsg= networkMsg; 
		log = new MMLogger();
		playerName= networkMsg.ref.aPlayer.getPlayerName();
		
    		
	}

	public void run() {
		Long endtime;
        try{
            while (true) {
           
            	  //  server.setNewMsg("msg " +counter);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String receivedMsg = new String( packet.getData(), 0, packet.getLength());
                System.err.println("receiver  recieve "+ receivedMsg);
                endtime=System.nanoTime();
                log.doLogging(endtime.toString()+ "; receive by " +playerName +";" +receivedMsg);
                log.doLogging("; byte"+ Integer.toString(packet.getLength()) +";");
                networkMsg.put(receivedMsg);
            }
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
       
       
        
        
    }
}