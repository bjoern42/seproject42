package de.htwg.project42.model.GameObjects;

import java.io.File;

public interface LevelLoaderInterface {
	void setInputFile(File file);
	void setOutputFile(File file);
	int[] readNext();
	void writeNext(Integer line[]);
	void closeStreams();
}
