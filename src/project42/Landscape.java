package project42;

import java.io.File;
import java.util.List;


public class Landscape{
final int GRAVITY = 10, SPEED = 10, JUMP_HEIGHT = 16;
int width, height;
Player player = null;
List<Enemy> enemies = null;
Observer objects = null;
Observable observable = null;
GUI gui = null;
boolean jump = true, running = true;
	
	public Landscape(GUI pGui,File map,Observable pObservable, int pWidth,int pHeight, int pLength){
		gui = pGui;
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
	
	private void pause(){
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		new Thread(){
			public void run(){
				while(running){
					pause();
					boolean update = false;
					//gravity
					boolean isMovableArea = objects.isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight());
					if(jump && isMovableArea){
						player.move(0, GRAVITY*2);
						update = true;
					}
					//enemies
					for(Enemy e:enemies){
						int direction = e.getDirection();
						if(e.isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
							System.out.println("hit");
							running = false;
							break;
						}
						if(objects.isInFrame(e.getX()) && objects.isMovableArea(e.getX()+(SPEED/4+SPEED)*direction, e.getY(), e.getWidth(), e.getHeight())){
							e.move(SPEED/4*direction, 0);
							update = true;
						}else{
							e.changeDirection();
						}
					}
					if(update){
						observable.update(0);
					}
				}
				gui.stop();
			}
		}.start();
	}
	
	public void jump(){
		if(jump && !objects.isMovableArea(player.getX(), player.getY() + GRAVITY, player.getWidth(), player.getHeight())){
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
					while(objects.isMovableArea(player.getX(), player.getY() + GRAVITY*2, player.getWidth(), player.getHeight())){
						pause();
						player.move(0, GRAVITY*2);
						observable.update(0);
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