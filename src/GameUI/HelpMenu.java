package GameUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import Entity.Brick;
import Entity.ItemCard;
import Entity.Player;

@SuppressWarnings("serial")
public class HelpMenu extends JPanel {

	JLabel intro,main;
	BufferedImage space, shiftA,shiftC, mouse, arrow,shift_s, client_menu, server_menu, standalone_menu;
	JLabel lSpace, lShiftA,lShiftC,lMouse,Larrow, LShift_s;
	JScrollPane scroll;
	String intro_s, main_s;



	public HelpMenu(){
		setLayout(null);
		this.setVisible(true);

		intro_s = "<html>Welcome to Network game simulation. The game aims to simulate network security"
				+ "issues to raise awareness of network security education to the users."
				+ "Throughout the game the user will learn security concept and tools a hacker may use "
				+ "to attack a network. "
				+"A player has to solve three different tasks to win the game.  The player can move around "
				+ "from room to room to pick the necessary card for solving the security assignment."
				+ "The Task detail can be view by moving to the Task room and selected the icon information,"
				+ "followed by press the 'space key'. <br/> <br/>"
				+ "If a player has pickup intrusion type of card, then the player must drop it immediately "
				+ " and solve it. for example, used the patch type of card "
				+ "<br/> <br/> The following are a list of a key that should be used in this game: </html>";

		main_s="<html>The game can be played as a team(network game) or stand-alone.\n"
				+ " Figure 1 illustrates how to start the stand-alone game. For network game, \n"
				+ " a server has to start first, followed by client see Figure 2 and Figure 3 \n </html>";

		intro = new JLabel();
		intro.setVisible(true);
		intro.setText(intro_s);
		intro.setBounds(7,2, 700, 180);

		main = new JLabel();
		main.setVisible(true);
		main.setText(main_s);
		main.setBounds(7, 520, 700, 50);

		space=createBuffredImage("/space.png");
		shiftA=createBuffredImage("/shiftA.png");
		shiftC=createBuffredImage("/shiftC.png");
		mouse=createBuffredImage("/mouse.png");
		arrow=createBuffredImage("/arrow.png");
		shift_s=createBuffredImage("/shift_s.png");
		client_menu=createBuffredImage("/client_menu.png");
		server_menu=createBuffredImage("/server_menu.png");
		standalone_menu=createBuffredImage("/standalone.png");

		Larrow = new JLabel("<html> --> use arrow key to move from room to room </html>");
		Larrow.setVisible(true);
		Larrow.setBounds(150, 180, 450, 30);

		lSpace = new JLabel("<html> --> Use space key  to : <br/>"
				+ "1. draw or pick up the selected card <br/>"
				+ "2. open new window for trap room quiz </html>"); 
		lSpace.setVisible(true);
		lSpace.setBounds(150, 210, 450, 50);

		lShiftA = new JLabel ("<html> --> Use Shift+A key to: <br/>"
				+ "1.open new window of yours deck of card (access your items list)</html>");
		lShiftA.setVisible(true);
		lShiftA.setBounds(150, 290, 450, 30);

		lShiftC =  new JLabel ("<html> --> Use Shift+C key to: <br/>"
				+ "1.open new chat window to comunicating with other team members </html>");
		lShiftC.setVisible(true);
		lShiftC.setBounds(150, 330, 450, 50);
		
		LShift_s= new JLabel ("<html> --> Use Shift+S key to: <br/>"
				+ "1.Shuffle the card </html>");
		LShift_s.setVisible(true);
		LShift_s.setBounds(150, 370, 450, 80);

		lMouse = new JLabel("<html> --> use mouse to select or do some operation in new window.<br/>"
				+ "1. mouse can not be use in main window <br/> "
				+ "2. right mouse click to open new window. for example to use or select new card <br/> "
				+ "3. left mouse click to select cards in the deck or simply click function </html>");
		lMouse.setVisible(true);
		lMouse.setBounds(150, 430, 450, 80);
	
		this.add(intro);
		this.add(main);
		add(Larrow);
		add(lSpace);
		add(lShiftA);
		add(lShiftC);
		add(lMouse);
		add(LShift_s);
		setPreferredSize(new Dimension(700,900));


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

	public void run_help_menu(HelpMenu panel) {

		JFrame frame = new JFrame("Help");
		JPanel pan = new JPanel();
		panel.setVisible(true);
		
		pan.setVisible(true);
		pan.setLayout(new BorderLayout());
	    pan.add(panel);
	    
	    JScrollPane scroll = new JScrollPane(pan);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setVisible(true);
		frame.add(scroll);
		frame.setSize(new Dimension(750,500));
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	


	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(arrow,10,180,this);
		g.drawImage(space,10,230,this);
		g.drawImage(shiftA,10,290,this);
		g.drawImage(shiftC,10,340,this);
		g.drawImage(shift_s,10,380,this);
		g.drawImage(mouse,10,430,this);

		g.drawImage(standalone_menu,10,560,this);
		g.drawImage(server_menu,320,560,this);
		g.drawImage(client_menu,100,690,this);

	}


}
