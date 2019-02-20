package com.projectkrypton.server.utils;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * Updates health of players around a attacking player
 */

import com.projectkrypton.server.KryptonServer;
import com.projectkrypton.server.Player;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.UUID;

public class Attacks {

    //get the players hashmap from the main class KryptonServer
    private static HashMap<UUID, Player> players = KryptonServer.players;

    /**
     * This method takes in the attackerUUID and damage and apply
     * the damage to any other player is currently in this player's
     * hit box of 22*44
     *
     * This is detected using rectangle/hitbox overlapping detection
     *
     * The purpose of the synchronized keyword is to prevent calling of
     * this method when it isn't done running, since every single client
     * have its own thread.
     * @param attackerUUID
     * @param damage
     */
    public static synchronized void from(UUID attackerUUID, double damage){
        //get attacker object from UUID
        Player attacker = players.get(attackerUUID);
        //get attacker's location
        int attX = attacker.getX();
        int attY = attacker.getY();
        //create hitbox for the attacker
        Rectangle2D.Double attHitBox = new Rectangle2D.Double(attX-2,attY-3,22,44);
        /*
        Loop through every single player to check if the hitbox
        of the attacker collide with every other player
         */
        for(UUID id : players.keySet()){
            //this if statement is to prevent the attacker from getting damaged
            if (!id.equals(attackerUUID)) {
                //get the victim object
                Player victim = players.get(id);
                //get victim location
                double vicX = victim.getX();
                double vicY = victim.getY();
                //if the attacker's hitbox intersects the victim's hitbox
                //then damage the victim with argument double damage variable
                if (attHitBox.intersects(vicX, vicY, 22, 44)) {
                    victim.damage(attackerUUID, damage);
                }
            }
        }
    }

}
