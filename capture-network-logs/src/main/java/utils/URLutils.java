package utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLutils {

	private static String URL_regex = "^(http:\\/\\/|https:\\/\\/)(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.(com|io|info|org|net|eu|in)(\\/|[a-zA-Z]|.)*$";

	public static boolean IsCorrectURL(String s) {

		String pattern= URL_regex;
		Pattern patt = Pattern.compile(pattern);
		Matcher matcher = patt.matcher(s);
		return matcher.matches();
	}

	public static String Execute(String OS, String pathToBinary, String pathToScript, String URL) {

		String fileName = new SimpleDateFormat("'RESULT_"+"'yyyyMMddHHmm'.json'").format(new Date());
		File _TempOutputFile;
		try {_TempOutputFile = File.createTempFile("RESULT_", ".json");} 
		catch(Exception e) {_TempOutputFile = new File(fileName);};
		_TempOutputFile.deleteOnExit();

		String query = pathToBinary+" "+pathToScript+" \""+URL+"\" > "+_TempOutputFile;
		ProcessBuilder builder =  null;
		if(OS.toLowerCase().contains("windows")) {
			builder = new ProcessBuilder("cmd.exe", "/c",query);
		}
		else if (OS.toLowerCase().contains("mac")) {
			try {
			//	console.add("\n"+"chmod a+x "+pathToBinary);
				Runtime.getRuntime().exec("chmod a+x "+pathToBinary);
			} catch (IOException e1) {}

			builder = new ProcessBuilder("/bin/bash","-c",query);
		}

		try {
			Process p;
			p = builder.start();
			p.waitFor();
			p.destroy();

		} catch (IOException e2) {} 
		catch (InterruptedException e3) {}


		return _TempOutputFile.getAbsolutePath().toString();
	}


}
