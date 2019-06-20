package com.icann;
import java.util.Arrays;
import java.util.List;
import com.icann.e2e.Suite;

public class Launcher {
	public static String sProject = "unset";
	public static String sSuiteParameter = "unset";
	public static String sCommandLineArgs = "unset";
	
	private static List<String> lsPossibleProjectSuiteParams = Arrays.asList(
			"dms _removeallregistryagreements", 
			"dms createcontentmodal.contenttypes",
			"dms login.accessadminpageswithnonadmin", 
			"dms poc", 
			"dms smokeboardmeeting",
			"dms smokelogin", 
			"e2e pocsuite");
	
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
			sSuiteParameter = args[3];
						
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
			
			switch (sProject) {
			//PLEASE ADD YOUR NEW PROJECT/SUITE PARAMETER TO lsPossibleProjectSuiteParams ABOVE!
			case "cms":
				switch (sSuiteParameter){ 

				default:
					Helper.logError("Unrecognized suite parameter for project " + sProject + ":  " + sSuiteParameter);
				}
				break;
			case "dms":
				switch (sSuiteParameter){
				case "_removeallregistryagreements":
					sSuiteToRun = "_RemoveAllRegistryAgreements";
					break;
				case "smokeboardmeeting":
					sSuiteToRun = "_SmokeBoardMeeting";
					break;
				case "createcontentmodal.contenttypes":
					sSuiteToRun = "CreateContentModalTests";
					break;
				case "login.accessadminpageswithnonadmin":
					sSuiteToRun = "Login_AccessAdminPagesWithNonAdmin";
					break;											
				case "poc":
					sSuiteToRun = "DmsPoc";
					break;						
				case "smokelogin":
					sSuiteToRun = "_SmokeLogin";
					break;						
				default:
					Helper.logError("Unrecognized suite parameter for project " + sProject + ":  " + sSuiteParameter);
				}
				break;
			case "e2e":
				Suite.bUsingSuite = true;
				
				switch (sSuiteParameter){
				case "pocsuite":
					sSuiteToRun = "PocSuite";
					
					break;
				default:
					
				}
				
				Suite.setSuiteVars(sSuiteToRun);
			default:
				
			}
		} else {
			//specify which arguments?
			Helper.logError("You must send arguments to execute this jar:  <environment> <saucelabs> <suitetorun>");
			Helper.logMessage("    <project>     = cms|dms|e2e|_utility");
			Helper.logMessage("    <environment> = dev|staging|prod");
			Helper.logMessage("    <wheretorun>  = bs|local");
			Helper.logMessage("    <suitetorun>  = suite name to run within a project's \"tests\" folder");
			Helper.logMessage("    <additional>  = ");
			Helper.logMessage("");
			
			System.exit(1);
		}
		
		if (sSuiteToRun.equals("unset")) {
			Helper.logError ("Could not find mapped suite for project/suite parameters:  " + sProject + "/" + sSuiteParameter);
			
			Helper.logMessage("Possible project/suite parameters:");
			for (int i=0; i<=lsPossibleProjectSuiteParams.size(); i++) {
				Helper.logMessage("    " + lsPossibleProjectSuiteParams.get(i));
			}
			System.exit(1);

		} else {
			String sJUnitSuite = "com.icann." + sProject + ".tests." + sSuiteToRun;
			Helper.logMessage("Running suite/test:  " + sSuiteToRun + " (" + sJUnitSuite + ")");
			Helper.logMessage("");
			org.junit.runner.JUnitCore.main(sJUnitSuite);
		}
    }
}
