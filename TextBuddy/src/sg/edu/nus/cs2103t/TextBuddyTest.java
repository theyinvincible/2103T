package sg.edu.nus.cs2103t;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextBuddyTest {
	String actual, expected;
	
	@Test
	public void addTest() {
		TextBuddy.runCommand("add one");
		TextBuddy.runCommand("add two");
		TextBuddy.runCommand("add three");
		TextBuddy.runCommand("add four");
		TextBuddy.runCommand("add five six seven");
		actual = TextBuddy.runCommand("display");
		expected = "1. one\r\n2. two\r\n3. three\r\n4. four\r\n5. five six seven\r\n";
		assertEquals(expected, actual);
	}

	@Test
	public void invalidDeleteTest(){
		actual = TextBuddy.runCommand("delete three");
		expected = "invalid command format: delete three";
		assertEquals(expected, actual);
		actual = TextBuddy.runCommand("delete 10");
		expected = "invalid command format: delete 10";
		assertEquals(expected, actual);
	}
	
	@Test
	public void deleteTest(){
		actual = TextBuddy.runCommand("delete 3");
		expected = "deleted from null: 'three'";
		assertEquals(expected, actual);
		actual = TextBuddy.runCommand("display");
		expected = "1. one\r\n2. two\r\n3. four\r\n4. five six seven\r\n";
		assertEquals(expected, actual);
	}
	
	@Test
	public void clearTest(){
		actual = TextBuddy.runCommand("clear");
		expected = "all content deleted from null";
		assertEquals(expected,actual);
		
		actual = TextBuddy.runCommand("display");
		expected = "null is empty";
		assertEquals(expected, actual);
	}
}
