package de.htwg.project42.view;

import java.io.File;
import java.util.List;


import de.htwg.project42.controller.Landscape;
import de.htwg.project42.model.Block;
import de.htwg.project42.model.Enemy;
import de.htwg.project42.model.Player;
import de.htwg.project42.observer.Observable;

/**
 * TUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class TUI implements Observable {
private static final int LANDSCAPE_SIZE = 800, LANDSCAPE_LENGTH = 5, START_PAUSE = 2000, WALK_CYCLES = 100;
private Landscape landscape = null;	

	public static void main(String[] args) {
		new TUI(new File("mapTUI.lvl"), LANDSCAPE_SIZE, LANDSCAPE_SIZE, LANDSCAPE_LENGTH);
	}
	
	/**
	 * Creates TUI.
	 */
	public TUI(Landscape pLandscape){
		landscape = pLandscape;
		landscape.activateTUI(this);
	}
	
	/**
	 * Creates TUI and starts tests.
	 */
	public TUI(File map,int pSizeX, int pSizeY, int pLength){
		landscape = new Landscape(map, this, pSizeX, pSizeY, pLength);
		landscape.start();
		test();
	}
	
	/**
	 * Testruns.
	 */
	private void test(){
		landscape.jump();
		try {
			Thread.sleep(START_PAUSE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<WALK_CYCLES;i++){
			landscape.right();
		}
		for(int i=0;i<WALK_CYCLES;i++){
			landscape.left();
		}
		landscape.jump();
	}
	
	/**
	 * Prints the landscape.
	 */
	@Override
	public void update() {
		List<Block[]> objects = landscape.getVisibleBlocks();
		Player player = landscape.getPlayer();
		
		for(int i=0;i<objects.size();i++){
			int rowX = objects.get(i)[0].getX();
			print(rowX+"\t");
			for(Block block:objects.get(i)){
				print(block.toString());
			}
			if(rowX-player.getX() < player.getWidth() && rowX-player.getX() > -player.getWidth()){
				print("<-player");
			}
			print("\n");
		}
		print("------------------------------------------\n");
		print("Player: "+player.getX()+" "+player.getY()+"\n");
		List<Enemy> list = landscape.getEnemies();
		for(int i=0; i<list.size() ;i++){
			Enemy e = list.get(i);
			if(e.isDead()){
				print("Enemy "+i+" died at: "+e.getX()+" "+e.getY()+"\n");
			}else{
				print("Enemy "+i+" is at: "+e.getX()+" "+e.getY()+"\n");
			}
		}
	}
	
	/**
	 * Prints given String.
	 * @param s - String
	 */
	private void print(String s){
		System.out.print(s);
	}
}
