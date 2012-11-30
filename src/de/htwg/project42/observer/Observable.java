package de.htwg.project42.observer;

/**
 * Observable interface for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public interface Observable {
	/**
	 * Called if GameObject position should be changed.
	 * @param pChange - change
	 */
	void update(int pChange);
}
