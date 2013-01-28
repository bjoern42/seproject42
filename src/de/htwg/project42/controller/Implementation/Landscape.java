package de.htwg.project42.controller.Implementation;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;


import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.BulletInterface;
import de.htwg.project42.model.GameObjects.ButtonInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.GameObjectsInterface;
import de.htwg.project42.model.GameObjects.GateInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.model.GameObjects.Implementation.Bullet;
import de.htwg.project42.model.GameObjects.Implementation.Enemy;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.observer.Observer;

/**
 * Controller class for JumpNRun game.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@Singleton
public final class Landscape extends Observer implements LandscapeInterface{
private double enemyJumpChances = STANDARD_ENEMY_JUMP_CHANCES;
private int width, height;
private PlayerInterface player = null;
private List<EnemyInterface> enemies = null;
private List<BulletInterface> bullets = null;
private List<BlockInterface> crates = null;
private List<BlockInterface[]> objects = null;
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
	@Override
	public void addAnObserver(Observable o){
		addObserver(o);
	}
	
	/**
	 * Removes an Observer
	 */
	@Override
	public void removeAnObserver(Observable o){
		removeObserver(o);
	}
	
	/**
	 * Loads a Level.
	 */
	@Override
	public boolean loadLevel(File map){
		if(!level.loadData(map)){
			return false;
		}
		player.reset();
		objects = level.getBlocks();
		enemies = level.getEnemies();
		crates = level.getCrates();
		bullets = level.getBullets();
		return true;
	}
	
	/**
	 * Starts the game.
	 */
	@Override
	public void start(){
		new Thread(){
			public void run(){
				while(player.getHealth() > 0 && !player.isInGoal()){
					player.pause(RUN_PAUSE);
					gravity();
					handleEnemies();
					handleBullets();
					notifyObserver();
				}
				level.releaseButtons();
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
			for(BlockInterface c:crates){
				if(c.getY() < height && level.isInFrame(c.getX()) && isMovableArea(c.getX(), c.getY() + GRAVITY/2, c.getWidth(), c.getHeight(),LevelInterface.CRATE_MOVING, c)){
					c.move(0, GRAVITY/2);
				}
			}
			for(EnemyInterface e:enemies){
				if(level.isInFrame(e.getX()) && e.getJump() && isMovableArea(e.getX(), e.getY() + GRAVITY/2, e.getWidth(), e.getHeight(),LevelInterface.ENEMY_MOVING, e)){
					e.move(0, GRAVITY/2);
				}
			}
			if(player.getJump() && isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING, player)){
				player.move(0, GRAVITY*2);
				if(player.getY()>height){
					player.setHealth(0);
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
			if(!player.getLock() && isMovableArea(player.getX(), player.getY() + 1, player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING, player) && e.isInArea(player.getX(), player.getY()+1, player.getWidth(), player.getHeight()) && !e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				e.kill();
			}else if(!e.isDead() && e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
				player.hit();
			}
			boolean isMovableArea = isMovableArea(e.getX()+(SPEED/ENEMY_SPEED_FACTOR)*direction, e.getY(), e.getWidth(), e.getHeight(),LevelInterface.ENEMY_MOVING, e);
			if(!e.isDead() && level.isInFrame(e.getX()) && isMovableArea){
				try {
					mutex.acquire();
					e.move(SPEED/ENEMY_SPEED_FACTOR*direction, 0);
				} catch (InterruptedException ie) {
					logger.error(ie);
				}finally{
					mutex.release();
				}	
				if(Math.random() > enemyJumpChances){
					jump(e, GRAVITY/2, JUMP_HEIGHT,LevelInterface.ENEMY_MOVING);
				}
			}else if(!isMovableArea){
				e.changeDirection();
			}
		}
	}
	
	
	/**
	 * Increases Y-Position and decreases it afterwards till its on the ground again.
	 * @param object - GameObject that is going to jump
	 * @param gravity - Gravity factor
	 * @param height - Height
	 * @param player - specify if jump is called by player or enemy
	 */
	public void jump(final GameObjectsInterface object, final int gravity, final int height, final int moving){
		if(object.getJump() && !isMovableArea(object.getX(), object.getY() + gravity, object.getWidth(), object.getHeight(),moving, object)){
			object.setJump(false);
			new Thread(){
				public void run(){
					for(int j = 0; j < height;j++){
						try{
							mutex.acquire();
							if(isMovableArea(object.getX(), object.getY() - gravity, object.getWidth(), object.getHeight(),moving, object)){
								object.move(0, -gravity);
								notifyObserver();
							}else{
								break;
							}
						} catch (InterruptedException ie) {
							logger.error(ie);
						}finally{
							mutex.release();
						}
						object.pause(GameObjectsInterface.PAUSE);
					}
					object.setJump(true);
				}
			}.start();
		}
	}
	
	/**
	 * Checks if there is any block in the specified area.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @param playerMoving - specify if isMovableArea is called by player
	 * @return true if player can move to the specified area, false if not.
	 */
	public boolean isMovableArea(int pX, int pY, int pWidth, int pHeight, int pMoving, GameObjectsInterface object){
		int size = level.getBlockSize();
		int x = (level.getChange()+pX) / size, y = pY / size;
		if(!handleCrateCollision(pX, pY, pWidth, pHeight, pMoving, object)){
			return false;
		}
		for(int i=-1;i<=2;i++){
			if(x+i >= 0 && x+i < objects.size()){
				BlockInterface block[] = objects.get(x+i);
				for(int j=-1;j<=pHeight/size;j++){
					if(y+j >= 0 && y+j<block.length && block[y+j].isInArea(pX, pY, pWidth, pHeight)){
						if(block[y+j].getType() == BlockInterface.TYP_GRAS){
							return false;
						}else if(block[y+j].getType() == BlockInterface.TYP_GATE && !((GateInterface)block[y+j]).isOn()){
							return false;
						}else if(pMoving == LevelInterface.PLAYER_MOVING && block[y+1].getType() == BlockInterface.TYP_WATER){
								player.setHealth(0);
						}else if(pMoving == LevelInterface.PLAYER_MOVING && block[y+j].getType() == BlockInterface.TYP_COIN){
							player.increaseCoins();
							block[y+j].setType(BlockInterface.TYP_AIR);
						}else if(pMoving == LevelInterface.PLAYER_MOVING && block[y+1].getType() == BlockInterface.TYP_GOAL){
							player.setGoal(true);
						}else if(pMoving == LevelInterface.PLAYER_MOVING && block[y+1].getType() == BlockInterface.TYP_BUTTON){
							((ButtonInterface)block[y+1]).press(player);
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Handles collision with crates.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @return true if player can move to the specified area, false if not.
	 */
	private boolean handleCrateCollision(int pX, int pY, int pWidth, int pHeight, int pMoving, GameObjectsInterface object){
		if(pMoving == LevelInterface.CRATE_MOVING){
			//check collision with enemies
			for(EnemyInterface e:enemies){
				if(e.isInArea(pX, pY, pWidth, pHeight)){
					e.kill();
				}
			}
			for(BulletInterface b:bullets){
				if(b.isInArea(pX, pY, pWidth, pHeight)){
					b.remove();
				}
			}
			for(Entry<Integer, ButtonInterface> b:level.getButtons()){
				if(b.getValue().isInArea(pX, pY, pWidth, pHeight-b.getValue().getHeight())){
					b.getValue().press(object);
				}
			}
		}
		
		for(BlockInterface crate:crates){
			if(crate.isInArea(pX, pY, pWidth, pHeight)){
				if(pMoving == LevelInterface.CRATE_MOVING){
					//check collision with other crate
					if(crate.getY() != (pY-GRAVITY/2)){
						return false;
					}
				}else if(pMoving == LevelInterface.PLAYER_MOVING && pY+pHeight >= crate.getY()+crate.getHeight()/2){
					//check collision with player
					if(pX < crate.getX()+crate.getWidth()*QUARTER && isCrateMovable(crate, pX, false)){
						//collision left
						crate.move(SPEED, 0);
					}else if(pX > crate.getX()+crate.getWidth()*THREE_QUARTERS && isCrateMovable(crate, pX, true)){
						//collision right
						crate.move(-SPEED, 0);
					}else{
						return false;
					}
				}else{
					return false;
				}	
			}
		}
		return true;
	}
	
	/**
	 * Checks if there is enough space to move the crate to the specified area.
	 * @param pCrate - crate
	 * @param pX - X-Position
	 * @param left - indicates whether the block is being moved left or right.
	 * @return true if movable false if not
	 */
	private boolean isCrateMovable(BlockInterface pCrate, int pX, boolean left){
		int size = level.getBlockSize();
		int xOffset = SPEED;
		int indexY = (pCrate.getY()+pCrate.getHeight()-1)/size;
		int indexX = (level.getChange()+pX-1) / size;
		if(left){
			indexX += -1;
			xOffset *= -1;
		}else{
			indexX += 2;
		}
		if(indexX < 0){
			return true;
		}
		BlockInterface block = objects.get(indexX)[indexY];
		for(EnemyInterface e:enemies){
			if(!e.isDead() && level.isInFrame(e.getX()) && e.isInArea(pCrate.getX()+xOffset, pCrate.getY(), pCrate.getWidth(), pCrate.getHeight())){
				return false;
			}
		}
		for(BlockInterface c:crates){
			if(c != pCrate && level.isInFrame(c.getX()) && c.isInArea(pCrate.getX()+xOffset, pCrate.getY(), pCrate.getWidth(), pCrate.getHeight())){
				return false;
			}
		}
		if(block.getType() == BlockInterface.TYP_BUTTON){
			((ButtonInterface)block).press(pCrate);
			return true;
		}else if(block.getType() == BlockInterface.TYP_GATE && ((GateInterface)block).isOn()){
			return true;
		}
		
		if(block.getType() != BlockInterface.TYP_AIR && block.getType() != BlockInterface.TYP_COIN && block.isInArea(pCrate.getX()+xOffset, pCrate.getY(), size, size)){
			return false;
		}
		return true;
	}
	
	/**
	 * Makes the player jump.
	 */
	@Override
	public void jump(){
		jump(player, GRAVITY, JUMP_HEIGHT,LevelInterface.PLAYER_MOVING);
	}
	
	/**
	 * Moves the player to the right.
	 * This is realized by moving all visible blocks to the left.
	 */
	@Override
	public void right(){
		try {
			mutex.acquire();
			if(isMovableArea(player.getX() + SPEED, player.getY(), player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING, player)){
				level.update(-SPEED);
				player.setLastDirection(1);
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
	@Override
	public void left(){
		try {
			mutex.acquire();
			if(isMovableArea(player.getX() - SPEED, player.getY(), player.getWidth(), player.getHeight(),LevelInterface.PLAYER_MOVING, player)){
				level.update(SPEED);
				player.setLastDirection(-1);
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
	@Override
	public PlayerInterface getPlayer(){
		return player;
	}
	
	/**
	 * Returns a list of all enemies.
	 * @return Enemies
	 */
	@Override
	public List<EnemyInterface> getEnemies(){
		return enemies;
	}

	/**
	 * Returns a list of all crates.
	 * @return Crates
	 */
	@Override
	public List<BlockInterface> getCrates(){
		return crates;
	}
	
	/**
	 * Returns a list of all visible blocks.
	 * @return visible blocks
	 */
	@Override
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

	@Override
	public void shoot() {
		level.addBullet(new Bullet(player.getX(), player.getY()+50, 15, player.getLastDirection()));
	}
	
	public void handleBullets(){
		if(bullets == null){
			System.out.println("no bullets there\n");
			return;
			}else
		for(BulletInterface b:bullets){
			if(b.isGone()){
				level.removeBullet(b);}
			b.move(b.getSpeed()*b.getDirection(), 0);
			}
		}

	@Override
	public List<BulletInterface> getBullets() {
		return bullets;
	}
}
