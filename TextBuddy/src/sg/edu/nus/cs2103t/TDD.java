package sg.edu.nus.cs2103t;

import static org.junit.Assert.*;

import org.junit.Test;

public class TDD {

	@Test
	public void testSortUniqueFirstLetters() {
		TextBuddy.runCommand("add one");
		TextBuddy.runCommand("add dinky");
		TextBuddy.runCommand("add ingress");
		TextBuddy.runCommand("sort");
		
		String actual = TextBuddy.runCommand("display");
		String expected = "1. dinky\r\n2. ingress\r\n3. one\r\n";
		
		assertEquals(expected, actual);
	}

}
