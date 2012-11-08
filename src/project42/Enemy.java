package project42;

public class Enemy extends Block{
int direction = 1;

	public Enemy(int pX, int pY, int pSize, int pType) {
		super(pX, pY, pSize, pType);
	}

	public void move(int pX, int pY){
		x += pX;
		y += pY;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public void changeDirection(){
		direction *= -1;
	}
}
