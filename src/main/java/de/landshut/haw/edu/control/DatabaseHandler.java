package de.landshut.haw.edu.control;

import java.sql.*;
import java.util.ArrayList;

import de.landshut.haw.edu.util.Constants;
import de.landshut.haw.edu.util.ErrorCodes;
import de.landshut.haw.edu.util.ResultLine;
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
	
	private ServerProperties props;
	
	
	
	public DatabaseHandler (ServerProperties props) {
		
		System.out.println("DatabaseHandler created");
		
		this.props = props;
		
		init();
		
	}
	
	
	
	/** 
	 * Load connection driver and
	 * establish a connection to the database.
	 * @param props Holds information about SQL driver and credentials
	 */
	private void init() {
		
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
	public String[] getSQLDataStringArray(long oldDate, long newDate) {
		
		String[] result = null;
		
		try {
			
			pstmt = con.prepareStatement(props.getProperty("SQL_STATEMENT"));
			
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
	 * 	Load data from DB
	 */
	public String[] getSQLDataStringArray(long currentDate) {
		
		String[] result = null;
		
		try {
			
			pstmt = con.prepareStatement(props.getProperty("SQL_STATEMENT_2"));
			
			pstmt.setLong(1, currentDate);

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
	 * Get data from SQL server and return result as array list.
	 * @param oldDate min search parameter
	 * @param newDate max search parameter
	 * @return ArrayList with ResultLine elements
	 */
	public ArrayList<ResultLine> getSQLDataAsArrayList(long oldDate, long newDate) {		
		
		ArrayList<ResultLine> result;
		
		try {
			pstmt = con.prepareStatement(props.getProperty("SQL_STATEMENT"));
			
			pstmt.setLong(1, oldDate);
			
			pstmt.setLong(2, newDate);

			rs = pstmt.executeQuery();
						
			result = prepareDataToResultLineArrayList(rs);
			
			rs.close();
			
			pstmt.close();
			
			return result;
			
		} catch ( SQLException e ) {
			
			System.err.println("getSQLData SQLException.");
			
			System.exit(ErrorCodes.LOAD_SQL_ERR);
			
		} 
		
		return null;
		
	}
	
	
	
	/** 
	 * 	Load column names and ordering of elements.
	 * @return column names as string array 
	 */
	public String[] getSQLHeaderData() {
		
		Statement stmt = null;
		
		ResultSetMetaData rsmd;
		
		StringBuilder string = new StringBuilder();
		
		ArrayList<String> information = new ArrayList<String>();
		
	    try {
	    	
	        stmt = con.createStatement();
	        
	        ResultSet rs = stmt.executeQuery(props.getProperty("SQL_HEADER_INFO"));
	        
	        rsmd = rs.getMetaData();
	        
	        int columnsNumber = rsmd.getColumnCount();
	        
	        //add header
	        for(int i = 1; i <= columnsNumber; i++) {
	        	
	        	string.append(rsmd.getColumnName(i) + Constants.DELIMETER);
	        	
	        }
	        
	        information.add(string.toString());
	        
	        information.add(props.getProperty("SQL_ORDER"));
	        
	    } catch ( SQLException e ) {
	    	
			System.err.println("getSQLHeaderData SQLException.");
			
			System.exit(ErrorCodes.PREPARE_SQL_ERR);
			
		}
	    
	    return information.toArray(new String[information.size()]);
	}
	
	
	
	/**
	 * Get maximum timestamp.
	 * @param query
	 * @return
	 */
	public long getMaxTimestamp() {
		
		long timestamp = 0;
		
		Statement stmt = null;
		
	    try {
	        stmt = con.createStatement();
	        
	        ResultSet rs = stmt.executeQuery(props.getProperty("SQL_ENDTTIME"));
	        
	        while ( rs.next() ) {
	        	timestamp = rs.getLong(1);
			}        
	        
	    } catch ( SQLException e ) {
			System.err.println( "getTimestamp SQLException." );
			System.exit(ErrorCodes.PREPARE_SQL_ERR);
		}
	    
	    return timestamp;
	}
	
	
	
	
	
	/**
	 * Get minimum timestamp.
	 * @param query
	 * @return
	 */
	public long getMinTimestamp() {
		
		long timestamp = 0;
		
		Statement stmt = null;
		
	    try {
	        stmt = con.createStatement();
	        
	        ResultSet rs = stmt.executeQuery(props.getProperty("SQL_STARTTIME"));
	        
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
	 * Write content of ResultSet into an ArrayList
	 * @param rs
	 * @return
	 */
	private ArrayList<ResultLine> prepareDataToResultLineArrayList(ResultSet rs) {
		
		ArrayList<ResultLine> data = new ArrayList<ResultLine>();
		
		ResultLine tmp;

		try {
			while ( rs.next() ) {
				
				tmp = new ResultLine();
					
				tmp.setId(rs.getInt(props.getProperty("ID_COLUMN")));
				
				tmp.setTimestamp(rs.getLong(props.getProperty("ID_COLUMN")));
				
				tmp.setX(rs.getDouble(props.getProperty("X_COLUMN")));
				
				tmp.setY(rs.getDouble(props.getProperty("Y_COLUMN")));
				
				tmp.setZ(rs.getDouble(props.getProperty("Z_COLUMN")));
				
				tmp.setAcceleration(rs.getDouble(props.getProperty("ACCELERATION_COLUMN")));
				
				tmp.setAccelerationX(rs.getDouble(props.getProperty("ACCELERATION_X_COLUMN")));
				
				tmp.setAccelerationY(rs.getDouble(props.getProperty("ACCELERATION_Y_COLUMN")));
				
				tmp.setAccelerationZ(rs.getDouble(props.getProperty("ACCELERATION_Z_COLUMN")));
				
				tmp.setVelocityX(rs.getDouble(props.getProperty("VELOCITY_X_COLUMN")));
				
				tmp.setVelocityY(rs.getDouble(props.getProperty("VELOCITY_Y_COLUMN")));
				
				tmp.setVelocityZ(rs.getDouble(props.getProperty("VELOCITY_Z_COLUMN")));
				
				data.add(tmp);
				
			}
			
			rs.close();
			
		} catch ( SQLException e ) {
			
			System.err.println("Prepare SQL data error.");
			
			System.exit(ErrorCodes.PREPARE_SQL_ERR);
			
		} 
	
		return data;
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