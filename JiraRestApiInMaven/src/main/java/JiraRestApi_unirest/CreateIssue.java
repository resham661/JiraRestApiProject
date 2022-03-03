package JiraRestApi_unirest;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class CreateIssue {
	
	public static void main(String[] args) throws UnirestException, JacksonException {

		// The dataTable definition using the Jackson library		
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode dataTable = new ObjectNode(factory);
		{
			com.fasterxml.jackson.databind.node.ObjectNode fields = dataTable.putObject("fields");
			{
				com.fasterxml.jackson.databind.node.ObjectNode project = fields.putObject("project");
				{
					project.put("key", "FP");
				}
				fields.put("summary", "Main order flow broken");		    
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
					issuetype.put("name","Story");
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
		

		//String encodedString = Base64.getEncoder().encodeToString(dataTable.getBytes());

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

	}
}