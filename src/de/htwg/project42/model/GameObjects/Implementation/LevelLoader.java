package de.htwg.project42.model.GameObjects.Implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.google.inject.Singleton;

import de.htwg.project42.model.GameObjects.LevelLoaderInterface;

/**
 * Level loader for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@Singleton
public final class LevelLoader implements LevelLoaderInterface {
private BufferedReader reader = null;
private BufferedWriter writer = null;
private Logger logger = Logger.getLogger("de.htwg.project42.view.TUI");

	/**
	 * Sets the file the map will be loaded from.
	 */
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
	
	/**
	 * Sets the file the map will be saved to.
	 */
	public void setOutputFile(File file){
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	/**
	 * Writes a line to the file.
	 * @return map line
	 */
	public void writeNext(Integer line[]){
		try{
			for(int i=0; i<line.length; i++){
				writer.write(""+line[i]);
				if(i+1<line.length){
					writer.write(",");
				}
			}
			writer.newLine();
			writer.flush();
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	/**
	 * Closes input and output streams.
	 */
	public void closeStreams(){
		try {
			if(reader != null){
				reader.close();
			}
			if(writer != null){
				writer.close();
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
