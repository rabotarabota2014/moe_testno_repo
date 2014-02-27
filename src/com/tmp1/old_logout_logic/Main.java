package com.tmp1.old_logout_logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.ListCellRenderer;

public class Main {

	// kategorii
	// -exception (ist exception ke e vo ist folder)

	public static String secfoneVersion = "";

	public static Map<String, ArrayList<CheckIN_File>> androidOsBuildMODEL = new HashMap<String, ArrayList<CheckIN_File>>();
	public static Map<String, ArrayList<CheckIN_File>> canonicalName = new HashMap<String, ArrayList<CheckIN_File>>();
	public static Map<String, ArrayList<CheckIN_File>> versionName = new HashMap<String, ArrayList<CheckIN_File>>();

	public static Map<String, ArrayList<CheckIN_File>> canonicalName1 = new HashMap<String, ArrayList<CheckIN_File>>();
	public static Map<String, ArrayList<CheckIN_File>> canonicalName2 = new HashMap<String, ArrayList<CheckIN_File>>();
	public static Map<String, ArrayList<CheckIN_File>> canonicalName3 = new HashMap<String, ArrayList<CheckIN_File>>();

	// lista na mvcnIds so ako ja ima tuka nema da se zemi vo predvid toj
	// exception
	// vo smisla davas edna lista na mvcnID vo fajl, sekoj mvcn id vo nov red ,
	// i tie se ignorirat
	public static File blackListFile = null;

	public static boolean doNotCopy = true;

	public static File outputDirectory;

	public static ArrayList<CheckIN_File> all_checkInLogs = new ArrayList<CheckIN_File>();

