package com.icann.e2e;

import com.icann.Helper;
import com.icann._contentrecordtypes.RegistryAgreement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Suite {
	public static boolean bUsingSuite = false;
	
	/* 
	 * Special things to implement a testcase that can be used in a suite with different/shared data:
	 *   e2e/tests/YourE2ESuiteName.java 
	 *     > create a new case in the suite parameter
	 *   Launcher.java
	 *     > create a new case in the suite parameter
	 *   e2e/Suite.java
	 *     > create public static member variables in the Suite object which will be populated using the setSuiteVars method
	 *   <project>/tests/YourTestCase.java
	 *     > create a list of the suite variables you want to pull into a testcase - should have the same name as the suite variable:  List<String> lsSuiteVars = Arrays.asList("sALabel", "sTypeOfTld", "sAgreementType", "sAgreementDate");
	 *      
	 * <more?>
	 */
	
	
	//maybe name these better - use test names in the variable names?  or do we care?
	public static String sALabel;
	public static String sTypeOfTld;
	public static String sAgreementType;
	public static String sAgreementDate;
	public static String sNodeId;
	public static RegistryAgreement raRecord = new RegistryAgreement();
	
	public static void setSuiteVars(String sSuiteName) {
		switch (sSuiteName) {
		case "pocsuite":
			sALabel = "suite_" + Helper.todayString() + "_e2e_automation";
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
