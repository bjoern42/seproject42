package project42;


public class Block extends GameObject  implements Observable{
static final int TYP_LUFT = 0;	
static final int TYP_GRAS = 1;
int type = TYP_LUFT;

	public Block(int pX, int pY, int pSize,int pType) {
		super(pX, pY, pSize, pSize);
		type = pType;
	}
	
	public int getType(){
		return type;
	}
	
	@Override
	public String toString(){
		return "["+String.valueOf(type)+"]";
	}

	@Override
	public void update(int pChange) {
		System.out.print(this);
		x += pChange;
		getX();
		getY();
		getWidth();
		getHeight();
	}
}
