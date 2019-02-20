package com.projectkrypton.client;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * The purpose of this class is to send and receive
 * information from and to the server. Basically the
 * communication device for this program
 */

import com.projectkrypton.client.Player.GCharacter;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

public class ConnectionManager{

    //some required variables/objects that are needed for this class
	private BufferedReader buf;
	private PrintStream out;
	private Socket socket;
    //run determines either the communication should continue
    //first allows the program to know either this packet is the first packet or not
	private boolean run = true, first = true;
    //This queue stores the information that is to be send to the server
	private Queue<String> dataQ = new LinkedList<String>();
    //the name of the player
	private String name;
    //players hashmap from main class Client
	private HashMap<UUID, Player> players = Client.players;
    //get the self player instance
	private Player self = Client.self;
    //
	private static int maxPlayers = 0;

    /**
     * This is the construtor method the the ConnectionManager class
     * that reads in the IP, port and the name of the player.
     * @param IP
     * @param port
     * @param name
     * @throws IOException
     */
	public ConnectionManager(String IP, String port, String name) throws IOException {
        this.name = name;
        self.setName(name);
        socket = new Socket(IP, Integer.parseInt(port));//TODO throws numberformatexception
        buf = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintStream(socket.getOutputStream());
    }

    /**
     * This method concludes the function of this class
     * It is responsible for both sending and receiving
     * information from and to the server.
     *
     * Sent packet format:
     * - keep live packet: KEEPLIVE (the purpose of this packet is to for the client to continue to receive
     * information about other players even when the player is not moving and not attacking [idling])
     * - initialization packet: init,x,y,playerName,GCharacterEnum
     * - location update packet: update,x,y,facingDirection (recall that 0 is left, 1 is right)
     * - attack packet: attack,x,y,damage(Double type),facingDirection
     *
     * Received packet format:
     * ATTACKING/NORMAL|UUID|playerName|GCharacterEnum|x|y|facingVar|health
     * # sign is used to differentiate multiple players, example:
     * ATTACKING|b039d756-3377-11e6-ac61-9e71128cae77|Mark|PL_Mark|650|100|0|100.0#NORMAL|cf2fccce-3377-11e6-ac61-9e71128cae77|Matthew|PL_Matthew|750|150|1|100.0
     *
     * @throws IOException
     * @throws InterruptedException
     */
	public void communicate() throws IOException, InterruptedException {
        //runs if communication is still allowed
        if (run) {
            //if it is the first time, send initialization packet
            if (first) {
                out.println("init," + self.getX() + "," + self.getY() + "," + name + "," + self.getCharacter().toString());
            } else if (!dataQ.isEmpty()) { // if the to-be-send information queue is not empty, send it
                out.println(dataQ.poll());
            } else { // if the dataQ is empty then send KEEPLIVE packet to sustain the connection
                out.println("KEEPLIVE");
            }
            //get the information from the server
            String[] info = buf.readLine().split("[#]");
            /*
             Starting index for player information. This is necessary because
             the first packet that the server returns contains map,uuidAssignedForThisPlayer,playersinformation
             So it is not valid to start reading player information at 0 when the first two
             isn't other players information but map and the UUID assigned for this player
             */
            int startInd = 0;
            //if this is the first packet received
            if (first) {
                //set the startInd to 2
                startInd = 2;
                //read in the map rows
                String[] rows = info[0].split("~");
                //initialize map 2d char array
                char[][] map = new char[rows.length][rows[0].length()];
                //cache in the information for the map
                for (int i = 0; i < map.length; i++) {
                    char[] c = rows[i].toCharArray();
                    map[i] = Arrays.copyOf(c, c.length);
                }
                //update the map of the client to this.
                Client.map = map;
                //set the UUID of this player to the server assigned UUID
                self.setUUID(UUID.fromString(info[1]));
            }
            first = false;
            /*
            declaration of other players UUID to later to be compared
            with the players hashmap to determine either any player left
            */
            ArrayList<UUID> updatedPlayers = new ArrayList<UUID>();
            for (int i = startInd; i < info.length; i++) {
                //get the information string of the targeted player
                String s = info[i];
                //parse the string into client-program-understandable info
                String[] playerInfo = s.split("[|]");
                boolean attacking = playerInfo[0].trim().equals("ATTACKING");
                UUID pId = UUID.fromString(playerInfo[1].trim());
                String name = playerInfo[2].trim();
                GCharacter character = GCharacter.valueOf(playerInfo[3].trim());
                int x = Integer.parseInt(playerInfo[4].trim());
                int y = Integer.parseInt(playerInfo[5].trim());
                int facing = Integer.parseInt(playerInfo[6].trim()); // recall that 0 is left 1 is right
                double health = Double.parseDouble(playerInfo[7].trim());
                //if the information received is for the client player then only update damage and health
                if (pId.equals(self.getUUID())) {
                    self.setHealth(health);
                } else if (players.containsKey(pId)) {//if this info is for other player
                    //update information
                    updatedPlayers.add(pId);
                    Player p = players.get(pId);
                    p.setAttacking(attacking);
                    p.setName(name);
                    p.setX(x);
                    p.setY(y);
                    p.setHealth(health);
                    p.setFacing(facing);
                } else {
                    //if a new player joined the game
                    updatedPlayers.add(pId);
                    Utils.addPlayer(new Player(pId, character, name, x, y, health));
                }

            }
            /*
            this segment of code determines either any player have left the game
            by comparing the updatePlayer UUID list with the local players hashmap
             */
            if (updatedPlayers.size()!=players.keySet().size()){
                Iterator<UUID> ids = players.keySet().iterator();
                while (ids.hasNext()){
                    UUID id = ids.next();
                    if (!updatedPlayers.contains(id)){
                        ids.remove();
                        Utils.removePlayer(id);
                    }
                }
            }

            //Display lost window and exit the game
            if(!Client.self.isALive()){
                Client.loseWindow.setLocationRelativeTo(Client.gameWin);
            	//Client.gameWin.setVisible(false);
            	Client.loseWindow.setVisible(true);
            	Client.gameWin.setVisible(false);
            	Thread.sleep(5000);
                Client.battleMusic.stop();
                Client.gameWin.dispatchEvent(new WindowEvent(Client.gameWin, WindowEvent.WINDOW_CLOSING));
            }

            //this is to determine either the player have won or not and display the won window
            maxPlayers = Math.max(maxPlayers, updatedPlayers.size());
            if(maxPlayers!=0&&updatedPlayers.size()==0){
            	Utils.removePlayer(Client.self.getUUID());
            	end();
            	Client.winWindow.setLocationRelativeTo(Client.gameWin);
            	Client.winWindow.setVisible(true);
            	Client.gameWin.setVisible(false);
            	Thread.sleep(5000);
                Client.battleMusic.stop();
            	Client.gameWin.dispatchEvent(new WindowEvent(Client.gameWin, WindowEvent.WINDOW_CLOSING));
            }
        } else {
            //close the input/output streams and socket
            buf.close();
            out.close();
            socket.close();
        }
    }

    //close the communication gateway
	public void end() {
		run = false;
	}

    /**
     * This method is responsible for updating the player
     * information to the dataQ. The purpose of the preL
     * system time is to prevent multiple information to
     * be send during one tick. The consequences of not
     * having the variable will lead to not synchronized
     * movement/information between the server and the client
     */
    long preL = System.currentTimeMillis();
	public void update(int x, int y, double damage){
        long curL = System.currentTimeMillis();
        //if the current time and last sent time is bigger than a tick then execute
        if (curL-preL>=5) {
            //if the damage is bigger than -1 that means it is a attack
            if (damage>-1){
                sendData("attack,"+x+","+y+","+damage+","+self.getFacing());
            }//other wise it is only location update packet
            else sendData("update," + x + "," + y + "," + self.getFacing());
            //set current time to be the previous time
			preL=curL;
        }

    }

    //update location helper method
    public void updateLoc(int x, int y){
        update(x,y,-1);
    }

    //attack helper method
	public void attack(int x, int y, double damage){
		update(x,y,damage);
	}

    //dataQ query method
	private void sendData(String data) {
		dataQ.add(data);
	}
}
