package de.htwg.project42.model.GameObjects.Implementation;

import org.apache.log4j.Logger;

import de.htwg.project42.model.GameObjects.GameObjectsInterface;

/**
 * Abstract GameObject class for all objects in the landscape.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public abstract class GameObject implements GameObjectsInterface{
private int x, y, width, height;
private boolean jump = true;
private Logger logger = Logger.getLogger("de.htwg.project42.view.TUI");

	/**
	 * Creates GameObect.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pWidth - Width
	 * @param pHeight - Height
	 */
	public GameObject(int pX, int pY, int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		x = pX;
		y = pY;
	}
	
	/**
	 * Checks if GameObject is in the area of the specified parameters.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @return true if it is in the area, false if not.
	 */
	@Override
	public boolean isInArea(int pX, int pY, int pWidth, int pHeight){
		if(pX + pWidth > x && pX < x + width && pY + pHeight > y && pY < y + height){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns X-Position
	 * @return X-Position
	 */
	@Override
	public int getX(){
		return x;
	}
	
	/**
	 * Sets X-Position.
	 * @param pX - X-Position
	 */
	@Override
	public void setX(int pX){
		x = pX;
	}
	
	/**
	 * Returns Y-Position
	 * @return Y-Position
	 */
	@Override
	public int getY(){
		return y;
	}
	
	/**
	 * Sets Y-Position.
	 * @param pY - Y-Position
	 */
	@Override
	public void setY(int pY){
		y = pY;
	}
	
	/**
	 * Returns width
	 * @return Width
	 */
	@Override
	public int getWidth(){
		return width;
	}
	
	/**
	 * Returns height
	 * @return Height
	 */
	@Override
	public int getHeight(){
		return height;
	}

	/**
	 * Returns if the GameObject is on the ground or not.
	 * @return true if it is on the ground, false if not.
	 */
	@Override
	public boolean getJump(){
		return jump;
	}
	
	/**
	 * Sets jump.
	 * @param pJump - jump
	 */
	@Override
	public void setJump(boolean pJump){
		jump = pJump;
	}
	
	/**
	 * Moves the GameObject.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 */
	@Override
	public void move(int pX, int pY){
		x += pX;
		y += pY;
	}
	
	/**
	 * Sleeps a specified time.
	 * @param pause - time to sleep
	 */
	@Override
	public void pause(int pause){
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}
}
