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

}
