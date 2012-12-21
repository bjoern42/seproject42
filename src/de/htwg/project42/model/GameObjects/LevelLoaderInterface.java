package de.htwg.project42.model.GameObjects;

import java.io.File;

public interface LevelLoaderInterface {
	void setInputFile(File file);
	int[] readNext();
}
