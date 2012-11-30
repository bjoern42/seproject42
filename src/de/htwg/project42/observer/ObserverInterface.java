package de.htwg.project42.observer;

/**
 * Observer interface for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public interface ObserverInterface {
	
	/**
	 * Add observable to observer
	 * @param o - observable
	 */
	void addObserver(Observable o);
	
	/**
	 * Remove all observables from observer.
	 */
	void removeAllObserver();
	
	/**
	 * Notifies all added observables.
	 */
	void notifyObserver();
}
