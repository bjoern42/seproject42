package de.htwg.project42.model;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import de.htwg.project42.model.GameObjects.Block;
import de.htwg.project42.model.GameObjects.GameObject;
import de.htwg.project42.model.GameObjects.Level;
import de.htwg.project42.model.GameObjects.Player;
import de.htwg.project42.observer.Observer;

public class GameObjectTest extends Observer{

	@Test
	public void testGameObject() {
		
	}

	@Test
	public void testIsInArea() {
		GameObject obj = new Block(10,20,10,1);
		Player player = new Player(0, 0, 10, 20);
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
		GameObject obj = new Player(0,0,100,200);
		assertEquals("Result",0,obj.getX());
	}

	@Test
	public void testGetY() {
		GameObject obj = new Player(0,0,100,200);
		assertEquals("Result",0,obj.getY());
	}

	@Test
	public void testGetWidth() {
		GameObject obj = new Player(0,0,100,200);
		assertEquals("Result",100,obj.getWidth());
	}

	@Test
	public void testGetHeight() {
		GameObject obj = new Player(0,0,100,200);
		assertEquals("Result",200,obj.getHeight());
	}

	@Test
	public void testGetJump() {
		GameObject obj = new Player(0,0,100,200);
		assertEquals("Result",true,obj.getJump());
		obj.setJump(false);
		assertEquals("Result",false,obj.getJump());
	}

	@Test
	public void testMove() {
		GameObject obj = new Player(0,0,100,200);
		obj.move(5,0);
		assertEquals("Result",5,obj.getX());
	}

	@Test
	public void testJump() {
		Player obj = new Player(600,500,100,200);
		Level level = new Level(obj, new File("map.lvl"), 100, 12);
		int y = obj.getY();
		obj.setJump(false);
		obj.jump(level, this, 10, 10, true);
		GameObject.pause(100);
		assertEquals("Result",true,y == obj.getY());
		obj.setY(400);
		obj.setX(0);
		obj.setJump(true);
		obj.jump(level, this, 10, 10, true);
		GameObject.pause(100);
		assertEquals("Result",false,y == obj.getY());
		obj.setY(y);
		obj.setX(600);
		obj.setJump(true);
		obj.jump(level, this, 10, 3, true);
		GameObject.pause(100);
		assertEquals("Result",false,y == obj.getY());
	}

	@Test
	public void testPause() {
		long time = System.currentTimeMillis();
		GameObject.pause(10);
		assertEquals("Result", false, time == System.currentTimeMillis());
	}

}
