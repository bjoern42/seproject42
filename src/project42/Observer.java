package project42;

import java.util.LinkedList;

public class Observer implements Observable {
LinkedList<GameObject[]> objects = new LinkedList<GameObject[]>();
	
	public void add(GameObject[] object){
		objects.add(object);
	}
	
	public void removeFirst(){
		objects.removeFirst();
	}
	
	public int getSize(){
		return objects.size();
	}
	
	@Override
	public void update() {
		for(GameObject block[] : objects){
			for(int i=0; i<block.length; i++){
				block[i].update();
			}
		}
	}
}
