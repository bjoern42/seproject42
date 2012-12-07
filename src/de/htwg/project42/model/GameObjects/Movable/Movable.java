package de.htwg.project42.model.GameObjects.Movable;

/**
 * Movable interface for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public interface Movable {
	/**
	 * Called if GameObject position needs to be changed.
	 */
	void update(int pChange);
}
