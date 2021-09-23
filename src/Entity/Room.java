package Entity;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;


import GameUI.MMUI;
import GameUI.RivalAnalyser;
import GameUI.RoomUI;






@SuppressWarnings("serial")
public class Room extends RootEntity {

	private static final int ITEMDIMSIZE = 40;
	private static final int NOITEMROOM = 8;


	private static final int DOORSIZE=4;

	public Vector<Player> playerInRoom;
	public Vector<ItemCard> itemCardInRoom;
	public Vector<ItemCard> itemCardInGlobalRoom;
	public Vector<ItemCard> deletedItemCardInRoom;
	public Vector<ItemCard> shuffleComp;
	public Vector<ItemCordinate> itemCord;
	public ArrayList<Brick> walls;
	public ArrayList<NextRoom> listNextRoom;

	public Map<String, String> adjecentRoom;

	/*
	 * roomID is a primary key for each room. used to define room as unique key attribute compare to rest 
	 * of the room. for example TSRWireLess stand for  task room wireless attack 
	 * following are predefine roomID especially for task room 
	 * 		tsrwireless , tsrsosmedia, tsrsecanalisis  
	 * 		TRR1, TRR2, TRR3
	 * 		RS1,RS2,RS3,RS3
	 * 
	 */
	public  String  roomID;


	Color color;
	protected RoomUI refRoomUI;
	protected String displayItem;

	// wall constraint 
	boolean w,es,n,s;


	/*
	 * for Multiplayer option
	 */


	public boolean  E_door, W_door, N_door, S_door;
	public final int BRICKSIZE=10;
	public int roomLength=0;
	public int roomDimension=0;
	public int SHUFFLE=0, shuffle_counter=0;
	boolean shuffle_init=false;
	public boolean roomIDS; 

	//Animate the detection method
	int animateXDiameter=20, animateYDiameter=20;
	boolean grow;

	// scheduler variable 
	ScheduledExecutorService scheduler;




	public Room(int x, int y, String roomID, boolean E_door, boolean W_door, boolean N_door, boolean S_door, Color color,Player aPlayer, RoomUI refRoomUI) {
		super(x,y);

		this.roomID=roomID;
		this.E_door=E_door;
		this.W_door=W_door;
		this.N_door=N_door;
		this.S_door=S_door;
		displayItem="";
		this.color=color;
		roomIDS=false;
		this.refRoomUI=refRoomUI;
		this.playerInRoom = new Vector<Player>();
		this.itemCardInRoom=new Vector<ItemCard>();
		shuffleComp=new Vector<ItemCard>();
		walls=new ArrayList<Brick>();
		listNextRoom= new ArrayList<NextRoom>();
		this.deletedItemCardInRoom = new Vector<ItemCard>();
		itemCord=new Vector<ItemCordinate>();
		scheduler = Executors.newScheduledThreadPool(10);

		if (aPlayer != null) {
			aPlayer.setCurrentRoom(this);
			playerInRoom.add(aPlayer);
		}

	}
	public static int getNoitemroom() {
		return NOITEMROOM;
	}

	public String getRoomID() {
		return roomID;
	}

	public void reshuffleCards(RoomUI refUI, boolean netMSG, String [] itemID) {
		Player player = null;
		List <ItemCard> playerItemList = null;
		if (getPlayer() != null) {
			player=getPlayer();
			playerItemList=player.getPlayerItemList();
		}
		else 
			return;

		shuffle_counter=0;
		List <ItemCard> tempItems = new ArrayList<ItemCard>();
		if (getPlayer()!=null)
			SHUFFLE=getPlayer().SHUFFLE;

		for (Room room : refUI.roomsDefault) {
			room.itemCardInRoom.clear();
			room.deletedItemCardInRoom.clear();

			for (ItemCard item : room.shuffleComp) {

				if (!playerHoldCard(item, playerItemList,player)) 
					tempItems.add(item);

			}
			//System.out.println(room.roomID + " item size  " +tempItems.size());
			if(tempItems.size() > 0)
				room.loadRoom( auxMethod(tempItems),   refUI);
			tempItems.clear();
		}


		SHUFFLE++;
		if (getPlayer()!=null)
			getPlayer().SHUFFLE=SHUFFLE;


	}

