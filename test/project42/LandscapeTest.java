package project42;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.controller.Landscape;
import de.htwg.project42.model.Block;
import de.htwg.project42.model.Enemy;
import de.htwg.project42.model.Player;
import de.htwg.project42.observer.Observer;

public class LandscapeTest {
	File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("test.lvl");
	}

	@Test
	public void testLandscape() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		assertEquals("Result", 400, landscape.getWidth());
		assertEquals("Result", 400, landscape.getHeight());
	}

	@Test
	public void testLeft() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		Block block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		Player player = landscape.getPlayer();
		player.setY(100);
		landscape.left();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x+10, block.getX());
		player.setX(100);
		x = block.getX();
		landscape.left();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x, block.getX());
	}

	@Test
	public void testRight() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		Block block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		Player player = landscape.getPlayer();
		player.setY(100);
		landscape.right();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x, block.getX());
		player.setX(100);
		landscape.right();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x - 10, block.getX());
	}


	@Test
	public void testGetPlayer() {
		Landscape landscape = new Landscape(map, null, 400, 400, 4);
		assertEquals("Result", landscape.getPlayer(), landscape.getPlayer());
	}

	@Test
	public void testGetEnemies() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		assertEquals("Result", landscape.getEnemies(), landscape.getEnemies());
	}
	
	@Test
	public void testGravity() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		Player player = landscape.getPlayer();
		player.setX(200);
		int y = player.getY();
		landscape.gravity();
		assertEquals("Result", y + landscape.getGravity() * 2, player.getY());
		player.setY(800);
		y = player.getY();
		landscape.gravity();
		assertEquals("Result", 0, player.getHealth());
		player.setJump(false);
		player.setY(0);
		y = player.getY();
		landscape.gravity();
		assertEquals("Result", y, player.getY());
		player.setJump(true);
		player.setY(100);
		y = player.getY();
		landscape.gravity();
		assertEquals("Result", y, player.getY());
		player.setJump(false);
		player.setY(600);
		y = player.getY();
		landscape.gravity();
		assertEquals("Result", y, player.getY());
	}

	/*
1,1,1,1
1,0,3,1
4,X,X,1
1,1,1,1
0,0,2,1
0,0,0,1
1,1,1,1
	 */
	@Test
	public void testHandleEnemies() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		landscape.setEnemyJumpChances(0);
		Player player = landscape.getPlayer();
		Enemy enemy = landscape.getEnemies().get(0);
		int health = player.getHealth();
		player.setX(enemy.getX());
		player.setY(enemy.getY());
		landscape.handleEnemies();
		assertEquals("Result", health - 1, player.getHealth());
		enemy.setX(500);
		int x = enemy.getX();
		landscape.handleEnemies();
		assertEquals("Result", x, enemy.getX());
		enemy.setX(400);
		landscape.handleEnemies();
		assertEquals("Result", 400, enemy.getX());
		System.out.println(enemy.getDirection());
		player.setX(enemy.getX());
		player.setY(enemy.getY());
		player.setLock(false);
		landscape.handleEnemies();
		assertEquals("Result", false, enemy.isDead());
		player.setX(enemy.getX());
		player.setY(enemy.getY() - player.getHeight()+10);
		player.setLock(false);
		landscape.handleEnemies();
		assertEquals("Result", false, enemy.isDead());
		player.setX(enemy.getX());
		player.setY(enemy.getY() - player.getHeight());
		player.setLock(false);
		landscape.handleEnemies();
		assertEquals("Result", true, enemy.isDead());
		health = player.getHealth();
		player.setX(enemy.getX());
		player.setY(enemy.getY());
		landscape.handleEnemies();
		assertEquals("Result", health, player.getHealth());
	}

	@Test
	public void testPause() {
		long time = System.currentTimeMillis();
		Landscape.pause(10);
		assertEquals("Result", false, time == System.currentTimeMillis());
	}

	@Test
	public void testJump() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.jump();
		Landscape.pause(100);
		assertEquals("Result", true, player.getY() == y);
	}
	
	@Test
	public void testStart() {
		Observer observer = new Observer(null, map, 100, 4);
		Landscape landscape = new Landscape(map, observer, 400, 400, 4);
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.start();
		Landscape.pause(100);
		assertEquals("Result", false, y == player.getY());
		player.setHealth(0);
		player.setY(100);
		y = 100;
		landscape.start();
		Landscape.pause(200);
		assertEquals("Result", true, y == player.getY());
	}
}