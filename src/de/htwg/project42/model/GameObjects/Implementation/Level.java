package de.htwg.project42.model.GameObjects.Implementation;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.ButtonInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.GateInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.LevelLoaderInterface;

/**
 * Level for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@Singleton
public final class Level implements LevelInterface {
private List<BlockInterface[]> objects = new LinkedList<BlockInterface[]>();
private List<EnemyInterface> enemies = new LinkedList<EnemyInterface>();
private List<BlockInterface> crates = new LinkedList<BlockInterface>();
private Map<Integer, ButtonInterface> buttons = new HashMap<Integer, ButtonInterface>();
private Map<Integer, GateInterface> gates = new HashMap<Integer, GateInterface>();
private LevelLoaderInterface loader = new LevelLoader();
private int start, length, size, change = 0;

	/**
	 * Creates Level.
	 * @param pSize - Blocksize
	 * @param pLength - Visible blocks
	 */
	@Inject
	public Level(@Named("blockSize") int pSize, @Named("visibleBlockIndex") int pLength){
		length = pLength+2;
		size = pSize;
	}
	
	/**
	 * Loads map data from file.
	 * @param map - .lvl file
	 */
	public boolean loadData(File map){
		start = 0;
		change = 0;
		enemies.clear();
		crates.clear();
		objects.clear();
		int blockType[] = null;
		int i = 0;
		loader.setInputFile(map);
		while((blockType = loader.readNext()) != null){
			LinkedList<BlockInterface> blocks = new LinkedList<BlockInterface>();
			for(int j=0; j<blockType.length; j++){
				if(blockType[j] == BlockInterface.TYP_ENEMY){
					addEnemy(new Enemy(size*i, size*blocks.size(), size));
					blockType[j] = BlockInterface.TYP_AIR;
				}else if(blockType[j] == BlockInterface.TYP_CRATE){
					addCrate(new Block(size*i, size*blocks.size(), size, blockType[j]));
					blockType[j] = BlockInterface.TYP_AIR;
				}else if(blockType[j] == BlockInterface.TYP_BUTTON){
					blocks.add(new Button(this, size*i, size*blocks.size(), size, blockType[j]));
					buttons.put(blockType[++j], (ButtonInterface) blocks.getLast());
					GateInterface gate = gates.get(blockType[j]);
					if (gate != null){
						buttons.get(blockType[j]).registerSwitchable(gate);
					}
					continue;
				}else if(blockType[j] == BlockInterface.TYP_GATE){
					blocks.add(new Gate(size*i, size*blocks.size(), size, blockType[j]));
					ButtonInterface button = buttons.get(blockType[++j]);
					if(button != null){
						button.registerSwitchable((GateInterface)blocks.getLast());
					}else{
						gates.put(blockType[j],(GateInterface) blocks.getLast());
					}
					continue;
				}
				
				blocks.add(new Block(size*i, size*blocks.size(), size, blockType[j]));
			}
			objects.add(blocks.toArray(new BlockInterface[blocks.size()]));
			i++;
		}
		loader.closeStreams();
		if(objects.size() < length){
			return false;
		}
		return true;
	}
	
	/**
	 * Releases all Buttons.
	 */
	public void releaseButtons(){
		for(Entry<Integer, ButtonInterface> entry: buttons.entrySet()){
			entry.getValue().release();
		}
		buttons.clear();
		gates.clear();
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
	 * Changes the positions of all visible blocks.
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
		for(BlockInterface c:crates){
			c.update(pChange);
		}
		change += pChange*-1;
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
	 * Returns all blocks.
	 * @return crates
	 */
	public List<BlockInterface[]> getBlocks(){
		return objects;
	}
	
	/**
	 * Returns all buttons
	 * @return buttons
	 */
	public Set<Entry<Integer, ButtonInterface>> getButtons(){
		return buttons.entrySet();

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
	
	/**
	 * Returns the x-offset.
	 * @return change
	 */
	public int getChange(){
		return change;
	}
	
	/**
	 * Returns the size of a block.
	 * @return size
	 */
	public int getBlockSize(){
		return size;
	}
}
