package de.htwg.project42.model.GameObjects;

import java.util.List;

public interface LevelInterface {
	static int PLAYER_MOVING = 0;
	static int ENEMY_MOVING = 1;
	static int CRATE_MOVING = 2;
	static int SPEED = 10;
	
	List<BlockInterface[]> getVisibleBlocks();
	void setBlocks(List<BlockInterface[]> objects);
	boolean isMovableArea(int x, int i, int width, int height, int pMoving);
	boolean isInFrame(int x);
	void update(int speed2);
	List<EnemyInterface> getEnemies();
	void addEnemy(EnemyInterface enemy);
	void addCrate(BlockInterface crate);
	void removeFirst();
	int getStart();
	void setStart(int i);
	void removeLast();
	int getLength();
	List<BlockInterface> getCrates();
}
