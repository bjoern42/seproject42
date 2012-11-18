package project42;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

	@Test
	public void testPlayer() {
		Player player = new Player(0, 0, 10, 20);
		assertEquals("Result",0,player.getX());
		assertEquals("Result",0,player.getY());
		assertEquals("Result",10,player.getWidth());
		assertEquals("Result",20,player.getHeight());
	}

	@Test
	public void testMove() {
		Player player = new Player(0, 0, 10, 20);
		player.move(10,10);
		assertEquals("Result",10,player.getX());
		assertEquals("Result",10,player.getY());
	}

	@Test
	public void testHit(){
		Player player = new Player(0, 0, 10, 20);
		int health = player.getHealth();
		player.hit();
		assertEquals("Result",health-1,player.getHealth());
		player.hit();
		assertEquals("Result",true,player.getLock());
		player.setHealth(0);
		Landscape.pause(1100);
		player.hit();
		assertEquals("Result",0,player.getHealth());
	}
	
	@Test
	public void testGetLock(){
		Player player = new Player(0, 0, 10, 20);
		player.hit();
		assertEquals("Result",true,player.getLock());
	}
	
	@Test
	public void testSetHealth(){
		Player player = new Player(0, 0, 10, 20);
		player.setHealth(42);
		assertEquals("Result",42,player.getHealth());
	}
	
	@Test
	public void testGetCoins(){
		Player player = new Player(0, 0, 10, 20);
		assertEquals("Result",0,player.getCoins());
	}
	
	@Test
	public void testIncreaseCoins(){
		Player player = new Player(0, 0, 10, 20);
		player.increaseCoins();
		assertEquals("Result",1,player.getCoins());
		player.coins = player.coinsForLife;
		player.increaseCoins();
		assertEquals("Result",0,player.getCoins());
	}
}