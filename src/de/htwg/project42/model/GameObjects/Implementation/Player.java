package de.htwg.project42.model.GameObjects.Implementation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.htwg.project42.model.GameObjects.PlayerInterface;


/**
 * Player for JumpNRun
 * @author bjeschle,toofterd
 * @version 1.0
 */
@Singleton
public final class Player extends GameObject implements PlayerInterface{
private int health = STD_HEALTH, coins = STD_COINS, startX, startY;
private boolean lock = false, goal = false;

	/**
	 * Creates Player.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pWidth - Width
	 * @param pHeight - Height
	 */
	@Inject
	public Player(@Named("playerX") int pX, @Named("playerY") int pY, @Named("playerWidth") int pWidth, @Named("playerHeight") int pHeight) {
		super(pX, pY, pWidth, pHeight);
		startX = pX;
		startY = pY;
	}
	
	/**
	 * Decreases player health if the player isn't invincible.
	 * Makes player invincible for 1 second.
	 */
	public void hit(){
		if(!lock){
			lock = true;
			if(health > 0){
				health--;
			}
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

	@Override
	public void reset() {
		setX(startX);
		setY(startY);
		setHealth(STD_HEALTH);
		setGoal(false);
		setCoins(0);
	}
}
