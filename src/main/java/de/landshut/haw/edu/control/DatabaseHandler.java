package de.landshut.haw.edu.control;

import java.sql.*;
import java.util.ArrayList;

import de.landshut.haw.edu.util.Constants;
import de.landshut.haw.edu.util.ErrorCodes;
import de.landshut.haw.edu.util.ServerProperties;

/** 
 * Description of DatabaseHandler <br>
 * Set up connection to database and provide methods to query data.
 * @author Michael Walter
 * @version 1.0 
 */
public class DatabaseHandler {
	
	private Connection con = null;
	
	private ResultSet rs = null;
	
	private PreparedStatement pstmt = null;
	
	
	public DatabaseHandler () {
		System.out.println("DatabaseHandler created");
	}
	
	
	/** 
	 * Load connection driver and
	 * establish a connection to the database.
	 * @param props Holds information about SQL driver and credentials
	 */
	public void init(ServerProperties props) {
		
		// load DB driver
		try {
			Class.forName(props.getProperty("dbDriver"));
			
	    } catch ( ClassNotFoundException e ) {
	    	System.err.println( "Driver '" + props.getProperty("dbDriver") + "' not found!" );
	    	System.exit(ErrorCodes.DRIVER_ERR);
	    }
		
		// establish a connection
		try {
			con = DriverManager.getConnection(
	    		  props.getProperty("dbUrl"),props.getProperty("dbAccount"), props.getProperty("dbPassword") );
			
	    } catch ( SQLException e ) {
			System.err.println( "Establishing connection to database not possible." );
			System.exit(ErrorCodes.DATABASE_CONNECT_ERR);
	    }
		
	}
	
	
	/** 
	 * 	Load data from DB
	 */
	public String[] getSQLData(long oldDate, long newDate) {
		
		String[] result = null;
		
		try {
			pstmt = con.prepareStatement(Constants.SQL_STATEMENT);
			
			pstmt.setLong(1, oldDate);
			pstmt.setLong(2, newDate);

			rs = pstmt.executeQuery();
						
			result = prepareData(rs);
			
			rs.close();
			
			pstmt.close();
			
		} catch ( SQLException e ) {
			System.err.println("getSQLData SQLException.");
			System.exit(ErrorCodes.LOAD_SQL_ERR);
		} 
		
		return result;
	}
	
	
	/** 
	 * 	Load column names and return them as a string
	 */
	public String[] getSQLHeaderData() {
		
		Statement stmt = null;
		
		ResultSetMetaData rsmd;
		
		StringBuilder string = new StringBuilder();
		
		ArrayList<String> information = new ArrayList<String>();
		
	    try {
	        stmt = con.createStatement();
	        
	        ResultSet rs = stmt.executeQuery(Constants.SQL_HEADER_INFO);
	        
	        rsmd = rs.getMetaData();
	        
	        int columnsNumber = rsmd.getColumnCount();
	        
	        //add header
	        for(int i = 1; i <= columnsNumber; i++) {
	        	string.append(rsmd.getColumnName(i) + Constants.DELIMETER);
	        }
	        
	        information.add(string.toString());
	        
	        information.add(Constants.SQL_ORDER);
	        
//	        string = new StringBuilder();
//	        
//	        //add type
//	        for(int i = 1; i <= columnsNumber; i++) {
//	        	string.append(rsmd.getColumnType(i) + " ");
//	        }
//	        
//	        information.add(string.toString());
	        
	    } catch ( SQLException e ) {
			System.err.println("getSQLHeaderData SQLException.");
			System.exit(ErrorCodes.PREPARE_SQL_ERR);
		}
	    
	    return information.toArray(new String[information.size()]);
	}
	
	
	public long getTimestamp(String query) {
		
		long timestamp = 0;
		
		Statement stmt = null;
		
	    try {
	        stmt = con.createStatement();
	        
	        ResultSet rs = stmt.executeQuery(query);
	        
	        while ( rs.next() ) {
	        	timestamp = rs.getLong(1);
			}        
	        
	    } catch ( SQLException e ) {
			System.err.println( "getTimestamp SQLException." );
			System.exit(ErrorCodes.PREPARE_SQL_ERR);
		}
	    
	    return timestamp;
	}
	
	
	/** Description of prepareData(ResultSet rs)
	 * 	Prepare data for transmission.
	 */
	private String[] prepareData(ResultSet rs) {
		
		StringBuilder string = new StringBuilder();
		
		ArrayList<String> data = new ArrayList<String>();
		
		ResultSetMetaData rsmd;
		
		int columnsNumber;
		
		try {
			rsmd = rs.getMetaData();
			
			columnsNumber = rsmd.getColumnCount();
			//add content
			while ( rs.next() ) {
				for(int i = 1; i <= columnsNumber; i++) {
					string.append(rs.getString(i) + Constants.DELIMETER);
				}
				
			    data.add(string.toString());
		        
		        string = new StringBuilder();
			}
			
			rs.close();
			
		} catch ( SQLException e ) {
			System.err.println("Prepare SQL data error.");
			System.exit(ErrorCodes.PREPARE_SQL_ERR);
		} 
	
		return data.toArray(new String[data.size()]);
	}

	
	/**
	 * Close the connection to the database.
	 * @throws SQLException Closing failed
	 */
	public void closeConnection() {
		try {
			if(!con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			System.err.println("Explicit closing of DB connection failed.");
		}
	}
	

}