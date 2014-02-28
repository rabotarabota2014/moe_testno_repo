package com.tmp1;

import java.util.Comparator;

public class MyComparator implements Comparator<CheckIN_File> {
	public int compare(CheckIN_File chair1, CheckIN_File chair2) {
		return chair1.versionNumber.compareToIgnoreCase(chair2.versionNumber);
		
		//return chair1.versionTypeRelease.compareToIgnoreCase(chair2.versionTypeRelease);
	}
}
