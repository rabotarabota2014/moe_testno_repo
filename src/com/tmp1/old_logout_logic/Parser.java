package com.tmp1.old_logout_logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	public static String getStackTrace(String text) {
		String s = "";
		if (text != null) {
			int pocetokNaTrace = text
					.indexOf("=========== Stack trace ===========");
			if (pocetokNaTrace > -1) {
				s = text.substring(pocetokNaTrace);
			} else {
				return text;
			}
		}
		return s;
	}

	public static String getCanonicalType(String text) {
		String s = "";
		if (text != null) {
			int pocetokNaTrace = text.indexOf("Class canonical name:");
			if (pocetokNaTrace > -1) {
				String ss = text.substring(pocetokNaTrace);
				int endIndex = ss.indexOf("\n");
				if (endIndex > -1) {
					// System.out.println(ss.substring("Class canonical name:".length(),
					// endIndex).trim());
					return ss.substring("Class canonical name:".length(),
							endIndex).trim();
				} else {
					return "";
				}

			} else {
				return "";// nema takvo nesto tuka
			}
		}
		return s;
	}

	public static String getFirstLine(String text) {
		String s = "";
		if (text != null) {
			int endIndex = text.indexOf("\n");
			if (endIndex > -1) {
				if (text.length() > 70) {
					return text.substring(0, 70).trim();
				} else {
					return text.substring(0, endIndex).trim();
				}
			} else {
				return "";
			}

		}
		return s;
	}

	public static String getandroidOsBuildMODEL(String text) {
		String s = "";
		if (text != null) {
			int pocetokNaTrace = text.indexOf("android.os.Build.MODEL:");
			if (pocetokNaTrace > -1) {
				String ss = text.substring(pocetokNaTrace);
				int endIndex = ss.indexOf("\n");
				if (endIndex > -1) {
					// System.out.println(ss.substring("Class canonical name:".length(),
					// endIndex).trim());
					return ss.substring("android.os.Build.MODEL:".length(),
							endIndex).trim();
				} else {
					return "";
				}

			} else {
				return "";// nema takvo nesto tuka
			}
		}
		return s;
	}

	public static String getVersionName(String text) {
		return getTextAfterTheText(text, "Version name:");
	}

	public static String getTextAfterTheText(String textToParse, String token) {
		String s = "";
		if (textToParse != null) {
			int pocetokNaTrace = textToParse.indexOf(token);
			if (pocetokNaTrace > -1) {
				String ss = textToParse.substring(pocetokNaTrace);
				int endIndex = ss.indexOf("\n");
				if (endIndex > -1) {
					// System.out.println(ss.substring("Class canonical name:".length(),
					// endIndex).trim());
					return ss.substring(token.length(), endIndex).trim();
				} else {
					return "";
				}

			} else {
				return "";// nema takvo nesto tuka
			}
		}
		return s;
	}

	public static String getMvcnID(String filePathName) {
		// com.navayo.secfone.sipua-trace-27766090-1.4.2543-1366894760-5058
		String s = "";
		if (filePathName != null) {
			int pocetokNaTrace = filePathName
					.indexOf("com.navayo.secfone.sipua-trace-");
			if (pocetokNaTrace > -1) {
				String ss = filePathName
						.substring("com.navayo.secfone.sipua-trace-".length());

				if (ss.length() > 0) {
					int endIndexFinal = ss.indexOf("-");

					String mvcnID = ss.substring(0, endIndexFinal).trim();
					if (mvcnID.contains(".")) {
						return "";
					} else {
						if (mvcnID.length() > 3) {
							return mvcnID;
						} else {
							return "";
						}
					}
				}
				return "";
			} else {
				return "";
			}

		}
		return s;
	}

	public static CheckIN_File parseCheckIN(String string) {
		CheckIN_File f = new CheckIN_File();
		String[] lines = string.split("\n");

		for (int i = 0; i < lines.length; i++) {

			String l = lines[i];
			//System.out.println("line:" + lines[i]);

			if (l.contains("/sda1")) {
				continue;
			}

			if (l.contains("[default]")) {
				f.default_bool = true;
				continue;
			}

			if (l.contains("[com.navayo")) {
				String tmp1 = l.trim().split(" ")[l.trim().split(" ").length - 1];
				if (f.packageName.length() > 0) {
					f.packageName = tmp1.substring(1, tmp1.length() - 1)
							+ " ; " + f.packageName;
				} else {
					f.packageName = tmp1.substring(1, tmp1.length() - 1);
				}
				// System.out.println(f.packageName);
				continue;
			}

			if (l.contains("[orange]")) {
				if (f.versionTypeOrange.length() > 0) {
					if (!f.versionTypeOrange.equals("orange")) {
						System.out.println("KONFLIKT !!!!!!!!!!!!!!!!!");
					}
				}
				f.versionTypeOrange = "orange";
				// System.out.println(f.versionTypeOrange);
				continue;
			}

			if (l.contains("[red]")) {
				if (f.versionTypeOrange.length() > 0) {
					if (!f.versionTypeOrange.equals("red")) {
						System.out.println("KONFLIKT1 !!!!!!!!!!!!!!!!!");
					}
				}
				f.versionTypeOrange = "red";
				// System.out.println(f.versionTypeOrange);
				continue;
			}

			if (l.contains("[release]")) {
				if (f.versionTypeRelease.length() > 0) {
					if (!f.versionTypeRelease.equals("release")) {
						System.out.println("KONFLIKT2 !!!!!!!!!!!!!!!!!");
					}
				}
				f.versionTypeRelease = "release";
				// System.out.println(f.versionTypeRelease);
				continue;
			}

			if (l.contains("[debug]")) {
				if (f.versionTypeRelease.length() > 0) {
					if (!f.versionTypeRelease.equals("debug")) {
						System.out.println("KONFLIKT3 !!!!!!!!!!!!!!!!!");
					}
				}
				f.versionTypeRelease = "debug";
				// System.out.println(f.versionTypeRelease);
				continue;
			}

			Pattern pattern = Pattern.compile("\\d{6}");
			if (l.matches(".*\\d{6}.*")) {

				Matcher matcher = pattern.matcher(l);
				matcher.find();
				String version = matcher.group();
				if (version.startsWith("14")) {
					f.voipVersionNumber = version;
				}
				if (version.startsWith("13")) {
					f.baseVersionNumber = version;
				}
				// System.out.println(matcher.group());
				// System.out.println(matcher.replaceAll("\t"));
				continue;
			}

			Pattern pattern1 = Pattern.compile("\\d\\.\\d\\.\\d{4}\\.\\d{4}");
			if (l.matches(".*\\d\\.\\d\\.\\d{4}\\.\\d{4}.*")) {

				Matcher matcher1 = pattern1.matcher(l);
				matcher1.find();
				String version = matcher1.group();
				//System.out.println(matcher1.group());
				if (version.startsWith("1.3")) {
					f.baseONLYVersionNumber = version;
				}
				// System.out.println(matcher.group());
				// System.out.println(matcher.replaceAll("\t"));
				continue;
			}

			Pattern pattern2 = Pattern.compile("\\d{4}");
			if (l.matches(".*\\d{4}.*")) {

				Matcher matcher2 = pattern2.matcher(l);
				matcher2.find();
				String version = matcher2.group();
				//System.out.println(matcher2.group());
				if (version.startsWith("10")) {
					f.smsVersionNumber = version;
				}
				// System.out.println(matcher.group());
				// System.out.println(matcher.replaceAll("\t"));
				continue;
			}

		}
		return f;
	}
}
