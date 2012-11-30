package de.htwg.project42.model;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.controller.Landscape;
import de.htwg.project42.model.Block;
import de.htwg.project42.model.GameObject;
import de.htwg.project42.model.Player;
import de.htwg.project42.observer.Observable;

public class LevelTest implements Observable{
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("map.lvl");
	}
	
	@Test
	public void testUpdate(){
		Player player = new Player(0, 0, 10, 20);
		Level level = new Level(player,map, 100, 12);
		Block block = level.getVisibleBlocks().get(0)[0];
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
		Player player = new Player(0, 0, 10, 20);
		Level level = new Level(player,map, 100, 12);
		assertEquals("Result",false,level.isMovableArea(0, 500, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(0, 0, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(8000, 0, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(100, 500, 100, 200,false));
		assertEquals("Result",true,level.isMovableArea(200, 400, 100, 200,true));
	}
	
	@Test
	public void testJump(){
		Landscape landscape = new Landscape(map,this,1200,800,12);
		Player player = landscape.getPlayer();
		int y = 500;
		player.setY(y);
		player.setJump(true);
		landscape.jump();
		assertEquals("Result",false,player.getJump());
		GameObject.pause(100);
		assertEquals("Result",false,y == player.getY());
	}
	
	@Test
	public void testRemoveFirst(){
		Level level = new Level(null,map,100, 12);
		level.removeFirst();
		assertEquals("Result",1,level.getStart());
		level.setStart(8000);
		level.removeFirst();
		assertEquals("Result",8000,level.getStart());
	}
	
	@Test
	public void testRemoveLast(){
		Level level = new Level(null,map,100, 12);
		level.removeLast();
		assertEquals("Result",0,level.getStart());
		level.setStart(10);
		level.removeLast();
		assertEquals("Result",9,level.getStart());
	}
	
	@Test
	public void testIsInFrame(){
		Level level = new Level(null,map,100, 12);
		assertEquals("Result",false,level.isInFrame(1300));
		assertEquals("Result",true,level.isInFrame(800));
		assertEquals("Result",false,level.isInFrame(-200));
	}
	
	@Test
	public void testGetVisibleBlocks(){
		Level level = new Level(null,map,100, 12);
		assertEquals("Result",level.getLength(),level.getVisibleBlocks().size());
		level = new Level(null,new File("mapTUI.lvl"),100, 12);
		assertEquals("Result",5,level.getVisibleBlocks().size());
	}
	
	@Test
	public void testGetEnemies(){
		Level level = new Level(null,map,100, 12);
		assertEquals("Result",level.getEnemies(),level.getEnemies());
	}

	@Override
	public void update() {}
}