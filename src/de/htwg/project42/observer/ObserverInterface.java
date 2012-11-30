package de.htwg.project42.observer;

public interface ObserverInterface {
	void addObserver(Observable o);
	void removeAllObserver();
	void notifyObserver();
}
