package com.icann.e2e;

import com.icann.Helper;
import com.icann._contentrecordtypes.RegistryAgreement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Suite {
	public static boolean bUsingSuite = false;
	
	//maybe name these better - use test names?
	public static String sALabel;
	public static String sTypeOfTld;
	public static String sAgreementType;
	public static String sAgreementDate;
	public static String sNodeId;
	public static RegistryAgreement raRecord = new RegistryAgreement();
	
	public static void setSuiteVars(String sSuiteName) {
		switch (sSuiteName) {
		case "pocsuite":
			sALabel = "suite_" + Helper.todayString() + "_dms_automation";
			sTypeOfTld = "gTLD ";
			sAgreementType = "Base";
			sAgreementDate = Helper.todayString("dd") + " " + Helper.todayString("Month") + " " + Helper.todayString("yyyy");
		default:
			Helper.logError("Oops; did not find suite name:  " + sSuiteName);
		}
	}
	
	//can i use a method to set these suite variables when passed in the var name and value?  hmmmmm
//	public static void setVariable(String sVariableName, String sValue) {
//		try {
//			Suite.class.getField(sVariableName).set(Suite, sValue);
//		} catch (Exception e) {
//			
//		}
//	}
	
	public static List<String> lsSuiteFields(){
		List<String> lsFields = new ArrayList<String>();
		
		Field[] fields = Suite.class.getDeclaredFields();
		Helper.logDebug("Suite class field list: ");         
		for (Field field : fields) {
			Helper.logDebug(field.getName());  
			lsFields.add(field.getName());
		}
		
		return lsFields;
	}
}
