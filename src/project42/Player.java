package project42;

public final class Player extends GameObject{
final int coinsForLife = 50;
int health = 3,coins = 0;
boolean lock = false;

	public Player(int pX, int pY, int pWidth, int pHeight) {
		super(pX, pY, pWidth, pHeight);
	}
	
	public void hit(){
		if(!lock){
			if(health > 0){
				health--;
				lock = true;
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
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int pHealth){
		health = pHealth;
	}
	
	public int getCoins(){
		return coins;
	}
	
	public void increaseCoins(){
		coins++;
		if(coins > coinsForLife){
			coins = 0;
			health++;
		}
	}
}
