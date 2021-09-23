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

public class DisplayInformation extends JFrame implements ActionListener{

	private JPanel panelText, panelImg,mainContainer ;
	private JButton exitBtn;
	public JTextArea aText;
	protected GridBagConstraints c ;
	protected GridBagLayout gridbag;
	protected Container contentPane;
	protected Entity.Player refPlayer;
	protected JScrollPane scroll;
	


	public DisplayInformation(String title){
		
		 createAndShowGUI(title);
		
	
	}
	
	 private  void createAndShowGUI(String title) {
	        //Make sure we have nice window decorations.
	        setDefaultLookAndFeelDecorated(true);
	        setTitle(title);
	        addComponentsToPane(getContentPane());

	        //Display the window.
	        pack();
	        setVisible(true);
	        setLocationRelativeTo(null);
	    }
	 
	 private void  addComponentsToPane(Container contentPane) {
		 
			gridbag = new GridBagLayout();
			c = new GridBagConstraints();
			contentPane.setLayout(gridbag);
			aText = new JTextArea();
			scroll = new JScrollPane(aText);
		    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		    scroll.setPreferredSize(new Dimension(600,300));

			//natural height, maximum width
			c.fill = GridBagConstraints.HORIZONTAL; 
			c.ipady = 80;      //make this component tall
			c.weightx = 0.0;
			c.gridwidth = 4;
			c.gridx = 0;
			c.gridy = 0;
			contentPane.add(scroll,c);

			exitBtn=new JButton("Exit");
			exitBtn.addActionListener(this);
			c.ipady = 0;       //reset to default
			c.weighty = 1.0;   //request any extra vertical space
			c.anchor = GridBagConstraints.PAGE_END; //bottom of space
			c.insets = new Insets(10,540,10,10);  //top padding
			
			c.gridx = 2;       //aligned with button 2
			c.gridwidth = 2;   //2 columns wide
			c.gridy = 2;       //third row
			contentPane.add(exitBtn,c);
	 }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(refPlayer!=null)
			refPlayer.reloadGame();
		dispose();


	}
	public void setPlayer(Entity.Player player){
		refPlayer=player;
	}

	public JTextArea getInfoTextArea(){
		return aText;
	}

}
