package smsAndMailer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class SendSMSandMail {
	private static Connection con;
	private static Statement stmt = null;
	private static String date, logDate, s_id, dir=null;
	private static List<String> studentDirectionList = new ArrayList<>();
	private static List<String> studentInfoList = new ArrayList<>();
	final String zero = "00000";
	final String zeroSix = "000000";
	final int zeroLength = zero.length();
	final int zeroSixLength = zeroSix.length();
	private final String USER_AGENT = "Mozilla/5.0";
	
	public SendSMSandMail() throws SQLException, EmailException, IOException {
		
		date = getTodaysDate();
		
		databaseConnection();
		
		stmt = con.createStatement();
		
	    getStudentIdAndDirectionFromLogDatabase();
	    
	    putAllStudentInfoIntoList();
	    
	    sendEmailAndSMS();
	    
	    clearListsForNextLogs();
	      
	      con.close();
	}

	private void sendEmailAndSMS() throws EmailException, 
	UnsupportedEncodingException, MalformedURLException,
			IOException, ProtocolException, SQLException {
		for(String obj:studentInfoList) {
		    
		    String [] separateText = obj.split("	");
            String stud_id = separateText[0];
            String in_out = separateText[1];
            String p_d_t = separateText[2];
            String s_name = separateText[3];
            String p_no = separateText[4];
            
            if(p_no.equals("null")){
        		System.out.println("Phone number for " 
        	+ stud_id + " " + s_name + " is not in the database.");
        	} else {
            	sendSMS(in_out, p_d_t, s_name, p_no);
        	}
     	   

		    System.out.println(obj);
		    
		    updateStudentReportInLogDatabase(p_d_t);
	    }
	}

	private void clearListsForNextLogs() {
		studentInfoList.clear();
	    studentDirectionList.clear();
	}

	private void updateStudentReportInLogDatabase(String p_d_t) throws SQLException {
		String sql3 = "UPDATE "+logDate+" SET report ='1' WHERE punch_date_time = '"+p_d_t+"'";
		stmt.executeUpdate(sql3);
		System.out.println("Log database Updated!");
	}

	private void sendSMS(String in_out, String p_d_t, String s_name, String p_no)
			throws UnsupportedEncodingException, MalformedURLException, IOException, ProtocolException {
		String url = "http://tra.bulksmshyderabad.co.in/websms/"
		    		+ "sendsms.aspx?userid=USERID&password=PASSWORD&"
		    		+ "sender=UNQTUT&mobileno="+URLEncoder.encode(p_no, "UTF-8")
		    		+"&msg="+URLEncoder.encode(s_name+" has "+in_out
		    				+" The Unique Tutorials on "+p_d_t, "UTF-8");
		
		URL obj1 = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj1.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

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
		System.out.println("SMS sent!");
	}

//	private void sendEmail(String in_out, String p_d_t, String s_name, String p_email) throws EmailException {
//		Email email = new SimpleEmail();
//		email.setHostName("EMAIL HERE");
//		email.setSmtpPort(587);
//		email.setAuthenticator(new DefaultAuthenticator("EMAIL HERE", "PASSWORD HERE"));
//		email.setSSLOnConnect(false);
//		email.setFrom("EMAIL HERE");
//		email.setSubject("Today's attendance report");
//		email.setMsg(s_name+" has "+in_out+" The Unique Tutorials on "+p_d_t);
//		email.addTo(p_email);
//		email.send();
//		System.out.println("Email sent!");
//	}

	private void putAllStudentInfoIntoList() throws SQLException {
		for(String obj:studentDirectionList) {
	    	String [] separateText = obj.split("	");
            String stud_id = separateText[0];
            String in_out = separateText[1];
            String p_d_t = separateText[2];
            
            
            String sql2 = "SELECT p_no, s_name FROM main_details_1 WHERE s_id = " +stud_id;
	  	    ResultSet rs2 = stmt.executeQuery(sql2);
	  	    //STEP 5: Extract data from result set
	  	    while(rs2.next()){
	  	       //Retrieve by column name
	  	  	      studentInfoList.add(obj + "	" + rs2.getString("s_name") + "	" 
	  	  	    		  + rs2.getString("p_no"));
	  	      }
	    }
	}

	private void getStudentIdAndDirectionFromLogDatabase() throws SQLException {
		String sql = "SELECT student_id, direction, punch_date_time"
	    		+ " FROM " + logDate + " WHERE report = '0'";
	    ResultSet rs = stmt.executeQuery(sql);
	    //STEP 5: Extract data from result set
	    while(rs.next()){
	       //Retrieve by column name
	    	  s_id = rs.getString("student_id");
	    	  s_id = s_id.startsWith(zeroSix) ? s_id.substring(zeroSixLength) : s_id;
	  		  s_id = s_id.startsWith(zero) ? s_id.substring(zeroLength) : s_id;
	  		  dir = rs.getString("direction");
	  		  dir = dir.replace("0", "reached at");
	  		  dir = dir.replace("1", "departed from");
	    	  studentDirectionList.add(s_id+"	"+dir+"	"
	    			  					+rs.getString("punch_date_time"));
	      }
	}
	
	private static void databaseConnection() {
		try
			{  
				Class.forName("com.mysql.cj.jdbc.Driver");
				con=DriverManager.getConnection(  
				"jdbc:mysql://IP:PORT/DATABASE","USERNAME","PASSWORD");
			}
			catch(Exception e)
			{
				System.out.println(e);  
			}
	}
	
	private String getTodaysDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter dtfLog = DateTimeFormatter.ofPattern("MM_yyyy"); 
		LocalDateTime now = LocalDateTime.now();  
		date = dtf.format(now) + ".txt";
		logDate = "log_" + dtfLog.format(now);
		return date;
	}
}
