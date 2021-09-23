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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import Entity.ItemCard;
import Entity.Room;


public class NetworkCard extends ItemCard 
{
	
	
	private int digitalPoint;
	/*
	 * potion --> add 5 point to life span 
	 * zero --> completely nullified intrusion detection card 
	 * normal --> reduce digital point damage  by the intrusion detection card 
	 */
	public String networkCardType;
	public String fileConfigurationPath,IPaddress,domainName;
	//private ImageIcon removedIcon ;
	private boolean drawOpacity;
	boolean attach_to_game=false;

	
	public NetworkCard(String IPaddress,String domainName, Room aRoom,int x,int y, String networkCardType,String cardId, String decriptionFunction,int digitalPoint, String pathImgBg, String pathImgForeground, String fileConfigPath)
	{
		
		super(aRoom,x,y,cardId,decriptionFunction,digitalPoint,pathImgBg,pathImgForeground);
		this.networkCardType=networkCardType;
		this.digitalPoint=digitalPoint;
		this.IPaddress=IPaddress;
		this.fileConfigurationPath=fileConfigPath;
		this.domainName=domainName;
		drawOpacity=false;
		
	}
	
	public boolean isAttach_to_game() {
		return attach_to_game;
	}
	public void setAttach_to_game(boolean attach_to_game) {
		this.attach_to_game = attach_to_game;
	}
	
	public void setDrawOpacity(boolean drawOpacity) {
		this.drawOpacity = drawOpacity;
	}
	public int getDamageDigitalPoint(){
		
		return digitalPoint;
	}
	
	public String getMagicCardType(){
		return networkCardType;
	}
	
	public String getmagicCardTypee(){
		return networkCardType;
	}
	
	public String checkServerConfiguration(String IPaddress) {
		String ret=null;
			if(this.IPaddress.equals(IPaddress)) {

			java.net.URL fileURL = Room.class.getResource(fileConfigurationPath);
			try {
				BufferedReader br = new BufferedReader(new FileReader(fileURL.getPath()));
				
				for (String line; (line = br.readLine()) != null;) {
					ret+=line+"\n";
				}
				br.close();
				}catch (IOException e) {

			}
					
			
			}
			return ret;
	}

	public boolean isDrawOpacity() {
		// TODO Auto-generated method stub
		return drawOpacity;
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

	


