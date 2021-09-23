package GameUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import Entity.ItemCard;
import Entity.Player;

public class SosMediaUI extends JFrame {

	protected boolean runWelco;
	ImageIcon img;
	JButton actBut;
	JLabel TFDescription;
	JTextField tfPlayer,tfVictem;
	JPanel panel;
	Player player;
	// text for general information text2 for user interaction 
	JTextArea text,text2,textVictem;
	
	boolean p1,p2; // phase 1..n
	boolean terminate=false;
	JLabel iconVictem;
	String [] key1, key2, key3;
	protected Map<String, String> map1 = new HashMap<String, String>(), map2 = new HashMap<String,
			String>(), map3 = new HashMap<String, String>();
	// reference to room 
	TaksRoom room;
	private String setLog="";

	int index=0,index2=0;
	JPanel scrollPanel;
	TaksRoom refTaskRoom;

	public SosMediaUI(Player player,JPanel panel,JScrollPane scroll, JTextArea text,JLabel iconVictem, String instruction, String input,String input2,boolean runWelco){

		this.player=player;
		refTaskRoom= (TaksRoom) this.player.playerCurrentRoom();
		this.panel=panel;
//		this.text=text;
		this.text = new JTextArea(24,32);
		this.iconVictem=iconVictem;
		this.text.setEditable(false);
		p1=false;
		tfVictem=new JTextField();
		tfVictem.setVisible(false);
		
		text2= new JTextArea();
		text2.setVisible(false);
		textVictem=new JTextArea();
		textVictem.setVisible(false);
		
	   

		img = createImageIcon("/lock.png");
		//panel.setBounds(r);
		panel.setLayout(null);

		iconVictem.setBounds(20, 20, 300, 280);
		iconVictem.setIcon(img);
	

		//text.setBounds(320, 20, 320, 280);
		scroll.setPreferredSize(new Dimension(500,280) );
		scrollPanel= new JPanel();
		scrollPanel.setBounds(320, 20, 500, 280);
		scrollPanel.setLayout(new FlowLayout());
		scrollPanel.add(scroll);
		scrollPanel.setVisible(true);
		panel.add(scrollPanel);
		
		
		
//		this.runWelco=runWelco;
//		if(runWelco)
//		{
//			this.text.setText(input);
//		}
//		else{
//			this.text.setText(instruction);
//			runWelco=true;
//		}
//		
		
		panel.add(iconVictem);
		panel.add(tfVictem);
		panel.add(text2);
		panel.add(textVictem);
		

		loadDecode();
		panel.addMouseListener(new MouseAdapter(){  @Override
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

		add(panel);
		setBounds(50, 50, 880, 650);
		//pack();
		setVisible(true);
	}

	public void runDecode(){
		actBut.setVisible(true);
		TFDescription.setVisible(true);
		tfPlayer.setVisible(true);

	}
	public void setMapKey1(Map<String, String> map,String [] key){
		key1=key;
		map1=map;

	
	}
	public void setMapKey2(Map<String, String> map, String []key){
		key2=key;
		map2=map;
	}
	public void setMapKey3(Map<String, String> map, String []key){
		key3=key;
		map3=map;
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
	public void loadDecode(){
		
		String s = "Mission complete see \n SET log for victem credential \n";
		tfPlayer=new JTextField();
		tfPlayer.setBounds(330,360,320,40);
		tfPlayer.setVisible(false);
		tfPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!p2)
					loadPhase1();
				else{
					
					textVictem.setText(split(tfPlayer.getText()));
					tfVictem.setText(key3[index2]);
					text2.setText(tfVictem.getText());
					index2++;
					tfPlayer.setText(map3.get(key3[index2]));
					
					System.err.println("prit index2 f "+index2 + map3.size());
					if(index2==map3.size()){
						terminate=true;
						room.setUPSosMediaDB("map2");
						tfPlayer.setText("enter to exit");
						//tfPlayer.setEnabled(false);
						text2.setText(s);
						img = createImageIcon("/login.png");
						iconVictem.setIcon(img);
						refTaskRoom.ta.append("\n"+map2.get(key2[0]));
				
						player.setNumberCompletedTask();
						player.addDigitalPoint(75);
						room.taskCompleted=true;
						room.getRefRoomUI().send_Net_Msg_Task_Completed(player, room.roomID);
					}
						if(terminate) {
						if(tfPlayer.getText().equals("")) {
							player.checkStatus();
							dispose(); 
						}
						
					}
					
				}


			}
		});

