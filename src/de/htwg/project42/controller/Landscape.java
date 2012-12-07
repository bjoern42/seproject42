package de.htwg.project42.controller;

import java.io.File;
import java.util.List;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;


import de.htwg.project42.model.GameObjects.Block;
import de.htwg.project42.model.GameObjects.Enemy;
import de.htwg.project42.model.GameObjects.GameObject;
import de.htwg.project42.model.GameObjects.Level;
import de.htwg.project42.model.GameObjects.Player;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.observer.Observer;

/**
 * Controller class for JumpNRun game.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Landscape extends Observer{
private static final int GRAVITY = 10, SPEED = 10, JUMP_HEIGHT = 16, RUN_PAUSE = 20, ENEMY_SPEED_FACTOR = 4;
private static final double STANDARD_ENEMY_JUMP_CHANCES = 0.995;
private double enemyJumpChances = STANDARD_ENEMY_JUMP_CHANCES;
private int width, height;
private Player player = null;
private List<Enemy> enemies = null;
private Level level = null;
private Mutex mutex = new Mutex();
	
	/**
	 * Generates Landscape.
	 * @param map - map to be loaded.
	 * @param pObservable - Observer
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @param pLength - Visible columns
	 */
	public Landscape(File map,Observable pObservable, int pWidth,int pHeight, int pLength){
		addObserver(pObservable);
		width = pWidth;
		height = pHeight;
		int size = width/pLength;
		player = new Player(pLength*size/2, 0, size, size*2);
		level = new Level(player, map, size ,pLength);
		enemies = level.getEnemies();
	}
	
	public void activateTUI(Observable o){
		addObserver(o);
	}
	
	/**
	 * Returns the Level
	 * @return level
	 */
	public Level getLevel(){
		return level;
	}
	
	/**
	 * Returns the player.
	 * @return Player
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Returns a list of all enemies.
	 * @return Enemies
	 */
	public List<Enemy> getEnemies(){
		return enemies;
	}
	
	/**
	 * Returns a list of all visible blocks.
	 * @return visible blocks
	 */
	public List<Block[]> getVisibleBlocks(){
		return level.getVisibleBlocks();
	}
	
	/**
	 * Starts the game.
	 */
	public void start(){
		new Thread(){
			public void run(){
				while(player.getHealth() > 0 && !player.isInGoal()){
					GameObject.pause(RUN_PAUSE);
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
			for(Enemy e:enemies){
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
		for(Enemy e:enemies){
			int direction = e.getDirection();
			if(!player.getLock() && level.isMovableArea(player.getX(), player.getY() + 1, player.getWidth(), player.getHeight(),true) && e.isInArea(player.getX(), player.getY()+1, player.getWidth(), player.getHeight()) && !e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				e.kill();
			}else if(!e.isDead() && e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				player.hit();
			}
			boolean isMovableArea = level.isMovableArea(e.getX()+(SPEED/ENEMY_SPEED_FACTOR)*direction, e.getY(), e.getWidth(), e.getHeight(),false);
			if(!e.isDead() && level.isInFrame(e.getX()) && isMovableArea){
				e.move(SPEED/ENEMY_SPEED_FACTOR*direction, 0);
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
			if(level.isMovableArea(player.getX() + SPEED, player.getY(), player.getWidth(), player.getHeight(),true)){
				level.update(-SPEED);
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
			if(level.isMovableArea(player.getX() - SPEED, player.getY(), player.getWidth(), player.getHeight(),true)){
				level.update(SPEED);
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
