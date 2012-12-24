package de.htwg.project42.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.project42.model.GameObjects.GateInterface;
import de.htwg.project42.model.GameObjects.Implementation.Gate;

public class GateTest {

	@Test
	public void testIsOn() {
		GateInterface gate = new Gate(0, 0, 100, 100);
		gate.off();
		assertEquals("Result",false,gate.isOn());
		gate.on();
		assertEquals("Result",true,gate.isOn());
	}
}
