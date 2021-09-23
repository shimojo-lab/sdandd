package GameUI;
import java.awt.*;
import javax.swing.*;

import Entity.ItemCard;
import Entity.Player;

public class PlayerProp extends JPanel{
	private Player player;
	

	public PlayerProp(Player player, int minHeight, int minWidth, int prefHeight, int prefWidth, Color col)
	{
		player.setRefPlayerProp(this);
		this.setMinimumSize( new Dimension(minWidth, minHeight ) );
		//this.setPreferredSize( new Dimension( prefWidth, prefHeight ));
		this.setBackground(col);
		this.player=player;
		player.setRefPlayerProp(this);
		setLayout(null);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

//	public void paintComponent(Graphics g) { 
//		super.paintComponent(g);
		
		g.drawString(player.getPlayerName(), 0, 10);
		g.drawString("Digital Point :", 0, 43);
		g.drawString("Life-Span 	   :", 0, 73);
		
		g.drawString(" Intrusion Detector Level " +player.networkInstrusionDetectorLevel + "/6", 0, 100);
		g.drawString(" Room Intrusion Detector Level " +player.instrusionDetectorRoom + "/3", 0, 130);
		
		player.drawDigitalPoint(g, 90, 30);
		player.drawLifeSpan(g,90,60);
		g.drawLine(5, 170, 290, 170);
		
		if(player.getPlayerItemList().size()>0){	
			int i=0, increment=55;
			for (ItemCard item : player.getPlayerItemList()){
				g.drawImage(item.getImage(), i*increment ,190, this);
				i++;
			}
		}
		
		

	}

}
