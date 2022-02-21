package JiraRestApi_unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GetIssue {

	public static void main(String[] args) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest
				.get("https://resham1.atlassian.net/rest/api/3/issue/FP-3")
				  .basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
				  .header("Accept", "application/json")
				  .asJson();

		int responseCode = response.getStatus();
		System.out.println(responseCode);	
		System.out.println();
		System.out.println(response.getBody());

	}
}