	public static void main123123(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Started...");

		if (args.length != 1) {
			// usage
			// Feature #6241 Log parser for logs from the stacktrace server
			System.out
					.println("The app requires one argument containing the path to the file with the log");
			System.out
					.println("Example to start the application looks like this:");
			System.out
					.println("java -jar Log_Update_Parser.jar /home/krste_ristevski/11-02-exceptions/ftp.log");
			System.out.println();
			return;
		}

		File logFilePath = new File("");

		if (args.length == 1) {
			// System.out.println(args[0]);
			try {
				logFilePath = new File(args[0]);
			} catch (Exception e) {
				System.out.println("Please provide valid path to the log path");
				return;
			}

			if (!logFilePath.exists()) {
				System.out.println("Please provide valid path to the log file");
				return;
			}

		} else {
			System.out
					.println("Example to start the application looks like this:");
			System.out
					.println("java -jar Log_Update_Parser.jar /home/krste_ristevski/11-02-exceptions/ftp.log");
			return;
		}

		// getAllLines(logFilePath.getAbsoluteFile());

		System.out.println();
		// zemi go cel text
		String allText = readFileINTOString(logFilePath.getAbsolutePath()
				.toString());
		String[] all_chunks = allText.split("Logout");

		// System.out.println(all_chunks[1]);
		// System.out.println(all_chunks[2]);
		// System.out.println(all_chunks[3]);

		CheckIN_File ch;

		for (int i = 1; i < all_chunks.length - 1; i++) {

			ch = Parser.parseCheckIN(all_chunks[i]);

			if (ch.packageName == null) {
				System.out.println("null package name");
			} else {
				if (ch.packageName.length() > 0) {
					all_checkInLogs.add(ch);
				}
			}

			if (i > 15000000)
				break;
		}

		System.out.println();

		ArrayList<CheckIN_File> voipVersion = new ArrayList<CheckIN_File>();
		ArrayList<CheckIN_File> baseVersion = new ArrayList<CheckIN_File>();
		ArrayList<CheckIN_File> smsVersion = new ArrayList<CheckIN_File>();
		ArrayList<CheckIN_File> baseOnlyVersion = new ArrayList<CheckIN_File>();

		System.out.println("All update logs:" + all_checkInLogs.size());
		for (int i = 0; i < all_checkInLogs.size(); i++) {

			CheckIN_File ttt = all_checkInLogs.get(i);

			boolean voipAdd = true;
			for (int j = 0; j < voipVersion.size(); j++) {
				if(i==j)continue;
				//System.out.println(j);
				if (ttt.isVoipEqual(voipVersion.get(j))) {
					voipAdd = false;
					break;
				}
			}
			if (voipAdd) {
				if (ttt.voipVersionNumber.length() > 0) {
					voipVersion.add(ttt);
				}
			}
			
			
			
			boolean baseAdd = true;
			for (int j = 0; j < baseVersion.size(); j++) {
				if(i==j)continue;
				//System.out.println(j);
				if (ttt.isBaseEqual(baseVersion.get(j))) {
					baseAdd = false;
					break;
				}
			}
			if (baseAdd) {
				if (ttt.baseVersionNumber.length() > 0) {
					baseVersion.add(ttt);
				}
			}
			
			
			
			
			boolean smsAdd = true;
			for (int j = 0; j < smsVersion.size(); j++) {
				if(i==j)continue;
				//System.out.println(j);
				if (ttt.isSmsEqual(smsVersion.get(j))) {
					smsAdd = false;
					break;
				}
			}
			if (smsAdd) {
				if (ttt.smsVersionNumber.length() > 0) {
					smsVersion.add(ttt);
				}
			}
			
			
			
			boolean backendAdd = true;
			for (int j = 0; j < baseOnlyVersion.size(); j++) {
				if(i==j)continue;
				//System.out.println(j);
				if (ttt.isbackendEqual(baseOnlyVersion.get(j))) {
					backendAdd = false;
					break;
				}
			}
			if (backendAdd) {
				if (ttt.baseONLYVersionNumber.length() > 0) {
					baseOnlyVersion.add(ttt);
				}
			}
			
			

			
			
			 
		}
		System.out.println();

		System.out.println("Voip("
				+ voipVersion.size() + "):");
		for (int j = 0; j < voipVersion.size(); j++) {
			System.out.println("	"+voipVersion.get(j).voipToString());
		}
		System.out.println();

		
		
		System.out.println("Base("
				+ baseVersion.size() + "):");
		for (int j = 0; j < baseVersion.size(); j++) {
			System.out.println("	"+baseVersion.get(j).baseToString());
		}
		System.out.println();
		
		
		System.out.println("Sms("
				+ smsVersion.size() + "):");
		for (int j = 0; j < smsVersion.size(); j++) {
			System.out.println("	"+smsVersion.get(j).smsToString());
		}
		System.out.println();
		
		System.out.println("backend("
				+ baseOnlyVersion.size() + "):");
		for (int j = 0; j < baseOnlyVersion.size(); j++) {
			System.out.println("	"+baseOnlyVersion.get(j).backendToString());
		}
		System.out.println();
		
		
		
		
		
		System.out.println();
		System.out.println("End.");

		if (1 < 10)
			return;
		// File outputDirectory = new
		// File("/home/krste_ristevski/dev/test/remoteServerZaGolub/remoteServer/000");
		outputDirectory = new File(
				"/home/krste_ristevski/dev/test/remoteServerZaGolub/remoteServer/000");
		createDir(outputDirectory.getAbsolutePath());

		ArrayList<CheckIN_File> listaNaAllLogFiles = new ArrayList<CheckIN_File>();

		// lista tupovi na exceptions
		Map<String, ArrayList<CheckIN_File>> mapTypeExceptions = new HashMap<String, ArrayList<CheckIN_File>>();
		// istko kako gore samo da nema duplikati
		Map<String, ArrayList<CheckIN_File>> mapTypeUNIQUEExceptions = new HashMap<String, ArrayList<CheckIN_File>>();

		Map<String, ArrayList<CheckIN_File>> mapTypeFirstLine = new HashMap<String, ArrayList<CheckIN_File>>();
		// vvv argImplement

		// ----------napolni LogFile listata
		// ArrayList<File> listataFiles =
		// listAllFiles("/home/krste_ristevski/dev/test/remoteServerZaGolub/remoteServer/remoteServer/");
		ArrayList<File> listataFiles = listAllFiles(logFilePath.toString());
		for (int i = 0; i < listataFiles.size(); i++) {
			File f = listataFiles.get(i);
			String textot = readFileAsString(f.getAbsolutePath());
			CheckIN_File lf = new CheckIN_File();
			lf.path = f;

			// search po version
			if (secfoneVersion.length() > 0) {
				if (!lf.path.getAbsolutePath().contains(secfoneVersion)) {
					continue;
				}
			}
			lf.stackTrace = Parser.getStackTrace(textot);
			lf.canonicalName = Parser.getCanonicalType(textot);
			lf.firstLine = Parser.getFirstLine(textot);
			// System.out.println(lf.canonicalName);
			lf.androidOsBuildMODEL = Parser.getandroidOsBuildMODEL(textot);
			lf.versionName = Parser.getVersionName(textot);

			lf.mvcnidName = Parser.getMvcnID(lf.path.getName());

			// FILTER 'black list'
			// if (!blackList.contains(lf.mvcnidName)) {
			// listaNaAllLogFiles.add(lf);
			// // System.out.println(lf.mvcnidName);
			// }
		}
		// -----------------------

		// for (int i = 0; i < listaNaAllLogFiles.size(); i++) {
		// String log = listaNaAllLogFiles.get(i).stackTrace;
		// if (log.length() > 0) {
		// // System.out.println(log);
		// //
		// System.out.println(listaNaAllLogFiles.get(i).path.getAbsoluteFile());
		//
		// // boolean daliNajdeMatch=false;
		// if (mapTypeExceptions.containsKey(log)) {
		// ArrayList<LogFile> arrLogs = mapTypeExceptions.get(log);
		// arrLogs.add(listaNaAllLogFiles.get(i));
		// // mapTypeExceptions.put(log,arrLogs );
		// } else {
		// ArrayList<LogFile> arrLogs = new ArrayList<LogFile>();
		// arrLogs.add(listaNaAllLogFiles.get(i));
		// mapTypeExceptions.put(log, arrLogs);
		// }
		//
		// }
		// }

		// ####################### CANONICAL NAME

		listCanonical(listaNaAllLogFiles);
		listandroidOsBuildMODEL(listaNaAllLogFiles);
		listVersionName(listaNaAllLogFiles);

		listMVCNname(listaNaAllLogFiles);
		// for (int i = 0; i < listaNaAllLogFiles.size(); i++) {
		// //tuka bese sve porano :)
		// }
		// =====================================================

		//
		// System.out.println("Number of all files = " +
		// listaNaAllLogFiles.size());
		// // System.out.println(mapTypeCanonical.keySet().size());
		// // System.out.println(mapTypeCanonical.values().size());
		// // System.out.println(mapTypeCanonical.size());
		// for (String key : mapTypeCanonical.keySet()) {
		// System.out.println("TYPE: \""+key+"\", number of files:"
		// + mapTypeCanonical.get(key).size());
		//
		// // search po stackTrace
		// ArrayList<LogFile> nizaOdPosebenTipNaGreska = mapTypeCanonical
		// .get(key);
		//
		// int brojNaFolder = 0;
		// for (int i = 0; i < nizaOdPosebenTipNaGreska.size(); i++) {
		//
		// String log111 = nizaOdPosebenTipNaGreska.get(i).stackTrace;
		//
		// if (log111.length() > 0) {
		//
		// System.out.println("   File path:"+nizaOdPosebenTipNaGreska.get(i).path);
		//
		// // if (mapTypeExceptions.containsKey(log111)) {
		// // ArrayList<LogFile> arrLogs = mapTypeExceptions
		// // .get(log111);
		// // arrLogs.add(mapTypeCanonical.get(key).get(i));
		// //
		// // // System.out.println("ISTI---------------------------"
		// // // + i + "  "
		// // // + mapTypeCanonical.get(key).get(i).path);
		// //
		// // if (mapTypeCanonical.get(key).get(i).canonicalName
		// // .length() > 0) {
		// //// createDir(outputDirectory.getAbsolutePath()
		// //// + "/"
		// //// + mapTypeCanonical.get(key).get(i).canonicalName
		// //// + "/"
		// //// + mapTypeCanonical.get(key).get(i).type);
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
		// //// createDir(outputDirectory.getAbsolutePath() + "/"
		// //// + "null" + "/"
		// //// + mapTypeCanonical.get(key).get(i).type);
		// // copyFile(
		// // mapTypeCanonical.get(key).get(i).path,
		// // outputDirectory.getAbsolutePath()
		// // + "/"
		// // + "null"
		// // + "/"
		// // + mapTypeCanonical.get(key).get(i).path
		// // .getName());
		// // }
		// //
		// // } else {
		// // brojNaFolder++;
		// // mapTypeCanonical.get(key).get(i).type = brojNaFolder;
		//
		// if (mapTypeCanonical.get(key).get(i).canonicalName
		// .length() > 0) {
		// // createDir(outputDirectory.getAbsolutePath()
		// // + "/"
		// // + mapTypeCanonical.get(key).get(i).canonicalName
		// // + "/"
		// // + mapTypeCanonical.get(key).get(i).type);
		//
		// // vvv
		// copyFile(
		// mapTypeCanonical.get(key).get(i).path,
		// outputDirectory.getAbsolutePath()
		// + "/"
		// + mapTypeCanonical.get(key).get(i).canonicalName
		// + "/"
		// + mapTypeCanonical.get(key).get(i).path
		// .getName());
		//
		// } else {
		// createDir(outputDirectory.getAbsolutePath() + "/"
		// + "null" + "/"
		// + mapTypeCanonical.get(key).get(i).firstLine);
		// copyFile(
		// mapTypeCanonical.get(key).get(i).path,
		// outputDirectory.getAbsolutePath()
		// + "/"
		// + "null"
		// + "/" +mapTypeCanonical.get(key).get(i).firstLine +"/"
		// + mapTypeCanonical.get(key).get(i).path
		// .getName());
		// }
		//
		// // System.out.println("NOVO---------------------------"
		// // + i + "  "
		// // + mapTypeCanonical.get(key).get(i).path);
		//
		// ArrayList<LogFile> arrLogs = new ArrayList<LogFile>();
		// arrLogs.add(mapTypeCanonical.get(key).get(i));
		// mapTypeExceptions.put(log111, arrLogs);
		// //}
		// }
		//
		// // if (nizaOdPosebenTipNaGreska.get(i).canonicalName.length() >
		// // 0) {
		// // System.out
		// // .println(nizaOdPosebenTipNaGreska.get(i).canonicalName);
		// // } else {
		// //
		// // }
		// }
		// // System.out.println(mapTypeExceptions.keySet().size());
		// // System.out.println(mapTypeExceptions.values().size());
		// // System.out.println("		Exception size:	" +
		// mapTypeExceptions.size());
		// // for (String key1 : mapTypeExceptions.keySet()) {
		// // System.out.println("		path:"
		// // + mapTypeExceptions.get(key1).get(0).path);
		// // }
		//
		// }
		//
		// // System.out.println(mapTypeExceptions.keySet().size());
		// // System.out.println(mapTypeExceptions.values().size());
		// // System.out.println(listaNaAllLogFiles.size());
		// // for (String key : mapTypeExceptions.keySet()) {
		// // System.out.println(mapTypeExceptions.get(key).size());
		// // }
		//
		//
		//
		//
		// //###################################################################
		//
		//
		//
		//
		//
		//
		//
		//
		//

	}

