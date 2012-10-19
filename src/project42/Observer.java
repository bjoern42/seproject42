package project42;

import java.util.LinkedList;

public class Observer implements Observable {
LinkedList<Block[]> objects = new LinkedList<Block[]>();
Player player = null;

	public Observer(Player pPlayer){
		player  = pPlayer;
	}
	
	public void add(Block[] object){
		objects.add(object);
	}
	
	public void removeFirst(){
		objects.removeFirst();
	}
	
	public int getSize(){
		return objects.size();
	}
	
	@Override
	public void update() {
		boolean inArea = false;
		for(Block block[] : objects){
			for(int i=0; i<block.length; i++){
				block[i].update();
				if( block[i].getType() == Block.TYP_GRAS && block[i].isInArea(player.getX(), player.getY(), player.getWidth(), player.getHeight())){
					inArea = true;
				}
			}
		}
		if(!inArea){
			player.move(0, 10);
		}else{
			System.out.println(player.getY());
		}
	}
}
