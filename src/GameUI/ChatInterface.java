package GameUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import Entity.Brick;
import Entity.Room;






public class ChatInterface extends JPanel implements ActionListener {
	protected  int prefWidth;
	JTextField tfPlayer;
	public JTextArea text;
	JScrollPane scroll;
	JPanel panel;
	JLabel label;
	String netMsg ="";
	List<String> players;
	public MMUI ref;


public ChatInterface(MMUI ref) {
	this.setLayout(null);
	this.setMinimumSize(new Dimension(300, 300));
	players=new ArrayList<String>();
	prefWidth=300;
	this.setBackground(Color.LIGHT_GRAY);
	this.ref=ref;
	this.panel=new JPanel();
	panel.setLayout(new FlowLayout());
	
	label=new JLabel();
	tfPlayer= new JTextField();
	tfPlayer.setEditable(true);
	tfPlayer.setPreferredSize(new Dimension(prefWidth-20,40));
	tfPlayer.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			 netMsg ="7;"+ref.aPlayer.getPlayerName()+";"+ tfPlayer.getText();
			 ref.publisher.setNewMsg(netMsg);
			 tfPlayer.setText("");
		}

	});
	
	this.text= new JTextArea();
	this.text.setEditable(false);
	this.text.setVisible(true);

	scroll = new JScrollPane(this.text);
	scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	scroll.setPreferredSize(new Dimension(prefWidth,170));

	panel.add(scroll);
	panel.add(label);
	panel.add(tfPlayer); 
	panel.setVisible(true);
	panel.setBounds(0, 0, prefWidth, prefWidth);
	
	
	add(panel);
	

}

@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub

}
public void run_chat_Interface(ChatInterface panel) {

	JFrame frame = new JFrame("Chat box @ " + ref.aPlayer.getPlayerName());
	panel.setVisible(true);
	frame.add(panel);
	frame.setSize(new Dimension(300,250));
	frame.setVisible(true);

}


}


