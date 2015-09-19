package sg.edu.nus.cs2103t;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextBuddyTest {
	TextBuddy textBuddy = new TextBuddy("testfile.txt");

	@Test
	public void testSortUniqueFirstLetters() {
		textBuddy.runCommand("clear");
		
		textBuddy.runCommand("add one");
		textBuddy.runCommand("add dinky");
		textBuddy.runCommand("add ingress");
		textBuddy.runCommand("sort");
		
		String actual = textBuddy.runCommand("display");
		String expected = "1. dinky\r\n2. ingress\r\n3. one\r\n";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSortSameWords(){
		textBuddy.runCommand("clear");
		
		textBuddy.runCommand("add one");
		textBuddy.runCommand("add ingress");
		textBuddy.runCommand("add dinky");
		textBuddy.runCommand("add ingress");
		textBuddy.runCommand("add ingress");
		textBuddy.runCommand("sort");
		
		String actual = textBuddy.runCommand("display");
		String expected = "1. dinky\r\n2. ingress\r\n3. ingress\r\n4. ingress\r\n5. one\r\n";

		assertEquals(expected, actual);
	}

	@Test
	public void sortEmptyFile(){
		textBuddy.runCommand("clear");
		
		String actual = textBuddy.runCommand("sort");
		String expected = "Nothing to be sorted";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSearchOneWord(){
		textBuddy.runCommand("clear");
		
		textBuddy.runCommand("add one");
		textBuddy.runCommand("add ingress");
		textBuddy.runCommand("add dinky");
		
		String actual = textBuddy.runCommand("search ingress");
		String expected = "2. ingress\r\n";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSearchMultipleLinesContainingWord(){
		textBuddy.runCommand("clear");
		
		textBuddy.runCommand("add i have five toes");
		textBuddy.runCommand("add constantly toeing the line");
		textBuddy.runCommand("add that's a toeriffic idea");
		textBuddy.runCommand("add this line shall not have the word toe");
		textBuddy.runCommand("add just kidding");
		
		String actual = textBuddy.runCommand("search toe");
		String expected = "1. i have five toes\r\n2. constantly toeing the line\r\n"
				+ "3. that's a toeriffic idea\r\n4. this line shall not have the word toe\r\n";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSearchEmptyFile(){
		textBuddy.runCommand("clear");
		
		String actual = textBuddy.runCommand("search word");
		String expected = "word can't be found in testfile.txt";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSearchUnfoundWord(){
		textBuddy.runCommand("clear");
		
		textBuddy.runCommand("add tew chee");
		textBuddy.runCommand("add chwee kueh");
		
		String actual = textBuddy.runCommand("search fries");
		String expected = "fries can't be found in testfile.txt";
		
		assertEquals(expected, actual);
	}
	
}
