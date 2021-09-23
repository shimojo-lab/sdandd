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


public class InstrusionDetectorCard extends ItemCard 
{
	
	
	public int damageDigitalPoint;
	private String instrusionType;
	//private ImageIcon removedIcon ;

	public InstrusionDetectorCard(Room aRoom, int x, int y, String instrusionType,int damageDigitalPoint, String cardId, String decriptionFunction,int digitalPoint, String pathImgBg, String pathImgForeground)
	{
		
		
		super(aRoom,x,y,cardId,decriptionFunction,digitalPoint,pathImgBg,pathImgForeground);
		this.instrusionType=instrusionType;
		this.damageDigitalPoint=damageDigitalPoint;
		
	
		
	}

	public int getDamageDigitalPoint(){
		
		return damageDigitalPoint;
	}
	
	public String getInstrusionType(){
		return instrusionType;
	}
	
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		
//		new IntrusionWorker(this).execute();
//		
//	}
//
//	public class IntrusionWorker extends  SwingWorker<Void,Void>{
//			
//			private ItemCard ref;
//			
//			public IntrusionWorker(ItemCard ref ) {
//				this.ref=ref;
//			}
//
//			@Override
//			protected Void doInBackground() throws Exception {
//
//				try{	
//					
//					
//					Player aPlayer= aRoom.getPlayer();
//					if(aPlayer!=null){
//						if(isDropPlayercard()){
//							aPlayer.removeItemFromList(ref);
//							
//							/*
//							 *  check for player is in wireless room and drop correct item 
//							 */
//							//System.out.println("task room nnn");
//							
//				
//							ref.setVisible(false);
//							aPlayer.refPlayerProp.loadGUI(aPlayer);
//						
//
//						}else 
//						{
//							
//							if(aPlayer.getNumberItemCarry() < aPlayer.ITEMCARRY){
//							new PopUpCardInfoGUI(ref);
//							aPlayer.addItemToList(ref);
//							
//							
//							aPlayer.damageLifeSpan(getDamageDigitalPoint());
//							aPlayer.addInstrusionLevel();
//							aPlayer.refPlayerProp.loadGUI(aPlayer);
//							
//							}
//							else {
//								String taskDescription ="\n \n"
//										+ "Player Carry item list has reach \n"
//										+ "it is limit "+aPlayer.getNumberItemCarry() +"/"+aPlayer.ITEMCARRY+"\n"
//										+ "to add new item card, please remove some item from your item list ...!!!!";
//								DisplayInformation disp =new DisplayInformation();
//								disp.setBounds(100, 100, 400, 200);
//								disp.aText.setText(taskDescription);
//							}
//								
//								
//							
//						}
//						
//						aPlayer.checkStatus();
//					}
//
//					} catch(java.lang.NullPointerException ex){}
//					
//					//this.setIcon(removedIcon);
//					//this.setEnabled(false);
//
//
//					/*
//					 * should fire remove item by player in this section ??
//					 * should checkPlayerwin or lose the games?? 
//					 * 
//					 */
//				return null;
//			}
//			
//	}

	
}

