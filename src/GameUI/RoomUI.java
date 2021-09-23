package GameUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;
import javax.swing.*;

import Entity.Brick;
import Entity.ImageHolder;
import Entity.ItemCard;
import Entity.NextRoom;
import Entity.Player;
import Entity.ResourcesRoom;
import Entity.Room;
import Entity.RootEntity;
import Entity.Trap;
import Entity.TrapRoom;

public class RoomUI extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int WIDTH = 600;
	public int HEIGHT = 600;
	public int offSet;
	public ArrayList<Room> roomsDefault;
	public List<Room> roomsGlobal;
	public ArrayList <Brick> roomUIWall;
	public ArrayList <ItemCard> roomUIItem;
	public ArrayList <Brick> roomUITrap;
	public ArrayList <NextRoom> roomUINextRoom;
	public ArrayList <NetworkCard> netCard;
	public ArrayList <String> card_attached_to_roomUI;
	public boolean roomSyncron;
	//	public ArrayList <Trap> roomUITrap;
	//public JButton wireLess1,wireLess2,wireLess3;


	/*
	 * keys binding parameter 
	 */
	private static final String UP = "UP";
	private static final String DOWN = "DOWN";
	private static final String LEFT = "LEFT";
	private static final String RIGHT = "RIGHT";
	private static final String SHIFT = "SHIFT";
	private static final String A = "A";
	private static final String SPACE = "SPACE";


	/*
	 * Draw string in specific coordinate  
	 */
	protected int drawStringCoorX, drawStringCoorY;
	protected boolean drawString;
	protected String msgToDraw;
	



	/*
	 * item manipulation variable 
	 * 
	 */
	protected boolean itemCollision=false;
	protected ItemCard itemTobeDeleted=null;

	/*
	 * trap manipulation variable 
	 */
	protected boolean trapRoomCollision=false;
	public  Brick trapTobeDeleted=null;
	public boolean permitRemoveTrapWall=false;

	/*
	 * trap room index data base question.
	 * to keep all index of question update;
	 */

	public int MAPQUIZINDEX;

	protected MMUI refMMUI;
	private final int DISTANCE = 10;

	private final int LEFT_COLLISION = 1;
	private final int RIGHT_COLLISION = 2;
	private final int TOP_COLLISION = 3;
	private final int BOTTOM_COLLISION = 4;
	private boolean L_Col=false, R_Col=false, T_Col=false, B_Col=false;
	public Player player;
	public boolean shiftKeyPressed=false;
	public  String [][] roomDesignDefault;
	public  String [][] roomDesignGlobal;
	private String netMsg;
	public boolean standalone;
	int condition;
	InputMap inputMap;
	ActionMap actionMap;
	JButton iner;
	ImageHolder currentOpacity;
	NetworkCard net_Opacity;
	List<String>userLateJoinGame;

	//IDS field 
	Timer time;
	public RoomUI( Player player, MMUI refMMUI) {
		addKeyListener(new TAdapter());

		condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		inputMap = this.getInputMap(condition);
		actionMap = this.getActionMap();
		standalone=false;

		//keyBinding();
		setFocusable(true);
		this.player=player;
		drawStringCoorX=0;
		drawStringCoorY=0;
		msgToDraw="";
		drawString=false;
		roomsDefault= new ArrayList<Room>();
		roomUIWall=new ArrayList<Brick>();
		roomUIItem=new ArrayList<ItemCard>();
		roomUITrap =new ArrayList<Brick>();
		roomUINextRoom=new ArrayList<NextRoom>();
		netCard= new ArrayList<NetworkCard>();
		//double check with netcard to avoid redudancy 
		card_attached_to_roomUI= new ArrayList<String>();
		MAPQUIZINDEX=0;
		// syn the server and new player game environment upon late player joined to the game network;
		roomSyncron=false;
		this.refMMUI=refMMUI;
		
		if(!refMMUI.defaultScreen) {
			WIDTH =600;
			HEIGHT =600;
			
		}

		userLateJoinGame=new ArrayList<String>();
		
	}

	public Room getRoomObjectRoomUI(String id) {
		Room ret=null;
		for (Room room : roomsDefault) {
			if (room.roomID.equals(id)) {
				ret =room;
				break;
			}
		}

		return ret;

	}


	public Room getRoomObjectGlobalRoomUI(String id) {
		System.err.println("room name xxxxxx " + id);
		Room ret=null;
		for (Room room : roomsGlobal) {
			if (room.roomID.equals(id)) {
				ret =room;
				break;
			}
		}

		return ret;

	}
	public ItemCard getDeletedItemCardGlobalRoomUI(String id) {

		for (Room room : roomsGlobal) {
			for(ItemCard item :room.deletedItemCardInRoom) {
				if(item.cardId.equals(id)) {
					return item;
				}
			}
		}
		return null;

	}
	public ItemCard getItemCardGlobalRoomUI(String id) {

		for (Room room : roomsGlobal) {
			for(ItemCard item :room.itemCardInRoom) {
				System.out.println(" compare result  item " + item.cardId + " item id " +id);
				if(item.cardId.equals(id)) {
					return item;
				}
			}
		}
		return null;

	}




	private void make_matrix_adjecent(int roomsize, List<Room> roomCollection) {
		if (roomsize==3)
			roomDesignDefault=new String[roomsize][roomsize];
		else
			roomDesignGlobal= new String[roomsize][roomsize];

		//System.err.println("roomcollection size " +roomCollection.size() +" room size " + roomsize);
		int index=0;
		for(int i=0;i<roomsize;i++) {
			for(int j=0;j<roomsize;j++) {
				if (roomsize==3) {
					roomDesignDefault[i][j]=roomCollection.get(index).roomID;
				}
				else
					roomDesignGlobal[i][j]=roomCollection.get(index).roomID;
				index++;
			}
		}
	}



	public void proceedGameEnvironment(int roomsize, List<Room> roomCollection, String playerRoom) {
		if (roomsize==3){
			buildGameEnvironment(roomsize,roomCollection);
			refMMUI.loadRoomItem(roomsDefault,true,this);
			remakeRoomsItem(roomsDefault);
		}
		else {
			//	System.err.print("kdkdaklaklsdh;alkvjadv'avnakdlv advkdhv ahdkvaklvhkad");
			make_matrix_adjecent( roomsize, roomCollection);
			roomDesignGlobal= new String [roomsize][roomsize];
			roomsGlobal=roomCollection;
			setElementGlobalMatrix(roomCollection);

			String [][] matrix = makeDefaultMatrix(playerRoom);
			if (matrix !=null) {

				buildGameEnvironment(3,resizeListToDefaultList(matrix));
				refMMUI.loadRoomItem(roomsGlobal,false,this);
				remakeRoomsItem(roomsDefault);
				//refMMUI.loadRoomItem(roomsDefault);
				//refMMUI.loadRoomItem(roomsGlobal);
			}

		}
	}

	public List<Room> resizeListToDefaultList(String [][] defaultMatrix){

		List<Room> ret = new ArrayList<Room>();

		for (int i=0;i<defaultMatrix.length;i++)
			for (int j=0;j<defaultMatrix.length;j++) {
				for(Room r : roomsGlobal) {

					if (r.roomID.equals(defaultMatrix[i][j])) {
						// System.err.println("room id global "+r.roomID + " room id "+ defaultMatrix[i][j] );
						ret.add(r);
						break;
					}
				}
			}
		return ret;
	}
	private void setElementGlobalMatrix(List<Room> rooms) {
		int index=0;
		for (int i =0; i< roomDesignGlobal.length;i++)
			for (int j =0; j< roomDesignGlobal.length;j++) {
				roomDesignGlobal[i][j]=rooms.get(index).roomID;
				index++;
			}


	}
	public String [][] makeDefaultMatrix(String roomID){

		int row=0, col=0;  // row and colum of 
		String roomName = null;

		for (int i=0; i<roomDesignGlobal.length;i++)
			for (int j=0; j<roomDesignGlobal.length;j++)
				if(roomDesignGlobal[i][j].equals(roomID)) {
					row=i;
					col=j;
					roomName=roomDesignGlobal[i][j];
					break;
				}
		if(countAdjecentRoom( roomDesignGlobal, roomName) == 4)
			return defaultMatrix(row-1, col-1, row+1, col+1);

		if(countAdjecentRoom( roomDesignGlobal, roomName) == 3) {
			if(col==0)
				col+=1;
			else
				col-=1;
			if(row==roomDesignGlobal.length-1)
				row-=1;
			if(row==0) 
				row++;
			if(col==0 ) 
				col++;
			return defaultMatrix(row-1, col-1, row+1, col+1);
		}

		if(countAdjecentRoom( roomDesignGlobal, roomName) == 2) {
			if(col==0) {
				//row+=1;
				col+=1;
				if(row==roomDesignGlobal.length-1)
					row-=1;
				if(row==0)
					row++;
			}
			else {
				if(row==roomDesignGlobal.length-1) // case row == col == matrix lenght 
					row--;
				else
					row+=1;
				col-=1;
			}

			return defaultMatrix(row-1, col-1, row+1, col+1);
		}

		return null;
	}

	private String [][] defaultMatrix(int i, int j, int rowLength, int colLength){
		String [][] defaultMatrix = new String[3][3];
		int k=0,l=0; 
		for (int auxI=i;auxI<=rowLength;auxI++) {
			for (int auxJ=j; auxJ<=colLength;auxJ++) {
				//System.err.println( "value of "+auxI + " " + k  + " value of  coolum " + auxJ + " " +l);
				defaultMatrix[k][l]=roomDesignGlobal[auxI][auxJ];
				if(l==2)
					l=0;
				else
					l++;
			}
			k++;
		}

		return defaultMatrix;
	}
	private int  countAdjecentRoom( String [][] roomDesign, String roomName) {
		int count=0;
		int x=0,y=0;
		for ( int row = 0; row < roomDesign.length; row ++ )
			for ( int col = 0; col < roomDesign.length; col++ ){
				if (roomDesign[row][col].equals(roomName)){
					x=row;
					y=col;
					//System.err.println(" x "+x+" y "+y);
					break;
				}
			}
		//	if(x!=0 ||y!=0|| x!=roomDesign.length || y!=roomDesign.length ) {

		// case North node (i,j+1)         
		if (y+1 < roomDesign.length)   
			count++;
		// case South (i,j-1) 
		if (y-1 >= 0)  
			count++;
		//case East node (i+1,j) 
		if (x+1 <roomDesign.length) 
			count++;
		// case West node (i-1,j)  
		if (x-1 >=0) 
			count++;
		//}


		//System.err.println("count adjecent point " + count);
		return count;
	}
	private void print (List<Room> rooms) {
		for (Room r : rooms)
			System.err.println ("room " +r.roomID);
	}
	public void buildGameEnvironment(int roomsize, List<Room> roomCollection) {

		print (roomCollection);
		int roomDimension = WIDTH / roomsize;
		//int auxInner=HEIGHT/roomsize;
		//int endDimension=roomDimension;
		int incrementInner;
		int incrementOuter=roomDimension;
		int index=0;
		int offsetX, offsetY;
		make_matrix_adjecent(roomsize,roomCollection);
		offSet=(WIDTH % (roomsize));
		for(int i=0;i<roomsize;i++) {
			//incrementInner=roomDimension;
			incrementInner=roomDimension;
			
			offsetX=10*i;
			for(int j=0;j<roomsize;j++) {
				offsetY=10*j;
				roomCollection.get(index).setX(incrementOuter);
				roomCollection.get(index).setY(incrementInner);
				roomCollection.get(index).setRefRoomUI(this);

				if (roomCollection.get(index) instanceof TrapRoom) {
					//	((TrapRoom) roomCollection.get(index)).loadTrapRoom();
					//						((TrapRoom) roomCollection.get(index)).populateRoomUITraps();
					System.out.println( "size " + roomCollection.get(index).E_door);

				}

				roomCollection.get(index).roomSetUp(roomsize,roomCollection.get(index).getX(), roomCollection.get(index).getY(),offsetX,offsetY,roomDimension,roomCollection.get(index).roomID);
				//				roomCollection.get(index).populateRoomUIWalls();
				//				roomCollection.get(index).populateRoomUINextRoom();


				if (roomCollection.get(index) instanceof TaksRoom) {
					((TaksRoom) roomCollection.get(index)).loadTaskRoomComp(refMMUI.defaultScreen);

				}
				roomsDefault.add(roomCollection.get(index));
				//incrementInner+=roomDimension;
				incrementInner+=roomDimension;
				index++;
			}
			incrementOuter+=roomDimension;
		}

		//	loadTaskRoomComponent();

	}

	public void loadTaskRoomComponent() {
		for (Room room :roomsDefault) {
			if (room instanceof TaksRoom)
				((TaksRoom) room).loadTaskRoomComp(refMMUI.defaultScreen);
		}
	}

	public  int getRoomWidth() {
		return WIDTH;
	}

	public  int getRoomHeight() {
		return HEIGHT;
	}
	public void drawItemName(ItemCard item) {
		drawString=true;
		itemCollision=true;
		//itemTobeDeleted=item;
		//System.out.println(" item name here");
		if(item!=null) {
			msgToDraw=item.cardId;

			if(item.position.equals("T"))
				drawStringCoorY=item.getY()-30;
			if(item.position.equals("B"))
				drawStringCoorY=item.getY()+70;
			drawStringCoorX=item.getX();
			repaint();

		}
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int j = 0; j < roomsDefault.size();j++) {
			roomsDefault.get(j).IDSDetection(g);
			roomsDefault.get(j).drawRoomProperties(g);
			

			if(roomsDefault.get(j) instanceof TrapRoom) {
				((TrapRoom) roomsDefault.get(j)).drawTrapRoomProperties(g);
				//System.out.println(rooms.get(j).roomID);
			}
			if(roomsDefault.get(j) instanceof TaksRoom) {
				((TaksRoom) roomsDefault.get(j)).drawTaskRoomProperties(g);

				if(netCard.size()>0) {
					for(NetworkCard net : netCard) {
						if(((TaksRoom) roomsDefault.get(j)).roomID.equals(net.aRoom.roomID)){
							if(net.isDrawOpacity()) {
								float opacity = 0.5f;
								((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
								g.drawImage(net.getImage(),net.getX(),net.getY(),this);

							}
							else
								g.drawImage(net.getImage(),net.getX(),net.getY(),this);
						}
					}
				}

			}

		}
		if(drawString) {
			player.playerCurrentRoom().drawItemName(g, msgToDraw, drawStringCoorX, drawStringCoorY);

		}

	}

	public Frame getRefMMUI() {

		return refMMUI;
	}
	public Room getRoom(String ID, List<Room> rooms) {

		for (Room room :rooms) {
			//System.err.println(room.roomID + " == " +ID);
			if(room.roomID.equals(ID))
				return room;
		}
		return null;
	}





	class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_SHIFT ) 
				shiftKeyPressed=false;
			if (key == KeyEvent.VK_A ) 
				shiftKeyPressed=false;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			drawString=false;

			if (key == KeyEvent.VK_LEFT) {
				if (checkBrickCollision(player,LEFT_COLLISION)) {
					return;
				}
				if(checkTrapCollision(LEFT_COLLISION)) {
					return;
				}


				if (player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG") !=null)
					drawItemName(player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG"));


				if(nextRoomCol(LEFT_COLLISION)==true) 
					return;

				checkImageHolderCollosion (player.getX(), player.getY());

				if (checkNetCardCollosion(LEFT_COLLISION)!=null) {
					checkNetCardCollosion(LEFT_COLLISION).setDrawOpacity(true);
					net_Opacity=checkNetCardCollosion(LEFT_COLLISION);
					if(shiftKeyPressed) {
						System.out.println("shif key is " +shiftKeyPressed);
						net_Opacity.move(-DISTANCE, 0);
					}
				}
				if (checkNetCardCollosion(LEFT_COLLISION)==null) {
					if(net_Opacity!=null)
						net_Opacity.setDrawOpacity(false);
				}
				player.move(-DISTANCE, 0);

			} 
			else if (key == KeyEvent.VK_RIGHT) {

				if (checkBrickCollision(player,RIGHT_COLLISION)) {
					return;
				}
				if(checkTrapCollision(RIGHT_COLLISION)) {
					//if(checkTrapCollision())
					return;
				}

				if (player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG") !=null)
					drawItemName(player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG"));

				if(nextRoomCol(RIGHT_COLLISION)==true)
					return;
				checkImageHolderCollosion (player.getX(), player.getY());

				if (checkNetCardCollosion(LEFT_COLLISION)!=null) {
					checkNetCardCollosion(LEFT_COLLISION).setDrawOpacity(true);
					net_Opacity=checkNetCardCollosion(LEFT_COLLISION);
					repaint();
					if(shiftKeyPressed) {
						net_Opacity.move(DISTANCE, 0);
						return;
					}
				}
				if (checkNetCardCollosion(LEFT_COLLISION)==null) {
					if(net_Opacity!=null)
						net_Opacity.setDrawOpacity(false);
				}
				player.move(DISTANCE, 0);

			} 
			else if (key == KeyEvent.VK_UP) {

				if (checkBrickCollision(player,TOP_COLLISION)) {
					return;
				}

				if(checkTrapCollision(TOP_COLLISION)) {
					return;
				}

				if (player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG") !=null)
					drawItemName(player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG"));


				if (nextRoomCol(TOP_COLLISION)==true)
					return;			
				checkImageHolderCollosion (player.getX(), player.getY());

				if (checkNetCardCollosion(LEFT_COLLISION)!=null) {
					checkNetCardCollosion(LEFT_COLLISION).setDrawOpacity(true);
					net_Opacity=checkNetCardCollosion(LEFT_COLLISION);
					repaint();
					if(shiftKeyPressed) {
						net_Opacity.move(0,-DISTANCE);
						return;
					}
				}
				if (checkNetCardCollosion(LEFT_COLLISION)==null) {
					if(net_Opacity!=null)
						net_Opacity.setDrawOpacity(false);
				}
				player.move(0, -DISTANCE);

			}
			else if (key == KeyEvent.VK_DOWN) {

				if (checkBrickCollision(player,BOTTOM_COLLISION)) 
					return;

				if(checkTrapCollision(BOTTOM_COLLISION)) {

					return;
				}


				if (player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG") !=null) {
					drawItemName(player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG"));

				}

				if(nextRoomCol(BOTTOM_COLLISION)==true) {
					{
						System.err.println("enter to matrix");
						return;
					}

				}

				checkImageHolderCollosion (player.getX(), player.getY());

				if (checkNetCardCollosion(LEFT_COLLISION)!=null) {
					checkNetCardCollosion(LEFT_COLLISION).setDrawOpacity(true);
					net_Opacity=checkNetCardCollosion(LEFT_COLLISION);
					repaint();
					if(shiftKeyPressed) {
						net_Opacity.move(0,DISTANCE);
						return;
					}
				}
				if (checkNetCardCollosion(LEFT_COLLISION)==null) {
					if(net_Opacity!=null)
						net_Opacity.setDrawOpacity(false);
				}
				player.move(0, DISTANCE);

			} 

			else if(key ==KeyEvent.VK_SPACE) {
				if (player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG") !=null) {
					itemTobeDeleted=player.playerCurrentRoom().checkItemCarCollosion(player.getX(), player.getY(), "FG");
					playerEnquequeItem();
				}

				if(trapRoomCollision) {

					((TrapRoom) player.playerCurrentRoom()).trapRoomInterface("","Trap Room \n To proceed, first drop one of the player item card \n"
							+ "and answer at least 3 question to get access to next room" );
				}

				if(checkImageHolderCollosion (player.getX(), player.getY())) {
					executeTaskRoomTask();
				}

				if(net_Opacity!=null) {
					net_Opacity.setDrawOpacity(false);
				}
			}
			else if(key == KeyEvent.VK_SHIFT ) {
				shiftKeyPressed=true;

			}
			else if(key == KeyEvent.VK_A && shiftKeyPressed) {
				new DropCardGUI(player);
				shiftKeyPressed=false;
			}

			else if(key == KeyEvent.VK_C && shiftKeyPressed) {
				if (refMMUI.successfully_join_network || refMMUI.server)
					refMMUI.chatInterface.run_chat_Interface(refMMUI.chatInterface);
				shiftKeyPressed=false;

			}

			else if(key == KeyEvent.VK_S && shiftKeyPressed) {

				refMMUI.errorMsgDisplay("The deck of cards has been successfully reshuffling!!!\n" + 
						"Note each time you reshuffle the deck of card, \n will reduce number hint displayed on the card.!!!! ", player.playerID, false);
				reshuffle(false,null);
				String aux_net_packet=";";
				for (ItemCard item: player.getPlayerItemList()) {
					aux_net_packet=aux_net_packet+item.cardId+";";
				}
				netMsg="9;"+ player.playerID + ";"+player.SHUFFLE +";"+player.getNumberItemCarry()+aux_net_packet;
				if(!standalone)
					send_network_message(netMsg);
				shiftKeyPressed=false;


			}


			repaint();
		}
	}
	public void send_network_message(String netMsg) {
		if(refMMUI.successfully_join_network)
			refMMUI.publisher.setNewMsg(netMsg);
	}

	public void send_Net_Msg_Task_Completed(Player aPlayer, String task_RoomID) {
		if(!refMMUI.successfully_join_network)
			return;
		netMsg="14;"+ aPlayer.playerID + ";"+task_RoomID+";";
		send_network_message(netMsg);
	}
	public void reshuffle(boolean netMSG, String [] itemID){
		for (Room room : roomsDefault) {
			room.reshuffleCards(this, netMSG,itemID);
		}

	}
	private void playerEnquequeItem() {

		if(player.addItemToList(itemTobeDeleted)) {
			new PopUpCardInfoGUI(itemTobeDeleted,player);
			itemTobeDeleted.setImage(itemTobeDeleted.getBackgroundImg());
			player.playerCurrentRoom().deletedItemCardInRoom.add(itemTobeDeleted);
			player.playerCurrentRoom().itemCardInRoom.remove(itemTobeDeleted);
			roomUIItem.remove(itemTobeDeleted);
			netMsg="3;"+player.playerCurrentRoom().roomID +";"+ itemTobeDeleted.cardId+";"+player.playerID;
			if(!standalone)
				send_network_message(netMsg);
			player.refPlayerProp.repaint();
			player.checkStatus();
		}
		else {
			JOptionPane.showMessageDialog(null, 
					"Item List is full, remove item in list to add new item", 
					"ADD Item Fail", 
					JOptionPane.WARNING_MESSAGE);
		}
		




	}


	// netwrok card against player 
	private boolean checkNetCardCollosionBrick(ItemCard entity, int type) {

		TaksRoom r ;

		if(player.playerCurrentRoom() instanceof TaksRoom) {
			r = (TaksRoom) player.playerCurrentRoom();
			if(r.taskID.equals("networkSecurity")) {
				for (Brick b : r.trapBrick) {

					if (type == LEFT_COLLISION) {
						if (player.isLeftCollision(b)) 
							return true;

					}
					else if (type == RIGHT_COLLISION) {
						if (player.isRightCollision(b)) 
							return true;
					}
					else if (type == TOP_COLLISION) {
						if (player. isTopCollision(b)) 
							return true;

					}
					else if (type == BOTTOM_COLLISION) {
						if (player.isBottomCollision(b)) 
							return true;
					}
				} 

			}
		}
		return false;
	}
	private NetworkCard checkNetCardCollosion(int type) {

		TaksRoom r ;

		if(player.playerCurrentRoom() instanceof TaksRoom) {
			r = (TaksRoom) player.playerCurrentRoom();
			if(r.taskID.equals("networkSecurity")) {
				for (NetworkCard net : netCard) {
					if (checkNetCardCollosionBrick(net,type)) {
						//System.err.println("colosion between wall and netcards occurd");
						return null;
					}
					for (int i=net.getX(); i<net.getX()+net.WFG+20;i++) {
						for (int j =net.getY(); j<net.getY()+net.HFG+20;j++) {
							if(player.getX()==i && player.getY()==j) {
								return net;
							}

						}
					}
				}
			}
		}
		return null;
	}



	private boolean checkImageHolderCollosion (int x, int y) {
		TaksRoom r ;

		if(player.playerCurrentRoom() instanceof TaksRoom) {

			r = (TaksRoom) player.playerCurrentRoom();
			if(currentOpacity != null)
				r.setImgHolderOpacity(currentOpacity,false);

			if(r.checkImageHolderCollosion(x, y) !=null){
				currentOpacity=r.checkImageHolderCollosion(x, y);
				return true;
			}
		}
		return false;
	}
	private void executeTaskRoomTask() {
		TaksRoom r ;

		if(player.playerCurrentRoom() instanceof TaksRoom) {
			r = (TaksRoom) player.playerCurrentRoom();

			String op = currentOpacity.operation;

			switch (op) {
			case "HINT" : r.loadHint();
			break;
			case "PHONE" :r.fireSosMedEvent();
			break;
			case "WL1" : r.fireWLEvent(op);
			break;
			case "WL2" : r.fireWLEvent(op);
			break;
			case "WL3" : r.fireWLEvent(op);
			break;
			case"NEXTLEVEL" : r.fireDNSCachePoisoning();
			default :
				break;

			}

		}
	}
	private boolean checkBrickCollision(RootEntity entity, int type) {
		
		boolean collosion = false;
		Brick brick=null;
		for (Brick b : player.playerCurrentRoom().walls) {
			if (type == LEFT_COLLISION) {
				if (entity.isLeftCollision(b)) { 
					collosion= true;
					brick=b;
				}
			}
			else if (type == RIGHT_COLLISION) {
				if (entity.isRightCollision(b)) {
					collosion= true;
					brick=b;
				}
			}
			else if (type == TOP_COLLISION) {
				if (entity. isTopCollision(b)) {
					collosion= true;
					brick=b;
				}
			}
			else if (type == BOTTOM_COLLISION) {
				if (entity.isBottomCollision(b)) {
					collosion= true;
					brick=b;
				}
			}
		} 
		if(collosion) {
			if(brick.isNextDoorAccess() || brick.center.equals("no")) {
				collosion=true;
			}
			else
				collosion=false;
		}
		
		return collosion;
	}
	private boolean checkTrapCollision(int type) {
		Brick trap=null;
		TrapRoom room=null;
		boolean retvalue=false;

		if(player.playerCurrentRoom() instanceof TrapRoom) {
			room= (TrapRoom) player.playerCurrentRoom();
			for (int i = 0; i < room.trapsWallImg.size(); i++) {
				trap =room.trapsWallImg.get(i);
				if (type == LEFT_COLLISION) {
					if (player.isLeftCollision(trap)) {
						retvalue= true;
						break;
					}

				}
				else if (type == RIGHT_COLLISION) {
					if (player.isRightCollision(trap)) { 
						retvalue= true;
						break;
					}
				}
				else if (type == TOP_COLLISION) {
					if (player.isTopCollision (trap)) {
						retvalue= true;
						break;
					}

				}
				else if (type == BOTTOM_COLLISION) {
					if (player.isBottomCollision(trap)) { 
						retvalue= true;
						break;
					}
				}
			} 

			if(retvalue) {
				trapRoomCollision=true;
				trapTobeDeleted=trap;
			}
			else {
				trapRoomCollision=false;
				trapTobeDeleted=null;
			}
		}
		return retvalue;
	}

	public Room getRoomUIRoom(String id) {
		Room ret=null;
		for (Room room : roomsDefault) {
			if (room.roomID.equals(id)) {
				ret =room;
				break;
			}
		}

		return ret;

	}


	public void removeTrap(String roomId, boolean netMsg) {
		TrapRoom trap;
		if (netMsg) {
			if(getRoomUIRoom(roomId) == null) {
				trap = (TrapRoom) this.getRoomObjectGlobalRoomUI(roomId);
				((TrapRoom) this.getRoomObjectGlobalRoomUI(roomId)).setTrapDoor(trapTobeDeleted.operation);
			}
			else {
				trap = (TrapRoom) getRoomUIRoom(roomId);
				((TrapRoom) getRoomUIRoom(roomId)).setTrapDoor(trapTobeDeleted.operation);
			}
		}

		else {
			trap = (TrapRoom) player.playerCurrentRoom();
			((TrapRoom) player.playerCurrentRoom()).setTrapDoor(trapTobeDeleted.operation);
		}

		for (int i=0; i< trap.trapsWallImg.size();i++) {
			if (trapTobeDeleted.operation.equals(trap.trapsWallImg.get(i).operation))
				trap.trapsWallImg.remove(trap.trapsWallImg.get(i));

		}

		/*
		 * inform other playet in the network to removed trap wall 
		 */
		if (!netMsg) {
			String x = "4;"+player.playerID+";"+trapTobeDeleted.roomID+";"+trapTobeDeleted.operation;
			if(!standalone)
				send_network_message(x);
		}

		int k=0;

		while (k < roomUITrap.size()) {
			Brick b2 = roomUITrap.get(k);	
			if(b2.roomID.equals(trapTobeDeleted.roomID) && b2.operation.equals(trapTobeDeleted.operation)) {
				roomUITrap.remove(b2);
				//System.out.println("removed " +k);
			}
			k++;	
		}


		repaint();
	}


	public void setTrapTobeDeleted(Brick trapTobeDeleted) {
		this.trapTobeDeleted = trapTobeDeleted;
	}

	private NextRoom moveNextMatrix(int type, NextRoom room) {
		NextRoom nextRoom = new NextRoom(player.playerCurrentRoom().roomID, 0, 0, "/roomcon_hori.png" ,"","",false);
		Map<String, String> adjecentRoom=player.playerCurrentRoom().findAdjecentRoom(this.roomDesignGlobal, room.roomID);
		if (type == LEFT_COLLISION) {
			nextRoom.direction=adjecentRoom.get("W");
			nextRoom.nextRoom=adjecentRoom.get("W");
			return nextRoom;

		}
		else if (type == RIGHT_COLLISION) {
			nextRoom.direction=adjecentRoom.get("E");
			nextRoom.nextRoom=adjecentRoom.get("E");
			return nextRoom;

		}
		else if (type == TOP_COLLISION) {
			nextRoom.direction=adjecentRoom.get("S");
			nextRoom.nextRoom=adjecentRoom.get("S");
			return nextRoom;

		}
		else if (type == BOTTOM_COLLISION) {
			nextRoom.direction=adjecentRoom.get("N");
			nextRoom.nextRoom=adjecentRoom.get("N");
			return nextRoom;
		}
		return null;

	}



	private boolean nextRoomCol(int type) {
		NextRoom nextRoom=checkNextRoomCollision(type), NextRoomGlobal;
		if( nextRoom!=null) {
			if (nextRoom.nextRoom.equals("DEADEND")) {
				if(!refMMUI.successfully_join_network) {
					numberMoves(type,-2);
					return false;
				}

				NextRoomGlobal=moveNextMatrix(type, nextRoom);
				//System.err.println("NextRoomGlobal.nextRoom " +NextRoomGlobal.nextRoom);
				if (NextRoomGlobal.nextRoom.equals("DEADEND")) {
					numberMoves(type,-2);
					return false;
				}
				else {
					numberMoves(type,4);
					setPlayerNextRoom(NextRoomGlobal,true);
					return true;

				}
			}
			else {
				//				System.err.println("NextRoomGlobal.nextRoom number of moves");
				numberMoves(type,4);
				setPlayerNextRoom(nextRoom,false);

				return true;
			}
		}
		else 
			return false;

	}

	private void setPlayerNextRoom (NextRoom x, boolean moveMatrix) {

		String nextRoom=x.nextRoom;
		Room  room = player.playerCurrentRoom();
		room.shuffle_counter=0;

		room.removePlayerFromRoom(player);
		if(moveMatrix) {
			List auxRoomsDefault= new ArrayList<Room>();
			auxRoomsDefault.clear();
			getRoomObjectGlobalRoomUI(room.roomID).removePlayerFromRoom(player);
			Room r= getRoom(nextRoom,roomsGlobal);
			player.setCurrentRoom(r);
			r.addPlayerToRoom(player);
			roomDesignDefault=makeDefaultMatrix(r.roomID);
			//			System.err.println("current rooom " + player.playerCurrentRoom().roomID +"next room  "+ nextRoom);
			// copy roomsDefault list member to roomsglobal 
			copyRoomDefaultItemToRoomGlobal(roomsDefault,roomsGlobal);
			roomsDefault.clear();
			roomUINextRoom.clear();
			for (int i=0;i<roomDesignDefault.length;i++) {
				for (int j=0; j<roomDesignDefault[i].length;j++) {
					for (Room r1 : roomsGlobal) {
						//System.err.println(" roomsGlobal.size "+ roomsGlobal.size());
						if(r1.roomID.equals(roomDesignDefault[i][j])) {
							auxRoomsDefault.add(r1);
							break;
						}

					}

				}
			}

			buildGameEnvironment(3,auxRoomsDefault);
			//if(!standalone)
			send_network_message("5;"+player.playerID+";"+player.getPlayerName()+";"+getRoom(nextRoom,roomsGlobal).roomID+";"+ room.roomID);
			this.refMMUI.playerLoc="5;"+player.playerID+";"+player.getPlayerName()+";"+getRoom(nextRoom,roomsGlobal).roomID+";"+room.roomID;
			remakeRoomsItem(roomsDefault);
			update_other_player_Loc();

		}
		else {

			//			System.err.println("current rooo " + player.playerCurrentRoom().roomID +"next room  "+ nextRoom);
			getRoom(nextRoom,roomsDefault).addPlayerToRoom(player);
			player.setCurrentRoom(getRoom(nextRoom,roomsDefault));
			//if(!standalone)
			send_network_message("5;"+player.playerID+";"+player.getPlayerName()+";"+getRoom(nextRoom,roomsDefault).roomID+";"+room.roomID);
			this.refMMUI.playerLoc="5;"+player.playerID+";"+player.getPlayerName()+";"+getRoom(nextRoom,roomsDefault).roomID+";"+room.roomID;
		}


	}
	private void update_other_player_Loc() {
		for (Room r : roomsDefault) {
			if(r.getListPlayerInRoom().size()>0) {
				int i=0;
				for (Player p : r.getListPlayerInRoom()) {
					if(!p.self) {
						p.setX(r.getX()-125 + (i*4));
						p.setY(r.getY()-125 + (i*4));
						System.out.println("p.setY(r.getY()-125 + (i*4)); "+ (r.getY()-125 + (i*4)));
					}
					i++;
				}

			}
		}
		repaint();
	}

	private void remakeRoomsItem(List<Room> rooms) {
		ItemCard [] itemsInRoom;
		for (Room room : rooms) {
			System.err.println("room remdimension  " + room.roomID  +" room dimension " +rooms.size());
			itemsInRoom= new ItemCard [room.itemCardInRoom.size()];
			for (int i=0; i< room.itemCardInRoom.size();i++) {
				itemsInRoom[i]=room.itemCardInRoom.get(i);
				System.err.println(" item name " +room.itemCardInRoom.get(i).cardId);
			}

			room.loadRoom(itemsInRoom,this);

			if(room instanceof TaksRoom)
			{
				for (NetworkCard net : netCard) {
					if(net.aRoom.roomID.equals(room.roomID))
						((TaksRoom) room).set_locality_attched_net_card( net ,true, refMMUI.defaultScreen);
				}
			}
		}

	}
	private void copyRoomDefaultItemToRoomGlobal(List<Room> roomsDefault, List<Room>roomsGlobal) {
		for (Room rdefault : roomsDefault)
			for(int i=0;i <roomsGlobal.size();i++) 
				if(rdefault.roomID.equals(roomsGlobal.get(i).roomID)) {
					System.out.println("room name "+rdefault.roomID +" room size " +roomsDefault.size() + " antes copy item size "+roomsGlobal.get(i).itemCardInRoom.size());
					roomsGlobal.remove(roomsGlobal.get(i));
					roomsGlobal.add(i, rdefault);
					System.out.println("depois  copy item size "+roomsGlobal.get(i).itemCardInRoom.size());

					break;
				}

	}

	public void numberMoves(int direction, int numberMoves){
		if (numberMoves == 0)
			numberMoves=1;
		if(direction == LEFT_COLLISION)
			player.move(-DISTANCE*numberMoves, 0);
		if(direction== RIGHT_COLLISION)
			player.move(DISTANCE*numberMoves, 0);
		if(direction== TOP_COLLISION)
			player.move(0,-DISTANCE*numberMoves);
		if(direction== BOTTOM_COLLISION)
			player.move(0,DISTANCE*numberMoves);

	}

	private ItemCard checkItemCollision() {
		//	System.out.println("trap size "+ roomUITrap.size());
		Rectangle playerCol= new Rectangle(player.getX(),player.getY(),10,10);
		for (int i = 0; i < roomUIItem.size(); i++) {
			ItemCard item = roomUIItem.get(i);
			Rectangle itemCol = new Rectangle(item.getX(),item.getY(),10,10);

			if (playerCol.intersects(itemCol)) {
				//	System.out.println("col ");
				return item;
			}
			//System.out.println(" not col ");
		}

		return null;
	}


	public NextRoom checkNextRoomCollision(int type) {
		NextRoom room=null;

		for (int i = 0; i < roomUINextRoom.size(); i++) {

			room =roomUINextRoom.get(i);
			if (type == LEFT_COLLISION ) {
				if (player.isLeftCollision(room)) 
					return room;


			}
			else if (type == RIGHT_COLLISION ) {
				if (player.isRightCollision(room)) 
					return room;
			}
			else if (type == TOP_COLLISION) {
				if (player.isTopCollision (room)) 
					return room;

			}
			else if (type == BOTTOM_COLLISION ) {
				if (player.isBottomCollision(room)) 
					return room;
			}
		} 
		return null;
	}

	public String generate_syn_file() {
		String ret="";
		for ( Room room : roomsGlobal) {
			if(room instanceof TrapRoom) {
				TrapRoom r = (TrapRoom) room;
				//  case trap room start the file with "trap_start"
				ret=ret+"trap_start;"+r.roomID+";";
				if(r.W_door)
					ret=ret+"L;";
				if(r.E_door)
					ret=ret+"R;";
				if(r.N_door)
					ret=ret+"B;";
				if(r.S_door)
					ret=ret+"T;";
				ret=ret+"trap_stop;";
			}
			for (ItemCard item : room.deletedItemCardInRoom) {
				ret=ret+item.cardId+";";
			}
		}
		// netcard 
		if(netCard.size()>0) {
			ret=ret+"net_start;";
			for (NetworkCard net : netCard) {
				ret=ret+net.aRoom.roomID+":"+net.cardId+";";
			}
			ret=ret+"net_stop;";
		}
		// case player has hold item
		if(player.playerItem.size()>0) {
			ret=ret+"other_player_start;";
			for (ItemCard item :player.playerItem)
				ret=ret+item.cardId+";";
			for (ItemCard item :player.team_players_Items)
				ret=ret+item.cardId+";";
			ret=ret+"other_player_stop;";
		}
		ret=ret+"ENDSYN;";
		return ret;
	}
	
	public boolean zero_move(Player player) {
		
	    boolean result=false;
		if (player.instrusionDetectorRoom == 2) {
			for (NextRoom room : player.playerCurrentRoom().listNextRoom) {
				if(room.nextRoom.equals("DEADEND")&& room.direction.equals("N")) {
					result=true;
					break;
				}
				
			}
		} 
		return result;
		 
	}
}
