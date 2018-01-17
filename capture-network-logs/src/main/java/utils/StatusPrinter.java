package utils;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import exceptions.IlligalOutputComponentException;

public class StatusPrinter {

	private static StatusPrinter instance = new StatusPrinter();

	JLabel label;

	private StatusPrinter() {

	}

	public static synchronized StatusPrinter getInstance() {
		if(instance == null){
			instance = new StatusPrinter();
		}
		return instance;
	}

	public void setOutput(final Object object) throws IlligalOutputComponentException {

		if(object instanceof JLabel) {
			label = (JLabel) object;
		}
		else {
			instance = null;
			throw new IlligalOutputComponentException("Only JLabel component is supported.");
		}

	}

	public synchronized void update(String msg, boolean isTemporary,String colorCode) {

		label.setText(null);

		switch(colorCode.toLowerCase()) {
		case "pass":
			label.setForeground(new Color(2, 142, 14));

			break;
		case "warn":
			label.setForeground(new Color(198, 117, 9));
			break;
		case "fail":
			label.setForeground(new Color(198, 0, 0));
			break;
		default:
			label.setForeground(Color.BLACK);
			break;
		}

		label.setText("<html>"+msg+"</html>");


		if(isTemporary) {
			Timer t = new Timer(3000, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					label.setText(null);
				}
			});
			t.setRepeats(false);
			t.start();
		}
		return;
		
	}

	
	public void dispose() {
		instance = null;
	}

}
