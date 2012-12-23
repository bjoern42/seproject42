package de.htwg.project42.controller.Implementation;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;


import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.observer.Observer;

/**
 * Controller class for JumpNRun game.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@Singleton
public final class Landscape extends Observer implements LandscapeInterface{
private static final int GRAVITY = 10, JUMP_HEIGHT = 16, RUN_PAUSE = 20, ENEMY_SPEED_FACTOR = 4;
private static final double STANDARD_ENEMY_JUMP_CHANCES = 0.995;
private double enemyJumpChances = STANDARD_ENEMY_JUMP_CHANCES;
private int width, height;
private PlayerInterface player = null;
private List<EnemyInterface> enemies = null;
private List<BlockInterface> crates = null;
private LevelInterface level = null;
private Mutex mutex = new Mutex();
private Logger logger = Logger.getLogger("de.htwg.project42.view.TUI");

	/**
	 * Generates Landscape.
	 * @param pPlayer - Player
	 * @param pLevel - Level
	 * @param pWidth - Width
	 * @param pHeight - Height
	 */
	@Inject
	public Landscape(PlayerInterface pPlayer, LevelInterface pLevel, @Named("landscapeWidth") int pWidth, @Named("landscapeHeight") int pHeight){
		width = pWidth;
		height = pHeight;
		player = pPlayer;
		level = pLevel;
	}
	
	/**
	 * Adds an Observer
	 */
	public void addAnObserver(Observable o){
		addObserver(o);
	}
	
	/**
	 * Loads a Level.
	 */
	public void loadLevel(File map){
		player.reset();
		level.loadData(map);
		enemies = level.getEnemies();
		crates = level.getCrates();
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
			if(player.getJump() && level.isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING)){
				player.move(0, GRAVITY*2);
				if(player.getY()>height){
					player.setHealth(0);
				}
			}
			for(EnemyInterface e:enemies){
				if(level.isInFrame(e.getX()) && e.getJump() && level.isMovableArea(e.getX(), e.getY() + GRAVITY/2, e.getWidth(), e.getHeight(),LevelInterface.ENEMY_MOVING)){
					e.move(0, GRAVITY/2);
				}
			}
			for(BlockInterface c:crates){
				if(c.getY() < height && level.isInFrame(c.getX()) && level.isMovableArea(c.getX(), c.getY() + GRAVITY/2, c.getWidth(), c.getHeight(),LevelInterface.CRATE_MOVING)){
					c.move(0, GRAVITY/2);
				}
			}
		} catch (InterruptedException e) {
			logger.error(e);
		}finally{
			mutex.release();
		}
	}
	
	/**
	 * Handles enemy walking and collisions.
	 */
	public void handleEnemies(){
		for(EnemyInterface e:enemies){
			int direction = e.getDirection();
			if(!player.getLock() && level.isMovableArea(player.getX(), player.getY() + 1, player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING) && e.isInArea(player.getX(), player.getY()+1, player.getWidth(), player.getHeight()) && !e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				e.kill();
			}else if(!e.isDead() && e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				player.hit();
			}
			boolean isMovableArea = level.isMovableArea(e.getX()+(LevelInterface.SPEED/ENEMY_SPEED_FACTOR)*direction, e.getY(), e.getWidth(), e.getHeight(),LevelInterface.ENEMY_MOVING);
			if(!e.isDead() && level.isInFrame(e.getX()) && isMovableArea){
				try {
					mutex.acquire();
					e.move(LevelInterface.SPEED/ENEMY_SPEED_FACTOR*direction, 0);
				} catch (InterruptedException ie) {
					logger.error(ie);
				}finally{
					mutex.release();
				}	
				if(Math.random() > enemyJumpChances){
					e.jump(level, this, GRAVITY/2, JUMP_HEIGHT,LevelInterface.ENEMY_MOVING);
				}
			}else if(!isMovableArea){
				e.changeDirection();
			}
		}
	}
	
	/**
	 * Makes the player jump.
	 */
	public void jump(){
		player.jump(level, this, GRAVITY, JUMP_HEIGHT,LevelInterface.PLAYER_MOVING);
	}
	
	/**
	 * Moves the player to the right.
	 * This is realized by moving all visible blocks to the left.
	 */
	public void right(){
		try {
			mutex.acquire();
			if(level.isMovableArea(player.getX() + LevelInterface.SPEED, player.getY(), player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING)){
				level.update(-LevelInterface.SPEED);
				notifyObserver();
			}
		} catch (InterruptedException e) {
			logger.error(e);
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
			if(level.isMovableArea(player.getX() - LevelInterface.SPEED, player.getY(), player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING)){
				level.update(LevelInterface.SPEED);
				notifyObserver();
			}
		} catch (InterruptedException e) {
			logger.error(e);
		}finally{
			mutex.release();
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
	 * Returns the Level
	 * @return level
	 */
	public LevelInterface getLevel(){
		return level;
	}
		
	/**
	 * Returns the player.
	 * @return Player
	 */
	public PlayerInterface getPlayer(){
		return player;
	}
	
	/**
	 * Returns a list of all enemies.
	 * @return Enemies
	 */
	public List<EnemyInterface> getEnemies(){
		return enemies;
	}

	/**
	 * Returns a list of all crates.
	 * @return Crates
	 */
	public List<BlockInterface> getCrates(){
		return crates;
	}
	
	/**
	 * Returns a list of all visible blocks.
	 * @return visible blocks
	 */
	public List<BlockInterface[]> getVisibleBlocks(){
		return level.getVisibleBlocks();
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
