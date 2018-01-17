package SwingWorkers;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingWorker;

import com.CaptureNetworkWindow;

import utils.ResourceManager;

public class ExecuteURLWorker extends SwingWorker<Boolean, Void>
{

	@Override
	protected Boolean doInBackground () throws Exception {

		String pathToScript="",pathToBinary="",query="",ResultOutputFile="";


		//extract files and store them
		try {
			pathToScript = ResourceManager.extract("/netsniff.js");
			CaptureNetworkWindow.txtAreaStatus.append("\n"+"Script located: "+pathToScript);
		} catch(Exception e1) {CaptureNetworkWindow.setStatus(e1.getMessage(),false,"fail");};

		try {

			String OS = System.getProperty("os.name").toLowerCase();
			if(OS.toLowerCase().contains("windows")) pathToBinary=ResourceManager.extract("/phantomjs.exe");
			else if(OS.toLowerCase().contains("mac")) pathToBinary=ResourceManager.extract("/phantomjs");

			CaptureNetworkWindow.txtAreaStatus.append("\n"+"PhantomJS located: "+pathToBinary);

		} catch(Exception e2) {CaptureNetworkWindow.setStatus(e2.getMessage(),false,"fail");};


		try {
			File temp = File.createTempFile("tempforURL", ".json");
			temp.deleteOnExit();
			ResultOutputFile=temp.getAbsolutePath();
			CaptureNetworkWindow.txtAreaStatus.append("\n"+"Temp file created: "+ResultOutputFile);
		} catch(IOException e3) {CaptureNetworkWindow.setStatus(e3.getMessage(),false,"fail");};



		CaptureNetworkWindow.setStatus("Collecting Network Calls...",false,"");
		CaptureNetworkWindow.txtAreaStatus.append("\n"+"********************");

		try {
			//CaptureNetworkWindow.setStatus("Executing Test 1 of 1...",false,"");
			query = pathToBinary+" "+pathToScript+" \""+CaptureNetworkWindow.txtUrlField.getText()+"\" > "+ResultOutputFile;
			ProcessBuilder builder =  null;
			if(System.getProperty("os.name").toLowerCase().contains("windows")) {
				System.out.println (query);
				builder = new ProcessBuilder("cmd.exe", "/c",query);
			}
			else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				Runtime.getRuntime().exec("chmod a+x "+pathToBinary);
				CaptureNetworkWindow.txtAreaStatus.append("\n"+"chmod a+x "+pathToBinary);
				builder = new ProcessBuilder("/bin/bash","-c",query);
			}

			Process p = builder.start();
			p.waitFor();

			CaptureNetworkWindow.ResultOutputFile = ResultOutputFile;

			p.destroy();


		} catch (IOException e6) {
			CaptureNetworkWindow.txtAreaStatus.append("\n"+"********************");
			CaptureNetworkWindow.setStatus(e6.getMessage(),false,"fail");
			CaptureNetworkWindow.txtAreaStatus.append("\n"+e6.getMessage());
		}
		catch (InterruptedException e7) {
			CaptureNetworkWindow.txtAreaStatus.append("\n"+"********************");
			CaptureNetworkWindow.setStatus(e7.getMessage(),false,"fail");
			CaptureNetworkWindow.txtAreaStatus.append("\n"+e7.getMessage());
		};
		CaptureNetworkWindow.setStatus("Something gone wrong. A <b>Reset</b> is recommended.",false,"");
		return false;
	}
	@Override
	protected void done()
	{
		try
		{
			CaptureNetworkWindow.txtAreaStatus.append("\nCompleted");
			CaptureNetworkWindow.setStatus("Select what you want to do.",false,"");
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
