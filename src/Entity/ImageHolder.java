package Entity;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageHolder  {
	  private BufferedImage image;
	public boolean draw;
	boolean drawOpacity;
	  public int x,y, W,H;
	  
	  
	

	public String roomID;
	
	  /*
	   * operation refer to positio of brick for example L,R,T,B  used in room for set up the fenced or 
	   * name of the brick for example trap1, trap2, trap3 for trap room 
	   * 
	   */
	  public String operation; 

	    public ImageHolder( String roomID, int x, int y, int W, int H, String imgPath ,String operation, boolean draw) {
	       this.x=x;
	       this.y=y;
	       this.W=W;
	       this.H=H;
	       drawOpacity=false;
	 
	        this.draw=draw;
	        this.operation=operation;
	        URL loc = this.getClass().getResource(imgPath);
	        if(loc !=null) {
		       // ImageIcon imageIcon = new ImageIcon(loc);
		        try {
					image = ImageIO.read(new File(loc.getPath()));
					 this.setImage(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     
	        }

	    }
	    
	  
	    
	    public boolean isDrawOpacity() {
			return drawOpacity;
		}



		public void setDrawOpacity(boolean drawOpacity) {
			this.drawOpacity = drawOpacity;
		}



		public BufferedImage getImage() {
			return image;
		}
	    
		private void setImage(BufferedImage image) {
			// TODO Auto-generated method stub
			this.image=image;
			
		}
		
		  public int getX() {
				return x;
			}

			public void setX(int x) {
				this.x = x;
			}

			public int getY() {
				return y;
			}

			public void setY(int y) {
				this.y = y;
			}

			public int getW() {
				return W;
			}

			public void setW(int w) {
				W = w;
			}

			public int getH() {
				return H;
			}

			public void setH(int h) {
				H = h;
			}

}
