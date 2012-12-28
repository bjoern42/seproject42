package de.htwg.project42.observer;

import java.util.LinkedList;
import java.util.List;

/**
 * Observer for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public class Observer implements ObserverInterface{
private List<Observable> observables = new LinkedList<Observable>();

	@Override
	public void addObserver(Observable o) {
		observables.add(o);
	}

	@Override
	public List<Observable> getObserver() {
		return observables;
	}
	
	@Override
	public void removeAllObserver() {
		observables.clear();
	}
	
	@Override
	public void removeObserver(Observable o) {
		observables.remove(o);
	}

	@Override
	public void notifyObserver() {
		for(Observable o:observables){
			o.update();
		}
	}

}
