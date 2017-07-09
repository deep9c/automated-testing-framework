package com.mtn.lib.bug

import groovyx.net.http.HTTPBuilder
import net.sf.json.JSONObject
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST
import static groovyx.net.http.Method.GET

public class Bug {
	def log
	def bugzillaServer
	def bugzillaURIPath
	HTTPBuilder http
	boolean isLoggedIn = false
	def LoginError
	def BugCreateError

	def mandatoryAttachParams = [
		'ids',
		'data',
		'file_name',
		'summary',
		'content_type'] as Set
	
	def defaultAttachParams = [comment:'None']

	def mandatoryCreateParams = [
		'product',
		'component',
		'description',
		'summary'] as Set
	def defaultCreateParams = [version:'1',
							   op_sys:'windows',
							   platform:'PC',
							   priority:'Normal',
							   severity:'Normal']
	
	public Bug(String server, String path, log) {
		this.log = log
		this.bugzillaServer  = new String(server)
		this.bugzillaURIPath = new String(path)
		http = new HTTPBuilder(bugzillaServer)
	}
	public boolean attach(HashMap overrides,String bugzillaUsername, String bugzillaPassword) {
		assert overrides.keySet().containsAll(mandatoryAttachParams)
		def allParams = new HashMap(defaultAttachParams)
		allParams.putAll(overrides)
		def params = new String("[{")
		for ( param in allParams) {
			params += "'" + param.key + "':'" + param.value + "',"
		}
		params = params.substring(0,params.length() - 1)
		params += "}]"
		if (isLoggedIn || login(bugzillaUsername,bugzillaPassword)) {
			JSONObject json = sendRequest (params, "Bug.add_attachment", "12347")
			assert json != null
			return true
		}
	}
	
	public String create(HashMap overrides,String bugzillaUsername, String bugzillaPassword, String wiproEmail) {
		log.info("Bug creation method; uname: "+bugzillaUsername+"; pwd: "+bugzillaPassword +" ... email: " + wiproEmail);
		assert overrides.keySet().containsAll(mandatoryCreateParams)
		def allParams = new HashMap(defaultCreateParams)
		allParams.putAll(overrides)
		def params = new String("[{")
		for ( param in allParams) {
			//log.info(param.key + ": " + param.value)
			params += "\"" + param.key + "\":\"" + param.value + "\","
		}
		params += "\"assigned_to\":\""+wiproEmail+"\","
		params = params.substring(0,params.length() - 1)
		params += "}]"
		//log.info("create Method__isLoggedIn: "+isLoggedIn);
		try{
		if (isLoggedIn || login(bugzillaUsername,bugzillaPassword)) {
			JSONObject json = sendRequest (params, "Bug.create", "12346");
			def BugCreateError = json.getString("error");
			if (!BugCreateError.equals("null")) {
				this.BugCreateError = BugCreateError;
				throw new Exception(BugCreateError);
				//return "BugCreationFailed";
			} 
			assert json != null
			return json.result.id
		}
		else{
			throw new Exception(LoginError);
		}
		}
		catch(Exception e){
			//log.info("Login Failed! Please check the Bugzilla Path, Username, Password in the Properties file.");
			//log.info("LoginError: "+LoginError);
			if (!BugCreateError.equals("null")) {
				throw new Exception(BugCreateError);
			}
			else if (LoginError.equals("null")) {
				throw new Exception("Bug Creation in Bugzilla Failed! Please contact the Administrator.");
			}
			else{
				throw new Exception(LoginError);
			}
		}
	}
	public boolean login(String bugzillaUsername, String bugzillaPassword) {
		def params = new String("[{'login':'"+bugzillaUsername+"','password':'"+bugzillaPassword+"','remember':'True'}]")
		//log.info("params:: "+params);
		//log.info("before login");
		JSONObject json = sendRequest (params, "User.login", "12345");
		//log.info("after login");
		//log.info("LOGIN Returned: "+json.toString());
		def LoginError = json.getString("error");
		//log.info("LoginError from LOGIN method: "+LoginError);
		if (LoginError.equals("null")) {
			isLoggedIn = true
		} else {
			this.LoginError = LoginError;
			isLoggedIn = false
		}
		//log.info("Login Method__isLoggedIn: "+isLoggedIn);
		return isLoggedIn
	}
	private JSONObject sendRequest (String params, String method, String id) {
		//log.info("Bugzilla call: method= " + method + ".... params= " + params);
		http.request (POST, JSON) { req->
			uri.path = bugzillaURIPath
			body = [
				"params" : params,
				"jsonrpc" : "1.0",
				"method" : method,
				"id" : id
			]
			headers.'Content-Type' = 'application/json'
			response.success = { resp, json ->
				//log.info resp.statusLine
				//log.info json.result.id
				return json
			}
			response.failure = { resp ->
				log.info "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
				return null
			}
		}
	}
}
