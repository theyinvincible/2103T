package sg.edu.nus.cs2103t;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;


/**
 * TextBuddy is a Command Line Interface program which allows users to write into
 * their specified text file and edit it accordingly. Users can add, delete, and sort
 * lines. Users can also clear the text file or search for a word in the file.
 * @author JingYin
 *
 */
public class TextBuddy {
	//messages shown to users
	private static final String MESSAGE_BEGIN = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_ADD = "added to %1$s: '%2$s'";
	private static final String MESSAGE_DELETE = "deleted from %1$s: '%2$s'";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s";
	private static final String MESSAGE_SORT = "sorted contents of %1$s in alphabetical order";
	private static final String MESSAGE_SEARCH_FAIL = "%1$s can't be found in %2$s";
	private static final String MESSAGE_SEARCH_EMPTY = "Nothing to be sorted";
	private static final String MESSAGE_EMPTY = "%1$s is empty";
	private static final String MESSAGE_INVALID = "invalid command format: %1$s";
	private static final String MESSAGE_ERROR_WRITING_INTO_FILE = "ERROR: Unable to write into file";

	//possible command types
	enum COMMAND_TYPE{
		ADD_TEXT, DELETE_TEXT, CLEAR_TEXT, DISPLAY_TEXT, SORT_TEXT, INVALID, SEARCH_TEXT, EXIT;
	}
	
	//data structure for storing texts
	public static ArrayList<String> textStorage = new ArrayList<>();
	
	private static Scanner scanner = new Scanner(System.in);
	
	private static String textFileName;
	
	private static Integer FILE_NAME_POSITION = 0;
	private static Integer ARRAY_START_INDEX = 0;
	private static Integer START_LINE_COUNT = 1;
	private static Integer ERROR_INDICATOR = -1;
	private static Integer ADJUST_TO_ARRAY_INDEX = -1;
	
	
	/**
	 * creates TextBuddy object and waits for user inputs until
	 * terminated
	 * @param args
	 */
	public static void main(String[] args){
		String fileName = args[FILE_NAME_POSITION];
		TextBuddy textBuddy = new TextBuddy(fileName);
				
		showUser(String.format(MESSAGE_BEGIN, textFileName));
		
		while (true) {
			String userInput = textBuddy.getUserInput();
			String feedback = textBuddy.runCommand(userInput);
			showUser(feedback);
			textBuddy.writeIntoFile();
		}
	}
	
	
	//asks user for input and returns the user input
	public String getUserInput(){
		System.out.print("command: ");
		return scanner.nextLine();
	}
	
	
	//constructor: stores name of text file
	public TextBuddy(String fileName){
		textFileName = fileName;
	}
	
	//displays to the user text that is input into this method
	private static void showUser(String text){
		System.out.println(text);
	}
	
	//executes the command given by the user
	public String runCommand(String userInput){
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
		case SORT_TEXT:
			return sortText();
		case SEARCH_TEXT:
			return searchText(userInput);
		case INVALID:
			return String.format(MESSAGE_INVALID, userInput);
		case EXIT:
			writeIntoFile();
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
	private COMMAND_TYPE getCommand(String userInput){
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
		} else if (command.equals("sort")){
			return COMMAND_TYPE.SORT_TEXT;
		} else if (command.equals("search")) {
			return COMMAND_TYPE.SEARCH_TEXT;
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
	private String addText(String userInput){
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
	private String deleteText(String userInput){
		
		int deletePosition = positionToRemove(userInput);
		String deletedText;
				
		if (deletePosition < ARRAY_START_INDEX || deletePosition >= textStorage.size()){
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
	private Integer positionToRemove(String userInput){
		String number = removeFirstWord(userInput);
		try{
			return Integer.parseInt(number) + ADJUST_TO_ARRAY_INDEX;
		} catch (NumberFormatException e){
			return ERROR_INDICATOR;
		}
	}
	
	/**
	 * This operation deletes all text in the text file
	 * @return message indicating successful clearance
	 */
	private String clearText(){
		textStorage.clear();
		return String.format(MESSAGE_CLEAR, textFileName);
	}
	
	
	/**
	 * @return text contained within the text file
	 */
	private String displayText(){
		String allText = "", textLine;
		int lineCount = START_LINE_COUNT;
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
	
	
	
	//formats text line in the form of "#. input content"
	private String formatTextLine(int lineCount, String text) {
		String lineNumber = Integer.toString(lineCount);
		return lineNumber + ". " + text + "\r\n";
	}
	
	
	/**
	 * sorts the text in alphabetical order
	 * @return message to indicate text file has been sorted
	 * 		   or that file is empty and has nothing to sort
	 */
	private String sortText() {
		if (textStorage.isEmpty()){
			return MESSAGE_SEARCH_EMPTY;
		}
		
		Collections.sort(textStorage);
		return String.format(MESSAGE_SORT, textFileName);
	}
	
	
	/**
	 * searches text file for word in user input
	 * @return returns lines which contain the word being searched for
	 */
	private String searchText(String userInput){
		String searchedWord = removeFirstWord(userInput);
		String textContainingWord = "";
		String currentLine;
		int index = START_LINE_COUNT;
		
		Iterator<String> textIter = textStorage.iterator();
		
		while (textIter.hasNext()){
			currentLine = textIter.next();
			if (currentLine.contains(searchedWord)){
				textContainingWord += formatTextLine(index, currentLine);
			}
			index++;
		}
		
		if (textContainingWord.equals("")){
			return String.format(MESSAGE_SEARCH_FAIL, searchedWord, textFileName);
		}
		
		return textContainingWord;
	}
	
	
	//writes contents of textStorage into text file
	//this method performs I/O operations and may result in IOExceptions
	private void writeIntoFile(){
		try{
			File file = new File(findPathName());
			if (!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			String fileContents = displayText();
			bw.write(fileContents);
			bw.close();
		} catch (IOException e){
			System.out.println(MESSAGE_ERROR_WRITING_INTO_FILE);
		}
	}
	
	
	
	//returns path of text file
	private String findPathName() {
		return System.getProperty("user.dir")+"/"+textFileName;
	}
	
	//removes the first word of String input
	private String removeFirstWord(String line){
		return line.replace(getFirstWord(line), "").trim();
	}
	
	//returns first word of String input
	private String getFirstWord(String line){
		return line.trim().split("\\s+")[ARRAY_START_INDEX];
	}
	
}

