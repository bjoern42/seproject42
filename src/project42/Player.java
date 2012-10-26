package project42;

public class Player extends GameObject{
	public Player(int pX, int pY, int pWidth, int pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
	
	public void move(int pX, int pY){
		x += pX;
		y += pY;
	}
}