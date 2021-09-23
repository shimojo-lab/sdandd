package GameUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import Entity.Brick;
import Entity.ItemCard;
import Entity.Player;

public class DNSCacheUI extends JPanel {


	ImageIcon server, pc;
	JButton victimPC, rootNS, secComNS, secComHTTPS,victimNS, attackerNS, attackerHTTPS, runDNSSPoof;
	ImageIcon vicpc,root,sec,sechttp,vicns,attns,atthttp;
	JLabel TFDescription, mission_score;
	JTextField tfPlayer;
	String useratServer;
	JPanel panel;
	Player player;
	// text for general information text2 for user interaction 
	JTextArea text;
	JScrollPane scroll;
	boolean p1,p2, runWelco; // phase 1..n
	ArrayList<Brick> dns_Cache=new ArrayList<Brick>();
	ArrayList<Brick> packets=new ArrayList<Brick>();
	String helpMsg;
	int animationCounter=0, c=0,  c_aux=0, completed_task;
	Brick ping_sec_net,ping_xxx_example,query_sec_net,query_xxx,cache_poisoning;
	List<Brick> linkPC_to_DNS_Victim,link_victim_DNS_to_attacker_ns_example_com,
	link_pc_vic_to_attacker_https_xxx,link_ns_vic_to_DNS_root,link_pc_to_http_secnet;

	protected Map<String, String> map1 = new HashMap<String, String>(), map2 = new HashMap<String,
			String>(), map3 = new HashMap<String, String>();
	// reference to room 
	TaksRoom room;
	private String setLog="";
	protected NetworkCard netServer ;
	boolean packetAnimation=false;
	ScheduledExecutorService scheduler;
//	Timer timer;
	boolean frozen = false, runCmd=false;
	
	int initlength = 0, len=0 , counter=0;
	private int scores [] = {0,0};