	private ItemCard []  auxMethod(List <ItemCard> itemcards) {
		ItemCard [] ret = new ItemCard[itemcards.size()];
		ItemCard item;
		for (int i=0; i< itemcards.size();i++) {
			item=itemcards.get(i);
			item.setForegroundImg(item.getForegroundImg());
			item.dropPlayercard=false;
			ret[i]=item;
		}
		return ret;
	}

	private boolean playerHoldCard(ItemCard item,List<ItemCard> playerItemCards, Player player) {

		if(refRoomUI.card_attached_to_roomUI.size() >0)
			for (int k=0;k< refRoomUI.card_attached_to_roomUI.size();k++) {
				if (item.cardId.equals(refRoomUI.card_attached_to_roomUI.get(k))) {
					//System.out.println("found the item ");
					return true;
				}
			}

		if(playerItemCards.size()==0 && player.team_players_Items.size() ==0)
			return false;
		else {
			//System.out.println("player.getPlayerItemList().size() " +player.team_players_Items.size() + " fucking item " +item.cardId);
			for (int i=0; i < playerItemCards.size();i++) {
				if (item.cardId.equals(playerItemCards.get(i).cardId)) {
					return true;	
				}
			}
			if(player.team_players_Items.size()>0)
				for (int j=0;j< player.team_players_Items.size();j++) {

					if (item.cardId.equals(player.team_players_Items.get(j).cardId)) {
						return true;
					}
				}



		}


		return false;
	}

	private void updateRoomUIItemCard(ArrayList <ItemCard> roomUIItem) {
		boolean add=true;
		for (ItemCard item : itemCardInRoom) {
			for(ItemCard it2 : roomUIItem) {
				if(item.cardId.equals(it2.cardId)) {
					add=false;
					break;
				}
			}
			if(add)
				roomUIItem.add(item);
		}
	}
	public ArrayList<NextRoom> getListNextRoom() {
		return listNextRoom;
	}
	public RoomUI getRefRoomUI() {
		return refRoomUI;
	}

	public void setRefRoomUI(RoomUI refRoomUI) {
		if(refRoomUI !=null)
			//		System.err.println( "setup room UI  " + refRoomUI.getClass());
			this.refRoomUI = refRoomUI;
	}


	public ItemCard checkItemCarCollosion(int mouseXCoord, int mouseYCoord, String BGorFG) {
		if (itemCardInRoom.size() == 0)
			return null;
		for (int k=0; k<itemCardInRoom.size();k++) {
			ItemCard item=itemCardInRoom.get(k);
			int itemX=0, itemY=0;;

			if (BGorFG.equals("BG")) {
				itemX=item.getX()+item.WBG;
				itemY=item.getY()+item.HBG;
			}
			else {
				itemX=item.getX()+item.WFG;
				itemY=item.getY()+item.HFG;
			}


			for (int i=item.getX(); i<itemX;i++) {
				for (int j =item.getY(); j<itemY;j++) {
					if(mouseXCoord==i && mouseYCoord==j) {
						return item;
					}
				}
			}
		}

		return null;
	}

	public String split(String text, boolean op){

		String [] split=null;
		try{
			split = text.split("!:!");
		}catch (java.lang.NullPointerException ex ){}

		String ret="";

		for (String temp: split){
			if (op)
				ret+=temp+"\n";
			else 
				ret+=temp+"\n";
		}

		return ret;	
	}


