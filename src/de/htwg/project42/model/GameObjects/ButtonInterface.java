package de.htwg.project42.model.GameObjects;

import de.htwg.project42.model.GameObjects.Features.Switchable;

public interface ButtonInterface extends BlockInterface{
	int RELEASE_TIME = 100;
	void press(GameObjectsInterface object);
	void release();
	boolean isPressed();
	void registerSwitchable(Switchable s);
}
