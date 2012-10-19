package project42;

import java.io.File;

public class Landscape {
final int sizeX = 12,sizeY = 8;
int width,height;

LevelLoader loader = null;
Player player = null;
Observer objects = null;

	public static void main(String[] args) {
		new Landscape(1200,800);
	}
	
	public Landscape(int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		player = new Player(5, 20, width/sizeX, height/sizeY);
		objects = new Observer(player);
		loader = new LevelLoader(new File("map.lvl"));
		for(int i=0; i<sizeX; i++){
			int blockType[] = loader.readNext();
			Block block[] = new Block[sizeY];
			for(int j=0; j<sizeY; j++){
				int size = width/sizeX;
				System.out.println(size);
				block[j] = new Block(size*i, size*j, size,blockType[j]);
			}
			objects.add(block);
		}

		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			objects.update();
			player.update();
		}
	}
}