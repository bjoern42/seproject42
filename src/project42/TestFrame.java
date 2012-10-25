package project42;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class TestFrame extends JFrame implements KeyListener, Observable{
List<Block[]> objects = new LinkedList<Block[]>();
Landscape landscape = null;
Player player = null;
Image buffer = null;
boolean up = false, right = false, left = false;

	public static void main(String[] args) {
		new TestFrame(1200, 800);
	}
	
	public TestFrame(int pWidth, int pHeight){
		super("TestFrame");
		setSize(pWidth, pHeight);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		landscape = new Landscape(this,pWidth, pHeight);
		setVisible(true);
		addKeyListener(this);
		
		while(true){
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(up){
				landscape.jump();
			}
			if(right){
				landscape.right();
			}
			if(left){
				landscape.left();
			}
		}
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
		Image img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/background.png"));
		bufG.drawImage(img,0,0, getWidth(), getHeight(),this);

		for(Block column[]:objects){
			for(Block block:column){
				switch (block.getType()){
					case Block.TYP_GRAS:{
						img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras2.jpg"));
						bufG.drawImage(img, block.getX(), block.getY(), block.getWidth(), block.getHeight(), this);
						bufG.drawString(""+block.getX()+","+block.getY(), block.getX(), block.getY());
						break;
					}
				}
				
			}
		}
		img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/player.png"));
		player = landscape.getPlayer();
//		bufG.drawString(""+player.getX()+","+player.getY(), player.getX(), player.getY());
		bufG.drawImage(img, player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
		
		g.drawImage(buffer,0,0,this);
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
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}