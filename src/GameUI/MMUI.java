package GameUI;



import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;
import static javax.swing.GroupLayout.Alignment.TRAILING;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import Entity.ItemCard;
import Entity.Player;
import Entity.PlayerRegitDB;
import Entity.ResourcesRoom;
import Entity.Room;
import Entity.TrapRoom;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MMUI extends JFrame {

	private static final String TITLE = "sD&D Security Board Game";
	protected HelpMenu help_menu;
	private JSplitPane splitPaneLRoot;
	private JSplitPane splitPaneSideToolsPane;
	//private JSplitPane newSplitPane;
	protected PlayerProp playerProp;
	public RivalAnalyser rivalAnalyser;
	private JMenuBar mainMenuBar;
	public JPanel mainContainer;
	private boolean reviewExerciseMode = false;
	private Room b,l,r;
	public ChatInterface  chatInterface;
	public int SCREEN_X=800, SCREEN_Y=760;
	public boolean defaultScreen;


	protected List<ItemCard>  listDigitalAssetCard, listInstrusionDetectorCard, listMagicPatchCard, listNetworkCard;
	protected Player aPlayer;
	public List<Player> listPlayer;
	protected List<Room> roomCollection;
	protected List<Room> roomCollectionClient;
	protected List<PlayerRegitDB> playerDB;
	// store last qty of carry item by the player 
	int lastQtyCarryItem =0;
	protected DomParsersD_D xml_parser;

	/*
	 * Network parameter 
	 */



	public  MulticastPublisher publisher;
	protected MulticastReceiver  reciever;
	protected NetworkMessageBuffer MSGBuffer;
	public boolean server, netINITROOM, LOGIN=false,successfully_join_network;

	public int numberOfPlayer;

	String roomProperties;
	
	private boolean syn=false;


	/*
	 * room definition 
	 * use to define next room position 
	 * manipulate user's move 
	 * 
	 */
	public int roomSize;

	//private Room [][] roomDesign=new Room[3][3];

	// deal with random room generation ( synchronise with positioning of room at server side with client app)
	public 	String serverRoomPositioning, playerLoc ;

	// synchoronise server room item with the apps 
	String itemAtRoom;

	/* important network parameter for exchange information 
	 *  0 : initialization parameter
	 *  1 : request initialization parameter
	 *  2 : request update resources in the room example remove item from the room 
	 *  3 : chat message, exchange message betwen player 
	 *  4 : request item from other player
	 */

	public MMUI() {

		/*
		 *  multi cast property
		 */
		System.setProperty("java.net.preferIPv4Stack", "true");
		server=false;
		serverRoomPositioning="1"; // init parameter
		playerLoc="5";
		netINITROOM=false;
		successfully_join_network=false;
		defaultScreen=true;

		setTitle(TITLE);
		setBackground(Color.gray);

		// Create the panels
		xml_parser = new DomParsersD_D(this);
		createMenu();
		aPlayer=new Player(null,1,"", 0, 0,true);
		aPlayer.setRefMMUI(this);

		chatInterface=new ChatInterface(this);

		// Create a splitter pane
		splitPaneLRoot = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		help_menu = new HelpMenu();

		splitPaneSideToolsPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);


		splitPaneSideToolsPane.setTopComponent(playerProp);
		splitPaneSideToolsPane.setBottomComponent(rivalAnalyser);
		splitPaneLRoot.setLeftComponent(splitPaneSideToolsPane);
		//splitPaneLR.setRightComponent(newSplitPane);

		mainContainer = new RoomUI(aPlayer,this);
		mainContainer.setPreferredSize(new Dimension(WIDTH ,HEIGHT ));


		//gameLoadRoom();



		splitPaneLRoot.setRightComponent(mainContainer);
		this.setJMenuBar(mainMenuBar);
		//list = new JList(inData);

		getContentPane().add(splitPaneLRoot);

		listDigitalAssetCard = new ArrayList<ItemCard>();
		listInstrusionDetectorCard = new ArrayList<ItemCard>();
		listMagicPatchCard = new ArrayList<ItemCard>();
		listNetworkCard= new ArrayList<ItemCard>();
		listPlayer=new ArrayList<Player>();
				
		if(xml_parser.getRooms() !=null)
			for (ItemCard r : xml_parser.getListDigitalAssetCard()) {
				System.err.println("parser the room xml "+ r.cardId);
			}

	}

	public String [] Room_Dimension() {

		int array_len = xml_parser.getROOMSIZE();

		if(array_len == 0) {
			String [] dimension = {"4"};
			return dimension;
		}

		if(array_len < 0) 
			return null;

		String [] dimension = new String[array_len];
		for (int i=4;i<=xml_parser.getROOMSIZE();i++) {
			dimension[i-4]=Integer.toString(i);
		}

		return dimension;
	}

	public void reloadMMGames(){
		dispose();
		new MMUI().showFrame();
		//showFrame();

	}

	public boolean duplicateUserName(String name) {
		for (PlayerRegitDB p : playerDB)
			if (p.playerName.equals(name))
				return true;
		return false;
	}


	public void runhelp() {
		help_menu.run_help_menu(help_menu);
	}

	public void buildGameWorld(String typeOfGame, String fileConfiguration) {
		roomCollection=new ArrayList<Room>();

		roomCollection = xml_parser.getRooms();
		if (typeOfGame.equals("server")||typeOfGame.equals("standalone")) {
			getRoomObjectMMUI("RS0").addPlayerToRoom(aPlayer);
			aPlayer.setCurrentRoom(getRoomObjectMMUI("RS0"));
		}

	}

	private int [] room_proprotion(int roomSize) {
		/*
		 * index 0 --> resources room (rooms-taskroom/(2/3))
		 * index 1 --> trap room  (rooms-taskroom/(1/3))
		 * index 3 --> task room (roomSize)
		 */

		double roomPropotion= (roomSize*roomSize)-roomSize;
		double tmp1, tmp2, tmp3;
		int bool=0;
		int tmp=0;
		int [] ret = new int [3];
		tmp1=roomPropotion*(2.0/3.0);
		ret[0]= (int) tmp1;

		tmp2=roomPropotion*(1.0/3.0);
		tmp=(int)tmp2;

		bool=(int)roomPropotion-(ret[0]+tmp) ;
		if(bool > 0 )
			ret[1]=tmp+1;
		else 
			ret[1]=tmp;
		ret[2]= roomSize;



		return ret;

	}

	/*
	 * depending on game type game, the game construction is made. for example network game or stand alone. 
	 * some important parameter such as boolean will be used to decide game design\
	 * 
	 * file configuration is the positioning of the room and item inside the room
	 * 
	 */

	boolean RS=true, TRR=false,TS=false;
	public void loadGameRoom(String typeOfGame, String fileConfiguration, int roomSize ){


		List <Room>roomCol=new ArrayList<Room>();
		int counter=0;

		int[] room_propotion= room_proprotion(roomSize);
		Random random = new Random();
		successfully_join_network=true;
		//set the player to RS0
		buildGameWorld( typeOfGame, fileConfiguration);
		/*
		 *  
		 */
		List <Room> taskRoom = new ArrayList<Room>();

		for (int iter=0; iter<roomCollection.size();iter++) {
			if(RS) {
				if(roomCollection.get(iter).roomID.equals("RS"+counter)) {
					roomCol.add(roomCollection.get(iter));

					if(counter < room_propotion[0]-1)
						counter++;
					else {
						counter=0;
						RS=false;
						TRR=true;
					}
				}
			}

			if(TRR) {
				if(roomCollection.get(iter).roomID.equals("TRR"+counter)) {
					roomCol.add(roomCollection.get(iter));
					if(counter < room_propotion[1]-1)
						counter++;
					else {
						counter=0;
						TRR=false;
						TS=true;
					}
				}
			}

			if(TS) {
				if(roomCollection.get(iter).roomID.equals("TSR"+counter)) {
					roomCol.add(roomCollection.get(iter));
					if(counter < room_propotion[2]-1)
						counter++;
					else {
						TS=false;
						break;
					}
				}
			}

		}


		/*
		 * Positioning room in the game 
		 */
		Collections.shuffle(roomCol);

		if (typeOfGame.equals("standalone"))
		{
			((RoomUI) mainContainer).proceedGameEnvironment(roomSize, roomCol,this.aPlayer.playerCurrentRoom().roomID);
			((RoomUI) mainContainer).standalone=true;
			((RoomUI) mainContainer).repaint();
			successfully_join_network=false;
			runhelp();
			return;
		}

		if (typeOfGame.equals("server")) {
			//Collections.shuffle(roomCol);
			if(server) {

				for(int i=0; i<roomCol.size();i++) {
					serverRoomPositioning = serverRoomPositioning+";"+roomCol.get(i).roomID;
				}
				if(roomSize>3) {
					((RivalAnalyser) rivalAnalyser).buildRivalAnalyserEnvironment( roomSize,roomCol);
				}
				((RoomUI) mainContainer).proceedGameEnvironment(roomSize, roomCol,this.aPlayer.playerCurrentRoom().roomID);
				//System.err.println(serverRoomPositioning);
			}
		}
		else {
			roomCollectionClient = new  ArrayList<Room>();;
			int rand = random.nextInt(roomSize*roomSize);
			roomCol.get(rand).playerInRoom.add(aPlayer);
			aPlayer.setCurrentRoom(roomCol.get(rand));
			String [] roomID=fileConfiguration.split(";");
			for(int k=2;k<roomID.length;k++) {
				if(roomID[k].equals("SYNSERVER"))
				{
					System.err.println("kaakajalaljadgldjgldaslgjaslgjadgjadgsljjlgsal");
					syn=true;
					break;
				}
				
				if(getRoomObjectMMUI(roomID[k])==null)
					break;
				roomCollectionClient.add(getRoomObjectMMUI(roomID[k]));
			}
			if(roomSize>3) {
				((RivalAnalyser) rivalAnalyser).buildRivalAnalyserEnvironment( roomSize,roomCollectionClient);
			}
			((RoomUI) mainContainer).proceedGameEnvironment(roomSize, roomCollectionClient,this.aPlayer.playerCurrentRoom().roomID);
		}

		playerLoc=playerLoc+";"+aPlayer.playerID+";"+aPlayer.getPlayerName()+";"+aPlayer.playerCurrentRoom().roomID+";none";

		if(!server )
			publisher.setNewMsg(playerLoc);
		if(syn)
			publisher.setNewMsg("13");
		runhelp();

	}
	public void send_network_msg(String netMsg) {
		if(successfully_join_network)
			publisher.setNewMsg(netMsg);
	}
	public void loadRoomItem(List<Room> rooms,boolean defaultroom, RoomUI roomUI) {
		createGameCard();
		for (Room r : rooms) {
			auxLoadRoomGlobalItem (r,roomUI);
		}

	}

	// listDigitalAssetCard, listInstrusionDetectorCard,listMagicPatchCard,listNetworkCard
	private void auxLoadRoomGlobalItem(Room room,RoomUI roomUI) {
		String roomName = room.roomID;

		switch(roomName){
		case "RS0" :  room.loadGlobalRoom(populateItemCardInRoom(3,0,1,3), roomUI);
		break;
		case "RS1" : room.loadGlobalRoom(populateItemCardInRoom(2,1, 1,3),roomUI);
		break;
		case "RS2" : room.loadGlobalRoom(populateItemCardInRoom(0,2,2,1),roomUI);
		break;
		case "RS3" : room.loadGlobalRoom(populateItemCardInRoom(2,2,1,0),roomUI);
		break;
		case "RS4" : room.loadGlobalRoom(populateItemCardInRoom(1,2,1,0),roomUI);
		break;
		case "RS5" : room.loadGlobalRoom(populateItemCardInRoom(1,1,1,0),roomUI);
		break;
		case "RS7" : room.loadGlobalRoom(populateItemCardInRoom(1,1,1,0),roomUI);
		break;
		case "RS8" : room.loadGlobalRoom(populateItemCardInRoom(0,0,1,0),roomUI);
		break;
		case "RS9" : room.loadGlobalRoom(populateItemCardInRoom(0,1,0,0),roomUI);
		break;
		case "RS10" : room.loadGlobalRoom(populateItemCardInRoom(0,0,1,0),roomUI);
		break;
		case "RS11" : room.loadGlobalRoom(populateItemCardInRoom(1,0,1,1),roomUI);
		break;
		case "RS12" : room.loadGlobalRoom(populateItemCardInRoom(1,1,1,1),roomUI);
		break;
		case "RS13" : room.loadGlobalRoom(populateItemCardInRoom(1,0,1,0),roomUI);
		break;
		case "RS14" : room.loadGlobalRoom(populateItemCardInRoom(1,0,0,1),roomUI);
		break;
		case "RS15" : room.loadGlobalRoom(populateItemCardInRoom(1,1,0,0),roomUI);
		break;
		case "TRR0" :room.loadGlobalRoom(populateItemCardInRoom(0,2,1,1),roomUI);
		break;
		case "TRR1" : room.loadGlobalRoom(populateItemCardInRoom(1,1,2,0),roomUI);
		break;
		default:
			break;
		}
	}


	public void errorMsgDisplay(String msg, int playerID, boolean win) {
		if(aPlayer.playerID==playerID)
			JOptionPane.showMessageDialog(this, msg);
		if (win) {
			JOptionPane.showMessageDialog(this, msg);
			this.reloadMMGames();
		}
	}

	public Room getRoomObjectMMUI(String id) {
		Room ret=null;
		for (Room room : roomCollection) {
			if (room.roomID.equals(id)) {
				ret =room;
				break;
			}
		}

		return ret;

	}

	/*
	 * special item scenario need to be define unless player move freely in between room 
	 * 
	 */

	int count=0;
	// listDigitalAssetCard, listInstrusionDetectorCard,listMagicPatchCard,listNetworkCard
	public ItemCard[] populateItemCardInRoom(int card1,int card2, int card3,int card4){
		count++;
		if(card1 > listDigitalAssetCard.size() )
			card1=listDigitalAssetCard.size();
		if(card2 > listInstrusionDetectorCard.size() )
			card2=listInstrusionDetectorCard.size();
		if(card3 > listMagicPatchCard.size() )
			card3=listMagicPatchCard.size();
		if(card4 > listNetworkCard.size() )
			card4=listNetworkCard.size();

		int sizeRoom=card1+card2+card3+card4;
		ItemCard [] temp= new ItemCard [sizeRoom];
		//int [] rand = randGen(sizeRoom);
	

		for (int i =0; i < sizeRoom; i++){
			card1--;
			if (card1 >= 0){		
				temp[i]=listDigitalAssetCard.get(0);
				listDigitalAssetCard.remove(temp[i]);
			} 
			else {
				card2--;
				if(card2>=0){
					temp[i]=this.listInstrusionDetectorCard.get(0);
					listInstrusionDetectorCard.remove(temp[i]);
				}
				else{
					card3--;
					if(card3>=0){
						temp[i]=this.listMagicPatchCard.get(0);
						listMagicPatchCard.remove(temp[i]);

					}
					else{
						card4--;
						if(card4>=0){
							temp[i]=this.listNetworkCard.get(0);
							listNetworkCard.remove(temp[i]);

						}
					}
				}
			}

		}
	return temp;


	}


	public int [] randGen(int size){
		int [] temp =new int [size];

		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=0; i<size; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);

		for (int i=0; i<size; i++) {
			temp [i]=list.get(i);
		}

		return temp;
	}

	public void createGameCard(){
		listDigitalAssetCard.clear();
		listDigitalAssetCard=xml_parser.getListDigitalAssetCard();

		listInstrusionDetectorCard.clear();
		listInstrusionDetectorCard=xml_parser.getListInstrusionDetectorCard();

		listMagicPatchCard.clear();
		listMagicPatchCard=xml_parser.getListMagicPatchCard();

		listNetworkCard.clear();
		listNetworkCard=xml_parser.getListNetworkCard();
	}

	public void createplayerProp(Player player) {
		playerProp = new PlayerProp (player, 300, 300, 300, 300, Color.LIGHT_GRAY);
	}


	public void createRivalAnalyser() {
		rivalAnalyser = new RivalAnalyser(this,300, 300, 300, 300,
				new BorderLayout(), Color.LIGHT_GRAY);

	}


	public void createMenu() {
		mainMenuBar = new JMenuBar();
		JMenu applicationMenu = new JMenu("Application");
		JMenuItem menuRoomLoad = new JMenuItem("Load Game");
		JMenuItem serverSetup = new JMenuItem("Server Setup");
		//		JMenu DBMenu = new JMenu("  ");
		//		JMenuItem runDB = new JMenuItem("  ");
		//		JMenuItem stopDB = new JMenuItem("  ");
		JCheckBoxMenuItem help = new JCheckBoxMenuItem(
				"How to play !!!");

		JMenuItem menuItemExit = new JMenuItem("Exit");
		JMenu viewMenu = new JMenu("Help");

		// networ configuration 

		JLabel ip = new JLabel ("IP Address: ");
		JLabel port = new JLabel ("Port  : ");
		JLabel playerName = new JLabel ("Player Name : ");
		JLabel numberOfPlayer = new JLabel ("Number of Player : ");
		JLabel roomSize = new JLabel ("Room size > 3 : ");
		JTextField ipField = new JTextField(20);
		JTextField portField = new JTextField(20);
		JTextField playerNameField = new JTextField(20);
		JTextField numberOfPlayerField = new JTextField(20);
		JComboBox roomSizeField = new JComboBox(Room_Dimension());



		help.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				help_menu.run_help_menu(help_menu);
			}
		});
		menuRoomLoad.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				final JFrame selectGame = new JFrame();
				selectGame.setPreferredSize(new Dimension(520,180));
				JButton okButton = new JButton("Ok");
				selectGame.setTitle("Game Scenario");
				selectGame.setBackground(Color.gray);
				JPanel listPanel = new JPanel();
				listPanel.setLayout(new GridLayout(2,0,0,0));

				JRadioButton netGame, standAlone;
				JPanel labelPanel = new JPanel();
				//labelPanel.setPreferredSize(new Dimension(250, 250));
				labelPanel.setLayout(new GridLayout(3, 0, 1, 1));
				ipField.setText("230.0.0.1");
				portField.setText("3001");
				labelPanel.add(ip);
				labelPanel.add(ipField);
				labelPanel.add(port);
				labelPanel.add(portField);
				labelPanel.add(playerName);
				labelPanel.add(playerNameField);
				labelPanel.setVisible(false);
				
				standAlone = new JRadioButton("Stand Alone", true);
				standAlone.setActionCommand("Alone");
				netGame = new JRadioButton("Network");

				standAlone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) 
					{
						standAlone.setSelected(true);
						netGame.setSelected(false);
						labelPanel.setVisible(false);
						selectGame.pack();

					}
				});


				netGame.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) 
					{
						standAlone.setSelected(false);
						labelPanel.setVisible(true);
						selectGame.pack();

					}
				});

				listPanel.add(standAlone);
				listPanel.add(netGame);

				JPanel okButtonPanel = new JPanel();

				okButtonPanel.setLayout(new BoxLayout(okButtonPanel, BoxLayout.LINE_AXIS));
				okButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
				okButtonPanel.add(Box.createHorizontalGlue());
				okButtonPanel.add(okButton);
				okButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) 
					{

						if(standAlone.isSelected())
						{
							//	VCE.getInstance().loadScenario(scenarioChoice);
							loadGameRoom("standalone","none",3);
							aPlayer.setPlayerName("StandAlone");
							createplayerProp(aPlayer);
							createRivalAnalyser();

							splitPaneSideToolsPane.setTopComponent(playerProp);
							splitPaneSideToolsPane.setBottomComponent(rivalAnalyser);
							//maxFrame();
							//selectGame.setVisible(false);
							selectGame.dispose();
						}
						else
						{
							if(netGame.isSelected()) {


								if(playerNameField.getText().isEmpty())
									JOptionPane.showMessageDialog(selectGame, "Please enter the server Parameter");
								else {
									try {
										netINITROOM=true;
										loadNetworkParameter(Integer.parseInt(portField.getText()),ipField.getText(),
												playerNameField.getText(), 0, 0);
										//										loadNetworkParameter(3001,"230.0.0.1",
										//												"test", 0, 0);

										createplayerProp(aPlayer);
										createRivalAnalyser();
										splitPaneSideToolsPane.setTopComponent(playerProp);
										splitPaneSideToolsPane.setBottomComponent(rivalAnalyser);
										selectGame.dispose();



									} catch (NumberFormatException | IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}


								}
							}
							//
						}
						maxFrame();
					}
				});
				selectGame.getContentPane().add(listPanel, BorderLayout.PAGE_START);
				selectGame.getContentPane().add(labelPanel, BorderLayout.WEST);
				selectGame.getContentPane().add(okButtonPanel, BorderLayout.PAGE_END);

				selectGame.setVisible(true);
				selectGame.pack();
				selectGame.setLocationRelativeTo(null);
			}
		});

		/*
		 * Initialization of network games
		 */

		serverSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// check whether task room size is >= 4 



				final JFrame serverConf = new JFrame();
				serverConf.setPreferredSize(new Dimension(560,200));
				serverConf.setTitle("Server Set up ");
				serverConf.setBackground(Color.gray);
				if (Room_Dimension() == null) {
					JOptionPane.showMessageDialog(serverConf, "Insuffient Rooms Please contact administrator "
							+ "to add rooms via roomFileConfig.xml ");
					return;
				}
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(5,0,0,0));


				ipField.setText("230.0.0.1");
				portField.setText("3001");
				panel.add(ip);
				panel.add(ipField);
				panel.add(port);
				panel.add(portField);
				panel.add(playerName);
				panel.add(playerNameField);
				panel.add(numberOfPlayer);
				panel.add(numberOfPlayerField);
				panel.add(roomSize);
				panel.add(roomSizeField);

				JPanel okButtonPanel = new JPanel();
				JButton okButtonS = new JButton("Ok");
				okButtonPanel.setLayout(new BoxLayout(okButtonPanel, BoxLayout.LINE_AXIS));
				okButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
				okButtonPanel.add(Box.createHorizontalGlue());
				okButtonPanel.add(okButtonS);
				okButtonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
				okButtonS.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) 
					{
						if(playerNameField.getText().isEmpty() || numberOfPlayerField.getText().isEmpty()) {
							JOptionPane.showMessageDialog(serverConf, "Please enter the server Parameter");
							return;
						}

						if(Integer.parseInt(numberOfPlayerField.getText()) <= 0 ) {
							JOptionPane.showMessageDialog(serverConf, "Number of player must greater or equal  1 !!!!");
							return;
						}
						if(roomSizeField.getSelectedItem() == null)
							JOptionPane.showMessageDialog(serverConf,  "Please select available size of room  in drop list !!!!");

						server=true;
						playerDB= new ArrayList<PlayerRegitDB>();


						createplayerProp(aPlayer);
						createRivalAnalyser();


						splitPaneSideToolsPane.setTopComponent(playerProp);
						splitPaneSideToolsPane.setBottomComponent(rivalAnalyser);
						//((RoomUI) mainContainer).buildGameEnvironment(3, "");




						try {
							loadNetworkParameter(Integer.parseInt(portField.getText()),ipField.getText(),
									playerNameField.getText(), Integer.parseInt(numberOfPlayerField.getText()),
									Integer.parseInt(roomSizeField.getSelectedItem().toString()));
							//														loadNetworkParameter(3001,"230.0.0.1",
							//																"player1", 4, 4);

						} catch (NumberFormatException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						maxFrame();
						serverConf.dispose();

					}



				});


				serverConf.getContentPane().add(panel, BorderLayout.PAGE_START);
				serverConf.getContentPane().add(okButtonPanel, BorderLayout.PAGE_END);

				serverConf.setVisible(true);
				serverConf.pack();
				serverConf.setLocationRelativeTo(null);

			}
		});

		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				System.exit(0);
			}
		});



		viewMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: To be done as part of view exercise
			}
		});




		applicationMenu.add(menuRoomLoad);
		applicationMenu.add(serverSetup);
		applicationMenu.addSeparator();		
		applicationMenu.add(menuItemExit);
		viewMenu.add(help);

		mainMenuBar.add(applicationMenu);
		mainMenuBar.add(viewMenu);
	}

	public void showFrame() {
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int x = gd.getDisplayMode().getWidth();
		int y = gd.getDisplayMode().getHeight();
		System.out.println(y);
		if(y<800) {
			SCREEN_X=800;
			SCREEN_Y=620;
			defaultScreen=false;
		}
		
		this.setPreferredSize(new Dimension(SCREEN_X,SCREEN_Y));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		

	}

	public void  maxFrame() {
		//this.setExtendedState(this.MAXIMIZED_BOTH);
		setSize(((RoomUI) mainContainer).getRoomWidth()+ 370,
				((RoomUI) mainContainer).getRoomHeight() + 110);
		this.setLocationRelativeTo(null);
	}

	public void loadNetworkParameter(int port, String IP,String player, int numberOfPlayer, int roomSize ) throws IOException {
		aPlayer.setPlayerName(player);
		if (server) {
			serverRoomPositioning+=";"+Integer.toString(roomSize);
			loadGameRoom("server","none",roomSize);
		}
		MSGBuffer= new NetworkMessageBuffer(this);
		aPlayer.setPlayerName(player);
		publisher = new MulticastPublisher(port, IP);
		publisher.setrefMMUI(this);
		reciever = new MulticastReceiver (port,IP,MSGBuffer);
		publisher.start();
		reciever.start();
		if (server) {
			aPlayer.playerID=1;
			playerDB= new ArrayList<PlayerRegitDB>();
			playerDB.add(new PlayerRegitDB(1, aPlayer.getPlayerName(),"Admin"));
			publisher.setConfFile(serverRoomPositioning);
			this.numberOfPlayer=numberOfPlayer;
			this.roomSize=roomSize;
		}
		else {
			aPlayer.playerID=0;
			publisher.setNewMsg("0;"+aPlayer.playerID+";"+aPlayer.getPlayerName()+";F");
		}

	}


	public ItemCard getItemCard(List<ItemCard> items, String id, String ref) {
		int i=0;
		//System.out.println(ref + " size "+items.size());
		while(i< items.size()) {
			//System.out.println(" my item "+items.get(i).cardId+ " compare "+ id );
			if(items.get(i).cardId.equals(id)) {

				return items.get(i);

			}
			i++;

		}
		return null;

	}
	private void addPlayerToChatInterface(int playerId,String playername) {
		String newplayer=Integer.toString(playerId)+" : "+playername;
		boolean insert=false;
		if(chatInterface.players.size()==0)
			chatInterface.players.add(newplayer);
		else {

			for (String s :chatInterface.players) {
				if (s.equals(newplayer)) {
					insert=false;
					break;
				}
				else 
					insert=true;
			}
			if(insert)
				chatInterface.players.add(newplayer);
		}

	}
	public void addPlayerToRoom(int playerId,String playername, String newRoom, String currentRoom) {
		//		if (this.aPlayer.getPlayerName().equals(playername))
		//			return;
		addPlayerToChatInterface(playerId,playername);
		Room room = null;
		boolean runDefaultRoom=false;
		if(((RoomUI) mainContainer).getRoomObjectRoomUI(newRoom)==null) {
			if(((RoomUI) mainContainer).getRoomObjectGlobalRoomUI(newRoom)!=null)
				room =((RoomUI) mainContainer).getRoomObjectGlobalRoomUI(newRoom);

		}
		else {
			room =((RoomUI) mainContainer).getRoomObjectRoomUI(newRoom);
			runDefaultRoom=true;
		}


		if(!room.isPlayerInRoom(playername)) {

			if(room.getListPlayerInRoom().size()>1) {
				Player p = room.getListPlayerInRoom().get(room.getListPlayerInRoom().size()-1);
				if(p.self) 
					room.addPlayerToRoom(new Player(room,playerId,playername,room.getX()-125,room.getY()-125,false));

				else
					room.addPlayerToRoom(new Player(room,playerId,playername,p.getX()+10,p.getY(),false));
			}
			else
				room.addPlayerToRoom(new Player(room,playerId,playername,room.getX()-125,room.getY()-125,false));
			//publisher.setNewMsg(playerLoc);
			publisher.setNewMsg("5;"+this.aPlayer.playerID+";"+aPlayer.getPlayerName()+";"+aPlayer.playerCurrentRoom().roomID+";"+"none");
		}

		if(!currentRoom.equals("none")) {
			if(((RoomUI) mainContainer).getRoomObjectRoomUI(currentRoom)!=null) {
				room =((RoomUI) mainContainer).getRoomObjectRoomUI(currentRoom);
				for(Player player : room.playerInRoom) {
					if(player.getPlayerName().equals(playername)) {
						room.removePlayerFromRoom(player);
						break;
					}

				}
			}
			else {
				room =((RoomUI) mainContainer).getRoomObjectGlobalRoomUI(currentRoom);
				for(Player player : room.playerInRoom) {
					if(player.getPlayerName().equals(playername)) {
						room.removePlayerFromRoom(player);
						break;
					}
				}

			}

		}
		//				else
		//					publisher.setNewMsg(playerLoc);

		if(runDefaultRoom) {
			if(room.getRefRoomUI()!=null)
				room.getRefRoomUI().repaint();
		}
		this.rivalAnalyser.repaint();
	}

}
