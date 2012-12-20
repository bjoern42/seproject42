package de.htwg.project42.model.GameObjects;

public interface PlayerInterface extends GameObjectsInterface{
	int getHealth();
	void setHealth(int i);
	void setGoal(boolean b);
	boolean isInGoal();
	boolean getLock();
	void setLock(boolean b);
	void hit();
	void increaseCoins();
	int getCoins();
	int getCoinsForLife();
	void setCoins(int coinsForLife);
}
