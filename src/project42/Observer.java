package project42;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Observer implements Observable {
LinkedList<Block[]> objects = new LinkedList<Block[]>();
int start, length, size, sizeX;

	public Observer(int pSize, int pSizeX, int pSizeY){
		start = 0;
		length = pSizeX+1;
		size = pSize;
		sizeX = pSizeX;
		LevelLoader loader = new LevelLoader(new File("map.lvl"));

		int blockType[] = null;
		int i = 0;
		while((blockType = loader.readNext()) != null){
			Block block[] = new Block[pSizeY];
			for(int j=0; j<pSizeY; j++){
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
			int x = sizeX*size;
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
	
	public int getLastX(){
		return objects.getLast()[0].getX();
	}
	
	@Override
	public void update(int pChange) {
		System.out.println(objects.get(start)[0].getX());
		if(objects.get(start)[0].getX() <= -size){
			removeFirst();
		}else if(objects.get(start+length-1)[0].getX() >= (length-1)*size){
			removeLast();
		}
		for(int i=start; i < start+length;i++){
			Block block[] = objects.get(i);
			System.out.print(block[0].getX()+"\t");
			for(int j=0; j<block.length; j++){
				block[j].update(pChange);
			}
			if(i==start+6){
				System.out.print("<-player");
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