package project42;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public final class GUI extends JPanel implements KeyListener, Observable{
final int ACTION_RIGHT = 0, ACTION_LEFT = 1, ACTION_NORMAL = 2, ACTION_JUMP = 3;
List<Block[]> objects = new LinkedList<Block[]>();
Landscape landscape = null;
Player player = null;
boolean up = false, right = false, left = false, running = true;;
Image buffer = null, imgGras, imgWater, imgPlayer_NORMAL, imgPlayer_JUMP, imgPlayer_RIGHT, imgPlayer_LEFT, imgBackground,imgEnemie;
int action = ACTION_NORMAL;
Thread game = null;

	public GUI(int pWidth, int pHeight, int pLength){		
		landscape = new Landscape(new File("bigmap.lvl"),this,pWidth, pHeight, pLength);
		
		imgBackground = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/tinybgj.jpg"));
		imgGras = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras.jpg"));
		imgPlayer_NORMAL = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_normal.gif"));
		imgPlayer_JUMP = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_jump.gif"));
		imgPlayer_RIGHT = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_right.gif"));
		imgPlayer_LEFT = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_left.gif"));
		imgEnemie = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy.png"));
		imgWater = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/water.gif"));
		addKeyListener(this);
		
		game = new Thread(){
			public void run(){
				while(running){
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(up){
						action = ACTION_JUMP;
						landscape.jump();
					}
					if(right){
						action = ACTION_RIGHT;
						landscape.right();
					}
					if(left){
						action = ACTION_LEFT;
						landscape.left();
					}
				}
			}
		};
	}

	public void start(){
		getGraphics().drawImage(imgBackground,0,0, getWidth(), getHeight(),this);
		Landscape.pause();
		requestFocus();
		running = true;
		game.start();
		landscape.start();
	}

	public void stop(){
		running = false;
	}
	
	@Override
	public void update(Graphics g){
		paint(g);
	}
	
	@Override
	public void paint(Graphics g){
		if(buffer==null){
			buffer = createVolatileImage(getWidth(),getHeight());
		}
		Graphics bufG= buffer.getGraphics();
		
		//ToDO: run loading sequenz just as long as imgBackground is buffering
		
		//LOADING SEQUENZ
//		bufG.setColor(Color.BLUE);
//		bufG.fillRect(0, 0, 1280, 800);
//		bufG.setColor(Color.BLACK);
//		Font fontBIG = new Font("Verdana", Font.BOLD, 64);
//		Font fontSMALL = new Font("Verdana", Font.BOLD, 12);
//		bufG.setFont(fontBIG);
//		bufG.drawString("LOADING", 400, 300);
//		bufG.setFont(fontSMALL);
		//END OF LOADING SEQUENZ
		
		objects = landscape.getVisibleBlocks();
		bufG.drawImage(imgBackground,0,0, getWidth(), getHeight(),this);
		bufG.setColor(Color.BLACK);

		for(Block column[]:objects){
			for(Block block:column){
				if(block != null){
					switch (block.getType()){
						case Block.TYP_GRAS:{
							bufG.drawImage(imgGras, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}
						case Block.TYP_WATER:{
							bufG.drawImage(imgWater, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}
					}
					//bufG.drawString(""+block.getX()+" "+block.getY(), block.getX(), block.getY());
				}				
			}
		}
		for(Enemy e:landscape.getEnemies()){
			if(e.isDead()){
				//draw dead enemy
			}else{
				bufG.drawImage(imgEnemie, e.getX(), e.getY(), e.getWidth(), e.getHeight(), this);
			}
		}
		player = landscape.getPlayer();
		paintPlayer(bufG);
		g.drawImage(buffer,0,0,this);
	}

	private void paintPlayer(Graphics g){
		switch(action){
			case ACTION_JUMP:{
				g.drawImage(imgPlayer_JUMP, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);	
				break;
			}
			case ACTION_NORMAL:{
				g.drawImage(imgPlayer_NORMAL, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);	
				break;
			}
			case ACTION_RIGHT:{
				g.drawImage(imgPlayer_RIGHT, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
				break;
			}
			case ACTION_LEFT:{
				g.drawImage(imgPlayer_LEFT, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
				break;
			}
		}
	}
	
	@Override
	public void update(int pChange) {
		repaint();
	}
	
	@Override
	public void keyPressed(final KeyEvent arg0) {
		switch (arg0.getKeyCode()){
			case KeyEvent.VK_UP:{
				up = true;
				break;
			}
			case KeyEvent.VK_LEFT:{
				left = true;
				break;
			}
			case KeyEvent.VK_RIGHT:{
				right = true;
				break;
			}
			case KeyEvent.VK_R:{
				player.reset();
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch (arg0.getKeyCode()){
			case KeyEvent.VK_UP:{
				up = false;
				break;
			}
			case KeyEvent.VK_LEFT:{
				left = false;
				break;
			}
			case KeyEvent.VK_RIGHT:{
				right = false;
				break;
			}
		}
		action = ACTION_NORMAL;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
