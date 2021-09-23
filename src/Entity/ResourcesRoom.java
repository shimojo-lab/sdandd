package Entity;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import GameUI.RoomUI;

public class ResourcesRoom extends Room {

	private int intendMoveRoom = 0; // increment by one if player hit boundary
	private String roomName;
	
	public ResourcesRoom(int x, int y, String roomID,String roomName,  boolean E_door, boolean W_door, boolean N_door, boolean S_door,  Color col, Player aPlayer,RoomUI refRoomUI) {
		
		super(x,y,roomID, E_door, W_door,N_door,S_door, col,aPlayer,refRoomUI);
		this.roomName=roomName;

	}
	public String getRoomName() {
		return roomName;
	}


	// one room can load up to 12 different card 
	
	
}
