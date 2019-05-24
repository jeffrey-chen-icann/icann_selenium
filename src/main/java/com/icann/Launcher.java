package com.icann;
import java.util.Arrays;

import com.icann.e2e.Suite;

public class Launcher {
	public static String sProject = "unset";
	public static String sCommandLineArgs = "unset";
	
	public static void main(String[] args)  {
		//expects arguments:
		//    <project> 
		//    <environment> 
		//    <wheretorun>
		//    <suite>
		//    <configuration> 
		
		String sSuiteToRun = "unset";
		sCommandLineArgs = Arrays.toString(args);
				
		String sBorder = "################################################################################";
		Helper.logMessage("", 0);
		Helper.logMessage("", 0);
		Helper.logMessage("", 0);
		Helper.logMessage(sBorder, 0);
		Helper.logMessage(sBorder, 0);
		Helper.logMessage(sBorder, 0);
		Helper.logMessage("", 0);
		
		Helper.logMessage("Command line arguments sent:  " + sCommandLineArgs);
		
		if (args.length >= 4){
			sProject = args[0];
			
			Environment.sEnvironment = args[1];
			String sWhereToRunParameter = args[2];
			String sSuiteParameter = args[3];
						
			//where to run
			switch (sWhereToRunParameter){
			case "local":
				break;
			case "bs":
			case "browserstack":
				Environment.bUseBrowserStack = true;
				break;
				
			default:
			}
			
			//suite?
			if (sSuiteParameter.toLowerCase().contains("suite")) {
				Suite.bUsingSuite = true;
				Suite.setSuiteVars(sSuiteParameter);
			}
			switch (sProject) {
			case "dms":
				switch (sSuiteParameter){
				case "poc":
					sSuiteToRun = "DmsPoc";
					break;						
				default:
					Helper.logError("Unrecognized suite parameter for project " + sProject + ":  " + sSuiteParameter);
					System.exit(1);
				}
				break;
			case "cms":
				switch (sSuiteParameter){ 

				default:
					Helper.logError("Unrecognized suite parameter for project " + sProject + ":  " + sSuiteParameter);
					System.exit(1);
				}
				break;
			case "e2e":
				switch (sSuiteParameter){
				case "pocsuite":
					sSuiteToRun = "PocSuite";
					break;
				default:
					
				}
			default:
				
			}
		} else {
			//specify which arguments?
			Helper.logError("You must send arguments to execute this jar:  <environment> <saucelabs> <suitetorun>");
			Helper.logMessage("    <project> = dms|cms|?");
			Helper.logMessage("    <environment> = dev|staging|prod");
			Helper.logMessage("    <wheretorun>   = bs|local");
			Helper.logMessage("    <suitetorun>  = suite name to run within a project's \"tests\" folder");
			Helper.logMessage("    <additional>  = ");
			System.exit(1);
		}
		

		
		String sJUnitSuite = "com.icann." + sProject + ".tests." + sSuiteToRun;
		Helper.logMessage("Running suite/test:  " + sSuiteToRun + " (" + sJUnitSuite + ")");
		Helper.logMessage("");
		org.junit.runner.JUnitCore.main(sJUnitSuite);
    }
}
