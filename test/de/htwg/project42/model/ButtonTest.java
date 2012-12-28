package de.htwg.project42.model;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwg.project42.model.GameObjects.ButtonInterface;
import de.htwg.project42.model.GameObjects.GateInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.model.GameObjects.Implementation.Button;
import de.htwg.project42.model.GameObjects.Implementation.Gate;
import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;
import de.htwg.project42.model.GameObjects.Implementation.Player;

public class ButtonTest {

	@Test
	public void testPress() {
		LevelInterface level = new Level(new LevelLoader(),100,3);
		PlayerInterface player = new Player(0, 0, 100, 200);
		GateInterface gate = new Gate(0, 0, 100, 100);
		ButtonInterface button = new Button(level, 0, 0, 100, 100);
		button.registerSwitchable(gate);
		button.press(player);
		button.release();
		assertEquals("Result",false,button.isPressed());
		button.press(player);
		player.pause(100);
		assertEquals("Result",true,button.isPressed());
		player.setX(100);
		button.press(player);
		player.pause(100);
		assertEquals("Result",false,button.isPressed());
		button.setX(1000);
		button.press(player);		
		assertEquals("Result",true,button.isPressed());
	}
}
