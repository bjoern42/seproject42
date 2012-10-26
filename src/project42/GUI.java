package project42;

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
public class GUI extends JPanel implements KeyListener, Observable{
final int ACTION_RIGHT = 0, ACTION_LEFT = 1, ACTION_NORMAL = 2, ACTION_JUMP = 3;
List<Block[]> objects = new LinkedList<Block[]>();
Landscape landscape = null;
Player player = null;
boolean up = false, right = false, left = false;
Image buffer = null, imgGras, imgPlayer_NORMAL, imgPlayer_JUMP, imgPlayer_RIGHT, imgPlayer_LEFT, imgBackground;
int action = ACTION_NORMAL;
Thread game = null;

	public GUI(int pWidth, int pHeight, int pLength){		
		landscape = new Landscape(new File("map.lvl"),this,pWidth, pHeight, pLength);
		
		imgBackground = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/background.png"));
		imgGras = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras.jpg"));
		imgPlayer_NORMAL = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_normal.gif"));
		imgPlayer_JUMP = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_jump.gif"));
		imgPlayer_RIGHT = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_right.gif"));
		imgPlayer_LEFT = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_left.gif"));
		
		addKeyListener(this);
		
		game = new Thread(){
			public void run(){
				while(true){
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
		requestFocus();
		game.start();
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
		objects = landscape.getVisibleBlocks();
		bufG.drawImage(imgBackground,0,0, getWidth(), getHeight(),this);

		for(Block column[]:objects){
			for(Block block:column){
				switch (block.getType()){
					case Block.TYP_GRAS:{
						bufG.drawImage(imgGras, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
						break;
					}
				}
				
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