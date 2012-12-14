package de.htwg.project42.controller.Implementation;

import java.util.List;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;


import de.htwg.project42.controller.iLandscape;
import de.htwg.project42.model.GameObjects.iBlock;
import de.htwg.project42.model.GameObjects.iEnemy;
import de.htwg.project42.model.GameObjects.iLevel;
import de.htwg.project42.model.GameObjects.iPlayer;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.observer.Observer;

/**
 * Controller class for JumpNRun game.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Landscape extends Observer implements iLandscape{
private static final int GRAVITY = 10, JUMP_HEIGHT = 16, RUN_PAUSE = 20, ENEMY_SPEED_FACTOR = 4;
private static final double STANDARD_ENEMY_JUMP_CHANCES = 0.995;
private double enemyJumpChances = STANDARD_ENEMY_JUMP_CHANCES;
private int width, height;
private iPlayer player = null;
private List<iEnemy> enemies = null;
private iLevel level = null;
private Mutex mutex = new Mutex();
	
	/**
	 * Generates Landscape.
	 * @param pObservable - Observer
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @param pLength - Visible columns
	 */
	public Landscape(iPlayer pPlayer, iLevel pLevel, int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		player = pPlayer;
		level = pLevel;
		enemies = level.getEnemies();
	}
	
	/**
	 * Adds an Observer
	 */
	public void addAnObserver(Observable o){
		addObserver(o);
	}
	
	/**
	 * Returns the Level
	 * @return level
	 */
	public iLevel getLevel(){
		return level;
	}
		
	/**
	 * Returns the player.
	 * @return Player
	 */
	public iPlayer getPlayer(){
		return player;
	}
	
	/**
	 * Returns a list of all enemies.
	 * @return Enemies
	 */
	public List<iEnemy> getEnemies(){
		return enemies;
	}
	
	/**
	 * Returns a list of all visible blocks.
	 * @return visible blocks
	 */
	public List<iBlock[]> getVisibleBlocks(){
		return level.getVisibleBlocks();
	}
	
	/**
	 * Starts the game.
	 */
	public void start(){
		new Thread(){
			public void run(){
				while(player.getHealth() > 0 && !player.isInGoal()){
					player.pause(RUN_PAUSE);
					gravity();
					handleEnemies();
					notifyObserver();
				}
				removeAllObserver();
			}
		}.start();
	}
	
	/**
	 * Handles the gravity.
	 */
	public void gravity(){
		try {
			mutex.acquire();
			if(player.getJump() && level.isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight(),true)){
				player.move(0, GRAVITY*2);
				if(player.getY()>height){
					player.setHealth(0);
				}
			}
			for(iEnemy e:enemies){
				if(level.isInFrame(e.getX()) && e.getJump() && level.isMovableArea(e.getX(), e.getY() + GRAVITY/2, e.getWidth(), e.getHeight(),false)){
					e.move(0, GRAVITY/2);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			mutex.release();
		}
	}
	
	/**
	 * Handles enemy walking and collisions.
	 */
	public void handleEnemies(){
		for(iEnemy e:enemies){
			int direction = e.getDirection();
			if(!player.getLock() && level.isMovableArea(player.getX(), player.getY() + 1, player.getWidth(), player.getHeight(),true) && e.isInArea(player.getX(), player.getY()+1, player.getWidth(), player.getHeight()) && !e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				e.kill();
			}else if(!e.isDead() && e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				player.hit();
			}
			boolean isMovableArea = level.isMovableArea(e.getX()+(iLevel.SPEED/ENEMY_SPEED_FACTOR)*direction, e.getY(), e.getWidth(), e.getHeight(),false);
			if(!e.isDead() && level.isInFrame(e.getX()) && isMovableArea){
				try {
					mutex.acquire();
					e.move(iLevel.SPEED/ENEMY_SPEED_FACTOR*direction, 0);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}finally{
					mutex.release();
				}	
				if(Math.random() > enemyJumpChances){
					e.jump(level, this, GRAVITY/2, JUMP_HEIGHT,false);
				}
			}else if(!isMovableArea){
				e.changeDirection();
			}
		}
	}
	
	/**
	 * Sets the chances for an enemy to jump.
	 * @param chances - chances to jump
	 */
	public void setEnemyJumpChances(double chances){
		enemyJumpChances = chances;
	}
	
	/**
	 * Makes the player jump.
	 */
	public void jump(){
		player.jump(level, this, GRAVITY, JUMP_HEIGHT,true);
	}
	
	/**
	 * Moves the player to the right.
	 * This is realized by moving all visible blocks to the left.
	 */
	public void right(){
		try {
			mutex.acquire();
			if(level.isMovableArea(player.getX() + iLevel.SPEED, player.getY(), player.getWidth(), player.getHeight(),true)){
				level.update(-iLevel.SPEED);
				notifyObserver();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			mutex.release();
		}		
	}
	
	/**
	 * Moves the player to the left.
	 * This is realized by moving all visible blocks to the right.
	 */
	public void left(){
		try {
			mutex.acquire();
			if(level.isMovableArea(player.getX() - iLevel.SPEED, player.getY(), player.getWidth(), player.getHeight(),true)){
				level.update(iLevel.SPEED);
				notifyObserver();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			mutex.release();
		}
	}
	
	/**
	 * Returns gravity constant.
	 * @return gravity
	 */
	public int getGravity(){
		return GRAVITY;
	}
	
	/**
	 * Returns height.
	 * @return Height
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * Returns width.
	 * @return Width
	 */
	public int getWidth(){
		return width;
	}
}
