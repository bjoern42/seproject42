package de.htwg.project42.model.GameObjects;

public interface iBlock extends iGameObjects{
	public static final int TYP_AIR = 0;	
	public static final int TYP_GRAS = 1;
	public static final int TYP_ENEMY = 2;
	public static final int TYP_WATER = 3;
	public static final int TYP_COIN = 4;
	public static final int TYP_GOAL = 5;
	public static final int TYP_CRATE = 6;
	int getType();
	void setType(int typAir);
	void setMovable(boolean b);
}
