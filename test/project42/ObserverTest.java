package project42;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ObserverTest {
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("map.lvl");
	}
	
	@Test
	public void testUpdate(){
		Player player = new Player(0, 0, 10, 20);
		Observer observer = new Observer(player,map, 100, 12);
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
		Player player = new Player(0, 0, 10, 20);
		Observer observer = new Observer(player,map, 100, 12);
		assertEquals("Result",false,observer.isMovableArea(0, 500, 100, 200,false));
	}
	
	@Test
	public void testJump(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.jump();
		
		assertEquals("Result",true,player.getJump());
		assertEquals("Result",true,y != player.getY());
	}
	
	@Test
	public void testRemoveFirst(){
		
	}
	
	@Test
	public void testRemoveLast(){
		
	}
	
	@Test
	public void testIsInFrame(){
		Observer observer = new Observer(null,map,100, 12);
		assertEquals("Result",false,observer.isInFrame(1300));
		assertEquals("Result",true,observer.isInFrame(800));
	}
	
	@Test
	public void testGetVisibleBlocks(){
		Observer observer = new Observer(null,map,100, 12);
		assertEquals("Result",observer.length,observer.getVisibleBlocks().size());
	}
	
	@Test
	public void testGetEnemies(){
		Observer observer = new Observer(null,map,100, 12);
		assertEquals("Result",observer.enemies,observer.getEnemies());
	}
}