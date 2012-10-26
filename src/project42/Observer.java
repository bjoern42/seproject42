package project42;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Observer implements Observable {
LinkedList<Block[]> objects = new LinkedList<Block[]>();
int start, length, size;

	public Observer(File map, int pSize, int pLength){
		start = 0;
		length = pLength+1;
		size = pSize;
		LevelLoader loader = new LevelLoader(map);
		int blockType[] = null;
		int i = 0;
		while((blockType = loader.readNext()) != null){
			Block block[] = new Block[blockType.length];
			for(int j=0; j<blockType.length; j++){
				block[j] = new Block(size*i, size*j, size,blockType[j]);
			}
			objects.add(block);
			i++;
		}
	}
	
	public List<Block[]> getVisibleBlocks(){
		return objects.subList(start, start+length);
	}
	
	public void removeFirst(){
		if(start+length < objects.size()){
			int x = (length-1)*size;
			for(Block b:objects.get(start+length)){
				b.setX(x);
			}
			start++;
		}
	}
	
	public void removeLast(){
		if(start > 0){
			int x = 0;
			for(Block b:objects.get(start)){
				b.setX(x);
			}
			start--;
		}
	}

	@Override
	public void update(int pChange) {
		if(objects.get(start)[0].getX() < -size-pChange){
			removeFirst();
		}else if(objects.get(start+length-1)[0].getX() > (length-1)*size-pChange){
			removeLast();
		}
		for(int i=start; i < start+length;i++){
			Block block[] = objects.get(i);
			for(int j=0; j<block.length; j++){
				block[j].update(pChange);
			}
		}
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