	public DNSCacheUI( Player player,JPanel panel, JTextArea text, String instruction, String input,String input2,boolean runWelco){

		this.player=player;
		this.panel=panel;
		room =  (TaksRoom) player.playerCurrentRoom();
		panel.setLayout(new FlowLayout());
		this.text = text;
		useratServer=player.getPlayerName()+"@127.0.0.1:~ $";
		helpMsg=instruction;
		vicpc= createImageIcon("/pc2.png");
		root= createImageIcon("/server2.png");
		sec= createImageIcon("/server2.png");
		sechttp= createImageIcon("/server2.png");
		vicns= createImageIcon("/server2.png");
		attns= createImageIcon("/server2.png");
		atthttp= createImageIcon("/server2.png");
		pakcetSetup();
		netServer=null;
		completed_task=0;
		panel.setVisible(true);
		TFDescription= new JLabel("Command : help , ping domain_name , cat  ");
		
		scheduler = Executors.newScheduledThreadPool(10);
		tfPlayer= new JTextField();
		tfPlayer.setText(useratServer);
		tfPlayer.setEditable(false);
		tfPlayer.setPreferredSize(new Dimension(480,40));
		tfPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String [] global = tfPlayer.getText().split(Pattern.quote(" $ "));
			//	System.err.println("glbal length " +global.length);
				for (int i=0; i<global.length;i++)
				//	System.err.println("glbal length " +global[i]);
				if (global.length < 2) {
					tfPlayer.setText(useratServer);
					String err ="invalid command !!! \n"
							+ "expected command :  help , ping domain_name , cat ";
					text.setText(err);
				}
				else {
					proceedMsg(global[1]);
					if(check_completed_mission()) {
						room.taskCompleted=true;
						room.getRefRoomUI().send_Net_Msg_Task_Completed(player, room.roomID);
						room.getRefRoomUI().repaint();
					}
				}

			}
		});


		this.text.setEditable(false);
		this.text.setVisible(true);

		this.runWelco=runWelco;
		if(runWelco)
		{
			this.text.setText(input);
		}
		else{
			this.text.setText(instruction);
			runWelco=true;
		}

		scroll = new JScrollPane(this.text);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(480,150));

		panel.add(scroll);
		panel.add(TFDescription);
		panel.add(tfPlayer);


		addMouseListener(new MouseAdapter(){  @Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) )  
				{   
					if(player.getPlayerItemList().size()>0)
						for (ItemCard item : player.getPlayerItemList())
							item.setDropPlayercard(true);
					new DropCardGUI(player);
	
				}

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}
		});

		rootNS = new JButton();
		rootNS.setIcon(root);
		rootNS.setVisible(true);
		rootNS.setBounds(140, 60,root.getIconWidth() ,root.getIconHeight());
		rootNS.addActionListener( new ACL("10.1.1.10"));
		add(rootNS);


		secComNS = new JButton();
		secComNS.setIcon(sec);
		secComNS.setVisible(true);
		secComNS.setBounds(260, 60,sec.getIconWidth() ,sec.getIconHeight());
		add(secComNS);

		secComHTTPS = new JButton();
		secComHTTPS.setIcon(sechttp);
		secComHTTPS.setVisible(true);
		secComHTTPS.setBounds(380, 60,sechttp.getIconWidth() ,sechttp.getIconHeight());
		add(secComHTTPS);

		victimPC = new JButton();
		victimPC.setIcon(vicpc);
		victimPC.setVisible(true);
		victimPC.setBounds(20, 270,vicpc.getIconWidth() ,vicpc.getIconHeight());
		victimPC.addActionListener(new ACL("10.1.2.5"));
		add(victimPC);

		victimNS = new JButton();
		victimNS.setIcon(vicns);
		victimNS.setVisible(true);
		victimNS.setBounds(140, 270,vicns.getIconWidth() ,vicns.getIconHeight());
		victimNS.addActionListener(new ACL("10.1.2.20"));
		add(victimNS);

		attackerNS = new JButton();
		attackerNS.setIcon(attns);
		attackerNS.setVisible(true);
		attackerNS.setBounds(260, 270,attns.getIconWidth() ,attns.getIconHeight());
		attackerNS.addActionListener(new ACL("10.1.3.30"));
		add(attackerNS);

		attackerHTTPS = new JButton();
		attackerHTTPS.setIcon(atthttp);
		attackerHTTPS.setVisible(true);
		attackerHTTPS.setBounds(380, 270,atthttp.getIconWidth() ,atthttp.getIconHeight());
		add(attackerHTTPS); 

		runDNSSPoof=new JButton("Run DNS Spoof");
		runDNSSPoof.setVisible(true);
		runDNSSPoof.setBounds(352, 400, 120, 30);
		runDNSSPoof.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("");
				String setup = "Easy phishing: Set up email templates, landing pages and listeners\n" + 
						"in Metasploit Pro -- learn more on http://rapid7.com/metasploit\n" + 
						"       =[ metasploit v4.14.10-dev                         ]\n" + 
						"+ -- --=[ 1639 exploits - 944 auxiliary - 289 post        ]\n" + 
						"+ -- --=[ 472 payloads - 40 encoders - 9 nops             ]\n" + 
						"+ -- --=[ Free Metasploit Pro trial: http://r-7.co/trymsp ]\n" + 
						"msf > use auxiliary/spoof/dns/bailiwicked_host \n" + 
						"msf auxiliary(bailiwicked_host) >";
				text.setText(setup);
				runCmd=true;
			}
			});
		
		mission_score = new JLabel ("Completed task :"+ completed_task+"/2");
		mission_score.setForeground(Color.RED);
		mission_score.setBounds(10,400 , 130, 50);
		mission_score.setVisible(true);
		
		add(runDNSSPoof);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(530,550));
		panel.setBounds(0, 430, 520, 700);
		add(panel);
		add(mission_score);
		setVisible(true);

	}

	public void set_DNS_Cache(ArrayList<Brick> bricks) {
		dns_Cache = bricks;
		linkPC_to_DNS_Victim=listOfBrick("L1");
		link_victim_DNS_to_attacker_ns_example_com=listOfBrick("L2");
		link_pc_vic_to_attacker_https_xxx=listOfBrick("L3");
		link_ns_vic_to_DNS_root=listOfBrick("L5");
		link_pc_to_http_secnet=listOfBrick("L6");
		//repaint();

	}

	private void pakcetSetup() {
		

		ping_sec_net= new Brick("DNS_Cache_Poisoning",10, 20,"/pingsecnet.png","ping_sec_net",false,"no",false);
		ping_xxx_example= new Brick("DNS_Cache_Poisoning",20, 20,"/pinxxx.png","ping_xxx_example",false,"no",false);
		query_sec_net= new Brick("DNS_Cache_Poisoning",30, 20,"/querysecnet.png","query_sec_net",false,"no",false);
		query_xxx= new Brick("DNS_Cache_Poisoning",40, 20,"/queryxxx.png","query_xxx",false,"no",false);
		cache_poisoning= new Brick("DNS_Cache_Poisoning",50, 20,"/cachepoisoning.png","cache_poisoning",false,"no",false);

		packets.add(ping_sec_net);
		packets.add(ping_xxx_example);
		packets.add(query_sec_net);
		packets.add(query_xxx);
		packets.add(cache_poisoning);
		
		

	}


	public String split(String text){

		String [] split=null;
		try{
			split = text.split("-");
		}catch (java.lang.NullPointerException ex ){}

		String ret="";

		for (String temp: split){
			ret+=temp+"\n";
		}

		return ret;


	}
	private void errMsg(String s) {
		
		JOptionPane.showMessageDialog(this, s);
		return;
	}
	private class ACL implements ActionListener{

		String IP;
		public ACL(String ip) {
			IP=ip;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!runCmd) {
				String msg = "First you have to run DNS Spoofing  \n ";
				errMsg(msg);
			}
			TaksRoom r = (TaksRoom) player.playerCurrentRoom();
			netServer = r.getNetworkComp_IP_or_Name(true,IP);
			useratServer=player.getPlayerName()+"@"+netServer.IPaddress+":~ $ ";
			tfPlayer.setEditable(true);
			tfPlayer.setText(useratServer);
		}

	}


	public void proceedMsg(String msg) {
		String [] split = msg.split(" ");
		switch (split[0]) {
		case "ping" : ping(split[1].trim());
		break;
		case "cat"  : displayMSg("cat");
		break; 
		case  "help" : displayMSg("help");
		default:
			break;

		}
	}

	private void displayMSg(String op) {

		if(op.equals("help"))
			text.setText(helpMsg);


		if(op.equals("cat")) {
			String msg = netServer.checkServerConfiguration(netServer.IPaddress);
			text.setText(msg);
		}
	}
	private void setDrawtoFalse(List<Brick> list) {
		for (Brick b :list)
			b.draw=false;
	}
	
	private String simulatePing(String IP, int icmp_seq, long startTime) {
		long elapse = (System.nanoTime()-startTime)/100L;
		String packet_reply = "64 bytes from "+IP+": "+"icmp_seq="+icmp_seq+ " ttl=255 "+"time="+elapse+" ms \n" ;
		return packet_reply;
				
	}
	private boolean run_poisoning =false,run_ping_reply=false;
	private String IP="";

	
	private void ping(String domainName) {
		//init varible control
		
		run_ping_reply=false;
		IP="";
		
		if (!validateInputDomain(domainName)) {
				String msg = "domain name entered incorrect \n "
						+ " correct domain name xxx.example.com and secnet.com";
				JOptionPane.showMessageDialog(this, msg);
				return;
		}
		animationCounter=0;
		c=0;
		c_aux=0;
		packetAnimation=false;
		setDrawtoFalse(packets);
		IP =room.getNetworkComp_IP_or_Name(false, domainName).IPaddress;

		/*
		 * decide to run dns query 
		 */
		String query="";
		if (domainName.equals("secnet.com"))
			 query = "00  \n \n \n";
		
		else {
		
			
			 query = "00 00 00 00 00 00 00  00 00 00 00 08 00 45 00   ........ ......E.\n" + 
					"0010  00 3c 51 e3 40 00 40 11  ea cb 7f 00 00 01 7f 00   .<Q.@.@. ........\n" + 
					"0020  00 01 ec ed 00 35 00 28  fe 3b 24 1a 01 00 00 01   .....5.( .;$.....\n" + 
					"0030  00 00 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c   .......www.xxx.ex\n" + 
					"0040  65 03 63 6f 6d 00 00 01  00 01                     ample.com... ..   \n"+ 
					"0000  00 00 00 00 00 00 00 00  00 00 00 00 08 00 45 00   ........ ......E.\n" + 
					"0010  00 7a 00 00 40 00 40 11  3c 71 7f 00 00 01 7f 00   .z..@.@. <q......\n" + 
					"0020  00 01 00 35 ec ed 00 66  fe 79 24 1a 81 80 00 01   ...5...f .y$.....\n" + 
					"0030  00 03 00 00 00 00 03 77  77 77 06 67 6f 6f 67 6c   .......w ww.examp\n" + 
					"0040  65 03 63 6f 6d 00 00 01  00 01 c0 0c 00 05 00 01   le.com... ........\n" + 
					"0050  00 05 28 39 00 12 03 77  77 77 01 6c 06 67 6f 6f   ..(9...w ww.e.exam\n" + 
					"0060  67 6c 65 03 63 6f 6d 00  c0 2c 00 01 00 01 00 00   ple.com. .,......\n" + 
					"0070  00 e3 00 04 42 f9 59 63  c0 2c 00 01 00 01 00 00   ....B.Yc .,......\n" + 
					"0080  00 e3 00 04 42 f9 59 68                            ....B.Yh fin  \n \n \n";
			 run_poisoning=true;
 
		}
		String [] split= query.split(" ");
		text.setText("");
	
	
	SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
		@Override
		protected Void doInBackground() throws Exception {
			for (int i = 0; i < split.length; i++) {
				//System.out.println("string length" +split.length + " iiiiiiii" +i);
				if(animationCounter !=5) {
					publish(split[i]);
					Thread.sleep(50);
				}
				
				if( i== split.length-1 ) {
					System.err.println("i== split.length-1" );
					run_ping_reply=true;
					for (int j=0; j<50;j++) {
						long startTime = System.nanoTime();
						publish(simulatePing(IP, j, startTime));
						Thread.sleep(50);
					}
				}
				
			}
			return null;
		}
		@Override
		protected void process(List<String> chunks) {
			text.append(chunks.get(chunks.size() - 1).toString());
			System.out.println("run reply " + run_ping_reply);
			if(run_ping_reply)
				animatedPacket(domainName, 5, 1);
			else {
				
				if(c==0 && c_aux==0)
					animationCounter++;
				if(c_aux==0)
					animatedPacket(domainName, animationCounter, 1);
				c++;
				c_aux++;
				
				if (c_aux==22)
					c_aux=0;
				if (c==84 )
					c=0;
				if(c==0) 
					animationCounter++;
			}

		}

		@Override
		protected void done() {}
	};
	worker.execute();
}
	
	private boolean validateInputDomain(String domainName) {
		if(domainName.equals("xxx.example.com") ||domainName.equals("secnet.com") )
			return true;
		else return false;
	}
	// reconsider refactor 
	int scheduler_counter=0;
	
	private void completed_mission(boolean poisoning, int value) {
		if(poisoning)
			scores[0]=value;
		else 
			scores[1]=value;
		int score= scores[0]+ scores[1];
		mission_score.setText("Completed task :"+ score+"/2");
	}
	private boolean check_completed_mission(){
		boolean r=false;
		int score= scores[0]+ scores[1];
		if(score==2) {
			r=true;
			if (!room.taskCompleted)
				JOptionPane.showMessageDialog(this,"Mission completed."," Alert",JOptionPane.WARNING_MESSAGE);
		}
		return r;
		
	}
	private void animatedPacket(String domainName, int animate_Index, int repetition) {

		if(domainName.equals("xxx.example.com")) {
			completed_mission(true,1);
			
			for (int j=0; j<10;j++) {
			
			// pc victim perform query to it own DNS for resolver the xxx.example.com 
			if(animate_Index==1) 
				drawPacket(linkPC_to_DNS_Victim,query_xxx,null,"query_xxx",false);
		
			// the DNS victim due to no cache therefore perform NS query to the root DNS it belong to 
			if(animate_Index==2) 
				drawPacket(link_ns_vic_to_DNS_root,query_xxx,null,"query_xxx",true);
		
			// the DNS victim  perform NS query to the root DNS attacker (xxx.example.com ) based on information respond from the DNS root it belong to 
			// at the same time the DNS attacker will launch cache poisoning to the the dns victim 
			 if(animate_Index==3) 
				drawPacket(link_victim_DNS_to_attacker_ns_example_com,query_xxx,cache_poisoning,"query_xxx",true);
		
			// the the  dns  victim add the resolve name into its cache and inform the PC victem  the resolve information. 
			 if(animate_Index==4) {
				 drawPacket(linkPC_to_DNS_Victim,query_xxx,null,"query_xxx",true);
			}
			 
			 // pc victim successfully ping the host xxx.example.com
			if(animate_Index==5) {
				for (int i=0; i<repetition;i++) {
					drawPacket(link_pc_vic_to_attacker_https_xxx,ping_xxx_example,null,"ping_xxx_example",true);
				//animationCounter=0;
				}
			}
		}
	}

		else if(domainName.equals("secnet.com")) {
			completed_mission(false,1);
			if(run_poisoning)
				drawPacket(link_pc_vic_to_attacker_https_xxx,ping_sec_net,null,"ping_xxx_example",true);
			else
				drawPacket(link_pc_to_http_secnet,ping_sec_net,null,"ping_xxx_example",true);
		} 
	}
	
	    //int counterAux=0;
		boolean runReverse=false, run_poisoining=false;

	    private void drawPacket(List <Brick> list,Brick packetBrick,Brick poisoning_packet, String packetType, boolean reverse) {
			
			   int LOOPS_PER_SECOND = 100;
			   this.packetAnimation=true;
			    packetBrick.draw=true;
			    if(poisoning_packet !=null)
			    	poisoning_packet.draw=true;

			 	counter=0;
			         ScheduledFuture<?> animationHandler = scheduler.scheduleAtFixedRate(() -> {
			    				packetBrick.setX(list.get(counter).getX());
			    				packetBrick.setY(list.get(counter).getY());
			    				repaint();
			    				 if(counter == 0 && runReverse) {
			    					 runReverse=false;
			    					 counter=0;
			    				 }
			    				 if(poisoning_packet !=null) {
			    					 if(runReverse) {
				    					poisoning_packet.setX(list.get(counter).getX());
				    					poisoning_packet.setY(list.get(counter).getY());
				    					repaint();
			    					 }
				    			}	
			    				if(counter> 0 && runReverse) 
			    					counter--;
			    				
			    				 if(counter<list.size() && !runReverse ) 
			    					counter++;	
			    				 
			    				 if(counter >= list.size()) {
			    					 runReverse=true;
			    					 counter=list.size()-1;
			    				 }

			        },1, 2000/LOOPS_PER_SECOND, TimeUnit.MILLISECONDS);

			       	scheduler.schedule(new Runnable() {
			    	       public void run() { 
			    	    	   packetBrick.draw=false;
			    	    	   if(poisoning_packet !=null) 
			   			    	poisoning_packet.draw=false;
			    	    	   repaint();
			    	    	   animationHandler.cancel(true);
			    	    	   }
			    	     },  1, TimeUnit.SECONDS);
			}
	private List<Brick> listOfBrick(String op)	{
		List<Brick> bricks= new ArrayList<Brick>();
		//System.err.println("dns cache size "+dns_Cache.size());
		for (Brick brick: this.dns_Cache) {
			
			if (brick.operation.equals(op))
				//System.err.println("brik operation "+ op+ " x " +brick.getX() + " y " + brick.getY() + " draw " +brick.draw);
				bricks.add(brick);
		}
		return bricks;
	}

	public void setErrorMsg( String msg){
		String s;
		if(!p1) 
			s="Password Cracker";
		else 
			s="SET";
		text.setText("Invalid card !!!!!! \n"
				+ "Carrd used  is  "+ msg
				+ " \n But required is " +s);

	}




	public  ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ItemCard.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			//System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (Brick b : this.dns_Cache) {
			if(b.draw ) {
				g.drawImage(b.getImage(),b.getX(),b.getY(),this);
			}else {
				if(b.operation.equals("HIDEN"))
					g.drawImage(b.getImage(),b.getX(),b.getY(),this);
				else {
					
					g.setFont(new Font("TimesRoman",Font.PLAIN, 10));
					g.setColor(Color.BLACK);
					String [] s=b.operation.split(";");
					int offset=3*s.length;
					for (int i=0; i<s.length;i++) {
						if(!b.operation.equals("L6")) 
							g.drawString(s[i], b.getX(), b.getY()+(i*offset));
					}
				}
			}
		}

		if(packetAnimation) {
			for (Brick b: packets)
				if(b.draw)
					try {
						
						g.drawImage(b.getImage(),b.getX(),b.getY(),this);
					//	System.err.println("done with draw now entering in sleep");
						Thread.sleep(5);
						}
						catch(InterruptedException ex) {}
					
		}

	}
	public void run_DNS_Cache(DNSCacheUI panel) {

		JFrame frame = new JFrame("DNS Cache Poisining");
		panel.setVisible(true);
		frame.add(panel);
		frame.setSize(new Dimension(530,700));
		frame.setVisible(true);

	}

}
