package de.htwg.project42.controller;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.controller.Implementation.Landscape;
import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;
import de.htwg.project42.model.GameObjects.Implementation.Player;
import de.htwg.project42.observer.Observable;

public class LandscapeTest implements Observable{
private LevelInterface level;
private Landscape landscape;
private File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("testmap.lvl");
		level = new Level(new LevelLoader(), 100, 12);
		PlayerInterface player = new Player(0, 0, 100, 200);
		landscape = new Landscape(player, level, 1200, 800);
		landscape.loadLevel(map);
		landscape.addAnObserver(this);
		level = landscape.getLevel();
	}

	@Test
	public void testLandscape() {
		assertEquals("Result", 1200, landscape.getWidth());
		assertEquals("Result", 800, landscape.getHeight());
	}

	@Test
	public void testLeft() {
		BlockInterface block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		PlayerInterface player = landscape.getPlayer();
		player.setY(0);
		player.setX(0);
		landscape.left();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x+10, block.getX());
		player.setY(600);
		player.setX(200);
		x = block.getX();
		landscape.left();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x, block.getX());
	}

	@Test
	public void testRight() {
		BlockInterface block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		PlayerInterface player = landscape.getPlayer();
		player.setY(0);
		player.setX(0);
		landscape.right();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x-10, block.getX());
		player.setX(600);
		player.setY(500);
		x = block.getX();
		landscape.right();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result", x, block.getX());
	}

	@Test
	public void testGetPlayer() {
		assertEquals("Result", landscape.getPlayer(), landscape.getPlayer());
	}

	@Test
	public void testGetEnemies() {
		assertEquals("Result", level.getEnemies(), landscape.getEnemies());
	}
	
	@Test
	public void testGetCrates() {
		assertEquals("Result", level.getCrates(), landscape.getCrates());
	}
	
	@Test
	public void testGravity() {
		PlayerInterface player = landscape.getPlayer();
		EnemyInterface e = landscape.getEnemies().get(0);
		BlockInterface crate = level.getCrates().get(0);
		player.setX(0);
		player.setY(0);
		e.setX(0);
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
		player.setY(500);
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
		crate.setX(0);
		crate.setY(0);
		y1 = crate.getY();
		landscape.gravity();
		assertEquals("Result", y1+LandscapeInterface.SPEED/2, crate.getY());
		crate.setY(600);
		crate.setX(700);
		y1 = crate.getY();
		landscape.gravity();
		assertEquals("Result", y1, crate.getY());
		crate.setY(8000);
		y1 = crate.getY();
		landscape.gravity();
		assertEquals("Result", y1, crate.getY());
		crate.setX(1300);
		crate.setY(600);
		y1 = crate.getY();
		landscape.gravity();
		assertEquals("Result", y1, crate.getY());
	}

	@Test
	public void testHandleEnemies() {
		landscape.setEnemyJumpChances(0);
		PlayerInterface player = landscape.getPlayer();
		EnemyInterface enemy = landscape.getEnemies().get(0);
		enemy.setX(400);
		enemy.setY(600);
		player.setX(1000);
		player.setY(1000);
		int x = enemy.getX();
		landscape.handleEnemies();
		assertEquals("Result", x, enemy.getX());
		enemy.setX(8000);
		x = enemy.getX();
		landscape.handleEnemies();
		assertEquals("Result", x, enemy.getX());
		enemy.setX(0);
		enemy.setY(0);
		int health = player.getHealth();
		player.setX(enemy.getX());
		player.setY(enemy.getY());
		landscape.handleEnemies();
		assertEquals("Result", health - 1, player.getHealth());
		enemy.setX(300);
		enemy.setY(600);
		x = enemy.getX();
		landscape.handleEnemies();
		assertEquals("Result", x, enemy.getX());
		enemy.setX(300);
		enemy.setY(600);
		x = enemy.getX();
		landscape.handleEnemies();
		assertEquals("Result", x+LandscapeInterface.SPEED/LandscapeInterface.ENEMY_SPEED_FACTOR, enemy.getX());
		enemy.setX(400);
		enemy.setY(600);
		enemy.changeDirection();
		x = enemy.getX();
		landscape.handleEnemies();
		assertEquals("Result", x-LandscapeInterface.SPEED/LandscapeInterface.ENEMY_SPEED_FACTOR, enemy.getX());
		player.setX(enemy.getX());
		player.setY(enemy.getY());
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
		player.setLock(false);
		landscape.handleEnemies();
		assertEquals("Result", health, player.getHealth());
		enemy.setX(0);
		enemy.setY(0);
		player.setX(1000);
		player.setY(1000);
		x = enemy.getX();
		landscape.handleEnemies();
		assertEquals("Result", x, enemy.getX());
	}
	
	@Test
	public void testStart() {
		PlayerInterface player = landscape.getPlayer();
		int y = player.getY();
		landscape.start();
		player.pause(100);
		assertEquals("Result", false, y == player.getY());
		player.setHealth(0);
		player.setY(100);
		y = 100;
		landscape.start();
		player.pause(1000);
		assertEquals("Result", 0, landscape.getObserver().size());
		player.setGoal(true);
		player.setHealth(1);
		player.setY(100);
		y = 100;
		player.pause(500);
		landscape.start();
		player.pause(500);
		assertEquals("Result", 0, landscape.getObserver().size());
	}
	
	@Test
	public void testAddAnObserver() {
		landscape.removeAllObserver();
		assertEquals("Result", 0, landscape.getObserver().size());
		landscape.addObserver(this);
		assertEquals("Result", this, landscape.getObserver().get(0));
	}
	
	@Test
	public void testRemoveAnObserver() {
		landscape.addObserver(this);
		assertEquals("Result", this, landscape.getObserver().get(0));
		int amount = landscape.getObserver().size();
		landscape.removeAnObserver(this);
		assertEquals("Result", amount-1, landscape.getObserver().size());
	}
	
	@Test
	public void testJump() {
		PlayerInterface player = landscape.getPlayer();
		int y = player.getY();
		player.setJump(false);
		landscape.jump(player, 10, 10, LevelInterface.PLAYER_MOVING);
		player.pause(100);
		assertEquals("Result",true,y == player.getY());
		player.setY(500);
		player.setJump(true);
		landscape.jump(player, 10, 10, LevelInterface.PLAYER_MOVING);
		player.pause(100);
		assertEquals("Result",false,y == player.getY());
		player.setY(400);
		player.setJump(true);
		landscape.jump(player, 10, 10, LevelInterface.PLAYER_MOVING);
		player.pause(100);
		assertEquals("Result",false,y == player.getY());
		player.setY(400);
		player.setJump(false);
		landscape.jump(player, 10, 10, LevelInterface.PLAYER_MOVING);
		player.pause(100);
		assertEquals("Result",false,y == player.getY());
		player.setY(400);
		player.setX(0);
		player.setJump(true);
		landscape.jump(player, 10, 10, LevelInterface.PLAYER_MOVING);
		player.pause(100);
		assertEquals("Result",false,y == player.getY());
		
		player.setX(600);
		player.setY(500);
		player.setJump(true);
		y = player.getY();
		landscape.jump();
		player.pause(100);
		assertEquals("Result", false, player.getY() == y);
	}
	
	@Test
	public void testIsMovableArea(){
		EnemyInterface e = landscape.getEnemies().get(0);
		BlockInterface c = landscape.getCrates().get(0);
		PlayerInterface p = landscape.getPlayer();
		
		assertEquals("Result",false,landscape.isMovableArea(0, 500, 100, 200,LevelInterface.ENEMY_MOVING, e));
		assertEquals("Result",true,landscape.isMovableArea(0, 0, 100, 200,LevelInterface.ENEMY_MOVING, e));
		assertEquals("Result",true,landscape.isMovableArea(8000, 0, 100, 200,LevelInterface.ENEMY_MOVING, e));
		assertEquals("Result",true,landscape.isMovableArea(100, 500, 100, 200,LevelInterface.ENEMY_MOVING, e));
		assertEquals("Result",true,landscape.isMovableArea(200, 400, 100, 200,LevelInterface.PLAYER_MOVING, p));
		assertEquals("Result",true,landscape.isMovableArea(0, 300, 100, 200,LevelInterface.PLAYER_MOVING, p));
		assertEquals("Result",true,landscape.isMovableArea(0, 8000, 100, 200,LevelInterface.PLAYER_MOVING, p));
		
		assertEquals("Result",false,landscape.isMovableArea(610, 500, 100, 200,LevelInterface.PLAYER_MOVING, p));
		assertEquals("Result",false,landscape.isMovableArea(610, 500, 100, 200,LevelInterface.CRATE_MOVING, c));
		
		assertEquals("Result",false,landscape.isMovableArea(1310, 0, 100, 200,LevelInterface.PLAYER_MOVING, p));
		assertEquals("Result",true,landscape.isMovableArea(1400, 500, 100, 200,LevelInterface.PLAYER_MOVING, p));
		assertEquals("Result",true,landscape.isMovableArea(1310, 0, 100, 200,LevelInterface.PLAYER_MOVING, p));
		
		landscape.isMovableArea(100, 500, 100, 200,LevelInterface.PLAYER_MOVING, p);
		assertEquals("Result",0,landscape.getPlayer().getHealth());
	}
		
	@Test
	public void testHandleCrateCollision(){
		BlockInterface crate = level.getCrates().get(0);
		EnemyInterface enemy = level.getEnemies().get(0);
		PlayerInterface player = landscape.getPlayer();
		player.setX(1000);
		assertEquals("Result",false,landscape.isMovableArea(610, 440, 100, 200,LevelInterface.PLAYER_MOVING, player));
		assertEquals("Result",false,landscape.isMovableArea(610, 500, 100, 200,LevelInterface.ENEMY_MOVING, enemy));
		
		assertEquals("Result",false,landscape.isMovableArea(610, 500, 100, 200,LevelInterface.PLAYER_MOVING, player));
		assertEquals("Result",true,landscape.isMovableArea(790, 500, 1, 200,LevelInterface.PLAYER_MOVING, player));
		crate.setX(600);
		assertEquals("Result",false,landscape.isMovableArea(690, 500, 100, 200,LevelInterface.PLAYER_MOVING, player));
		assertEquals("Result",true,landscape.isMovableArea(610, 500, 100, 200,LevelInterface.PLAYER_MOVING, player));
		enemy.setX(600);
		enemy.setY(500);
		landscape.isMovableArea(600, 500, 100, 100,LevelInterface.CRATE_MOVING, crate);
		assertEquals("Result",true,enemy.isDead());
	}
	
	@Test
	public void testIsCrateMovable(){
		BlockInterface crate = landscape.getCrates().get(0);
		EnemyInterface enemy = landscape.getEnemies().get(0);
		PlayerInterface player = landscape.getPlayer();
		
		crate.setX(0);
		crate.setY(400);
		assertEquals("Result",true,landscape.isMovableArea(90, 300, 100, 200, LevelInterface.PLAYER_MOVING, player));
		crate.setX(700);
		crate.setY(600);
		enemy.setX(8000);
		assertEquals("Result",true,landscape.isMovableArea(790, 500, 1, 200, LevelInterface.PLAYER_MOVING, player));
		crate.setX(700);
		enemy.setX(600);
		assertEquals("Result",false,landscape.isMovableArea(790, 500, 10, 200, LevelInterface.PLAYER_MOVING, player));
		enemy.kill();
		assertEquals("Result",true,landscape.isMovableArea(790, 500, 1, 200, LevelInterface.PLAYER_MOVING, player));
		assertEquals("Result",true,landscape.isMovableArea(600, 500, 100, 200, LevelInterface.PLAYER_MOVING, player));
		level.getCrates().get(0).setX(1000);
		assertEquals("Result",false,landscape.isMovableArea(1090, 500, 100, 200, LevelInterface.PLAYER_MOVING, player));
	
		crate.setX(1200);
		crate.setY(0);
		assertEquals("Result",false,landscape.isMovableArea(1110, 0, 100, 200, LevelInterface.PLAYER_MOVING, player));
		
		crate.setX(1300);
		crate.setY(600);
		landscape.isMovableArea(1310, 500, 100, 200, LevelInterface.PLAYER_MOVING, player);
		assertEquals("Result",true,level.getButtons().iterator().next().getValue().isPressed());
		
		BlockInterface crateTwo = landscape.getCrates().get(1);
		crateTwo.setX(1000);
		crateTwo.setY(600);
		assertEquals("Result",true,landscape.isMovableArea(1090, 500, 100, 200, LevelInterface.PLAYER_MOVING, player));
		
		crate.setX(1200);
		crate.setY(0);
		assertEquals("Result",true,landscape.isMovableArea(1110, 0, 100, 200, LevelInterface.PLAYER_MOVING, player));
	
		crate.setX(300);
		crate.setY(500);
		assertEquals("Result",true,landscape.isMovableArea(390, 400, 100, 200, LevelInterface.PLAYER_MOVING, player));
	
		crate.setX(1400);
		crate.setY(600);
		assertEquals("Result",false,landscape.isMovableArea(1400, 610, 100, 100, LevelInterface.CRATE_MOVING, crate));
	}
	
	@Test
	public void testLoadLevel(){
		assertEquals("Result",false,landscape.loadLevel(new File("pom.xml")));
		assertEquals("Result",true,landscape.loadLevel(map));
	}
	
	@Override
	public void update() {}
}