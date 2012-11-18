package project42;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class GameObjectTest {

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
		obj.jump = false;
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
		Observer observer = new Observer(obj, new File("map.lvl"), 100, 12);
		int y = obj.getY();
		obj.jump = false;
		obj.jump(observer, observer, 10, 10, true);
		Landscape.pause(100);
		assertEquals("Result",true,y == obj.getY());
		obj.y = 400;
		obj.x = 0;
		obj.jump = true;
		obj.jump(observer, observer, 10, 10, true);
		Landscape.pause(100);
		assertEquals("Result",false,y == obj.getY());
		obj.y = y;
		obj.x = 600;
		obj.jump = true;
		obj.jump(observer, observer, 10, 3, true);
		Landscape.pause(100);
		assertEquals("Result",false,y == obj.getY());
	}

}
