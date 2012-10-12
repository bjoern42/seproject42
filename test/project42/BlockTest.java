package project42;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BlockTest {

	@Before
	public void setUp() throws Exception {
		Block tester = new Block(10,1);
		
		assertEquals("Result",1,tester.type);
		assertEquals("Result",10,tester.getWidth());
		assertEquals("Result",10,tester.getHeight());
	}

	@Test
	public void testPaint() {
	}

	@Test
	public void testBlock() {
	}

	@Test
	public void testToString() {
		Block tester = new Block(10,1);
		assertEquals("Result","[1]",tester.toString());
	}

}
