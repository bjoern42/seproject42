package de.htwg.project42.model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.controller.Implementation.Landscape;
import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.model.GameObjects.Implementation.Block;
import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.Player;

public class LevelTest{
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("testmap.lvl");
	}
	
	@Test
	public void testUpdate(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		LevelInterface level = new Level(player, 100, 12);
		level.loadData(map);
		BlockInterface block = level.getVisibleBlocks().get(0)[0];
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
		PlayerInterface player = new Player(0, 0, 10, 20);
		LevelInterface level = new Level(player, 100, 12);
		level.loadData(map);
		assertEquals("Result",false,level.isMovableArea(0, 500, 100, 200,LevelInterface.ENEMY_MOVING));
		assertEquals("Result",true,level.isMovableArea(0, 0, 100, 200,LevelInterface.ENEMY_MOVING));
		assertEquals("Result",true,level.isMovableArea(8000, 0, 100, 200,LevelInterface.ENEMY_MOVING));
		assertEquals("Result",true,level.isMovableArea(100, 500, 100, 200,LevelInterface.ENEMY_MOVING));
		assertEquals("Result",true,level.isMovableArea(200, 400, 100, 200,LevelInterface.PLAYER_MOVING));
		assertEquals("Result",true,level.isMovableArea(0, 300, 100, 200,LevelInterface.PLAYER_MOVING));
		assertEquals("Result",true,level.isMovableArea(0, 8000, 100, 200,LevelInterface.PLAYER_MOVING));
		
		assertEquals("Result",false,level.isMovableArea(610, 500, 100, 200,LevelInterface.PLAYER_MOVING));
		assertEquals("Result",true,level.isMovableArea(610, 500, 100, 200,LevelInterface.CRATE_MOVING));
	}
		
	@Test
	public void testHandleCrateCollision(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		LevelInterface level = new Level(player, 100, 12);
		level.loadData(map);
		BlockInterface crate = level.getCrates().get(0);
		player.setX(1000);
		assertEquals("Result",false,level.isMovableArea(610, 440, 100, 200,LevelInterface.PLAYER_MOVING));
		assertEquals("Result",false,level.isMovableArea(610, 500, 100, 200,LevelInterface.ENEMY_MOVING));
		
		assertEquals("Result",false,level.isMovableArea(610, 500, 100, 200,LevelInterface.PLAYER_MOVING));
		assertEquals("Result",true,level.isMovableArea(790, 500, 1, 200,LevelInterface.PLAYER_MOVING));
		crate.setX(600);
		assertEquals("Result",false,level.isMovableArea(690, 500, 100, 200,LevelInterface.PLAYER_MOVING));
		assertEquals("Result",true,level.isMovableArea(610, 500, 100, 200,LevelInterface.PLAYER_MOVING));
	}
	
	@Test
	public void testIsCrateMovable(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		LevelInterface level = new Level(player, 100, 12);
		level.loadData(map);
		BlockInterface crate = level.getCrates().get(0);
		EnemyInterface enemy = level.getEnemies().get(0);
		crate.setX(0);
		crate.setY(400);
		assertEquals("Result",true,level.isMovableArea(90, 300, 100, 200,LevelInterface.PLAYER_MOVING));
		crate.setX(700);
		crate.setY(600);
		enemy.setX(8000);
		assertEquals("Result",true,level.isMovableArea(790, 500, 1, 200,LevelInterface.PLAYER_MOVING));
		crate.setX(700);
		enemy.setX(600);
		assertEquals("Result",false,level.isMovableArea(790, 500, 10, 200,LevelInterface.PLAYER_MOVING));
		enemy.kill();
		assertEquals("Result",true,level.isMovableArea(790, 500, 1, 200,LevelInterface.PLAYER_MOVING));
		assertEquals("Result",true,level.isMovableArea(600, 500, 100, 200,LevelInterface.PLAYER_MOVING));
		level.getCrates().get(0).setX(1000);
		assertEquals("Result",true,level.isMovableArea(1090, 500, 100, 200,LevelInterface.PLAYER_MOVING));
	}
	
	@Test
	public void testJump(){
		PlayerInterface player = new Player(600, 0, 100, 200);
		LevelInterface level = new Level(player, 100 ,12);
		level.loadData(map);
		LandscapeInterface landscape = new Landscape(player, level, 1200, 800);
		
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
		LevelInterface level = new Level(null,100, 12);
		level.loadData(map);
		level.removeFirst();
		assertEquals("Result",1,level.getStart());
		level.setStart(8000);
		level.removeFirst();
		assertEquals("Result",8000,level.getStart());
	}
	
	@Test
	public void testRemoveLast(){
		LevelInterface level = new Level(null,100, 12);
		level.loadData(map);
		level.removeLast();
		assertEquals("Result",0,level.getStart());
		level.setStart(10);
		level.removeLast();
		assertEquals("Result",9,level.getStart());
	}
	
	@Test
	public void testIsInFrame(){
		LevelInterface level = new Level(null,100, 12);
		level.loadData(map);
		assertEquals("Result",false,level.isInFrame(1300));
		assertEquals("Result",true,level.isInFrame(800));
		assertEquals("Result",false,level.isInFrame(-200));
	}
	
	@Test
	public void testGetVisibleBlocks(){
		LevelInterface level = new Level(null,100, 12);
		level.loadData(map);
		assertEquals("Result",level.getLength(),level.getVisibleBlocks().size());
		level.loadData(new File("mapTUI.lvl"));
		assertEquals("Result",5,level.getVisibleBlocks().size());
	}
	
	@Test
	public void testGetEnemies(){
		LevelInterface level = new Level(null,100, 12);
		level.loadData(map);
		assertEquals("Result",level.getEnemies(),level.getEnemies());
	}
	
	@Test
	public void testBlocks(){
		LevelInterface level = new Level(null,100, 12);
		level.loadData(map);
		LinkedList<BlockInterface[]> list = new LinkedList<BlockInterface[]>();
		BlockInterface tmp[] = new Block[1];
		tmp[0] = new Block(0, 0, 100, 42);
		list.add(tmp);
		level.setBlocks(list);
		assertEquals("Result",42,level.getVisibleBlocks().get(0)[0].getType());
	}
}