package de.htwg.project42.model.GameObjects;

public interface iEnemy extends iGameObjects{
	boolean getJump();
	int getDirection();
	void changeDirection();
	void kill();
	boolean isDead();
}
