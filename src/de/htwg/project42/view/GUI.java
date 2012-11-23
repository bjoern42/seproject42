package de.htwg.project42.view;

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


import de.htwg.project42.controller.Landscape;
import de.htwg.project42.model.Block;
import de.htwg.project42.model.Enemy;
import de.htwg.project42.model.Player;
import de.htwg.project42.observer.Observable;

/**
 * GUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@SuppressWarnings("serial")
public final class GUI extends JPanel implements KeyListener, Observable{
private final int ACTION_RIGHT = 0, ACTION_LEFT = 1, ACTION_NORMAL = 2, ACTION_JUMP = 3;
private List<Block[]> objects = new LinkedList<Block[]>();
private Landscape landscape = null;
private Player player = null;
private boolean up = false, right = false, left = false;
private Image buffer = null, imgGras, imgPlayer_NORMAL, imgPlayer_JUMP, imgPlayer_RIGHT, imgPlayer_LEFT, imgBackground,imgEnemie,imgEnemieDead,imgWater,imgHealth,imgCoin,imgCoinCount;
private int action = ACTION_NORMAL;
private JumpNRun main = null;
private GUI gui;

	/**
	 * Creates GUI.
	 * @param pMain - JumpNRun
	 * @param map - Game map
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @param pLength - Visible blocks
	 */
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

	/**
	 * Starts Game.
	 */
	public void start(){
		getGraphics().drawImage(imgBackground,0,0, getWidth(), getHeight(),this);
		Landscape.pause(100);
		requestFocus();
		landscape.start();
		new Thread(){
			public void run(){
				while(player.getHealth() > 0){
					Landscape.pause(20);
					if(up){
						action = ACTION_JUMP;
						landscape.jump();
					}if(right){
						action = ACTION_RIGHT;
						landscape.right();
					}if(left){
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
	
	/**
	 * Draws all components. (Double buffered)
	 */
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

	/**
	 * Draws the player.
	 * @param g - Graphics
	 */
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
	
	/**
	 * Paint components.
	 */
	@Override
	public void update(int pChange) {
		repaint();
	}
	
	/**
	 * Key handling.
	 */
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

	/**
	 * Key handling.
	 */
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
