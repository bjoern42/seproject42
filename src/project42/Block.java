package project42;

import java.awt.Graphics;

public class Block extends GameObject{

	public Block(int size) {
		super(size, size);
	}

	@Override
	public void paint(Graphics g,int x,int y) {
		g.drawRect(x,y,width,height);
	}
}
