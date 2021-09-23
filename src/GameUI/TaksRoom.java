
package GameUI;


import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import Entity.Brick;
import Entity.ImageHolder;
import Entity.ItemCard;
import Entity.Player;
import Entity.Room;

@SuppressWarnings("serial")
public class TaksRoom extends Room {

	private static final long serialVersionUID = 1L;

	private int intendMoveRoom = 0; // increment by one if player hit boundary
	BufferedImage imgBg;
	private String taskDescription="";
	private Image image;
	public String [][] canvas = new String [30][30];

	/*
	 *  define  task to be loaded 
	 *  1. wirelessAtack task 
	 *  2. socialMedia task 
	 *  3. networkSecurity 
	 */
	public String taskID;
	
	/*
	 * global variable for wireless penetration 
	 */
	public ImageHolder wireLess1,wireLess2,wireLess3;
	//control order of game 
	protected boolean wl1,wl2,v1,v2,v3;
	int index=0;
	String congrat="";
	ImageHolder hint;

	//net security task 
	public int numberOfNetworkMounted=0;
	ImageHolder loadNextNetTask;
	

	ImageHolder phone;
	protected Map<String, String> mapQuizTSR = new HashMap<String, String>();
	protected ArrayList<ImageHolder> taskRoomImg;
	public ArrayList<NetworkCard> servers;
	protected ArrayList<Brick> dns_Cache;
	public ArrayList<Brick> trapBrick;
	private String [] keys;


	JPanel p = new JPanel();
	JTextArea ta,tb,tc ;
	JTextField tf;
	JLabel l1 = new JLabel();
	JLabel l2 = new JLabel();
	JLabel complete = new JLabel();
	String welcome="";
	TextInteraction task;
	SosMediaUI sosMedia;
	boolean lastTask=false;
	JButton button = new JButton();
	boolean taskCompleted=false;
	JScrollPane scroll;


	protected String roomName;
	protected ImageIcon imageIcon;
	protected ImageHolder hintImage;
	public String DNS_CACHE_POISONING;



	public TaksRoom(int x, int y, String roomID,String taskID, String roomName, String taskDescription,  boolean E_door, boolean W_door, boolean N_door, boolean S_door, Color col, Player aPlayer, String imagePath,RoomUI refRoomUI) {

		super(x,y,roomID,E_door,W_door,N_door,S_door,col, aPlayer, refRoomUI);
		this.taskDescription=taskDescription;
		this.roomName=roomName;
		wl1=false;
		wl2=false;
		v1=true;
		v2=false;
		v3=false;
		taskRoomImg= new ArrayList<ImageHolder>();
		servers =  new ArrayList<NetworkCard>();
		dns_Cache= new ArrayList<Brick>();
		trapBrick=new ArrayList<Brick>();


		this.taskID=taskID;


		/*
		 *  this JComponet are component of wireless task 
		 */
		
	  	ta = new JTextArea();
		tf = new JTextField();
		
		
		
		tf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (index==0)
					ta.setText("");
				if(index < mapQuizTSR.size()){
				
				ta.append(keys[index]+ mapQuizTSR.get(keys[index])+" \n ");
	
					tf.setText(keys[index]);
					index++;	
				}
				else {					
					ta.append("\n"+congrat);
					if(tf.getText().equals("")){
						//System.out.println("print which object gen "+e.getSource().toString());

						task.dispose();
						//task=null;
						
						if(lastTask){
							getPlayer().addDigitalPoint(25);
							getPlayer().setNumberCompletedTask();
							hint.draw=false;

							// check win and loss 
							getPlayer().checkStatus();
							taskCompleted=true;
							getRefRoomUI().send_Net_Msg_Task_Completed(getPlayer(), getRoomID());



						}
					}
					tf.setText("");
				}
			}
		});
		
		
		
		try {
			// Load the background image
			if(!imagePath.equals("")) {
				java.net.URL imgURL = TaksRoom.class.getResource(imagePath);
				imgBg = ImageIO.read(new File(imgURL.getPath()));  
			}

		}catch (IOException exp) {
			exp.printStackTrace();
		}
		
		
		scroll = new JScrollPane(ta);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}
