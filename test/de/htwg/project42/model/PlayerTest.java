package de.htwg.project42.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.model.GameObjects.Implementation.Player;

public class PlayerTest {

	@Test
	public void testPlayer() {
		PlayerInterface player = new Player(0, 0, 10, 20);
		assertEquals("Result",0,player.getX());
		assertEquals("Result",0,player.getY());
		assertEquals("Result",10,player.getWidth());
		assertEquals("Result",20,player.getHeight());
	}

	@Test
	public void testMove() {
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.move(10,10);
		assertEquals("Result",10,player.getX());
		assertEquals("Result",10,player.getY());
	}

	@Test
	public void testHit(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		int health = player.getHealth();
		player.hit();
		assertEquals("Result",health-1,player.getHealth());
		player.hit();
		assertEquals("Result",true,player.getLock());
		player.setHealth(0);
		player.pause(1100);
		player.hit();
		assertEquals("Result",0,player.getHealth());
	}
	
	@Test
	public void testGetLock(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.hit();
		assertEquals("Result",true,player.getLock());
	}
	
	@Test
	public void testSetLock(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.setLock(true);
		assertEquals("Result",true,player.getLock());
	}
	
	@Test
	public void testSetHealth(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.setHealth(42);
		assertEquals("Result",42,player.getHealth());
	}
	
	@Test
	public void testGetCoins(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		assertEquals("Result",0,player.getCoins());
	}
	
	@Test
	public void testIncreaseCoins(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.increaseCoins();
		assertEquals("Result",1,player.getCoins());
		player.setCoins(player.getCoinsForLife());
		player.increaseCoins();
		assertEquals("Result",0,player.getCoins());
	}
	
	@Test
	public void testSetGoal(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.setGoal(true);
		assertEquals("Result",true,player.isInGoal());
	}
	
	@Test
	public void testUpdate(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.update(42);
		assertEquals("Result",0,player.getX());
	}
	
	@Test
	public void testReset(){
		PlayerInterface player = new Player(0, 0, 10, 20);
		player.setX(100);
		player.setY(100);
		player.setHealth(42);
		player.setCoins(42);
		player.reset();
		assertEquals("Result",0,player.getX());
		assertEquals("Result",0,player.getY());
		assertEquals("Result",3,player.getHealth());
		assertEquals("Result",0,player.getCoins());
	}
}
