package de.htwg.project42.model.GameObjects.Implementation;

import java.util.LinkedList;
import java.util.List;

import de.htwg.project42.model.GameObjects.iBlock;
import de.htwg.project42.model.GameObjects.iEnemy;
import de.htwg.project42.model.GameObjects.iLevel;
import de.htwg.project42.model.GameObjects.iLevelLoader;
import de.htwg.project42.model.GameObjects.iPlayer;
import de.htwg.project42.model.GameObjects.Movable.Movable;

/**
 * Level for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Level implements iLevel,Movable {
private List<iBlock[]> objects = new LinkedList<iBlock[]>();
private List<iEnemy> enemies = new LinkedList<iEnemy>();
private List<iBlock> crates = new LinkedList<iBlock>();
private int start, length, size, change = 0;
private iPlayer player = null;

	/**
	 * Creates Level.
	 * @param pPlayer - Player
	 * @param pSize - Blocksize
	 * @param pLength - Visible blocks
	 */
	public Level(iLevelLoader loader, iPlayer pPlayer, int pSize, int pLength){
		player = pPlayer;
		start = 0;
		length = pLength+2;
		size = pSize;
		int blockType[] = null;
		int i = 0;
		while((blockType = loader.readNext()) != null){
			iBlock block[] = new Block[blockType.length];
			for(int j=0; j<blockType.length; j++){
				if(blockType[j] == iBlock.TYP_ENEMY){
					addEnemy(new Enemy(size*i, size*j, size));
					blockType[j] = iBlock.TYP_AIR;
				}
				block[j] = new Block(size*i, size*j, size,blockType[j]);
				if(blockType[j] == iBlock.TYP_CRATE){
					addCrate(block[j]);
					block[j].setMovable(true);
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
	public void addEnemy(iEnemy pEnemy){
		enemies.add(pEnemy);
	}
	
	/**
	 * Returns all enemies.
	 * @return enemies
	 */
	public List<iEnemy> getEnemies(){
		return enemies;
	}
	
	/**
	 * adds a crate.
	 * @param block - Crate
	 */
	public void addCrate(iBlock block){
		crates.add(block);
	}
	
	/**
	 * Returns all crates.
	 * @return crates
	 */
	public List<iBlock> getCrates(){
		return crates;
	}
	
	/**
	 * Returns visible blocks.
	 * @return visible blocks
	 */
	public List<iBlock[]> getVisibleBlocks(){
		if(getStart()+getLength()>objects.size()){
			return objects;
		}
		return objects.subList(getStart(), getStart()+getLength());
	}
	
	/**
	 * Sets blocks.
	 * @param list - blocks
	 */
	public void setBlocks(List<iBlock[]> list){
		objects = list;
	}
	
	/**
	 * Removes first block column if there are blocks left.
	 */
	public void removeFirst(){
		if(getStart()+getLength() < objects.size()){
			int x = (getLength()-1)*size;
			for(iBlock b:objects.get(getStart()+getLength())){
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
			for(iBlock b:objects.get(getStart())){
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
			iBlock block[] = objects.get(i);
			for(int j=0; j<block.length; j++){
				block[j].update(pChange);
			}
		}
		for(iEnemy e:enemies){
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
	 * @return
	 */
	public boolean isMovableArea(int pX, int pY, int pWidth, int pHeight,boolean playerMoving){
		int x = (change+pX) / size, y = pY / size;
		
		if(playerMoving && !handleCrateCollision(pX, pY, pWidth, pHeight)){
			return false;
		}
		for(int i=-1;i<=2;i++){
			if(x+i >= 0 && x+i < objects.size()){
				iBlock block[] = objects.get(x+i);
				for(int j=-1;j<=pHeight/size;j++){
					if(y+j >= 0 && y+j<block.length && block[y+j].isInArea(pX, pY, pWidth, pHeight)){
						if(block[y+j].getType() == iBlock.TYP_GRAS){
							return false;
						}else if(block[y+1].getType() == iBlock.TYP_WATER){
							player.setHealth(0);
						}else if(playerMoving && block[y+j].getType() == iBlock.TYP_COIN){
							player.increaseCoins();
							block[y+j].setType(iBlock.TYP_AIR);
						}else if(block[y+1].getType() == iBlock.TYP_GOAL){
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
	 * @return
	 */
	private boolean handleCrateCollision(int pX, int pY, int pWidth, int pHeight){
		for(iBlock crate:crates){
			if(crate.isInArea(pX, pY, pWidth, pHeight)){
				if(crate.getType() == Block.TYP_CRATE){
					if(pY+pHeight >= crate.getY()+crate.getHeight()/2){
						if(pX < crate.getX()+crate.getWidth()/4){
							//collision left
							crate.move(iLevel.SPEED,0);
						}else if(pX > crate.getX()+crate.getWidth()*(3/4)){
							//collision right
							crate.move(-iLevel.SPEED,0);
						}
					}else{
						return false;
					}	
				}
			}
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