	public static void listMVCNname(ArrayList<CheckIN_File> listaNaAllLogFiles) {

		Map<String, ArrayList<CheckIN_File>> reportList = new HashMap<String, ArrayList<CheckIN_File>>();

		for (int i = 0; i < listaNaAllLogFiles.size(); i++) {

			// mvcnname
			String str_mvcnidName = listaNaAllLogFiles.get(i).mvcnidName;
			createDir(outputDirectory.getAbsolutePath() + "/versionName/");
			if (str_mvcnidName.length() > 0) {
				createDir(outputDirectory.getAbsolutePath() + "/versionName/"
						+ listaNaAllLogFiles.get(i).versionName);
				if (versionName.containsKey(str_mvcnidName)) {
					ArrayList<CheckIN_File> arrLogs = versionName
							.get(str_mvcnidName);
					arrLogs.add(listaNaAllLogFiles.get(i));
					reportList.put(str_mvcnidName, arrLogs);

					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath() + "/versionName/"
									+ listaNaAllLogFiles.get(i).versionName
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());

				} else {
					ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
					arrLogs.add(listaNaAllLogFiles.get(i));
					versionName.put(str_mvcnidName, arrLogs);

					reportList.put(str_mvcnidName, arrLogs);
					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath() + "/versionName/"
									+ listaNaAllLogFiles.get(i).versionName
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());
				}

			} else {
				// bez canonikal
				createDir(outputDirectory.getAbsolutePath()
						+ "/versionName/null/");
				String t123 = listaNaAllLogFiles.get(i).stackTrace;
				if (t123.length() > 0) {
					if (canonicalName.containsKey("null")) {
						ArrayList<CheckIN_File> arrLogs = canonicalName
								.get("null");
						arrLogs.add(listaNaAllLogFiles.get(i));

						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/versionName/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());

						reportList.put(str_mvcnidName, arrLogs);
					} else {
						ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
						arrLogs.add(listaNaAllLogFiles.get(i));
						versionName.put("null", arrLogs);
						reportList.put(str_mvcnidName, arrLogs);
						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/versionName/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());
					}
				}
			}

			// versionName
			// versionNameversionNameversionNameversionNameversionNameversionName
		}
		System.out.println("Different mvcnids:" + reportList.size());
		for (String key1 : reportList.keySet()) {
			System.out.println("	mvcnid: " + key1 + " ("
					+ reportList.get(key1).size() + ")");
			for (int j = 0; j < reportList.get(key1).size(); j++) {
				System.out.println("		path: "
						+ reportList.get(key1).get(j).path);
			}
		}

		System.out.println();
	}

	public static void listVersionName(
			ArrayList<CheckIN_File> listaNaAllLogFiles) {

		Map<String, ArrayList<CheckIN_File>> reportList = new HashMap<String, ArrayList<CheckIN_File>>();

		for (int i = 0; i < listaNaAllLogFiles.size(); i++) {

			// versionName
			// versionNameversionNameversionNameversionNameversionNameversionNameversionName
			String str_versionName = listaNaAllLogFiles.get(i).versionName;
			createDir(outputDirectory.getAbsolutePath() + "/versionName/");
			if (str_versionName.length() > 0) {
				createDir(outputDirectory.getAbsolutePath() + "/versionName/"
						+ listaNaAllLogFiles.get(i).versionName);
				if (versionName.containsKey(str_versionName)) {
					ArrayList<CheckIN_File> arrLogs = versionName
							.get(str_versionName);
					arrLogs.add(listaNaAllLogFiles.get(i));
					reportList.put(str_versionName, arrLogs);

					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath() + "/versionName/"
									+ listaNaAllLogFiles.get(i).versionName
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());

				} else {
					ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
					arrLogs.add(listaNaAllLogFiles.get(i));
					versionName.put(str_versionName, arrLogs);
					reportList.put(str_versionName, arrLogs);
					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath() + "/versionName/"
									+ listaNaAllLogFiles.get(i).versionName
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());
				}

			} else {
				// bez canonikal
				createDir(outputDirectory.getAbsolutePath()
						+ "/versionName/null/");
				String t123 = listaNaAllLogFiles.get(i).stackTrace;
				if (t123.length() > 0) {
					if (canonicalName.containsKey("null")) {
						ArrayList<CheckIN_File> arrLogs = canonicalName
								.get("null");
						arrLogs.add(listaNaAllLogFiles.get(i));

						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/versionName/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());

						reportList.put(str_versionName, arrLogs);
					} else {
						ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
						arrLogs.add(listaNaAllLogFiles.get(i));
						versionName.put("null", arrLogs);
						reportList.put(str_versionName, arrLogs);
						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/versionName/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());
					}
				}
			}

			// versionName
			// versionNameversionNameversionNameversionNameversionNameversionName
		}
		System.out.println("Defferent versions:" + reportList.size());
		for (String key1 : reportList.keySet()) {
			System.out.println("	Version: " + key1 + " ("
					+ reportList.get(key1).size() + ")");
			for (int j = 0; j < reportList.get(key1).size(); j++) {
				System.out.println("		path: "
						+ reportList.get(key1).get(j).path);
			}
		}
		System.out.println();
	}

	public static void listandroidOsBuildMODEL(
			ArrayList<CheckIN_File> listaNaAllLogFiles) {

		Map<String, ArrayList<CheckIN_File>> reportList = new HashMap<String, ArrayList<CheckIN_File>>();

		for (int i = 0; i < listaNaAllLogFiles.size(); i++) {
			// androidOsBuildMODEL androidOsBuildMODEL androidOsBuildMODEL
			// androidOsBuildMODEL
			String str_androidOsBuildMODEL = listaNaAllLogFiles.get(i).androidOsBuildMODEL;
			createDir(outputDirectory.getAbsolutePath()
					+ "/androidOsBuildMODEL/");
			if (str_androidOsBuildMODEL.length() > 0) {
				createDir(outputDirectory.getAbsolutePath()
						+ "/androidOsBuildMODEL/"
						+ listaNaAllLogFiles.get(i).androidOsBuildMODEL);
				if (androidOsBuildMODEL.containsKey(str_androidOsBuildMODEL)) {
					ArrayList<CheckIN_File> arrLogs = androidOsBuildMODEL
							.get(str_androidOsBuildMODEL);
					arrLogs.add(listaNaAllLogFiles.get(i));
					reportList.put(str_androidOsBuildMODEL, arrLogs);

					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath()
									+ "/androidOsBuildMODEL/"
									+ listaNaAllLogFiles.get(i).androidOsBuildMODEL
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());

				} else {
					ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
					arrLogs.add(listaNaAllLogFiles.get(i));
					androidOsBuildMODEL.put(str_androidOsBuildMODEL, arrLogs);
					reportList.put(str_androidOsBuildMODEL, arrLogs);
					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath()
									+ "/androidOsBuildMODEL/"
									+ listaNaAllLogFiles.get(i).androidOsBuildMODEL
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());
				}

			} else {
				// bez canonikal
				createDir(outputDirectory.getAbsolutePath()
						+ "/androidOsBuildMODEL/null/");
				String t123 = listaNaAllLogFiles.get(i).stackTrace;
				if (t123.length() > 0) {
					if (androidOsBuildMODEL.containsKey("null")) {
						ArrayList<CheckIN_File> arrLogs = androidOsBuildMODEL
								.get("null");
						arrLogs.add(listaNaAllLogFiles.get(i));

						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/androidOsBuildMODEL/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());

						reportList.put(str_androidOsBuildMODEL, arrLogs);
					} else {
						ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
						arrLogs.add(listaNaAllLogFiles.get(i));
						androidOsBuildMODEL.put("null", arrLogs);
						reportList.put(str_androidOsBuildMODEL, arrLogs);
						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/androidOsBuildMODEL/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());
					}
				}
			}
			// androidOsBuildMODEL androidOsBuildMODEL androidOsBuildMODEL
			// androidOsBuildMODELA

		}
		// System.out.println(reportList.keySet().size());
		// System.out.println(mapTypeExceptions.values().size());
		System.out.println("Defferent os build version:" + reportList.size());
		for (String key1 : reportList.keySet()) {
			System.out.println("	Type: " + key1 + " ("
					+ reportList.get(key1).size() + ")");
			for (int j = 0; j < reportList.get(key1).size(); j++) {
				System.out.println("		path: "
						+ reportList.get(key1).get(j).path);
			}
		}
		System.out.println();

	}

	public static void listCanonical(ArrayList<CheckIN_File> listaNaAllLogFiles) {

		Map<String, ArrayList<CheckIN_File>> reportList = new HashMap<String, ArrayList<CheckIN_File>>();

		for (int i = 0; i < listaNaAllLogFiles.size(); i++) {
			// canonicalNamecanonicalNamecanonicalNamecanonicalNamecanonicalNamecanonicalName
			String str_canonicalName = listaNaAllLogFiles.get(i).canonicalName;
			createDir(outputDirectory.getAbsolutePath() + "/canonicalName/");
			if (str_canonicalName.length() > 0) {
				createDir(outputDirectory.getAbsolutePath() + "/canonicalName/"
						+ listaNaAllLogFiles.get(i).canonicalName);
				if (canonicalName.containsKey(str_canonicalName)) {
					ArrayList<CheckIN_File> arrLogs = canonicalName
							.get(str_canonicalName);
					arrLogs.add(listaNaAllLogFiles.get(i));

					reportList.put(str_canonicalName, arrLogs);

					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath()
									+ "/canonicalName/"
									+ listaNaAllLogFiles.get(i).canonicalName
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());

				} else {
					ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
					arrLogs.add(listaNaAllLogFiles.get(i));
					canonicalName.put(str_canonicalName, arrLogs);

					reportList.put(str_canonicalName, arrLogs);

					copyFile(
							listaNaAllLogFiles.get(i).path,
							outputDirectory.getAbsolutePath()
									+ "/canonicalName/"
									+ listaNaAllLogFiles.get(i).canonicalName
									+ "/"
									+ listaNaAllLogFiles.get(i).path.getName());
				}

			} else {
				// bez canonikal
				createDir(outputDirectory.getAbsolutePath()
						+ "/canonicalName/null/");
				String t123 = listaNaAllLogFiles.get(i).stackTrace;
				if (t123.length() > 0) {
					if (canonicalName.containsKey("null")) {
						ArrayList<CheckIN_File> arrLogs = canonicalName
								.get("null");
						arrLogs.add(listaNaAllLogFiles.get(i));

						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/canonicalName/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());

						reportList.put(str_canonicalName, arrLogs);
					} else {
						ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
						arrLogs.add(listaNaAllLogFiles.get(i));
						canonicalName.put("null", arrLogs);
						reportList.put(str_canonicalName, arrLogs);
						copyFile(
								listaNaAllLogFiles.get(i).path,
								outputDirectory.getAbsolutePath()
										+ "/canonicalName/null/"
										+ "/"
										+ listaNaAllLogFiles.get(i).path
												.getName());
					}
				}
			}
			// canonicalName
			// canonicalNamecanonicalNamecanonicalNamecanonicalNamecanonicalName
		}

		// System.out.println(reportList.keySet().size());
		// System.out.println(mapTypeExceptions.values().size());
		System.out.println("Number of all types of exceptions:"
				+ reportList.size());
		for (String key1 : reportList.keySet()) {
			System.out.println("	Type: " + key1 + " ("
					+ reportList.get(key1).size() + ")");
			for (int j = 0; j < reportList.get(key1).size(); j++) {
				System.out.println("		path: "
						+ reportList.get(key1).get(j).path);
			}
		}
		System.out.println();
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

	public static void nekaSedi(ArrayList<CheckIN_File> listaNaAllLogFiles) {
		int i = 0;

		Map<String, ArrayList<CheckIN_File>> mapTypeCanonical = new HashMap<String, ArrayList<CheckIN_File>>();
		String log = listaNaAllLogFiles.get(i).canonicalName;

		if (log.length() > 0) {
			// createDir(outputDirectory.getAbsolutePath() + "/"
			// + listaNaAllLogFiles.get(i).canonicalName);

			// boolean daliNajdeMatch=false;
			if (mapTypeCanonical.containsKey(log)) {
				ArrayList<CheckIN_File> arrLogs = mapTypeCanonical.get(log);
				arrLogs.add(listaNaAllLogFiles.get(i));
				// mapTypeExceptions.put(log,arrLogs );
			} else {
				ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
				arrLogs.add(listaNaAllLogFiles.get(i));
				mapTypeCanonical.put(log, arrLogs);
			}

		} else {
			// bez canonikal
			// createDir(outputDirectory.getAbsolutePath() + "/null");
			String t123 = listaNaAllLogFiles.get(i).stackTrace;
			if (t123.length() > 0) {
				if (mapTypeCanonical.containsKey("null")) {
					ArrayList<CheckIN_File> arrLogs = mapTypeCanonical
							.get("null");
					arrLogs.add(listaNaAllLogFiles.get(i));
					// mapTypeExceptions.put(log,arrLogs );
				} else {
					ArrayList<CheckIN_File> arrLogs = new ArrayList<CheckIN_File>();
					arrLogs.add(listaNaAllLogFiles.get(i));
					mapTypeCanonical.put("null", arrLogs);
				}
			}
		}

		// System.out.println("Number of all files = " +
		// listaNaAllLogFiles.size());
		// // System.out.println(mapTypeCanonical.keySet().size());
		// // System.out.println(mapTypeCanonical.values().size());
		// // System.out.println(mapTypeCanonical.size());
		// for (String key : mapTypeCanonical.keySet()) {
		// System.out.println("TYPE: \""+key+"\", number of files:"
		// + mapTypeCanonical.get(key).size());
		//
		// // search po stackTrace
		// ArrayList<LogFile> nizaOdPosebenTipNaGreska = mapTypeCanonical
		// .get(key);
		//
		// int brojNaFolder = 0;
		// for (int i = 0; i < nizaOdPosebenTipNaGreska.size(); i++) {
		//
		// String log111 = nizaOdPosebenTipNaGreska.get(i).stackTrace;
		//
		// if (log111.length() > 0) {
		//
		// System.out.println("   File path:"+nizaOdPosebenTipNaGreska.get(i).path);
		//
		// // if (mapTypeExceptions.containsKey(log111)) {
		// // ArrayList<LogFile> arrLogs = mapTypeExceptions
		// // .get(log111);
		// // arrLogs.add(mapTypeCanonical.get(key).get(i));
		// //
		// // // System.out.println("ISTI---------------------------"
		// // // + i + "  "
		// // // + mapTypeCanonical.get(key).get(i).path);
		// //
		// // if (mapTypeCanonical.get(key).get(i).canonicalName
		// // .length() > 0) {
		// //// createDir(outputDirectory.getAbsolutePath()
		// //// + "/"
		// //// + mapTypeCanonical.get(key).get(i).canonicalName
		// //// + "/"
		// //// + mapTypeCanonical.get(key).get(i).type);
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
		// //// createDir(outputDirectory.getAbsolutePath() + "/"
		// //// + "null" + "/"
		// //// + mapTypeCanonical.get(key).get(i).type);
		// // copyFile(
		// // mapTypeCanonical.get(key).get(i).path,
		// // outputDirectory.getAbsolutePath()
		// // + "/"
		// // + "null"
		// // + "/"
		// // + mapTypeCanonical.get(key).get(i).path
		// // .getName());
		// // }
		// //
		// // } else {
		// // brojNaFolder++;
		// // mapTypeCanonical.get(key).get(i).type = brojNaFolder;
		//
		// if (mapTypeCanonical.get(key).get(i).canonicalName
		// .length() > 0) {
		// // createDir(outputDirectory.getAbsolutePath()
		// // + "/"
		// // + mapTypeCanonical.get(key).get(i).canonicalName
		// // + "/"
		// // + mapTypeCanonical.get(key).get(i).type);
		//
		// // vvv
		// copyFile(
		// mapTypeCanonical.get(key).get(i).path,
		// outputDirectory.getAbsolutePath()
		// + "/"
		// + mapTypeCanonical.get(key).get(i).canonicalName
		// + "/"
		// + mapTypeCanonical.get(key).get(i).path
		// .getName());
		//
		// } else {
		// createDir(outputDirectory.getAbsolutePath() + "/"
		// + "null" + "/"
		// + mapTypeCanonical.get(key).get(i).firstLine);
		// copyFile(
		// mapTypeCanonical.get(key).get(i).path,
		// outputDirectory.getAbsolutePath()
		// + "/"
		// + "null"
		// + "/" +mapTypeCanonical.get(key).get(i).firstLine +"/"
		// + mapTypeCanonical.get(key).get(i).path
		// .getName());
		// }
		//
		// // System.out.println("NOVO---------------------------"
		// // + i + "  "
		// // + mapTypeCanonical.get(key).get(i).path);
		//
		// ArrayList<LogFile> arrLogs = new ArrayList<LogFile>();
		// arrLogs.add(mapTypeCanonical.get(key).get(i));
		// mapTypeExceptions.put(log111, arrLogs);
		// //}
		// }
		//
		// // if (nizaOdPosebenTipNaGreska.get(i).canonicalName.length() >
		// // 0) {
		// // System.out
		// // .println(nizaOdPosebenTipNaGreska.get(i).canonicalName);
		// // } else {
		// //
		// // }
		// }
		// // System.out.println(mapTypeExceptions.keySet().size());
		// // System.out.println(mapTypeExceptions.values().size());
		// // System.out.println("		Exception size:	" +
		// mapTypeExceptions.size());
		// // for (String key1 : mapTypeExceptions.keySet()) {
		// // System.out.println("		path:"
		// // + mapTypeExceptions.get(key1).get(0).path);
		// // }
		//
		// }
		//
		// // System.out.println(mapTypeExceptions.keySet().size());
		// // System.out.println(mapTypeExceptions.values().size());
		// // System.out.println(listaNaAllLogFiles.size());
		// // for (String key : mapTypeExceptions.keySet()) {
		// // System.out.println(mapTypeExceptions.get(key).size());
		// // }
		//
		//
		//
		//
		// //###################################################################
		//
		//
		//
		//
		//
		//
		//
		//
		//

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
