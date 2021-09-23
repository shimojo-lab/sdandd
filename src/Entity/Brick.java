package Entity;

import java.awt.Image;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Brick extends RootEntity {
	 
	  public boolean draw;
	  public String roomID;
	  /*
	   * operation refer to positio of brick for example L,R,T,B  used in room for set up the fenced or 
	   * name of the brick for example trap1, trap2, trap3 for trap room 
	   * 
	   */
	  public String operation,center; 
	  
	  /*
	   * access wall nextroom (silver wall) false
	   * false --> default access granted 
	   * true ---> access denied 
	   */
	 boolean nextDoorAccess;

	   
		public Brick( String index, int x, int y, String imgPath ,String operation, boolean draw,String center,boolean nextDoorAccess) {
	    	
	        super(x, y);
	        this.roomID=index;
	        this.draw=draw;
	        this.operation=operation;
		    this.setImage(createBuffredImage(imgPath));
		    this.nextDoorAccess=nextDoorAccess; 
		    this.center=center;
	    }
	    
	    public Brick getBrick(List<Brick>list, String op) {
	    	
	    	for (Brick b : list)
	    		if (b.operation.equals(op))
	    			return b;
	    	return null;
	    	
	    }
	    
	    
	    public boolean isNextDoorAccess() {
			return nextDoorAccess;
		}

		public void setNextDoorAccess(boolean nextDoorAccess) {
			this.nextDoorAccess = nextDoorAccess;
		}
		
		public void setCenter(String value) {
			this.center=value;
		}

	    
	    

}
