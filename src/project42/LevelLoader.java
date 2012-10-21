package project42;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class LevelLoader {
BufferedReader reader = null;

	public LevelLoader(File file){
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
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
