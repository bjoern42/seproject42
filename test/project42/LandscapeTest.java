package project42;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LandscapeTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testLandscape() {
		Landscape landscape = new Landscape(null,10,20);
		assertEquals("Result",10,landscape.width);
		assertEquals("Result",20,landscape.height);
	}
	
	@Test
	public void testLeft(){
		Observer observer = new Observer(100, 12, 8);
		Landscape landscape = new Landscape(observer,10,20);
		Block block = observer.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		landscape.left();
		assertEquals("Result",x+10,block.getX());
	}
	
	@Test
	public void testRight(){
		Observer observer = new Observer(100, 12, 8);
		Landscape landscape = new Landscape(observer,10,20);
		Block block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		landscape.right();
		assertEquals("Result",x-10,block.getX());
	}
}