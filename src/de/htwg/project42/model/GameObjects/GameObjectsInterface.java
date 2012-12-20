package de.htwg.project42.model.GameObjects;

import de.htwg.project42.model.GameObjects.Movable.Movable;
import de.htwg.project42.observer.ObserverInterface;

public interface GameObjectsInterface extends Movable{
	int getX();
	void setX(int pX);
	int getY();
	void setY(int pY);
	int getWidth();
	int getHeight();
	void move(int i, int j);
	boolean isInArea(int x, int y, int width, int height);
	void jump(final LevelInterface level, final ObserverInterface observer, final int gravity, final int height,final int moving);
	boolean getJump();
	void setJump(boolean b);
	void pause(int pause);
}
