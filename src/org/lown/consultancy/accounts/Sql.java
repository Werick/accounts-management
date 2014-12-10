/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author LENOVO USER
 */
public class Sql {
     // static global private Connection object
    private static Connection dbConn = null;
    //Developement Credentials
    private static Properties prop = new Properties();
    private static InputStream input = null;
    
     //deve credentials
    private  static String URL="jdbc:mysql://localhost:3308/accounts?autoReconnect=true";
    private  static String PASSWORD="accounts";
    private  static String USERNAME="acounts";
    
    private static void loadProperties()
    {
        try {
 
		input = new FileInputStream("accounts-runtime.properties");
 
		// load a properties file
		prop.load(input);
 
		// get the property value and print it out
		System.out.println(prop.getProperty("connection.username"));
		System.out.println(prop.getProperty("connection.password"));
		System.out.println(prop.getProperty("connection.url"));
 
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    }
    
    // non-static public method to get dbConn connection object
    public static Connection getConnection() {
        // this condition will check if the Connection is not already open then open it.
        if(dbConn==null) {
            loadProperties();

            try{
                URL=prop.getProperty("connection.url");
                USERNAME=prop.getProperty("connection.username");
                PASSWORD=prop.getProperty("connection.password");
                        
                dbConn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                AccountsManagement.logger.info("Opening Connection to the Database");
                System.out.println("Openning Connection to the database");

            }
            catch(SQLException e){
                System.err.println("There was a problem connecting to the database");
                e.printStackTrace();
                //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, e);
                //Log Error messages to the log file
                AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
            }
//            finally{
//                if(dbConn != null)
//                    try{dbConn.close();}catch(SQLException e){e.printStackTrace();}
//            }
        }
        return dbConn;
    }
    
    public  static Statement createStatement()  {
                
        /*
         * By default, ResultSets are completely retrieved and stored in memory. 
         * In most cases this is the most efficient way to operate, and due to the design of the MySQL network protocol is easier to implement. 
         * If you are working with ResultSets that have a large number of rows or large values, and can not allocate heap space in your JVM for 
         * the memory required, you can tell the driver to stream the results back one row at a time.
         * 
         * To enable this functionality, you need to create a Statement instance in the following manner:

            stmt = conn.createStatement (java.sql.ResultSet.TYPE_FORWARD_ONLY,
               java.sql.ResultSet.CONCUR_READ_ONLY);
                stmt.setFetchSize (Integer.MIN_VALUE); 
         * 
         * 
         * The combination of a forward-only, read-only result set, with a fetch size of Integer.MIN_VALUE Serves as a Signal to 
         * the driver to stream Result sets row-by-row. After this any result sets created with the statement will be retrieved row-by-row.
         * 
         * There are some caveats with this approach. You will have to read all of the rows in the result set (or close it)
         * before you can issue any other queries on the connection, or an exception will be thrown. 
         */
        
        Statement statement=null;
        try{
            AccountsManagement.logger.info("Creating Statement...");  
            dbConn=getConnection();
            
            statement = dbConn.createStatement();
           statement.setFetchSize(Integer.MIN_VALUE); //this is important for large tables and allows data streaming
        }
        catch(SQLException ex)
        {
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", ex);
        }
        return statement;
    }
    
     /**
     *
     * @return
     */
    public static java.sql.Timestamp getCurrentTimeStamp() 
    {
 
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
 
    }
    
    /**
     * Return a result set given an sql statement
     * @param String query 
     * @return ResultSet
     */
    public static ResultSet executeQuery(String query){
        ResultSet rs=null;
        try
        {
            Statement statement = createStatement();
            //AccountsManagement.logger.info("Executing Query: " + query);
            rs=statement.executeQuery(query);            //statement.
            //rs.next();
            
        }
        catch(SQLException ex)
        {
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", ex);
        }
       
        return rs;
    }
    
}
