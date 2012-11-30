package de.htwg.project42.model;

import de.htwg.project42.observer.Observable;

/**
 * Block for JumpNRun
 * @author bjeschle,toofterd
 * @version 1.0
 */
public class Block extends GameObject  implements Observable{
public static final int TYP_AIR = 0;	
public static final int TYP_GRAS = 1;
public static final int TYP_ENEMY = 2;
public static final int TYP_WATER = 3;
public static final int TYP_COIN = 4;
private int type = TYP_AIR;

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
		x += pChange;
	}
}
