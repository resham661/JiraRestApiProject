package JiraRestApi_unirest;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class JiraApp {

	public static void main(String[] args) throws UnirestException, IOException, GeneralSecurityException {

		JiraApp jiraApp = new JiraApp();
		jiraApp.createIssue();
		jiraApp.getTicket();
		jiraApp.updateIssue();
		jiraApp.deleteIssue();	
		jiraApp.addAttachmentToJira();
		
		System.out.println("Attaching the file from the google drive to Jira: ");
		DownloadFileFromGdrive.main(args);
	}
		
	public void createIssue() throws UnirestException {
		// The dataTable definition using the Jackson library		
		ObjectMapper mapper = new ObjectMapper();
		com.fasterxml.jackson.databind.node.ObjectNode dataTable = mapper.createObjectNode();
		{
			com.fasterxml.jackson.databind.node.ObjectNode fields = dataTable.putObject("fields");
			{
				com.fasterxml.jackson.databind.node.ObjectNode project = fields.putObject("project");
				{
					project.put("key", "FP");
				}
				fields.put("summary", "Main order flow broken1");		    
				com.fasterxml.jackson.databind.node.ObjectNode description = fields.putObject("description");
				{
					description.put("type", "doc");
					description.put("version", 1);
					com.fasterxml.jackson.databind.node.ArrayNode content = description.putArray("content");
					com.fasterxml.jackson.databind.node.ObjectNode content0 = content.addObject();
					{
						content0.put("type", "paragraph");
						com.fasterxml.jackson.databind.node.ArrayNode content1 = content0.putArray("content");
						com.fasterxml.jackson.databind.node.ObjectNode content01 = content1.addObject();
						{
							content01.put("text", "Order entry fails when selecting supplier.");
							content01.put("type", "text");
						}
					}
				}
				com.fasterxml.jackson.databind.node.ObjectNode issuetype = fields.putObject("issuetype");
				{
					issuetype.put("name","Bug");
				} 		    
				com.fasterxml.jackson.databind.node.ObjectNode reporter = fields.putObject("reporter");
				{
					reporter.put("id", "61f77fddf51e850070836bc9");
				}
				com.fasterxml.jackson.databind.node.ObjectNode assignee = fields.putObject("assignee");
				{
					assignee.put("id", "61f77fddf51e850070836bc9");
				}
			}
		}
					    
		// Connect Jackson ObjectMapper to Unirest
		//setObjectMapper() should only be called once, for setting the mapper; 
		//once the mapper instance is set, it will be used for all request and responses.
		Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
			private ObjectMapper jacksonObjectMapper = new ObjectMapper();
			
			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
						
			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});				

		HttpResponse<JsonNode> response = Unirest.post("https://resham1.atlassian.net/rest/api/3/issue/")
						.basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
						.header("Accept", "application/json")
						.header("Content-Type", "application/json")
						.body(dataTable)
						.asJson();

		int responseCode = response.getStatus();	
		if(responseCode == 201) {
			System.out.println("Created the ticket successfully...");
			System.out.println(responseCode);
			System.out.println(response.getBody());
		}
		else {
			System.out.println(responseCode);
		}
		System.out.println();
	}	
	
	public void getTicket() throws UnirestException {
		HttpResponse<JsonNode> response = Unirest.get("https://resham1.atlassian.net/rest/api/3/issue/FP-24")
				  .basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
				  .header("Accept", "application/json")
				  .asJson();

		int responseCode = response.getStatus();
		if(responseCode == 200) {
			System.out.println(responseCode);
			System.out.println(response.getBody());
		}
		else {
			System.out.println("Please check the Issue id or key");
		}
		System.out.println();
	}
	
	public void updateIssue() throws UnirestException {
		// The dataTable definition using the Jackson library
		com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
		com.fasterxml.jackson.databind.node.ObjectNode dataTable = mapper.createObjectNode();
		{
			com.fasterxml.jackson.databind.node.ObjectNode update = dataTable.putObject("update");
			{   	    
				com.fasterxml.jackson.databind.node.ArrayNode labels = update.putArray("labels");
				com.fasterxml.jackson.databind.node.ObjectNode labels0 = labels.addObject();
				{
					labels0.put("add", "triaged");
				}
				com.fasterxml.jackson.databind.node.ObjectNode labels1 = labels.addObject();
				{
					labels1.put("remove", "newlabel");
				}
				ArrayNode summary = update.putArray("summary");
				ObjectNode summary0 = summary.addObject();
				{
					summary0.put("set", "Order entry fails when selecting supplier section.");
				}
			}
			com.fasterxml.jackson.databind.node.ObjectNode fields = dataTable.putObject("fields");
			{
				ObjectNode description = fields.putObject("description");
				{
					description.put("type", "doc");
					description.put("version", 1);
					ArrayNode content = description.putArray("content");
					ObjectNode content0 = content.addObject();
					{
						content0.put("type", "paragraph");
						ArrayNode content1 = content0.putArray("content");
						ObjectNode content01 = content1.addObject();
						{
							content01.put("text", "Order entry fails when selecting supplier.");
							content01.put("type", "text");
						}
					}
				}	    
				ObjectNode assignee = fields.putObject("assignee");
				{
					assignee.put("id", "61f77fddf51e850070836bc9");
				}
			}
		}
		// Connect Jackson ObjectMapper to Unirest
		Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
			private ObjectMapper jacksonObjectMapper = new ObjectMapper();
			
			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
						
			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		// This code sample uses the 'Unirest' library:
		HttpResponse<JsonNode> response = Unirest.put("https://resham1.atlassian.net/rest/api/3/issue/FP-30")
					.basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
					.header("Accept", "application/json")
					.header("Content-Type", "application/json")
					.body(dataTable)
					.asJson();

		int responseCode = response.getStatus();	
		if(responseCode == 204) {
			System.out.println("Updated the ticket successfully...");
			System.out.println(responseCode);
		}
		else {
			System.out.println("Please check the Issue id or key");
		}	
		System.out.println();
	}
	
	public void deleteIssue() throws UnirestException {
		HttpResponse<String> response = Unirest.delete("https://resham1.atlassian.net/rest/api/3/issue/FP-24")
				  .basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
				  .asString();

		int responseCode = response.getStatus();
		if(responseCode == 204) {
			System.out.println("Deleted the ticket successfully...");
			System.out.println(responseCode);
		}
		else {
			System.out.println("Please check the Issue id or key");
		}	
		System.out.println();
	}
	
	public void addAttachmentToJira() throws UnirestException {
		 HttpResponse<JsonNode> response = Unirest.post("https://resham1.atlassian.net/rest/api/2/issue/FP-30/attachments")
		         .basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
		         .header("Accept", "application/json")
		         .header("X-Atlassian-Token", "no-check")
		         .field("file", new File("newFile.txt"))
		         .asJson();

		 int responseCode = response.getStatus();
		 if(responseCode == 200) {
			System.out.println("Attached file added to jira successfully...");
			System.out.println(responseCode);
	        System.out.println(response.getBody());
		}
		else {
			System.out.println("Please check the Issue id or key");
		}	
		System.out.println();	
	}
}