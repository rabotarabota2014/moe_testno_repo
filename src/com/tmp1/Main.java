package com.tmp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ListCellRenderer;

public class Main {

	// kategorii
	// -exception (ist exception ke e vo ist folder)

	public static String secfoneVersion = "";

	// lista na mvcnIds so ako ja ima tuka nema da se zemi vo predvid toj
	// exception
	// vo smisla davas edna lista na mvcnID vo fajl, sekoj mvcn id vo nov red ,
	// i tie se ignorirat
	public static File blackListFile = null;

	public static boolean doNotCopy = true;

	public static File outputDirectory;

	public static ArrayList<CheckIN_File> all_checkInLogs = new ArrayList<CheckIN_File>();

	// vvv
	// /home/krste_ristevski/dev/test/checkIN/pureftp_by_ip/pureftp_10.0.0.1.log
	// ======================================

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Started...");

		File logFilePath = new File("");

		if (args.length != 1) {
			// usage
			// Feature #6241 Log parser for logs from the stacktrace server
			System.out
					.println("The app requires one argument containing the path to the folder with the logs");
			System.out
					.println("Example to start the application looks like this:");
			System.out
					.println("java -jar Log_Update_Parser.jar /home/krste_ristevski/11-02-exceptions/");
			System.out.println();
			return;
		}

		if (args.length == 1) {
			// System.out.println(args[0]);
			try {
				logFilePath = new File(args[0]);
			} catch (Exception e) {
				System.out
						.println("Please provide valid path to the folder with the log files");
				return;
			}

			if (!logFilePath.exists()) {
				System.out
						.println("Please provide valid path to the folder with the log files");
				return;
			}

			if (logFilePath.list() == null) {
				System.out
						.println("Please provide valid path to the folder with the log files");
				return;
			}

		} else {
			System.out
					.println("Example to start the application looks like this:");
			System.out
					.println("java -jar Log_Update_Parser.jar /home/krste_ristevski/11-02-exceptions/");
			return;
		}

		// getAllLines(logFilePath.getAbsoluteFile());

		System.out.println();
		String allText = "";
		// zemi go cel text

		for (int i = 0; i < logFilePath.list().length; i++) {
			File f = new File(logFilePath.getAbsolutePath() + "/"
					+ logFilePath.list()[i]);
			// System.out.println(logFilePath.getAbsolutePath()+"/"+logFilePath.list()[i]);
			if (f.exists()) {
				if (f.isFile()) {
					// System.out.println("a"+f.getAbsolutePath());
					allText = allText
							+ "\n"
							+ readFileINTOString(f.getAbsolutePath().toString());
				}
			}
		}

		//============================================================================
		String[] all_chunks = allText.split("com.navayo.secfone.sipua");
		CheckIN_File ch;
		for (int i = 1; i < all_chunks.length - 1; i++) {
			// System.out.println();
			// System.out.println("line:"+all_chunks[i]);
			// 1 voip, 2 base, 3 sms
			ch = Parser.parseCheckIN_byPackageName(all_chunks[i], 1);

			all_checkInLogs.add(ch);

			// if (ch.packageName == null) {
			// System.out.println("null package name");
			// } else {
			// if (ch.packageName.length() > 0) {
			// all_checkInLogs.add(ch);
			// }
			// }

		}

		// if(1<10)return;

		// System.out.println();

		ArrayList<CheckIN_File> voipVersion = new ArrayList<CheckIN_File>();
		ArrayList<CheckIN_File> baseVersion = new ArrayList<CheckIN_File>();
		ArrayList<CheckIN_File> smsVersion = new ArrayList<CheckIN_File>();
		ArrayList<CheckIN_File> hisecVersion = new ArrayList<CheckIN_File>();
		ArrayList<CheckIN_File> baseOnlyVersion = new ArrayList<CheckIN_File>();

		Map<String, ArrayList<CheckIN_File>> voipGROUPED = new HashMap<String, ArrayList<CheckIN_File>>();
		Map<String, ArrayList<CheckIN_File>> baseGROUPED = new HashMap<String, ArrayList<CheckIN_File>>();
		Map<String, ArrayList<CheckIN_File>> smsGROUPED = new HashMap<String, ArrayList<CheckIN_File>>();
		Map<String, ArrayList<CheckIN_File>> hisecGROUPED = new HashMap<String, ArrayList<CheckIN_File>>();

