package de.htwg.project42.model.GameObjects;

public interface EnemyInterface extends GameObjectsInterface{
	boolean getJump();
	int getDirection();
	void changeDirection();
	void kill();
	boolean isDead();
}
