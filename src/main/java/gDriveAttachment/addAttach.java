package gDriveAttachment;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;

public class addAttach {

	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".credentials/n/MyProjectWithJira");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 * 
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/drive-java-quickstart
	 */
	private static final java.util.Collection<String> SCOPES = DriveScopes.all();

	static {
	    try {
	        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	        DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
	    } catch (Throwable t) {
	        t.printStackTrace();
	        System.exit(1);
	    }
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws Exception 
	 */
	public static Credential authorize() throws Exception {
	    // Load client secrets.
	    InputStream in = addAttach.class.getResourceAsStream("/client_secret.json");
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

	    // Build flow and trigger user authorization request.
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
	            .setDataStoreFactory(DATA_STORE_FACTORY)
	            .setAccessType("offline").build();
	    Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	    System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
	    return credential;
	}

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws Exception 
	 */
	public static Drive getDriveService() throws Exception {
	    Credential credential = authorize();
	    return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) throws Exception {
	    // Build a new authorized API client service.
	    Drive service = getDriveService();
	    // Print the names and IDs for up to 10 files.
	    FileList result = service.files().list().execute();
	    List<com.google.api.services.drive.model.File> files = result.getFiles();
	    if (files == null || files.size() == 0) {
	        System.out.println("No files found.");
	    } else {

	        for (com.google.api.services.drive.model.File file : files) {

	            String fname = file.getName();
	            String ex = fname.substring(fname.lastIndexOf(".") + 1);

	            try {
	                com.google.api.services.drive.Drive.Files f = service.files();
	                HttpResponse httpResponse = null;
	                if (ex.equalsIgnoreCase("xlsx")) {
	                    httpResponse = f.export(file.getId(),
	                                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").executeMedia();

	                } else if (ex.equalsIgnoreCase("docx")) {
	                    httpResponse = f.export(file.getId(),
	                    		"application/vnd.openxmlformats-officedocument.wordprocessingml.document").executeMedia();
	                } else if (ex.equalsIgnoreCase("pptx")) {
	                    httpResponse = f.export(file.getId(), 
	                            "application/vnd.openxmlformats-officedocument.presentationml.presentation").executeMedia();

	                } else if (ex.equalsIgnoreCase("pdf") || ex.equalsIgnoreCase("jpg") || ex.equalsIgnoreCase("png")) {
	                    Get get = f.get(file.getId());
	                    httpResponse = get.executeMedia();
	                }
	                if (null != httpResponse) {
	                    InputStream instream = httpResponse.getContent();
	                    FileOutputStream output = new FileOutputStream(file.getName());
	                    try {
	                        int l;
	                        byte[] tmp = new byte[2048];
	                        while ((l = instream.read(tmp)) != -1) {
	                            output.write(tmp, 0, l);
	                        }
	                    } finally {
	                        output.close();
	                        instream.close();
	                    }
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
}