package project42;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public final class GUI extends JPanel implements KeyListener, Observable{
final int ACTION_RIGHT = 0, ACTION_LEFT = 1, ACTION_NORMAL = 2, ACTION_JUMP = 3;
List<Block[]> objects = new LinkedList<Block[]>();
Landscape landscape = null;
Player player = null;
boolean up = false, right = false, left = false;
Image buffer = null, imgGras, imgPlayer_NORMAL, imgPlayer_JUMP, imgPlayer_RIGHT, imgPlayer_LEFT, imgBackground,imgEnemie,imgEnemieDead,imgWater,imgHealth,imgCoin,imgCoinCount;
int action = ACTION_NORMAL;
JumpNRun main = null;
GUI gui;

	public GUI(final JumpNRun pMain, File map,int pWidth, int pHeight, int pLength){		
		landscape = new Landscape(map,this,pWidth, pHeight, pLength);
		player = landscape.getPlayer();
		main = pMain;
		gui = this;
		
		imgBackground = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/background.png"));
		imgGras = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras.jpg"));
		imgPlayer_NORMAL = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_normal.gif"));
		imgPlayer_JUMP = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_jump.gif"));
		imgPlayer_RIGHT = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_right.gif"));
		imgPlayer_LEFT = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_left.gif"));
		imgEnemie = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy.gif"));
		imgEnemieDead = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy_dead.png"));
		imgWater = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/water.png"));
		imgHealth = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/health.png"));
		imgCoin = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coin.png"));
		imgCoinCount = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coinCount.png"));
		addKeyListener(this);
	}

	public void start(){
		getGraphics().drawImage(imgBackground,0,0, getWidth(), getHeight(),this);
		Landscape.pause();
		requestFocus();
		landscape.start();
		new Thread(){
			public void run(){
				while(player.getHealth() > 0){
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
				JOptionPane.showMessageDialog(gui, "Player died!", "Game over!", JOptionPane.OK_OPTION);
				main.reset();
			}
		}.start();
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

		//paint landscape
		for(Block column[]:objects){
			for(Block block:column){
				if(block != null){
					switch (block.getType()){
						case Block.TYP_GRAS:{
							bufG.drawImage(imgGras, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}case Block.TYP_WATER:{
							bufG.drawImage(imgWater, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}case Block.TYP_COIN:{
							bufG.drawImage(imgCoin, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}
					}
//					bufG.drawString(""+block.getX()+" "+block.getY(), block.getX(), block.getY());
				}				
			}
		}
		//paint enemies
		for(Enemy e:landscape.getEnemies()){
			if(e.isDead()){
				bufG.drawImage(imgEnemieDead, e.getX(), e.getY(), e.getWidth(), e.getHeight(), this);
			}else{
				bufG.drawImage(imgEnemie, e.getX(), e.getY(), e.getWidth(), e.getHeight(), this);
			}
		}
		//paint player
		paintPlayer(bufG);
		for(int i=0;i<player.getHealth();i++){
			bufG.drawImage(imgHealth, 10+i*30, 10, 30, 30, this);
		}
		bufG.drawImage(imgCoinCount, getWidth()-50, 10, 50, 40, this);
		bufG.setFont(new Font("Verdana", Font.BOLD, 15));
		bufG.drawString(""+player.getCoins(),getWidth()-35, 35);
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
