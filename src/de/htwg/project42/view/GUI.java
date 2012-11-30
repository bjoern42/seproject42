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
import de.htwg.project42.model.GameObject;
import de.htwg.project42.model.Player;
import de.htwg.project42.observer.Observable;

/**
 * GUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@SuppressWarnings("serial")
public final class GUI extends JPanel implements KeyListener, Observable{
private static final int ACTION_RIGHT = 0, ACTION_LEFT = 1, ACTION_NORMAL = 2, ACTION_JUMP = 3, PAUSE_LONG = 100, PAUSE_SHORT = 20, HEALTH_SIZE = 30, COIN_SIZE = 50, COIN_STRING_POS =35, GAP = 10, FONT_SIZE = 15;
private List<Block[]> objects = new LinkedList<Block[]>();
private Landscape landscape = null;
private Player player = null;
private boolean up = false, right = false, left = false;
private Image buffer = null, img_gras, img_player_normal, img_player_jump, img_player_right, img_player_left, img_background,img_enemie,img_enemie_dead,img_water,img_health,img_coin,img_coin_count;
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
		
		img_background = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/background.png"));
		img_gras = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras.jpg"));
		img_player_normal = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_normal.gif"));
		img_player_jump = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_jump.gif"));
		img_player_right = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_right.gif"));
		img_player_left = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_left.gif"));
		img_enemie = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy.gif"));
		img_enemie_dead = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy_dead.png"));
		img_water = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/water.png"));
		img_health = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/health.png"));
		img_coin = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coin.png"));
		img_coin_count = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coinCount.png"));
		addKeyListener(this);
	}

	/**
	 * Starts Game.
	 */
	public void start(){
		getGraphics().drawImage(img_background,0,0, getWidth(), getHeight(),this);
		GameObject.pause(PAUSE_LONG);
		requestFocus();
		landscape.start();
		new Thread(){
			public void run(){
				while(player.getHealth() > 0){
					GameObject.pause(PAUSE_SHORT);
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
		bufG.drawImage(img_background,0,0, getWidth(), getHeight(),this);

		paintLandscape(bufG);
		paintEnemies(bufG);
		paintPlayer(bufG);
		paintOverlay(bufG);
		
		g.drawImage(buffer,0,0,this);
	}

	/**
	 * Draws the landscape.
	 * @param g - Graphics
	 */
	private void paintLandscape(Graphics g){
		for(Block column[]:objects){
			for(Block block:column){
				if(block != null){
					switch (block.getType()){
						case Block.TYP_GRAS:{
							g.drawImage(img_gras, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}case Block.TYP_WATER:{
							g.drawImage(img_water, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}case Block.TYP_COIN:{
							g.drawImage(img_coin, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}
					}
				}				
			}
		}
	}

	/**
	 * Draws the enemies.
	 * @param g - Graphics
	 */
	private void paintEnemies(Graphics g){
		for(Enemy e:landscape.getEnemies()){
			if(e.isDead()){
				g.drawImage(img_enemie_dead, e.getX(), e.getY(), e.getWidth(), e.getHeight(), this);
			}else{
				g.drawImage(img_enemie, e.getX(), e.getY(), e.getWidth(), e.getHeight(), this);
			}
		}
	}
	
	/**
	 * Draws the overlay.
	 * @param g - Graphics
	 */
	private void paintOverlay(Graphics g){
		for(int i=0;i<player.getHealth();i++){
			g.drawImage(img_health, GAP+i*HEALTH_SIZE, GAP, HEALTH_SIZE, HEALTH_SIZE, this);
		}
		g.drawImage(img_coin_count, getWidth()-COIN_SIZE, GAP, COIN_SIZE, COIN_SIZE-GAP, this);
		g.setFont(new Font("Verdana", Font.BOLD, FONT_SIZE));
		g.drawString(""+player.getCoins(),getWidth()-COIN_STRING_POS, COIN_STRING_POS);
	}
	
	/**
	 * Draws the player.
	 * @param g - Graphics
	 */
	private void paintPlayer(Graphics g){
		switch(action){
			case ACTION_JUMP:{
				g.drawImage(img_player_jump, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);	
				break;
			}
			case ACTION_NORMAL:{
				g.drawImage(img_player_normal, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);	
				break;
			}
			case ACTION_RIGHT:{
				g.drawImage(img_player_right, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
				break;
			}
			case ACTION_LEFT:{
				g.drawImage(img_player_left, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
				break;
			}
		}
	}
	
	/**
	 * Paint components.
	 */
	@Override
	public void update() {
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
