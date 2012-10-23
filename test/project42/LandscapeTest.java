package project42;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LandscapeTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testLandscape() {
		Landscape landscape = new Landscape(null,10,20);
		assertEquals("Result",10,landscape.width);
		assertEquals("Result",20,landscape.height);
	}
}