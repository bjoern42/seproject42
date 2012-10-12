package project42;

import java.awt.Graphics;

public abstract class GameObject {
protected int width,height;
	
	public GameObject(int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
	}
	public abstract void paint(Graphics g,int x,int y);
}
