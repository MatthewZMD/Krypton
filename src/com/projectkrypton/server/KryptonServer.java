package com.projectkrypton.server;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * The main class for the server
 */

import com.projectkrypton.server.utils.DataSender;
import com.projectkrypton.server.utils.Logger;
import com.projectkrypton.server.utils.Logger.LogType;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class KryptonServer extends Thread{

    // A boolean variable that determine either the server should be stopped or not
    public static boolean run;

    /*
    This hashmap contains all the connected players
    with their UUID as key and the player object as the value
    */
    public static HashMap<UUID, Player> players = new HashMap<UUID, Player>();

    //This is the server socket instance that accepts client connections
    static ServerSocket server;

    //This is the map of the game read from Map.txt stored in characters.
    static char[][] map;

    //This is the formatted map in a single string to be send to the clients
    public static String mapData = "";
    
	public static void main(String[] args) throws Exception {

        //Asking for the port of the server
        Logger.print(LogType.INFO, "Please input a port (50000 ~ 60000): ", false);

        //Input reader instance
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //port variable initialization
        int port = -1;

        /*
        This while loop reads the inputted port.
        It will continue running if the inputted
        information is either not a number or not
        int the correct range.
         */
        while (port==-1){
            try{
                //read in the input port
                port = Integer.parseInt(br.readLine());
                //check if inputted port is in valid range
                if (50000>port||port>60000){
                    Logger.print(LogType.WARN, "Integer with ranged from 50000 ~ 60000 only!", true);
                    Logger.print(LogType.INFO, "Please input a port (50000 ~ 60000): ", false);
                    //set port to -1 so that it will take in port input again
                    port=-1;
                }
                else Logger.print(LogType.INFO, "Port is valid, starting server...", true);
            }catch (Exception e){
                //this executes when the inputted value isn't a valid integer
                Logger.print(LogType.WARN, "Integer with ranged from 50000 ~ 60000 only!", true);
                Logger.print(LogType.INFO, "Please input a port (50000 ~ 60000): ", false);
            }
        }

        // caching map data from Map.txt to the map character 2D array
		map = mapArray();

        // processes the map 2D array to a formatted string for the clients to receive
		mapToString();

        //initialize client socket variable
		Socket client;
        try {
            //starts the server with the specified port read in above
			server = new ServerSocket(port);
	        run = true;
	        Logger.print(LogType.INFO, "Server Running... Accepting clients.", true);
            //Runs DataSender class in another thread
            new Thread(new DataSender()).start();
            //Runs server console in another thread
            new Thread(new Console()).start();

            //This while loop continue accepts new clients
	        while (run) {
                //get the client
	            client = server.accept();
                //start a ClientHandler on another thread
	            new Thread(new ClientHandler(client)).start();
	        }	
		} catch (Exception e) {
            //If the server supposed to be running then gives error
            if (run) {
                Logger.print(LogType.ERROR, e.getMessage(), true);
                e.printStackTrace();
            }
		}

	}
	
	public static char[][] mapArray() throws Exception{
        //Declaring a Scanner for reader the Map.txt file
		Scanner mapFile = new Scanner(new File("Map.txt"));
        //This stores map data line by line in to a list
		List<String> tmpMap = new ArrayList<String>();
		while(mapFile.hasNext()){
			tmpMap.add(mapFile.nextLine());
		}
        //get the row and col value of the map
		int rows = tmpMap.size();
		int cols = tmpMap.get(0).length();
        //initializing the map 2D array with given rows and cols integer lengths
		char[][] map = new char[rows][cols];
        //Separate String into chars from the tmpMap to 2d map array
		for(int i = 0;i<rows;i++){
			map[i] = tmpMap.get(i).toCharArray();
		}
        //close the scanner
		mapFile.close();
        //return the processed map variable
		return map;
	}

    /**
     * This method formats the 2D char array into
     * a single formatted transferable string for
     * the clients to receive.
     */
	public static void mapToString(){
		String s = "";
		for(char[] cs : map){
			for(char c: cs){s+=c;}
			s+="~";
		}
		mapData = s;
	}
}
