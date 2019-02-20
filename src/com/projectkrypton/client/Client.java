/**
 * Project Krypton - Client Side Main Program
 * Programmed by Matthew Zeng
 */
package com.projectkrypton.client;

import com.projectkrypton.client.Player.GCharacter;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Client {

	//Declare HashMaps, variables
    public static HashMap<UUID, Player> players = new HashMap<UUID, Player>();
    public static Player self = new Player(); 
    static Control control = new Control();
    public static ConnectionManager cm;
    static JFrame gameWin = new JFrame("Krypton!");
    static GameMap gameMap = new GameMap();
    static int mapRow;
    static int mapColumn;

    //Width of the player sprite
    public static int playerWidth = 88/4;
    //Height of the player sprite
    public static int playerHeight = 88/2;
    //Bottom right coordinates of the player
    public static int playerX2 = self.getX()+playerWidth;
    public static int playerY2 = self.getY()+playerHeight;

    //Set the ratio to enlarge to proper window size
    public static int ratioX = 8;
    public static int ratioY = 12;
    //Declare x,y coordinates of the map
    public static int mapX;
    public static int mapY;
    //Declare map array
    public static char[][]map;

    /*******Menu GUI********/
    static JTextField username,ip,port;
    static JFrame menu;
    static ImageIcon returnToMenu;
    static JFrame rules;
    static JFrame credits;
    static JFrame enterInfo;
    static JFrame noConnection;
    static JFrame winWindow;
    static JFrame loseWindow;
    //Buttons
    static JButton playButton;
    static JButton rulesButton;
    static JButton creditsButton;
    static JButton backButton;
    static JButton startButton;
    
    //Declare Tileset images
    static Image wall;
    static Image rock;
    static Image parachute;
    static Image heart;
    static Image grass;
    static Image ice;
    static Image lava;
    static Image dirt;
    static Image backgroundImage;

    //music/soundeffect variables
    static Clip menuMusic;
    static Clip battleMusic;
    static Clip soundMT, soundMark, soundTim, soundAlex, soundNikolai, soundSimon;

    //Matthew Zeng
    //Main code that loads the GUI, music and sound effects
    public static void main(String[] args) throws Exception {


        //Music & Sound effects
        menuMusic = AudioSystem.getClip();
        battleMusic = AudioSystem.getClip();
        soundMT = AudioSystem.getClip();
        soundMark = AudioSystem.getClip();
        soundTim = AudioSystem.getClip();
        soundAlex = AudioSystem.getClip();
        soundNikolai = AudioSystem.getClip();
        soundSimon = AudioSystem.getClip();
        menuMusic.open(AudioSystem.getAudioInputStream(Client.class.getResource("/Music/Title.wav")));
        battleMusic.open(AudioSystem.getAudioInputStream(Client.class.getResource("/Music/Battle.wav")));
        soundMT.open(AudioSystem.getAudioInputStream(Client.class.getResource("/SoundEffects/MTEffect.wav")));
        soundMark.open(AudioSystem.getAudioInputStream(Client.class.getResource("/SoundEffects/MarkEffect.wav")));
        soundTim.open(AudioSystem.getAudioInputStream(Client.class.getResource("/SoundEffects/TimEffect.wav")));
        soundAlex.open(AudioSystem.getAudioInputStream(Client.class.getResource("/SoundEffects/AlexEffect.wav")));
        soundNikolai.open(AudioSystem.getAudioInputStream(Client.class.getResource("/SoundEffects/NikolaiEffect.wav")));
        soundSimon.open(AudioSystem.getAudioInputStream(Client.class.getResource("/SoundEffects/SimonEffect.wav")));

        menuMusic.start();
        menuMusic.loop(10000);
        /************MAIN MENU**************/
        //Create window
        menu = new JFrame("Krypton!");
        menu.setSize(1000, 600);
        menu.setResizable(false);
        menu.setLayout(new GridLayout(2, 1));
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(Color.WHITE);

        // Create title icon
        ImageIcon titleIcon = new ImageIcon(Client.class.getResource("/Menu/Title.png"));

        // Create buttons in the menu
        JLabel titleLabel = new JLabel(titleIcon, JLabel.CENTER);
        playButton = new JButton(new ImageIcon(Client.class.getResource("/Menu/PlayIcon.png")));
        playButton.setName("play");
        playButton.addActionListener(new menuButtonListener());
        playButton.setBackground(Color.WHITE);
        rulesButton = new JButton(new ImageIcon(Client.class.getResource("/Menu/RulesIcon.png")));
        rulesButton.setName("rules");
        rulesButton.addActionListener(new menuButtonListener());
        rulesButton.setBackground(Color.WHITE);
        creditsButton = new JButton(new ImageIcon(Client.class.getResource("/Menu/creditsIcon.png")));
        creditsButton.setName("credits");
        creditsButton.addActionListener(new menuButtonListener());
        creditsButton.setBackground(Color.WHITE);

        // Assemble window
        titlePanel.add(titleLabel);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(playButton);
        rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(rulesButton);
        creditsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(creditsButton);
        menu.add(titlePanel);
        menu.add(controlPanel);

        // Set menu visibility
        menu.setVisible(true);

        /**********Rules Window*********/
        // Create window
        rules = new JFrame("Krypton!");
        rules.setSize(1000, 600);
        rules.setResizable(false);
        rules.setLayout(new BorderLayout());
        rules.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels
        JPanel toolbar1 = new JPanel();
        toolbar1.setBackground(Color.WHITE);
        JPanel rulePanel = new JPanel();
        rulePanel.setBackground(Color.WHITE);

        // Create image icons for the rules page
        ImageIcon ruleIcon = new ImageIcon(Client.class.getResource("/Menu/rules.png"));
        returnToMenu = new ImageIcon(Client.class.getResource("/Menu/ReturnIcon.png"));

        // Create back button
        backButton = new JButton(returnToMenu);
        backButton.setName("returnMenu");
        backButton.setBackground(Color.WHITE);
        backButton.addActionListener(new menuButtonListener());
        JLabel ruleLabel = new JLabel(ruleIcon, JLabel.CENTER);

        // Assemble window
        toolbar1.add(backButton);
        rulePanel.add(ruleLabel);
        rules.add(rulePanel, BorderLayout.CENTER);
        rules.add(toolbar1, BorderLayout.SOUTH);

        // Set window visibility
        rules.setVisible(false);

        /********Credits Window*********/
        // Create window
        credits = new JFrame("Krypton!");
        credits.setSize(1000, 600);
        credits.setResizable(false);
        credits.setLayout(new BorderLayout());
        credits.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels
        JPanel toolbar2 = new JPanel();
        toolbar2.setBackground(Color.WHITE);
        JPanel creditsPanel = new JPanel();
        creditsPanel.setBackground(Color.WHITE);

        // Create icon
        ImageIcon creditIcon = new ImageIcon(Client.class.getResource("/Menu/credits.png"));

        // Create components
        backButton = new JButton(returnToMenu);
        backButton.setName("returnMenu");
        backButton.addActionListener(new menuButtonListener());
        backButton.setBackground(Color.WHITE);
        JLabel creditsLabel = new JLabel(creditIcon, JLabel.CENTER);

        // Assemble window
        toolbar2.add(backButton);
        creditsPanel.add(creditsLabel);
        credits.add(creditsPanel, BorderLayout.CENTER);
        credits.add(toolbar2, BorderLayout.SOUTH);

        // Set window visibility
        credits.setVisible(false);

        /**********Enter Info Window******/
        //Create window for the info menu
        enterInfo = new JFrame("Enter Information");
        enterInfo.setSize(1000/3, 600/2);
        enterInfo.setResizable(false);
        enterInfo.setLayout(new GridLayout((int) (4+Math.ceil(charNum/2)), 2));
        enterInfo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Ask for username, ip, port
        JLabel promptName = new JLabel("Enter Username:	",JLabel.CENTER);
        username = new JTextField(10);
        JLabel promptIP = new JLabel("Enter IP Address:	",JLabel.CENTER);
        ip = new JTextField(10);
        JLabel promptPort = new JLabel("Enter Port Number:	",JLabel.CENTER);
        port = new JTextField(10);
        //Create start button
        startButton = new JButton("START!");
        startButton.setName("start");
        startButton.addActionListener(new menuButtonListener());
        //Assemble components together
        enterInfo.add(promptName);
        enterInfo.add(username);
        enterInfo.add(promptIP);
        enterInfo.add(ip);
        enterInfo.add(promptPort);
        enterInfo.add(port);

        //AddCharacter icons for user to choose
        MTIcon = new JButton(new ImageIcon("resources/Characters/MTIcon.png"));
//        MTIcon = new JButton(Client.class.getResource("/Characters/MTIcon.png"));
        MTIcon.setName("0");
        MTIcon.addActionListener(new choosePlayerListener());
        enterInfo.add(MTIcon);
        MarkIcon = new JButton(new ImageIcon("resources/Characters/MarkIcon.png"));
//      MarkIcon = new JButton(Client.class.getResource("/Characters/MarkIcon.png"));
        MarkIcon.setName("1");
        MarkIcon.addActionListener(new choosePlayerListener());
        enterInfo.add(MarkIcon);
        TimIcon = new JButton(new ImageIcon("resources/Characters/TimIcon.png"));
//      TimIcon = new JButton(Client.class.getResource("/Characters/TimIcon.png"));
        TimIcon.setName("2");
        TimIcon.addActionListener(new choosePlayerListener());
        enterInfo.add(TimIcon);
        AlexIcon = new JButton(new ImageIcon("resources/Characters/AlexIcon.png"));
//      AlexIcon = new JButton(Client.class.getResource("/Characters/AlexIcon.png"));
        AlexIcon.setName("3");
        AlexIcon.addActionListener(new choosePlayerListener());
        enterInfo.add(AlexIcon);
        NikolaiIcon = new JButton(new ImageIcon("resources/Characters/NikolaiIcon.png"));
//      NikolaiIcon = new JButton(Client.class.getResource("/Characters/NikolaiIcon.png"));
        NikolaiIcon.setName("4");
        NikolaiIcon.addActionListener(new choosePlayerListener());
        enterInfo.add(NikolaiIcon);
        SimonIcon = new JButton(new ImageIcon("resources/Characters/SimonIcon.png"));
//      SimonIcon = new JButton(Client.class.getResource("/Characters/SimonIcon.png"));
        SimonIcon.setName("5");
        SimonIcon.addActionListener(new choosePlayerListener());
        enterInfo.add(SimonIcon);

        enterInfo.add(startButton);
        // Set window visibility
        enterInfo.setVisible(false);

        /*****Alert No Connection Window*****/
        noConnection = new JFrame("No Connection...");
        noConnection.setSize(1000/2, 600/2);
        noConnection.setResizable(false);
        noConnection.setLayout(new BorderLayout());
        noConnection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels
        JPanel toolbar3 = new JPanel();
        toolbar3.setBackground(Color.WHITE);
        JPanel noConnectPanel = new JPanel();
        noConnectPanel.setBackground(Color.WHITE);

        // Create icon
        ImageIcon noConnectIcon = new ImageIcon(Client.class.getResource("/Menu/noConnection.png"));

        // Create components
        backButton = new JButton(returnToMenu);
        backButton.setName("returnMenu");
        backButton.addActionListener(new menuButtonListener());
        JLabel noConnectLabel = new JLabel(noConnectIcon, JLabel.CENTER);

        // Assemble window
        toolbar3.add(backButton);
        noConnectPanel.add(noConnectLabel);
        noConnection.add(noConnectPanel, BorderLayout.CENTER);
        noConnection.add(toolbar3, BorderLayout.SOUTH);

        noConnection.setVisible(false);

        /****Win Window****/
        winWindow = new JFrame("You Win!");
        winWindow.setSize(1000/2, 600/2);
        winWindow.setResizable(false);
        winWindow.setLayout(new BorderLayout());
        winWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels
        JPanel winPanel = new JPanel();
        winPanel.setBackground(Color.WHITE);

        // Create icon
        ImageIcon winIcon = new ImageIcon(Client.class.getResource("/Menu/win.png"));
        JLabel winLabel = new JLabel(winIcon, JLabel.CENTER);

        // Assemble window
        winPanel.add(winLabel);
        winWindow.add(winPanel, BorderLayout.CENTER);

        winWindow.setVisible(false);

        /****Lose Window***/
        //Create lose window
        loseWindow = new JFrame("You Lose...");
        loseWindow.setSize(1000/2, 600/2);
        loseWindow.setResizable(false);
        loseWindow.setLayout(new BorderLayout());
        loseWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create panels
        JPanel losePanel = new JPanel();
        losePanel.setBackground(Color.WHITE);

        // Create icon
        ImageIcon loseIcon = new ImageIcon(Client.class.getResource("/Menu/lose.png"));

        // Create components
        JLabel loseLabel = new JLabel(loseIcon, JLabel.CENTER);

        // Assemble window
        losePanel.add(loseLabel);
        loseWindow.add(losePanel, BorderLayout.CENTER);
        //Set to invisible
        loseWindow.setVisible(false);


        /******Game Window******/


        //This is to get the client's screen size to adjust the game window accordingly

        gameWin.addKeyListener(control);
        gameWin.setSize(1280, 800);
        gameWin.addKeyListener(control);
        gameWin.setLocationRelativeTo(enterInfo);
//        gameWin.setContentPane(new backImage(ImageIO.read(new File("resources/bg.jpg"))));
        gameWin.getContentPane().add(gameMap);
        gameWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWin.setResizable(false);
        gameWin.setVisible(false);

        //Load character sprites
        loadCharacters();
        
        //Set default character
        self.setCharacter(GCharacter.PL_MT);

        //Initialize tilesets
        wall = new ImageIcon(Client.class.getResource("/Tilesets/wall.png")).getImage();
        rock = new ImageIcon(Client.class.getResource("/Tilesets/rock.png")).getImage();
        heart = new ImageIcon(Client.class.getResource("/heart.png")).getImage();
        grass = new ImageIcon(Client.class.getResource("/Tilesets/grass.png")).getImage();
        ice = new ImageIcon(Client.class.getResource("/Tilesets/ice.png")).getImage();
        lava = new ImageIcon(Client.class.getResource("/Tilesets/lava.png")).getImage();
        dirt = new ImageIcon(Client.class.getResource("/Tilesets/dirt.png")).getImage();
        backgroundImage = new ImageIcon("resources/bg.jpg").getImage();

    }


    //Matthew Zeng
    /***************Main Game*****************/
    public static void mainGame (String username, String ip, String port) throws Exception {
    	//Connect with the server through connection manager
        cm = new ConnectionManager(ip, port, username);
        //System.out.println(Arrays.deepToString(map));
        //Main Game Loop
        GameLoop gl = new GameLoop();
        new Thread(gl).start();
        Thread.sleep(1500); //Sleep for 1500
        mapRow = map.length;
        mapColumn = map[0].length;
        //Set map x,y size
        mapX = (mapColumn+3)*ratioX;
        mapY = (mapRow+4)*ratioY;
        //Set game window size
    }


    //Matthew Zeng
    //This section controls the movements of the player
    /***********************************Control Movements************************/
    public static int countAir = 0;
    public static int cntJmp = 0;
    private static boolean jumpable = false;
    public static void controlMove(){
    	//If the player is on the ground, it can jump
        jumpable=checkCollide(self.getX(), self.getY());
        //If user presses right
        if(control.isPressingRight()){
        	//If the player can go right without getting into obstacles
            if(control.testValid(1)){
            	//Set the side the player facing to right - 1
                self.setFacing(1);
                //Move the player to the right
                self.setX(self.getX()+1);
            }
            //Else if user is pressing left
        }
        if(control.isPressingLeft()){
        	//If the player can go left without getting into obstacles 
            if(control.testValid(0)){
            	//Set the player facing to left - 0
                self.setFacing(0);
                self.setX(self.getX()-1);
            }
            //Else if user is pressing up
        }
        if (control.isPressingUp()){
        	//Test whether the user can jump without getting into obstacles
            if (control.testValid(2)){
            	//If the player is on the ground,
            	//and the times it jump is less than the height it is allowed
                if (jumpable&&cntJmp<Control.jumpHeight){
                	//Move the player up
                    self.setY(self.getY()-Control.jumpHeight);
                    cntJmp++;
                }else{
                	//Else the player cannot jump
                    jumpable = false;
                }
            }
        }
        //Run the gravity method
        gravity();
        //Update the player coordinates
        update();
        //If the player is attacking
        if (self.isAttacking()){
        	//Tell the server it's position and the damage
            cm.attack(self.getX(), self.getY(), 0.05);
        }else {
        	//Else tells the server it's position
            cm.updateLoc(self.getX(), self.getY());
        }
    }
    //Gravity method
    public static void gravity(){
    	//If the player is not in contact with anything
        if(!checkCollide(self.getX(),self.getY())){
        	//If the reason that the player is not in contact with anything is
        	//NOT because it's jumping
        	//Fall down
            if(!jumpable){
                cntJmp=0; //Player's not jumping, therefore set countJump to 0
                //Fall
                self.setY(self.getY()+1);
            }
            //Count the amount of time it's in the air
            countAir++;
        }else countAir=0; //Set countAir to false if player's on the ground
    }

    //Matthew Zeng
    //This section displays the animations of the main game
    /*****************Animations**************************/
    //Declare HashMaps of the fighting/walking conditions of other players
    static HashMap<UUID, Integer> fightCntOther = new HashMap<UUID, Integer>();
    static HashMap<UUID, Integer> walkCntOther = new HashMap<UUID, Integer>();
    static HashMap<UUID, Integer> preXOther = new HashMap<UUID, Integer>();
    //Declare the variables to count attack,walk,previous x,y for animation use
    static int fightCnt = 0, walkCnt = 0, preX = self.getX();
    //GameMap class to paint the game
    public static class GameMap extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage,0,0,1280,800,this);
            //The usage of countX and countY is to count the coordinates from top-left to bottom-right
            int countX = 0,countY = 0;
            //Declare drawX and drawY variables that will set the coordinates
            int drawX,drawY;
            char element; //Declare the element variable that's from the map array
            String gCharacter;
            try {
            	//15 is the Y radius in respect to the player,
            	//The ratioY is the y-ratio that transforms the 2d map array into a coordinate in the map window
            	//Read the Y from the coordinate that has been divided by the ratio and +/- the radius,
            	//Take the maximum of the result comparing with 0 so it's not going to go off-bounds
            	//Same with taking the minimum of the result comparing with the maximum height of the map
                for (int y = Math.max(self.getY() / ratioY - 15, 0); y < Math.min(self.getY() / ratioY + (15 * 2), map.length); y++) {
                	//CountY starts counting from the beginning for drawing the exact y coordinates
                    countY++;
                    //20 is the X radius in respect to the player,
                    //The ratioX is the x-ratio that transforms the 2d map array into a coordinate in the map window
                    //Read the X from the coordinate that has been divided by the ratio and +/- the radius,
                    //Take the maximum of the result comparing with 0 so it's not going to go off-bounds
                    //Same with taking the minimum of the result comparing with the maximum width of the map
                    for (int x = Math.max(self.getX() / ratioX - 20, 0); x < Math.min(self.getX() / ratioX + (20 * 2)+10, map[0].length); x++) {
                    	//CountX starts counting from the beginning for drawing the exact x coordinates
                        countX++;
                        //Take the element within the radius from the map array
                        element = map[y][x];
                        //Set the drawX and drawY coordinates to the countX/Y multiply by the ratios
                        // then multiply by 4 and 2 specifically for enlarging into the right size of the camera
                        // to convert into a coordinate in the game window
                        drawX = countX * ratioX * 4;
                        drawY = countY * ratioY * 2;
                        //Display the elements tiles

                        drawX -= 32;
                        drawY -= 32;

                        if (element == 'H') {
                            g.drawImage(wall, drawX, drawY, this);
                        } else if (element == 'x') {
                            g.drawImage(rock, drawX, drawY, this);
                        } else if (element == 's') {
                            g.drawImage(grass, drawX, drawY, this);
                        } else if(element == 'i'){
                        	g.drawImage(ice, drawX, drawY, this);
                        } else if(element == 'f'){
                        	g.drawImage(lava, drawX, drawY, this);
                        } else if(element == 'd'){
                        	g.drawImage(dirt, drawX, drawY, this);
                        }

                        //Display Players Section
                        //Add a little bit amount to the x,y coordinates
                        //when displaying player so it's not off
                        drawX += 30;
                        drawY += 15;
                        
                        //Set font for drawing the players' names
                        g.setFont(new Font("TimesRoman", Font.BOLD, 15));
                        
                        if (self.getX() / ratioX == x && self.getY() / ratioY == y) {
                            //Draw the name of the player
                            Utils.centerStr(g, self.getName(), 100, drawX-10, drawY-20);

                            //Draw the health bars
                            if (self.getHealth()>=0){
                                g.drawImage(heart, drawX+5, drawY - 15, this);
                            }
                            if (self.getHealth()>=20){
                                g.drawImage(heart, drawX + 20, drawY - 15, this);
                            }
                            if (self.getHealth()>=40){
                                g.drawImage(heart, drawX + 35, drawY - 15, this);
                            }
                            if (self.getHealth()>=60){
                                g.drawImage(heart, drawX + 50, drawY - 15, this);
                            }
                            if (self.getHealth()>=80){
                                g.drawImage(heart, drawX + 65, drawY - 15, this);
                            }

                            gCharacter = self.getCharacter().toString();
                            
                            //If the player's facing right
                            if (self.getFacing() == 1) {
                            	//If the player's attacking
                                if (self.isAttacking()) {
                                    playAttackSound(self);
                                	//Display the movement animation
                                    if (fightCnt==75) fightCnt=0;
                                    if(fightCnt>37){
                                        g.drawImage(Characters[gameCharacter][1], drawX, drawY, this);
                                        //Display attack special effect
                                        if(gCharacter.equals("PL_MT")){
                                            for(int i = 0;i<2*5;i++){
                                                g.drawImage(attackEffect4[i], drawX+10, drawY-60, this);
                                            }
                                        }else if(gCharacter.equals("PL_Mark")){
                                            for(int i = 0;i<3*5;i++){
                                                g.drawImage(attackEffect3[i], drawX+20, drawY-80, this);
                                            }
                                        }else if(gCharacter.equals("PL_Tim")){
                                            for(int i = 0;i<2*5;i++){
                                                g.drawImage(attackEffect5[i], drawX+10, drawY-60, this);
                                            }
                                        }else if(gCharacter.equals("PL_Alex")){
                                            for(int i = 0;i<4*5;i++){
                                                g.drawImage(attackEffect2[i], drawX+10, drawY-50, this);
                                            }
                                        }else if(gCharacter.equals("PL_Nikolai")){
                                            for(int i = 0;i<3*5;i++){
                                                g.drawImage(attackEffect1[i], drawX+10, drawY-50, this);
                                            }
                                        }else if(gCharacter.equals("PL_Simon")){
                                            for(int i = 0;i<2*5;i++){
                                                g.drawImage(attackEffect6[i], drawX+10, drawY-60, this);
                                            }
                                        }
                                    }else {
                                        g.drawImage(Characters[gameCharacter][0], drawX, drawY, this);
                                    }
                                    //Count the amount of time it attacks
                                    fightCnt++;
                                } else {//If player's not attacking
                                	//If the player's coordinate is different from the previous coordinate
                                	// - infers player's moving, draw the moving animation
                                    if (self.getX()!=preX) {
                                        if (walkCnt == 50) walkCnt = 0;
                                        if (walkCnt > 25) {
                                            g.drawImage(Characters[gameCharacter][0], drawX, drawY, this);
                                        } else {
                                            g.drawImage(Characters[gameCharacter][4], drawX, drawY, this);
                                        }
                                        //Set the current coordinate to previous coordinate
                                        preX=self.getX();
                                        //Count the number of times it's moving
                                        walkCnt++;
                                    }else{
                                    	//Else if the player's still, simply draw the player
                                        g.drawImage(Characters[gameCharacter][0], drawX, drawY, this);
                                    }
                                }
                                //If the player's facing left
                            } else if (self.getFacing() == 0) {
                            	//If the player's attacking
                                if (self.isAttacking()) {
                                    playAttackSound(self);
                                	//Display attack animation
                                    if (fightCnt==75) fightCnt=0;
                                    if(fightCnt>37){
                                        g.drawImage(Characters[gameCharacter][3], drawX, drawY, this);
                                        //Display attack special effects
                                        if(gCharacter.equals("PL_MT")){
                                            for(int i = 0;i<2*5;i++){
                                                g.drawImage(attackEffect4[i], drawX-120, drawY-60, this);
                                            }
                                        }else if(gCharacter.equals("PL_Mark")){
                                            for(int i = 0;i<3*5;i++){
                                                g.drawImage(attackEffect3[i], drawX-110, drawY-80, this);
                                            }
                                        }else if(gCharacter.equals("PL_Tim")){
                                            for(int i = 0;i<2*5;i++){
                                                g.drawImage(attackEffect5[i], drawX-120, drawY-60, this);
                                            }
                                        }else if(gCharacter.equals("PL_Alex")){
                                            for(int i = 0;i<4*5;i++){
                                                g.drawImage(attackEffect2[i], drawX-120, drawY-50, this);
                                            }
                                        }else if(gCharacter.equals("PL_Nikolai")){
                                            for(int i = 0;i<3*5;i++){
                                                g.drawImage(attackEffect1[i], drawX-120, drawY-50, this);
                                            }
                                        }else if(gCharacter.equals("PL_Simon")){
                                            for(int i = 0;i<2*5;i++){
                                                g.drawImage(attackEffect6[i], drawX-120, drawY-60, this);
                                            }
                                        }
                                    }
                                    else {
                                        g.drawImage(Characters[gameCharacter][2], drawX, drawY, this);
                                    }
                                    //Count the times it's fighting
                                    fightCnt++;
                                } else { //If the player's not attacking
                                	//If the player's moving
                                    if (self.getX()!=preX) {
                                    	//display moving animation
                                        if (walkCnt == 50) walkCnt = 0;
                                        if (walkCnt > 25) {
                                            g.drawImage(Characters[gameCharacter][2], drawX, drawY, this);
                                        } else {
                                            g.drawImage(Characters[gameCharacter][5], drawX, drawY, this);
                                        }
                                        preX=self.getX();
                                        walkCnt++;
                                    }else{
                                        g.drawImage(Characters[gameCharacter][2], drawX, drawY, this);
                                    }
                                }
                            }
                        }
                        //Draw other characters
                        String otherCharacter;
                        int otherChar = 0;
                        Iterator<Player> ids = players.values().iterator();
                        while (ids.hasNext()){
                            Player p = ids.next();
                            if (p.getX() / ratioX == x && p.getY() / ratioY == y) {
                            	//Draw names of other players
                                Utils.centerStr(g, p.getName(), 100, drawX-10, drawY-20);
                                //Draw health bars of other players
                                if (p.getHealth()>=0){
                                    g.drawImage(heart, drawX+5, drawY - 15, this);
                                }
                                if (p.getHealth()>=20){
                                    g.drawImage(heart, drawX + 20, drawY - 15, this);
                                }
                                if (p.getHealth()>=40){
                                    g.drawImage(heart, drawX + 35, drawY - 15, this);
                                }
                                if (p.getHealth()>=60){
                                    g.drawImage(heart, drawX + 50, drawY - 15, this);
                                }
                                if (p.getHealth()>=80){
                                    g.drawImage(heart, drawX + 65, drawY - 15, this);
                                }
                                //Get the character of the players
                                otherCharacter = p.getCharacter().toString();
                                //AddCharacter
                                //Set the otherChar variable for displaying the character sprites from 2d sprite array
                                if (otherCharacter.equals("PL_MT")) otherChar = 0;
                                else if (otherCharacter.equals("PL_Mark")) otherChar = 1;
                                else if (otherCharacter.equals("PL_Tim")) otherChar = 2;
                                else if (otherCharacter.equals("PL_Alex")) otherChar = 3;
                                else if (otherCharacter.equals("PL_Nikolai")) otherChar = 4;
                                else if (otherCharacter.equals("PL_Simon")) otherChar = 5;
                                //If player's facing right
                                if (p.getFacing()==1){
                                	//if player's attacking
                                    if (p.isAttacking()) {
                                        playAttackSound(p);
                                    	//Draw fighting animation
                                        if (fightCntOther.get(p.getUUID()) == 75) fightCntOther.put(p.getUUID(), 0);
                                        if (fightCntOther.get(p.getUUID()) > 37) {
                                            g.drawImage(Characters[otherChar][1], drawX, drawY, this);
                                            //Display attack special effect
                                            if(otherCharacter.equals("PL_MT")){
                                                for(int i = 0;i<2*5;i++){
                                                    g.drawImage(attackEffect4[i], drawX+10, drawY-60, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Mark")){
                                                for(int i = 0;i<3*5;i++){
                                                    g.drawImage(attackEffect3[i], drawX+20, drawY-80, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Tim")){
                                                for(int i = 0;i<2*5;i++){
                                                    g.drawImage(attackEffect5[i], drawX+10, drawY-60, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Alex")){
                                                for(int i = 0;i<4*5;i++){
                                                    g.drawImage(attackEffect2[i], drawX+10, drawY-50, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Nikolai")){
                                                for(int i = 0;i<3*5;i++){
                                                    g.drawImage(attackEffect1[i], drawX+10, drawY-50, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Simon")){
                                                for(int i = 0;i<2*5;i++){
                                                    g.drawImage(attackEffect6[i], drawX+10, drawY-60, this);
                                                }
                                            }
                                        } else {
                                            g.drawImage(Characters[otherChar][0], drawX, drawY, this);
                                        }
                                        //Count fight
                                        fightCntOther.put(p.getUUID(),fightCntOther.get(p.getUUID())+1);
                                    }else{//If the player's not attacking
                                    	//If player's moving
                                        if (p.getX()!=preXOther.get(p.getUUID())) {
                                        	//Display moving animations
                                            if (walkCntOther.get(p.getUUID()) == 50) walkCntOther.put(p.getUUID(), 0);
                                            if (walkCntOther.get(p.getUUID()) > 25) {
                                                g.drawImage(Characters[otherChar][0], drawX, drawY, this);
                                            } else {
                                                g.drawImage(Characters[otherChar][4], drawX, drawY, this);
                                            }
                                            preXOther.put(p.getUUID(), p.getX());
                                            walkCntOther.put(p.getUUID(), walkCntOther.get(p.getUUID())+1);
                                        }else{
                                        	//Display player sprite
                                            g.drawImage(Characters[otherChar][0], drawX, drawY, this);
                                        }
                                    }
                                }else{//If player's facing left
                                	//If player's attacking
                                    if(p.isAttacking()){
                                        playAttackSound(p);
                                    	//Display attack animation
                                        if (fightCntOther.get(p.getUUID()) == 75) fightCntOther.put(p.getUUID(), 0);
                                        if (fightCntOther.get(p.getUUID()) > 37) {
                                            g.drawImage(Characters[otherChar][3], drawX, drawY, this);
                                            //Display attack special effect
                                            if(otherCharacter.equals("PL_MT")){
                                                for(int i = 0;i<2*5;i++){
                                                    g.drawImage(attackEffect4[i], drawX-120, drawY-60, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Mark")){
                                                for(int i = 0;i<3*5;i++){
                                                    g.drawImage(attackEffect3[i], drawX-110, drawY-80, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Tim")){
                                                for(int i = 0;i<2*5;i++){
                                                    g.drawImage(attackEffect5[i], drawX-120, drawY-60, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Alex")){
                                                for(int i = 0;i<4*5;i++){
                                                    g.drawImage(attackEffect2[i], drawX-120, drawY-50, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Nikolai")){
                                                for(int i = 0;i<3*5;i++){
                                                    g.drawImage(attackEffect1[i], drawX-120, drawY-50, this);
                                                }
                                            }else if(otherCharacter.equals("PL_Simon")){
                                                for(int i = 0;i<2*5;i++){
                                                    g.drawImage(attackEffect6[i], drawX-120, drawY-60, this);
                                                }
                                            }
                                        }
                                        else {
                                            g.drawImage(Characters[otherChar][2], drawX, drawY, this);
                                        }
                                        fightCntOther.put(p.getUUID(),fightCntOther.get(p.getUUID())+1);
                                    }else{
                                    	//If player's walking
                                        if (p.getX()!=preXOther.get(p.getUUID())) {
                                        	//Display walking animation
                                            if (walkCntOther.get(p.getUUID()) == 50) walkCntOther.put(p.getUUID(), 0);
                                            if (walkCntOther.get(p.getUUID()) > 25) {
                                                g.drawImage(Characters[otherChar][2], drawX, drawY, this);
                                            } else {
                                                g.drawImage(Characters[otherChar][5], drawX, drawY, this);
                                            }
                                            preXOther.put(p.getUUID(), p.getX());
                                            walkCntOther.put(p.getUUID(), walkCntOther.get(p.getUUID())+1);
                                        }else{
                                        	//Display walking sprite
                                            g.drawImage(Characters[otherChar][2], drawX, drawY, this);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Set countX to 0 for the next row
                    countX = 0;
                }
            }catch (NullPointerException e){}
        }
        private void playAttackSound(Player p){
            if (p.getCharacter()==GCharacter.PL_Alex) Utils.playMusic("alex", soundAlex);
            else if (p.getCharacter()==GCharacter.PL_Mark) Utils.playMusic("mark", soundMark);
            else if (p.getCharacter()==GCharacter.PL_MT) Utils.playMusic("mt", soundMT);
            else if (p.getCharacter()==GCharacter.PL_Nikolai) Utils.playMusic("nikolai", soundNikolai);
            else if (p.getCharacter()==GCharacter.PL_Simon) Utils.playMusic("simon", soundSimon);
            else if (p.getCharacter()==GCharacter.PL_Tim) Utils.playMusic("tim", soundTim);
        }
    }

    //Matthew Zeng
    //This method checks for collisions
    /**********************Check Collision*********************/
    public static boolean checkCollide(int x, int y){
        int x2 = (x+playerWidth);
        int y2 = (y+playerHeight);
        //If the player's coordinates out of the map, return true
        if(x<0||x2>mapX||y<0||y2>mapY) return true;
        //For the x's in the map
        for(int X = 0;X<map[0].length;X++){
        	//If the x is within the player's x - on the same column with the player
            if((x>=X*ratioX-playerWidth&&x<=(X+1)*ratioX)){
            	//Loop the y's around the player
                for(int Y = y/ratioY;Y<map.length;Y++){
                	//If there is an element that's not empty in that section
                    if(map[Y][X]!=' '){
                    	//If the player's bottom Y coordinate is lower than the obstacle, return true
                        if(y2>=Y*ratioY) return true;
                    }
                }
            }
        }
        return false;
    }

    //Matthew Zeng
    //Update the bottom right coordinates of the player
    public static void update(){
        playerX2 = self.getX()+playerWidth;
        playerY2 = self.getY()+playerHeight;
    }

    //Matthew Zeng
    //Display the map onto the console
    public static void display(){
        for(int i = Math.max(44-15,0);i<Math.min(44+15,map.length);i++){
            for(int j = Math.max(81-10,0);j<Math.min(81+10,map[i].length);j++){
                //if(map[i][j]!=' ')
                System.out.print("("+j*ratioX+","+i*ratioY+")");
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    //Matthew Zeng
    //Add character sections
    //Declare buffered images for AddCharacter
    static int charNum = 6; //Set amount of characters available
    static int gameCharacter; //Set the game character
    static JButton MTIcon;
    static BufferedImage MTSheet;
    static JButton MarkIcon;
    static BufferedImage MarkSheet;
    static JButton TimIcon;
    static BufferedImage TimSheet;
    static JButton AlexIcon;
    static BufferedImage AlexSheet;
    static JButton NikolaiIcon;
    static BufferedImage NikolaiSheet;
    static JButton SimonIcon;
    static BufferedImage SimonSheet;
    //Set the characters 2d bufferedimage array
    static BufferedImage[][]Characters = new BufferedImage[charNum][3*2];
    
    //Declare bufferedimage for Attack effects
    static BufferedImage attackSheet1;
    static BufferedImage[]attackEffect1 = new BufferedImage[3*5];
    static BufferedImage attackSheet2;
    static BufferedImage[]attackEffect2 = new BufferedImage[4*5];
    static BufferedImage attackSheet3;
    static BufferedImage[]attackEffect3 = new BufferedImage[3*5];
    static BufferedImage attackSheet4;
    static BufferedImage[]attackEffect4 = new BufferedImage[2*5];
    static BufferedImage attackSheet5;
    static BufferedImage[]attackEffect5 = new BufferedImage[2*5];
    static BufferedImage attackSheet6;
    static BufferedImage[]attackEffect6 = new BufferedImage[2*5];

    //Matthew
    //This method loads the characters from the sprite sheet into bufferedimage arrays
    public static void loadCharacters() throws IOException{
        //AddCharacter Sprites
        loadSprites(MTSheet, Characters[0], new File("resources/Characters/MT.png"), 3,2,playerWidth*4,playerHeight*2);
        loadSprites(MarkSheet, Characters[1], new File("resources/Characters/Mark.png"), 3,2,playerWidth*4,playerHeight*2);
        loadSprites(TimSheet, Characters[2], new File("resources/Characters/Tim.png"), 3,2,playerWidth*4,playerHeight*2);
        loadSprites(AlexSheet, Characters[3], new File("resources/Characters/Alex.png"), 3,2,playerWidth*4,playerHeight*2);
        loadSprites(NikolaiSheet, Characters[4], new File("resources/Characters/Nikolai.png"), 3,2,playerWidth*4,playerHeight*2);
        loadSprites(SimonSheet, Characters[5], new File("resources/Characters/Simon.png"), 3,2,playerWidth*4,playerHeight*2);
        
        //Load AddAnimation Sprites as well
        loadSprites(attackSheet1, attackEffect1, new File("resources/Animations/Attack1.png"),3,5,960/5,576/3);
        loadSprites(attackSheet2, attackEffect2, new File("resources/Animations/Attack2.png"),4,5,960/5,768/4);
        loadSprites(attackSheet3, attackEffect3, new File("resources/Animations/Attack3.png"),3,5,960/5,576/3);
        loadSprites(attackSheet4, attackEffect4, new File("resources/Animations/Attack4.png"),2,5,960/5,384/2);
        loadSprites(attackSheet5, attackEffect5, new File("resources/Animations/Attack5.png"),2,5,960/5,384/2);
        loadSprites(attackSheet6, attackEffect6, new File("resources/Animations/Attack6.png"),2,5,960/5,384/2);
        
    }
    
    //Matthew Zeng
    //This method loads the sprites
    public static void loadSprites(BufferedImage spriteSheet, BufferedImage[] spriteName, File img, int row, int col, int width, int height) throws IOException{
        spriteSheet = ImageIO.read(img);//Read the image from a spritesheet
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
            	//Get sub-images from a spritesheet
                spriteName[(i * col) + j] = spriteSheet.getSubimage(
                        j * width,
                        i * height,
                        width,
                        height
                );
            }
        }
    }

    //Matthew Zeng
    //Listener that listens to the menu buttons
    static class menuButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            JButton button = (JButton) event.getSource();
            String usage = button.getName();
            if(usage.equals("play")){ //if clicks play
                // Set game window position at menu
                enterInfo.setLocationRelativeTo(menu);
                // Deactivate menu
                menu.setVisible(false);
                // Activate game window
                enterInfo.setVisible(true);
            }else if(usage.equals("rules")){//if clicks rules
                // Set rules position at menu
                rules.setLocationRelativeTo(menu);
                // Deactivate menu
                menu.setVisible(false);
                // Activate rules
                rules.setVisible(true);
            }else if(usage.equals("credits")){//if clicks credits
                // Set credits position at menu
                credits.setLocationRelativeTo(menu);
                // Deactivate menu
                menu.setVisible(false);
                // Activate credits
                credits.setVisible(true);
            }else if(usage.equals("returnMenu")){//if clicks return
                // Set menu position at rules
                menu.setLocationRelativeTo(credits);
                // Deactivate credits
                credits.setVisible(false);
                // Deactivate rules
                rules.setVisible(false);
                //Deactivate win/lose windows
                winWindow.setVisible(false);
                loseWindow.setVisible(false);
                // Deactivate lost Connection
                noConnection.setVisible(false);
                // Activate menu
                menu.setVisible(true);
            }else if(usage.equals("start")){//if clicks starts
                try {
                    mainGame(username.getText(),ip.getText(),port.getText());
                    // Set game window position at enter info
                    gameWin.setLocationRelativeTo(enterInfo);
                    // Deactivate enter info
                    enterInfo.setVisible(false);
                    // Activate main game;
                    gameWin.setVisible(true);
                } catch (Exception e) {
                    //e.printStackTrace();
                    enterInfo.setVisible(false);
                    lostConnection();
                }
            }

        }
    }

    //Matthew Zeng
    //Method that displays lost connection
    public static void lostConnection(){
        battleMusic.stop();
        menuMusic.start();
        menuMusic.loop(10000);
    	//Close the game window and display no connection window
        noConnection.setLocationRelativeTo(Client.menu);
        gameWin.setVisible(false);
        noConnection.setVisible(true);
    }

    //Matthew Zeng
    //Choose player button listener for the user to choose a player when starting a game
    static class choosePlayerListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JButton button = (JButton)event.getSource();
            //Get the name of the buttons representing a character
            gameCharacter = Integer.parseInt(button.getName()); 
            //AddCharacter
            //Set the game character with the corresponding number
            if(gameCharacter ==0){
                self.setGCharacter(GCharacter.PL_MT);
            }else if(gameCharacter==1){
                self.setGCharacter(GCharacter.PL_Mark);
            }else if(gameCharacter==2) {
                self.setGCharacter(GCharacter.PL_Tim);
            }else if(gameCharacter==3) {
                self.setGCharacter(GCharacter.PL_Alex);
            }else if(gameCharacter==4) {
                self.setGCharacter(GCharacter.PL_Nikolai);
            }else if(gameCharacter==5) {
                self.setGCharacter(GCharacter.PL_Simon);
            }
        }
    }
 
}