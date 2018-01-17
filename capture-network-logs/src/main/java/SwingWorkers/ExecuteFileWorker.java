package SwingWorkers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import com.CaptureNetworkWindow;

import utils.ConsolePrinter;
import utils.Report;
import utils.ResourceManager;
import utils.Settings;
import utils.StatusPrinter;

public class ExecuteFileWorker extends SwingWorker<Boolean, Void>
{
	
	Settings settings = CaptureNetworkWindow.settings;
	ConsolePrinter console = CaptureNetworkWindow.console;
	StatusPrinter status = CaptureNetworkWindow.status;

	@Override
	protected Boolean doInBackground () throws Exception {

		String pathToScript="",pathToBinary="",query="",ResultOutputFile="", OS = "",_outputTempFile="",childURLs[]=null;
		boolean _isSSLpass=false,_isOriginPass=false;
		final boolean _detailMode = settings.getReportingMode().equals("detail")?true:false;

		Report report = Report.getInstance();
		report.createFile(settings.getOutput());

		//extract files and store them
		try {
			pathToScript = ResourceManager.extract("/netsniff.js");
			console.add("\n"+"Script located: "+pathToScript);
		} catch(Exception e1) {
			status.update(e1.getMessage(),false,"fail");};

		try {

			if(settings.getOS().contains("windows")) 
				pathToBinary=ResourceManager.extract("/phantomjs.exe");
					
			else 
				pathToBinary=ResourceManager.extract("/phantomjs");
			
			console.add("PhantomJS located: "+pathToBinary);

		} catch(Exception e2) {
			status.update(e2.getMessage(),false,"fail");};

		//Evaluate the input file
		String fileName = settings.getInput();
		final int lineCount = _CountFileLine(fileName)+1;
		console.add("File processed: "+fileName);
		console.add("Total Test Count: "+lineCount);
		FileReader fileReader = new FileReader(new File(fileName));
		BufferedReader br = new BufferedReader(fileReader);
		String line="";

		status.update("Collecting Network Calls...",false,"");
		console.add("********************");

		console.add(_detailMode?("\n"+"URL, Calls, SSL, Same Origin Policy"):("\n"+"URL, Calls, SSL, Same Origin Policy"));
		report.write(_detailMode?("Base URL,Calls,SSL,Same Origin Policy"):("Base URL,SSL,Same Origin Policy"));

		//System.out.println(_OutputFinal.getAbsolutePath().toString());

		int count=1;
		while (true) {
			line = br.readLine().trim();
			if (line == null) break;

			try {
				status.update("Executing Test "+(count++)+" of "+lineCount+"...",false,"");

				int code = 400;
				try { 
					URL url = new URL(line);
					HttpURLConnection connection = (HttpURLConnection)url.openConnection();
					//connection.setConnectTimeout(1000);
					connection.setRequestMethod("GET");
					connection.connect();
					code = connection.getResponseCode(); 
				} catch (Exception e) {
					code=400;
				};

				if(code % 100 == 4) {
					console.add(line + (_detailMode?",NaN"+",NaN"+",NaN":",NaN"+",NaN"));
					report.write(line+(_detailMode?",NaN"+",NaN"+",NaN":",NaN"+",NaN"));
				}

				else if(!IsCorrectURL(line)) {
					console.add(line+(_detailMode?",NaN"+",NaN"+",NaN":",NaN"+",NaN"));
					report.write(line+(_detailMode?",NaN"+",NaN"+",NaN":",NaN"+",NaN"));
				}

				else {

					childURLs = CaptureNetworkWindow._fetchNetworkCalls(CaptureNetworkWindow._Execute(OS, pathToBinary, pathToScript, line));

					if(childURLs == null) {
						report.write(_detailMode?
								(line + ",NaN,NaN,NaN"):
									(line + ",NaN,NaN"));
						continue;
					}

					if(_detailMode) {

						for (String s:childURLs) {
							boolean a=true,b=true;
							a = _verifySSL(s);
							b = _verifySameOrigin(s);

							report.write(line + ","+s+","+
									(a?"Pass":"Fail")+","+
									(b?"Pass":"Fail"));
							CaptureNetworkWindow.txtAreaStatus.append("\n"+line + ","+s+","+
									(a?"Pass":"Fail")+","+
									(b?"Pass":"Fail"));
						}
					}
					else  {
						boolean a=true,b=true;
						//ssl
						for (String s:childURLs) {
							if(!_verifySSL(s)){
								a=false;
								break;
							}
						}
						//same origin
						for (String s:childURLs) {
							if(!_verifySameOrigin(s)){
								b=false;
								break;
							}
						}

						CaptureNetworkWindow.txtAreaStatus.append("\n"+line + ","+
								(a?"Pass":"Fail")+","+
								(b?"Pass":"Fail"));
						report.write(line + ","+
								(a?"Pass":"Fail")+","+
								(b?"Pass":"Fail"));
					}


				}
				//CaptureNetworkWindow.txtAreaStatus.append("\n"+"********************");
				//writer.close();

				report.close();
			} catch (Exception e1) {
				//CaptureNetworkWindow.txtAreaStatus.append("\n"+"********************");
				//CaptureNetworkWindow.setStatus(e1.getMessage(),false,"fail");
				//CaptureNetworkWindow.txtAreaStatus.append("\n"+e1.getMessage());
				//CaptureNetworkWindow.setStatus("Something gone wrong. A <b>Reset</b> is recommended.",false,"");
				report.write(_detailMode?(line + ",NaN,NaN,NaN"):(line + ",NaN,NaN"));
				continue;
			};

			//			} catch (Exception e2) {
			//				CaptureNetworkWindow.txtAreaStatus.append("\n"+"********************");
			//				CaptureNetworkWindow.setStatus(e2.getMessage(),false,"fail");
			//				CaptureNetworkWindow.txtAreaStatus.append("\n"+e2.getMessage());
			//				CaptureNetworkWindow.setStatus("Something gone wrong. A <b>Reset</b> is recommended.",false,"");
			//				writer.println(_detailMode?(line + ",NaN,NaN,NaN"):(line + ",NaN,NaN"));
			//				continue;
			//			};
		} //End of While			

		console.add("\n"+"********************");
		//			writer.close();
		br.close();
		//CaptureNetworkWindow.txtAreaStatus.append("\nFind results in "+_OutputFinal.getAbsolutePath().toString());
		return true;
	}
	

	
	private int _CountFileLine(String filename) {
		
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(filename));
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			is.close();
			return (count == 0 && !empty) ? 1 : count;
		}catch(IOException e) {
			return -999;			
		}
	}


	protected static boolean _verifySSL(String URL) {

		if(URL.startsWith("https://")) return true;

		return false;
	}

	/*
	 * THis is only for Startpage.com domains
	 */

	protected static boolean _verifySameOrigin(String URL) { 		

		String whiteList[] = {"ixquick.com","ixquick.eu","startpage.com","startpage.info","ixquick.info"};

		if(!(URL.contains(whiteList[0]) ||
				URL.contains(whiteList[1]) ||
				URL.contains(whiteList[2]) ||
				URL.contains(whiteList[3]) ||
				URL.contains(whiteList[4]))) {
			return false;
		}
		return true;
	}

	@Override
	protected void done()
	{
		try
		{
			console.add("Completed");
			status.update("Find results in the below text pane.",false,"");
			CaptureNetworkWindow.chkFileMode.setSelected(false);
			CaptureNetworkWindow.executeMode(false);
			CaptureNetworkWindow.btnLoad.setVisible(false);
			CaptureNetworkWindow.btnReset.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
	}
}