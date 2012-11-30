package de.htwg.project42.view;

import java.io.File;
import java.util.List;


import de.htwg.project42.controller.Landscape;
import de.htwg.project42.model.Block;
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
		new TUI();
	}
	
	/**
	 * Creates TUI and starts tests.
	 */
	public TUI(){
		landscape = new Landscape(new File("mapTUI.lvl"),this,LANDSCAPE_SIZE, LANDSCAPE_SIZE,LANDSCAPE_LENGTH);
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
		print("Player: "+player.getX()+" "+player.getY()+"\n");
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
	}
	
	/**
	 * Prints given String.
	 * @param s - String
	 */
	private void print(String s){
		System.out.print(s);
	}
}
