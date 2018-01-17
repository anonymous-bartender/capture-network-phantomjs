package SwingWorkers;

import java.io.IOException;

import javax.swing.SwingWorker;

import com.CaptureNetworkWindow;

public class LoadDataWorker extends SwingWorker<Boolean, Void>
{

	@Override
	protected Boolean doInBackground () throws Exception {


		CaptureNetworkWindow.setStatus("Collecting Network Calls...",false,"");
		try {

			ProcessBuilder builder =  null;
			if(System.getProperty("os.name").toLowerCase().contains("windows")) {
				System.out.println (CaptureNetworkWindow.query);
				builder = new ProcessBuilder("cmd.exe", "/c",CaptureNetworkWindow.query);
			}
			else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				Runtime.getRuntime().exec("chmod a+x "+CaptureNetworkWindow.pathToBinary);
				CaptureNetworkWindow.txtAreaStatus.append("\n"+"chmod a+x "+CaptureNetworkWindow.pathToBinary);
				builder = new ProcessBuilder("/bin/bash","-c",CaptureNetworkWindow.query);
				//						CaptureNetworkWindow.pathToBinary,
				//						CaptureNetworkWindow.pathToScript,
				//						"\"",
				//						CaptureNetworkWindow.txtUrlField.getText(),
				//						"\">",
				//						CaptureNetworkWindow.tempFile);
			}

			Process p = builder.start();
			p.waitFor();
			CaptureNetworkWindow.setStatus("Network calls collected. Choose what you want to do.",false,"");
			return true;


		} catch (IOException e6) {
			CaptureNetworkWindow.setStatus(e6.getMessage(),false,"fail");
			CaptureNetworkWindow.txtAreaStatus.append("\n"+e6.getMessage());
		}
		catch (InterruptedException e7) {
			CaptureNetworkWindow.setStatus(e7.getMessage(),false,"fail");
			CaptureNetworkWindow.txtAreaStatus.append("\n"+e7.getMessage());
		};

		//CaptureNetworkWindow.progressBar.setIndeterminate(false);
		CaptureNetworkWindow.setStatus("Something gone wrong. Try to choose what you want to do.",false,"");
		return false;



	}
	@Override
	protected void done()
	{
		try
		{

			CaptureNetworkWindow.txtAreaStatus.append("\nCompleted");
			CaptureNetworkWindow.executeMode(false);
			//CaptureNetworkWindow.resetMode(true);
			CaptureNetworkWindow.btnLoad.setVisible(false);
			CaptureNetworkWindow.btnReset.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
	}


}
