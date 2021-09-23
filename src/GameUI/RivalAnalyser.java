package GameUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.List;

import javax.swing.*;

import Entity.Brick;
import Entity.Player;
import Entity.Room;
import Entity.TrapRoom;



public class RivalAnalyser extends JPanel implements ActionListener {

	public  String [][] roomDesign;
	public ArrayList<Brick> walls;
	private ArrayList<Room> rooms;
	protected  int WIDTH;
	private int roomSize;
	JTextField tfPlayer;
	public JTextArea text;
	JScrollPane scroll;
	JPanel panel;
	JLabel label;
	String netMsg ="";
	
	
	public MMUI ref;


	public RivalAnalyser(MMUI ref, int minWidth, int minHeight, int prefWidth,
			int prefHeight, BorderLayout layout, Color col) {
		this.setLayout(null);
		this.setMinimumSize(new Dimension(minWidth, minHeight));
		this.WIDTH=prefWidth;
		this.setBackground(col);
		this.ref=ref;
		rooms= new ArrayList<Room>();
		walls=new ArrayList<Brick>();
		this.panel=new JPanel();
		panel.setLayout(new FlowLayout());
	
		
		label=new JLabel();
		
//		tfPlayer= new JTextField();
//		tfPlayer.setEditable(true);
//		tfPlayer.setPreferredSize(new Dimension(prefWidth,40));
//		tfPlayer.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				 netMsg ="7;"+ref.aPlayer.getPlayerName()+";"+ tfPlayer.getText();
//				 ref.publisher.setNewMsg(netMsg);
//				 tfPlayer.setText("");
//			}
//
//		});
		
//		this.text= new JTextArea();
//		this.text.setEditable(false);
//		this.text.setVisible(true);
//
//		scroll = new JScrollPane(this.text);
//		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		scroll.setPreferredSize(new Dimension(prefWidth,100));
//
//		panel.add(scroll);
		panel.add(label);
		//panel.add(tfPlayer); 
		panel.setVisible(true);
		panel.setBounds(0, prefWidth+10, prefWidth, 150);
		
		add(panel);
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	private void make_matrix_adjecent(int roomsize, List<Room> roomCollection) {
		roomDesign=new String[roomsize][roomsize];

		//System.err.println("roomcollection size" +roomCollection.size());
		int index=0;
		for(int i=0;i<roomsize;i++) {
			for(int j=0;j<roomsize;j++) {
				roomDesign[i][j]=roomCollection.get(index).roomID;
				index++;
			}
		}
	}
	

	public void buildRivalAnalyserEnvironment(int roomsize, List<Room> roomCollection) {
		//System.err.println("room size "+roomsize);
		roomSize=roomsize;
		int roomDimension = WIDTH /(roomsize);
		int incrementInner;
		int incrementOuter=roomDimension;
		int index=0;
		//int offsetX, offsetY;
		make_matrix_adjecent(roomsize,roomCollection);
		//	int offSet = (WIDTH % (roomsize));
		for(int i=0;i<roomsize;i++) {
			incrementInner=roomDimension;
			//offsetX=10*i;
			for(int j=0;j<roomsize;j++) {
				//offsetY=10*j;
				roomCollection.get(index).setX(incrementOuter);
				roomCollection.get(index).setY(incrementInner);
				roomCollection.get(index).roomSetUpRivalInterface(roomsize,roomCollection.get(index).getX(), roomCollection.get(index).getY(),0,0,roomDimension,roomCollection.get(index).roomID,this);
				rooms.add(roomCollection.get(index));
				incrementInner+=roomDimension;
				index++;
			}
			incrementOuter+=roomDimension;
		}
	//System.err.println("rival wall size "+ walls.size());
		repaint();
	}
	
	private Brick getSecondBrick(Room r) {
		int count=0;
		for (Brick b : walls ) {
			if (b.roomID.equals(r.roomID))
				count++;
			if (count == 5)
				return b;
		}
		return null;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(walls.size()>0)
			for (Brick b : walls) 
				g.drawImage(b.getImage(),b.getX(),b.getY(),this);
		int j=0;
		for(Room r : rooms ){
			g.drawString(r.roomID,getSecondBrick(r).getX()+(j*10), getSecondBrick(r).getY()-10);
			if(r.playerInRoom.size()>0){
				for (int i=0; i< r.playerInRoom.size();i++) {
					Player player= r.playerInRoom.get(i);
					if(player.self)
						g.setColor(Color.RED);
					else 
						g.setColor(Color.MAGENTA);
					
					if (i < (roomSize/2)) 
						g.fillOval(getSecondBrick(r).getX()+(j*10), getSecondBrick(r).getY()-40,20 , 20);
				}

			}

		}
	}
}


