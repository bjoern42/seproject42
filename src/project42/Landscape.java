package project42;

import java.io.File;
import java.util.List;

public class Landscape{
final int GRAVITY = 10, SPEED = 10, JUMP_HEIGHT = 16;
int width, height;
LevelLoader loader = null;
Player player = null;
Observer objects = null;
Observable observable = null;
boolean jump = true;
	
	public Landscape(File map,Observable pObservable, int pWidth,int pHeight, int pLength){
		observable = pObservable;
		width = pWidth;
		height = pHeight;
		int size = width/pLength;
		player = new Player(pLength*size/2, 0, size, size*2);
		objects = new Observer(map, size ,pLength);
		gravity();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public List<Block[]> getVisibleBlocks(){
		return objects.getVisibleBlocks();
	}
	
	private void pause(){
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void gravity(){
		new Thread(){
			public void run(){
				while(true){
					pause();
					boolean isMovableArea = objects.isMovableArea(player.getX(), player.getY() + GRAVITY, player.getWidth(), player.getHeight());
					if(jump && isMovableArea){
						player.move(0, GRAVITY*2);
						observable.update(0);
					}
				}
			}
		}.start();
	}
	
	public void jump(){
		if(jump){
			jump = false;
			new Thread(){
				public void run(){
					for(int j = 0; j < JUMP_HEIGHT;j++){
						if(objects.isMovableArea(player.getX(), player.getY() - GRAVITY, player.getWidth(), player.getHeight())){
							pause();
							player.move(0, -GRAVITY);
							observable.update(0);
						}
					}
					for(int j = 0; j < JUMP_HEIGHT;j++){
						if(objects.isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight())){
							pause();
							player.move(0, GRAVITY*2);
							observable.update(0);
						}
					}
					jump = true;
				}
			}.start();
		}
	}
	
	public void right(){
		if(objects.isMovableArea(player.getX() + SPEED, player.getY(), player.getWidth(), player.getHeight())){
			objects.update(-SPEED);
			observable.update(0);
		}
	}
	
	public void left(){
		if(objects.isMovableArea(player.getX() - SPEED, player.getY(), player.getWidth(), player.getHeight())){
			objects.update(SPEED);
			observable.update(0);
		}
	}
}