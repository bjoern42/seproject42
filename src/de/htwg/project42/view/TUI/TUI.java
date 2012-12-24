package de.htwg.project42.view.TUI;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.EnemyInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;
import de.htwg.project42.observer.Observable;

import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.Player;
import de.htwg.project42.controller.Implementation.Landscape;

/**
 * TUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class TUI implements Observable {
private static final int LANDSCAPE_SIZE = 800, LANDSCAPE_LENGTH = 5, WALK_CYCLES = 100;
private LandscapeInterface landscape = null;
private Logger logger = Logger.getLogger("de.htwg.project42.view.TUI");

	public static void main(String[] args) {
		new TUI(null).test();
	}
	
	/**
	 * Creates TUI.
	 */
	public TUI(LandscapeInterface pLandscape){
		if(pLandscape == null){
			// Set up logging through log4j
			PropertyConfigurator.configure("log4j.properties");
			
			int size = LANDSCAPE_SIZE/LANDSCAPE_LENGTH;
			PlayerInterface player = new Player((LANDSCAPE_SIZE-size)/2, 0, size, size*2);
			LevelInterface level = new Level(size, LANDSCAPE_LENGTH-2);
			
			landscape = new Landscape(player, level, LANDSCAPE_SIZE, LANDSCAPE_SIZE);
			landscape.loadLevel(new File("mapTUI.lvl"));
		}else{
			landscape = pLandscape;
		}
		landscape.addAnObserver(this);
	}
		
	/**
	 * Testruns.
	 */
	private void test(){
		int size = LANDSCAPE_SIZE/LANDSCAPE_LENGTH;
		landscape.start();
		
		while(landscape.getPlayer().getY() != size);
		landscape.jump();
		while(landscape.getPlayer().getY() != size);
		
		for(int i=0;i<WALK_CYCLES;i++){
			landscape.right();
		}
		for(int i=0;i<WALK_CYCLES;i++){
			landscape.left();
		}
		landscape.getPlayer().setHealth(0);
	}
	
	/**
	 * Prints the landscape.
	 */
	@Override
	public void update() {
		List<BlockInterface[]> objects = landscape.getVisibleBlocks();
		PlayerInterface player = landscape.getPlayer();
		
		print("Player: "+player.getX()+" "+player.getY()+"\n");
		List<EnemyInterface> list = landscape.getEnemies();
		for(int i=0; i<list.size() ;i++){
			EnemyInterface e = list.get(i);
			if(e.isDead()){
				print("Enemy "+i+" died at: "+e.getX()+" "+e.getY()+"\n");
			}else{
				print("Enemy "+i+" is at: "+e.getX()+" "+e.getY()+"\n");
			}
		}
		
		for(int i=0;i<objects.size();i++){
			int rowX = objects.get(i)[0].getX();
			print(rowX+"\t");
			for(BlockInterface block:objects.get(i)){
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
		logger.info(s);
//		System.out.print(s);
	}
}
