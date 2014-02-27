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
			File f = new File(logFilePath.getAbsolutePath()+"/"+logFilePath.list()[i]);
			//System.out.println(logFilePath.getAbsolutePath()+"/"+logFilePath.list()[i]);
			if (f.exists()) {
				if (f.isFile()) {
					//System.out.println("a"+f.getAbsolutePath());
					allText = allText
							+ "\n"
							+ readFileINTOString(f.getAbsolutePath()
									.toString());
				}
			}
		}

		String[] all_chunks = allText.split("com.navayo.secfone.sipua");

		// System.out.println(all_chunks[1]);
		// System.out.println(all_chunks[2]);
		// System.out.println(all_chunks[3]);

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
		ArrayList<CheckIN_File> baseOnlyVersion = new ArrayList<CheckIN_File>();

		Map<String, ArrayList<CheckIN_File>> voipGROUPED = new HashMap<String, ArrayList<CheckIN_File>>();
		Map<String, ArrayList<CheckIN_File>> baseGROUPED = new HashMap<String, ArrayList<CheckIN_File>>();
		Map<String, ArrayList<CheckIN_File>> smsGROUPED = new HashMap<String, ArrayList<CheckIN_File>>();

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
				if (ttt.voipVersionNumber.length() > 0) {
					voipVersion.add(ttt);

					if (ttt.versionTypeRelease.equals("release")) {
						if (ttt.versionTypeOrange.equals("orange")) {
							if (voipGROUPED.containsKey("release_orange")) {
								ArrayList<CheckIN_File> niza = voipGROUPED
										.get("release_orange");
								niza.add(ttt);
								voipGROUPED.put("release_orange", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								voipGROUPED.put("release_orange", niza);
							}
						} else {
							if (voipGROUPED.containsKey("release_red")) {
								ArrayList<CheckIN_File> niza = voipGROUPED
										.get("release_red");
								niza.add(ttt);
								voipGROUPED.put("release_red", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								voipGROUPED.put("release_red", niza);
							}
						}
					} else {
						if (ttt.versionTypeOrange.equals("orange")) {
							if (voipGROUPED.containsKey("debug_orange")) {
								ArrayList<CheckIN_File> niza = voipGROUPED
										.get("debug_orange");
								niza.add(ttt);
								voipGROUPED.put("debug_orange", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								voipGROUPED.put("debug_orange", niza);
							}
						} else {
							if (voipGROUPED.containsKey("debug_red")) {
								ArrayList<CheckIN_File> niza = voipGROUPED
										.get("debug_red");
								niza.add(ttt);
								voipGROUPED.put("debug_red", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								voipGROUPED.put("debug_red", niza);
							}
						}
					}
				}
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

				baseVersion.add(ttt);
				if (ttt.baseVersionNumber.length() > 0) {
					if (ttt.versionTypeRelease.equals("release")) {
						if (ttt.versionTypeOrange.equals("orange")) {
							if (baseGROUPED.containsKey("release_orange")) {
								ArrayList<CheckIN_File> niza = baseGROUPED
										.get("release_orange");
								niza.add(ttt);
								baseGROUPED.put("release_orange", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								baseGROUPED.put("release_orange", niza);
							}
						} else {
							if (baseGROUPED.containsKey("release_red")) {
								ArrayList<CheckIN_File> niza = baseGROUPED
										.get("release_red");
								niza.add(ttt);
								baseGROUPED.put("release_red", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								baseGROUPED.put("release_red", niza);
							}
						}
					} else {
						if (ttt.versionTypeOrange.equals("orange")) {
							if (baseGROUPED.containsKey("debug_orange")) {
								ArrayList<CheckIN_File> niza = baseGROUPED
										.get("debug_orange");
								niza.add(ttt);
								baseGROUPED.put("debug_orange", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								baseGROUPED.put("debug_orange", niza);
							}
						} else {
							if (baseGROUPED.containsKey("debug_red")) {
								ArrayList<CheckIN_File> niza = baseGROUPED
										.get("debug_red");
								niza.add(ttt);
								baseGROUPED.put("debug_red", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								baseGROUPED.put("debug_red", niza);
							}
						}
					}

				}
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
				if (ttt.smsVersionNumber.length() > 0) {
					smsVersion.add(ttt);

					if (ttt.versionTypeRelease.equals("release")) {
						if (ttt.versionTypeOrange.equals("orange")) {
							if (smsGROUPED.containsKey("release_orange")) {
								ArrayList<CheckIN_File> niza = smsGROUPED
										.get("release_orange");
								niza.add(ttt);
								smsGROUPED.put("release_orange", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								smsGROUPED.put("release_orange", niza);
							}
						} else {
							if (smsGROUPED.containsKey("release_red")) {
								ArrayList<CheckIN_File> niza = smsGROUPED
										.get("release_red");
								niza.add(ttt);
								smsGROUPED.put("release_red", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								smsGROUPED.put("release_red", niza);
							}
						}
					} else {
						if (ttt.versionTypeOrange.equals("orange")) {
							if (smsGROUPED.containsKey("debug_orange")) {
								ArrayList<CheckIN_File> niza = smsGROUPED
										.get("debug_orange");
								niza.add(ttt);
								smsGROUPED.put("debug_orange", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								smsGROUPED.put("debug_orange", niza);
							}
						} else {
							if (smsGROUPED.containsKey("debug_red")) {
								ArrayList<CheckIN_File> niza = smsGROUPED
										.get("debug_red");
								niza.add(ttt);
								smsGROUPED.put("debug_red", niza);
							} else {
								ArrayList<CheckIN_File> niza = new ArrayList<CheckIN_File>();
								niza.add(ttt);
								smsGROUPED.put("debug_red", niza);
							}
						}
					}

				}
			}

		}

		System.out.println("Voip(" + getNumberOfIntemsInArrays(voipGROUPED)
				+ "):");
		System.out.println();
		ArrayList<CheckIN_File> niz = voipGROUPED.get("release_orange");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = voipGROUPED.get("release_red");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = voipGROUPED.get("debug_orange");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = voipGROUPED.get("debug_red");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Base(" + getNumberOfIntemsInArrays(baseGROUPED)
				+ "):");
		System.out.println();
		niz = baseGROUPED.get("release_orange");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = baseGROUPED.get("release_red");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = baseGROUPED.get("debug_orange");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = baseGROUPED.get("debug_red");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("SMS(" + getNumberOfIntemsInArrays(smsGROUPED)
				+ "):");
		System.out.println();
		niz = smsGROUPED.get("release_orange");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = smsGROUPED.get("release_red");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = smsGROUPED.get("debug_orange");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
		System.out.println();
		niz = smsGROUPED.get("debug_red");
		if (niz != null) {
			for (int i = 0; i < niz.size(); i++) {
				System.out.println(niz.get(i).toString());
			}
		}
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

	// public static void nekaSedi(ArrayList<CheckIN_File> listaNaAllLogFiles) {
	// int i = 0;
	//
	// Map<String, ArrayList<CheckIN_File>> mapTypeCanonical = new
	// HashMap<String, ArrayList<CheckIN_File>>();
	// String log = listaNaAllLogFiles.get(i).canonicalName;
	//
	// if (log.length() > 0) {
	// // createDir(outputDirectory.getAbsolutePath() + "/"
	// // + listaNaAllLogFiles.get(i).canonicalName);
	//
	// // boolean daliNajdeMatch=false;
	// if (mapTypeCanonical.containsKey(log)) {
	// ArrayList<CheckIN_File> arrLogs = mapTypeCanonical.get(log);
	// arrLogs.add(listaNaAllLogFiles.get(i));
	// // mapTypeExceptions.put(log,arrLogs );
	// } else {
	// ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
	// arrLogs.add(listaNaAllLogFiles.get(i));
	// mapTypeCanonical.put(log, arrLogs);
	// }
	//
	// } else {
	// // bez canonikal
	// // createDir(outputDirectory.getAbsolutePath() + "/null");
	// String t123 = listaNaAllLogFiles.get(i).stackTrace;
	// if (t123.length() > 0) {
	// if (mapTypeCanonical.containsKey("null")) {
	// ArrayList<CheckIN_File> arrLogs = mapTypeCanonical
	// .get("null");
	// arrLogs.add(listaNaAllLogFiles.get(i));
	// // mapTypeExceptions.put(log,arrLogs );
	// } else {
	// ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
	// arrLogs.add(listaNaAllLogFiles.get(i));
	// mapTypeCanonical.put("null", arrLogs);
	// }
	// }
	// }
	//
	// // System.out.println("Number of all files = " +
	// // listaNaAllLogFiles.size());
	// // // System.out.println(mapTypeCanonical.keySet().size());
	// // // System.out.println(mapTypeCanonical.values().size());
	// // // System.out.println(mapTypeCanonical.size());
	// // for (String key : mapTypeCanonical.keySet()) {
	// // System.out.println("TYPE: \""+key+"\", number of files:"
	// // + mapTypeCanonical.get(key).size());
	// //
	// // // search po stackTrace
	// // ArrayList<LogFile> nizaOdPosebenTipNaGreska = mapTypeCanonical
	// // .get(key);
	// //
	// // int brojNaFolder = 0;
	// // for (int i = 0; i < nizaOdPosebenTipNaGreska.size(); i++) {
	// //
	// // String log111 = nizaOdPosebenTipNaGreska.get(i).stackTrace;
	// //
	// // if (log111.length() > 0) {
	// //
	// //
	// System.out.println("   File path:"+nizaOdPosebenTipNaGreska.get(i).path);
	// //
	// // // if (mapTypeExceptions.containsKey(log111)) {
	// // // ArrayList<LogFile> arrLogs = mapTypeExceptions
	// // // .get(log111);
	// // // arrLogs.add(mapTypeCanonical.get(key).get(i));
	// // //
	// // // // System.out.println("ISTI---------------------------"
	// // // // + i + "  "
	// // // // + mapTypeCanonical.get(key).get(i).path);
	// // //
	// // // if (mapTypeCanonical.get(key).get(i).canonicalName
	// // // .length() > 0) {
	// // //// createDir(outputDirectory.getAbsolutePath()
	// // //// + "/"
	// // //// + mapTypeCanonical.get(key).get(i).canonicalName
	// // //// + "/"
	// // //// + mapTypeCanonical.get(key).get(i).type);
	// // //
	// // // // vvv
	// // // copyFile(
	// // // mapTypeCanonical.get(key).get(i).path,
	// // // outputDirectory.getAbsolutePath()
	// // // + "/"
	// // // + mapTypeCanonical.get(key).get(i).canonicalName
	// // // + "/"
	// // // + mapTypeCanonical.get(key).get(i).path
	// // // .getName());
	// // //
	// // // } else {
	// // //// createDir(outputDirectory.getAbsolutePath() + "/"
	// // //// + "null" + "/"
	// // //// + mapTypeCanonical.get(key).get(i).type);
	// // // copyFile(
	// // // mapTypeCanonical.get(key).get(i).path,
	// // // outputDirectory.getAbsolutePath()
	// // // + "/"
	// // // + "null"
	// // // + "/"
	// // // + mapTypeCanonical.get(key).get(i).path
	// // // .getName());
	// // // }
	// // //
	// // // } else {
	// // // brojNaFolder++;
	// // // mapTypeCanonical.get(key).get(i).type = brojNaFolder;
	// //
	// // if (mapTypeCanonical.get(key).get(i).canonicalName
	// // .length() > 0) {
	// // // createDir(outputDirectory.getAbsolutePath()
	// // // + "/"
	// // // + mapTypeCanonical.get(key).get(i).canonicalName
	// // // + "/"
	// // // + mapTypeCanonical.get(key).get(i).type);
	// //
	// // // vvv
	// // copyFile(
	// // mapTypeCanonical.get(key).get(i).path,
	// // outputDirectory.getAbsolutePath()
	// // + "/"
	// // + mapTypeCanonical.get(key).get(i).canonicalName
	// // + "/"
	// // + mapTypeCanonical.get(key).get(i).path
	// // .getName());
	// //
	// // } else {
	// // createDir(outputDirectory.getAbsolutePath() + "/"
	// // + "null" + "/"
	// // + mapTypeCanonical.get(key).get(i).firstLine);
	// // copyFile(
	// // mapTypeCanonical.get(key).get(i).path,
	// // outputDirectory.getAbsolutePath()
	// // + "/"
	// // + "null"
	// // + "/" +mapTypeCanonical.get(key).get(i).firstLine +"/"
	// // + mapTypeCanonical.get(key).get(i).path
	// // .getName());
	// // }
	// //
	// // // System.out.println("NOVO---------------------------"
	// // // + i + "  "
	// // // + mapTypeCanonical.get(key).get(i).path);
	// //
	// // ArrayList<LogFile> arrLogs = new ArrayList<LogFile>();
	// // arrLogs.add(mapTypeCanonical.get(key).get(i));
	// // mapTypeExceptions.put(log111, arrLogs);
	// // //}
	// // }
	// //
	// // // if (nizaOdPosebenTipNaGreska.get(i).canonicalName.length() >
	// // // 0) {
	// // // System.out
	// // // .println(nizaOdPosebenTipNaGreska.get(i).canonicalName);
	// // // } else {
	// // //
	// // // }
	// // }
	// // // System.out.println(mapTypeExceptions.keySet().size());
	// // // System.out.println(mapTypeExceptions.values().size());
	// // // System.out.println("		Exception size:	" +
	// // mapTypeExceptions.size());
	// // // for (String key1 : mapTypeExceptions.keySet()) {
	// // // System.out.println("		path:"
	// // // + mapTypeExceptions.get(key1).get(0).path);
	// // // }
	// //
	// // }
	// //
	// // // System.out.println(mapTypeExceptions.keySet().size());
	// // // System.out.println(mapTypeExceptions.values().size());
	// // // System.out.println(listaNaAllLogFiles.size());
	// // // for (String key : mapTypeExceptions.keySet()) {
	// // // System.out.println(mapTypeExceptions.get(key).size());
	// // // }
	// //
	// //
	// //
	// //
	// // //###################################################################
	// //
	// //
	// //
	// //
	// //
	// //
	// //
	// //
	// //
	//
	// }

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
