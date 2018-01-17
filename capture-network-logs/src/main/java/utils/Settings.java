package utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Properties;

public class Settings {
	
	private String fileMode = "url"; //file, url
	private String reportingMode = "summary"; //summary, detail
	private String input="";
	private String output = "";
	private String ssl_list[]= {"ixquick.com","ixquick.eu","startpage.com","startpage.info","ixquick.info"};
	private String domain_list[] = {"webintensive.com","belzabar.com"};	
	private String OS = "";
	
	
	public Settings() {
		//default
		
	}
	
	public Settings(String file) throws IOException {
		
		FileReader reader=new FileReader(file);  
	    Properties prop=new Properties();  
	    prop.load(reader);  
	    
	    fileMode =  prop.getProperty("fileMode");
		reportingMode = prop.getProperty("reportingMode");
		input= prop.getProperty("input");
		output =  prop.getProperty("output");
		ssl_list= prop.getProperty("ssl_list").split(",");
		domain_list= prop.getProperty("domain_list").split(",");
		
		reader.close();
		
	}
	
	public File SaveSettings(String path) {
		
		File f = new File(path+"/capturenetworklogs.config");
		
		try {
		
		f.createNewFile();
		
		FileReader reader=new FileReader(f);  
	    Properties prop=new Properties();  
	    prop.load(reader);  
	    
	    prop.setProperty("fileMode",String.valueOf(getFileMode()));
		prop.setProperty("reportingMode",getReportingMode());
		prop.setProperty("input",getInput());
		prop.setProperty("output",getOutput());
		
		String s = "";
		for (String e: getSSLlist()) s= e + ",";
		s = new StringBuilder(s).deleteCharAt(s.length() - 1).toString();
		prop.setProperty("ssl_list",s);
		
		s = "";
		for (String e: getDomainList()) s= e + ",";
		s = new StringBuilder(s).deleteCharAt(s.length() - 1).toString();
		prop.setProperty("domain_list",s);
		
		reader.close();
		
		return f;
		} catch(IOException e) {
			f= null;
			return f;
		}
	}
	
	//GETTER and SETTER
	
	public boolean getFileMode() {
		return fileMode=="file"?true:false;
	}
	
	public String getReportingMode() {
		return reportingMode;
	}
	
	public String[] getSSLlist() {
		return ssl_list;
	}
	public String[] getDomainList() {
		return domain_list;
	}
	public String getInput() {
		return input;
	}
	public String getOutput() {
		return output;
	}
	public String getOS() {
		return OS;
	}
	/////
	public void setFileMode(boolean x) {
		fileMode = x?"file":"url";
	}
	
	public void setReportingMode(String x) {
		reportingMode = x;
	}
	
	public void setSSLlist(String x) {
		ssl_list= x .split(",");
	}
	public void setDomainList(String x) {
		domain_list= x.split(",");
	}
	public void setInput(String x) {
		input = x;
	}
	public void setOutput(String x) {
		String t = new Timestamp(System.currentTimeMillis()).toString();
		output =  x+"/RESULT"+t+".csv";
	}
	public void setOS(String x) {
		OS = x;
	}
	

}