//	public void initWorld(int size) {
//		if (initArrayList(size)!=null)
//			this.dns_Cache=initArrayList(size);
//		
//	}
	
	public NetworkCard getNetworkComp_IP_or_Name(boolean IP, String IP_or_Name)
	{
		
		for (NetworkCard s : servers) {
			//System.err.println(IP_or_Name + " --> "+s.domainName  + " --> "+s.IPaddress);
			if (IP)
				if(s.IPaddress.equals(IP_or_Name))
					return s;
			if(!IP)
				if(s.domainName.equals(IP_or_Name))
					return s;
			
		}
		return null;
	}
	public  void  initcanvas(String design_layout) {
		
		String[] lines = design_layout.split(System.getProperty("line.separator"));
		for (int i=0;i<lines.length;i++) 
			for (int j=0;j<lines[i].length();j++)
				canvas[i][j]= Character.toString(lines[i].charAt(j)).trim();
	}
	
	public  ArrayList<Brick>  setcanvas(String design_layout, int size) {
		int 	bX,bY,temp;
		ArrayList<Brick> ret = new ArrayList<Brick>();
		initcanvas( design_layout);
		boolean write=false;
		
		if (size >1) {
		 	bX=10;
		 	bY=10;
		  temp=bX;
		}
		else {
				
			 	int var_X= walls.get(0).getX();
			 	
			 	bX=walls.get(0).getX()+15;
			 	if (var_X == 250)
			 		bX=walls.get(0).getX()+25;
			 	if (var_X == 500)
			 		bX=walls.get(0).getX()+35;
			 		
			    bY=walls.get(0).getY()-220;

			 temp=bX;
		}
		String[] lines = design_layout.split(System.getProperty("line.separator"));
		for (int i=0;i<canvas.length;i++) {
			for (int j=0;j<canvas[i].length;j++) {
				if(canvas[i][j]==null)
					break;
				if(canvas[i][j].equals("0") ||canvas[i][j].equals("")) {
					write=false;
					//System.err.println(canvas[i][j]);
				}
				else 
					write=true;
				if(write) {
					if (canvas[i][j].equals("#"))
						ret.add( new Brick(this.roomID,bX+10*(i+1), bY+10*(j+1),"/wall_sq.png",canvas[i][j],true,"no",false));
					else
						ret.add( new Brick(this.roomID,bX+10*(i+1), bY+10*(j+1),"/line.png",canvas[i][j],true,"no",false));
					//System.err.println(bX*(i+1) +" "+ bY*(j+1)+ " "+canvas[i][j]);
				}
				
			}
		}
		return ret;
		
		
	}
	
	 
	public ArrayList<Brick> initArrayList(int size, boolean defaultScreen) {
		int bricksize = this.BRICKSIZE*size;
		boolean stop_write_L6=false;
		ArrayList<Brick> ret = new ArrayList<Brick>();
		if (defaultScreen) {
			DNS_CACHE_POISONING=
				
				"**************************\n"+
				"L      S     S      S    *\n"+
				"                         *\n"+
				"      ##### ##### ##### **\n"+
				"      #   # #   # #   # **\n"+
				"      #   # #   # #   # **\n"+
				"        %     +     -   **\n"+
				"        %     +     -   **\n"+
				"   $$$$$3$$$$$4$$$$$5   **\n"+
				"   $    %     +     $   **\n"+
				"   $    %     +     $   **\n"+
				" @@1@@@@%^^^^^2     $   **\n"+
				" @ $   @%^    ^     $   **\n"+
				" @ $   @%^    ^     $   **\n"+
				"#   # #   # #   # #   #***\n"+
				"#   # #   # #   # #   #***\n"+
				"##### ##### ##### #####***\n"+
				"s    s     s     s      **\n"+
				"                        **\n"+
				"*                        *\n"+
				"*                        *\n"+
				"*************************";
			
			
		}
		else {
			DNS_CACHE_POISONING=
		"*********************\n"+
		"                    *\n"+
		"L  S    S     S     *\n"+
		"     ############ **\n"+
		"     #  ##  ##  # **\n"+
		"     #  ##  ##  # **\n"+
		"       %   +   -  **\n"+
		"       %   +   -  **\n"+
		"    $$$3$$$4$$$5  **\n"+
		"    $   %  ++  $  **\n"+
		"    $   %   +  $  **\n"+
		"   @1@@@%^^^2  $$ **\n"+
		"   @$  @%^  ^   $ **\n"+
		"   @$  @%^  ^   $ **\n"+
		"  #  ##  ##  ##  #**\n"+
		"  #  ##  ##  ##  #**\n"+
		"  ################**\n"+
		"  s   s   s  s    **\n"+
		"                   *\n"+
		"*                   *\n"+
		"**********************";
			
		}
		int 	bX,bY,temp;
	

		
		if (size >1) {
		 	bX=10;
		 	bY=10;
		  temp=bX;
		   
		}
		else {
				
			 	int var_X= walls.get(0).getX();
			 	
			 	bX=walls.get(0).getX()+15;
			 	if (var_X == 250)
			 		bX=walls.get(0).getX()+25;
			 	if (var_X == 500)
			 		bX=walls.get(0).getX()+35;
			 	if(defaultScreen)
			 		bY=walls.get(0).getY()-220;
			 	else 
			 		bY=walls.get(0).getY()-190;
			 temp=bX;
			
		}
			
		Brick b ;
		int countS=0, counts=0;
		 String []  S= {"rootNS;10.1.1.10;T","NS;secnet.com;10.1.4.40;T", "HTTPS;secnet.com;10.1.4.180;T"};
		 String []  s= {"Victim PC;10.1.2.5;B","Victim NS;10.1.2.20;B","NS;example.com;10.1.3.30;B", "AttackerHTTPS;xxx.example.com;10.1.3.55;B"};
		


		for (int i = 0; i < DNS_CACHE_POISONING.length(); i++) {
			

				char item = DNS_CACHE_POISONING.charAt(i);

				if (item == '\n') {
					bY += bricksize;
					bX=temp;
					
					
				} else if (item == '#') {

					b = new Brick(this.roomID,bX, bY,"/wall_sq.png","W",true,"no",false);
					ret.add(b);
					if (size==1)
						trapBrick.add(b);
					bX+=bricksize;
					
				}  
				else if (item == '@') {
					
					b = new Brick(this.roomID,bX, bY,"/line.png","L1",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				}  
				else if (item == '^') {
					b = new Brick(this.roomID,bX, bY,"/line.png","L2",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				}  

				else if (item == '$') {
					b = new Brick(this.roomID,bX, bY,"/line.png","L3",true,"no",false);
					ret.add(b);
					if(!stop_write_L6)
						ret.add(new Brick(this.roomID,bX, bY,"/line.png","L6",false,"no",false));
					bX+=bricksize;
				}
				else if (item == '+') {
					b = new Brick(this.roomID,bX, bY,"/line.png","L4",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				}  
				
				else if (item == '-') {
					b = new Brick(this.roomID,bX, bY,"/line.png","L6",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				}  

				else if (item == '%') {
					b = new Brick(this.roomID,bX, bY,"/line.png","L5",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
					//System.err.println("%%%");
				}  
				else if (item == '1' )  {
					b = new Brick(this.roomID,bX, bY,"/line.png","L3",true,"no",false);
					ret.add(b);
					ret.add(new Brick(this.roomID,bX, bY,"/line.png","L6",false,"no",false));
					b = new Brick(this.roomID,bX, bY,"/line.png","L1",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				}  
				else if (item == '2' ) {
					b = new Brick(this.roomID,bX, bY,"/line.png","L2",true,"no",false);
					ret.add(b);
					b = new Brick(this.roomID,bX, bY,"/line.png","L4",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				} 
				else if (item == '3' ) {
					b = new Brick(this.roomID,bX, bY,"/line.png","L3",true,"no",false);
					ret.add(b);
					ret.add(new Brick(this.roomID,bX, bY,"/line.png","L6",false,"no",false));
					b = new Brick(this.roomID,bX, bY,"/line.png","L5",true,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				}  
				else if (item == '4' ) {
					b = new Brick(this.roomID,bX, bY,"/line.png","L3",true,"no",false);
					ret.add(b);
					ret.add(new Brick(this.roomID,bX, bY,"/line.png","L6",false,"no",false));
					b = new Brick(this.roomID,bX, bY,"/line.png","L4",true,"no",false);
					ret.add(b);
					loadNextNetTask = new ImageHolder(this.getRoomName(),b.getX()+10,b.getY()+10,30,30,"/nextlevel.png","NEXTLEVEL",false);
					if(size==1)
						taskRoomImg.add(loadNextNetTask);
					bX+=bricksize;
				}  
				else if (item == '5' ) {
					b = new Brick(this.roomID,bX, bY,"/line.png","L3",true,"no",false);
					ret.add(b);
					ret.add(new Brick(this.roomID,bX, bY,"/line.png","L6",false,"no",false));
					stop_write_L6=true;
					bX+=bricksize;
					
				}  
				else if (item == 'S') {
					b = new Brick(this.roomID,bX, bY+5,"/wall_sq.png",S[countS],false,"no",false);
					ret.add(b);
					bX+=bricksize;
					countS++;
					
					
				}  
				else if (item == 's' ) {
					b = new Brick(this.roomID,bX, bY+10,"/wall_sq.png",s[counts],false,"no",false);
					ret.add(b);
					bX+=bricksize;
					counts++;
					
				}  
				else if (item == 'L') {
					b = new Brick(this.roomID,bX, bY,"/legend.png","HIDEN",false,"no",false);
					ret.add(b);
					bX+=bricksize;
					
				}  

				else if (item == ' ') {
					bX +=bricksize;
					
				}
			


		}
		return ret;
	}
	
	/*
	 * reposition the card based on server positioning in the layout given 
	 * 
	 */
	
	public void set_locality_attched_net_card(NetworkCard net , boolean newMatrix, boolean defaultScreen) {
		int bx=0,tx=0;
		if(defaultScreen) {
			bx=20;
			tx=-10;
		}
		boolean found=false;
		for (Brick b : dns_Cache) {
			if(!b.draw && !b.operation.equals("HIDEN")) {
				String [] s=b.operation.split(";");
				for (int i=0;i<s.length;i++)	{
					if(s[i].equals(net.IPaddress)) {
							if(s[s.length-1].equals("B")){
								net.setX(b.getX()-20+bx);
								net.setY(b.getY()-60);
							}else {
								net.setX(b.getX()+10+tx);
								net.setY(b.getY()+10);
							}
							net.setRoom(this);
							if(!newMatrix)
								getRefRoomUI().netCard.add(net);
							found=true;
					}
				}
			}
			if(found) {
				servers.add(net);
				if(numberOfNetworkMounted <7)
					numberOfNetworkMounted++;
				return;
			}
		}
	}
		
	
	public Brick getServerPox(String IPaddress ){
		Brick [] b;
		String [] split;
			b=getListServer(6);
			for (int i=0; i< b.length;i++) {
				split=b[i].operation.split(";");
				if(split[1].equals(IPaddress))
					return b[i];
			}
	
		return null;
		
	}
	
	public Brick[] getListServer(int numberOfServer){
		Brick [] brick = new Brick[numberOfServer];
		int i=0;
		for (Brick b : dns_Cache) {
			if(!b.draw && !b.operation.equals("HIDEN")) 
				brick[i]=b;
			i++;
			
		}
		return brick;
	}
	

	public ImageHolder checkImageHolderCollosion(int mouseXCoord, int mouseYCoord) {

		for (int k=0; k<taskRoomImg.size();k++) {
			//System.out.println(k);
			ImageHolder imgHolder=taskRoomImg.get(k);

			for (int i=imgHolder.getX(); i<imgHolder.getX()+imgHolder.getW();i++) {
				for (int j =imgHolder.getY(); j<imgHolder.getY()+imgHolder.getH();j++) {

					if(mouseXCoord==i && mouseYCoord==j) {
						if(taskRoomImg.get(k).draw) {
							taskRoomImg.get(k).setDrawOpacity(true);
							return imgHolder;
						}
					}
				}
			}
		}

		return null;
	}
	
	
	public void setImgHolderOpacity(ImageHolder img, boolean val) {

		for (int i=0; i< taskRoomImg.size();i++) {
			if(taskRoomImg.get(i).equals(img)) {
				taskRoomImg.get(i).setDrawOpacity(val);
				return;
			}

		}
	}

	private Brick getFirstTopBrick() {
		int i=0;
		for (Brick b : this.walls) {
			if (b.roomID.equals(this.roomID) && b.operation.equals("L")) {
				if(i==1)
					return b;
				i++;
			}
		}
		return null;
	}
	public void updateImageHolderDrawField(String ClassID, boolean val) {
	//	System.out.println("class id ");
		for (int i=0; i< taskRoomImg.size();i++) {
			//System.out.println("class id " +taskRoomImg.get(i).operation +" size " +taskRoomImg.size());
			if(taskRoomImg.get(i).operation.equals(ClassID)) {
				taskRoomImg.get(i).draw=val;
				this.refRoomUI.repaint();
				return;
			}
		}
	}

	public void drawTaskRoomProperties(Graphics g ) {

		//if (this.imgBg!=null) {
			//			Brick b= getFirstTopBrick();
			//			g.drawImage(imgBg,b.getX()+15,b.getY()+15,refRoomUI);
			for (Brick b : this.dns_Cache) {
				if(b.draw) {
					g.drawImage(b.getImage(),b.getX(),b.getY(),refRoomUI);
				}else {
					if(!b.operation.equals("HIDEN")) {
					g.setFont(new Font("TimesRoman",Font.PLAIN, 10));
					g.setColor(Color.BLACK);
					String [] s=b.operation.split(";");
					int offset=3*s.length;
					
					//for (int i=0; i<s.length-1;i++) { 
					try {
							g.drawString(s[s.length-2], b.getX(), b.getY());
							
					} catch (java.lang.ArrayIndexOutOfBoundsException | java.lang.NullPointerException ex ) {}
					//}
						
				}
				}

			}
		//}
		for (ImageHolder img : this.taskRoomImg) {

			if(img.isDrawOpacity()) {
				float opacity = 0.5f;
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
				g.drawImage(img.getImage(),img.getX(),img.getY(),refRoomUI);

			}
			if(img.draw)
				g.drawImage(img.getImage(),img.getX(),img.getY()+10,refRoomUI);

		}

		if (taskCompleted) {
			g.setFont(new Font("TimesRoman",Font.BOLD, 16));
			g.setColor(Color.red);
			g.drawString("TASK COMPLETED" , this.getX()-200, this.getY()-125);
		}
	}



	public String getRoomName() {
		return roomName;
	}

	public void loadHint(){
		if(!lastTask){
			DisplayInformation disp =new DisplayInformation("~Display Task Discription~");
			if(getPlayer()!=null) 
				disp.aText.setText(taskDescription);
			
		}

	}
	public void loadTaskRoomComp(boolean defaultScreen) {
		taskRoomImg.clear();
		int x=0,y=0;
		if(defaultScreen) {
			x=this.getX()-190;
			y=this.getY()-200;
		}
		else {
			x=this.getX()-170;
			y=this.getY()-180;
		}
			
		hint = new ImageHolder(this.getRoomName(),x,y,30,30,"/info.png","HINT",true);
		taskRoomImg.add(hint);
		if(taskID.equals("wirelessAttack")){
			loadWireLessTask();

		}

		if(taskID.equals("socialMedia"))
			loadSocialMediaTask();

		if(taskID.equals("networkSecurity"))
			loadNetworkAnaliticTask(defaultScreen);


		//setVisible(true);
	}


	public void loadSocialMediaTask(){
		phone = new ImageHolder(this.getRoomName(),this.getX()-125, this.getY()-150,50,80,"/phone.png", "PHONE", true);
		taskRoomImg.add(phone);
	}

	public void setUPSosMediaDB(String op){

		if(op.equals("map1")){
			mapQuizTSR.clear();
			keys=loadMapQuiz(mapQuizTSR,"/socialMedia.txt");
			sosMedia.setMapKey1(mapQuizTSR, keys);
		}

		if(op.equals("map2")){
			mapQuizTSR.clear();
			keys=loadMapQuiz(mapQuizTSR,"/socialMedia2.txt");
			sosMedia.setMapKey2(mapQuizTSR, keys);
		}
		if(op.equals("map3")){
			mapQuizTSR.clear();
			keys=loadMapQuiz(mapQuizTSR,"/socialMedia3.txt");
			sosMedia.setMapKey3(mapQuizTSR, keys);
		}

	}
	public void loadNetworkAnaliticTask(boolean defaultScreen){
			if (initArrayList(1,defaultScreen)!=null) {
				this.dns_Cache=initArrayList(1,defaultScreen);
				
			}
	}


	public void loadWireLessTask(){
		wireLess1 = new ImageHolder(this.getRoomName(), this.getX()-150, this.getY()-140,147,34, "/ssid_descovery.png", "WL1", false);
		wireLess2 = new ImageHolder(this.getRoomName(),this.getX()-150, this.getY()-90,147,34, "/deauth.png", "WL2", false);
		wireLess3 = new ImageHolder(this.getRoomName(),this.getX()-150, this.getY()-40,147,34, "/crack_pwd.png", "WL3", false);

		taskRoomImg.add(wireLess1);
		taskRoomImg.add(wireLess2);
		taskRoomImg.add(wireLess3);


	}
	public void fireSosMedEvent() {
//		ta = new JTextArea();
//		ta.setText("");
		welcome="Welcome To Social Media simulation \n"
				+ "Use Password crakcer o unlock the Phone  \n"
				+ " and Press Decode button  !!!";

		congrat="Congratualation you succesfully gain monitoring access to SSID information \n"
				+ "next task is to deauthenticated user from wireless acess point and capture the \n"
				+ "deauthentication packet for used in AIREPLAY-NG tool";
		ta.setText(welcome);
		if(getPlayer()!=null && !taskCompleted) {
			index=0;
			sosMedia= new SosMediaUI(getPlayer(),p,scroll, ta, l1, welcome, "","Please Press Enter",false);

		}
	}
	public void fireWLEvent(String wireless){
		

		if(wireless.equals("WL1")) {
			if(getPlayer()!=null && !taskCompleted) {
				index=0;
				welcome="Welcome To wireless attacks simulation \n"
						+ "to Start simulates the AIRMOM/AIRODUMP-NG  Press enter on text field bellow \n"
						+ "to run the comand and output will be displayed on screen  !!!";

				congrat="Congratualation you succesfully gain monitoring access to SSID information \n"
						+ "next task is to deauthenticated user from wireless acess point and capture the \n"
						+ "deauthentication packet for used in AIREPLAY-NG tool \n \n";
				
				keys=loadMapQuiz(mapQuizTSR,"/airmonDumpScenario.txt");
				ta.setText(welcome);
				task= new TextInteraction(p,scroll, null, tf, null,null, welcome, "","Please Press Enter",false);
				wl1=true;
				updateImageHolderDrawField(wireLess1.operation, false);

			}

		}

		if(wireless.equals("WL2")) {
			if(getPlayer()!=null && !taskCompleted) {
				if (!wl1)
					refRoomUI.refMMUI.errorMsgDisplay(" To run 'Deauthentication' !!! \n First you must run SSID Discovery ...",getPlayer().playerID,false);
				else{
					
					index=0;
					welcome="Welcome To wireless attacks simulation second step \n"
							+ "to Start simulates the AIREPLAY-NG Deauthentication Tools Press enter on text field bellow \n"
							+ "to run comand and output will be displayed on screen  !!!";
					congrat="Congratualation you succesfully Deautenticated user in wireless access point \n"
							+ "next task is to crack access point password utilising the deauthentication packet with \n"
							+ "AIRCRACK-NG Tool \n \n";
					mapQuizTSR.clear();
					ta.setText(welcome);
					keys=loadMapQuiz(mapQuizTSR,"/deautentication.txt");
					task= new TextInteraction(p,scroll, null, tf, null,null, welcome, "","Please Press Enter",false);
					wl2=true;

					updateImageHolderDrawField(wireLess2.operation, false);


				}
			}

		}



		if(wireless.equals("WL3")) {
			if(getPlayer()!=null && !taskCompleted) {
				if (!wl2)
					refRoomUI.refMMUI.errorMsgDisplay(" To run 'Cracks Passwd' !!! \n First you must run SSID Discovery and Deauthentication...",getPlayer().playerID,false);
				else{
					index=0;
					welcome="Welcome To wireless attacks simulation final step \n"
							+ "to Start simulates the AIRCRACK-NG Tool Press enter on text field bellow \n"
							+ "to run comand and output will be displayed on screen  !!!";
					congrat="Congratualation mission complete, now you have successfuly access the wireless \n \n";
					mapQuizTSR.clear();
					ta.setText(welcome);
					lastTask=true;
					keys=loadMapQuiz(mapQuizTSR,"/crackPasswd.txt");
					task= new TextInteraction(p, scroll,  null, tf, null,null, welcome, "","Please Press Enter",false);

					updateImageHolderDrawField(wireLess3.operation, false);



				}
			}

		}


	}



	public void fireDNSCachePoisoning() {
		JPanel p = new JPanel();
		JTextArea ta = new JTextArea();
		JLabel l1 = new JLabel();
		String wel= 	"1. To run the simulation first click on Run DNS Spoof button. \n"
				+ "This will enable DNSspoof running in background while waiting for \n" 
				+ "incoming ns query for xxx.example.comn \n \n"
				+ " 2. To use the server or PC click on the PC or server this will allow you \n \n"
				+ "enter to selected server or pc. \n \n"
				+ "From here use input command box for inputing command. \n \n"
				+ "For example ping, pwd, ls and cat  \n \n"
				+ "3. Ping the attacker legitimate web address xxx.example.com.\n \n"
				+ "the attacker DNS will reply legitimate NS record for xxx.example.com \n \n"
				+ "but also make query request and reply for secnet.com domain  \n \n"
				+ "4. after some time the dns cache poisoning eventually succeed  \n \n"
				+ "5. from PC victim ping secnet.com and verify reply is IP address\n \n"
				+ "of attacker PC \n";
			
			
		DNSCacheUI panel = new DNSCacheUI(this.getPlayer(),p, ta, wel, "","Please Press Enter",false);
		panel.set_DNS_Cache(initArrayList(2,true));
		panel.run_DNS_Cache(panel);
		
	}

}


