package de.htwg.project42.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.Implementation.Block;

public class BlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBlock() {
		BlockInterface tester = new Block(0,0,10,1);
		
		assertEquals("Result",1,tester.getType());
		assertEquals("Result",10,tester.getWidth());
		assertEquals("Result",10,tester.getHeight());
		assertEquals("Result",0,tester.getX());
		assertEquals("Result",0,tester.getY());
	}
	
	@Test
	public void testToString() {
		BlockInterface tester = new Block(0,0,10,1);
		assertEquals("Result","[1]",tester.toString());
	}

	@Test
	public void testGetType(){
		BlockInterface tester = new Block(0,0,10,1);
		assertEquals("Result",1,tester.getType());
	}
	
	@Test
	public void testSetType(){
		BlockInterface tester = new Block(0,0,10,1);
		tester.setType(2);
		assertEquals("Result",2,tester.getType());
	}
	
	@Test
	public void testSetX(){
		BlockInterface tester = new Block(0,0,10,1);
		tester.setX(20);
		assertEquals("Result",20,tester.getX());
	}
	
	@Test
	public void testUpdate(){
		BlockInterface tester = new Block(0,0,10,1);
		tester.update(10);
		assertEquals("Result",10,tester.getX());
	}
}