	public Map<String, String> findAdjecentRoom( String [][] roomDesign, String roomName){

		Map<String, String> map = new HashMap<String, String>();

		int x=0,y=0;

		for ( int row = 0; row < roomDesign.length; row ++ )
			for ( int col = 0; col < roomDesign.length; col++ ){
				if (roomDesign[row][col].equals(roomName)){
					x=row;
					y=col;
					break;
				}
			}


		// case North node (i,j+1)         
		if (y+1 < roomDesign.length)   
			map.put("N", roomDesign[x][y+1]);
		if (y+1 >= roomDesign.length)    
			map.put("N", "DEADEND");

		// case South (i,j-1) 
		if (y-1 < 0)  
			map.put("S", "DEADEND");  
		if (y-1 >= 0)  
			map.put("S", roomDesign[x][y-1]);

		//case East node (i+1,j) 
		if (x+1 <roomDesign.length) 
			map.put("E", roomDesign[x+1][y]);
		if (x+1 >= roomDesign.length)    
			map.put("E", "DEADEND");

		// case West node (i-1,j)  
		if (x-1 <0) 
			map.put("W", "DEADEND");
		if (x-1 >= 0)    
			map.put("W", roomDesign[x-1][y]);



		return map;
	}

	public String [] loadMapQuiz(Map<String, String> mapQuiz, String path){
		String [] temp = new String[100];;
		java.net.URL fileURL = Room.class.getResource(path);
		System.out.println("path loadmapquiz : " +Room.class.getResource(path));
		boolean answer=false;
		String ans="none", quest="";
		int i=0;
		boolean optionSplit=false;

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileURL.getPath()));
			if(path.equals("/questionDB.txt"))
				optionSplit=true;


			for (String line; (line = br.readLine()) != null;) {

				if(answer){
					if(ans.equals("none")!=true){
						mapQuiz.put(ans, quest);
						temp[i]=ans;
						ans="none";
						quest="";
						i++;

					}

					ans=line;  

				}

				if( line.equals(";")!=true)
					if(!answer)
						quest = quest + split(line,optionSplit)+"\n";



				if(line.equals(";")){
					answer=true;
				}
				else
					answer=false;

			}
			br.close();
		}catch (IOException e) {

		}

		return temp;


	}

	private void emptyWalls() {
		if (walls.size()==0)
			return;
		if(this instanceof TrapRoom) {
			TrapRoom r = (TrapRoom) this;
			r.trapsWallImg.clear();

		}
		walls.clear();

	}
	private void emptyNextRoom() {
		if (listNextRoom.size()==0)
			return;
		listNextRoom.clear();

	}

	private NextRoom findNextRoom(String roomID) {
		for(NextRoom nextRoom : listNextRoom) {
			if (nextRoom.roomID.equals(roomID))
				return nextRoom;
		}
		return null;
	}

	public void roomDoorAccessConfig(int no_of_room, String roomID, boolean netMsg) {
		boolean executed;

		for (int i=0; i<no_of_room;i++) {
			executed=false;
			for(int j=1;j<5;j++) {
				NextRoom room=findNextRoom(roomID);
				switch (j) {
				case 1: if(this.W_door)
				{ 
					this.W_door=false;
					aux_center_access_door_config(roomID,"L",true);
					executed=true;
				}
				break;
				case 2: if(this.E_door)
				{	
					this.E_door=false;
					aux_center_access_door_config(roomID,"R",true);
					executed=true;
				}
				break;
				case 4: if(this.N_door) 
				{	
					this.N_door=false;
					aux_center_access_door_config(roomID,"T",true);
					executed=true;
				}
				break;
				case 3: if(this.S_door) 
				{	
					this.S_door=false;
					aux_center_access_door_config(roomID,"B",true);
					executed=true;
				}
				break;
				}
				if(executed)
					break;
			} 

		}

		if(!netMsg){
			int id =this.getPlayer().playerID;
			String msg= "15;"+Integer.toString(id)+";"+Integer.toString(no_of_room)+";"+roomID;
			this.refRoomUI.send_network_message(msg);
			
		}
	}
	public void aux_center_access_door_config(String roomID, String direction, boolean block) {
		for (Brick brick : walls) {
			if(brick.roomID.equals(roomID) && brick.operation.equals(direction) && brick.center.equals("yes")) {
				if (block) {
					brick.draw=true;
					brick.nextDoorAccess=true;
				}
				else {
					brick.draw=false;
					brick.nextDoorAccess=false;
				}
			}

		}

	}
	private void roomDoorSetup(boolean door, String doorDirection, String ID, int x, int y, String filepath1,String filepath2,String doorFalsePic, String nextroom, String opNextDoor,String opBrick, boolean draw ) {
		if (this instanceof TrapRoom)
		{
			nextroom=adjecentRoom.get(doorDirection);
			listNextRoom.add(new NextRoom(ID,x,y,filepath1,nextroom,opNextDoor,draw)); 
		}
		else {
			if (door) {
				nextroom=adjecentRoom.get(doorDirection);
				listNextRoom.add(new NextRoom(ID,x,y,filepath1,nextroom,opNextDoor,draw)); 
				Brick brick=new Brick(ID,x,y,doorFalsePic,opBrick,false,"yes",false);
				brick.center="yes";
				//System.out.println(brick.center);
				walls.add(brick); 
			}
			else {
				Brick brick=new Brick(ID,x,y,doorFalsePic,opBrick,draw,"yes",false);
				brick.nextDoorAccess=true;
				brick.center="yes";
			//	System.out.println(brick.center);
				walls.add(brick); 
			}
		}

	}
	//	// make sure room dimension match default matrix [3][3] before call this method
	public void roomSetUp(int roomsize, int XCoordinate, int YCoordinate,int offsetX,int offsetY, int dimension, String roomID) {
		//System.out.println(XCoordinate + "" + YCoordinate+ " "+offsetX +" "+ offsetY );
		emptyWalls();
		emptyNextRoom();
		roomDimension=dimension;
		int centerPoint= (dimension/(BRICKSIZE*2))-1;
		int counterX =0, counterY=0;
		int RIGTHLEFT=YCoordinate-dimension,TOPBOT=XCoordinate-dimension;
		String N = null,S=null,E=null,W=null;

		roomLength=dimension;
		if(roomsize==3)
			adjecentRoom =findAdjecentRoom(refRoomUI.roomDesignDefault, roomID);
		else 
			adjecentRoom =findAdjecentRoom(refRoomUI.roomDesignGlobal, roomID);
		//System.err.println( " room name " + roomID +" "+""+adjecentRoom.get("N")+ " "+adjecentRoom.get("S")+ " "+ adjecentRoom.get("W")+ " "+adjecentRoom.get("E"));
		int index=0;
		String ID=this.roomID;

		while( TOPBOT < XCoordinate+offsetX) {
			if (counterX == centerPoint) {
				for (int i=0; i<DOORSIZE;i++) {

					roomDoorSetup(this.N_door,"N",ID, TOPBOT,  YCoordinate+offsetY, "/roomcon_hori.png","/wall_hori.png","/silver_jori.png",N,"N","T",true);
					roomDoorSetup(this.S_door,"S",ID, TOPBOT,  YCoordinate+offsetY-dimension, "/roomcon_hori.png","/wall_hori.png","/silver_hori.png",S,"S","B",true);

					counterX++;
					TOPBOT=TOPBOT+BRICKSIZE;
				}
			}
			else {
				walls.add(new Brick(ID,TOPBOT,YCoordinate+offsetY,"/wall_hori.png","T",true,"no",false)); // top boundary 
				walls.add(new Brick(ID,TOPBOT,YCoordinate+offsetY-dimension,"/wall_hori.png","B",true,"no",false)); 
			}

			TOPBOT=TOPBOT+BRICKSIZE;
			counterX++;
		}

		while(RIGTHLEFT < YCoordinate+offsetY) {

			if (counterY == centerPoint) {
				for (int i=0; i<DOORSIZE;i++) {
					roomDoorSetup(this.W_door,"W",ID, XCoordinate+offsetX-dimension, RIGTHLEFT, "/roomcon_hori.png","/wall_ver.png","/silver_ver.png",W,"W","L",true);
					roomDoorSetup(this.E_door,"E",ID, XCoordinate+offsetX ,  RIGTHLEFT, "/roomcon_hori.png","/wall_ver.png","/silver_ver.png",E,"E","R",true);

					counterY++;
					RIGTHLEFT+=BRICKSIZE;
				}

			}
			else {
				walls.add(new Brick(ID,XCoordinate+offsetX ,RIGTHLEFT,"/wall_ver.png","R",true,"no",false));	
				walls.add(new Brick(ID, XCoordinate+offsetX-dimension,RIGTHLEFT,"/wall_ver.png","L",true,"no",false)); // left boundary// right boundary 
			}

			RIGTHLEFT=RIGTHLEFT+BRICKSIZE;
			counterY++;
		}	

		if(getPlayer() !=null) {
			// magic number 10 
			getPlayer().setX(this.getBrick("B",true,walls).getX()+20);
			getPlayer().setY(this.getBrick("B",true,walls).getY()+20);

		}

		//	System.err.println(" door accesss TRAP ROOM "+  this.S_door);
		populateRoomUIWalls();
		populateRoomUINextRoom();
		//	System.err.println("listNextRoom size dgsfggfgsfgfdgsdgfgsgfhshhsshhhsfsfhs room name "+ roomID +listNextRoom.size());
		if (this instanceof TrapRoom) {
			TrapRoom room = (TrapRoom) this;
			//	System.err.println(" door accesss TRAP ROOM "+  room.S_door);
			room.populateRoomUITraps();
			room.loadTrapRoom();

		}

	}


	public void roomSetUpRivalInterface(int roomsize, int XCoordinate, int YCoordinate,int offsetX,int offsetY, int dimension, String roomID, RivalAnalyser panel) {
		//	System.err.println("rrrrrrrrr "+XCoordinate + "" + YCoordinate+ " "+offsetX +" "+ offsetY );
		roomDimension=dimension;
		int centerPoint= (dimension/(BRICKSIZE*2))-1;
		int counterX =0, counterY=0;
		int RIGTHLEFT=YCoordinate-dimension,TOPBOT=XCoordinate-dimension;
		String N,S,E,W;

		roomLength=dimension;

		adjecentRoom =findAdjecentRoom(panel.roomDesign, roomID);
		//System.out.println( " room name " + roomID +" "+""+adjecentRoom.get("N")+ " "+adjecentRoom.get("S")+ " "+ adjecentRoom.get("W")+ " "+adjecentRoom.get("E"));
		int index=0;
		String ID=this.roomID;

		while( TOPBOT < XCoordinate+offsetX) {
			panel.walls.add(new Brick(ID,TOPBOT,YCoordinate+offsetY,"/wall_hori.png","T",true,"no",false)); // top boundary 
			panel.walls.add(new Brick(ID,TOPBOT,YCoordinate+offsetY-dimension,"/wall_hori.png","B",true,"no",false)); 
			TOPBOT=TOPBOT+BRICKSIZE;
			counterX++;
		}

		while(RIGTHLEFT < YCoordinate+offsetY) {
			panel.walls.add(new Brick(ID,XCoordinate+offsetX ,RIGTHLEFT,"/wall_ver.png","R",true,"no",false));	
			panel.walls.add(new Brick(ID, XCoordinate+offsetX-dimension,RIGTHLEFT,"/wall_ver.png","L",true,"no",false)); // left boundary// right boundary 
			RIGTHLEFT=RIGTHLEFT+BRICKSIZE;
			counterY++;
		}	
	}

	public int getRoomDimension() {
		return roomDimension;
	}


	public void populateRoomUIWalls() {

		for (Brick brick : walls) {
			refRoomUI.roomUIWall.add(brick);
		}
	}

	public void populateRoomUINextRoom() {

		for (NextRoom room : listNextRoom) {
			refRoomUI.roomUINextRoom.add(room);
		}
	}

	public void populateRoomUIItems( RoomUI roomUI) {
		roomUI.roomUIItem.clear();
		for (ItemCard item : itemCardInRoom) {
			roomUI.roomUIItem.add(item);
		}
	}

	public int [] randGen(int size){
		int [] temp =new int [size];

		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=0; i<size; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);

		for (int i=0; i<size; i++) {
			temp [i]=list.get(i);
		}

		return temp;
	}

	public Player getPlayer() {

		for (Player player : playerInRoom) {
			if (player.self)
				return player;

		}
		return null;
	}

	public void drawItemName(Graphics g, String msgToDraw, int x, int y) {
		//System.err.println("shuffle size " + SHUFFLE + " counter " +shuffle_counter);

		if(getPlayer().SHUFFLE >3 || thresholdDrawItem(getPlayer().SHUFFLE) < 0)
			return;
		else {
			if(thresholdDrawItem(SHUFFLE) == 0)
				g.drawString(msgToDraw, x, y);
			else if(shuffle_counter <= thresholdDrawItem(getPlayer().SHUFFLE)) {
				g.drawString(msgToDraw, x, y);
				shuffle_counter++;
			}

		}


	}


	private int thresholdDrawItem(int shuffle) {
		// 8 is the max number cards in the room 
		// 1 show 6 card , 2 show 4 and 3 show 2 only 

		if (shuffle==0)
			return 0;
		else 
			return ((8 - shuffle*2)*2)+1;

	}
	public void drawPlayer(Graphics g, Player player) {
		player.drawBall(g);
	}
	public void drawRoomProperties(Graphics g) {
		// Draw brick   
		for (Brick brick : walls) {
			if(brick.draw)
				g.drawImage(brick.getImage(), brick.getX() , brick.getY(),refRoomUI);
		}		


		//Draw item card 
		if (itemCardInRoom.size()>0) {
			for (ItemCard item : itemCardInRoom) {
				g.drawImage(item.getImage(), item.getX() , item.getY(),refRoomUI);
			}
		}


		if(!this.roomID.equals("TSR2") ) {
			if(g==null)
			return ;
			
			g.setFont(new Font("TimesRoman",Font.PLAIN, 12));
			g.setColor(Color.BLACK);
			try {
				g.drawString(roomID,getX()-30, getY()-(roomLength-45));
			} catch  (java.lang.NullPointerException ex ) {
				
				
			} 
		}

		if (playerInRoom.size()>0) {
			for (Player player : playerInRoom) {
				//if(!player.self)
				player.drawBall(g);
			}
		}

	}
	public void IDSDetection(Graphics g) {
		
		if(roomIDS &&  this.getPlayer()!=null) {

			int LOOPS_PER_SECOND = 100;

			ScheduledFuture<?> animationHandler = scheduler.scheduleAtFixedRate(() -> {
				//System.out.println("animation is running ");
				animateDrawOval();
				if(getPlayer().instrusionDetectorRoom < 2)
					color=new Color(255,255,0,50);
				else
					color=new Color(255,0,0,50);
				g.setColor(color);
				g.fillOval(getBrick("B",true,walls).getX()-20, getBrick("B",true,walls).getY()+20, animateXDiameter, animateYDiameter);
				refRoomUI.repaint();

			},1, 2000/LOOPS_PER_SECOND, TimeUnit.MILLISECONDS);

			scheduler.schedule(new Runnable() {
				public void run() {
					animationHandler.cancel(true);
				}
			},  1, TimeUnit.SECONDS);
		}

//		else {
//					//	scheduler.shutdownNow();
//			drawRoomProperties(g);
//		}





	}

	public void animateDrawOval() {

		if (animateXDiameter >= 200) {
			grow = false;
		}
		if (animateXDiameter <= 20) {
			grow = true;
		}

		if (grow) {
			animateXDiameter += 3;
			animateYDiameter += 3;
		} else {
			animateXDiameter -= 3;
			animateYDiameter -= 3;
		}
	}

	public List<Player> getListPlayerInRoom(){
		return playerInRoom;
	}

	public boolean validateMoveToRoom(Room nextRoom) {
		return true;
	}

	public void addPlayerToRoom(Player aPlayer) {
		playerInRoom.add(aPlayer);

	}

	public void removePlayerFromRoom(Player aPlayer) {
		playerInRoom.remove(aPlayer);
	}

	public boolean isPlayerInRoom(String playerID) {
		for (Player player :playerInRoom ) {

			if(player.getPlayerName().equals(playerID))
				return true;
		}

		return false;
	}

	public void setVectorItemList(ItemCard [] itemsInRoom) {
		for (int i=0; i<itemsInRoom.length;i++) {
			itemCardInRoom.add(itemsInRoom[i]);
		}
	}

	public ItemCard getItemCardInRoom(String itemCardID , List<ItemCard> aList) {

		for (ItemCard item :aList ) {
			if (item.getCardIdClass().equals(itemCardID))
				return item;
		}
		return null;
	}

	public Brick getBrick( String operation, boolean seconBrick, List<Brick> bricks) {
		int countB=0, countT=0;
		for (Brick brick : bricks) {	
			if(brick.operation.equals(operation)) {
				if(seconBrick) {
					if(countT == 2) {
						return brick;
					}
					if (countB == 4 ) {
						return brick;
					}
					if(operation.equals("T"))
						countT++;
					else
						countB++;
				}else
					return brick;		
			}
		}
		return null;

	}
	
	public void loadRoom(ItemCard [] itemsInRoom,  RoomUI roomUI) {
		int splitItem, yDirection=0;
		int tX, tY,bX, bY;
		boolean removed = false;

		List<ItemCard> items = new ArrayList<ItemCard>();
		//if(!shuffle_init) 
		itemCardInRoom.clear();

		if (itemsInRoom.length > NOITEMROOM) {
			new Dialog(refRoomUI.getRefMMUI(), " Item Cards should not over more thann 8 items");
			//System.out.println("errro     rrrrr");
		}
		else {

			for (int i = 0; i < itemsInRoom.length; i++) {
				//				System.err.println(" number of deleted item size "+ deletedItemCardInRoom.size());
				if (deletedItemCardInRoom.size() > 0) {
					for(ItemCard item : this.deletedItemCardInRoom) {
						//					 System.err.println(" item.cardid "+item.cardId +" == " +itemsInRoom[i].cardId);
						if(item.cardId.equals(itemsInRoom[i].cardId)) {
							removed=true;
							break;	 
						}
					}
				}
				if(!removed) {
					items.add(itemsInRoom[i]);
				}else
					removed=false;
			}

			splitItem = items.size()/2;
		
			bX=this.getBrick("B",true,walls).getX()-40;
			bY=this.getBrick("B",true,walls).getY()+40;
			tX=this.getBrick("T",true,walls).getX()+20;
			tY=this.getBrick("T",true,walls).getY()-40;
			

			for(int i= 0; i<items.size();i++) {
				if (i < splitItem) {
					if(i>0)
						tX+=ITEMDIMSIZE;
					items.get(i).setX(tX);
					items.get(i).setY(tY);
					items.get(i).position="T";
				}
				else {
					if(i>0)
						bX+=ITEMDIMSIZE;
					items.get(i).setX(bX);
					items.get(i).setY(bY);
					items.get(i).position="B";
				}

				items.get(i).setRoom(this);
				itemCardInRoom.add(	items.get(i));

			}

		}
		loadshuffleComp();
		populateRoomUIItems(roomUI);
		shuffle_init=true;
	}
	public void loadGlobalRoom(ItemCard [] itemsInRoom, RoomUI roomUI) {
		//itemCardInRoom.clear();
		for (int i = 0; i < itemsInRoom.length; i++) {
			itemsInRoom[i].setRoom(this);
			if(NOITEMROOM > itemCardInRoom.size())
				itemCardInRoom.add(itemsInRoom[i]);
		}
		//			System.out.println(" nullllllllll load globalroom " );
		populateRoomUIItems(roomUI);

	}

	private void loadshuffleComp() {

		if(shuffle_init) 
			return;
		for (ItemCard item : itemCardInRoom) {
			this.shuffleComp.add(item);
			this.itemCord.add(new ItemCordinate(item.cardId,item.getX(),item.getY()));
		}

	}

	private class ItemCordinate{
		String id;
		int x;
		int y;
		public ItemCordinate(String id, int x, int y) {
			this.id=id;
			this.x=x;
			this.y=y;
		}
	}

}
