package utils;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;

import exceptions.IlligalOutputComponentException;

public class IBorder {
	
	
	private static IBorder instance = new IBorder();



	private IBorder() {
		
		

	}

	public static IBorder getInstance() {
		if(instance == null){
			instance = new IBorder();
		}
		return instance;
	}

	public synchronized void setBorder(final Object object) throws IlligalOutputComponentException {
		Border border = BorderFactory.createLineBorder(Color.RED, 1);

		//String objectType = null;
		Timer t = null;

		((JComponent) object).setBorder(border);		

		if(object instanceof JButton) {
			//objectType="Button.border";
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JButton) object).setBorder(UIManager.getBorder("Button.border"));
				}
			});
		}
		else if(object instanceof JTextArea) {
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JTextArea) object).setBorder(UIManager.getBorder("TextArea.border"));
				}
			});
		}
		else if(object instanceof JCheckBox) {
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JCheckBox) object).setBorder(UIManager.getBorder("CheckBox.border"));
				}
			});
		}
		else if(object instanceof JEditorPane) {
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JEditorPane) object).setBorder(UIManager.getBorder("EditorPane.border"));
				}
			});
		}		
		else if(object instanceof JTextPane) {
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JTextPane) object).setBorder(UIManager.getBorder("TextPane.border"));
				}
			});
		}		
		else if(object instanceof JTextField){
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JTextField) object).setBorder(UIManager.getBorder("TextField.border"));
				}
			});
		}
		else if(object instanceof JRadioButton) {
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JRadioButton) object).setBorder(UIManager.getBorder("RadioButton.border"));
				}
			});
		}
		else {
			//objectType="Button.border";
			t = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((JButton) object).setBorder(UIManager.getBorder("Button.border"));
				}
			});
		}

		t.setRepeats(false);
		t.start();

	}

	public void dispose() {
		instance = null;
	}

}