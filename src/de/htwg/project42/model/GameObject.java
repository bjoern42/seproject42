package de.htwg.project42.model;

import de.htwg.project42.controller.Landscape;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.observer.Observer;

/**
 * Abstract GameObject class for all objects in the landscape.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public abstract class GameObject{
protected int x, y, width, height,change = 0;
protected boolean jump = true;

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
	public int getX(){
		return x;
	}
	
	/**
	 * Sets X-Position.
	 * @param pX - X-Position
	 */
	public void setX(int pX){
		x = pX;
	}
	
	/**
	 * Returns Y-Position
	 * @return Y-Position
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets Y-Position.
	 * @param pY - Y-Position
	 */
	public void setY(int pY){
		y = pY;
	}
	
	/**
	 * Returns width
	 * @return Width
	 */
	public int getWidth(){
		return width;
	}
	
	/**
	 * Returns height
	 * @return Height
	 */
	public int getHeight(){
		return height;
	}

	/**
	 * Returns if the GameObject is on the ground or not.
	 * @return true if it is on the ground, false if not.
	 */
	public boolean getJump(){
		return jump;
	}
	
	/**
	 * Sets jump.
	 * @param pJump - jump
	 */
	public void setJump(boolean pJump){
		jump = pJump;
	}
	
	/**
	 * Moves the GameObject.
	 * @param pX - X-Position
	 * @param pY - Y-Position
	 */
	public void move(int pX, int pY){
		x += pX;
		y += pY;
	}
	
	/**
	 * Increases Y-Position and decreases it afterwards till its on the ground again.
	 * @param observer - Observer
	 * @param observable - Observable
	 * @param gravity - Gravity factor
	 * @param height - Height
	 * @param player - specify if jump is called by player or enemy
	 */
	public void jump(final Observer observer, final Observable observable, final int gravity, final int height,final boolean player){
		if(jump && !observer.isMovableArea(getX(), getY() + gravity, getWidth(), getHeight(),player)){
			jump = false;
			new Thread(){
				public void run(){
					for(int j = 0; j < height;j++){
						if(observer.isMovableArea(getX(), getY() - gravity, getWidth(), getHeight(),player)){
							Landscape.pause(20);
							move(0, -gravity);
							observable.update(0);
						}
					}
					while(observer.isMovableArea(getX(), getY() + gravity*2, getWidth(), getHeight(),player)){
						Landscape.pause(20);
						move(0, gravity*2);
						observable.update(0);
					}
					jump = true;
				}
			}.start();
		}
	}
}
