package project42;


public abstract class GameObject{
int x, y, width, height,change = 0;
boolean jump = true, falling = false;

	public GameObject(int pX, int pY, int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		x = pX;
		y = pY;
	}
	
	public boolean isInArea(int pX, int pY, int pWidth, int pHeight){
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

	public boolean getJump(){
		return jump;
	}
	
	public void move(int pX, int pY){
		x += pX;
		y += pY;
	}
	
	public void jump(final Observer observer, final Observable observable, final int gravity, final int height){
		if(jump && !observer.isMovableArea(getX(), getY() + gravity, getWidth(), getHeight())){
			jump = false;
			new Thread(){
				public void run(){
					for(int j = 0; j < height;j++){
						if(observer.isMovableArea(getX(), getY() - gravity, getWidth(), getHeight())){
							Landscape.pause();
							move(0, -gravity);
							observable.update(0);
						}
					}
					while(observer.isMovableArea(getX(), getY() + gravity*2, getWidth(), getHeight())){
						Landscape.pause();
						move(0, gravity*2);
						observable.update(0);
					}
					jump = true;
				}
			}.start();
		}
	}
}
