package GameUI;

import java.util.Timer;
import java.util.TimerTask;

import Entity.Brick;
import Entity.ItemCard;
import Entity.PlayerRegitDB;
import Entity.Room;
import Entity.TrapRoom;


public class NetworkMessageBuffer {

	private boolean available= false;
	private String data;
	protected MMUI ref;
	protected RoomUI refRoomUI;
	protected int counter_reconnect_server;
	protected 	Timer timer  ;
	protected boolean userThreshold , runTimer;
	boolean while_wait;


	public NetworkMessageBuffer(MMUI ref) {

		this.ref=ref;
		counter_reconnect_server=0;
		userThreshold=false;
		runTimer=false;
		refRoomUI=(RoomUI) ref.mainContainer;
	}

	public int getCounter_reconnect_server() {
		return counter_reconnect_server;
	}

	public void setCounter_reconnect_server() {
		this.counter_reconnect_server++;
	}


	public synchronized void put (String x){
		while(available){
			try{
				wait();
			}catch (InterruptedException e){}
		}
		data=x;
		available=false;
		processNetworkMsg(x);
		//notify();
		notifyAll();

	}

	public synchronized String get(){
		while(!available){
			try{
				wait();
			}catch (InterruptedException e){}

		}
		available=true;
		//notify();
		notifyAll();
		return data;
	}

	public void processNetworkMsg(String data) {

		String [] inputData=data.split(";");
		int reqTagParameter= Integer.parseInt(inputData[0]);

		switch(reqTagParameter) {
		case 0: authenticationRequest(data); 
		break;
		case 1: respondInitNetGame(data); 
		break;
		case 2: requestInitNetGame(); 
		break;
		case 3: removeRoomCard(data);  
		break ;
		case 4: removeTrap(data);
		break;
		case 5: updateOtherPlayerLocation(data); 
		break;
		case 6: authenticationRespond(data);  
		break;
		case 7: chatMsg(data); 
		break;
		case 8: exchangeCard(data); 
		break;
		case 9: reshuffle(data);
		break;
		case 10: dequeue_player_item(data);
		break;
		case 11: net_card_attached_to_roomUI(data);
		break;
		case 12: syn_server_pplayer(data);
		break;
		case 13: syn_server();
		break;
		case 14: set_task_complete(data);
		break;
		case 15: IDS_detected(data);
		break;
		case 16: nullified_IDS(data);
		break;
		case 88: updateServerfield(); 
		break;
		case 99: gameOver(data); 
		break;
		default :
			break ;

		}
	}

	private void nullified_IDS(String data2) {
		String [] split = data.split(";");
		int id = Integer.parseInt(split[1]);
		int no_ids = Integer.parseInt(split[2]);
		Room r = refRoomUI.getRoomObjectGlobalRoomUI(split[3]);
		boolean default_room=false;
		if (refRoomUI.getRoomObjectRoomUI(split[3])==null)
			r=refRoomUI.getRoomObjectGlobalRoomUI(split[3]);
		else {
			r=refRoomUI.getRoomObjectRoomUI(split[3]);
			default_room=true;
		}

		if(ref.aPlayer.playerID !=id) {
			ref.aPlayer.nullified_IDS_Door(r,no_ids);
			ref.aPlayer.networkInstrusionDetectorLevel--;
			ref.aPlayer.refPlayerProp.repaint();
			if(default_room)
				r.getRefRoomUI().repaint();
		}
		
	}

	private void IDS_detected(String data) {
		String [] split = data.split(";");
		int id = Integer.parseInt(split[1]);
		int no_of_door = Integer.parseInt(split[2]);
		Room r = refRoomUI.getRoomObjectGlobalRoomUI(split[3]);
		boolean default_room=false;
		if (refRoomUI.getRoomObjectRoomUI(split[3])==null)
			r=refRoomUI.getRoomObjectGlobalRoomUI(split[3]);
		else {
			r=refRoomUI.getRoomObjectRoomUI(split[3]);
			default_room=true;
		}

		if(ref.aPlayer.playerID !=id) {
			r.roomDoorAccessConfig(no_of_door, split[3], true);
			ref.aPlayer.addInstrusionLevel(false);
			if(default_room)
				r.getRefRoomUI().repaint();
		}
	}

	private void timerrequest(int seconds) {
		if(ref.successfully_join_network || userThreshold) 
			return;
		System.err.println("running the reques " +getCounter_reconnect_server());
		runTimer=true;
		timer = new Timer();
		timer.schedule(new  RemindTimer(), seconds*1000);

	}

	private class RemindTimer extends TimerTask{

