package Entity;

import java.awt.Image;

import java.net.URL;

import javax.swing.ImageIcon;

public class Trap extends RootEntity {
	 
	  public boolean draw;
	  /*
	   * operation refer to positio of brick for example L,R,T,B  used in room for set up the fenced or 
	   * name of the brick for example trap1, trap2, trap3 for trap room 
	   * 
	   */
	  public String operation; 
	  public int width, height;

	    public Trap(int x, int y, int width, int height,  String imgPath ,String operation, boolean draw) {
	        super(x, y);
	        this.width=width;
	        this.height=height;
	        this.draw=draw;
	        this.operation=operation;
	        URL loc = this.getClass().getResource(imgPath);
	        if(loc !=null) {
	        	 this.setImage(createBuffredImage(loc.getPath()));
	        }
	        else 
	        	System.err.println("nullll");

	    }

}
