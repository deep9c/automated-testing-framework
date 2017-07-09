package com.mtn.lib.bug

import java.util.Properties;
import java.io.InputStream;
import java.io.OutputStream;

public class General{

	def static log;

	public static String Mode;
	public static String EndPoint;
	public static String RootProjectFolder;
	public static String ReqABMFolder;
	public static String ExpectedProvABMFolder;
	public static String ProvRespABMFolder;
	public static String ExpectedRespABMFolder;
	public static String BugzillaUsername;
	public static String BugzillaPassword;
	public static String WiproEmail;
	public static String BugzillaURL;
	
	public static General generalObject;
	
	public static String xmlfileName;
	public static String FlagSentFromGroovy;
	
	private static Properties prop = new Properties();
	//private static Properties prop_out = new Properties();
	private static InputStream input = null;
	//private static OutputStream output = null;
	//private staticBoolean Erroroccurred=Boolean.FALSE;
	
	
	public General(log){
		this.log = log;
		generalObject = this;
	}
	
	public static boolean setProperties(log1){
		try{
			if(log1 != null){
				log = log1;
			}
			loadPropFile();
			setMode();
			setEndPoint();
			setRootProjectFolder();
			setReqABMFolder();
			setExpectedProvABMFolder();
			setProvRespABMFolder();
			setExpectedRespABMFolder();
			setBugzillaUsername();
			setBugzillaPassword();
			setWiproEmail();
			setBugzillaURL();
			
			validateProperties();
			
			return true;
		}
		catch(Exception e){
			throw new Exception(e.message);
		}
		finally{
		if (input != null) {
			try {
				input.close();
			} catch (Exception e) {
				log.error(e.message);
			}
		}
		}
	}
	
	public static boolean checkFlagAndDefaultResponse(){
		if(FlagSentFromGroovy.equals("False")){
			loadPropFile();
			setProvRespABMFolder();
			String defaultResponsePayload = new File(RootProjectFolder+ProvRespABMFolder+"DefaultResponse.xml").text;
			return false;
		}
		return true;
			
	}
	
	private static void loadPropFile(){
		input = new FileInputStream("MTN_AutoTesting.properties");		//can throw FileNotFoundException
		prop.load(input);
	}
	
	private static String checkNullAndNormalise(String PropValue){
		if(PropValue == null){
			return "PropValue is null";
		}
		else{
			PropValue=PropValue.replaceAll("\\s+", " ").trim();		// Normalise the string to remove additional whitespaces
			//PropValue=PropValue.replace("\\+", "\\\\");
		}
		return PropValue;
	}
	
	private static void setMode(){
		String LocalMode = null;
		LocalMode = prop.getProperty("Mode");
		LocalMode =  checkNullAndNormalise(LocalMode);
		if(LocalMode.equals("PropValue is null")){
			//log.error("Please assign the 'Mode' in the properties file correctly.");
			throw new Exception("Please assign the 'Mode' in the properties file correctly.");
		}
		if(!(LocalMode.equals("Normal")) && !(LocalMode.equals("CheckOnlyResponse")) && !(LocalMode.equals("SendDefaultResponse"))) {
			//log.error("Invalid value of 'Mode'. Valid values are: Normal, CheckOnlyResponse, SendDefaultResponse");
			throw new Exception("Invalid value of 'Mode'. Valid values are: Normal, CheckOnlyResponse, SendDefaultResponse");
		}
		Mode = LocalMode;
	}
	
	private static void setEndPoint(){
		String LocalEndPoint = null;
		LocalEndPoint = prop.getProperty("EndPoint");
		LocalEndPoint =  checkNullAndNormalise(LocalEndPoint);
		if(LocalEndPoint.equals("PropValue is null")){
			//log.error("Please assign the 'EndPoint' in the properties file correctly.");
			throw new Exception("Please assign the 'EndPoint' in the properties file correctly.");
		}
		EndPoint = LocalEndPoint;
	}
	
	private static void setRootProjectFolder(){
		String LocalRootProjectFolder = null;
		LocalRootProjectFolder = prop.getProperty("RootProjectFolder");
		LocalRootProjectFolder =  checkNullAndNormalise(LocalRootProjectFolder);
		if(LocalRootProjectFolder.equals("PropValue is null")){
			//log.error("Please assign the 'RootProjectFolder' in the properties file correctly.");
			throw new Exception("Please assign the 'RootProjectFolder' in the properties file correctly.");
		}
		RootProjectFolder = LocalRootProjectFolder;
	}
	
	private static void setReqABMFolder(){
		String LocalReqABMFolder = null;
		LocalReqABMFolder = prop.getProperty("ReqABMFolder");
		LocalReqABMFolder =  checkNullAndNormalise(LocalReqABMFolder);
		if(LocalReqABMFolder.equals("PropValue is null")){
			//log.error("Please assign the 'ReqABMFolder' in the properties file correctly.");
			throw new Exception("Please assign the 'ReqABMFolder' in the properties file correctly.");
		}
		ReqABMFolder = LocalReqABMFolder;
	}
	
	private static void setExpectedProvABMFolder(){
		String LocalExpectedProvABMFolder = null;
		LocalExpectedProvABMFolder = prop.getProperty("ExpectedProvABMFolder");
		LocalExpectedProvABMFolder =  checkNullAndNormalise(LocalExpectedProvABMFolder);
		if(LocalExpectedProvABMFolder.equals("PropValue is null")){
			//log.error("Please assign the 'ExpectedProvABMFolder' in the properties file correctly.");
			throw new Exception("Please assign the 'ExpectedProvABMFolder' in the properties file correctly.");
		}
		ExpectedProvABMFolder = LocalExpectedProvABMFolder;
	}
	
