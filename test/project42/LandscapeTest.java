package project42;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LandscapeTest {
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("mapTUI.lvl");
	}

	@Test
	public void testLandscape() {
		Observer observer = new Observer(map,100, 12);
		Landscape landscape = new Landscape(null,map,observer,1200,800,12);
		assertEquals("Result",10,landscape.width);
		assertEquals("Result",20,landscape.height);
		landscape.jump();
	}
	
	@Test
	public void testLeft(){
		Observer observer = new Observer(map,100, 12);
		Landscape landscape = new Landscape(null,map,observer,1200,800,12);
		Block block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		landscape.left();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result",x+10,block.getX());
	}
	
	@Test
	public void testRight(){
		Observer observer = new Observer(map,100, 12);
		Landscape landscape = new Landscape(null,map,observer,1200,800,12);
		Block block = landscape.getVisibleBlocks().get(0)[0];
		int x = block.getX();
		landscape.right();
		block = landscape.getVisibleBlocks().get(0)[0];
		assertEquals("Result",x-10,block.getX());
	}
	
	@Test
	public void testJump(){
		Observer observer = new Observer(map,100, 12);
		Landscape landscape = new Landscape(null,map,observer,1200,800,12);
		Player player = landscape.getPlayer();
		int y = player.getY();
		landscape.jump();
		assertEquals("Result",false,landscape.jump);
		assertEquals("Result",true,y != player.getY());
		
	}
}