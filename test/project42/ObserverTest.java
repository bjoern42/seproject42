package project42;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ObserverTest {
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("mapTUI.lvl");
	}
	
	@Test
	public void testUpdate(){
		Observer observer = new Observer(null,map, 100, 12);
		Block block = observer.getVisibleBlocks().get(0)[0];
		int oldX = block.getX();
		observer.update(10);
		assertEquals("Result",oldX+10,block.getX());
		observer.update(-10);
		assertEquals("Result",oldX,block.getX());
		observer.update(-110);
		assertEquals("Result",true,block != observer.getVisibleBlocks().get(0)[0]);
		observer.update(+220);
		assertEquals("Result",true,block == observer.getVisibleBlocks().get(0)[0]);
	}

	
	@Test
	public void testIsMovableArea(){
		Observer observer = new Observer(null,map, 100, 12);
		assertEquals("Result",false,observer.isMovableArea(0, 500, 100, 200,false));
	}
}