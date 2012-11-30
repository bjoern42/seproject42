package de.htwg.project42.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Level for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class Level implements Movable {
private List<Block[]> objects = new LinkedList<Block[]>();
private List<Enemy> enemies = new LinkedList<Enemy>();
private int start, length, size, change = 0;
private Player player = null;

	/**
	 * Creates Level.
	 * @param pPlayer - Player
	 * @param map - Map
	 * @param pSize - Blocksize
	 * @param pLength - Visible blocks
	 */
	public Level(Player pPlayer, File map, int pSize, int pLength){
		player = pPlayer;
		start = 0;
		length = pLength+2;
		size = pSize;
		LevelLoader loader = new LevelLoader(map);
		int blockType[] = null;
		int i = 0;
		while((blockType = loader.readNext()) != null){
			Block block[] = new Block[blockType.length];
			for(int j=0; j<blockType.length; j++){
				if(blockType[j] == Block.TYP_ENEMY){
					enemies.add(new Enemy(size*i, size*j, size));
					blockType[j] = Block.TYP_AIR;
				}
				block[j] = new Block(size*i, size*j, size,blockType[j]);
			}
			objects.add(block);
			i++;
		}
	}
	
	/**
	 * Returns visible blocks.
	 * @return visible blocks
	 */
	public List<Block[]> getVisibleBlocks(){
		if(getStart()+getLength()>objects.size()){
			return objects;
		}
		return objects.subList(getStart(), getStart()+getLength());
	}
	
	/**
	 * Sets blocks.
	 * @param list - blocks
	 */
	public void setBlocks(List<Block[]> list){
		objects = list;
	}
	
	/**
	 * Returns all enemies.
	 * @return enemies
	 */
	public List<Enemy> getEnemies(){
		return enemies;
	}
	
	/**
	 * Removes first block column if there are blocks left.
	 */
	public void removeFirst(){
		if(getStart()+getLength() < objects.size()){
			int x = (getLength()-1)*size;
			for(Block b:objects.get(getStart()+getLength())){
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
			for(Block b:objects.get(getStart())){
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
			Block block[] = objects.get(i);
			for(int j=0; j<block.length; j++){
				block[j].update(pChange);
			}
		}
		for(Enemy e:enemies){
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
		for(int i=-1;i<=2;i++){
			if(x+i >= 0 && x+i < objects.size()){
				Block block[] = objects.get(x+i);
				for(int j=-1;j<=pHeight/size;j++){
					if(y+j >= 0 && y+j<block.length && block[y+j].isInArea(pX, pY, pWidth, pHeight)){
						if(block[y+j].getType() == Block.TYP_GRAS){
							return false;
						}else if(block[y+1].getType() == Block.TYP_WATER){
							player.setHealth(0);
						}else if(playerMoving && block[y+j].getType() == Block.TYP_COIN){
							player.increaseCoins();
							block[y+j].setType(Block.TYP_AIR);
						}
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
