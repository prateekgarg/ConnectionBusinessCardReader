package imageToJson;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
//import java.nio.file.Files;

import javax.imageio.ImageIO;
//import javax.net.ssl.HttpsURLConnection;
//import org.json.*;
import org.apache.commons.codec.binary.Base64;

//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ImageToJSON {

	//private final String USER_AGENT = "Mozilla/5.0";
	
	public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeBase64URLSafeString(imageByteArray);
    }
	public static byte[] decodeImage(String imageDataString) {
        return Base64.decodeBase64(imageDataString);
    }
	public static void main(String[] args) throws Exception {
		
		
		File file = new File("Resources/Sample Card.jpg");
		FileInputStream imageInFile;
		
		try{
			
			imageInFile = new FileInputStream(file);
			byte imageData[] = new byte[(int) file.length()];
			BufferedImage bi = ImageIO.read(file);
			WritableRaster raster = bi.getRaster();
			DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
			data.getData();
			
			
			//byte imageData2[] = Files.readAllBytes(file.toPath());
			
			
			imageInFile.read(imageData);
			//System.out.println("byte array length" + imageData2.length);
			
			//String toPass = new String(imageData);
			
			
			
			String imageDataString = encodeImage(imageData);
			//System.out.println("String Length" + imageDataString.length());
			//System.out.println("Image Data String: \n"+imageDataString);
			//String JsonString = "{\"imageString\" : \"" + imageDataString + "\"}";
			//JsonParser par = new JsonParser();
			
			//JsonObject value = (JsonObject)par.parse(JsonString);
			
			//System.out.println("Json Object"+ value.toString().length());
			
			/*
			 *  byte array
				httpwebreq
				req - method-post
				content len() -array len
				req stream get()
				write in stream
				flushStream()
				close() 

			 */
			
			//byte arr[] = new byte[(int)value.toString().length()];
			
			ImageToJSON obj = new ImageToJSON();
			obj.sendPost(imageDataString);
						
		}catch(FileNotFoundException e){
			System.out.println("Image Not Found"+e);
		}
		catch(IOException ioe){
			System.out.println("Exception while reading Image" + ioe);
		}
		catch (JsonSyntaxException e) {
			
			System.out.println("Json syntax is messed up it seems" + e);
		}
		
		
	}
	private void sendPost(String value) throws Exception {
		
//		for(char c : value.toCharArray()){
//			if(c == '_')
//				c = '/';
//		}
		
		String val2 = value.replaceAll("_", "/");
		
		String url = "http://businesscardreader.cloudapp.net/api/values";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost",8888)));
		//con.setRequestProperty("http.proxyhost", "localhost");
		
		//add request header
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");
		
		
		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345"; //parameters needed
 
		// Send post request
		con.setDoOutput(true);
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		//wr.writeBytes(urlParameters);
		wr.write("\""+value+"\"");
		wr.flush();
		wr.close();
 
		//int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + urlParameters);
//		System.out.println("Response Code : " + responseCode);
// 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
 
	}

}
