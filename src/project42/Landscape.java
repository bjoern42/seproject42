package project42;

import java.io.File;

public class Landscape {
final int sizeX = 12,sizeY = 8;
int width,height;

LevelLoader loader = null;
Player player = null;
Observer objects = new Observer();

	public static void main(String[] args) {
		new Landscape(1200,800);
	}
	
	public Landscape(int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		player = new Player(0, 0, width/sizeX, height/sizeY);
		loader = new LevelLoader(new File("map.lvl"));
		for(int i=0; i<sizeX; i++){
			int blockType[] = loader.readNext();
			GameObject block[] = new Block[sizeY];
			for(int j=0; j<sizeY; j++){
				int size = width/sizeX;
				block[j] = new Block(size*i, size*j, size,blockType[j]);
			}
			objects.add(block);
		}

		objects.update();
		player.update();
	}
}
