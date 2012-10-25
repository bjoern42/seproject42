package project42;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBlock() {
		Block tester = new Block(0,0,10,1);
		
		assertEquals("Result",1,tester.type);
		assertEquals("Result",10,tester.getWidth());
		assertEquals("Result",10,tester.getHeight());
		assertEquals("Result",0,tester.getX());
		assertEquals("Result",0,tester.getY());
	}
	
	@Test
	public void testIsInArea(){
		Block tester = new Block(10,20,10,1);
		Player player = new Player(0, 0, 10, 20);
		assertEquals("Result",false,tester.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		player = new Player(11, 0, 10, 20);
		assertEquals("Result",false,tester.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		player = new Player(21, 0, 10, 20);
		assertEquals("Result",false,tester.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		player = new Player(11, 31, 10, 20);
		assertEquals("Result",false,tester.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
		
		player = new Player(11, 21, 10, 20);
		assertEquals("Result",true,tester.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight()));
	}
	
	@Test
	public void testGetX(){
		Block tester = new Block(0,0,10,1);
		assertEquals("Result",0,tester.getX());
	}
	
	@Test
	public void testGetY(){
		Block tester = new Block(0,0,10,1);
		assertEquals("Result",0,tester.getY());
	}
	
	@Test
	public void testGetWidth(){
		Block tester = new Block(0,0,10,1);
		assertEquals("Result",10,tester.getWidth());
	}

	@Test
	public void testGetHeight(){
		Block tester = new Block(0,0,10,1);
		assertEquals("Result",10,tester.getHeight());
	}
	
	@Test
	public void testToString() {
		Block tester = new Block(0,0,10,1);
		assertEquals("Result","[1]",tester.toString());
	}

	@Test
	public void testGetType(){
		Block tester = new Block(0,0,10,1);
		assertEquals("Result",1,tester.getType());
	}
	
	@Test
	public void testSetX(){
		Block tester = new Block(0,0,10,1);
		tester.setX(20);
		assertEquals("Result",20,tester.getX());
	}
	
	@Test
	public void testUpdate(){
		Block tester = new Block(0,0,10,1);
		tester.update(10);
		assertEquals("Result",10,tester.getX());
	}
}
