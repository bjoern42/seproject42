package de.htwg.project42.model.GameObjects.Implementation;


import java.util.LinkedList;

import de.htwg.project42.model.GameObjects.ButtonInterface;
import de.htwg.project42.model.GameObjects.GameObjectsInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.Features.Switchable;

public class Button extends Block implements ButtonInterface, Runnable {
private LinkedList<Switchable> switchables = new LinkedList<Switchable>();
private GameObjectsInterface object = null;
private LevelInterface level = null;
private boolean pressed = false;

	public Button(LevelInterface pLevel, int pX, int pY, int pSize, int pType) {
		super(pX, pY, pSize, pType);
		level = pLevel;
	}

	@Override
	public void press(GameObjectsInterface pObject) {
		if(!pressed){
			pressed = true;
			for(Switchable s:switchables){
				s.on();
			}
			object = pObject;
			new Thread(this).start();
		}
	}
	
	@Override
	public void release() {
		pressed = false;
		for(Switchable s:switchables){
			s.off();
		}
	}
	
	@Override
	public boolean isPressed() {
		return pressed;
	}

	@Override
	public void run() {
		while(pressed && (isInArea(object.getX(), object.getY(), object.getWidth(), object.getHeight()) || !level.isInFrame(getX()))){
			pause(RELEASE_TIME);
		}
		release();
	}

	@Override
	public void registerSwitchable(Switchable s) {
		switchables.add(s);
	}
}
