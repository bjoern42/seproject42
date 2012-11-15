package project42;

import java.io.File;
import java.util.List;


public final class Landscape{
final int GRAVITY = 10, SPEED = 10, JUMP_HEIGHT = 16;
int width, height;
Player player = null;
List<Enemy> enemies = null;
Observer objects = null;
Observable observable = null;
	
	public Landscape(File map,Observable pObservable, int pWidth,int pHeight, int pLength){
		observable = pObservable;
		width = pWidth;
		height = pHeight;
		int size = width/pLength;
		player = new Player(pLength*size/2, 0, size, size*2);
		objects = new Observer(map, size ,pLength);
		enemies = objects.getEnemies();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public List<Enemy> getEnemies(){
		return enemies;
	}
	
	public List<Block[]> getVisibleBlocks(){
		return objects.getVisibleBlocks();
	}
	
	public static void pause(){
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		new Thread(){
			public void run(){
				while(true){
					pause();
					boolean update = false;
					//gravity
					if(player.getJump() && objects.isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight())){
						player.move(0, GRAVITY*2);
						update = true;
					}
					//enemies
					for(Enemy e:enemies){
						int direction = e.getDirection();
						//collision with enemy
						if(!player.getLock() && objects.isMovableArea(player.getX(), player.getY() + 1, player.getWidth(), player.getHeight()) && e.isInArea(player.getX(), player.getY()+1, player.getWidth(), player.getHeight()) && !e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
							e.kill();
						}else if(!e.isDead() && e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
							player.hit();
						}
						//move enemy
						boolean isMovableArea = objects.isMovableArea(e.getX()+(SPEED/4)*direction, e.getY(), e.getWidth(), e.getHeight());
						if(!e.isDead() && objects.isInFrame(e.getX()) && isMovableArea){
							e.move(SPEED/4*direction, 0);
							if(Math.random() > 0.995){
								e.jump(objects, observable, GRAVITY/2, JUMP_HEIGHT);
							}
							update = true;
						}else if(!isMovableArea){
							e.changeDirection();
						}
					}
					if(update){
						observable.update(0);
					}
				}
			}
		}.start();
	}
	
	public void jump(){
		player.jump(objects, observable, GRAVITY, JUMP_HEIGHT);
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
