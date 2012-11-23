package de.htwg.project42.controller;

import java.io.File;
import java.util.List;


import de.htwg.project42.model.Block;
import de.htwg.project42.model.Enemy;
import de.htwg.project42.model.Player;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.observer.Observer;

/**
 * Controller class for JumpNRun game.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Landscape{
private final int GRAVITY = 10, SPEED = 10, JUMP_HEIGHT = 16;
private double enemyJumpChances = 0.995;
private int width, height;
private Player player = null;
private List<Enemy> enemies = null;
private Observer objects = null;
private Observable observable = null;
	
	/**
	 * Generates Landscape.
	 * @param map - map to be loaded.
	 * @param pObservable - Observer
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @param pLength - Visible columns
	 */
	public Landscape(File map,Observable pObservable, int pWidth,int pHeight, int pLength){
		observable = pObservable;
		width = pWidth;
		height = pHeight;
		int size = width/pLength;
		player = new Player(pLength*size/2, 0, size, size*2);
		objects = new Observer(player, map, size ,pLength);
		enemies = objects.getEnemies();
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
		return objects.getVisibleBlocks();
	}
	
	/**
	 * Sleeps a specified time.
	 * @param pause - time to sleep
	 */
	public static void pause(int pause){
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the game.
	 */
	public void start(){
		new Thread(){
			public void run(){
				while(player.getHealth() > 0){
					pause(20);
					gravity();
					handleEnemies();
					observable.update(0);
				}
			}
		}.start();
	}
	
	/**
	 * Handles the gravity.
	 */
	public void gravity(){
		if(player.getJump() && objects.isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight(),true)){
			player.move(0, GRAVITY*2);
			if(player.getY()>height){
				player.setHealth(0);
			}
		}
	}
	
	/**
	 * Handles enemy walking and collisions.
	 */
	public void handleEnemies(){
		for(Enemy e:enemies){
			int direction = e.getDirection();
			if(!player.getLock() && objects.isMovableArea(player.getX(), player.getY() + 1, player.getWidth(), player.getHeight(),true) && e.isInArea(player.getX(), player.getY()+1, player.getWidth(), player.getHeight()) && !e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				e.kill();
			}else if(!e.isDead() && e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				player.hit();
			}
			boolean isMovableArea = objects.isMovableArea(e.getX()+(SPEED/4)*direction, e.getY(), e.getWidth(), e.getHeight(),false);
			if(!e.isDead() && objects.isInFrame(e.getX()) && isMovableArea){
				e.move(SPEED/4*direction, 0);
				if(Math.random() > enemyJumpChances){
					e.jump(objects, observable, GRAVITY/2, JUMP_HEIGHT,false);
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
		player.jump(objects, observable, GRAVITY, JUMP_HEIGHT,true);
	}
	
	/**
	 * Moves the player to the right.
	 * This is realized by moving all visible blocks to the left.
	 */
	public void right(){
		if(objects.isMovableArea(player.getX() + SPEED, player.getY(), player.getWidth(), player.getHeight(),true)){
			objects.update(-SPEED);
			observable.update(0);
		}
	}
	
	/**
	 * Moves the player to the left.
	 * This is realized by moving all visible blocks to the right.
	 */
	public void left(){
		if(objects.isMovableArea(player.getX() - SPEED, player.getY(), player.getWidth(), player.getHeight(),true)){
			objects.update(SPEED);
			observable.update(0);
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
