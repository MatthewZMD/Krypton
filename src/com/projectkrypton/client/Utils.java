package com.projectkrypton.client;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * This is a Utility class that contains several useful methods
 */

import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Mark on 6/14/2016.
 */
public class Utils {

    //getting essential hashmaps from the main class
    static HashMap<UUID, Player> players = Client.players;
    static HashMap<UUID, Integer> fightCntOther = Client.fightCntOther;
    static HashMap<UUID, Integer> walkCntOther = Client.walkCntOther;
    static HashMap<UUID, Integer> preXOther = Client.preXOther;

    //Add a new player to the local information hashmaps
    static void addPlayer(Player p){
        UUID id = p.getUuid();
        players.put(id, p);
        fightCntOther.put(id, 0);
        walkCntOther.put(id, 0);
        preXOther.put(id, 0);
    }

    //remove a player from the local information hashmaps
    static void removePlayer(UUID id) {
        players.remove(id);
        fightCntOther.remove(id);
        walkCntOther.remove(id);
        preXOther.remove(id);
    }

    //update a centered nametag above a player's head.
    static void centerStr(Graphics g, String s, int width, int XPos, int YPos){
        int stringLen = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int start = width/2 - stringLen/2;
        g.drawString(s, start + XPos, YPos);
    }

    //player attack sound affect in sync
    /**
     * The purpose of the hashmap is to store the last time
     * the sound for the argument player have played, to insure
     * the sound won't be started again when it isn't finished yet.
     */
    static HashMap<String, Long> preTs = new HashMap<String, Long>();
    static synchronized void playMusic(String name, Clip c){
        long curT = System.currentTimeMillis();
        if(!preTs.containsKey(name)||curT-preTs.get(name)>300) {
            c.start();
            c.setMicrosecondPosition(0);
            preTs.put(name, curT);
        }
    }

}