		@Override
		public void run() {
			if(getCounter_reconnect_server() < 10) {
				setCounter_reconnect_server();
				if(!ref.LOGIN)
					ref.publisher.setNewMsg("0;"+ref.aPlayer.playerID+";"+ref.aPlayer.getPlayerName()+";"+"T");
				else 
					ref.publisher.setNewMsg("2;");
				System.err.println("running the request " +getCounter_reconnect_server());
			}
			else {
				ref.errorMsgDisplay("time out respond from Server please try later !!!   ", ref.aPlayer.playerID, false);
				//runTimer=false;
				timer.cancel();
			}


		}

	}

	private void reshuffle(String data) {
		String [] split = data.split(";");
		int id = Integer.parseInt(split[1]);
		int shuffle = Integer.parseInt(split[2]);
		int index = Integer.parseInt(split[3]);
		String [] itemID;
		if (index ==0)
			itemID=null;
		else
			itemID= new String[index];

		for (int i=0; i<index; i++)
			itemID[i]=split[4+i];

		if(ref.aPlayer.playerID !=id) {
			ref.aPlayer.SHUFFLE=shuffle;
			refRoomUI.reshuffle(true,itemID);
		}
	}

	private void set_task_complete(String data) {

		String [] split = data.split(";");
		int id = Integer.parseInt(split[1]);
		TaksRoom r;
		boolean default_room=false;

		if(ref.aPlayer.playerID !=id) {
			if (refRoomUI.getRoomObjectRoomUI(split[2])==null)
				r=(TaksRoom) refRoomUI.getRoomObjectGlobalRoomUI(split[2]);
			else {
				r=(TaksRoom) refRoomUI.getRoomObjectRoomUI(split[2]);
				default_room=true;
			}
			r.taskCompleted=true;

			if(default_room)
				r.getRefRoomUI().repaint();
		}


	}


	private void net_card_attached_to_roomUI(String data2) {
		String [] split = data.split(";");
		int id = Integer.parseInt(split[1]);
		TaksRoom r ;
		String [] itemID;
		boolean default_room=false;


		if (refRoomUI.getRoomObjectRoomUI(split[3])==null)
			r=(TaksRoom) refRoomUI.getRoomObjectGlobalRoomUI(split[3]);
		else {
			r=(TaksRoom) refRoomUI.getRoomObjectRoomUI(split[3]);
			default_room=true;
		}

		if(ref.aPlayer.playerID !=id) {
			refRoomUI.card_attached_to_roomUI.add(split[2]);
			NetworkCard net= (NetworkCard) refRoomUI.getDeletedItemCardGlobalRoomUI(split[2]);
			if (net!=null) {	
				net.setImage(net.getBackgroundImg());
				r.set_locality_attched_net_card(net,false,this.ref.defaultScreen);
				if(default_room)
					r.getRefRoomUI().repaint();
			}
			else 
				System.err.println ("nulll pointer rrrrrrrrrrrrrrr");
		}

	}

	private void updateServerfield() {

		if(ref.aPlayer.playerID == 0) {
			ref.aPlayer.setNumberCompletedTask();
		}

	}

	private void gameOver(String data) {
		String [] split = data.split(";");
		String win = split[1];

		String s = "Congratualation  You have successfully Completed \n "
				+ "MM Board Games to play again clik exit button ";

		String f = "Sorry You have failed   \n "
				+ "MM Board Games to play again clik exit button ";
		String msg;

		if (win.equals("true"))
			msg = s;
		else 
			msg=f;

		this.ref.errorMsgDisplay(msg, 99, true);


	}

	private void exchangeCard(String data) {
		String [] split = data.split(";");
		int playerId=Integer.parseInt(split[1].trim());
		Room r;
		ItemCard item;

		if(ref.aPlayer.playerID == playerId) {
			ref.rivalAnalyser.label.setText("new card succesfully added"+split[3]);
			if(refRoomUI.getRoomObjectRoomUI(split[4]) ==null)
				r =refRoomUI.getRoomObjectGlobalRoomUI(split[4]);
			else 
				r =refRoomUI.getRoomObjectRoomUI(split[4]);
			item= r.getItemCardInRoom(split[3], r.deletedItemCardInRoom);
			item.setBackgroundImg(item.getBackgroundImg());
			ref.aPlayer.addItemToList(item);

		}

	}

	private void chatMsg(String data) {
		String [] split = data.split(";");

		//		if(!ref.aPlayer.getPlayerName().equals(split[1])) {
		ref.rivalAnalyser.label.setText("your have new message from "+split[1] );
		ref.chatInterface.text.append(split[1]+" : "+split[2] + "\n");

		//	}
	}

