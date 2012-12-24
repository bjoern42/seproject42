package de.htwg.project42.model.GameObjects;

public interface PlayerInterface extends GameObjectsInterface{
	int COINS_FOR_LIFE = 50;
	int STD_HEALTH = 3;
	int STD_COINS = 0;
	int INVINCIBLE_LOCK_DURATION = 1000;
	
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
	void reset();
}
