package de.htwg.project42.model;

import de.htwg.project42.controller.Landscape;

/**
 * Player for JumpNRun
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Player extends GameObject{
private static final int coinsForLife = 50;
private int health = 3,coins = 0;
private boolean lock = false;

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
					Landscape.pause(1000);
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
		return coinsForLife;
	}
}
