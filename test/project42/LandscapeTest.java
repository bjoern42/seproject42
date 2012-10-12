package project42;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LandscapeTest {

	@Before
	public void setUp() throws Exception {
		Landscape tester = new Landscape(10,20);
		assertEquals("Result",10,tester.width);
		assertEquals("Result",20,tester.height);
	}

	@Test
	public void testLandscape() {
	}

	@Test
	public void testPrint() {
	}

}
