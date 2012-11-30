package de.htwg.project42.model;

import static org.junit.Assert.*;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LevelLoaderTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testLevelLoader() {
		LevelLoader mapNotExist = new LevelLoader(new File("map2.lvl"));
		LevelLoader mapExist = new LevelLoader(new File("map.lvl"));
		
		assertEquals("Result",null,mapNotExist.readNext());
		assertEquals("Result",0,mapExist.readNext()[0]);
	}

	@Test
	public void testReadNext() {
		LevelLoader mapExist = new LevelLoader(new File("map.lvl"));
		assertEquals("Result",0,mapExist.readNext()[0]);
	}

}
