package JiraRestApi_unirest;

import java.io.File;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class addAttachmentToJira {

	public static void main(String[] args) throws UnirestException {
		// This code sample uses the  'Unirest' library:
		 // http://unirest.io/java.html
		 HttpResponse<JsonNode> response = Unirest.post("https://resham1.atlassian.net/rest/api/2/issue/FP-26/attachments")
		         .basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
		         .header("Accept", "application/json")
		         .header("X-Atlassian-Token", "no-check")
		         .field("file", new File("newFile.txt"))
		         .asJson();

		         System.out.println(response.getBody());
	}

}
