package project42;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class TUI implements Observable {
List<Block[]> objects = new LinkedList<Block[]>();
Landscape landscape = null;	
Player player = null;

	public static void main(String[] args) {
		new TUI().test();
	}
	
	public TUI(){
		landscape = new Landscape(null,new File("mapTUI.lvl"),this,800, 800,4);
	}
	
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
	
	@Override
	public void update(int pChange) {
		objects = landscape.getVisibleBlocks();
		player = landscape.getPlayer();
		System.out.println("Player: "+player.getX()+" "+player.getY());
		for(int i=0;i<objects.size();i++){
			int rowX = objects.get(i)[0].getX();
			System.out.print(rowX+"\t");
			for(Block block:objects.get(i)){
				System.out.print(block);
			}
//			if(i==7){
//				System.out.print("<-player");
//			}
			if(rowX-player.getX() < player.getWidth() && rowX-player.getX() > -player.getWidth()){
				System.out.print("<-player");
			}
			System.out.println();
		}
		System.out.println("------------------------------------------");
	}
}