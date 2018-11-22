package ua.com.ncu.portlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;


public class ApiTableau {
	
	private static Log _log = LogFactoryUtil.getLog(ApiTableau.class.getName());
	
	SSLSocketFactory sslSocketFactory = null;
	HostnameVerifier hostnameVerifier = null;
	int responseCode;
	public String siteId;
	private String tbHostname;
	private String tbUser;
	private String tbPassword;
	private String tbContentUrl;
	private String tbSchema;
	public String error;
	
	public ApiTableau(String hostname, String schema, 
					String user, String password, String contentUrl, Boolean trustSelfTLS){
		tbHostname = hostname;
		tbSchema = schema;
		tbUser = user;
		tbPassword = password;
		tbContentUrl = contentUrl;
		
		if(trustSelfTLS){
			trustSefTSL();
		}
		
	}
	
	public String singIn(){
		error = null;
		String token = null;
		String reqBody = "<tsRequest>" +
				"<credentials name=\""+ tbUser +"\" password=\"" + tbPassword + "\" >" +
				"<site contentUrl=\"" + tbContentUrl + "\" />" +
				"</credentials>" +
				"</tsRequest>";
		//InputStream resBody = postRequest("https://" + tbHostname + "/api/" + tbSchema + "/auth/signin",
		InputStream resBody = postRequest(tbHostname + "/api/" + tbSchema + "/auth/signin",
										reqBody,
										null,
										hostnameVerifier, 
										sslSocketFactory);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(resBody);

		   if (doc.getElementsByTagName("error").getLength() == 0) {
			   token = doc.getElementsByTagName("credentials").item(0)
			    		.getAttributes().getNamedItem("token").getNodeValue();
			   _log.debug("Token: " + doc.getElementsByTagName("credentials").item(0)
			    		.getAttributes().getNamedItem("token").getNodeValue());
			 
			   siteId = doc.getElementsByTagName("site").item(0)
			    		.getAttributes().getNamedItem("id").getNodeValue();
			   _log.debug("Site Id: " + doc.getElementsByTagName("site").item(0)
			    		.getAttributes().getNamedItem("id").getNodeValue());
			   
			   _log.debug("User Id: " + doc.getElementsByTagName("user").item(0)
			    		.getAttributes().getNamedItem("id").getNodeValue());
			   //return token;
		   } else {
			   error = errorHandler(doc);
			   _log.debug(error);
		   }
		   
		} catch (SAXException e) {
			 _log.debug("Promleb with xml string");
	    	   e.printStackTrace();
	       } catch (ParserConfigurationException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		}
		return token;
	}
	
	public void singOut(Map<String, String> property){
		//InputStream resBody = postRequest("https://" + tbHostname + "/api/" + tbSchema + "/auth/signout",
		InputStream resBody = postRequest(tbHostname + "/api/" + tbSchema + "/auth/signout",
		 		  "",
		 		  property,
		 		  hostnameVerifier, 
		 		  sslSocketFactory);
		if(responseCode != 204){
			 _log.debug("Error signs out of the current session. Response Code: " + responseCode);
		}
		 _log.debug("Successful signs out of the current session. Response Code: " + responseCode);
	}

	public boolean getUsersonSite(String username, String siteId, Map<String, String> property){
		error = null;

		   //InputStream resBody = getRequest("https://" + tbHostname + "/api/" + tbSchema + "/sites/" + siteId + "/users?filter=name:eq:" + username,
		   InputStream resBody = getRequest(tbHostname + "/api/" + tbSchema + "/sites/" + siteId + "/users?filter=name:eq:" + username,
						  "", 
						  property, 
						  hostnameVerifier, 
						  sslSocketFactory);
		
		   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
		   try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(resBody);
			   if (doc.getElementsByTagName("error").getLength() == 0) {
				   String totalAvailable = doc.getElementsByTagName("pagination").item(0)
				    		.getAttributes().getNamedItem("totalAvailable").getNodeValue();
				   
				    _log.debug("Total User Available: " + doc.getElementsByTagName("pagination").item(0)
				    		.getAttributes().getNamedItem("totalAvailable").getNodeValue());
				  if(totalAvailable.equals("1")) { return true; }
			   } else {
				   error = errorHandler(doc);
				    _log.debug(error);
			   }
			   
			} catch (SAXException e) {
		    	    _log.debug("Promleb with xml string");
		    	   e.printStackTrace();
		       } catch (ParserConfigurationException e) {e.printStackTrace();
			} catch (IOException e) {e.printStackTrace();
			}
		return false;
	}
	
	public boolean addUsertoSite(String username, String siteId, String siteRole,
								 Map<String, String> property){
		error = null;
		//String sURL = "https://" + tbHostname + "/api/" + tbSchema + "/sites/" + siteId + "/users";
		String sURL = tbHostname + "/api/" + tbSchema + "/sites/" + siteId + "/users";
		
		String reqBody = "<tsRequest>" +
				   		 "<user name=\"" + username + "\"" +
				   		 " siteRole=\"" + siteRole + "\"/>" +
				   		 "</tsRequest>";
		   
		InputStream resBody = postRequest(sURL,reqBody, property, 
									   hostnameVerifier, sslSocketFactory);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(resBody);

		   if (doc.getElementsByTagName("error").getLength() == 0) {
			   
			    _log.debug("Created User name: " + doc.getElementsByTagName("user").item(0)
			    		.getAttributes().getNamedItem("name").getNodeValue());
			   
			    _log.debug("Created User Id: " + doc.getElementsByTagName("user").item(0)
			    		.getAttributes().getNamedItem("id").getNodeValue());
			 
			    _log.debug("Add Site Role: " + doc.getElementsByTagName("user").item(0)
			    		.getAttributes().getNamedItem("siteRole").getNodeValue());
			    
			   return true;
		   } else {
			   error = errorHandler(doc);
			    _log.debug(error);
		   }
		   
		} catch (SAXException e) {
	    	    _log.debug("Promleb with xml string");
	    	   e.printStackTrace();
	       } catch (ParserConfigurationException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		}
		
	return false;
	}
	
	public String errorHandler(Document doc){
		return "Error: " + doc.getElementsByTagName("error").item(0)
					.getAttributes().getNamedItem("code").getNodeValue() + "|" +
					"Summary: " + doc.getElementsByTagName("summary").item(0).getTextContent() + "|" +
					"Detail: " + doc.getElementsByTagName("detail").item(0).getTextContent();
	}
	
	public String getTicket(String username, String remoteAddr){
		BufferedReader in = null;
		 try {
			 StringBuffer reqBody = new StringBuffer();
			 reqBody.append(URLEncoder.encode("username", "UTF-8"));
			 reqBody.append("=");
			 reqBody.append(URLEncoder.encode(username, "UTF-8"));
			 reqBody.append("&");
			 reqBody.append(URLEncoder.encode("client_ip", "UTF-8"));
			 reqBody.append("=");
			 reqBody.append(URLEncoder.encode(remoteAddr, "UTF-8"));
		 
		 
		 //String sURL = "https://" + tbHostname + "/trusted";
		 String sURL = tbHostname + "/trusted";
	 
		 InputStream resBody = postRequest(sURL,reqBody.toString(), null, 
				   hostnameVerifier, sslSocketFactory);
		 
		 in = new BufferedReader(new InputStreamReader(resBody));
		 
		 StringBuffer ticket = new StringBuffer();
			 String inputLine;

			while ((inputLine = in.readLine()) != null)  {
			     ticket.append(inputLine);        
			}
			//System.out.println("ticket : " + ticket.toString());
			  return ticket.toString();
		} catch (IOException e) {e.printStackTrace(); }
		 finally {
	         try {
	                if (in != null) in.close();
	         	  } catch (IOException e) {} }
      return null;
	}

	// *** POST *** //
	public InputStream postRequest(String sURL,
								   String oBody,
								   Map<String, String> property,
								   HostnameVerifier hostnameVerifier, 
								   SSLSocketFactory sslSocketFactory){
		
		InputStream body = null;
		try {
			URL url = new URL(sURL);
			
			//HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			if( sURL.startsWith("http:")) {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				if (property != null){
					for (String p : property.keySet()){
						connection.setRequestProperty(p, property.get(p));
					}
				}
							
				OutputStreamWriter writer = new OutputStreamWriter(
			            connection.getOutputStream());
				writer.write(oBody);
				writer.close();
				
				responseCode = connection.getResponseCode();
				
				if (responseCode == 200 || responseCode == 201) {					    
					body =  connection.getInputStream();
					
				}  else {
					body = connection.getErrorStream();
				}
				
			} else {
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
				if(hostnameVerifier != null) { connection.setHostnameVerifier(hostnameVerifier); }
				if(sslSocketFactory != null) { connection.setSSLSocketFactory(sslSocketFactory); }
				
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				if (property != null){
					for (String p : property.keySet()){
						connection.setRequestProperty(p, property.get(p));
					}
				}
							
				OutputStreamWriter writer = new OutputStreamWriter(
			            connection.getOutputStream());
				writer.write(oBody);
				writer.close();
				
				responseCode = connection.getResponseCode();
				
				if (responseCode == 200 || responseCode == 201) {					    
					body =  connection.getInputStream();
					
				}  else {
					body = connection.getErrorStream();
				}
			}
			

			
		} catch (MalformedURLException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		}	
		return body;
	}
	
	// *** GET *** //
	public InputStream getRequest(String sURL,
			   String oBody,
			   Map<String, String> property,
			   HostnameVerifier hostnameVerifier, 
			   SSLSocketFactory sslSocketFactory){

		InputStream body = null;
		try {
			URL url = new URL(sURL);

			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			if(hostnameVerifier != null) { connection.setHostnameVerifier(hostnameVerifier); }
			if(sslSocketFactory != null) { connection.setSSLSocketFactory(sslSocketFactory); }
			connection.setRequestMethod("GET");
			if (property != null){
				for (String p : property.keySet()){
					connection.setRequestProperty(p, property.get(p));
				}
			}

			responseCode = connection.getResponseCode();
			if (responseCode == 200 || responseCode == 201) {
				body =  connection.getInputStream();
			}  else {
				body = connection.getErrorStream();
			}

		} catch (MalformedURLException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();}	
		
		return body;
	}

	public void trustSefTSL(){
		
		SSLContext context;
		try {
			context = SSLContext.getInstance("TLS");
		
		  
		  context.init(null, new TrustManager[]{ new X509TrustManager() {

			    private X509Certificate[] accepted;

			    @Override
			    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
			    }

			    @Override
			    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
			        accepted = xcs;
			    }

			    @Override
			    public X509Certificate[] getAcceptedIssuers() {
			        return accepted;
			    }
			}}, null);
		  
		  sslSocketFactory = context.getSocketFactory();
		  
		  hostnameVerifier = new HostnameVerifier() {

			    @Override
			    public boolean verify(String string, SSLSession ssls) {
			        return true;
			    }
			};
		  
		} catch (NoSuchAlgorithmException e) { e.printStackTrace();
		} catch (KeyManagementException e) {e.printStackTrace();
		}
	}	
}