	public void authenticationRequest(String data) {
		// 0 : net parameter, 1: temporary id, 2: player name
		String [] split = data.split(";");

		if(ref.server) {

			if(ref.playerDB.size() < ref.numberOfPlayer) {
				/*
				 * assuming the access controll is performed already 
				 */

				System.err.println( "value of split[split.length-1] " +split[split.length-1] + " index " +(split.length-1));
				if(ref.duplicateUserName(split[2])) {
					if(split[3].equals("T")!=true) 
						ref.publisher.setNewMsg("6;"+"false;"+split[1]+";Duplicate player name");
					return;
				}


				PlayerRegitDB last = ref.playerDB.get(ref.playerDB.size()-1);
				int id =last.ID+1;
				ref.playerDB.add(new PlayerRegitDB(id, split[2], "normal"));
				ref.publisher.setNewMsg("6;"+"true;"+id+";"+split[1]+";"+split[2]);

				if(refRoomUI.roomSyncron) {
					refRoomUI.userLateJoinGame.add(Integer.toString(id));
				}

			}

			else {
				ref.publisher.setNewMsg("6;"+"false;"+split[1]+";Fail to joined  network games");

			}
		}

		timerrequest(5);

	}
	public void updateOtherPlayerLocation(String data) {

		String [] split = data.split(";");

		if(!ref.aPlayer.getPlayerName().equals(split[1])) {
			ref.addPlayerToRoom(Integer.parseInt(split[1]),split[2],split[3],split[4]);


		}

	}

	public void authenticationRespond(String data) {

		// 0 : net parameter, 1: auth reply (true or false)  , 2: id allocated  3: temporary id 4: player name 
		String [] split = data.split(";");
		if (split[1].equals("true")) {
			if(ref.aPlayer.playerID == Integer.parseInt(split[3]) 
					&& ref.aPlayer.getPlayerName().equals(split[4])){
				ref.aPlayer.playerID=Integer.parseInt(split[2]);
				ref.publisher.setNewMsg("2;");
				timerrequest(5);
			}
		}
		else {

			ref.errorMsgDisplay(split[3],Integer.parseInt(split[2]),false);
			ref.LOGIN=false;
			userThreshold=true;
		}



	}
	public void requestInitNetGame() {
		if(ref.server) {
			if(refRoomUI.roomSyncron)
				ref.publisher.setNewMsg(ref.publisher.getConfFile()+";SYNSERVER");
			else 
				ref.publisher.setNewMsg(ref.publisher.getConfFile());
			// option create sysn method 

			//			if(refRoomUI.userLateJoinGame.size()>0) {
			//				String syn_file=refRoomUI.generate_syn_file();
			//				for(String user : refRoomUI.userLateJoinGame) {
			//					ref.publisher.setNewMsg("12;"+user+";"+syn_file);
			//				}
			//			}

		}
	}

	public void respondInitNetGame(String data) {
		String [] split = data.split(";");
		int roomsize= Integer.parseInt(split[1]);
		if(!ref.server){
			ref.netINITROOM=true;
			if(!ref.successfully_join_network)
				ref.loadGameRoom("net", data, roomsize);
		}
	}

	public void removeTrap(String data) {
		//	String x = "4;"+player.playerID+";"+trapTobeDeleted.roomID+";"+trapTobeDeleted.operation;
		Brick b;
		String [] split = data.split(";");
		Room r;
		if(refRoomUI.getRoomObjectRoomUI(split[2]) == null) {
			r =refRoomUI.getRoomObjectGlobalRoomUI(split[2]);
			((TrapRoom) r).setTrapDoor(split[3]);
		}
		else {
			r =refRoomUI.getRoomObjectRoomUI(split[2]);
			b=((TrapRoom) r).getBrikFromTrapRoom(split[2],split[3]);
			r.getRefRoomUI().setTrapTobeDeleted(b);
			if(r.getRefRoomUI().player.playerID != Integer.parseInt(split[1]))
				r.getRefRoomUI().removeTrap(split[2],true);
		}

	}
	public void dequeue_player_item(String data)
	{
		String [] split = data.split(";");
		Room r ;
		if (refRoomUI.getRoomObjectRoomUI(split[3])==null)
			r=refRoomUI.getRoomObjectGlobalRoomUI(split[3]);
		else 
			r=refRoomUI.getRoomObjectRoomUI(split[3]);


		if (r.getItemCardInRoom(split[2], r.itemCardInRoom)!=null) {
			int playerId=Integer.parseInt(split[1].trim());
			if(ref.aPlayer.playerID !=playerId)
				ref.aPlayer.team_players_Items.remove(r.getItemCardInRoom(split[2], r.itemCardInRoom));
		}
	}

