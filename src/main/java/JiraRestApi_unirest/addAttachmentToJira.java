package JiraRestApi_unirest;

import java.io.File;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class addAttachmentToJira {

	public static void main(String[] args) throws UnirestException {
		
		 HttpResponse<JsonNode> response = Unirest.post("https://resham1.atlassian.net/rest/api/2/issue/FP-27/attachments")
		         .basicAuth("reshamguru123@gmail.com", "eupW4RF1ajQoM8EybdbF5139")
		         .header("Accept", "application/json")
		         .header("X-Atlassian-Token", "no-check")
		         .field("file", new File("newFile.txt"))
		         .asJson();
		 
		 int responseCode = response.getStatus();
		 if(responseCode == 200) {
			 System.out.println("Attached the document in jira successfully...");
			 System.out.println(responseCode);
			 System.out.println(response.getBody());
		 }
		 else {
			 System.out.println(responseCode);
			 System.out.println(response.getBody());
		 }
	}
}