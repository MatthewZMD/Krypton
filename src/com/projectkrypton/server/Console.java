package com.projectkrypton.server;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * This class responsible for reading commands for
 * the server from the console.
 */

import com.projectkrypton.server.utils.Logger;
import com.projectkrypton.server.utils.Logger.LogType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;

class Console implements Runnable{

    //gets the players hashmap from the main class KryptonServer
    private static HashMap<UUID, Player> players = KryptonServer.players;

    @Override
    /**
     * Implementation method for being a Runnable type
     * This method is responsible for taking input for the console
     * and process them accordingly.
     */
    public void run() {
        //Input reader initialization
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //run while the server is still up
        while (KryptonServer.run){
            try {
                //reads in input
                String input = br.readLine();
                //stops the server and exits the program
                if (input.equalsIgnoreCase("stop")){
                    Logger.print(Logger.LogType.INFO, "Server stopping...", true);
                    KryptonServer.run = false;
                    Thread.sleep(5*2); // wait for 2 ticks
                    KryptonServer.server.close();
                    Logger.print(Logger.LogType.INFO, "Goodbye.", true);
                    System.exit(0);
                }else if (input.equalsIgnoreCase("list")){ // Lists all the online players
                	if(players.keySet().size()==0){
                		Logger.print(LogType.WARN, "No one is online!!", true);
                	}
                    for (UUID id : players.keySet()){
                        Player p = players.get(id);
                        Logger.print(Logger.LogType.INFO, p.getName()+":", true);
                        Logger.print(Logger.LogType.INFO, "  - UUID: " + id.toString(), true);
                        Logger.print(Logger.LogType.INFO, "  - location: " + p.getX() + "," + p.getY(), true);
                        Logger.print(Logger.LogType.INFO, "  - health: " + p.getHealth(), true);
                    }
                }else if (input.equalsIgnoreCase("help")){ // lists the help page
                	Logger.print(LogType.INFO, "----------------------------[HELP]----------------------------", true);
                	Logger.print(LogType.INFO, "stop - This command will shut the server down", true);
                	Logger.print(LogType.INFO, "list - This will list online players with their information", true);
                	Logger.print(LogType.INFO, "credit - prints out the credits of this program", true);
                	Logger.print(LogType.INFO, "--------------------------------------------------------------", true);
                }else if (input.equalsIgnoreCase("credit")){ // prints the about page
                    Logger.print(LogType.INFO, "----------------------------[ABOUT]---------------------------", true);
                    Logger.print(LogType.INFO, "Krypton Server: This program is created by Mark and Matthew", true);
                    Logger.print(LogType.INFO, "--------------------------------------------------------------", true);
                }else{//if the program does not recognize the input then print out the message below
                	Logger.print(LogType.WARN, "Invalid command! Type help for a list of valid commands.", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
