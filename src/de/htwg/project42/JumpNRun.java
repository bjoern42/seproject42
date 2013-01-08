package de.htwg.project42;

import org.apache.log4j.PropertyConfigurator;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.model.GameObjects.LevelLoaderInterface;
import de.htwg.project42.view.GUI.MainMenuGUI;



public final class JumpNRun {

	public static void main(String[] args) {
		new JumpNRun();
	}
	
	private JumpNRun(){
		// Set up logging through log4j
		PropertyConfigurator.configure("log4j.properties");
		
		// Set up Google Guice Dependency Injector
		Injector injector = Guice.createInjector(new JumpNRunModule());
		
		// Build up the application, resolving dependencies automatically by Guice
		LandscapeInterface landscape  = injector.getInstance(LandscapeInterface.class);
		LevelLoaderInterface loader = injector.getInstance(LevelLoaderInterface.class);
		
		new MainMenuGUI(landscape, loader, JumpNRunModule.LANDSCAPE_SIZE_X, JumpNRunModule.LANDSCAPE_SIZE_Y, JumpNRunModule.LANDSCAPE_LENGTH);
	}
}
