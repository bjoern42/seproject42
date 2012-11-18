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
		assertEquals("Result",true,observer.isMovableArea(0, 0, 100, 200,false));
		assertEquals("Result",true,observer.isMovableArea(8000, 0, 100, 200,false));
		assertEquals("Result",true,observer.isMovableArea(100, 500, 100, 200,false));
		assertEquals("Result",true,observer.isMovableArea(200, 400, 100, 200,true));
	}
	
	@Test
	public void testJump(){
		Observer observer = new Observer(null,map,100, 12);
		Landscape landscape = new Landscape(map,observer,1200,800,12);
		Player player = landscape.getPlayer();
		int y = 500;
		player.y = y;
		player.jump = true;
		landscape.jump();
		assertEquals("Result",false,player.getJump());
		Landscape.pause(100);
		assertEquals("Result",false,y == player.getY());
	}
	
	@Test
	public void testRemoveFirst(){
		Observer observer = new Observer(null,map,100, 12);
		observer.removeFirst();
		assertEquals("Result",1,observer.start);
		observer.start = 8000;
		observer.removeFirst();
		assertEquals("Result",8000,observer.start);
	}
	
	@Test
	public void testRemoveLast(){
		Observer observer = new Observer(null,map,100, 12);
		observer.removeLast();
		assertEquals("Result",0,observer.start);
		observer.start = 10;
		observer.removeLast();
		assertEquals("Result",9,observer.start);
	}
	
	@Test
	public void testIsInFrame(){
		Observer observer = new Observer(null,map,100, 12);
		assertEquals("Result",false,observer.isInFrame(1300));
		assertEquals("Result",true,observer.isInFrame(800));
		assertEquals("Result",false,observer.isInFrame(-200));
	}
	
	@Test
	public void testGetVisibleBlocks(){
		Observer observer = new Observer(null,map,100, 12);
		assertEquals("Result",observer.length,observer.getVisibleBlocks().size());
		observer = new Observer(null,new File("mapTUI.lvl"),100, 12);
		assertEquals("Result",5,observer.getVisibleBlocks().size());
	}
	
	@Test
	public void testGetEnemies(){
		Observer observer = new Observer(null,map,100, 12);
		assertEquals("Result",observer.enemies,observer.getEnemies());
	}
}