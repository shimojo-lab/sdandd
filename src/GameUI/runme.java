package GameUI;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.SwingUtilities;

public class runme {
	

	public static void main(String[] args) {
		
//		Player aPlayer=new Player(null,"player1", 60, 60);
//		PlayerProp playerProp = new PlayerProp (100, 100, 300, 300, new BorderLayout(),Color.black);
		 	//MMUI runme = new MMUI();
		
		MMUI runme = new MMUI();
		
		  SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		     		runme.showFrame();
	        }
	      });
		 
		 
		
	}

}
