package com;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.*;
import SwingWorkers.*;
import exceptions.IlligalOutputComponentException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Window.Type;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.SystemColor;
import javax.swing.JProgressBar;
import java.awt.Component;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class CaptureNetworkWindow {

	//public static String pathToSaveFile = "/Reports";
	public String ResultOutputFile = "/temp.json";
	public String pathToScript = "/netsniff.js";
	public static String pathToBinary_Win = "/phantomjs.exe";
	public static String pathToBinary_Mac = "/phantomjs";
	public static String query;
	public static ProcessBuilder builder = new ProcessBuilder("");
	public String VM_os = "windows";
	//protected static String fileOfURL = "";

	//protected static boolean fileMode = false;
	public JFrame frmCaptureNetworkCalls;
	private JTextField txtUrlField;
	private JLabel lblStatusText;
	private JTextArea txtAreaStatus;
	private JProgressBar progressBar;
	private JButton btnVerifySsl;
	private JButton btnSameOriginPolicy;
	private JButton btnLoad;
	private JButton btnReset;
	private JButton btnClose;
	private JFileChooser fileChooser;
	private JFileChooser folderChooser;
	private JButton btnFileUpload;
	private JCheckBox chkFileMode;
	private JRadioButton rdbtnDetaill;
	private JPanel panelFileMode;
	private JTextField txtFileSaveDirectory;
	private JButton btnFileSaveDirectory;
	private JRadioButton rdbtnSummary;
	private JPanel panelURLMode;
	private JMenuItem menuOpt1;
	private JMenuItem menuOpt2;
	private JMenuItem menuOpt3;
	private JMenuItem menuOpt3A;
	private JMenuItem menuOpt3B;
	private JMenuItem menuOpt2A;
	private JMenuItem menuOpt2B;
	private JMenu menuParent;
	private JMenuBar menuBar;
	
	private String pathToBinary;
	//private WebDriver driver = null;
	public String osName; //windows, mac, linux

	public static Settings settings;
	public static ConsolePrinter console;
	public static StatusPrinter status;
	public static IBorder border;


	/**
	 * Launch the application from App.Main.
	 */


	/**
	 * Create the application.
	 */
	public CaptureNetworkWindow() {
		
		settings = new Settings();
		console = ConsolePrinter.getInstance();
		status = StatusPrinter.getInstance();
		border = IBorder.getInstance();
		
		
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		settings.setOS(VM_os);
		
		frmCaptureNetworkCalls = new JFrame();
		frmCaptureNetworkCalls.setIconImage(Toolkit.getDefaultToolkit().getImage(CaptureNetworkWindow.class.getResource("/javax/swing/plaf/metal/icons/Inform.gif")));
		frmCaptureNetworkCalls.setType(Type.POPUP);
		frmCaptureNetworkCalls.setResizable(false);
		frmCaptureNetworkCalls.setTitle("Capture Network Calls");
		frmCaptureNetworkCalls.getContentPane().setBackground(UIManager.getColor("menu"));
		frmCaptureNetworkCalls.setBounds(100, 100, 369, 446);
		frmCaptureNetworkCalls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCaptureNetworkCalls.getContentPane().setLayout(null);

		lblStatusText = new JLabel("");
		lblStatusText.setFont(new Font("Trebuchet MS", Font.BOLD, 11));
		lblStatusText.setBounds(26, 123, 312, 34);
		frmCaptureNetworkCalls.getContentPane().add(lblStatusText);

		chkFileMode = new JCheckBox("File Mode");
		chkFileMode.setToolTipText("Select the execution mode");
		chkFileMode.setBounds(254, 17, 86, 23);
		chkFileMode.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		chkFileMode.setSelected(settings.getFileMode());
		chkFileMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnReset.isVisible()) {
					try {border.setBorder(btnReset);}
					catch (IlligalOutputComponentException e1) { console.add(e1); }
					return;
				}
				else
					settings.setFileMode(chkFileMode.isSelected());
			}
		});

		txtUrlField = new JTextField();
		if(settings.getFileMode()) txtUrlField.setBounds(26, 83, 266, 29);
		else txtUrlField.setBounds(26, 83, 312, 29);
		txtUrlField.setToolTipText("Provide the WebPage Name");
		txtUrlField.setColumns(10);

		JLabel lblProvideTheUrl = new JLabel("<html>Provide the URL of the Web Page to Capture the Network Calls and click <b>Load<b>.</html>");
		lblProvideTheUrl.setBounds(26, 47, 309, 34);
		lblProvideTheUrl.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));

		panelURLMode = new JPanel();
		panelURLMode.setBounds(26, 180, 312, 29);
		
		panelURLMode.setLayout(null);
		panelURLMode.setVisible(!settings.getFileMode());

		btnVerifySsl = new JButton("Verify SSL");
		btnVerifySsl.setBounds(0, 0, 125, 29);
		panelURLMode.add(btnVerifySsl);
		btnVerifySsl.setEnabled(false);
		btnVerifySsl.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));

		btnSameOriginPolicy = new JButton("Same Origin Policy");
		btnSameOriginPolicy.setBounds(187, 0, 125, 29);
		panelURLMode.add(btnSameOriginPolicy);
		btnSameOriginPolicy.setEnabled(false);
		btnSameOriginPolicy.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
		btnSameOriginPolicy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//readfile("sameoriginpolicy");
				_readfile("sameoriginpolicy", ResultOutputFile, false);
			}
		});
		btnVerifySsl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//readfile("ssl");
				_readfile("ssl", ResultOutputFile, false);
			}
		});

		btnReset = new JButton("Reset");
		btnReset.setToolTipText("Revert back to State Zero");
		btnReset.setVisible(false);

		btnFileUpload = new JButton("");
		btnFileUpload.setToolTipText("Select the input file");
		btnFileUpload.setBounds(295, 83, 43, 29);
		
		btnFileUpload.setIcon(new ImageIcon(CaptureNetworkWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/upFolder.gif")));
		btnFileUpload.setVisible(settings.getFileMode());

		btnFileUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(fileChooser.showOpenDialog(frmCaptureNetworkCalls)== JFileChooser.APPROVE_OPTION) {

					String s = fileChooser.getSelectedFile().getAbsolutePath().toString().replaceAll("\\", "/");
					txtUrlField.setText("file://"+s);
					settings.setInput(s);
				}
			}
		});
		btnReset.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
		btnReset.setBounds(69, 370, 80, 29);
		
		btnReset.setIcon(null);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//fileMode=false;
				chkFileMode.setSelected(false);
				txtAreaStatus.setText("");
				txtUrlField.setText("");
				btnLoad.setVisible(true);
				btnReset.setVisible(false);
				//resetMode(false);
			}
		});

		btnClose = new JButton("Close");
		btnClose.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				status.update("Closing the Application.", false,"");
				Timer t2 = new Timer(3000, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				t2.setRepeats(false);
				t2.start();

			}
		});
		btnClose.setBounds(213, 370, 80, 29);


		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportBorder(new SoftBevelBorder(BevelBorder.RAISED, new Color(255, 255, 255), new Color(255, 255, 255), null, null));
		scrollPane.setBounds(26, 230, 312, 120);
		

		txtAreaStatus = new JTextArea();
		txtAreaStatus.setBorder(null);
		txtAreaStatus.setBackground(SystemColor.info);
		scrollPane.setViewportView(txtAreaStatus);
		txtAreaStatus.setForeground(Color.BLACK);
		txtAreaStatus.setLineWrap(true);

		progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		progressBar.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		progressBar.setBounds(26, 223, 312, 7);
		

		JSeparator separator = new JSeparator();
		separator.setBounds(26, 115, 312, 2);
		

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(26, 162, 312, 2);
		

		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select input file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		folderChooser = new JFileChooser();
		folderChooser.setMultiSelectionEnabled(false);
		folderChooser.setCurrentDirectory(new java.io.File("."));
		folderChooser.setDialogTitle("Select Output Folder");
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		panelFileMode = new JPanel();
		panelFileMode.setBounds(26, 168, 312, 55);
		
		panelFileMode.setLayout(null);
		panelFileMode.setVisible(settings.getFileMode());

		txtFileSaveDirectory = new JTextField();
		txtFileSaveDirectory.setBounds(0, 0, 266, 29);
		panelFileMode.add(txtFileSaveDirectory);
		txtFileSaveDirectory.setToolTipText("Provide the WebPage Name");
		txtFileSaveDirectory.setColumns(10);

		btnFileSaveDirectory = new JButton("");
		btnFileSaveDirectory.setToolTipText("Select the output folder");
		btnFileSaveDirectory.setBounds(269, 0, 43, 29);
		panelFileMode.add(btnFileSaveDirectory);
		btnFileSaveDirectory.setIcon(new ImageIcon(CaptureNetworkWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		btnFileSaveDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(folderChooser.showOpenDialog(frmCaptureNetworkCalls)== JFileChooser.APPROVE_OPTION) {

					txtFileSaveDirectory.setText(folderChooser.getSelectedFile().getAbsolutePath().toString());
				}
			}
		});		

		rdbtnSummary = new JRadioButton("Summary",true);
		rdbtnSummary.setToolTipText("A Summarized CSV will be created");
		rdbtnSummary.setBounds(35, 32, 109, 23);
		panelFileMode.add(rdbtnSummary);

		rdbtnDetaill = new JRadioButton("Detail");
		rdbtnDetaill.setToolTipText("A Detailed CSV will be created");
		rdbtnDetaill.setBounds(174, 32, 109, 23);
		panelFileMode.add(rdbtnDetaill);

		ButtonGroup bg=new ButtonGroup();    
		bg.add(rdbtnDetaill);bg.add(rdbtnSummary);    

		btnLoad = new JButton(settings.getFileMode()?"Execute":"Load");
		btnLoad.setBounds(69, 370, 80, 29);
		
		btnLoad.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
		btnLoad.setIcon(null);


		//Menu Items
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 51, 21);
		

		menuParent = new JMenu("Options");
		menuOpt1 = new JMenuItem("Open/Save");
		menuOpt2 = new JMenuItem("Data");
		menuOpt2A = new JMenuItem("SSL Filter List");
		menuOpt2B = new JMenuItem("Domain Filter List");
		menuOpt3 = new JMenuItem("Debug");
		menuOpt3A = new JMenuItem("On");
		menuOpt3B = new JMenuItem("Off");


		menuOpt2.add(menuOpt2B);		
		menuOpt3.add(menuOpt3A);
		menuOpt3.add(menuOpt3B);
		menuParent.add(menuOpt1);	
		menuParent.add(menuOpt2);
		menuParent.add(menuOpt3);
		menuBar.add(menuParent);
		//menu ends here

		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				load();
			}
		});
		
		
		frmCaptureNetworkCalls.getContentPane().add(chkFileMode);
		frmCaptureNetworkCalls.getContentPane().add(txtUrlField);
		frmCaptureNetworkCalls.getContentPane().add(lblProvideTheUrl);
		frmCaptureNetworkCalls.getContentPane().add(panelURLMode);
		frmCaptureNetworkCalls.getContentPane().add(btnFileUpload);
		frmCaptureNetworkCalls.getContentPane().add(btnReset);
		frmCaptureNetworkCalls.getContentPane().add(btnClose);
		frmCaptureNetworkCalls.getContentPane().add(scrollPane);
		frmCaptureNetworkCalls.getContentPane().add(progressBar);
		frmCaptureNetworkCalls.getContentPane().add(separator_1);
		frmCaptureNetworkCalls.getContentPane().add(separator);
		frmCaptureNetworkCalls.getContentPane().add(panelFileMode);
		frmCaptureNetworkCalls.getContentPane().add(btnLoad);
		frmCaptureNetworkCalls.getContentPane().add(menuBar);
		
	}

	public void CollectTraffic(String URL) {


		status.update("Collecting Network Calls...",false,"");
		try {
			//String query = pathToBinary+" "+pathToScript+" "+" "+URL+">"+temp;
			//query=pathToBinary+" "+pathToScript+" "+txtUrlField.getText()+">"+tempFile;
			//ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", query);
			/**Attempt 2***/
			if(System.getProperty("os.name").toLowerCase().contains("windows")) {
				System.out.println (query);
				builder = new ProcessBuilder("cmd.exe", "/c",query);

			}
			else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				Runtime.getRuntime().exec("chmod a+x "+pathToBinary);
				console.add("\n"+"chmod a+x "+pathToBinary);
				builder = new ProcessBuilder("/bin/bash","-c", query);
			}

			/*******/
			builder.redirectErrorStream(true);
			Process p = builder.start();

			if(p.waitFor()==0){

				progressBar.setIndeterminate(false);
				status.update("Network calls collected. Choose what you want to do.",false,"");
				return;
			}

			//			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(tempFile)));
			//			String l;
			//			while((l=br.readLine())!=null) bw.write(l);
			//			bw.close();
			//			
			//			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			//			String line;
			//			while (true) {
			//				line = r.readLine();
			//				if (line == null) { break; }
			//				System.out.println(line);
			//			}
		} catch (IOException e6) {
			e6.printStackTrace();
			status.update(e6.getMessage(),false,"fail");
			console.add("\n"+e6.getMessage());
			return;
		}
		catch (InterruptedException e7) {
			e7.printStackTrace();
			status.update(e7.getMessage(),false,"fail");
			console.add("\n"+e7.getMessage());
			return;
		};

		progressBar.setIndeterminate(false);
		status.update("Network calls collected. Choose what you want to do.",false,"");
		return;
	}

	public static boolean _verifySSL(String URL) {

		if(URL.startsWith("https://")) return true;

		return false;
	}

	/*
	 * THis is only for XXXX domains.
	 * Following data has been masked.
	 */

	public static boolean _verifySameOrigin(String URL) { 		

		String whiteList[] = {"google.com","google.eu","google.com","google.info","google.info"};

		if(!(URL.contains(whiteList[0]) ||
				URL.contains(whiteList[1]) ||
				URL.contains(whiteList[2]) ||
				URL.contains(whiteList[3]) ||
				URL.contains(whiteList[4]))) {
			return false;
		}
		return true;
	}

	public static String[] _fetchNetworkCalls(String fileName) {

		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(fileName));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject log = (JSONObject) jsonObject.get("log");
			JSONArray entries = (JSONArray) log.get("entries");

			String[] childURLs = new String[entries.size()];

			System.out.println("#############");

			for(int i=0; i<entries.size(); i++) {
				try {
					JSONObject eachEntries = (JSONObject) entries.get(i);
					JSONObject request = (JSONObject) eachEntries.get("request");
					//System.out.println(String.valueOf(request.get("url")));
					childURLs[i] = String.valueOf(request.get("url")); //(String) request.get("url");
				} 
				catch (Exception e1) {
					e1.printStackTrace();
					childURLs[i] =  null;}
			}

			return childURLs;

		} catch (Exception e2) {

			try {
				Object obj = new JSONParser().parse(new FileReader(fileName)); 									//debug
				//debug		
				System.out.println(String.valueOf(obj));															//debug	
			} catch(Exception e){System.out.println(fileName + "\t It is here.");};								//debug	
			e2.printStackTrace();
			status.update(e2.getMessage(),false,"fail");
			return null;
		}
	}

	public void _readfile(String validateType,String fileName, boolean isFileMode) {

		if(isFileMode)_readfile(validateType,fileName);

		try {

			if (!new File(fileName).exists()) return;

			else {

				boolean flag = true;

				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader(ResultOutputFile));
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject log = (JSONObject) jsonObject.get("log");
				JSONArray entries = (JSONArray) log.get("entries");





				switch(validateType.toLowerCase()) {
				case "ssl":
					console.add("\n********************");
					for(int i=0; i<entries.size(); i++) {
						JSONObject eachEntries = (JSONObject) entries.get(i);
						JSONObject request = (JSONObject) eachEntries.get("request");
						String url = (String) request.get("url");
						if(!validateURL(url,"SSL","")) {
							flag=false; //Supported = SSL, Contains specific text
							console.add("\n"+url.toString()+", Fail");
						}
						else console.add("\n"+url.toString()+", Pass");
					}
					console.add("\n********************");
					if(flag) status.update("All calls from this Web Page are SSL certified.",false,"pass");
					else status.update("There are some issues with the Network calls. Some of Calls does not have valid Certificate.",false,"fail");
					break;
				case "sameoriginpolicy":
					String whiteList[] = {"ixquick.com","ixquick.eu","startpage.com","startpage.info","ixquick.info"};
					for(int i=0; i<entries.size(); i++) {
						JSONObject eachEntries = (JSONObject) entries.get(i);
						JSONObject request = (JSONObject) eachEntries.get("request");
						String url = (String) request.get("url");

						if(!(validateURL(url,"sameorigin",whiteList[0]) ||
								validateURL(url,"sameorigin",whiteList[1]) ||
								validateURL(url,"sameorigin",whiteList[2]) ||
								validateURL(url,"sameorigin",whiteList[3]) ||
								validateURL(url,"sameorigin",whiteList[4]))) {
							flag=false;
							console.add("\n"+url.toString()+", Fail");
						}
						else console.add("\n"+url.toString()+", Pass");

					}
					console.add("\n********************");
					if(flag) status.update("All calls from this Web Page are under Same Origin Policy.",false,"pass");
					else status.update("There are calls that fails the Same Origin Policy.",false,"fail");
					break;
				}
			}
		} 
		catch (ParseException e1) {
			status.update(e1.getMessage(),false,"fail");			
		}
		catch (IOException e2) {
			status.update(e2.getMessage(),false,"fail");	
		}
	}

	public void readfile(String validateType ) {

		try {

			if (!new File(ResultOutputFile).exists()||txtUrlField.getText().isEmpty()){
				//status.update("First Load the URL!", true, "warn");
				//errrLoadUIredBorder();
				return;
			} 
			else {

				//status.update("Analyzing Network Calls...",false,"");	
				boolean flag = true;

				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader(ResultOutputFile));
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject log = (JSONObject) jsonObject.get("log");
				JSONArray entries = (JSONArray) log.get("entries");

				switch(validateType.toLowerCase()) {
				case "ssl":
					for(int i=0; i<entries.size(); i++) {
						JSONObject eachEntries = (JSONObject) entries.get(i);
						JSONObject request = (JSONObject) eachEntries.get("request");
						String url = (String) request.get("url");

						if(!validateURL(url,"SSL","")) {
							flag=false; //Supported = SSL, Contains specific text
							console.add("\n********************\n");
							console.add(url.toString());

						}
					}
					if(flag) status.update("All calls from this Web Page are SSL certified.",false,"pass");
					else status.update("There are some issues with the Network calls. Some of Calls does not have valid Certificate.",false,"fail");
					break;
				case "sameoriginpolicy":

					String baseWord = trimURL(txtUrlField.getText().toString());//txtUrlField.getText().replaceFirst("^(http://www\\.|http://|www\\.)","");
					for(int i=0; i<entries.size(); i++) {
						JSONObject eachEntries = (JSONObject) entries.get(i);
						JSONObject request = (JSONObject) eachEntries.get("request");
						String url = (String) request.get("url");

						if(!validateURL(url,"sameorigin",baseWord)) {
							flag=false; //Supported = SSL, Contains specific text
							console.add("\n********************\n");
							console.add(url.toString());
						}
					}
					if(flag) status.update("All calls from this Web Page are under Same Origin Policy.",false,"pass");
					else status.update("There are calls that fails the Same Origin Policy.",false,"fail");

					break;


				}

			}
		} 
		catch (ParseException e1) {
			status.update(e1.getMessage(),false,"fail");			
		}
		catch (IOException e2) {
			status.update(e2.getMessage(),false,"fail");	
		}

	}

	public boolean _readfile(String validateType,String ResultOutputFile) {

		try {
			if (!new File(ResultOutputFile).exists()||txtUrlField.getText().isEmpty()) return false;
			else {

				//boolean flag = true;
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader(ResultOutputFile));
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject log = (JSONObject) jsonObject.get("log");
				JSONArray entries = (JSONArray) log.get("entries");

				switch(validateType.toLowerCase()) {
				case "ssl":
					for(int i=0; i<entries.size(); i++) {
						JSONObject eachEntries = (JSONObject) entries.get(i);
						JSONObject request = (JSONObject) eachEntries.get("request");
						String url = (String) request.get("url");

						if(!validateURL(url,"SSL","")) return false;

					}
					return true;
				case "sameoriginpolicy":

					String whiteList[] = {"ixquick.com","ixquick.eu","startpage.com","startpage.info","ixquick.info"};
					for(int i=0; i<entries.size(); i++) {
						JSONObject eachEntries = (JSONObject) entries.get(i);
						JSONObject request = (JSONObject) eachEntries.get("request");
						String url = (String) request.get("url");


						if(!(validateURL(url,"sameorigin",whiteList[0]) ||
								validateURL(url,"sameorigin",whiteList[1]) ||
								validateURL(url,"sameorigin",whiteList[2]) ||
								validateURL(url,"sameorigin",whiteList[3]) ||
								validateURL(url,"sameorigin",whiteList[4]))) {
							return false;
						}

					}
					return true;
				}
			}
		}
		catch (ParseException e1) {
			status.update(e1.getMessage(),false,"fail");

		}
		catch (IOException e2) {
			status.update(e2.getMessage(),false,"fail");	
		}
		return false;

	}


	public static boolean validateURL(String Url, String ValidationType, String searchText){

		if(ValidationType.toLowerCase().contains("ssl")) return Url.toLowerCase().startsWith("https://");

		if(ValidationType.toLowerCase().contains("sameorigin")) return Url.toLowerCase().contains(searchText);

		return false;
	}

	public void checkSystem () {

		//status.update("Checking System...",false,"");

		try {
			File temp = File.createTempFile("temp", ".json");
			temp.deleteOnExit();
			ResultOutputFile=temp.getAbsolutePath();
			console.add("\n"+"Temp file created: "+ResultOutputFile);
		} catch(IOException e4) {status.update(e4.getMessage(),false,"fail");};

		try {
			pathToScript = ResourceManager.extract("/netsniff.js");
			console.add("\n"+"Script located: "+pathToScript);
		} catch(Exception e5) {status.update(e5.getMessage(),false,"fail");};

		try {

			String OS = System.getProperty("os.name").toLowerCase();
			if(OS.toLowerCase().contains("windows")){
				pathToBinary=ResourceManager.extract("/phantomjs.exe");
				console.add("\n"+"PhantomJS located: "+pathToBinary);
				query=settings.getFileMode()?"-999":(pathToBinary+" "+pathToScript+" \""+txtUrlField.getText()+"\" > "+ResultOutputFile);
				//builder = new ProcessBuilder("cmd.exe", "/c",query);
			}
			else if(OS.toLowerCase().contains("mac")){
				pathToBinary=ResourceManager.extract("/phantomjs");
				console.add("\n"+"PhantomJS located: "+pathToBinary);
				query=settings.getFileMode()?"-999":(pathToBinary+" "+pathToScript+" \""+txtUrlField.getText()+"\" > "+ResultOutputFile);
				//builder = new ProcessBuilder(query);
			}

		} catch(Exception e5) {status.update(e5.getMessage(),false,"fail");};

	}


	protected static boolean IsCorrectURL(String s) {
		try {
			String pattern= "^(http:\\/\\/|https:\\/\\/)(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.(com|io|info|org|net|eu|in)(\\/|[a-zA-Z]|.)*$";
			Pattern patt = Pattern.compile(pattern);
			Matcher matcher = patt.matcher(s);
			//System.out.println(matcher.matches()?"Match":"Not Match");
			return matcher.matches();
		} catch (RuntimeException e) {
			console.add(e);
			return false;
		}       
	}

	public static String trimURL (String s) {

		String pattern = "^(https:\\/\\/|http:\\/\\/)?(www\\.)?|\\.(com|io|info|org|net|eu|in)(\\/|[a-zA-Z]|.)*$";
		String temp= s.replaceAll(pattern, "");
		return temp;

	}

	public void load() {

		progressBar.setIndeterminate(true);
		try{

			if (txtUrlField.getText().length()==0) {
				border.setBorder(txtUrlField);
				progressBar.setIndeterminate(false);
				return;
			}	

			else if (txtUrlField.getText().startsWith("file://")&& 
					!new File(txtUrlField.getText().replace("file://", "")).exists()) {
				border.setBorder(txtUrlField);
				border.setBorder(btnFileUpload);
				status.update("Please provide valid file path.", true, "warn");
				progressBar.setIndeterminate(false);
				return;
			}

			else if(settings.getFileMode() && !IsCorrectURL(txtUrlField.getText().toString())) {
				border.setBorder(txtUrlField);
				border.setBorder(btnLoad);
				status.update("Please provide valid URL.<br>e.g. https://www.google.com/about/security/", true, "warn");
				progressBar.setIndeterminate(false);
				return;
			}

			else if (txtUrlField.getText().startsWith("file://")&& 
					new File(txtUrlField.getText().replace("file://", "")).exists()&&
					txtFileSaveDirectory.getText().length()<2) {
				border.setBorder(txtFileSaveDirectory);
				border.setBorder(btnFileSaveDirectory);
				progressBar.setIndeterminate(false);
				return;
			}

			else {
				status.update("Initializing things. Analyzing Network Calls...",false,"");
				txtAreaStatus.setText("Setting up the environment");
				executeMode(true);
				//resetMode(true);

				if(settings.getFileMode())	{	// &&		new File(txtUrlField.getText().replace("file://", "")).exists())
					//fileMode = true;
					//fileOfURL = txtUrlField.getText().replace("file://", "");
					ExecuteFileWorker worker = new ExecuteFileWorker();
					worker.execute();
				}
				else {
					//Successfull attempt
					//					checkSystem();
					//					_LoadDataWorker worker = new _LoadDataWorker();
					//					worker.execute();


					//2nd attempt
					ExecuteURLWorker worker = new ExecuteURLWorker();
					worker.execute();
				}

			}

			//executeMode(false);

		} catch(Exception e1) {
			executeMode(false);
			status.update("Something went wrong.",false,"fail");
			console.add(e1.getMessage());
			resetMode(true);
		}	


	}

	public void executeMode(boolean isActive) {

		if(isActive) btnClose.setText("Cancel & Close");

		frmCaptureNetworkCalls.setCursor(Cursor.getPredefinedCursor(isActive?Cursor.WAIT_CURSOR:Cursor.DEFAULT_CURSOR));
		progressBar.setIndeterminate(isActive);

		if(chkFileMode.isSelected()) panelFileMode.setEnabled(!isActive);
		chkFileMode.setEnabled(!isActive);
		txtUrlField.setEnabled(!isActive);
		btnFileUpload.setEnabled(!isActive);

		panelURLMode.setEnabled(!isActive);

		btnLoad.setEnabled(!isActive);


	}

	protected void resetMode(boolean isActive) {

		btnLoad.setVisible(!isActive);
		btnReset.setVisible(isActive);

		panelFileMode.setEnabled(!isActive);

		chkFileMode.setEnabled(!isActive);
		txtUrlField.setEnabled(!isActive);
		btnFileUpload.setEnabled(!isActive);

		panelURLMode.setEnabled(isActive);

		if(!isActive) {
			txtAreaStatus.setText("");
			txtUrlField.setText("");
			chkFileMode.setSelected(false);
		}

	}

	protected String _Execute(String OS, String pathToBinary, String pathToScript, String URL) {

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
				console.add("\n"+"chmod a+x "+pathToBinary);
				Runtime.getRuntime().exec("chmod a+x "+pathToBinary);
			} catch (IOException e1) {console.add("\n"+e1.getMessage());}

			builder = new ProcessBuilder("/bin/bash","-c",query);
		}

		try {
			Process p;
			p = builder.start();
			p.waitFor();
			p.destroy();

		} catch (IOException e2) {console.add("\n"+e2.getMessage());} 
		catch (InterruptedException e3) {console.add("\n"+e3.getMessage());}


		return _TempOutputFile.getAbsolutePath().toString();
	}


	public static int _CountFileLine(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
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
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}





