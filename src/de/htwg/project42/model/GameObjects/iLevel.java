package de.htwg.project42.model.GameObjects;

import java.util.List;

public interface iLevel {
	final static int SPEED = 10;
	List<iBlock[]> getVisibleBlocks();
	void setBlocks(List<iBlock[]> objects);
	boolean isMovableArea(int x, int i, int width, int height, boolean b);
	boolean isInFrame(int x);
	void update(int speed2);
	List<iEnemy> getEnemies();
	void addEnemy(iEnemy enemy);
	void removeFirst();
	int getStart();
	void setStart(int i);
	void removeLast();
	int getLength();
}
