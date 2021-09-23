package GameUI;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Entity.Brick;
import Entity.ItemCard;
import Entity.Player;
import Entity.ResourcesRoom;
import Entity.Room;
import Entity.TrapRoom;

public class DomParsersD_D {
	protected MMUI ref;

	public List<Room> rooms;
	protected int ROOMSIZE;
	


	protected List<ItemCard>  listDigitalAssetCard, listInstrusionDetectorCard, listMagicPatchCard, listNetworkCard , net_card, resources_card;

	

	public DomParsersD_D(MMUI ref) {
		rooms= new ArrayList<Room>();
		listDigitalAssetCard= new ArrayList<ItemCard>();
		listInstrusionDetectorCard= new ArrayList<ItemCard>();
		listMagicPatchCard= new ArrayList<ItemCard>();
		listNetworkCard= new ArrayList<ItemCard>();
		net_card =new ArrayList<ItemCard>();
		resources_card = new ArrayList<ItemCard>();
		
		ROOMSIZE=0;
		this.ref = ref;
		parserRoomComponent("roomFileConfig.xml");
		parserItemCardCompoment("ItemCardConfigFile.xml");
	}
	public int getROOMSIZE() {
		return ROOMSIZE;
	}

	public List<ItemCard> getListNetworkCard() {
		return listNetworkCard;
	}

	public List<ItemCard> getListDigitalAssetCard() {
		return listDigitalAssetCard;
	}

	public List<ItemCard> getListInstrusionDetectorCard() {
		return listInstrusionDetectorCard;
	}

	public List<ItemCard> getListMagicPatchCard() {
		return listMagicPatchCard;
	}

	

	public List<Room> getRooms() {
		return rooms;
	}

	public void parserRoomComponent(String XML_file_path) {
		int number_of_room=0;
		int x = 0;
		int y = 0;
		String roomID="";
		String taskID = "";
		String taskDescription = "";
		String imagePath = "";

		String roomName="";
		boolean E_door=false;
		boolean W_door=false;
		boolean N_door=false;
		boolean S_door=false;
		Color col=null;
		Player aPlayer=null;

		RoomUI refRoomUI=null;

		Document doc = parserDocument(XML_file_path);
		doc.getDocumentElement().normalize();
		
		NodeList nList = doc.getElementsByTagName("room");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String classType=eElement.getAttribute("class");

				if(classType.equals("TaskRoom")) {
					x=Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent());
					y=Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent());
					roomID =eElement.getElementsByTagName("roomID").item(0).getTextContent();

					taskID=eElement.getElementsByTagName("taskID").item(0).getTextContent();
					taskDescription=eElement.getElementsByTagName("taskDescription").item(0).getTextContent();
					imagePath=eElement.getElementsByTagName("imgPath").item(0).getTextContent();


