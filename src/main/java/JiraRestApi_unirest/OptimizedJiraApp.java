package JiraRestApi_unirest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class OptimizedJiraApp {
	
	final String username="reshamguru123@gmail.com";
	final String api_key="rZ1f4xYBOj4LOekkx5lz82C4";
	final String url = "https://resham1.atlassian.net/rest/api/3/issue/";
	
	/** Application name. */
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
  	
        // Load client secrets.
        InputStream in = OptimizedJiraApp.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
       
        //returns an authorized Credential object.
        return credential;
    }
    
	public void createIssue() throws UnirestException {
		//The dataTable definition using the Jackson library		
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

		HttpResponse<JsonNode> response = Unirest.post(url)
						.basicAuth(username, api_key)
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
		HttpResponse<JsonNode> response = Unirest.get(url + "FP-25")
				  .basicAuth(username, api_key)
				  .header("Accept", "application/json")
				  .asJson();

		int responseCode = response.getStatus();
		if(responseCode == 200) {
			System.out.println(responseCode);
			System.out.println(response.getBody());
		}
		else {
			System.out.println("Please check the Issue id or key\n" + response.getBody());
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

		HttpResponse<JsonNode> response = Unirest.put(url + "FP-30")
					.basicAuth(username, api_key)
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
			System.out.println("Please check the Issue id or key\n" + response.getBody());
		}	
		System.out.println();
	}
	
	public void deleteIssue() throws UnirestException {
		HttpResponse<String> response = Unirest.delete(url + "FP-24")
				  .basicAuth(username, api_key)
				  .asString();

		int responseCode = response.getStatus();
		if(responseCode == 204) {
			System.out.println("Deleted the ticket successfully...");
			System.out.println(responseCode);
		}
		else {
			System.out.println("Please check the Issue id or key\n" + response.getBody());
		}	
		System.out.println();
	}
	
	public void addAttachmentToJira() throws UnirestException {
		 HttpResponse<JsonNode> response = Unirest.post(url + "FP-30/attachments")
		         .basicAuth(username, api_key)
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
			System.out.println("Please check the Issue id or key\n" + response.getBody());
		}	
		System.out.println();	
	}
	
	public void DownloadFileFromGdrive() throws IOException, GeneralSecurityException, UnirestException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT,JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
        String fileId = "1mB8ur76r5gxBtkIhiRyejerHtg04yUvY";
        
        OutputStream outputstream = new FileOutputStream(fileId);
        service.files().get(fileId)
        .executeMediaAndDownloadTo(outputstream);   
        
        HttpResponse<JsonNode> response = Unirest.post(url + "FP-27/attachments")
		         .basicAuth(username, api_key)
		         .header("Accept", "application/json")
		         .header("X-Atlassian-Token", "no-check")
		         .field("file", new File(fileId))
		         .asJson();

        int responseCode = response.getStatus();
		if(responseCode == 200) {
			System.out.println("Attached the document in jira from the Google Drive successfully...");
			System.out.println(responseCode);
			System.out.println(response.getBody());
		}
		else {
			System.out.println("Please check the Issue id or key\n" + response.getBody());
		}        
        outputstream.close();
    }
	
	public static void main(String[] args) throws UnirestException, IOException, GeneralSecurityException {

		OptimizedJiraApp jiraApp = new OptimizedJiraApp();
		jiraApp.createIssue();
		jiraApp.getTicket();
		jiraApp.updateIssue();
		jiraApp.deleteIssue();	
		jiraApp.addAttachmentToJira();
		jiraApp.DownloadFileFromGdrive();
	}	
}