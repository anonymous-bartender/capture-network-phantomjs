package utils;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

	private static Report instance = new Report();
	private PrintWriter writer;

	private Report() {

	}

	public static synchronized Report getInstance() {
		if(instance == null){
			instance = new Report();
		}
		return instance;
	}

	public boolean createFile(String pathToFile) {

		try {
			File _OutputFinal = new File(pathToFile);
			if(_OutputFinal.exists()) _OutputFinal.delete();
			
			if(_OutputFinal.createNewFile()) {
				writer = new PrintWriter(_OutputFinal, "UTF-8");
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void write(String txt) {
		writer.println(txt);
		writer.flush();
	}
	
	public void close() {
		writer.close();
		instance = null;
	}
	
	private String getOutputFileName() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yMdHms");
		 String t = dateFormatter.format(new Date());
		
		
		return ARGS_OUTPUT_FILE+"/RESULT"+t+".csv";
	}

}
