package de.htwg.project42.model;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.model.GameObjects.Implementation.Block;
import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;
import de.htwg.project42.model.GameObjects.Implementation.Player;
import de.htwg.project42.observer.Observer;

public class GameObjectTest extends Observer{

	@Test
	public void testGameObject() {
		
	}

	@Test
	public void testIsInArea() {
		BlockInterface obj = new Block(10,20,10,1);
		PlayerInterface player = new Player(0, 0, 10, 20);
		assertEquals("Result",false,obj.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		player = new Player(11, 0, 10, 20);
		assertEquals("Result",false,obj.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		player = new Player(21, 0, 10, 20);
		assertEquals("Result",false,obj.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		player = new Player(11, 31, 10, 20);
		assertEquals("Result",false,obj.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		
		player = new Player(11, 21, 10, 20);
		assertEquals("Result",true,obj.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
	}

	@Test
	public void testGetX() {
		PlayerInterface obj = new Player(0,0,100,200);
		assertEquals("Result",0,obj.getX());
	}

	@Test
	public void testGetY() {
		PlayerInterface obj = new Player(0,0,100,200);
		assertEquals("Result",0,obj.getY());
	}

	@Test
	public void testGetWidth() {
		PlayerInterface obj = new Player(0,0,100,200);
		assertEquals("Result",100,obj.getWidth());
	}

	@Test
	public void testGetHeight() {
		PlayerInterface obj = new Player(0,0,100,200);
		assertEquals("Result",200,obj.getHeight());
	}

	@Test
	public void testGetJump() {
		PlayerInterface obj = new Player(0,0,100,200);
		assertEquals("Result",true,obj.getJump());
		obj.setJump(false);
		assertEquals("Result",false,obj.getJump());
	}

	@Test
	public void testMove() {
		PlayerInterface obj = new Player(0,0,100,200);
		obj.move(5,0);
		assertEquals("Result",5,obj.getX());
	}

	@Test
	public void testJump() {
		PlayerInterface obj = new Player(600,500,100,200);
		LevelInterface level = new Level(new LevelLoader(), obj, 100, 12);
		level.loadLevel(new File("map.lvl"));
		int y = obj.getY();
		obj.setJump(false);
		obj.jump(level, this, 10, 10, LevelInterface.PLAYER_MOVING);
		obj.pause(100);
		assertEquals("Result",true,y == obj.getY());
		obj.setY(500);
		obj.setJump(true);
		obj.jump(level, this, 10, 10, LevelInterface.PLAYER_MOVING);
		obj.pause(100);
		assertEquals("Result",false,y == obj.getY());
		obj.setY(400);
		obj.setJump(true);
		obj.jump(level, this, 10, 10, LevelInterface.PLAYER_MOVING);
		obj.pause(100);
		assertEquals("Result",false,y == obj.getY());
		obj.setY(400);
		obj.setJump(false);
		obj.jump(level, this, 10, 10, LevelInterface.PLAYER_MOVING);
		obj.pause(100);
		assertEquals("Result",false,y == obj.getY());
		obj.setY(400);
		obj.setX(0);
		obj.setJump(true);
		obj.jump(level, this, 10, 10, LevelInterface.PLAYER_MOVING);
		obj.pause(100);
		assertEquals("Result",false,y == obj.getY());
	}

	@Test
	public void testPause() {
		PlayerInterface obj = new Player(600,500,100,200);
		long time = System.currentTimeMillis();
		obj.pause(10);
		assertEquals("Result", false, time == System.currentTimeMillis());
	}

}
