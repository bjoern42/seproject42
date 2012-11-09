package project42;

public final class Player extends GameObject{
int health = 3;
boolean lock = false;

	public Player(int pX, int pY, int pWidth, int pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
	
	public void hit(){
		if(!lock){
			if(health > 0){
				System.out.println("health--");
				health--;
				lock = true;
			}else{
				System.out.println("You are dead!");
			}
		}else{
			new Thread(){
				public void run(){
					try {
						Thread.sleep(1000);
						lock = false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	
	public boolean getLock(){
		return lock;
	}
}
