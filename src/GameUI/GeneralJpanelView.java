package GameUI;

import static javax.swing.GroupLayout.Alignment.TRAILING;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;

import Entity.ItemCard;
import Entity.Player;

public class GeneralJpanelView extends JPanel{

	private JPanel panelText, panelImg,mainContainer ;
	private JButton exitBtn;
	private Player player;

	public GeneralJpanelView(Vector<ItemCard>  playerItemList){

		if(playerItemList.size() >0){
			
			GridBagLayout gridbag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			setLayout(gridbag);
			setOpaque(false);

			
			//natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.5;
			int i=0;

			for (ItemCard aCard : playerItemList){
				c.gridx = i;
				c.gridy = 0;
//				gridbag.setConstraints(aCard, c);
//				add(aCard);
				i++;

			}
			this.setOpaque(false);

			setVisible(true);
		}
	}






}
