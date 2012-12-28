package de.htwg.project42.view.GUI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.ButtonInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.GateInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.observer.Observable;
import de.htwg.project42.view.TUI.TUI;

/**
 * GUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@SuppressWarnings("serial")
public final class GUI extends JPanel implements KeyListener, Observable{
private static final int ACTION_RIGHT = 0, ACTION_LEFT = 1, ACTION_NORMAL = 2, ACTION_JUMP = 3, PAUSE_LONG = 100, PAUSE_SHORT = 20, HEALTH_SIZE = 30, COIN_SIZE = 50, COIN_STRING_POS =35, GAP = 10, FONT_SIZE = 15;
private List<BlockInterface[]> objects = new LinkedList<BlockInterface[]>();
private LandscapeInterface landscape = null;
private PlayerInterface player = null;
private boolean up = false, right = false, left = false;
private Image buffer = null;
private Image imgPlayerNormal, imgPlayerJump, imgPlayerRight, imgPlayerLeft;
private Image imgBackground ,imgHealth, imgCoin, imgCoinCount;
private Image imgEnemie,imgEnemieDead;
private Image imgGras, imgWater, imgGoal, imgCrate, imgButtonPressed, imgButtonReleased,imgGateClosed, imgGateOpened;
private int action = ACTION_NORMAL;
private MainMenuGUI main = null;
private GUI gui;
private TUI tui;

	/**
	 * Creates GUI.
	 * @param pMain - Main
	 * @param pLandscape - Landscape
	 */
	public GUI(final MainMenuGUI pMain, LandscapeInterface pLandscape){		
		landscape = pLandscape;
		landscape.addAnObserver(this);
		
		player = landscape.getPlayer();
		main = pMain;
		gui = this;
		
		imgBackground = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/background.png"));
		imgGras = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras.jpg"));
		imgPlayerNormal = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_normal.gif"));
		imgPlayerJump = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_jump.gif"));
		imgPlayerRight = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_right.gif"));
		imgPlayerLeft = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player_left.gif"));
		imgEnemie = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy.gif"));
		imgEnemieDead = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy_dead.png"));
		imgWater = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/water.png"));
		imgHealth = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/health.png"));
		imgCoin = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coin.png"));
		imgCoinCount = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coinCount.png"));
		imgGoal = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/goal.png"));
		imgCrate = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/crate.png"));
		imgButtonPressed = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/buttonPressed.png"));
		imgButtonReleased = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/buttonReleased.png"));
		imgGateClosed = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gateClosed.png"));
		imgGateOpened = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gateOpened.png"));
		
		addKeyListener(this);
	}

	/**
	 * Starts Game.
	 * @param pTUI - runs parallel TUI if true
	 */
	public void start(boolean pTUI){
		getGraphics().drawImage(imgBackground,0,0, getWidth(), getHeight(),this);
		if(pTUI){
			tui = new TUI(landscape);
		}
		up = false;
		right = false;
		left = false;
		player.pause(PAUSE_LONG);
		requestFocus();
		landscape.start();
		new Thread(){
			public void run(){
				while(player.getHealth() > 0 && !player.isInGoal()){
					player.pause(PAUSE_SHORT);
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
				if(player.isInGoal()){
					JOptionPane.showMessageDialog(gui, "You won!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
				}else if(player.getHealth() == 0){
					JOptionPane.showMessageDialog(gui, "Player died!", "Game over!", JOptionPane.OK_OPTION);
				}
				landscape.removeAnObserver(tui);
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

		paintLandscape(bufG);
		paintEnemies(bufG);
		paintCrates(bufG);
		paintPlayer(bufG);
		paintOverlay(bufG);
		
		g.drawImage(buffer,0,0,this);
	}

	/**
	 * Draws the landscape.
	 * @param g - Graphics
	 */
	private void paintLandscape(Graphics g){
		for(BlockInterface column[]:objects){
			for(BlockInterface block:column){
				if(block != null){
					switch (block.getType()){
						case BlockInterface.TYP_GRAS:{
							g.drawImage(imgGras, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}case BlockInterface.TYP_WATER:{
							g.drawImage(imgWater, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}case BlockInterface.TYP_COIN:{
							g.drawImage(imgCoin, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}
						case BlockInterface.TYP_GOAL:{
							g.drawImage(imgGoal, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							break;
						}
						case BlockInterface.TYP_GATE:{
							if(((GateInterface)block).isOn()){
								g.drawImage(imgGateOpened, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							}else{
								g.drawImage(imgGateClosed, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							}
							break;
						}
						case BlockInterface.TYP_BUTTON:{
							if(((ButtonInterface)block).isPressed()){
								g.drawImage(imgButtonPressed, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							}else{
								g.drawImage(imgButtonReleased, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
							}
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
		for(EnemyInterface e:landscape.getEnemies()){
			if(e.isDead()){
				g.drawImage(imgEnemieDead, e.getX(), e.getY(), e.getWidth(), e.getHeight(), this);
			}else{
				g.drawImage(imgEnemie, e.getX(), e.getY(), e.getWidth(), e.getHeight(), this);
			}
		}
	}
	
	
	private void paintCrates(Graphics g){
		for(BlockInterface c:landscape.getCrates()){
			g.drawImage(imgCrate, c.getX(), c.getY(), c.getWidth(), c.getHeight(), this);
		}
	}
	
	/**
	 * Draws the overlay.
	 * @param g - Graphics
	 */
	private void paintOverlay(Graphics g){
		for(int i=0;i<player.getHealth();i++){
			g.drawImage(imgHealth, GAP+i*HEALTH_SIZE, GAP, HEALTH_SIZE, HEALTH_SIZE, this);
		}
		g.drawImage(imgCoinCount, getWidth()-COIN_SIZE, GAP, COIN_SIZE, COIN_SIZE-GAP, this);
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
				g.drawImage(imgPlayerJump, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);	
				break;
			}
			case ACTION_NORMAL:{
				g.drawImage(imgPlayerNormal, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);	
				break;
			}
			case ACTION_RIGHT:{
				g.drawImage(imgPlayerRight, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
				break;
			}
			case ACTION_LEFT:{
				g.drawImage(imgPlayerLeft, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
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
			case KeyEvent.VK_ESCAPE:{
				player.setHealth(0);
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
