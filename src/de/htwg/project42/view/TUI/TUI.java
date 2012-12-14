package de.htwg.project42.view.TUI;

import java.io.File;
import java.util.List;


import de.htwg.project42.controller.iLandscape;
import de.htwg.project42.model.GameObjects.iBlock;
import de.htwg.project42.model.GameObjects.iEnemy;
import de.htwg.project42.model.GameObjects.iLevel;
import de.htwg.project42.model.GameObjects.iLevelLoader;
import de.htwg.project42.model.GameObjects.iPlayer;
import de.htwg.project42.observer.Observable;

import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;
import de.htwg.project42.model.GameObjects.Implementation.Player;
import de.htwg.project42.controller.Implementation.Landscape;

/**
 * TUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class TUI implements Observable {
private static final int LANDSCAPE_SIZE = 800, LANDSCAPE_LENGTH = 5, START_PAUSE = 2000, WALK_CYCLES = 100;
private iLandscape landscape = null;
private iPlayer player;

	public static void main(String[] args) {
		new TUI(null).test();
	}
	
	/**
	 * Creates TUI.
	 */
	public TUI(iLandscape pLandscape){
		if(pLandscape == null){
			int size = LANDSCAPE_SIZE/LANDSCAPE_LENGTH;
			player = new Player((LANDSCAPE_LENGTH-1)*size/2, 0, size, size*2);
			iLevelLoader loader = new LevelLoader(new File("mapTUI.lvl"));
			iLevel level = new Level(loader, player, size, LANDSCAPE_LENGTH+2);
			landscape = new Landscape(player, level, LANDSCAPE_SIZE, LANDSCAPE_SIZE);
		}else{
			landscape = pLandscape;
		}
		landscape.addAnObserver(this);
	}
		
	/**
	 * Testruns.
	 */
	private void test(){
		landscape.start();
		
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
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		landscape.jump();
	}
	
	/**
	 * Prints the landscape.
	 */
	@Override
	public void update() {
		List<iBlock[]> objects = landscape.getVisibleBlocks();
		iPlayer player = landscape.getPlayer();
		
		for(int i=0;i<objects.size();i++){
			int rowX = objects.get(i)[0].getX();
			print(rowX+"\t");
			for(iBlock block:objects.get(i)){
				print(block.toString());
			}
			if(rowX-player.getX() < player.getWidth() && rowX-player.getX() > -player.getWidth()){
				print("<-player");
			}
			print("\n");
		}
		print("------------------------------------------\n");
		print("Player: "+player.getX()+" "+player.getY()+"\n");
		List<iEnemy> list = landscape.getEnemies();
		for(int i=0; i<list.size() ;i++){
			iEnemy e = list.get(i);
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
