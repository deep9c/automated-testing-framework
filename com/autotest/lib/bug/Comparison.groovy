package com.mtn.lib.bug

import com.mtn.lib.bug.Bug;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

 /*
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
*/

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.ElementNameQualifier;
import org.custommonkey.xmlunit.ElementNameAndAttributeQualifier;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.custommonkey.xmlunit.examples.MultiLevelElementNameAndTextQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.SAXException;


public class Comparison {
 
    def log;
	public Comparison(log) {
		this.log = log
	}
	public boolean compare(String expectedXML,String actualXML,String reqXML, List<Difference> differences,String bugSummary,String ServiceName,String Opco,String bugzillaUsername, String bugzillaPassword, String wiproEmail, String bugzillaURL) {
 // 	************************START COMPARING**************************************
        XMLUnit.setIgnoreComments(Boolean.TRUE);
        //XMLUnit.setIgnoreWhitespace(Boolean.TRUE);
        XMLUnit.setNormalizeWhitespace(Boolean.TRUE);
        XMLUnit.setIgnoreAttributeOrder(Boolean.TRUE);
        XMLUnit.setNormalize(Boolean.TRUE);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(Boolean.TRUE);
        
        
        try {
            Diff diff = new Diff(expectedXML, actualXML);
            diff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier());
            //log.info("Similar? " + diff.similar());
			//return("similar: "+diff.similar());
			if(diff.similar())
			{
				return (Boolean.TRUE);
			}
            //System.out.println("Identical? " + diff.identical());
            
            
            DetailedDiff detDiff = new DetailedDiff(diff);
            
            //detDiff.overrideMatchTracker(new MatchTrackerImpl());
            
            
            List differencesInXML = detDiff.getAllDifferences();
            for (Object object : differencesInXML) {
                Difference difference = (Difference)object;
               
                Boolean similar=difference.isRecoverable();
                //String desc_string=difference.toString();
				//String desc=difference.getDescription();
                if(!similar)
                {
                	differences.add(difference);
					// ************ CALL BUGZILLA ***************
					
					Bug b = new Bug(bugzillaURL,"/jsonrpc.cgi", log)
					String diffc =  difference.toString()
					//log.info(diffc)
					
//log.info("Before bug logging; uname: "+bugzillaUsername+"; pwd: "+bugzillaPassword);
String newId = b.create(['product':'Middleware','component':ServiceName,'description':diffc,'summary':bugSummary,'version':'unspecified','cf_location':Opco,'cf_bug_source':'Development'], bugzillaUsername, bugzillaPassword,wiproEmail );


log.info("Bug logged");

def attachExpectedXML = expectedXML.bytes.encodeBase64();
def attachActualXML = actualXML.bytes.encodeBase64();
def attachReqXML = reqXML.bytes.encodeBase64();

b.attach(['ids':newId,'data':attachExpectedXML.toString(),'file_name':'expectedXML.txt','summary':'Expected XML Payload','content_type':'text/plain'], bugzillaUsername, bugzillaPassword )
b.attach(['ids':newId,'data':attachActualXML.toString(),'file_name':'actualXML.txt','summary':'Actual XML Payload','content_type':'text/plain'], bugzillaUsername, bugzillaPassword )
b.attach(['ids':newId,'data':attachReqXML.toString(),'file_name':'reqXML.txt','summary':'Request XML Payload','content_type':'text/plain'], bugzillaUsername, bugzillaPassword )
log.info("XMLs attached");

					
                	//log.info("***********************");
                	//log.info(difference);
                }
            }
			
 
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Boolean.FALSE);
	}
}
