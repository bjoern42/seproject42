package project42;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public final class Observer implements Observable {
LinkedList<Block[]> objects = new LinkedList<Block[]>();
LinkedList<Enemy> enemies = new LinkedList<Enemy>();
int start, length, size, change = 0;

	public Observer(File map, int pSize, int pLength){
		start = 0;
		length = pLength+2;
		size = pSize;
		LevelLoader loader = new LevelLoader(map);
		int blockType[] = null;
		int i = 0;
		while((blockType = loader.readNext()) != null){
			Block block[] = new Block[blockType.length];
			for(int j=0; j<blockType.length; j++){
				if(blockType[j] == Block.TYP_ENEMY){
					enemies.add(new Enemy(size*i, size*j, size,blockType[j]));
					blockType[j] = Block.TYP_AIR;
				}
				block[j] = new Block(size*i, size*j, size,blockType[j]);
			}
			objects.add(block);
			i++;
		}
	}
	
	public List<Block[]> getVisibleBlocks(){
		if(start+length>objects.size()){
			return objects;
		}
		return objects.subList(start, start+length);
	}
	
	public List<Enemy> getEnemies(){
		return enemies;
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

	public boolean isInFrame(int pX){
		if(pX > -size && pX < (length-1)*size){
			return true;
		}
		return false;
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
		for(Enemy e:enemies){
			e.update(pChange);
		}
		change += pChange*-1;
	}
	
	public boolean isMovableArea(int pX, int pY, int pWidth, int pHeight){
		int x = (change+pX) / size, y = pY / size;
		for(int i=-1;i<=2;i++){
			if(x+i >= 0 && x+i < objects.size()){
				Block block[] = objects.get(x+i);
				for(int j=-1;j<=pHeight/size;j++){
					if(y+j >= 0 && y+j<block.length && block[y+j].isInArea(pX, pY, pWidth, pHeight) && block[y+j].getType() == Block.TYP_GRAS){
						return false;
					}
				}
			}
		}
		return true;
//		for(Block block[] : objects){
//			for(int i=0; i<block.length; i++){
//				if(block[i].isInArea(pX, pY, pWidth, pHeight) && block[i].getType() == Block.TYP_GRAS){
//					return false;
//				}
//			}
//		}
	}
}
