package Entity;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class NextRoom extends RootEntity {

	
	public boolean draw;
	/*
	 * operation refer to positio of brick for example L,R,T,B  used in room for set up the fenced or 
	 * name of the brick for example trap1, trap2, trap3 for trap room 
	 * 
	 */
	public String nextRoom,direction,roomID;

	public NextRoom(String roomID, int x, int y, String imgPath ,String nextRoom,String direction, boolean draw) {
		
		super(x, y);
	//	System.out.println("next room " + nextRoom);
		this.roomID=roomID;
		this.draw=draw;
		this.nextRoom=nextRoom;
		this.direction=direction;
		this.setImage(createBuffredImage(imgPath));
		//System.err.println("effective call");

	}

}