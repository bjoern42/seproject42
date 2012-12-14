package de.htwg.project42.model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.controller.iLandscape;
import de.htwg.project42.controller.Implementation.Landscape;
import de.htwg.project42.model.GameObjects.iBlock;
import de.htwg.project42.model.GameObjects.iLevel;
import de.htwg.project42.model.GameObjects.iPlayer;
import de.htwg.project42.model.GameObjects.Implementation.Block;
import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;
import de.htwg.project42.model.GameObjects.Implementation.Player;

public class LevelTest{
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("map.lvl");
	}
	
	@Test
	public void testUpdate(){
		iPlayer player = new Player(0, 0, 10, 20);
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, player, 100, 12);
		iBlock block = level.getVisibleBlocks().get(0)[0];
		int oldX = block.getX();
		level.update(10);
		assertEquals("Result",oldX+10,block.getX());
		level.update(-10);
		assertEquals("Result",oldX,block.getX());
		level.update(-110);
		assertEquals("Result",true,block != level.getVisibleBlocks().get(0)[0]);
		level.update(+220);
		assertEquals("Result",true,block == level.getVisibleBlocks().get(0)[0]);
	}

	
	@Test
	public void testIsMovableArea(){
		iPlayer player = new Player(0, 0, 10, 20);
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, player, 100, 12);
		assertEquals("Result",false,level.isMovableArea(0, 500, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(0, 0, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(8000, 0, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(100, 500, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(200, 400, 100, 200,true));
		assertEquals("Result",true,level.isMovableArea(0, 300, 100, 200,true));
		assertEquals("Result",true,level.isMovableArea(0, 8000, 100, 200,true));
	}
		
	@Test
	public void testJump(){
		iPlayer player = new Player(600, 0, 100, 200);
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader,player, 100 ,12);
		iLandscape landscape = new Landscape(player, level, 1200, 800);
		
		int y = 500;
		player.setY(y);
		player.setJump(true);
		landscape.jump();
		assertEquals("Result",false,player.getJump());
		player.pause(100);
		assertEquals("Result",false,y == player.getY());
	}
	
	@Test
	public void testRemoveFirst(){
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, null,100, 12);
		level.removeFirst();
		assertEquals("Result",1,level.getStart());
		level.setStart(8000);
		level.removeFirst();
		assertEquals("Result",8000,level.getStart());
	}
	
	@Test
	public void testRemoveLast(){
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, null,100, 12);
		level.removeLast();
		assertEquals("Result",0,level.getStart());
		level.setStart(10);
		level.removeLast();
		assertEquals("Result",9,level.getStart());
	}
	
	@Test
	public void testIsInFrame(){
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, null,100, 12);
		assertEquals("Result",false,level.isInFrame(1300));
		assertEquals("Result",true,level.isInFrame(800));
		assertEquals("Result",false,level.isInFrame(-200));
	}
	
	@Test
	public void testGetVisibleBlocks(){
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, null,100, 12);
		assertEquals("Result",level.getLength(),level.getVisibleBlocks().size());
		loader = new LevelLoader(new File("mapTUI.lvl"));
		level = new Level(loader, null,100, 12);
		assertEquals("Result",5,level.getVisibleBlocks().size());
	}
	
	@Test
	public void testGetEnemies(){
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, null,100, 12);
		assertEquals("Result",level.getEnemies(),level.getEnemies());
	}
	
	@Test
	public void testBlocks(){
		LevelLoader loader = new LevelLoader(map);
		iLevel level = new Level(loader, null,100, 12);
		LinkedList<iBlock[]> list = new LinkedList<iBlock[]>();
		iBlock tmp[] = new Block[1];
		tmp[0] = new Block(0, 0, 100, 42);
		list.add(tmp);
		level.setBlocks(list);
		assertEquals("Result",42,level.getVisibleBlocks().get(0)[0].getType());
	}
}