	private static void setProvRespABMFolder(){
		String LocalProvRespABMFolder = null;
		LocalProvRespABMFolder = prop.getProperty("ProvRespABMFolder");
		LocalProvRespABMFolder =  checkNullAndNormalise(LocalProvRespABMFolder);
		if(LocalProvRespABMFolder.equals("PropValue is null")){
			//log.error("Please assign the 'ProvRespABMFolder' in the properties file correctly.");
			throw new Exception("Please assign the 'ProvRespABMFolder' in the properties file correctly.");
		}
		ProvRespABMFolder = LocalProvRespABMFolder;
	}
	
	private static void setExpectedRespABMFolder(){
		String LocalExpectedRespABMFolder = null;
		LocalExpectedRespABMFolder = prop.getProperty("ExpectedRespABMFolder");
		LocalExpectedRespABMFolder =  checkNullAndNormalise(LocalExpectedRespABMFolder);
		if(LocalExpectedRespABMFolder.equals("PropValue is null")){
			//log.error("Please assign the 'ExpectedRespABMFolder' in the properties file correctly.");
			throw new Exception("Please assign the 'ExpectedRespABMFolder' in the properties file correctly.");
		}
		ExpectedRespABMFolder = LocalExpectedRespABMFolder;
	}
	
	private static void setBugzillaUsername(){
		String LocalBugzillaUsername = null;
		LocalBugzillaUsername = prop.getProperty("BugzillaUsername");
		LocalBugzillaUsername =  checkNullAndNormalise(LocalBugzillaUsername);
		if(LocalBugzillaUsername.equals("PropValue is null")){
			//log.error("Please assign the 'BugzillaUsername' in the properties file correctly.");
			throw new Exception("Please assign the 'BugzillaUsername' in the properties file correctly.");
		}
		BugzillaUsername = LocalBugzillaUsername;
	}
	
	private static void setWiproEmail(){
		String LocalWiproEmail = null;
		LocalWiproEmail = prop.getProperty("WiproEmail");
		LocalWiproEmail =  checkNullAndNormalise(LocalWiproEmail);
		if(LocalWiproEmail.equals("PropValue is null")){
			//log.error("Please assign the 'WiproEmail' in the properties file correctly.");
			throw new Exception("Please assign the 'WiproEmail' in the properties file correctly.");
		}
		WiproEmail = LocalWiproEmail;
	}
	
	private static void setBugzillaPassword(){
		String LocalBugzillaPassword = null;
		LocalBugzillaPassword = prop.getProperty("BugzillaPassword");
		LocalBugzillaPassword =  checkNullAndNormalise(LocalBugzillaPassword);
		if(LocalBugzillaPassword.equals("PropValue is null")){
			//log.error("Please assign the 'BugzillaPassword' in the properties file correctly.");
			throw new Exception("Please assign the 'BugzillaPassword' in the properties file correctly.");
		}
		BugzillaPassword = LocalBugzillaPassword;
	}
	
	private static void setBugzillaURL(){
		String LocalBugzillaURL = null;
		LocalBugzillaURL = prop.getProperty("BugzillaURL");
		LocalBugzillaURL =  checkNullAndNormalise(LocalBugzillaURL);
		if(LocalBugzillaURL.equals("PropValue is null")){
			//log.error("Please assign the 'BugzillaURL' in the properties file correctly.");
			throw new Exception("Please assign the 'BugzillaURL' in the properties file correctly.");
		}
		BugzillaURL = LocalBugzillaURL;
	}
	
	private static void validateProperties(){
		// -------- Check whether ReqABMFolder & ExpectedProvABMFolder contain same file names --------
		try{
			File reqABM_FolderPointer = new File(RootProjectFolder+ReqABMFolder);
			File[] filesList = reqABM_FolderPointer.listFiles();
			if(filesList != null) {
				for (File xmlfileInReqABMFolder : filesList) {
					String xmlfileName=xmlfileInReqABMFolder.getName();
					String checkPresenceOfReqABM = new File(RootProjectFolder+ReqABMFolder+xmlfileName).text;
					String expectedResponse = new File(RootProjectFolder+ExpectedRespABMFolder+xmlfileName).text;
					if(Mode.equals("Normal") || Mode.equals("SendDefaultResponse")) {
						String expectedProvABM = new File(RootProjectFolder+ExpectedProvABMFolder+xmlfileName).text;
						String provRespABM = new File(RootProjectFolder+ProvRespABMFolder+xmlfileName).text;
					}
				}
				if(Mode.equals("SendDefaultResponse")) {
						String defaultResponsePayload = new File(RootProjectFolder+ProvRespABMFolder+"DefaultResponse.xml").text;
				}
			}
			else{
				throw new Exception("Path of root folder in Properties file does not exist--> "+RootProjectFolder+ReqABMFolder);
			}
		}
		catch(FileNotFoundException e){
			throw new Exception("File not found: " + e.message);
		}
		
		//---- Check Bugzilla Login ---
		try{
		Bug b1 = new Bug(BugzillaURL,"/jsonrpc.cgi", log);
		boolean bugzillaLoggedIn = b1.login(BugzillaUsername, BugzillaPassword);
		if(!bugzillaLoggedIn){
			throw new Exception("Bugzilla Login Exception--> " + b1.LoginError);
			//return; 
		}
		}
		catch(Exception e){
			throw new Exception("Bugzilla Exception--> " + e.message);
		}
	}
	
	public static void setMockProperties(String xmlfileName1, String FlagSentFromGroovy1){
		if(!xmlfileName1.equals("")){
			xmlfileName = xmlfileName1;
		}
		FlagSentFromGroovy = FlagSentFromGroovy1;
	}
}