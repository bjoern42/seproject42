package project42;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Landscape implements KeyListener{
final int SIZE_X = 12,SIZE_Y = 8, GRAVITY = 10, SPEED = 10, JUMP_HEIGHT = 10;
int width, height, counter = 0;
LevelLoader loader = null;
Player player = null;
Observer objects = null;

private boolean test = true;

	public static void main(String[] args) {
		new Landscape(1200,800);
	}
	
	public Landscape(int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		int size = width/SIZE_X;
		player = new Player(SIZE_X*size/2, SIZE_Y*size-size*3, size, size*2);
		objects = new Observer(size ,SIZE_X, SIZE_Y);		
		System.out.println(player.getX());
		startGravity();
//		jump();
	}
	
	private void startGravity(){
		new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(100);
						if(objects.isMovableArea(player.getX(), player.getY() + GRAVITY, player.getWidth(), player.getHeight())){
							player.move(0, GRAVITY);
						}
						movetest(test);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	private void movetest(boolean right){
		if(right){
			right();
		}else{
			left();
		}
	}
	
	private void jump(){
		for(int i = 0; i < JUMP_HEIGHT;i++){
			if(objects.isMovableArea(player.getX(), player.getY() - GRAVITY, player.getWidth(), player.getHeight())){
				player.move(0, -GRAVITY*2);
			}
		}
	}
	
	private void right(){
		if(objects.isMovableArea(player.getX() + SPEED, player.getY(), player.getWidth(), player.getHeight())){
			counter+=SPEED;
			if(counter > player.getWidth()){
				counter = SPEED;
				objects.removeFirst();
			}
			objects.update(-SPEED);
		}else{
			counter = 0;
			test = false;
		}
	}
	
	private void left(){
		if(objects.isMovableArea(player.getX() - SPEED, player.getY(), player.getWidth(), player.getHeight())){
			counter+=SPEED;
			if(counter > player.getWidth()){
				counter = SPEED;
				objects.removeLast();
			}
			
			objects.update(SPEED);
		}
		else{
			counter = 0;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode()){
			case KeyEvent.VK_KP_UP:{
				jump();
				break;
			}
			case KeyEvent.VK_KP_LEFT:{
				left();
				break;
			}
			case KeyEvent.VK_KP_RIGHT:{
				right();
				break;
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}