package project42;

import java.awt.Graphics;

public class Block extends GameObject{
static final int TYP_LUFT = 0;	
static final int TYP_GRAS = 1;
int type = TYP_LUFT;

	public Block(int size,int pType) {
		super(size, size);
		type = pType;
	}

	@Override
	public void paint(Graphics g,int x,int y) {
		g.drawRect(x,y,width,height);
	}
	
	@Override
	public String toString(){
		return "["+String.valueOf(type)+"]";
	}
}
