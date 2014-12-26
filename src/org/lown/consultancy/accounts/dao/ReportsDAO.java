/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.ReportDescriptor;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.User;

/**
 *
 * @author LENOVO USER
 */
public class ReportsDAO {
    
    
    

public List<ReportDescriptor> getPettyCashReport() 
    {
        List<ReportDescriptor> pettyCashList=new ArrayList<ReportDescriptor>();
        try
        {
            //log info
            AccountsManagement.logger.info("Create Petty Cash Report... ");
            String sqlStmt="SELECT tx_date,tx_type, description,if(tx_type='CR',amount,null) as cramount, ";
                    sqlStmt+="if(tx_type='DR',amount,null) as dramount ";
                    sqlStmt+="FROM cash_bank c ";
                    sqlStmt+="where account_tx_id=6 and voided=0; ";
                    
                    
           // Open();
           // Statement statement = c.createStatement();
            double curBalance=0.0;
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                ReportDescriptor report=new ReportDescriptor();
                report.setTransactionDate(rs.getDate("tx_date"));
                report.setDescription(rs.getString("description"));
                report.setTxType(rs.getString("tx_type"));
                report.setCrAmount(rs.getDouble("cramount"));
                report.setDrAmount(rs.getDouble("dramount"));
                curBalance=curBalance+report.getDrAmount()-report.getCrAmount();
                report.setBalance(curBalance);                
                pettyCashList.add(report);              
            }
            
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.fillInStackTrace());
         }  
        return  pettyCashList;
    }
}
