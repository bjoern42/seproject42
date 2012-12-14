package de.htwg.project42.model.GameObjects.Implementation;

import de.htwg.project42.model.GameObjects.iPlayer;


/**
 * Player for JumpNRun
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Player extends GameObject implements iPlayer{
private static final int COINS_FOR_LIFE = 50, STD_HEALTH = 3, STD_COINS = 0, INVINCIBLE_LOCK_DURATION = 1000;
private int health = STD_HEALTH, coins = STD_COINS;
private boolean lock = false, goal = false;

	/**
	 * Creates Player.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pWidth - Width
	 * @param pHeight - Height
	 */
	public Player(int pX, int pY, int pWidth, int pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
	
	/**
	 * Decreases player health if the player isn't invincible.
	 * Makes player invincible for 1 second.
	 */
	public void hit(){
		if(!lock){
			if(health > 0){
				health--;
				lock = true;
			}
		}else{
			new Thread(){
				public void run(){
					pause(INVINCIBLE_LOCK_DURATION);
					lock = false;
				}
			}.start();
		}
	}
	
	/**
	 * Checks if player is invincible.
	 * @return true if player is invincible, false if not
	 */
	public boolean getLock(){
		return lock;
	}
	
	/**
	 * Sets if player is invincible or not.
	 * @param pLock - lock
	 */
	public void setLock(boolean pLock){
		lock = pLock;
	}
	
	/**
	 * Returns player health.
	 * @return health
	 */
	public int getHealth(){
		return health;
	}
	
	/**
	 * Sets player health.
	 * @param pHealth - health
	 */
	public void setHealth(int pHealth){
		health = pHealth;
	}
	
	/**
	 * Returns amount of collected coins.
	 * @return coins
	 */
	public int getCoins(){
		return coins;
	}
	
	/**
	 * Increases amount of collected coins.
	 */
	public void increaseCoins(){
		setCoins(getCoins() + 1);
		if(getCoins() > getCoinsForLife()){
			setCoins(0);
			health++;
		}
	}

	/**
	 * Sets the amount of collected coins.
	 * @param pCoins - collected coins
	 */
	public void setCoins(int pCoins) {
		coins = pCoins;
	}

	/**
	 * Returns how many coins are needed to get a life.
	 * @return coinsForLife
	 */
	public int getCoinsForLife() {
		return COINS_FOR_LIFE;
	}

	/**
	 * Checks if player has reached the goal.
	 * @return true if player is in goal, false if not
	 */
	public boolean isInGoal() {
		return goal;
	}

	/**
	 * Sets goal.
	 * @param pGoal - goal
	 */
	public void setGoal(boolean pGoal) {
		goal = pGoal;
	}

	@Override
	public void update(int pChange) {}
}
