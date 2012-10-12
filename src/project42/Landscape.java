package project42;

public class Landscape {
private final int sizeX = 12,sizeY = 8;
private int width,height;
private GameObject block[][] = new Block[sizeX][sizeY];

	public Landscape(int pWidth,int pHeight){
		width = pWidth;
		height = pHeight;
		for(int i=0; i<block.length; i++){
			for(int j=0; j<block.length; j++){
				block[i][j] = new Block();
			}
		}
	}
}
