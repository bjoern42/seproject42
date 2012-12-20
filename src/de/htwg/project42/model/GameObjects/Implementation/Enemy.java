package de.htwg.project42.model.GameObjects.Implementation;

import de.htwg.project42.model.GameObjects.EnemyInterface;

/**
 * Enemy for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Enemy extends Block implements EnemyInterface{
private int direction = 1;
private boolean dead = false;

	/**
	 * Creates an enemy.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pSize - Enemysize
	 */
	public Enemy(int pX, int pY, int pSize) {
		super(pX, pY, pSize, Block.TYP_AIR);
	}
	
	/**
	 * Returns enemy walking direction.
	 * @return direction.
	 */
	public int getDirection(){
		return direction;
	}
	
	/**
	 * Changes enemy walking direction.
	 */
	public void changeDirection(){
		direction *= -1;
	}
	
	/**
	 * Kills enemy.
	 */
	public void kill(){
		dead = true;
	}
	
	/**
	 * Returns if the enemy is alive or not.
	 * @return true if enemy is dead. Otherwise it returns false.
	 */
	public boolean isDead(){
		return dead;
	}
}
