package de.htwg.project42.model.GameObjects;

import de.htwg.project42.model.GameObjects.Features.Movable;

public interface GameObjectsInterface extends Movable{
	int PAUSE = 20;
	int getX();
	void setX(int pX);
	int getY();
	void setY(int pY);
	int getWidth();
	int getHeight();
	void move(int i, int j);
	boolean isInArea(int x, int y, int width, int height);
	boolean getJump();
	void setJump(boolean b);
	void pause(int pause);
}
