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
	public void testSearchOneWord(){
		textBuddy.runCommand("clear");
		
		textBuddy.runCommand("add one");
		textBuddy.runCommand("add ingress");
		textBuddy.runCommand("add dinky");
		
		String actual = textBuddy.search("ingress");
		String expected = "ingress";
		
		assertEquals(expected, actual);
		
	}
}
