package Entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import GameUI.DisplayInformation;
import GameUI.InstrusionDetectorCard;
import GameUI.MMUI;
import GameUI.MagicPatchCard;
import GameUI.PlayerProp;
import GameUI.ResourcesCard;

public class Player extends RootEntity{

	protected final int  ITEMCARRY=5;
	private int lifeSpan, digitalPoint;
	private Room currentRoom;
	public List<ItemCard> playerItem;
	public  Vector<ItemCard> team_players_Items;
	private List<ItemCard> PlayerDropList; //Used to reshuffle cards in a room 
	public ItemCard lastdropItemCard, lastDropIDSCard;
	protected ItemCard newItemCard;
	

	private String playerName;
	public int completedTask, playerID;
	public int networkInstrusionDetectorLevel;
	public int instrusionDetectorRoom;

	public int radius = 20; // Ball radius
	
	public int SHUFFLE;
	
	int index=0;
	public PlayerProp refPlayerProp;
	public  MMUI refMMUI;

	boolean playerStatus=false;
	public  boolean self;
	/* important network parameter for exchange information 
	 *  0 : initialization parameter
	 *  1 : request initialization parameter
	 *  2 : request update resources in the room example remove item from the room 
	 *  3 : chat message, exchange message betwen player 
	 *  4 : request item from other player
	 */
	

	public Player( Room currentRoom, int playerID, String playerName, int x, int y, boolean self){
		
		super(x,y);
		this.playerID=playerID;
		this.setPlayerName(playerName);		
		this.lifeSpan=100;
		this.digitalPoint=0;
		this.currentRoom=currentRoom;
		this.playerItem= new ArrayList<ItemCard>();
		team_players_Items=new Vector<ItemCard>();
		this.PlayerDropList= new ArrayList<ItemCard>();	
		this.completedTask=0;
		this.self=self;
		networkInstrusionDetectorLevel=0;
		instrusionDetectorRoom=0;
		lastdropItemCard=null;
		newItemCard=null;
		lastDropIDSCard=null;
		SHUFFLE=0;


	}

	

	public void setRefPlayerProp(PlayerProp ui){
		refPlayerProp=ui;

	}

	public void resetPlayer(){
		lifeSpan=100;
		digitalPoint=0;
		playerItem.clear();;	
		PlayerDropList.clear();
		completedTask=0;
		networkInstrusionDetectorLevel=0;
		instrusionDetectorRoom=0;
		lastdropItemCard=null;
		newItemCard=null;
	}
	
	public int getNumberCompletedTask(){
		return completedTask;
	}
	public void setNumberCompletedTask(){
		String netMsg="88;";
		if(this.playerID!=0 && !this.playerCurrentRoom().refRoomUI.standalone)
			this.refMMUI.send_network_msg(netMsg);
		 
		completedTask+=1;
		
		checkStatus();
	}


	public void checkStatus(){
	
		String s = "Congratualation " + playerName + ", You have successfully Completed \n "
				+ "MM Board Games to play again clik exit button ";

		String f = "Sorry " + playerName + ", You have failed   \n "
				+ "MM Board Games to play again clik exit button ";
		String netMsg="99;";

		// for win 
		if(getNumberCompletedTask() == 3 || getDigitalPoint()>99){
			DisplayInformation temp = new DisplayInformation("~Display Information~");
			temp.aText.setText(s);
			temp.setPlayer(this);
			
			if(!playerCurrentRoom().refRoomUI.standalone)
				this.refMMUI.send_network_msg(netMsg+"true");
		}

		//for lost 
		if (networkInstrusionDetectorLevel==6 || instrusionDetectorRoom ==3  ){
			DisplayInformation temp = new DisplayInformation("~Display Information~");
			temp.aText.setText(f);
			temp.setPlayer(this);
			this.refMMUI.send_network_msg(netMsg+"false");
		}
		
		if (this.currentRoom.refRoomUI.zero_move(this)) {
			
			DisplayInformation temp = new DisplayInformation("~Display Information~");
			temp.aText.setText(f);
			temp.setPlayer(this);
			this.refMMUI.send_network_msg(netMsg+"false");
		}

	}
	public void reloadGame(){
		refMMUI.reloadMMGames();
	}

	public void drawBall(Graphics g) {
		if(g ==null )
			return;
		if(self)
			g.setColor(Color.RED);
		else 
			g.setColor(Color.MAGENTA);
		try {
			g.fillOval(getX(), getY(),radius , radius);
			//g.drawString("X " +getX()+ " Y "+getY() +" Room "+this.currentRoom.roomID, this.getX(), this.getY()-10);
		} catch  (java.lang.NullPointerException ex ) {
		
			
		} 
		
	}
	
	
	public void drawLifeSpan(Graphics g, int x, int y) {
		if (70<=getLifeSpan() && getLifeSpan() <=100)
			g.setColor(Color.GREEN);
		if (30<=getLifeSpan() && getLifeSpan() <=70)
			g.setColor(Color.YELLOW);
		if (0<=getLifeSpan() && getLifeSpan() <=30)
			g.setColor(Color.RED);
		g.fillRect(x, y,getLifeSpan()*2 , 20);
	}
	public void drawDigitalPoint(Graphics g, int x, int y) {
		g.setColor(Color.YELLOW);
		if(getDigitalPoint()>0)
			g.fillRect(x, y, getDigitalPoint()*2, 20);
		else 
			g.fillRect(x, y, 1, 20);
	}

	
	