		// System.out.println("All update logs:" + all_checkInLogs.size());
		for (int i = 0; i < all_checkInLogs.size(); i++) {

			CheckIN_File ttt = all_checkInLogs.get(i);

			boolean voipAdd = true;
			for (int j = 0; j < voipVersion.size(); j++) {
				if (i == j)
					continue;
				// System.out.println(j);
				if (ttt.isVoipEqual(voipVersion.get(j))) {
					voipAdd = false;
					break;
				}
			}
			if (voipAdd) {
				addToTheList(1,ttt,voipVersion,voipGROUPED);
			}

		}

		System.out.println();
		all_checkInLogs.clear();
		all_chunks = allText.split("com.navayo.secfone.base");
		for (int i = 1; i < all_chunks.length - 1; i++) {
			// 1 voip, 2 base, 3 sms
			ch = Parser.parseCheckIN_byPackageName(all_chunks[i], 2);
			all_checkInLogs.add(ch);
		}

		for (int i = 0; i < all_checkInLogs.size(); i++) {
			CheckIN_File ttt = all_checkInLogs.get(i);
			boolean baseAdd = true;
			for (int j = 0; j < baseVersion.size(); j++) {
				if (i == j)
					continue;
				// System.out.println(j);
				if (ttt.isBaseEqual(baseVersion.get(j))) {
					baseAdd = false;
					break;
				}
			}
			if (baseAdd) {
				addToTheList(2,ttt,baseVersion,baseGROUPED);
			}
		}

		System.out.println();
		all_checkInLogs.clear();
		all_chunks = allText.split("com.navayo.secfone.sms.android");
		for (int i = 1; i < all_chunks.length - 1; i++) {
			// 1 voip, 2 base, 3 sms
			ch = Parser.parseCheckIN_byPackageName(all_chunks[i], 3);
			all_checkInLogs.add(ch);
		}

		for (int i = 0; i < all_checkInLogs.size(); i++) {
			CheckIN_File ttt = all_checkInLogs.get(i);

			boolean smsAdd = true;
			for (int j = 0; j < smsVersion.size(); j++) {
				if (i == j)
					continue;
				// System.out.println(j);
				if (ttt.isSmsEqual(smsVersion.get(j))) {
					smsAdd = false;
					break;
				}
			}
			if (smsAdd) {
				addToTheList(3,ttt,smsVersion,smsGROUPED);
			}

		}
		/////////////////////////////////////////////
		
		
//		System.out.println();
//		all_checkInLogs.clear();
//		all_chunks = allText.split("com.navayo.secfone.base");
//		for (int i = 1; i < all_chunks.length - 1; i++) {
//			// 1 voip, 2 base, 3 sms,4 hisec
//			ch = Parser.parseCheckIN_byPackageName(all_chunks[i], 4);
//			all_checkInLogs.add(ch);
//		}
//
//		for (int i = 0; i < all_checkInLogs.size(); i++) {
//			CheckIN_File ttt = all_checkInLogs.get(i);
//
//			boolean hisecAdd = true;
//			for (int j = 0; j < smsVersion.size(); j++) {
//				if (i == j)
//					continue;
//				// System.out.println(j);
//				if (ttt.isSmsEqual(smsVersion.get(j))) {
//					hisecAdd = false;
//					break;
//				}
//			}
//			if (hisecAdd) {
//				addToTheList(4,ttt,hisecVersion,hisecGROUPED);
//			}
//
//		}

