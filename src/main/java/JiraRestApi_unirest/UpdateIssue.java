package JiraRestApi_unirest;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class UpdateIssue {

	public static void main(String[] args) throws UnirestException {

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
		Unirest.setObjectMapper(new ObjectMapper() {
		   private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = 
				   new com.fasterxml.jackson.databind.ObjectMapper();

		   public <T> T readValue(String value, Class<T> valueType) {
		       try {
		           return jacksonObjectMapper.readValue(value, valueType);
		       } catch (IOException e) {
		           throw new RuntimeException(e);
		       }
		   }

		   public String writeValue(Object value) {
		       try {
		           return jacksonObjectMapper.writeValueAsString(value);
		       } catch (JsonProcessingException e) {
		           throw new RuntimeException(e);
		       }
		   }
		});

		HttpResponse<JsonNode> response = Unirest.put("https://resham1.atlassian.net/rest/api/3/issue/FP-18")
		  .basicAuth("reshamguru123@gmail.com", "kVDRMlQotpT6Wm8mu3FP9C46")
		  .header("Accept", "application/json")
		  .header("Content-Type", "application/json")
		  .body(dataTable)
		  .asJson();

		int responseCode = response.getStatus();
		if(responseCode == 204) {
			System.out.println("Updated the ticket successfully...");
			System.out.println(responseCode);
//			System.out.println(response.getBody());
		}
		else {
			System.out.println("Please check the Issue id or key");
		}
	}
}