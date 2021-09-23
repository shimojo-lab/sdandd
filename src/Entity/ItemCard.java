package Entity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;






public class ItemCard extends RootEntity
{

	public String cardId;
	protected String decriptionFunction;
	private int digitalPoint;
	private BufferedImage backgroundImg;
	public BufferedImage foregroundImg;
	public Room aRoom;
	protected boolean dropPlayercard;
	public static final int WBG=100,HBG=100;
	public static final int WFG=30,HFG=30;
	public String position;
	public String pathImgForeground;
	
	

	/*
	 * a specific card used to set up specific room 
	 */
	private String designatedRoom;
	//private MMKeyBinding keyBinding;



	@SuppressWarnings("deprecation")
	public ItemCard(Room aRoom, int x,int y, String cardId, String decriptionFunction,int digitalPoint, String pathImgForeground,String pathImgBackground)
	{
		super(x,y);

		this.cardId=cardId;
		this.decriptionFunction=decriptionFunction;
		this.digitalPoint=digitalPoint;
		position="";
		
		
		
		backgroundImg= createBuffredImage(pathImgBackground);
		this.setImage(backgroundImg);
		foregroundImg=createBuffredImage(pathImgForeground);
		this.setImage(foregroundImg);
		this.aRoom=aRoom;
		dropPlayercard=false;
		if(aRoom !=null) {
			if(aRoom.getNoitemroom() > aRoom.itemCardInRoom.size())
				aRoom.itemCardInRoom.add(this);
		}
		
		this.pathImgForeground=pathImgForeground;
		
	}
	public void setForegroundImg(BufferedImage foregroundImg) {
		this.foregroundImg = foregroundImg;
		this.setImage(foregroundImg);
	}
	public BufferedImage getForegroundImg() {
		return foregroundImg;
	}
	public BufferedImage getBackgroundImg() {
		return backgroundImg;
	}
	
	public void setBackgroundImg(BufferedImage backgroundImg) {
		this.backgroundImg = backgroundImg;
		this.setImage(backgroundImg);
	}
	
	public String getCardIdClass(){
		return cardId;
	}
	public boolean isDropPlayercard() {
		return dropPlayercard;
	}

	public void setDropPlayercard(boolean dropPlayercard) {
		this.dropPlayercard = dropPlayercard;
	}

	public void setRoom(Room room){
		aRoom=room;
	}

	public Room getItemRoom(){
		return aRoom;
	}

	public String getcardIdClass() {
		return cardId;
	}

	public String getDescriptionFunction(){
		return decriptionFunction;
	}

	public int getDigitalPoint(){
		return digitalPoint;
	}
	
	
	


	

	
	
//	public class ItemWorker extends  SwingWorker<Void,Void>{
//		
//		private ItemCard refItemCard;
//		String netMsg;
//		
//		public ItemWorker(ItemCard ref ) {
//			this.refItemCard=ref;
//		}
//		@Override
//		protected Void doInBackground() throws Exception {
//			// TODO Auto-generated method stub
//			
//			try{	
//				
//				
//				Player aPlayer= aRoom.getPlayer();
//				
//				if(aPlayer!=null && aPlayer.self){
//					
//					if(isDropPlayercard()){
//						aPlayer.removeItemFromList(refItemCard);
//						
//						/*
//						 *  check for player is in wireless room and drop correct item 
//						 */
//						//System.out.println("task room nnn");
//						
//						
//						if (aPlayer.playerCurrentRoom() instanceof TaksRoom){
//							
//							ItemCard item=aPlayer.lastdropItemCard;
//							TaksRoom room = (TaksRoom) aPlayer.playerCurrentRoom() ;
//							if (room.roomID.equals("tsrwireLess")){
//								if(item.cardId.equals("airMonNG")){
//									room.v1=true;
//									room.loadRoom();
//								}
//								
//								if(item.cardId.equals("AirePlayNG")){
//									room.v1=true;
//									room.loadRoom();												
//								}
//								
//								if(item.cardId.equals("AireCrackNG")){
//									room.v1=true;
//									room.loadRoom();
//								}
//							}
//							
//							if (room.roomID.equals("tsrsosmedia")){
//								if(item.cardId.equals("Password Cracker"))
//									room.sosMedia.runDecode();
//								else if(item.cardId.equals("SET")){
//									room.sosMedia.actBut.setText("Run SET");
//									room.sosMedia.actBut.setVisible(true);
//									room.sosMedia.tfPlayer.setText("");
//									room.sosMedia.TFDescription.setVisible(false);
//								}else 
//									room.sosMedia.setErrorMsg(item.cardId);
//								
//								
//									
//							}
//
//						}
//						refItemCard.setVisible(false);
//						aPlayer.refPlayerProp.loadGUI(aPlayer);
//						
//					
//
//					}else 
//					{
//						
//						if(aPlayer.getNumberItemCarry() < aPlayer.ITEMCARRY){
//						new PopUpCardInfoGUI(refItemCard);
//						aPlayer.addItemToList(refItemCard);
//						netMsg="2;"+aPlayer.playerCurrentRoom().roomID +";"+ refItemCard.cardId;
//						aPlayer.refMMUI2.publisher.setNewMsg(netMsg);
//						
//						aPlayer.refPlayerProp.loadGUI(aPlayer);
//						}
//						else {
//							String taskDescription ="\n "
//									+ "Player Carry item list has reach \n"
//									+ "it is limit "+aPlayer.getNumberItemCarry() +"/"+aPlayer.ITEMCARRY+"\n"
//									+ "to add new item card, please remove some item from your item list ...!!!!";
//							DisplayInformation disp =new DisplayInformation();
//							disp.setBounds(100, 100, 400, 200);
//							disp.aText.setText(taskDescription);
//						}
//							
//							
//						
//					}
//					
//					aPlayer.checkStatus();
//				}
//
//				} catch(java.lang.NullPointerException ex){}
//				
//				//this.setIcon(removedIcon);
//				//this.setEnabled(false);
//
//
//				/*
//				 * should fire remove item by player in this section ??
//				 * should checkPlayerwin or lose the games?? 
//				 * 
//				 */
//				
//			return null;
//		}
//		
//	}
	
	
	

}

