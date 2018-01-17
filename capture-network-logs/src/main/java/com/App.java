package com;

import java.awt.EventQueue;

public class App {
	
	
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CaptureNetworkWindow window = new CaptureNetworkWindow();
					window.frmCaptureNetworkCalls.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-999);
				}
			}
		});

	}

}
