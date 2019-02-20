/**
 * Project Krypton - Client Side Control Class
 * Programmed by Matthew Zeng
 */

package com.projectkrypton.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Control implements KeyListener {
	//Set the player to the self
    static Player self = Client.self;
    //Set the keydown array
    private static boolean[] keyDown = new boolean[4];//0 left, 1 right, 2 up, 3 attack
    public static int jumpHeight = 70;
    //As the key is being pressed
    public void keyPressed(KeyEvent e) {
    	//Get the key pressed
        int key = e.getKeyCode();
        //If pressed right
        if(key==KeyEvent.VK_RIGHT){
        	//If the player can go right
            if(testValid(1)){
            	//Set keydown1 to true
                keyDown[1]=true;
            }
          //If pressed left
        }else if(key==KeyEvent.VK_LEFT){
        	//If the player can go left
            if(testValid(0)){
            	//Set keydown0 to true
                keyDown[0]=true;
            }
            //If pressed up and the player is on the ground
        }else if(key==KeyEvent.VK_UP&&Client.countAir<2){
        	//If the player can jump
            if(testValid(2)){
            	//Set keydown0 to true
                keyDown[2] = true;
            }
            //If pressed F to attack
        }else if(key==KeyEvent.VK_F){
        	//Set the player to attacking
            self.setAttacking(true);
        }
    }

    //Matthew Zeng
    //This code test whether a movement is valid
    //1 for move right, 0 for move left, 2 for jump
    public boolean testValid(int key){
    	//Remember the original coordinates
        int oriX = self.getX();
        int oriY = self.getY();
        //Move the player up a bit so it's not colliding with the ground
        oriY--;
        
        //Move the player right/left/up
        //Check Right
        if(key==1) oriX++;
            //Check Left
        else if(key==0) oriX--;
            //Check Jump
        else if(key==2)
            oriY-=jumpHeight;
        
        //Check if it's colliding after it's moved
        boolean collide = Client.checkCollide(oriX,oriY);

        //If it's colliding, return false, vise-versa
        return !collide;
    }

    //Key release method
    public void keyReleased(KeyEvent e) {
    	//Set the keydowns to false
    	//This method smoothes the movement
        int key = e.getKeyCode();
        if(key==KeyEvent.VK_RIGHT){
            self.setFacing(1);
            keyDown[1]=false;
        }else if(key==KeyEvent.VK_LEFT){
            self.setFacing(0);
            keyDown[0]=false;
        }else if (key==KeyEvent.VK_UP){
            keyDown[2]=false;
        }else if (key==KeyEvent.VK_F){
            self.setAttacking(false);
        }
    }

    public void keyTyped(KeyEvent arg0) {

    }

    //If pressing right
    public boolean isPressingRight(){
        return keyDown[1];
    }

    //If pressing left
    public boolean isPressingLeft(){
        return keyDown[0];
    }

    //If pressing up
    public boolean isPressingUp() {return keyDown[2];}

}