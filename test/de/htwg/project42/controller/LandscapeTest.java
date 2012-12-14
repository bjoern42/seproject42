package de.htwg.project42.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.controller.Implementation.Landscape;
import de.htwg.project42.model.GameObjects.iBlock;
import de.htwg.project42.model.GameObjects.iEnemy;
import de.htwg.project42.model.GameObjects.iLevel;
import de.htwg.project42.model.GameObjects.iPlayer;
import de.htwg.project42.model.GameObjects.Implementation.Block;
import de.htwg.project42.model.GameObjects.Implementation.Enemy;
import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;
import de.htwg.project42.model.GameObjects.Implementation.Player;
import de.htwg.project42.observer.Observable;

public class LandscapeTest implements Observable{
private List<iBlock[]> objects = new LinkedList<iBlock[]>();
private iLevel level;
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
			int row4[] = {0,0,0,1};
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
			LevelLoader loader = new LevelLoader(mapF);
			iPlayer player = new Player(200, 0, 100, 200);
			level = new Level(loader, player, 100 ,4);
			landscape = new Landscape(player,level, 400, 400);
			landscape.addAnObserver(this);
			level = landscape.getLevel();
			level.setBlocks(objects);

			level.addEnemy(new Enemy(400, 200, 100));
		}
	}

	@Test
	public void testLandscape() {
		assertEquals("Result", 400, landscape.getWidth());
		assertEquals("Result", 400, landscape.getHeight());
	}

	@Test
	public void testLeft() {
		iBlock block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		iPlayer player = landscape.getPlayer();
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
		iBlock block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		iPlayer player = landscape.getPlayer();
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
		iPlayer player = landscape.getPlayer();
		iEnemy e = landscape.getEnemies().get(0);
		player.setX(200);
		e.setX(200);
		e.setY(0);
		int y1 = player.getY();
		int y2 = e.getY();
		landscape.gravity();
		assertEquals("Result", y1 + landscape.getGravity() * 2, player.getY());
		assertEquals("Result", y2 + landscape.getGravity() / 2, e.getY());
		player.setY(800);
		y1 = player.getY();
		landscape.gravity();
		assertEquals("Result", 0, player.getHealth());
		player.setJump(false);
		player.setY(0);
		e.setJump(false);
		e.setY(0);
		y1 = player.getY();
		y2 = e.getY();
		landscape.gravity();
		assertEquals("Result", y1, player.getY());
		assertEquals("Result", y2, e.getY());
		player.setJump(true);
		player.setY(100);
		y1 = player.getY();
		landscape.gravity();
		assertEquals("Result", y1, player.getY());
		player.setJump(false);
		player.setY(600);
		e.setX(8000);
		y1 = player.getY();
		y2 = e.getY();
		landscape.gravity();
		assertEquals("Result", y1, player.getY());
		assertEquals("Result", y2, e.getY());
	}

	@Test
	public void testHandleEnemies() {
		landscape.setEnemyJumpChances(0);
		iPlayer player = landscape.getPlayer();
		iEnemy enemy = landscape.getEnemies().get(0);
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
		iPlayer player = landscape.getPlayer();
		int y = player.getY();
		landscape.jump();
		player.pause(100);
		assertEquals("Result", true, player.getY() == y);
	}
	
	@Test
	public void testStart() {
		iPlayer player = landscape.getPlayer();
		int y = player.getY();
		landscape.start();
		player.pause(100);
		assertEquals("Result", false, y == player.getY());
		player.setHealth(0);
		player.setY(100);
		y = 100;
		player.pause(500);
		landscape.start();
		player.pause(500);
		assertEquals("Result", y, player.getY());
		player.setGoal(true);
		player.setHealth(1);
		player.setY(100);
		y = 100;
		player.pause(500);
		landscape.start();
		player.pause(500);
		assertEquals("Result", y,  player.getY());
	}
	
	@Test
	public void testAddAnObserver() {
		landscape.removeAllObserver();
		assertEquals("Result", 0, landscape.getObserver().size());
		landscape.addObserver(this);
		assertEquals("Result", this, landscape.getObserver().get(0));
	}
	
	@Override
	public void update() {}
}