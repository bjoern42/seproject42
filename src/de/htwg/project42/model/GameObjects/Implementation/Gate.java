package de.htwg.project42.model.GameObjects.Implementation;

import de.htwg.project42.model.GameObjects.GateInterface;

public class Gate extends Block implements GateInterface{
private boolean state = false;

	public Gate(int pX, int pY, int pSize, int pType) {
		super(pX, pY, pSize, pType);
	}

	@Override
	public void on() {
		state = true;
	}

	@Override
	public void off() {
		state = false;
	}

	@Override
	public boolean isOn() {
		return state;
	}
}
