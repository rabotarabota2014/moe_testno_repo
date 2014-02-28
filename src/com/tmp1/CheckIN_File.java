package com.tmp1;

import java.io.File;

public class CheckIN_File {

	public String packageName = "";// com.navayo.secfone.base,com.navayo.secfone.sipua,com.navayo.secfone.sms.android

	public String voipVersionNumber = "";// 142948
	public String baseVersionNumber = "";// 132948
	public String smsVersionNumber = "";// 1024

	public String versionNumber = "";// opst za site, useful za comparatorot

	public String baseONLYVersionNumber = "";// 1.3.2087.2282

	public String versionTypeRelease = "";// release

	public String versionTypeOrange = "";// orange

	public boolean default_bool = false;

	public boolean isVoipEqual(CheckIN_File arg) {
		// System.out.println(arg.voipVersionNumber+"	"+voipVersionNumber+"	"+arg.equals(voipVersionNumber));
		if (!arg.voipVersionNumber.equals(voipVersionNumber)) {
			return false;
		} else {
			if (!arg.versionTypeOrange.equals(versionTypeOrange)) {
				return false;
			} else {
				if (!arg.versionTypeRelease.equals(versionTypeRelease)) {
					return false;
				}
			}
		}

		return true;
	}

	public String toString() {
		if (isValid()) {
			return versionTypeRelease + "		" + versionNumber + "		"
					+ versionTypeOrange;
		} else {
//			return "NOT COMPLETE " + versionTypeRelease + "		" + versionNumber
//					+ "		" + versionTypeOrange;
			//return "";//just ignore this one in a milion
			return versionTypeRelease + "		" + versionNumber
			+ "		" + "red";
		}
	}

	public boolean isValid() {
		if (versionNumber.length() > 0) {
			if (versionTypeOrange.length() > 0) {
				if (versionTypeRelease.length() > 0) {
					return true;
				}
			}
		}

		return false;
	}

	// public String voipToString() {
	// return voipVersionNumber + "	" + versionTypeRelease + "		"
	// + versionTypeOrange;
	// }
	//
	// public String baseToString() {
	// return baseVersionNumber + "	" +versionTypeRelease + "		"
	// + versionTypeOrange;
	// }
	//
	// public String smsToString() {
	// return smsVersionNumber + "	" + versionTypeRelease + "		"
	// + versionTypeOrange;
	// }
	//
	// public String backendToString() {
	// return baseONLYVersionNumber + "	" + versionTypeRelease + "		"
	// + versionTypeOrange;
	// }

	public boolean isbackendEqual(CheckIN_File arg) {
		if (!arg.baseONLYVersionNumber.equals(baseONLYVersionNumber)) {
			return false;
		} else {
			if (!arg.versionTypeOrange.equals(versionTypeOrange)) {
				return false;
			} else {
				if (!arg.versionTypeRelease.equals(versionTypeRelease)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isBaseEqual(CheckIN_File arg) {
		if (!arg.baseVersionNumber.equals(baseVersionNumber)) {
			return false;
		} else {
			if (!arg.versionTypeOrange.equals(versionTypeOrange)) {
				return false;
			} else {
				if (!arg.versionTypeRelease.equals(versionTypeRelease)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isSmsEqual(CheckIN_File arg) {
		if (!arg.smsVersionNumber.equals(smsVersionNumber)) {
			return false;
		} else {
			if (!arg.versionTypeOrange.equals(versionTypeOrange)) {
				return false;
			} else {
				if (!arg.versionTypeRelease.equals(versionTypeRelease)) {
					return false;
				}
			}
		}
		return true;
	}
}

// =========== OS details ===========
// android.os.Build.MODEL: HTC Desire
// android.os.Build.VERSION.RELEASE: 2.2
// android.os.Build.TIME: 1281365888000
// android.os.Build.PRODUCT: htc_bravo
// android.os.Build.BOOTLOADER: 0.93.0001
//
// =========== Phone details ===========
// android.os.Build.MANUFACTURER: HTC
// android.os.Build.DEVICE: bravo
// android.os.Build.MODEL: HTC Desire
// Network type: 3
// Mvcn status: 4
// Is voip aipc active (pid): -1
// Is base aipc active (pid): -1
//
// =========== App details ===========
// Class canonical name: java.lang.NoClassDefFoundError
// MvcnId: 0
// Version name: 1.4.2562
// Version code: 142562
// Base version code: 1.3.2087.2372
// Base version info: 1.3.2087.2372_2013-03-14_14:09:48
// Build config name: debug
// Application log level: 5
// Is sms app installed: true
// Update interval: 300000
// Use encrypted database: false
//
// =========== Stack trace ===========
// java.lang.NoClassDefFoundError: com.navayo.secfone.updater.R$string
// at
// com.navayo.secfone.updater.SecfoneUpdateReceiver.showSecfoneBaseUpdateNotification(SecfoneUpdateReceiver.java:123)
// at
// com.navayo.secfone.updater.SecfoneUpdateReceiver.access$1(SecfoneUpdateReceiver.java:119)
// at
// com.navayo.secfone.updater.SecfoneUpdateReceiver$CheckForUpdateTask.onPostExecute(SecfoneUpdateReceiver.java:113)
// at
// com.navayo.secfone.updater.SecfoneUpdateReceiver$CheckForUpdateTask.onPostExecute(SecfoneUpdateReceiver.java:1)
// at android.os.AsyncTask.finish(AsyncTask.java:417)
// at android.os.AsyncTask.access$300(AsyncTask.java:127)
// at android.os.AsyncTask$InternalHandler.handleMessage(AsyncTask.java:429)
// at android.os.Handler.dispatchMessage(Handler.java:99)
// at android.os.Looper.loop(Looper.java:144)
// at com.navayo.secfone.sipua.SecfoneApp$1.run(SecfoneApp.java:224)