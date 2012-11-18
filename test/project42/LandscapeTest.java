package project42;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LandscapeTest {
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("map.lvl");
	}

	@Test
	public void testLandscape() {
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		assertEquals("Result",1200,landscape.width);
		assertEquals("Result",800,landscape.height);
		landscape.jump();
	}
	
	@Test
	public void testLeft(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		Block block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		Player player = landscape.getPlayer();
		player.y = 500;
		landscape.left();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result",x,block.getX());
		player.y = 0;
		landscape.left();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result",x+10,block.getX());
	}
	
	@Test
	public void testRight(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		Block block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		Player player = landscape.getPlayer();
		player.y = 500;
		player.x = 400;
		landscape.right();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result",x,block.getX());
		player.y = 0;
		landscape.right();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result",x-10,block.getX());
	}
	
	@Test
	public void testGetPlayer(){
		Landscape landscape = new Landscape(map,null,1200,800,12);
		assertEquals("Result",landscape.player,landscape.getPlayer());
	}
	
	@Test
	public void testGetEnemies(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		assertEquals("Result",landscape.enemies,landscape.getEnemies());
	}
	
	@Test
	public void testGravity(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.gravity();
		assertEquals("Result",y+landscape.GRAVITY*2,player.getY());
		player.y = 800;
		y = player.getY();
		landscape.gravity();
		assertEquals("Result",0,player.getHealth());
		player.jump = false;
		player.y = 0;
		y = player.getY();
		landscape.gravity();
		assertEquals("Result",y,player.getY());
		player.jump = true;
		player.y = 600;
		y = player.getY();
		landscape.gravity();
		assertEquals("Result",y,player.getY());
		player.jump = false;
		player.y = 600;
		y = player.getY();
		landscape.gravity();
		assertEquals("Result",y,player.getY());
	}
	
	@Test
	public void testHandleEnemies(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		Player player = landscape.getPlayer();
		Enemy enemy = landscape.getEnemies().get(0);
		int health = player.getHealth();
		player.x = enemy.x;
		player.y = enemy.y;
		landscape.handleEnemies();
		assertEquals("Result",health-1,player.getHealth());
		enemy.x = 400;
		landscape.handleEnemies();
		assertEquals("Result",400,enemy.getX());
		enemy.x = 8000;
		landscape.handleEnemies();
		assertEquals("Result",8000,enemy.getX());
		player.x = enemy.x;
		player.y = enemy.y;
		player.lock = false;
		landscape.handleEnemies();
		assertEquals("Result",false,enemy.isDead());
		player.x = enemy.x;
		player.y = enemy.y-player.getHeight();
		player.lock = false;
		landscape.handleEnemies();
		assertEquals("Result",true,enemy.isDead());
		health = player.getHealth();
		player.x = enemy.x;
		player.y = enemy.y;
		landscape.handleEnemies();
		assertEquals("Result",health,player.getHealth());
	}
	
	@Test
	public void testPause(){
		long time = System.currentTimeMillis();
		Landscape.pause(10);
		assertEquals("Result",false,time == System.currentTimeMillis());
	}
	
	@Test
	public void testStart(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.start();
		Landscape.pause(100);
		assertEquals("Result",false,y == player.getY());
		player.setHealth(0);
		y = player.getY();
		landscape.start();
		Landscape.pause(100);
		assertEquals("Result",true,y == player.getY());
	}
}