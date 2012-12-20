package de.htwg.project42.model.GameObjects;

public interface BlockInterface extends GameObjectsInterface{
	static int TYP_AIR = 0;	
	static int TYP_GRAS = 1;
	static int TYP_ENEMY = 2;
	static int TYP_WATER = 3;
	static int TYP_COIN = 4;
	static int TYP_GOAL = 5;
	static int TYP_CRATE = 6;
	int getType();
	void setType(int pType);
}
