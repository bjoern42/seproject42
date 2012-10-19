package project42;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObserverTest {

	@Test
	public void testAdd() {
		Observer observer = new Observer();
		observer.add(new GameObject[1]);
		assertEquals("Result",1,observer.getSize());
	}

	@Test
	public void testRemoveFirst() {
		Observer observer = new Observer();
		observer.add(new GameObject[1]);
		observer.removeFirst();
		assertEquals("Result",0,observer.getSize());
	}

	@Test
	public void testGetSize() {
		Observer observer = new Observer();
		observer.add(new GameObject[1]);
		assertEquals("Result",1,observer.getSize());
	}
	
	@Test
	public void testUpdate() {
		
	}

}
