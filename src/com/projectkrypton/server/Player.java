package com.projectkrypton.server;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * This is the player object class [Nearly identical to the Client's player object]
 *
 * This class contains essential information for the Player
 */


import java.io.PrintStream;
import java.util.UUID;

public class Player {

    //Some essential variables for the Player object
    private String name;
    private int x, y, facing; // For facing 0 is left 1 is right
    private double health;
    private UUID attacker; // the attacker that is currently damaging the player
    private boolean isAttacking;
    private UUID uuid;
    private GCharacter character;
    private PrintStream printStream;

    //Enum characters for the game
    public enum GCharacter{PL_MT, PL_Mark, PL_Tim, PL_Alex, PL_Nikolai, PL_Simon}

    //The constructor of the Player object that read in information
    public Player(PrintStream printStream, UUID uuid, GCharacter character, String name, int x, int y, double health){
        this.printStream = printStream;
        this.character = character;
        this.uuid = uuid;
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = health;
        //default facing direction is left
        facing = 0;
    }

    public PrintStream getPrintStream(){
        return printStream;
    }

    public GCharacter getCharacter(){
        return character;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setFacing(int f){
        facing = f;
    }

    public int getFacing(){
        return facing;
    }

    /**
     * Returns true when the health is bigger than 0
     * Returns false when the health is lower than 0
     * @return A boolean value
     */
    public boolean isALive(){
        if (health>0) return true;
        return false;
    }

    /**
     * Takes in the attacker uuid and the damage
     * that that attacker have upon this player.
     *
     * Decrease current health by the argument amt
     * attack damage
     *
     * Sets isDamaging true
     * @param from
     * @param amt
     */
    public void damage(UUID from, double amt) {
        attacker = from;
        health-=amt;
    }

    public UUID getAttacker(){
        return attacker;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public double getHealth() {
        return health;
    }

    /**
     * This method returns the player object with argument UUID
     * @param id
     * @return A Player object with argument UUID
     */
    public static Player getPlayer(UUID id){
        return KryptonServer.players.get(id);
    }

}