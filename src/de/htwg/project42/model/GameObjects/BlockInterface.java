package de.htwg.project42.model.GameObjects;

public interface BlockInterface extends GameObjectsInterface{
	int TYP_AIR = 0;	
	int TYP_GRAS = 1;
	int TYP_ENEMY = 2;
	int TYP_WATER = 3;
	int TYP_COIN = 4;
	int TYP_GOAL = 5;
	int TYP_CRATE = 6;
	int getType();
	void setType(int pType);
}
