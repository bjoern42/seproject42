package de.htwg.project42.model.GameObjects.Implementation;

import de.htwg.project42.model.GameObjects.BlockInterface;

/**
 * Block for JumpNRun
 * @author bjeschle,toofterd
 * @version 1.0
 */
public class Block extends GameObject implements BlockInterface{
private int type = BlockInterface.TYP_AIR;

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
