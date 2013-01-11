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
	public void testSetInputFile() {
		LevelLoader loader = new LevelLoader();
		loader.setInputFile(new File("map2.lvl"));
		System.out.println(loader);
		assertEquals("Result",null,loader.readNext());
		
		loader.setInputFile(new File("map.lvl"));
		assertEquals("Result",0,loader.readNext()[0]);
	}

	@Test
	public void testReadNext() {
		LevelLoader loader = new LevelLoader();
		loader.setInputFile(new File("map.lvl"));
		System.out.println(loader);
		assertEquals("Result",0,loader.readNext()[0]);
		loader.closeStreams();
	}

	@Test
	public void testSetOutputFile() {
		File file = new File("output.lvl");
		LevelLoader loader = new LevelLoader();
		loader.setOutputFile(file);
		loader.setInputFile(file);
		Integer dummy[] = new Integer[2];
		dummy[0] = 42;
		dummy[1] = 42;
		loader.writeNext(dummy);
		assertEquals("Result",42,loader.readNext()[0]);
		file.delete();
		loader.closeStreams();
	}

	
	@Test
	public void testCloseStreams() {
		File file = new File("output.lvl");
		LevelLoader loader = new LevelLoader();
		loader.closeStreams();
		loader.setInputFile(file);
		loader.closeStreams();
		loader.setOutputFile(file);
		loader.closeStreams();
		Integer dummy[] = new Integer[1];
		dummy[0] = 42;
		loader.writeNext(dummy);
		assertEquals("Result",null,loader.readNext());
	}
}
