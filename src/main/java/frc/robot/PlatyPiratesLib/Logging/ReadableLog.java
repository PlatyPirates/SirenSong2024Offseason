package frc.robot.PlatyPiratesLib.Logging;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.io.File;

/*
 * A log (saved as a .txt file) that notes the times at which events occured in the robots code,
 * such as enable/disable events, errors, and other details for debugging.
 * 
 * Log files are saved in ~/RobotLogs. If a user does not already have a RobotLogs folder, the program
 * will create one. Log files are named after the time and date when they are created.
 * 
 * Log statements include the date and time as well as a message to describe the event.
 * 
 */

public class ReadableLog {

    PrintWriter fileWriter;
    Date today = new Date();
    String recentOutput = "No log file output.";

    public ReadableLog(){
        try {
            File logFolder = new File("RobotLogs");
            if(!logFolder.exists()){
                logFolder.mkdir();
            }

            String logFileName = "RobotLogs\\RobotLog - " + today.toString() + ".txt";
            logFileName = logFileName.replace(':','꞉'); //The character ':' is not allowed in file names, so we replace all colons (the time is in format 00:00:00) with a character that looks the same, but has a different unicode.

            File logFile = new File(logFileName);
            logFile.createNewFile();

            fileWriter = new PrintWriter(new FileWriter(logFileName,true));
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                System.out.println("Error (ReadableLog/42): Failed to write to ReadableLog: File not found at specified path!");
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints a message to the log file. All messages are accompanied by the date and time in the format:
     * Day Month Date HR:MIN:SEC TIMEZONE Year
     * 
     * @param info the message to be output into the log
     */

    public void print(String info){
        String message = today.toString() + " - " + info;

        try{
            if(!message.equals(recentOutput)){
                fileWriter.println(today.toString() + " - " + info);
                recentOutput = message;
            }
        } catch(Exception e){
            e.printStackTrace();;
        }
    }

    /**
     * Returns the most recent message printed to the log file. This can be uploaded to SmartDashboard
     * or any other driver consoles for debugging purposes.
     * 
     * @return recent message output to the log file
     */

    public String getRecentOutput(){
        return recentOutput;
    }
}
