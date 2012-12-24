package de.htwg.project42.model;

import static org.junit.Assert.*;

import java.io.File;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.ButtonInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.Implementation.Block;
import de.htwg.project42.model.GameObjects.Implementation.Level;

public class LevelTest{
File map = null;

	@Before
	public void setUp() throws Exception {
		map = new File("testmap.lvl");
	}
	
	@Test
	public void testUpdate(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		BlockInterface block = level.getVisibleBlocks().get(0)[0];
		int oldX = block.getX();
		level.update(10);
		assertEquals("Result",oldX+10,block.getX());
		level.update(-10);
		assertEquals("Result",oldX,block.getX());
		level.update(-110);
		assertEquals("Result",true,block != level.getVisibleBlocks().get(0)[0]);
		level.update(+220);
		assertEquals("Result",true,block == level.getVisibleBlocks().get(0)[0]);
	}
	
	@Test
	public void testRemoveFirst(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		level.removeFirst();
		assertEquals("Result",1,level.getStart());
		level.setStart(8000);
		level.removeFirst();
		assertEquals("Result",8000,level.getStart());
	}
	
	@Test
	public void testRemoveLast(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		level.removeLast();
		assertEquals("Result",0,level.getStart());
		level.setStart(10);
		level.removeLast();
		assertEquals("Result",9,level.getStart());
	}
	
	@Test
	public void testIsInFrame(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		assertEquals("Result",false,level.isInFrame(1300));
		assertEquals("Result",true,level.isInFrame(800));
		assertEquals("Result",false,level.isInFrame(-200));
	}
	
	@Test
	public void testGetVisibleBlocks(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		assertEquals("Result",level.getLength(),level.getVisibleBlocks().size());
		level.loadData(new File("mapTUI.lvl"));
		assertEquals("Result",5,level.getVisibleBlocks().size());
	}
	
	@Test
	public void testGetEnemies(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		assertEquals("Result",level.getEnemies(),level.getEnemies());
	}
	
	@Test
	public void testSetBlocks(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		LinkedList<BlockInterface[]> list = new LinkedList<BlockInterface[]>();
		BlockInterface tmp[] = new Block[1];
		tmp[0] = new Block(0, 0, 100, 42);
		list.add(tmp);
		level.setBlocks(list);
		assertEquals("Result",42,level.getVisibleBlocks().get(0)[0].getType());
	}
	
	@Test
	public void testGetBlockSize(){
		LevelInterface level = new Level(100, 12);
		assertEquals("Result",100,level.getBlockSize());
	}
	
	@Test
	public void testGetChange(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		assertEquals("Result",0,level.getChange());
		level.update(10);
		assertEquals("Result",-10,level.getChange());
	}
	
	@Test
	public void testGetBlocks(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		assertEquals("Result",18,level.getBlocks().size());
	}
	
	@Test
	public void testGetCrates(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		assertEquals("Result",2,level.getCrates().size());
	}
	
	@Test
	public void testReleaseButtons(){
		LevelInterface level = new Level(100, 12);
		level.loadData(map);
		ButtonInterface button = level.getButtons().iterator().next().getValue();
		button.press(level.getEnemies().get(0));
		assertEquals("Result",true,button.isPressed());
		level.releaseButtons();
		assertEquals("Result",false,button.isPressed());
	}
}