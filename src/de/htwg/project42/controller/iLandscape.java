package de.htwg.project42.controller;

import java.util.List;

import de.htwg.project42.model.GameObjects.iBlock;
import de.htwg.project42.model.GameObjects.iEnemy;
import de.htwg.project42.model.GameObjects.iPlayer;
import de.htwg.project42.observer.Observable;;

public interface iLandscape {
	iPlayer getPlayer();
	void start();
	void jump();
	void right();
	void left();
	List<iBlock[]> getVisibleBlocks();
	List<iEnemy> getEnemies();
	void addAnObserver(Observable observer);
}