	public void removeRoomCard(String data) {
		String [] split = data.split(";");
		boolean roomdefault=false;
		Room r ;
		if(ref.server) {
			refRoomUI.roomSyncron=true;
		}
		if (refRoomUI.getRoomObjectRoomUI(split[1])==null)
			r=refRoomUI.getRoomObjectGlobalRoomUI(split[1]);
		else {
			r=refRoomUI.getRoomObjectRoomUI(split[1]);
			roomdefault=true;
		}

		if (r.getItemCardInRoom(split[2], r.itemCardInRoom)!=null) {
			int playerId=Integer.parseInt(split[3].trim());
			if(ref.aPlayer.playerID !=playerId) {
				ref.aPlayer.team_players_Items.add(r.getItemCardInRoom(split[2], r.itemCardInRoom));
				System.out.println("out put the enque item " +ref.aPlayer.team_players_Items.get(0).cardId);
			}
			r.deletedItemCardInRoom.addElement(r.getItemCardInRoom(split[2],r.itemCardInRoom));
			r.itemCardInRoom.remove(r.getItemCardInRoom(split[2],r.itemCardInRoom));


			if(roomdefault)
				r.getRefRoomUI().repaint();

		}


	}

	private void syn_server() {

		if(refRoomUI.userLateJoinGame.size()>0) {
			String syn_file=refRoomUI.generate_syn_file();
			for(String user : refRoomUI.userLateJoinGame) {
				ref.publisher.setNewMsg("12;"+user+";"+syn_file);
			}
			refRoomUI.userLateJoinGame.clear();
		}

	}

	private void syn_server_pplayer(String data2) {
		boolean net=false,trap=false,other_player=false;
		String [] split = data.split(";");
		TrapRoom traproom = null;

		int userid=Integer.parseInt(split[1]);
		if (ref.aPlayer.playerID == userid ) {

			for (int i=2; i < split.length;i++ ) {
				if(split[i].equals("ENDSYN"))
					break;

				if(trap) {
					if(!split[i].equals("trap_stop")) {
						if(split[i].contains("TRR")) {
							traproom=(TrapRoom) refRoomUI.getRoomObjectGlobalRoomUI(split[i]);
						}
						else {
							if(traproom!=null) {
								System.out.println(" trap lablellllllllllllllllll "+split[i]);
								//	String x = "4;"+player.playerID+";"+trapTobeDeleted.roomID+";"+trapTobeDeleted.operation;
								Brick b=traproom.getBrikFromTrapRoom(traproom.roomID,split[i]);
								traproom.getRefRoomUI().setTrapTobeDeleted(b);
								traproom.getRefRoomUI().removeTrap(traproom.roomID,true);
							}
						}
					}
				}
				if (net) {
					if(!split[i].equals("net_stop")) {

						String[] netSplit= split[i].split(":");
						ItemCard item = refRoomUI.getDeletedItemCardGlobalRoomUI(netSplit[1]);
						Room room= refRoomUI.getRoomObjectGlobalRoomUI((netSplit[0]));
						item.setBackgroundImg(item.getBackgroundImg());
						((TaksRoom) room).set_locality_attched_net_card( (NetworkCard) item ,false,ref.defaultScreen);
						item.setRoom(room);
						refRoomUI.netCard.add((NetworkCard) item);
					}



				}
				if(other_player) {
					ItemCard item_op = refRoomUI.getDeletedItemCardGlobalRoomUI(split[i]);
					ref.aPlayer.team_players_Items.add(item_op);
				}

				if(!trap && !net && !other_player){
					if(split[i].equals("trap_start") || split[i].equals("net_start")
							|| split[i].equals("other_player_start")) {}
					else {
						System.err.println("  errorororororo rrrr   " +split[i] );
						ItemCard item = refRoomUI.getItemCardGlobalRoomUI(split[i]);
						Room room = refRoomUI.getRoomObjectGlobalRoomUI(item.aRoom.roomID);

						room.deletedItemCardInRoom.addElement(item);
						room.itemCardInRoom.remove(item);
					}

				}

				if(split[i].equals("trap_start"))
					trap=true;
				if(split[i].equals("trap_stop"))
					trap=false;

				if(split[i].equals("net_start"))
					net=true;
				if(split[i].equals("net_stop"))
					net=false;

				if(split[i].equals("other_player_start"))
					other_player=true;
				if(split[i].equals("other_player_stop"))
					other_player=false;




			}
		}


	}

}
