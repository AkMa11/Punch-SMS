package smsAndMailer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileRead {
	private static Connection con;
	private static List<String> date_time_list=new ArrayList<>();
	private static String date, logDate;
	
	public FileRead() throws IOException, SQLException {
		
		date = getTodaysDate();
		
		databaseConnection();
		
		checkIfTableExistsAndCreateNewIfItDoesnt();
         
		readLogFile();
		
		con.close();
	}

	private void readLogFile() throws SQLException {
		String filePath = "E:\\logs\\" + date;
		
		try {
		    BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
		    String lineText = null;
		 
		    while ((lineText = lineReader.readLine()) != null) {
            	String [] separateText = lineText.split(",");
                String s_id = separateText[0];
                String date_time = separateText[1];
                String in_out = separateText[2];
                
                checkingIfLogEntryExistsInDatabase(s_id, date_time, in_out);
		    }
		 
		    lineReader.close();
		    } catch (IOException ex) {
//		    System.err.println(ex);
		    System.out.println("No logs today.");
		}
	}

	private void checkingIfLogEntryExistsInDatabase(String s_id, String date_time, String in_out) throws SQLException {
		boolean ans = date_time_list.contains(date_time);
		
		if(ans) {
			System.out.println("No new entries.");
		} else {
			sendingLogToMonthlyDatabase(s_id, date_time, in_out);
		}
	}

	private void sendingLogToMonthlyDatabase(String s_id, String date_time, String in_out) throws SQLException {
		System.out.println("New entry detected.");
		
		String query = " insert into " + logDate
				+ "(student_id, punch_date_time, direction, report)"
				+ " values (?, ?, ?, ?)";
        
		PreparedStatement preparedStmt = con.prepareStatement(query);
		preparedStmt.setString (1, s_id);
		preparedStmt.setString (2, date_time);
		preparedStmt.setString   (3, in_out);
		preparedStmt.setString(4, "0");

		preparedStmt.execute();
		
		System.out.println("Entry append success.");
	}

	private String getTodaysDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter dtfLog = DateTimeFormatter.ofPattern("MM_yyyy"); 
		LocalDateTime now = LocalDateTime.now();  
		date = dtf.format(now) + ".txt";
		logDate = "log_" + dtfLog.format(now);
		return date;
	}

	private static void checkIfTableExistsAndCreateNewIfItDoesnt() throws SQLException {
		Statement stmt = con.createStatement();
		
		DatabaseMetaData dbm = con.getMetaData();
    	// check if "current month log" table is there
    	ResultSet tables = dbm.getTables(null, null, logDate, null);
    	if (tables.next()) {
    	  // Table exists
    		getDateAndTimeFromDatabaseAndPutThemInList(stmt);
    	}
    	else {
    	  // Table does not exist
    		createNewMonthLogTable(stmt);
    	}
	}
	private static void createNewMonthLogTable(Statement stmt) throws SQLException {
		String sql = "CREATE TABLE " + logDate + " (student_id VARCHAR(10), "
				+ "punch_date_time VARCHAR(25), direction VARCHAR(1), "
				+ "report VARCHAR(1))";
		stmt.executeUpdate(sql);
		System.out.println("Table created");
	}
	private static void getDateAndTimeFromDatabaseAndPutThemInList(Statement stmt) throws SQLException {
		String sql = "SELECT punch_date_time FROM " + logDate;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
		   date_time_list.add(rs.getString("punch_date_time"));
		}
	}

	private static void databaseConnection() {
		try
			{  
				Class.forName("com.mysql.cj.jdbc.Driver");
				con=DriverManager.getConnection(  
				"jdbc:mysql://IP:PORT/DATABASE","USERNAME","PASSWORD");
				System.out.println("Connection success!"); 
			}
			catch(Exception e)
			{
				System.out.println(e);  
			}
	}

}