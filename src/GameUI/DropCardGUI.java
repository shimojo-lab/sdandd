package GameUI;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTextArea;

import Entity.Brick;
import Entity.ImageHolder;
import Entity.ItemCard;
import Entity.Player;
import Entity.Room;
import Entity.TrapRoom;

public class DropCardGUI extends JPanel{

	
	private JButton exitBtn, transfer;
	protected Player player;
	protected JRadioButton transferItem;
	JComboBox playerList, itemList;
	JLabel  label1, label2;
	boolean isexecuted;
	
	JFrame frame;
	



	public DropCardGUI(Player player){
		this.player=player;
		isexecuted=false;
		
		if(player.getNumberItemCarry() == 0 ) {
			player.refMMUI.errorMsgDisplay("You have 0 item in your ItemCarry list !!!", player.playerID,false);
			return;
		}
		
		if(player.getPlayerItemList().size()>0){
			frame = new JFrame();
			frame.setTitle(player.getPlayerName() + "'s List Cards  ");
			this.setLayout(null);
			setPreferredSize(new Dimension(500,400));
			
			transferItem = new JRadioButton("Transfer Card");
			transferItem.setBounds(20, 120, 150, 50);
			transferItem.setVisible(true);

			transferItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) 
				{
					
					setupTranferCard();
					repaint();

				}
			});

			exitBtn=new JButton("Exit");
			exitBtn.setVisible(true);
			exitBtn.addActionListener( new ActionListener (){

				@Override
				public void actionPerformed(ActionEvent e) {
					if(isexecuted == false)
						frame.dispose();
						
					Room room= player.playerCurrentRoom();
					if (room instanceof TrapRoom){
						String [] temp;
						temp = ((TrapRoom) room).keys;
						try {
						((TrapRoom) room).text.setText(((TrapRoom) room).mapQuiz.get(temp[((TrapRoom) room).index]));
						}catch (NullPointerException ex) {}
						isexecuted=false;
					}
					frame.dispose();
					
				}
				
			});

			
			this.addMouseListener(new MouseAdapter() {
				     public void mouseClicked(MouseEvent e) {

							try{	
								if(player!=null ){
									
									isexecuted=true;
									ItemCard itemCard = null;
									for (ItemCard item :player.getPlayerItemList()) {
										if(checkCollosion(e.getX(),e.getY(),item)) {
											itemCard=item;
											break;
										}
									
									}
									if(itemCard.isDropPlayercard()){
										player.removeItemFromList(itemCard);
										player.validate_Comsume_Item(itemCard);
										
										Room pRoom = player.playerCurrentRoom();
										//System.err.println("00000 Image path not found :airMonNG " +pRoom.roomID);
										if (pRoom instanceof TrapRoom){
											TrapRoom traproom=(TrapRoom) player.playerCurrentRoom();
											traproom.runTrap=true;
											traproom.textField.setEditable(true);
											
										}
										
										/*
										 * drop
										 */
										
										/*
										 *  check for player is in wireless room and drop correct item 
										 */
										
										if (pRoom instanceof TaksRoom){
											//System.err.println("1111 Image path not found :airMonNG " +pRoom.roomID);
											ItemCard item=player.lastdropItemCard;
											TaksRoom room = (TaksRoom) pRoom ;
											if (room.roomID.equals("TSR0")){
												if(remove_iString (item.cardId).equals("airMonNG")){
												  //	System.err.println(" Image path not found :airMonNG ");
													room.updateImageHolderDrawField(room.wireLess1.operation, true);
//													room.v1=true;
													//room.loadTaskRoomComp();
												}
												
												if(remove_iString(item.cardId).equals("AirePlayNG")){
												//  	System.err.println(" Image path not found : AirePlayNG");
													room.updateImageHolderDrawField(room.wireLess2.operation, true);
//													room.v2=false;
												//	room.loadTaskRoomComp();												
												}
												
												if(remove_iString (item.cardId).equals("AireCrackNG")){
												  //	System.err.println(" Image path not found : v ");
													room.updateImageHolderDrawField(room.wireLess3.operation,true);
//													room.v3=false;
													//room.loadTaskRoomComp();
												}
											}
											
											if (room.roomID.equals("TSR1")){
												if(remove_iString (item.cardId).equals("Password Cracker"))
													room.sosMedia.runDecode();
												else if(remove_iString(item.cardId).equals("SET")){
													room.sosMedia.actBut.setText("Run SET");
													room.sosMedia.actBut.setVisible(true);
													room.sosMedia.tfPlayer.setText("");
													room.sosMedia.TFDescription.setVisible(false);
												}else 
													room.sosMedia.setErrorMsg(item.cardId);
											}
											if (room.roomID.equals("TSR2") ){
												NetworkCard net;
												try {
													 net = (NetworkCard) item;
												}catch(java.lang.ClassCastException ex) {
												
													return;
												}
												//if(net.networkCardType.equals("NetCard"))
													net.setImage(item.getBackgroundImg());
												//net.setAttach_to_game(true);
												room.servers.add(net);
												room.getRefRoomUI().card_attached_to_roomUI.add(net.cardId);
												room.set_locality_attched_net_card(net,false,room.getRefRoomUI().refMMUI.defaultScreen);
												if(room.numberOfNetworkMounted == 7) {
														for(ImageHolder img : room.taskRoomImg) 
															if(img.operation.equals("NEXTLEVEL")) {
																img.draw=true;
																break;
															}
														//room.getRefRoomUI().repaint();
												
												}
												room.getRefRoomUI().repaint();
												player.refMMUI.publisher.setNewMsg("11;"+Integer.toString(player.playerID)+";"+item.cardId+";"+room.roomID);

											}


										}
										
									}

									
								}

								} catch(java.lang.NullPointerException ex){}
							player.refPlayerProp.repaint();
							repaint();
						}
							
				});
			
			}

		
			if(player.refMMUI.server || player.refMMUI.successfully_join_network) {
				add(transferItem);
				exitBtn.setBounds(400, 300, 70, 30);
				frame.setPreferredSize(new Dimension(520,420));
				
			}
			
			else {
				exitBtn.setBounds(400, 300, 70, 30);
				frame.setPreferredSize(new Dimension(520,420));
			}
			add(exitBtn,exitBtn.getBounds());
			frame.add(this);
			//frame.setDefaultCloseOperation(0);
					
			
			//		setUndecorated(true);
			//		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			frame.pack();
			frame.setVisible(true);
			//player.checkStatus();
		}
	// return string with our _i
	private String remove_iString (String _iString) {
		String [] split = _iString.split("_");
		return  split[0];
	}
	
	private void setupTranferCard() {
		
		label1= new JLabel();
		label1.setText("Transfer to :");
		label1.setBounds(40, 160, 100, 30);
		label1.setVisible(true);
		add(label1);
		
		label2= new JLabel();
		label2.setText("Select Card : ");
		label2.setBounds(40, 190, 100, 50);
		label2.setVisible(true);
		add(label2);
		
		String [] players = players();
		playerList = new JComboBox(players);
		playerList.setBounds(140, 170, 150, 30);
		playerList.setVisible(true);
		add(playerList);
		
		itemList = new JComboBox(playerItemCard());
		itemList.setBounds(140, 210, 150, 30);
		itemList.setVisible(true);
		add(itemList);
		
		transfer= new JButton("Transfer");
		transfer.setBounds(300, 250, 100, 30);
		transfer.setVisible(true);
		transfer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(playerList.getSelectedItem() == null || itemList.getSelectedItem() ==null)
					errorMsgDisplay( "Please select available item in drop list !!!!");
				else {
					
					String playerName =playerList.getSelectedItem().toString();
					String [] split = playerName.split(":");
					String card = itemList.getSelectedItem().toString();
					ItemCard item=getplayerItemCard(card);
					player.removeItemFromList(item);
					repaint();
					player.refMMUI.publisher.setNewMsg("8;"+split[0]+";"+split[1]+";"+item.cardId+";"+item.getItemRoom().roomID);
					player.refMMUI.playerProp.repaint();
					
				}
				
			}
			
		});
		add(transfer);
		frame.setVisible(true);
		 
		
	}
	
	private ItemCard getplayerItemCard(String itemID) {
		
		for (ItemCard item : player.getPlayerItemList())
			if (item.cardId.equals(itemID))
				return item;
		
		return null;
	}
	
	public void errorMsgDisplay(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}
	
	private String [] players() {
		int size =player.refMMUI.chatInterface.players.size();
		String [] p = new String[size];
		
		for (int i=0; i<size;i++)
			p[i]= player.refMMUI.chatInterface.players.get(i);
		return p;
		
	}


	private String [] playerItemCard() {
		
		String [] cards = new String[player.getNumberItemCarry()];
		
		for (int i=0; i<player.getPlayerItemList().size();i++)
			cards[i]= player.getPlayerItemList().get(i).cardId;
	return cards;
		
	}
	
	private boolean checkCollosion(int mouseXCoord, int mouseYCoord, ItemCard item) {
		

		for (int i=item.getX(); i<item.getX()+item.WBG;i++)
			for (int j =item.getY(); j<item.getY()+item.HBG;j++) {
				if(mouseXCoord==i && mouseYCoord==j)
					return true;
			}
		return false;
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(player == null )
			return;
		if (player.getPlayerItemList().size() > 0) {
			int xCord=10;
			int yCord=10;
			int offset=100;
			int incX=10;
			for (ItemCard item :player.getPlayerItemList()) {
			//	System.err.println("player item size "+player.getPlayerItemList().size() );
				if (incX==10) {
					item.setX(incX);
					g.drawImage(item.getImage(), incX , yCord, this);
				}
				else {
					item.setX(incX);
					g.drawImage(item.getImage(), incX , yCord, this);
				}
				item.setY(yCord);
				incX+=(xCord+offset);
			}
			}
		}
		
		
	}

	





