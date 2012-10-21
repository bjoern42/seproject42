package project42;

import java.io.File;
import java.util.LinkedList;

public class Observer implements Observable {
LinkedList<Block[]> objects = new LinkedList<Block[]>();
int start, length;
	
	public Observer(int size, int pSizeX, int pSizeY){
		start = 0;
		length = pSizeX;
		
		LevelLoader loader = new LevelLoader(new File("map.lvl"));

		int blockType[] = null;
		int i = 0;
		while((blockType = loader.readNext()) != null){
			i++;
			Block block[] = new Block[pSizeY];
			for(int j=0; j<pSizeY; j++){
				block[j] = new Block(size*i, size*j, size,blockType[j]);
			}
			objects.add(block);
		}

	}
	
	public void removeFirst(){
		if(start < objects.size()){
			start++;
		}
	}
	
	public void removeLast(){
		if(start > 0){
			start--;
		}
	}
	
	public int getLastX(){
		return objects.getLast()[0].getX();
	}
	
	@Override
	public void update(int pChange) {
		for(int i=start; i < start+length;i++){
			Block block[] = objects.get(i);
			for(int j=0; j<block.length; j++){
				block[j].update(pChange);
			}
			System.out.println();
		}
		System.out.println("------------------------------------------");
	}
	
	public boolean isMovableArea(int pX, int pY, int pWidth, int pHeight){
		boolean movable = true;
		for(Block block[] : objects){
			for(int i=0; i<block.length; i++){
				if( block[i].getType() == Block.TYP_GRAS && block[i].isInArea(pX, pY, pWidth, pHeight)){
					movable = false;
				}
			}
		}
		return movable;
	}
}
