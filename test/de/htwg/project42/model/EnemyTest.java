package de.htwg.project42.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.project42.model.GameObjects.Enemy;

public class EnemyTest {

	@Test
	public void testEnemy() {
		
	}

	@Test
	public void testGetDirection() {
		Enemy e = new Enemy(0, 0, 100);
		assertEquals("Result",1,e.getDirection());
	}

	@Test
	public void testChangeDirection() {
		Enemy e = new Enemy(0, 0, 100);
		e.changeDirection();
		assertEquals("Result",-1,e.getDirection());
	}

	@Test
	public void testKill() {
		Enemy e = new Enemy(0, 0, 100);
		e.kill();
		assertEquals("Result",true,e.isDead());
	}

	@Test
	public void testIsDead() {
		Enemy e = new Enemy(0, 0, 100);
		assertEquals("Result",false,e.isDead());
		e.kill();
		assertEquals("Result",true,e.isDead());
	}
}
