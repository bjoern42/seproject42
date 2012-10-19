package project42;


public abstract class GameObject implements Observable{
int x, y, width, height;

	public GameObject(int pX, int pY, int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		x = pX;
		y = pY;
	}
	
	public boolean isInArea(int pX, int pY, int pWidth, int pHeight){
		pY += 10;
		if(pX + pWidth > x && pX < x + width && pY + pHeight > y && pY < y + height){
			return true;
		}
		return false;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

}