	public int getLifeSpan(){

		return lifeSpan;

	}

	public void damageLifeSpan(int cardDamageEffect){

		lifeSpan-=cardDamageEffect;

	}

	public void addLifeSpanPoint(int potionPotion){

		if (lifeSpan < 100)
			lifeSpan+=potionPotion;
		if(lifeSpan > 100)
			lifeSpan=100;
	}


	public int getDigitalPoint(){

		return digitalPoint;
	}


	public void damageDigitalPoint(int cardDamageEffect){

		digitalPoint-=cardDamageEffect;

	}

	public void addDigitalPoint(int point){

		digitalPoint+=point;
	}


	public int getNumberItemCarry(){

		return playerItem.size();

	}
	private void instrusionCardDetected(ItemCard item) {
		if (item instanceof InstrusionDetectorCard) {
			lastDropIDSCard=item;
			damageDigitalPoint(((InstrusionDetectorCard) item).getDamageDigitalPoint());
			damageLifeSpan(((InstrusionDetectorCard) item).getDamageDigitalPoint());
			addInstrusionLevel(true);	
			playerCurrentRoom().roomIDS=true;
			/*
			 * update room door access 
			 * update network IDS counter 
			 */
			playerCurrentRoom().roomDoorAccessConfig(instrusionDetectorRoom,playerCurrentRoom().roomID,false);
			
		}
	}
	
	public void validate_Comsume_Item(ItemCard item) {
		
		
		if (item instanceof ResourcesCard) {
			this.addDigitalPoint(item.getDigitalPoint());
		}
		
		if (item instanceof MagicPatchCard) {
			String magicCardType=((MagicPatchCard) item).getmagicCardTypee();
				if(magicCardType.equals("potion"))
					addLifeSpanPoint(item.getDigitalPoint());
				if(magicCardType.equals("normal"))
					addDigitalPoint(item.getDigitalPoint());
				
				if(magicCardType.equals("zero")){
					if(lastDropIDSCard!=null){
						addLifeSpanPoint(((InstrusionDetectorCard) lastDropIDSCard).damageDigitalPoint);
						nullified_IDS_Door(this.currentRoom,instrusionDetectorRoom);
						instrusionDetectorRoom--;
						networkInstrusionDetectorLevel--;
						lastDropIDSCard=null;
						if(this.instrusionDetectorRoom ==0)
							this.currentRoom.roomIDS=false;
						refPlayerProp.repaint();
						int id =playerID;
						String msg= "16;"+Integer.toString(id)+";"+Integer.toString(instrusionDetectorRoom)+";"+this.currentRoom.roomID;
						this.refMMUI.send_network_msg(msg);
					}
				}
			
		}
		
		
	}
	public void nullified_IDS_Door(Room room, int numberIDS) {
		if(numberIDS > 1 ) {
			room.aux_center_access_door_config(room.roomID,"R",false);
			room.aux_center_access_door_config(room.roomID,"T",false);
			room.aux_center_access_door_config(room.roomID,"B",false);
		}
		else 
			room.aux_center_access_door_config(room.roomID,"L",false);
		room.refRoomUI.repaint();
	}

	public boolean addItemToList(ItemCard item){
		//ItemCard genName() =null;
		item.setDropPlayercard(true);
		if (playerItem.size() < ITEMCARRY){
			newItemCard=item;
			playerItem.add(item);
			instrusionCardDetected(item);
			return true;

		}
		return false;

	}

	public void removeItemFromList(ItemCard item){
		item.setBackgroundImg(item.getBackgroundImg());
		item.setDropPlayercard(false);
		lastdropItemCard=item;
		if(item instanceof InstrusionDetectorCard) {
			lastDropIDSCard=item;
//			if(this.instrusionDetectorRoom ==0)
//				this.currentRoom.roomIDS=false;
		}
		playerItem.remove(item);
		String netMsg="10;"+playerID+";"+ item.cardId+";"+ item.getItemRoom().roomID;
		if(this.playerID!=0 && !this.playerCurrentRoom().refRoomUI.standalone)
			this.refMMUI.publisher.setNewMsg(netMsg);
		addItemToPlayerDropList(item);
		
	}

	public List<ItemCard> getPlayerItemList(){
		return playerItem;
	}

	public List<ItemCard> getPlayerDropmList(){
		return PlayerDropList;
	}

	public void addItemToPlayerDropList(ItemCard item){
		PlayerDropList.add(item);
	} 

	public void removeAllPlayerDropList(){
		PlayerDropList.removeAll(getPlayerDropmList());
	} 

	

	public void setCurrentRoom(Room aRoom){
		currentRoom=aRoom;
		aRoom.playerInRoom.add(this);
	}

	public Room playerCurrentRoom(){
		return currentRoom;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String genName(){

		String temp="uniqueCard"+index;
		index++;
		return temp;

	}
	
	public void addInstrusionLevel(boolean roomIDS){

		
		networkInstrusionDetectorLevel++;
		if(roomIDS)
			instrusionDetectorRoom++;
		refPlayerProp.repaint();
	
	}
	
	public int  getIDL(){

		return networkInstrusionDetectorLevel;
	}
	public int getIDR(){
		return instrusionDetectorRoom;
	}



	public void setRefMMUI(MMUI mmui){
		refMMUI=mmui;
	}
	
	


}
