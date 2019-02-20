package com.projectkrypton.client;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * This is the player object class [Nearly identical to the server's player object]
 *
 * This class contains essential information for the Player
 */

import java.util.UUID;

public class Player {

    //Some essential variables for the Player object
    private String name;
    private int x, y, facing; // For facing 0 is left 1 is right
    private double health;
    private boolean isAttacking;
    private UUID uuid;
    private GCharacter character;

    //Enum characters for the game
    public enum GCharacter{PL_MT, PL_Mark, PL_Tim, PL_Alex, PL_Nikolai, PL_Simon}

    //the default constructor and also initializes the location of the player
    public Player(){
        setX(650); setY(0);
    }

    //The constructor of the Player object that read in information
    public Player(UUID uuid, GCharacter character, String name, int x, int y, double health){
        this.uuid = uuid;
        this.character = character;
        this.name = name;
        this.x = x;
        this.y = y;
        this.health = health;
        facing = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setGCharacter(GCharacter character){
        this.character = character;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setCharacter(GCharacter character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public GCharacter getCharacter(){
        return character;
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

    public boolean isALive(){
        if (health>0) return true;
        return false;
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
}
