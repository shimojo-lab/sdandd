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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.IconView;

import GameUI.DropCardGUI;
import GameUI.GeneralJpanelView;
import GameUI.MMUI;
import GameUI.RoomUI;

public class TrapRoom extends Room {

	//private static final long serialVersionUID = 1L;

	private int intendMoveRoom = 0; // increment by one if player hit boundary
	private ImageIcon trapImg1,trapImg2,trapImg3,trapImg4;
	private JButton trapBtn1, trapBtn2,trapBtn3,trapBtn4;
	public Map<String, String> mapQuiz = new HashMap<String, String>();
	public boolean runTrap=false;
	public JTextArea text;
	public String [] keys;
	public JTextField textField;

	private static final int TRAPSIZE = 70;
	//protected ArrayList<Trap> trapsWall;
	public Vector<Brick> trapsWallImg;

	boolean retVal=false;

	RoomUI roomUIPanel = null;


	/*
	 * the following variable used for method trapRoomInterface()
	 */
	int simpleCorrectAnswer=0;
	public int index=0;
	private String roomName;


	public TrapRoom( int x, int y, String roomID, String roomName,boolean E_door, boolean W_door, boolean N_door, boolean S_door, Color col, Player aPlayer, RoomUI refRoomUI) {

		super(x,y,roomID,E_door, W_door,N_door,S_door,col, aPlayer,refRoomUI);
		this.roomName=roomName;
		//trapsWall= new ArrayList<Trap>();
		trapsWallImg= new Vector<Brick>();
		keys=loadMapQuiz(mapQuiz,"/questionDB.txt");



	}
	public Brick getBrikFromTrapRoom( String roomID, String operation) {

		for (Brick b : trapsWallImg) {
			if (b.roomID.equals(roomID) && b.operation.equals(operation))
				return b;
		}
		return null;
	}

	public String getRoomName() {
		return roomName;
	}

	public Vector<Brick> getTrapsWallBrick() {
		return trapsWallImg;
	}


	public NextRoom getTrapBrick(String dir) {
		NextRoom  ret = null;

		//System.err.println("listNextRoom size " +listNextRoom.size());
		for (NextRoom trap :getListNextRoom()) {
			//System.err.println("roomID " +trap.roomID + " dir " + trap.direction);
			if (trap.direction.equals(dir)) {
				if(trap.roomID.equals(this.roomID))
					ret = trap; 
				break;

			}
		}
		return ret;
	}

	public void setTrapDoor(String direction) {
		switch (direction){
		case "L": this.W_door=true; 
		break;
		case "R" : this.E_door=true;
		break;
		case "B" :this.N_door=true;
		break;
		case "T": this.S_door=true;
		break ;
		default :
			break ;

		}
	}
	public void loadTrapRoom() {
		
		NextRoom L = getTrapBrick("W");
		NextRoom R = getTrapBrick("E");
		NextRoom T = getTrapBrick("N");
		NextRoom B = getTrapBrick("S");
		
			int lx=L.getX()+10;
			int ly=L.getY()+50;
	
			int rx=R.getX()-10;
			int ry= R.getY()+50;
		
			int tx=T.getX()+50;
			int ty=T.getY()-10;
		
			int bx=B.getX()+50;
			int by=B.getY()+10;

		for (int i=0;i<6;i++) {
			if (i>0) {
				ly-=10;
				ry-=10;
				bx-=10;
				tx-=10;
			}
			if(!W_door)
				trapsWallImg.add(new Brick(this.roomID,lx,ly,"/wall_ver.png","L",true,"no",false));
			if(!E_door)
				trapsWallImg.add(new Brick(this.roomID,rx,ry,"/wall_ver.png","R",true,"no",false));
			if(!N_door)
				trapsWallImg.add(new Brick(this.roomID,bx,by,"/wall_hori.png","B",true,"no",false));
			if(!S_door)
				trapsWallImg.add(new Brick(this.roomID,tx,ty,"/wall_hori.png","T",true,"no",false));
		}

	}


	public void populateRoomUITraps() {

		for (Brick trap : trapsWallImg) {
			refRoomUI.roomUITrap.add(trap);
		}
	}



	public void drawTrapRoomProperties(Graphics g ) {
		// Draw Trap walls    
		for (Brick trap : trapsWallImg) {
			//if(trap.draw)
			g.drawImage(trap.getImage(), trap.getX() , trap.getY(),refRoomUI);

			// System.out.println("drwaing item x " +brick.getX() + " y "+ brick.getY());
		}
	}




	public void trapRoomInterface(String sourceComp,String welcome){

		runTrap=false; // use to guarantee  user have to drop his card  
		Player player = this.getPlayer();
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setSize(500, 500);
		panel.setVisible(true);
		text = new JTextArea();
		text.setSize(450, 250);
		textField = new JTextField();
		textField.setSize(450, 30);
		textField.setEditable(false);
		JLabel score = new JLabel();
		JLabel value = new JLabel();
		
	
		index=refRoomUI.MAPQUIZINDEX;
		
			


		panel.addMouseListener(new MouseAdapter(){  @Override
			public void mousePressed(MouseEvent e) {

			if (SwingUtilities.isRightMouseButton(e) )  
			{   
				if(getPlayer().getPlayerItemList().size()>0)
					for (ItemCard item : getPlayer().getPlayerItemList())
						item.setDropPlayercard(true);
				new DropCardGUI(getPlayer());

			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}
		});

		panel.setLayout(null);

		text.setBounds(20, 20, 500, 250);
		text.setEditable(false);
		if(runTrap)
		{
			textField.setEditable(true);
			text.setText(mapQuiz.get(keys[index]));
		}
		else
			text.setText(welcome);

		panel.add(text);
		score.setText("Correct Answer : ");
		score.setForeground(Color.RED);
		score.setBounds(20,270,120,30);
		panel.add(score);
		value.setText("0/3");
		value.setForeground(Color.RED);
		value.setBounds(120,270,50,30);
		panel.add(value);

		textField.setBounds(20,295,500,30);
		panel.add(textField);

		textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String answer="";
				if(index==mapQuiz.size() -1) {
					index=0; 
					refRoomUI.MAPQUIZINDEX=0;
					
				}
				
				if (simpleCorrectAnswer <3){
					answer=index+1+">"+textField.getText();
					if(answer.equalsIgnoreCase(keys[index])){
						simpleCorrectAnswer++;
						value.setText(simpleCorrectAnswer+"/3");

					}
					index++;
					if(index==keys.length)
						index=0;
					textField.setText("");
					text.setText(mapQuiz.get(keys[index]));
				}
				if(simpleCorrectAnswer == 3){
					text.setText("Congratualation you have win  trap room !!!! ");
					//							ItemCard item=player.lastdropItemCard;
					//							item.setDropPlayercard(true);
					//							player.addItemToList(item);
					player.addDigitalPoint(5);
					player.playerCurrentRoom().refRoomUI.removeTrap("",false);
					simpleCorrectAnswer=0;
					refRoomUI.MAPQUIZINDEX=index;
					frame.dispose();
					player.refPlayerProp.repaint();;

				}
			}
		});

		frame.add(panel);
		frame.setTitle("~TRAP ROOM~");
		frame.setBounds(100, 100, 560, 400);
		frame.setMinimumSize(new Dimension(500, 0));
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}


