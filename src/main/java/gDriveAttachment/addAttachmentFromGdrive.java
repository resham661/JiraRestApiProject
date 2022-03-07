package gDriveAttachment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;

public class addAttachmentFromGdrive {

	public static void main(String[] args) throws UnirestException, JSONException, IOException {
		new addAttachmentFromGdrive();
	}
	
	public addAttachmentFromGdrive() throws IOException, JSONException {
		 
		 String CHARSET = "UTF-8";
		 BufferedReader br = null;
		 
		 
		 String path = "https://drive.google.com/file/d/10GR1I-P9EDuhQPPWY8c2dwLytwfQRp2V/view?usp=sharing";
		 File file = new File(path);
		 
		 URL url = new URL("https://resham1.atlassian.net/rest/api/2/issue/FP-28/attachments");
		 Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy", 8080));
		 HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
		 conn.setRequestMethod("POST");
		 conn.setDoOutput(true);
		 conn.setDoInput(true);
		 conn.setRequestProperty("Cookie", "atlassian.xsrf.token=BLJU-90TC-ITLY-J6LN_8678acc02ca7c5e7a78a68a55ff0e47bbb334781_lout");
		 conn.setRequestProperty("Content-Type", "application/json");
		 conn.setRequestProperty("X-Atlassian-Token", "nocheck");

		 System.out.println(conn.getResponseCode());
		 System.out.println(conn.getResponseMessage());
		 
		 OutputStream outputStream = conn.getOutputStream();
		 PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, CHARSET), true);
		 try ( FileInputStream inputStream = new FileInputStream(file);) {
		 byte[] buffer = new byte[4096];
		 int bytesRead;
		 while ((bytesRead = inputStream.read(buffer)) != -1) {
		 outputStream.write(buffer, 0, bytesRead);
		 }
		 outputStream.flush();
		 }
		 writer.flush();

		 
		 if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
		 br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
		 "UTF-8"));
		 } else {
		 br = new BufferedReader(new InputStreamReader(conn.getErrorStream(),
		 "UTF-8"));
		 }
		 
		 String lineOfResponse = "";
		 StringBuilder strBuild = new StringBuilder();
		 
		 while ((lineOfResponse = br.readLine()) != null) {
		 strBuild.append(lineOfResponse);
		 }
		 
		 String response = strBuild.toString();
		 
		 System.out.println(response);
		 
		 conn.disconnect();
		 br.close();
		 }
}
