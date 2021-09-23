package GameUI;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
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

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import Entity.ItemCard;
import Entity.Player;

public class PopUpCardInfoGUI  extends JFrame implements ActionListener{

	private JButton exitBtn;
	private JTextArea meta, des;
	private ItemCard card;
	Player player;


	public PopUpCardInfoGUI(ItemCard aCard, Player player){
		
		card=aCard;
		this.player= player;
		

		setTitle("~"+aCard.getcardIdClass()+ "Details Information~");
		setBounds(300, 300, 600, 600);
		Container contentPane = getContentPane();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		contentPane.setLayout(gridbag);
		ImageIcon image = new ImageIcon(aCard.getBackgroundImg());
		JButton iconBut = new JButton(image);
		
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10,0,0,0);  //top padding
		c.ipady = 40;      //make this component tall
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(iconBut,c);

		meta = new JTextArea();
		meta.append("Card Name   \t: "+aCard.getcardIdClass() + "\n");
		meta.append("Room   \t: "+aCard.getItemRoom().roomID + "\n");
		meta.append("Digital Point   \t: "+aCard.getDigitalPoint() + "\n");
		
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(10,15,10,10);  //top padding
		contentPane.add(meta,c);
		
		
		
		des= new JTextArea();
		des.append("Card Description  : "+aCard.getDescriptionFunction()+ "\n");
		c.insets = new Insets(10,0,10,10);
		c.ipady = 80;      //make this component tall
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		
		JScrollPane scroll = new JScrollPane(des);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setPreferredSize(new Dimension(600,150));
		contentPane.add(scroll,c);
	
		aCard.setBackgroundImg(aCard.getBackgroundImg());	
		exitBtn=new JButton("Exit");
		exitBtn.addActionListener(this);


		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.anchor = GridBagConstraints.SOUTH; //bottom of space
		c.insets = new Insets(10,400,10,10);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridwidth = 2;   //2 columns wide
		c.gridy = 2;       //third row
		
		contentPane.add(exitBtn,c);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
     	player.refPlayerProp.repaint();
		dispose();

	}



}
