package de.htwg.project42.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.model.Block;
import de.htwg.project42.model.Enemy;
import de.htwg.project42.model.GameObject;
import de.htwg.project42.model.Player;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.observer.Observer;

public class LandscapeTest implements Observable{
private List<Block[]> objects = new LinkedList<Block[]>();
private Observer observer;
private Landscape landscape;

	@Before
	public void setUp() throws Exception {
		initialise();
	}

	private void initialise(){
		if(landscape == null){
			File mapF = null;
			for(File f: new File(".").listFiles()){
				if(f.getName().endsWith(".lvl")){
					mapF = f;
					break;
				}
			}
			int map[][] = new int[7][];
			int row[] = {1,1,1,1};
			map[0] = row;
			int row1[] = {1,0,3,1};
			map[1] = row1;
			int row2[] = {4,0,0,1};
			map[2] = row2;
			int row3[] = {1,1,1,1};
			map[3] = row3;
			int row4[] = {0,0,2,1};
			map[4] = row4;
			int row5[] = {0,0,0,1};
			map[5] = row5;
			int row6[] = {1,1,1,1};
			map[6] = row6;
			for(int i=0; i<map.length;i++){
				Block b[] = new Block[map[i].length];
				for(int j=0; j<map[i].length;j++){
					b[j] = new Block(100*i, 100*j, 100, map[i][j]);
				}
				objects.add(b);
			}
			landscape = new Landscape(mapF, this, 400, 400, 4);
			observer = landscape.getObserver();
			observer.setBlocks(objects);
			Enemy e = landscape.getEnemies().get(0);
			e.setX(400);
			e.setY(200);
		}
	}

	@Test
	public void testLandscape() {
		assertEquals("Result", 400, landscape.getWidth());
		assertEquals("Result", 400, landscape.getHeight());
	}

	@Test
	public void testLeft() {
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
		assertEquals("Result", landscape.getPlayer(), landscape.getPlayer());
	}

	@Test
	public void testGetEnemies() {
		assertEquals("Result", landscape.getEnemies(), landscape.getEnemies());
	}
	
	@Test
	public void testGravity() {
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

	@Test
	public void testHandleEnemies() {
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
	public void testJump() {
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.jump();
		GameObject.pause(100);
		assertEquals("Result", true, player.getY() == y);
	}
	
	@Test
	public void testStart() {
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.start();
		GameObject.pause(100);
		assertEquals("Result", false, y == player.getY());
		player.setHealth(0);
		player.setY(100);
		y = 100;
		GameObject.pause(100);
		landscape.start();
		GameObject.pause(500);
		assertEquals("Result", true, y == player.getY());
	}

	@Override
	public void update(int pChange) {}
}