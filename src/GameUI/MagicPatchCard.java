package GameUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import Entity.ItemCard;
import Entity.Room;


public class MagicPatchCard extends ItemCard 
{
	
	
	private int digitalPoint;
	/*
	 * potion --> add 5 point to life span 
	 * zero --> completely nullified intrusion detection card 
	 * normal --> reduce digital point damage  by the intrusion detection card 
	 */
	protected String magicCardType;
	//private ImageIcon removedIcon ;
	
	

	public MagicPatchCard(Room aRoom,int x,int y, String magicCardType,String cardId, String decriptionFunction,int digitalPoint, String pathImgBg, String pathImgForeground)
	{
		
		super(aRoom,x,y,cardId,decriptionFunction,digitalPoint,pathImgBg,pathImgForeground);
		this.magicCardType=magicCardType;
		this.digitalPoint=digitalPoint;
	
		
		
	}

	public int getDamageDigitalPoint(){
		
		return digitalPoint;
	}
	
	public String getMagicCardType(){
		return magicCardType;
	}
	
	public String getmagicCardTypee(){
		return magicCardType;
	}

//	@Override
//	public void mouseClicked(MouseEvent e) {
//			
//		new MagicWorker(this).execute();
//		
//	}
//	
//	public class MagicWorker extends  SwingWorker<Void,Void>{
//		
//		private ItemCard ref;
//		
//		public MagicWorker(ItemCard ref ) {
//			this.ref=ref;
//		}
//
//		@Override
//		protected Void doInBackground() throws Exception {
//			// TODO Auto-generated method stub
//			Player aPlayer= aRoom.getPlayer();
//			if(aPlayer!=null){
//				
//				if(ref.isDropPlayercard()){
//					if(magicCardType.equals("potion"))
//						aPlayer.addLifeSpanPoint(digitalPoint);
//					
//					if(magicCardType.equals("normal"))
//						aPlayer.addDigitalPoint(digitalPoint);
//					
//					
//					if(magicCardType.equals("zero")){
//						ItemCard item =aPlayer.lastdropItemCard;
//						if( item instanceof InstrusionDetectorCard){
//							aPlayer.addLifeSpanPoint(((InstrusionDetectorCard) item).damageDigitalPoint);
//							aPlayer.instrusionDetectorRoom--;
//							aPlayer.playerInstrusionDetectorLevel--;
//							aPlayer.refPlayerProp.loadGUI(aPlayer);
//							
//						}
//					}
//					
//					aPlayer.removeItemFromList(ref);
//					ref.setVisible(false);
//					
//				
//
//				}else 
//				{
//					
//					
//					if(aPlayer.getNumberItemCarry() < aPlayer.ITEMCARRY){
//						new PopUpCardInfoGUI(ref);
//						aPlayer.addItemToList(ref);
//
//						
//						}
//						else {
//							String taskDescription ="\n \n"
//									+ "Player Carry item list has reach \n"
//									+ "it is limit "+aPlayer.getNumberItemCarry() +"/"+aPlayer.ITEMCARRY+"\n"
//									+ "to add new item card, please remove some item from your item list ...!!!!";
//							DisplayInformation disp =new DisplayInformation();
//							disp.setBounds(100, 100, 400, 200);
//							disp.aText.setText(taskDescription);
//						}
//				}
//			}
//			return null;
//		}
//	}



	
}

