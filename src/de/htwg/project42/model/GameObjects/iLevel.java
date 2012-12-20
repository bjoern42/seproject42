package de.htwg.project42.model.GameObjects;

import java.util.List;

public interface iLevel {
	final static int SPEED = 10;
	public static final int PLAYER_MOVING = 0;
	public static final int ENEMY_MOVING = 1;
	public static final int CRATE_MOVING = 2;
	List<iBlock[]> getVisibleBlocks();
	void setBlocks(List<iBlock[]> objects);
	boolean isMovableArea(int x, int i, int width, int height, int pMoving);
	boolean isInFrame(int x);
	void update(int speed2);
	List<iEnemy> getEnemies();
	void addEnemy(iEnemy enemy);
	void addCrate(iBlock crate);
	void removeFirst();
	int getStart();
	void setStart(int i);
	void removeLast();
	int getLength();
	List<iBlock> getCrates();
}
