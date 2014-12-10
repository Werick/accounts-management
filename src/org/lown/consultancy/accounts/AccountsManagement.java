/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.lown.consultancy.accounts.dialog.LoginDialog;

/**
 *
 * @author LENOVO USER
 */
public class AccountsManagement {

    
    public static final Logger logger = Logger.getLogger(AccountsManagement.class.getName());  
    public static FileHandler fh;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
          loadJdbcDriver(); // Load the JDBC driver we will use.
          try {  
             
            // This block configure the logger with handler and formatter
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            java.util.Date today = new java.util.Date();
            fh = new FileHandler("LownAccounting_"+df.format(today)+".log",true);  //create file if does not exist
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  

            // the following statement is used to log any messages  
            logger.info("LOWN Accounting Application logs...");  

            } catch (SecurityException e) {  
                e.printStackTrace();  
                //Logger.getLogger(FacesFingerPrintProject.class.getName()).log(Level.SEVERE, "ERROR", e);
                logger.log(Level.SEVERE, "ERROR", e);
                //logger.w
            } catch (IOException e) {  
                e.printStackTrace();
                logger.log(Level.SEVERE, "ERROR", e);
            }  

            logger.info("Application Starting..."); 
            
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {                
              LoginDialog.createAndShowGUI();
            }
    });
    }
    
    private static void loadJdbcDriver() {
        final String driverName = "com.mysql.jdbc.Driver";
        try {
            Class.forName(driverName).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(AccountsManagement.class.getName()).log(Level.SEVERE,
                    "Can't load JDBC driver " + driverName, ex);
            System.exit(1);
            logger.log(Level.SEVERE, "ERROR", ex);
        }
    }
}
