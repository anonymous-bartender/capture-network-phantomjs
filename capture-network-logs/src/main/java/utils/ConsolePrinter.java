package utils;

import javax.swing.JTextArea;

import exceptions.IlligalOutputComponentException;

public class ConsolePrinter {

	private static ConsolePrinter instance = new ConsolePrinter();

	JTextArea txtArea;

	private ConsolePrinter() {

	}

	public static synchronized ConsolePrinter getInstance() {
		if(instance == null){
			instance = new ConsolePrinter();
		}
		return instance;
	}

	public void setOutput(final Object object) throws IlligalOutputComponentException {

		if(object instanceof JTextArea) {
			txtArea = (JTextArea) object;
		}
		else {
			instance = null;
			throw new IlligalOutputComponentException("Only JTextArea component is supported.");
		}

	}

	public synchronized void add(String status) {
		txtArea.append("\n");
		txtArea.append(status);
	}

	public synchronized void add(Exception ex) {

		StackTraceElement ele[] = ex.getStackTrace();

		for(StackTraceElement e:ele) {
			txtArea.append(e.toString());
		}

	}

	public void dispose() {
		instance = null;
	}

}