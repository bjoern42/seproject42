package de.htwg.project42;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.htwg.project42.controller.LandscapeInterface;
import de.htwg.project42.model.GameObjects.LevelInterface;
import de.htwg.project42.model.GameObjects.LevelLoaderInterface;
import de.htwg.project42.model.GameObjects.PlayerInterface;

public class JumpNRunModule extends AbstractModule{
public static final int LANDSCAPE_SIZE_X = 1200, LANDSCAPE_SIZE_Y = 800, LANDSCAPE_LENGTH = 12;

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("playerX")).to(LANDSCAPE_SIZE_X/2);
		bindConstant().annotatedWith(Names.named("playerY")).to(0);
		bindConstant().annotatedWith(Names.named("playerWidth")).to(LANDSCAPE_SIZE_X/LANDSCAPE_LENGTH);
		bindConstant().annotatedWith(Names.named("playerHeight")).to((LANDSCAPE_SIZE_X/LANDSCAPE_LENGTH)*2);
		bindConstant().annotatedWith(Names.named("blockSize")).to(LANDSCAPE_SIZE_X/LANDSCAPE_LENGTH);
		bindConstant().annotatedWith(Names.named("visibleBlockIndex")).to(LANDSCAPE_LENGTH);
		bindConstant().annotatedWith(Names.named("landscapeWidth")).to(LANDSCAPE_SIZE_X);
		bindConstant().annotatedWith(Names.named("landscapeHeight")).to(LANDSCAPE_SIZE_Y);
		
		
		bind(PlayerInterface.class).to(de.htwg.project42.model.GameObjects.Implementation.Player.class);
		bind(LevelInterface.class).to(de.htwg.project42.model.GameObjects.Implementation.Level.class);
		bind(LandscapeInterface.class).to(de.htwg.project42.controller.Implementation.Landscape.class);
		bind(LevelLoaderInterface.class).to(de.htwg.project42.model.GameObjects.Implementation.LevelLoader.class);
	}
}
