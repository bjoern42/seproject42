package project42;

public final class Enemy extends Block{
int direction = 1;
boolean dead = false;

	public Enemy(int pX, int pY, int pSize, int pType) {
		super(pX, pY, pSize, pType);
	}
	
	public int getDirection(){
		return direction;
	}
	
	public void changeDirection(){
		direction *= -1;
	}
	
	public void kill(){
		dead = true;
	}
	
	public boolean isDead(){
		return dead;
	}
}
