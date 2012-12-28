package de.htwg.project42.observer;

import java.util.List;

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
	 * Returns a List of all observables.
	 * @return list of observables
	 */
	List<Observable> getObserver();
	
	/**
	 * Remove all observables from observer.
	 */
	void removeAllObserver();
	
	/**
	 * Removes observable from observer
	 * @param o - observable
	 */
	void removeObserver(Observable o);
	
	/**
	 * Notifies all added observables.
	 */
	void notifyObserver();
}
