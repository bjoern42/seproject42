package project42;

import java.io.File;

public class Landscape {
private final int sizeX = 12,sizeY = 8;
private int width,height;
private GameObject block[][] = new Block[sizeX][sizeY];
private LevelLoader loader = null;

	public static void main(String[] args) {
		new Landscape(1200,800);
	}
	
	public Landscape(int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		loader = new LevelLoader(new File("map.lvl"));
		for(int i=0; i<block.length; i++){
			int blockType[] = loader.readNext();
			for(int j=0; j<block[i].length; j++){
				block[i][j] = new Block(width/sizeX,blockType[j]);
			}
		}
		print();
	}
	
	
	public void print(){
		for(int i=0; i<block[0].length; i++){
			for(int j=0; j<block.length; j++){
				System.out.print(block[j][i]);
//				System.out.println(block[j][i].getWidth()*j+","+block[j][i].getHeight()*i);
			}
			System.out.println();
		}
	}
}
