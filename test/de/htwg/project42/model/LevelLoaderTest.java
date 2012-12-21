package de.htwg.project42.model;

import static org.junit.Assert.*;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;

public class LevelLoaderTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testLevelLoader() {
		LevelLoader loader = new LevelLoader();
		loader.setInputFile(new File("map2.lvl"));
		assertEquals("Result",null,loader.readNext());
		
		loader.setInputFile(new File("map.lvl"));
		assertEquals("Result",0,loader.readNext()[0]);
	}

	@Test
	public void testReadNext() {
		LevelLoader loader = new LevelLoader();
		loader.setInputFile(new File("map.lvl"));
		assertEquals("Result",0,loader.readNext()[0]);
	}

}
