package project42;

import java.awt.Graphics;

public abstract class GameObject {
int width,height;
	
	public GameObject(int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public abstract void paint(Graphics g,int x,int y);
}