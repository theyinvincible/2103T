package sg.edu.nus.cs2103t;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class TextBuddy {
	//messages shown to users
	private static final String MESSAGE_BEGIN = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_ADD = "added to %1$s: '%2$s'";
	private static final String MESSAGE_DELETE = "deleted from %1$s: '%2$s'";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_INVALID = "invalid command format: %1$s";
		

	//possible command types
	enum COMMAND_TYPE{
		ADD_TEXT, DELETE_TEXT, CLEAR_TEXT, DISPLAY_TEXT, INVALID, EXIT;
	}
	
	//data structure for storing texts
	public static ArrayList<String> textStorage = new ArrayList<>();
	
	private static Scanner scanner = new Scanner(System.in);
	
	private static String textFileName;
	
	private static Integer FILE_NAME_POSITION = 0;
	private static Integer START_INDEX = 0;
	
	
	////////////////////////////////////////////////
	public static void main(String[] args){
		textFileName = args[FILE_NAME_POSITION];
				
		showUser(String.format(MESSAGE_BEGIN, textFileName));
		while (true) {
			System.out.print("command: ");
			String userInput = scanner.nextLine();
			String feedback = runCommand(userInput);
			showUser(feedback);
		}
	}
	
	private static void showUser(String text){
		System.out.println(text);
	}
	
	public static String runCommand(String userInput){
		if (userInput.trim().isEmpty()){
			return String.format(MESSAGE_INVALID, userInput);
		}
		
		COMMAND_TYPE command = getCommand(userInput);
		
		switch (command){
		case ADD_TEXT:
			return addText(userInput);
		case DELETE_TEXT:
			return deleteText(userInput);
		case CLEAR_TEXT:
			return clearText();
		case DISPLAY_TEXT:
			return displayText();
		case INVALID:
			return String.format(MESSAGE_INVALID, userInput);
		case EXIT:
			writeIntoFile(displayText());
			System.exit(0);
		default:
			throw new Error("Unrecognized command type");
		}
	}
	
	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * @param userInput
	 * 		the full string the user entered as the command
	 * @return the command type
	 */
	private static COMMAND_TYPE getCommand(String userInput){
		String command = getFirstWord(userInput);
		
		if (command == null){
			throw new Error("Command cannot be empty");
		}
		
		if (command.equalsIgnoreCase("add")){
			return COMMAND_TYPE.ADD_TEXT;
		} else if (command.equalsIgnoreCase("delete")){
			return COMMAND_TYPE.DELETE_TEXT;
		} else if (command.equalsIgnoreCase("clear")){
			return COMMAND_TYPE.CLEAR_TEXT;
		} else if (command.equalsIgnoreCase("display")){
			return COMMAND_TYPE.DISPLAY_TEXT;
		} else if (command.equalsIgnoreCase("exit")){
			return COMMAND_TYPE.EXIT;
		} else{
			return COMMAND_TYPE.INVALID;
		}
	}

	
	/**
	 * This operation is used to add text input from the user into the program
	 * @param userInput
	 * 		the full string the user entered as the command
	 * @return message to indicate that text has been added successfully
	 */
	private static String addText(String userInput){
		String textToAdd = removeFirstWord(userInput);
		textStorage.add(textToAdd);
		return String.format(MESSAGE_ADD, textFileName, textToAdd);
	}
	
	
	/**
	 * This operation is used to delete texts corresponding to the line number indicated
	 * by the user
	 * @param userInput
	 * 		the full string the user entered as the command
	 * @return message to indicate success or failure of the delete operation
	 */
	private static String deleteText(String userInput){
		
		int deletePosition = positionToRemove(userInput);
		String deletedText;
		
		if (deletePosition < START_INDEX || deletePosition > textStorage.size()){
			return String.format(MESSAGE_INVALID, userInput);
		} else if (textStorage.isEmpty()){
			return String.format(MESSAGE_EMPTY, textFileName);
		} else {
			deletedText = textStorage.remove(deletePosition);
			return String.format(MESSAGE_DELETE, textFileName, deletedText);
		}	
	}
	
	
	/**
	 * @param userInput
	 * @return the line number indicated by the user to remove from the text file
	 */
	private static Integer positionToRemove(String userInput){
		String number = removeFirstWord(userInput);
		try{
			return Integer.parseInt(number) - 1;
		} catch (NumberFormatException e){
			return -1;
		}
	}
	
	/**
	 * This operation deletes all text in the text file
	 * @return message indicating successful clearance
	 */
	private static String clearText(){
		textStorage.clear();
		return String.format(MESSAGE_CLEAR, textFileName);
	}
	
	
	/**
	 * @return text contained within the text file
	 */
	private static String displayText(){
		String allText = "", textLine;
		int lineCount = 1;
		Iterator<String> textIter = textStorage.iterator();
		
		if (textStorage.isEmpty()){
			return String.format(MESSAGE_EMPTY, textFileName);
		}

		while (textIter.hasNext()){
			textLine = formatTextLine(lineCount, textIter.next());
			allText += textLine;
			lineCount++;
		}
		
		return allText;
	}
	
	private static String formatTextLine(int lineCount, String text) {
		return Integer.toString(lineCount) + ". " + text + "\r\n";
	}
	
	private static void writeIntoFile(String text){
		try{
			File file = new File(findPathName());
			if (!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.close();
		} catch (IOException e){
			System.out.println("ERROR: Unable to write into file");
		}
	}
	
	private static String findPathName() {
		return System.getProperty("user.dir")+"/"+textFileName;
	}
	
	private static String removeFirstWord(String line){
		return line.replace(getFirstWord(line), "").trim();
	}
	
	private static String getFirstWord(String line){
		return line.trim().split("\\s+")[START_INDEX];
	}
	
}
