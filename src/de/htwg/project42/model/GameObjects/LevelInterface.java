package de.htwg.project42.model.GameObjects;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public interface LevelInterface {
	int PLAYER_MOVING = 0;
	int ENEMY_MOVING = 1;
	int CRATE_MOVING = 2;
	
	
	List<BlockInterface[]> getVisibleBlocks();
	List<BlockInterface[]> getBlocks();
	void setBlocks(List<BlockInterface[]> objects);
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
	int getChange();
	int getBlockSize();
	List<BlockInterface> getCrates();
	void loadData(File map);
	void releaseButtons();
	Set<Entry<Integer, ButtonInterface>> getButtons();
}
