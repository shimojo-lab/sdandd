package Entity;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;



public class RootEntity {

    private final int DISTANCE = 10;

    private int x;
    private int y;
   
    private BufferedImage image;

    public RootEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(BufferedImage img) {
        image = img;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public  BufferedImage createBuffredImage(String path) {
		java.net.URL imgURL = ItemCard.class.getResource(path);
		
		if (imgURL != null) {
			 try {
					return  ImageIO.read(new File(imgURL.getPath()));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} else {
			System.err.println("path not found );" + path);
	
		}
		return null;
	}
    
    public void move(int x, int y) {
        setX(getX()+x);
        setY(getY()+y);
    }

    public boolean isLeftCollision(RootEntity anEntity) {
    	
        if (((this.getX() - DISTANCE) == anEntity.getX()) &&
            (this.getY() == anEntity.getY())) {
         	//System.out.println("Left P.X "+(this.getX()-DISTANCE)+ " P.Y "+ this.getY() +" E.X "+ anEntity.getX() +" E.Y  "+anEntity.getY() );
            return true;
        } else {
       // 	System.out.println("Left P.X "+(this.getX()-DISTANCE)+ " P.Y "+ this.getY() +" E.X "+ anEntity.getX() +" E.Y  "+anEntity.getY() );
            return false;
        }
    }
    
    public boolean isRightCollision(RootEntity anEntity) {
    
        if (((this.getX() + DISTANCE) == anEntity.getX())
                && (this.getY() == anEntity.getY())) {
//       	System.out.println("Right P.X "+(this.getX()-DISTANCE)+ " P.Y "+ this.getY() +" E.X "+ anEntity.getX() +" E.Y  "+anEntity.getY() );
            return true;
            
        } else {
//        	System.out.println("right P.X "+(this.getX()-DISTANCE)+ " P.Y "+ this.getY() +" E.X "+ anEntity.getX()  +" E.Y  "+anEntity.getY() );
            return false;
        }
    }

    public boolean isTopCollision(RootEntity anEntity) {
   
        if (((this.getY() - DISTANCE) == anEntity.getY()) &&
            (this.getX() == anEntity.getX())) {
         	//System.out.println("top P.X "+(this.getX()-DISTANCE)+" P.Y "+ this.getY() + " E.X "+ anEntity.getX() +" E.Y  "+anEntity.getY() );
            return true;
        } else {
//        	System.out.println("top P.X "+(this.getX()-DISTANCE)+" P.Y "+ this.getY() + " E.X "+ anEntity.getX() +" E.Y  "+anEntity.getY() );
            return false;
        }
    }

    public boolean isBottomCollision(RootEntity anEntity) {
   
        if (((this.getY() + DISTANCE) == anEntity.getY())
                && (this.getX() == anEntity.getX())) {
//         System.out.println(" bottom P.X "+(this.getX()-DISTANCE)+" P.Y "+ this.getY() +  " E.X "+ anEntity.getX() +" E.Y  "+anEntity.getY() );
            return true;
        } else {
//        	System.out.println("bottom P.X "+(this.getX()-DISTANCE)+ " E.X "+ " P.Y "+ this.getY() + anEntity.getX() +" E.Y  "+anEntity.getY() );
//        	System.out.println(" root E.X "+ this.getX() +" E.Y  "+this.getY() ); 
        	return false;
        }
    }
}