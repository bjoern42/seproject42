package de.htwg.project42.controller;

import java.util.List;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.observer.Observable;;

public interface LandscapeInterface {
	PlayerInterface getPlayer();
	void start();
	void jump();
	void right();
	void left();
	List<BlockInterface[]> getVisibleBlocks();
	List<EnemyInterface> getEnemies();
	void setEnemies(List<EnemyInterface> enemies);
	void setCrates(List<BlockInterface> crates);
	void addAnObserver(Observable observer);
}
