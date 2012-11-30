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
private Landscape landscape = null;	

	public static void main(String[] args) {
		new TUI();
	}
	
	/**
	 * Creates TUI and starts tests.
	 */
	public TUI(){
		landscape = new Landscape(new File("mapTUI.lvl"),this,800, 800,5);
		landscape.start();
		test();
	}
	
	/**
	 * Testruns.
	 */
	private void test(){
		landscape.jump();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<100;i++){
			landscape.right();
		}
		for(int i=0;i<100;i++){
			landscape.left();
		}
		landscape.jump();
	}
	
	/**
	 * Prints the landscape.
	 */
	@Override
	public void update(int pChange) {
		List<Block[]> objects = landscape.getVisibleBlocks();
		Player player = landscape.getPlayer();
		System.out.println("Player: "+player.getX()+" "+player.getY());
		for(int i=0;i<objects.size();i++){
			int rowX = objects.get(i)[0].getX();
			System.out.print(rowX+"\t");
			for(Block block:objects.get(i)){
				System.out.print(block);
			}
			if(rowX-player.getX() < player.getWidth() && rowX-player.getX() > -player.getWidth()){
				System.out.print("<-player");
			}
			System.out.println();
		}
		System.out.println("------------------------------------------");
	}
}
