package de.htwg.project42.observer;

import java.util.LinkedList;
import java.util.List;

public class Observer implements ObserverInterface{
private List<Observable> observables = new LinkedList<Observable>();

	@Override
	public void addObserver(Observable o) {
		observables.add(o);
	}

	@Override
	public void removeAllObserver() {
		observables.clear();
	}

	@Override
	public void notifyObserver() {
		for(Observable o:observables){
			o.update();
		}
	}

}
