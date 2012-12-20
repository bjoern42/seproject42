package de.htwg.project42.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.Implementation.Enemy;

public class EnemyTest {

	@Test
	public void testEnemy() {
		
	}

	@Test
	public void testGetDirection() {
		EnemyInterface e = new Enemy(0, 0, 100);
		assertEquals("Result",1,e.getDirection());
	}

	@Test
	public void testChangeDirection() {
		EnemyInterface e = new Enemy(0, 0, 100);
		e.changeDirection();
		assertEquals("Result",-1,e.getDirection());
	}

	@Test
	public void testKill() {
		EnemyInterface e = new Enemy(0, 0, 100);
		e.kill();
		assertEquals("Result",true,e.isDead());
	}

	@Test
	public void testIsDead() {
		EnemyInterface e = new Enemy(0, 0, 100);
		assertEquals("Result",false,e.isDead());
		e.kill();
		assertEquals("Result",true,e.isDead());
	}
}
