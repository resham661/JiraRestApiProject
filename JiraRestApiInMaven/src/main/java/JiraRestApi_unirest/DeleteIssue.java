package JiraRestApi_unirest;

//import static org.junit.Assert.assertEquals;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class DeleteIssue {

	public static void main(String[] args) throws UnirestException {

		HttpResponse<String> response = Unirest.delete("https://resham1.atlassian.net/rest/api/3/issue/FP-17")
				  .basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
				  .asString();

		//System.out.println(response.getBody());
		int responseCode = response.getStatus();
		
		if(responseCode == 204) {
			System.out.println("Deleted the ticket successfully...");
			System.out.println(responseCode);
			//System.out.println(response.getBody());
		}
		else {
			System.out.println("Please check the Issue id or key");
		}	
	}
}