		System.out.println("Voip(" + getNumberOfIntemsInArrays(voipGROUPED)
				+ "):");
		System.out.println();
		printaj(voipGROUPED.get("release_orange"));
		printaj(voipGROUPED.get("release_red"));
		printaj(voipGROUPED.get("debug_orange"));
		printaj(voipGROUPED.get("debug_red"));
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Base(" + getNumberOfIntemsInArrays(baseGROUPED)
				+ "):");
		System.out.println();
		printaj(baseGROUPED.get("release_orange"));
		printaj(baseGROUPED.get("release_red"));
		printaj(baseGROUPED.get("debug_orange"));
		printaj(baseGROUPED.get("debug_red"));
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("SMS(" + getNumberOfIntemsInArrays(smsGROUPED)
				+ "):");
		System.out.println();
		printaj(smsGROUPED.get("release_orange"));
		printaj(smsGROUPED.get("release_red"));
		printaj(smsGROUPED.get("debug_orange"));
		printaj(smsGROUPED.get("debug_red"));
		// OVA BESE PO STARO
		// System.out.println();
		// Collections.sort(voipVersion, new MyComparator());
		// System.out.println("Voip(" + voipVersion.size() + "):");
		// for (int j = 0; j < voipVersion.size(); j++) {
		// System.out.println("	" + voipVersion.get(j).voipToString());
		// }
		// System.out.println();
		//
		// System.out.println("Base(" + baseVersion.size() + "):");
		// Collections.sort(baseVersion, new MyComparator());
		// for (int j = 0; j < baseVersion.size(); j++) {
		// System.out.println("	" + baseVersion.get(j).baseToString());
		// }
		// System.out.println();
		//
		// System.out.println("Sms(" + smsVersion.size() + "):");
		// Collections.sort(smsVersion, new MyComparator());
		// for (int j = 0; j < smsVersion.size(); j++) {
		// System.out.println("	" + smsVersion.get(j).smsToString());
		// }
		// System.out.println();
		// end OVA BESE PO STARO
		// System.out.println("backend(" + baseOnlyVersion.size() + "):");
		// for (int j = 0; j < baseOnlyVersion.size(); j++) {
		// System.out.println("	" + baseOnlyVersion.get(j).backendToString());
		// }
		// System.out.println();

		System.out.println();
		System.out.println("End.");

