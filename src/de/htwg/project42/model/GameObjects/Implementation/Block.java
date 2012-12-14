package de.htwg.project42.model.GameObjects.Implementation;

import de.htwg.project42.model.GameObjects.iBlock;
import de.htwg.project42.model.GameObjects.Movable.Movable;

/**
 * Block for JumpNRun
 * @author bjeschle,toofterd
 * @version 1.0
 */
public class Block extends GameObject implements iBlock,Movable{
private int type = iBlock.TYP_AIR;
private boolean movable = false;

	/**
	 * Creates a Block.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pSize - Blocksize
	 * @param pType - Blocktype
	 */
	public Block(int pX, int pY, int pSize,int pType) {
		super(pX, pY, pSize, pSize);
		type = pType;
	}
	
	/**
	 * Returns if the block is movable.
	 * @return true if movable, false if not
	 */
	public boolean isMovable(){
		return movable;
	}
	
	/**
	 * Sets the block movable
	 * @param pMovable - movable
	 */
	public void setMovable(boolean pMovable){
		movable = pMovable;
	}
	
	/**
	 * Returns the blocktype.
	 * @return blocktype
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Sets the blocktype.
	 * @param pType - blocktype
	 */
	public void setType(int pType){
		type = pType;
	}
	
	/**
	 * Prints block
	 */
	@Override
	public String toString(){
		return "["+type+"]";
	}

	/**
	 * Updates X-Position.
	 */
	@Override
	public void update(int pChange) {
		setX(getX()+pChange);
	}
}
