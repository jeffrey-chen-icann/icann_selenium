package com.icann._contentrecordtypes;

import java.util.Arrays;
import java.util.List;

import com.icann.dms.Metadata;

public class AboutTheBoard extends _ContentRecord{
	public String sPageTitle = "About the Board";
	public Metadata metadata; 
	
	//constructors
	public AboutTheBoard() {
	}

	public String toString() {
		return this.toList().toString();
	}
	
	public List<String> toList() {
		return Arrays.asList(this.sPageTitle);
	}
}
