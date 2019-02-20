package com.projectkrypton.server;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * This class handles the information received from the client
 */

import com.projectkrypton.server.utils.Attacks;
import com.projectkrypton.server.utils.DataSender;
import com.projectkrypton.server.utils.Logger;
import com.projectkrypton.server.utils.Logger.LogType;
import com.projectkrypton.server.Player.GCharacter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

class ClientHandler implements Runnable {

    //Socket for the client
    private Socket client;

    //constructor that takes in the socket for the client
    ClientHandler(Socket client){
        this.client = client;
    }

	@Override
    /**
     * Implementation method for being a Runnable type
     * This method takes in information/packets that are
     * sent by the client to the server and process them
     * into server-understandable information
     */
	public void run() {
        try{
            //Displays the Player information with its IP
            Logger.print(LogType.INFO, "A Player connected from " + client.getInetAddress().getHostName(), true);

            //Initializes a PrintStream object for the current client for sending informations to the client.
            PrintStream out = new PrintStream(client.getOutputStream());

            //Initializes a BufferedReader object for reading packets from the client.
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream())) ;

            //set the running variable as true
            boolean flag = true;

            //Declare a dummy Player object for the current Client.
            Player player = null;

            /*
            This while loop will stop when flag or the server stops
            This is the main content/function of the method.

            Player-to-Server Packet format:
            - keep live packet: KEEPLIVE (the purpose of this packet is to for the client to continue to receive
            information about other players even when the player is not moving and not attacking [idling])
            - initialization packet: init,x,y,playerName,GCharacterEnum
            - location update packet: update,x,y,facingDirection (recall that 0 is left, 1 is right)
            - attack packet: attack,x,y,damage(Double type),facingDirection
             */
            while(flag && KryptonServer.run) {
                try {
                    //Split info by (comma) into a String array
                    String[] info = buf.readLine().split(",");
                    //get the packet type
                    String option = info[0];
                    if (!option.equals("KEEPLIVE")){
                        //get location variable
                        int x = Integer.parseInt(info[1]);
                        int y = Integer.parseInt(info[2]);
                        if (option.equals("init")) {
                            //create new UUID for the player
                            UUID uuid = UUID.randomUUID();
                            //get the name of player
                            String name = info[3];
                            //get the GCharacter enum type of the player
                            GCharacter character = GCharacter.valueOf(info[4]);
                            //Creates a player object for this newly joined client
                            player = new Player(out, uuid, character, name, x, y, 100.0);
                            //Add this player to the DataSender client list
                            DataSender.addPlayer(player);
                        } else if (option.equals("update")) {
                            //update player location
                            player.setX(x);
                            player.setY(y);
                            //update facing (recall that 0 is left, 1 is right)
                            player.setFacing(Integer.parseInt(info[3]));
                            //set attacking state false
                            player.setAttacking(false);
                        } else if (option.equals("attack")) {
                            //set attacking state to true
                            player.setAttacking(true);
                            //update player location
                            player.setX(x);
                            player.setY(y);
                            //read in double damage information
                            double damage = Double.parseDouble(info[3]);
                            //get player facing information (recall that 0 is left, 1 is right)
                            player.setFacing(Integer.parseInt(info[4]));
                            //Update health of surrounding players with specified damage
                            Attacks.from(player.getUUID(), damage);
                        }
                    }
                }catch (SocketException e) {
                    //If a SocketException is thrown that means this player left the game
                    Logger.print(LogType.INFO, "Player " + player.getName() + " left the game.", true);
                    //remove player from DataSender list
                    DataSender.removePlayer(player);
                    //Stop this loop
                    flag = false;
                }
            }
            //closes the input/output stream
            out.close();
            buf.close();
        }catch (Exception e){
            /*
            if the server is still running and error is thrown
            /means something went wrong.
            */
            if (KryptonServer.run) {
                Logger.print(LogType.ERROR, "Unexpected error!", true);
                e.printStackTrace();
            }
        }
    }
}
