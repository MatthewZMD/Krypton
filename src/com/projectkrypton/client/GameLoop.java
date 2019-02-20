/**
 * Project Krypton - Client Side Game Loop
 * Programmed by Matthew Zeng
 */
package com.projectkrypton.client;

public class GameLoop implements Runnable{

    @Override
    public void run() {
        Client.menuMusic.stop();
        Client.battleMusic.start();
        Client.battleMusic.loop(10000);
    	//Main game loop
        while (true){
            try {
            	//Communicate with server
                Client.cm.communicate();
                //Call control move so players can move
                Client.controlMove();
                //Repaint window
                Client.gameMap.repaint();
                try {
                	//Sleep for 5 millisec
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }catch (Exception e){
                //e.printStackTrace();
            	//Display no connection
                Client.lostConnection();
                break;
            }
        }
    }
}