		// -----------------------
	}

	
	//1=voip,2=base,3-sms
	public static void addToTheList(int AppType,CheckIN_File ttt, ArrayList<CheckIN_File> appVersion, Map<String, ArrayList<CheckIN_File>> appGROUPED){
		if(AppType==1){
			if (ttt.voipVersionNumber.length() > 0) {
				
			}else{
				return;
			}
		}
		
		if(AppType==2){
			if (ttt.baseVersionNumber.length() > 0) {
				
			}else{
				return;
			}
		}
		
		if(AppType==3){
			if (ttt.smsVersionNumber.length() > 0) {
				
			}else{
				return;
			}
		}
			appVersion.add(ttt);

			if (ttt.versionTypeRelease.equals("release")) {
				if (ttt.versionTypeOrange.equals("orange")) {
					if (appGROUPED.containsKey("release_orange")) {
						ArrayList<CheckIN_File> niza = appGROUPED
								.get("release_orange");
						niza.add(ttt);
						appGROUPED.put("release_orange", niza);
					} else {
						ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
						niza.add(ttt);
						appGROUPED.put("release_orange", niza);
					}
				} else {
					if (appGROUPED.containsKey("release_red")) {
						ArrayList<CheckIN_File> niza = appGROUPED
								.get("release_red");
						niza.add(ttt);
						appGROUPED.put("release_red", niza);
					} else {
						ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
						niza.add(ttt);
						appGROUPED.put("release_red", niza);
					}
				}
			} else {
				if (ttt.versionTypeOrange.equals("orange")) {
					if (appGROUPED.containsKey("debug_orange")) {
						ArrayList<CheckIN_File> niza = appGROUPED
								.get("debug_orange");
						niza.add(ttt);
						appGROUPED.put("debug_orange", niza);
					} else {
						ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
						niza.add(ttt);
						appGROUPED.put("debug_orange", niza);
					}
				} else {
					if (appGROUPED.containsKey("debug_red")) {
						ArrayList<CheckIN_File> niza = appGROUPED
								.get("debug_red");
						niza.add(ttt);
						appGROUPED.put("debug_red", niza);
					} else {
						ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
						niza.add(ttt);
						appGROUPED.put("debug_red", niza);
					}
				}
			}
	}
	
	
	
	private static void printaj(ArrayList<CheckIN_File> arrayList) {
		if (arrayList != null) {
			Collections.sort(arrayList, new MyComparator());
			for (int i = 0; i < arrayList.size(); i++) {
				System.out.println(arrayList.get(i).toString());
			}
		}
		System.out.println();
	}

	private static int getNumberOfIntemsInArrays(
			Map<String, ArrayList<CheckIN_File>> voipGROUPED) {
		int num = 0;
		for (String key : voipGROUPED.keySet()) {
			num += voipGROUPED.get(key).size();
		}
		return num;
	}

	// -------------------------------------------
	public static ArrayList<File> listAllFiles(String path) {
		ArrayList<File> filePaths = new ArrayList<File>();
		filePaths.clear();
		String files;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				// if (files.endsWith(".txt") || files.endsWith(".TXT")) {
				// System.out.println(files);
				// }
				// System.out.println(listOfFiles[i].getAbsolutePath());
				filePaths.add(listOfFiles[i]);
			}
		}
		return filePaths;
	}

	public static String readFileAsString(String filePath) {
		try {
			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			return fileData.toString();
		} catch (Exception e) {

		}
		return "";
	}

	public static void createDir(String path) {
		if (doNotCopy)
			return;
		File theDir = new File(path);
		// System.out.println(path);
		// if the directory does not exist, create it
		if (!theDir.exists()) {
			boolean result = theDir.mkdir();
			// if(result) {
			// System.out.println("DIR created");
			// }
		}
	}

	public static String removeInvaliChars(String text) {
		String s = "";
		for (int i = 0; i < text.length(); i++) {
			if (Character.isLetter(text.charAt(i))) {
				s = s + text.charAt(i);
			}
			if (Character.isDigit(text.charAt(i))) {
				s = s + text.charAt(i);
			}
		}
		return s;
	}

	public static void copyFile(File sourceFile1, String destFile1) {
		if (doNotCopy)
			return;
		copyFile(sourceFile1.toString(), destFile1);
	}

	public static int kopija = 0;

	public static void copyFile(String sourceFile1, String destFile1) {
		if (doNotCopy)
			return;
		// System.out.println("kopija broj"+kopija++);
		File sourceFile = new File(sourceFile1);
		File destFile = new File(destFile1);

		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			inStream = new FileInputStream(sourceFile);
			outStream = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("sourceFile1 " + sourceFile1 + "       "
					+ "destFile1       " + destFile1);
		}
	}


	public static ArrayList<String> getBlackList(File blackListarg) {
		ArrayList<String> list = new ArrayList<String>();
		String s = readFileAsString(blackListarg.getAbsolutePath());
		// System.out.println("List of mvcn ids that will be igrored during the log parsing:");
		// System.out.println(s);
		// System.out.println();

		for (int i = 0; i < s.split("\n").length; i++) {
			// if((s.split("\n")[i]).length()>0){
			list.add((s.split("\n")[i]).trim());
			// System.out.println(s.split("\n")[i]);
			// }
		}
		return list;
	}

	public static ArrayList<String> getAllLines(File logPath) {
		String s1 = "";

		ArrayList<String> list = new ArrayList<String>();
		String s = readFileAsString(logPath.getAbsolutePath());
		// System.out.println("List of mvcn ids that will be igrored during the log parsing:");
		// System.out.println(s);
		// System.out.println();

		for (int i = 0; i < s.split("\n").length; i++) {
			// System.out.println(s.split("\n")[i]);
			// if((s.split("\n")[i]).length()>0){
			list.add((s.split("\n")[i]).trim());
			System.out.println(i);
			s1 = s1 + (s.split("\n")[i]).trim();
			// System.out.println(s.split("\n")[i]);
			// }
		}

		System.out.println(s1);
		return list;
	}

	public static String readFileINTOString(String pathname) {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			String lineSeparator = System.getProperty("line.separator");

			while (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} catch (Exception e) {
			return "";

		}
		// } finally {
		// scanner.close();
		// }
	}
}
