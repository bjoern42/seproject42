package de.htwg.project42.controller;

import java.io.File;
import java.util.List;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.observer.Observable;;

public interface LandscapeInterface {
	int SPEED = 10;
	int GRAVITY = 10;
	int JUMP_HEIGHT = 16;
	int RUN_PAUSE = 20;
	int ENEMY_SPEED_FACTOR = 4;
	double STANDARD_ENEMY_JUMP_CHANCES = 0.995;
	double QUARTER = 0.25;
	double THREE_QUARTERS = 0.75;
	
	PlayerInterface getPlayer();
	void start();
	void jump();
	void right();
	void left();
	List<BlockInterface[]> getVisibleBlocks();
	List<EnemyInterface> getEnemies();
	List<BlockInterface> getCrates();
	void addAnObserver(Observable observer);
	void loadLevel(File level);
}
