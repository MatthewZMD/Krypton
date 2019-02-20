package com.projectkrypton.server.utils;

/**
 * Author: YI HAN (MARK) ZHANG 348628991
 *
 * This class is responsible for the formatted console output
 */

public class Logger {

    //LogType enum
	public enum LogType{WARN, INFO, ERROR}

    /**
     * Takes in logtype, information string and either with next line or not
     * @param t
     * @param s
     * @param nextLine
     */
	public static void print(LogType t, String s, boolean nextLine){
		switch (t){
			case WARN:
                if (nextLine) p("{WARN}: " + s);
                else pLine("{WARN}: " + s);
				break;
			case INFO:
                if (nextLine) p("{INFO}: " + s);
                else pLine("{INFO}: " + s);
                break;
			case ERROR:
                if (nextLine) p("{ERROR}: " + s);
                else pLine("{ERROR}: " + s);
                break;
			default:
				p("default");
		}
	}

    /**
     * Formatted string output with prefix with nextLine
     * @param s
     */
	private static void p(String s){
		System.out.println("[KryptonS] " + s);
	}

    /**
     * Formatted string output with prefix on the same line
     * @param s
     */
    private static void pLine(String s){
        System.out.print("[KryptonS] " + s);
    }
	
}
