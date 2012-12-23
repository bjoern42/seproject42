package de.htwg.project42;

import org.apache.log4j.PropertyConfigurator;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.view.GUI.MainMenuGUI;



public class JumpNRun {
private Injector injector = null;
private LandscapeInterface landscape;

	public static void main(String[] args) {
		new JumpNRun();
	}
	
	private JumpNRun(){
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");
		
		// Set up Google Guice Dependency Injector
		injector = Guice.createInjector(new JumpNRunModule());
		
		// Build up the application, resolving dependencies automatically by Guice
		landscape  = injector.getInstance(LandscapeInterface.class);
		
		new MainMenuGUI(landscape, JumpNRunModule.LANDSCAPE_SIZE_X, JumpNRunModule.LANDSCAPE_SIZE_Y, JumpNRunModule.LANDSCAPE_LENGTH);
	}
}
