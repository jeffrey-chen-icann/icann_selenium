package com.icann._contentrecordtypes;

import java.util.Arrays;
import java.util.List;

public class BoardMeeting extends _ContentRecord {
	public String sPageTitle = "";
	public String sPageDate = "";
	public String sBoardMeetingType = "";

	
	//constructors
	public BoardMeeting(String sPageTitle, String sPageDate, String sBoardMeetingType) {
		this.sPageTitle = sPageTitle;
		this.sPageDate = sPageDate;
		this.sBoardMeetingType = sBoardMeetingType;
	}

	public String toString() {
		return this.toList().toString();
	}
	
	public List<String> toList() {
		return Arrays.asList(""); //this.sPageTitle, this.sPageDate, this.sBoardMeetingType
	}

}
