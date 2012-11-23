package de.htwg.project42.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Level loader for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
public final class LevelLoader {
private BufferedReader reader = null;

	/**
	 * Creates a level loader
	 * @param file
	 */
	public LevelLoader(File file){
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
