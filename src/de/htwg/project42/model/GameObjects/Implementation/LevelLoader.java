package de.htwg.project42.model.GameObjects.Implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import de.htwg.project42.model.GameObjects.LevelLoaderInterface;

/**
 * Level loader for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class LevelLoader implements LevelLoaderInterface {
private BufferedReader reader = null;
private Logger logger = Logger.getLogger("de.htwg.project42.view.TUI");


	public void setInputFile(File file){
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			logger.error(e);
		}
	}
	
	/**
	 * Reads next line of the file.
	 * @return map line
	 */
	public int[] readNext(){
		try {
			String tmp[] = reader.readLine().split(",");
			int retVal[] = new int[tmp.length];
			for(int i=0;i<retVal.length;i++){
				retVal[i] = Integer.parseInt(tmp[i]);
			}
			return retVal;
		} catch (Exception e) {
			return null;
		}
	}
}
