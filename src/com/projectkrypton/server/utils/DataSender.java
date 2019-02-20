package com.projectkrypton.server.utils;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * This class is responsible for sending/broadcasting info to all clients
 */

import com.projectkrypton.server.KryptonServer;
import com.projectkrypton.server.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class DataSender implements Runnable{

    //get players from the main class KryptonServer
    private static HashMap<UUID, Player> players = KryptonServer.players;
    //initialization of UUID queue
    private static Queue<UUID> UUID = new LinkedList<UUID>();
    //initialization of new player queue
    private static Queue<Player> addPlayers = new LinkedList<Player>();
    //initialization of left player queue
    private static Queue<Player> removePlayers = new LinkedList<Player>();

    @Override
    /*
     * Implementation method for being a Runnable type
     * Runs at 200 iteration per second while run is true
     */
    public void run() {
        while (KryptonServer.run){
            try {
                sync();
                Thread.sleep(5); //200 tps (tick per second)
                //remove or add player to prevent ConcurrentModificationException
                updatePlayerList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method sends/syncs information to every single online
     * players. Information include the location, status, name, id
     * and health of every single player. Also map information at
     * first packet
     */
    private static void sync(){
        //Loop through every single player connected
        for(UUID id : players.keySet()){
            //get the player object
            Player player = players.get(id);
            String output = "";

            /*
            when the newly connected player UUID queue isn't empty
            and the current player's UUID matches to the first UUID
            in the queue then send the map data as well as the UUID
            assigned for the player to the client.
            */
            if(!UUID.isEmpty()&&player.getUUID().equals(UUID.peek())){
            	output+=KryptonServer.mapData;
            	output+="#";
            	output+=UUID.poll();
            	output+="#";
            }

            /*
             * Loop through every single player and update them with
             * other player's information
             *
             * Data format:
             * ATTACKING/NORMAL|UUID|playerName|GCharacterEnum|x|y|facingVar|health
             *
             * # sign is used to differentiate multiple players, example:
             * ATTACKING|b039d756-3377-11e6-ac61-9e71128cae77|Mark|PL_Mark|650|100|0|100.0#NORMAL|cf2fccce-3377-11e6-ac61-9e71128cae77|Matthew|PL_Matthew|750|150|1|100.0
             */
            for (UUID target : players.keySet()) {
                //get target player object
                Player targetPlayer = players.get(target);
                String data = "";
                //add information to data accordingly
                if (targetPlayer.isAttacking()) data += "ATTACKING|";
                else data += "NORMAL|";
                data += targetPlayer.getUUID().toString() + "|";
                data += targetPlayer.getName() + "|";
                data += targetPlayer.getCharacter().toString() + "|";
                data += targetPlayer.getX() + "|";
                data += targetPlayer.getY() + "|";
                data += targetPlayer.getFacing() + "|";
                data += targetPlayer.getHealth();
                //add data into the final output string
                output += data;
                output += "#";
            }
            //sends the information to player
            send(player, output);
        }
    }


    /**
     * The purpose of this is to prevent ConcurrentModificationException
     * Removes and Adds player to the players hashmap after making sure
     * nothing is modifying the hashmap
     */
    private static void updatePlayerList(){
        while (!addPlayers.isEmpty()){
            Player p = addPlayers.poll();
            players.put(p.getUUID(), p);
        }
        while (!removePlayers.isEmpty()){
            players.remove(removePlayers.poll().getUUID());
        }
    }


    /**
     * The purpose of this is to prevent ConcurrentModificationException
     * add player to addPlayers queue to be added also send the uuid
     * @param p
     */
    public static void addPlayer(Player p){
        addPlayers.add(p);
        sendUUID(p.getUUID());
    }

    /**
     * The purpose of this is to prevent ConcurrentModificationException
     * add player to removePlayers queue waiting to be removed
     * @param p
     */
    public static void removePlayer(Player p){
        removePlayers.add(p);
    }

    private static void sendUUID(UUID id){
        UUID.add(id);
    }

    /**
     * Send player string information
     * @param p
     * @param str
     */
    private static void send(Player p, String str){
        p.getPrintStream().println(str);
    }
}
