package de.htwg.project42.model.GameObjects.Implementation;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.LevelLoaderInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.model.GameObjects.Movable.Movable;

/**
 * Level for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@Singleton
public final class Level implements LevelInterface, Movable {
private List<BlockInterface[]> objects = new LinkedList<BlockInterface[]>();
private List<EnemyInterface> enemies = new LinkedList<EnemyInterface>();
private List<BlockInterface> crates = new LinkedList<BlockInterface>();
private int start, length, size, change = 0;
private PlayerInterface player = null;
private static final double QUARTER = 0.25,THREE_QUARTERS = 0.75;
private LevelLoaderInterface loader;

	/**
	 * Creates Level.
	 * @param pPlayer - Player
	 * @param pSize - Blocksize
	 * @param pLength - Visible blocks
	 */
	@Inject
	public Level(LevelLoaderInterface pLoader, PlayerInterface pPlayer, @Named("blockSize") int pSize, @Named("visibleBlockIndex") int pLength){
		player = pPlayer;
		start = 0;
		length = pLength+2;
		size = pSize;
		loader = pLoader;
	}
	
	public void loadLevel(File map){
		objects.clear();
		enemies.clear();
		crates.clear();
		player.reset();
		int blockType[] = null;
		int i = 0;
		loader.setInputFile(map);
		while((blockType = loader.readNext()) != null){
			BlockInterface block[] = new Block[blockType.length];
			for(int j=0; j<blockType.length; j++){
				if(blockType[j] == BlockInterface.TYP_ENEMY){
					addEnemy(new Enemy(size*i, size*j, size));
					blockType[j] = BlockInterface.TYP_AIR;
				}
				block[j] = new Block(size*i, size*j, size,blockType[j]);
				if(blockType[j] == BlockInterface.TYP_CRATE){
					addCrate(block[j]);
				}
			}
			objects.add(block);
			i++;
		}
	}
	
	/**
	 * Adds an enemy.
	 * @param pEnemy - enemy
	 */
	public void addEnemy(EnemyInterface pEnemy){
		enemies.add(pEnemy);
	}
	
	/**
	 * Returns all enemies.
	 * @return enemies
	 */
	public List<EnemyInterface> getEnemies(){
		return enemies;
	}
	
	/**
	 * adds a crate.
	 * @param block - Crate
	 */
	public void addCrate(BlockInterface block){
		crates.add(block);
	}
	
	/**
	 * Returns all crates.
	 * @return crates
	 */
	public List<BlockInterface> getCrates(){
		return crates;
	}
	
	/**
	 * Returns visible blocks.
	 * @return visible blocks
	 */
	public List<BlockInterface[]> getVisibleBlocks(){
		if(getStart()+getLength()>objects.size()){
			return objects;
		}
		return objects.subList(getStart(), getStart()+getLength());
	}
	
	/**
	 * Sets blocks.
	 * @param list - blocks
	 */
	public void setBlocks(List<BlockInterface[]> list){
		objects = list;
	}
	
	/**
	 * Removes first block column if there are blocks left.
	 */
	public void removeFirst(){
		if(getStart()+getLength() < objects.size()){
			int x = (getLength()-1)*size;
			for(BlockInterface b:objects.get(getStart()+getLength())){
				b.setX(x);
			}
			setStart(getStart() + 1);
		}
	}
	
	/**
	 * Removes last block column if there are blocks left.
	 */
	public void removeLast(){
		if(getStart() > 0){
			int x = 0;
			for(BlockInterface b:objects.get(getStart())){
				b.setX(x);
			}
			setStart(getStart() - 1);
		}
	}

	/**
	 * Checks if the specified X-Position is in the visible frame.
	 * @param pX - X-Position
	 * @return true if it is in the frame, false if not
	 */
	public boolean isInFrame(int pX){
		if(pX > -size && pX < (getLength()-1)*size){
			return true;
		}
		return false;
	}
	
	/**
	 * Changes the positions of all blocks.
	 * @param pChange - change
	 */
	@Override
	public void update(int pChange) {
		if(objects.get(getStart())[0].getX() < -size-pChange){
			removeFirst();
		}else if(objects.get(getStart()+getLength()-1)[0].getX() > (getLength()-1)*size-pChange){
			removeLast();
		}
		for(int i=getStart(); i < getStart()+getLength();i++){
			BlockInterface block[] = objects.get(i);
			for(int j=0; j<block.length; j++){
				block[j].update(pChange);
			}
		}
		for(EnemyInterface e:enemies){
			e.update(pChange);
		}
		change += pChange*-1;
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
	public boolean isMovableArea(int pX, int pY, int pWidth, int pHeight, int pMoving){
		int x = (change+pX) / size, y = pY / size;
		if(pMoving != LevelInterface.CRATE_MOVING && !handleCrateCollision(pX, pY, pWidth, pHeight, pMoving)){
			return false;
		}
		for(int i=-1;i<=2;i++){
			if(x+i >= 0 && x+i < objects.size()){
				BlockInterface block[] = objects.get(x+i);
				for(int j=-1;j<=pHeight/size;j++){
					if(y+j >= 0 && y+j<block.length && block[y+j].isInArea(pX, pY, pWidth, pHeight)){
						if(block[y+j].getType() == BlockInterface.TYP_GRAS){
							return false;
						}else if(block[y+1].getType() == BlockInterface.TYP_WATER){
							player.setHealth(0);
						}else if(pMoving == LevelInterface.PLAYER_MOVING && block[y+j].getType() == BlockInterface.TYP_COIN){
							player.increaseCoins();
							block[y+j].setType(BlockInterface.TYP_AIR);
						}else if(block[y+1].getType() == BlockInterface.TYP_GOAL){
							player.setGoal(true);
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
	private boolean handleCrateCollision(int pX, int pY, int pWidth, int pHeight, int pMoving){
		for(BlockInterface crate:crates){
			if(crate.isInArea(pX, pY, pWidth, pHeight)){
				if(pMoving == LevelInterface.PLAYER_MOVING && pY+pHeight >= crate.getY()+crate.getHeight()/2){
					if(pX < crate.getX()+crate.getWidth()*QUARTER && isCrateMovable(crate, pX, false)){
						//collision left
						crate.move(LevelInterface.SPEED, 0);
					}else if(pX > crate.getX()+crate.getWidth()*THREE_QUARTERS && isCrateMovable(crate, pX, true)){
						//collision right
						crate.move(-LevelInterface.SPEED, 0);
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
	 * @param left - indicates wether the block is being moved left or right.
	 * @return true if movable false if not
	 */
	private boolean isCrateMovable(BlockInterface pCrate, int pX, boolean left){
		int xOffset = LevelInterface.SPEED;
		int indexY = pCrate.getY()/size;
		int indexX = (change+pX) / size;
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
			if(!e.isDead() && isInFrame(e.getX()) && e.isInArea(pCrate.getX()+xOffset, pCrate.getY(), pCrate.getWidth(), pCrate.getHeight())){
				return false;
			}
		}
		if(block.getType() != BlockInterface.TYP_AIR && block.getType() != BlockInterface.TYP_CRATE && block.isInArea(pCrate.getX()+xOffset, pCrate.getY(), size, size)){
			return false;
		}
		return true;
	}
	
	/**
	 * Returns start of visible frame.
	 * @return start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Sets start of visible frame.
	 * @param pStart - start
	 */
	public void setStart(int pStart) {
		start = pStart;
	}

	/**
	 * Returns number of visible blocks.
	 * @return length
	 */
	public int getLength() {
		return length;
	}
}