					roomName=eElement.getElementsByTagName("roomName").item(0).getTextContent();
					col=null;
					aPlayer=null;
					refRoomUI=null;
				}
				else 
					number_of_room= Integer.parseInt(eElement.getElementsByTagName("numbero_of_room").item(0).getTextContent());
				
				E_door= Boolean.valueOf(eElement.getElementsByTagName("E_door").item(0).getTextContent());
				W_door= Boolean.valueOf(eElement.getElementsByTagName("W_door").item(0).getTextContent());
				N_door= Boolean.valueOf(eElement.getElementsByTagName("N_door").item(0).getTextContent());
				S_door= Boolean.valueOf(eElement.getElementsByTagName("S_door").item(0).getTextContent());

				switch(classType) {
				case  "TaskRoom" : 
					rooms.add(createTaskRoom( x,  y,  roomID, taskID,  roomName,  taskDescription,   E_door,  W_door,  N_door,  S_door,  col,  aPlayer,  imagePath, refRoomUI));
					break;
				case "ResorucesRoom" : 
					create_Trap_Resources_room( true,  number_of_room,E_door,  W_door,  N_door,  S_door) ;
					
					break;
				case "TrapRoom" : 
					create_Trap_Resources_room( false,  number_of_room,E_door,  W_door,  N_door,  S_door) ;
					break;
				default :
					break;
				}


			}
		}



	}



	public void parserItemCardCompoment(String XML_file_path ) {

		int number_of_item=0;
		int x = 0;
		int y = 0;
		String roomsID="";
		String cardID = "";
		String decriptionFunction = "";
		int digitalPoint=0;
		String pathImgBg = "";
		String pathImgForeground="";
		
		//card type : instrusion card 
		String instrusionType="";
		int damageDigitalPoint=0;

		//card type : magic_patch card 
		String magicCardType="";
		
		// card type : network card 
		String IPaddress = "";
		String domainName="";
		String networkCardType="";
		String fileConfigPath="";
		

		Document doc = parserDocument(XML_file_path);
		doc.getDocumentElement().normalize();

		NodeList nList = doc.getElementsByTagName("itemcard");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String classType=eElement.getAttribute("class");
				
				number_of_item= Integer.parseInt(eElement.getElementsByTagName("numbero_of_item").item(0).getTextContent());
				x=Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent());
				y=Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent());
				roomsID=eElement.getElementsByTagName("Room").item(0).getTextContent();
				cardID = eElement.getElementsByTagName("cardID").item(0).getTextContent();
				decriptionFunction = eElement.getElementsByTagName("decriptionFunction").item(0).getTextContent();
			    digitalPoint=Integer.parseInt(eElement.getElementsByTagName("digitalPoint").item(0).getTextContent());
				pathImgBg = eElement.getElementsByTagName("pathImgBg").item(0).getTextContent();
				pathImgForeground= eElement.getElementsByTagName("pathImgForeground").item(0).getTextContent();
				
				if(classType.equals("InstrusionDetectorCard")) {
				
					instrusionType=eElement.getElementsByTagName("instrusionType").item(0).getTextContent();
					damageDigitalPoint=Integer.parseInt(eElement.getElementsByTagName("damageDigitalPoint").item(0).getTextContent());
				}
				if(classType.equals("MagicPatchCard")) {
					magicCardType=eElement.getElementsByTagName("magicCardType").item(0).getTextContent();
					
				}
				if(classType.equals("NetworkCard")) {
					IPaddress = eElement.getElementsByTagName("IPaddress").item(0).getTextContent();
					domainName=eElement.getElementsByTagName("domainName").item(0).getTextContent();
					networkCardType=eElement.getElementsByTagName("networkCardType").item(0).getTextContent();
					fileConfigPath=eElement.getElementsByTagName("fileConfigPath").item(0).getTextContent();
				
				}
				
				switch(classType) {
				case  "ResourcesCard" : 
					create_Resources_card( number_of_item, x, y, roomsID, cardID,
							 decriptionFunction, digitalPoint, pathImgBg, pathImgForeground);
					break;
				case "InstrusionDetectorCard" : 
					create_InstrusionDetectorCard( number_of_item, instrusionType,
							 damageDigitalPoint, x, y, roomsID, cardID,
							 decriptionFunction, digitalPoint, pathImgBg, pathImgForeground);
					break;
				case "MagicPatchCard" : 
					create_MagicPatchCard( number_of_item, magicCardType, x, y, roomsID, cardID,
							 decriptionFunction, digitalPoint, pathImgBg, pathImgForeground);
					break;
				case "NetworkCard" : 
					create_NetworkCard(number_of_item, IPaddress, domainName, networkCardType, 
							fileConfigPath, x, y, roomsID, cardID, decriptionFunction, damageDigitalPoint, pathImgBg, pathImgForeground);
					break;
				default :
					break;
				}


			}
		}


		
		

	}

	public Document parserDocument(String XML_file_path) {

		Document doc;
		try {
			File inputFile = new File(XML_file_path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);

			return doc;


		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public TaksRoom createTaskRoom(int x, int y, String roomID,String taskID, String roomName, String taskDescription,  boolean E_door, 
			boolean W_door, boolean N_door, boolean S_door, Color col, Player aPlayer, String imagePath,RoomUI refRoomUI){
		ROOMSIZE++;
		return new TaksRoom(x,y,roomID,taskID,roomName,taskDescription,E_door, W_door,  N_door,  S_door,  col,  aPlayer,  imagePath, refRoomUI);
	}

	public ResourcesRoom createResroucesroom(int x, int y, String roomID,String roomName,  boolean E_door,
			boolean W_door, boolean N_door, boolean S_door,  Color col, Player aPlayer,RoomUI refRoomUI){

		return new ResourcesRoom(x,y,roomID,roomName,E_door, W_door,  N_door,  S_door,  col,  aPlayer, refRoomUI);
	}

	public TrapRoom createTrapRoom( int x, int y, String roomID, String roomName,boolean E_door,
			boolean W_door, boolean N_door, boolean S_door, Color col, Player aPlayer, RoomUI refRoomUI){

		return new TrapRoom(x,y,roomID,roomName,E_door, W_door,  N_door,  S_door,  col,  aPlayer, refRoomUI);
	}

	public void create_Trap_Resources_room(boolean resourceRoom, int number_of_room,boolean E_door, 
			boolean W_door, boolean N_door, boolean S_door) {

		for (int i=0; i<number_of_room;i++) {
			if (resourceRoom)
				rooms.add(new ResourcesRoom(0,0,"RS"+i,"Resource Room "+i,E_door, W_door,  N_door,  S_door,Color.GRAY,null,null));
			else
				rooms.add(new TrapRoom(0,0,"TRR"+i,"Trap Room "+i,E_door, W_door,  N_door,  S_door,Color.GRAY,null,null));

		}
	}
	
	public void create_Resources_card(int number_of_item,int x,int y,String roomsID,String cardID,
			String decriptionFunction,int digitalPoint,String pathImgBg,String pathImgForeground) {
		
		String [] split=null;
		if(!roomsID.equals("null")) {
			split=roomsID.split(":");
		}
		for (int i=0; i< number_of_item;i++) {
			if(i==0)
				resources_card.add(new ResourcesCard(null,  x, y,  cardID+"_"+i, 
						decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground));
			 
			if (split !=null && i < split.length) 
				new ResourcesCard(getRoomObject(split[i]),  x, y,  cardID+"_"+i, 
						decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground);

			else 
				listDigitalAssetCard.add(new ResourcesCard(null,  x, y,  cardID+"_"+i, 
						decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground));
		}
		
		
	}
	
	public void create_InstrusionDetectorCard(int number_of_item,String instrusionType,
			int damageDigitalPoint,int x,int y,String roomsID,String cardID,
			String decriptionFunction,int digitalPoint,String pathImgBg,String pathImgForeground) {
		
		String [] split=null;
		if(!roomsID.equals("null")) {
			split=roomsID.split(":");
		}
		
		for (int i=0; i< number_of_item;i++) {
			 
			if (split !=null && i < split.length) 
				new  InstrusionDetectorCard(getRoomObject(split[i]),  x,  y,  instrusionType,
						damageDigitalPoint,  cardID+"_"+i,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground);
//			listInstrusionDetectorCard.add(new  InstrusionDetectorCard(getRoomObject(split[i]),  x,  y,  instrusionType,
//					damageDigitalPoint,  cardID,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground));
			
			else 
				listInstrusionDetectorCard.add(new  InstrusionDetectorCard(null,  x,  y,  instrusionType,
						damageDigitalPoint,  cardID+"_"+i,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground));
		}
		
		
	}
	public void create_MagicPatchCard(int number_of_room,String magicCardType,int x,int y,String roomsID,String cardID,
			String decriptionFunction,int digitalPoint,String pathImgBg,String pathImgForeground) {
		
		String [] split=null;
		if(!roomsID.equals("null")) {
			split=roomsID.split(":");
		}
		
		for (int i=0; i< number_of_room;i++) {
			 
			if (split !=null && i < split.length) 
				new  MagicPatchCard(getRoomObject(split[i]),  x,  y,  magicCardType,
						  cardID+"_"+i,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground);
//			listMagicPatchCard.add(new  MagicPatchCard(getRoomObject(split[i]),  x,  y,  magicCardType,
//					  cardID,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground));
			
			else 
				listMagicPatchCard.add(new  MagicPatchCard(null,  x,  y,  magicCardType,
						  cardID+"_"+i,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground));
		}
		
	}
	
	public void create_NetworkCard(int number_of_item,String IPaddress,String domainName,
			String networkCardType,String fileConfigPath,int x,int y,String roomsID,String cardID,
			String decriptionFunction,int digitalPoint,String pathImgBg,String pathImgForeground) {
		
		String [] split=null;
		if(!roomsID.equals("null")) {
			split=roomsID.split(":");
			
		}
		
		for (int i=0; i< number_of_item;i++) { 
			if(i==0)
				net_card.add(new  NetworkCard( IPaddress, domainName, null, x, y,  
						networkCardType, cardID+"_"+i,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground,  fileConfigPath));
			if (split !=null && i < split.length) 
				new  NetworkCard( IPaddress, domainName, getRoomObject(split[i]), x, y,  
						networkCardType, cardID+"_"+i,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground,  fileConfigPath);
			
			else 
				listNetworkCard.add(new  NetworkCard( IPaddress, domainName, null, x, y,  
						networkCardType, cardID+"_"+i,  decriptionFunction, digitalPoint,  pathImgBg,  pathImgForeground,  fileConfigPath));
		}
		
	}
	
	
	public Room getRoomObject(String id) {
		Room ret=null;
		for (Room room : rooms) {
			if (room.roomID.equals(id)) {
				ret =room;
				break;
			}
		}

		return ret;

	}



}