		TFDescription = new JLabel();
		TFDescription.setText("Input the Pass Word");
		TFDescription.setBounds(330,338,200,30);
		TFDescription.setVisible(false);
		actBut= new JButton();
		actBut.setBounds(450, 310, 100, 40);
		actBut.setText("Decode");
		actBut.setVisible(false);
		actBut.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				if(!p1) 
					runDecodePwd();
				else 
					runSET();
			}

			

		});

		panel.add(TFDescription);
		panel.add(actBut);
		panel.add(tfPlayer);


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
	protected void runDecodePwd() {

		Random randomGenerator = new Random();
		SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() throws Exception {
				Integer r1,r2,r3,r4;
				String s;
				for (int i = 0; i < 50; i++) {

					Thread.sleep(10);
					r1=randomGenerator.nextInt(10);
					r2=randomGenerator.nextInt(10);
					r3=randomGenerator.nextInt(10);
					r4=randomGenerator.nextInt(10);
					s=r1.toString()+r2.toString()+r3.toString()+r4.toString();
					publish(s);
				}
				return null;
			}


			@Override
			protected void process(List<String> chunks) {
				refTaskRoom.ta.setText(chunks.get(chunks.size() - 1).toString());
				
			}

			@Override
			protected void done() {
				actBut.setVisible(false);
				p1=true;
			}
		};
		worker.execute();
	}
	
	protected void runSET() {
		refTaskRoom.ta.setText("");
		room = (TaksRoom) player.playerCurrentRoom();
		SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() throws Exception {
				room.setUPSosMediaDB("map1");
				String s;
				for(Map.Entry<String, String> entry : map1.entrySet()){ 
					
					Thread.sleep(200);
					s=entry.getKey()+ "\n" +entry.getValue();
					publish(s);
					
				}
				return null;
			}


			@Override
			protected void process(List<String> chunks) {
				refTaskRoom.ta.append(chunks.get(chunks.size() - 1).toString());
				//text.setText(chunks.get(chunks.size() - 1).toString());
				//setLog+=refTaskRoom.ta.getText();
				///System.err.println(setLog);
			}

			@Override
			protected void done() {
				room.setUPSosMediaDB("map3");
				actBut.setVisible(false);
				img = createImageIcon("/fb.png");
				iconVictem.setIcon(img);
				textVictem.setBounds(20,320,270,230);
				textVictem.setVisible(true);
				tfVictem.setBounds(20,550,270,50);
				tfVictem.setVisible(true);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//text.setBounds(300, 20, 280, 280);
				text2.setBounds(400,320,270,230);
				text2.setVisible(true);
				tfPlayer.setBounds(400,550,270,50);
				tfPlayer.setText(map3.get(key3[index2]));
				
				
				p2=true;
				
				
				
			}
		};
		worker.execute();
		
	}
	
	

	public void loadPhase1(){
		String ins;
		
		if(p1)
			if(tfPlayer.getText().equals(refTaskRoom.ta.getText())){
				img = createImageIcon("/contactlist.png");
				iconVictem.setIcon(img);
								
				ins= "Succesfully cracked victem phone PIN.  \nNext use the contact list \n "
						+ "and sabotage victem social network credential. \n "
						+ "tool required SET";
				
				refTaskRoom.ta.setText(ins);
				
				
			}
			
	}
	
	

	public  ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ItemCard.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

}
