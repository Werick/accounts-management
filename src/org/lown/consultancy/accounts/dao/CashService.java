/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class CashService {
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    
    public CashService()
    {
        
    }
    
    
    /**
     * Update Money transfer from a  one account to another account
     * @param User user     
     * @
     */
    public void transferMoney() throws SQLException
    {
         try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Transfering from the source account
             */
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code,tx_type,amount,createdby,datecreated,account,account_tx_id) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
//            pst.setString(1, product.getProductCode());
//            pst.setString(2, product.getProductName()); 
//            pst.setInt(3,  product.getCategory().getCategory_id());
//            pst.setString(4, product.getMeasureUnit()); 
//            pst.setTimestamp(5, Sql.getCurrentTimeStamp());
            pst.setInt(6, MainMenu.gUser.getUserId());   
            pst.execute();
            
            
            /*
             * Transfering to the destination account
             */
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code,tx_type,amount,createdby,datecreated,account,account_tx_id) VALUES(?,?,?,?,?,?,?,?)";
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Product Record Added...");
        }
        catch (SQLException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            //Log error
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
            Sql.getConnection().rollback();
         }
    }
    public double getAvailableBankCollection()
    {
        double cash=getTotalBank()-getTotalBankPayments();
        
        return cash;
    }
    
    public double getAvailableMobileMoney()
    {
        double cash=getTotalMobileMoney()-getTotalMobileMoneyPayments();
        
        return cash;
    }
    
    public double getAvailableCashCollection()
    {
        double cash=getTotalCash()-getTotalCashPayments();
        
        return cash;
    }
    public double getAccountBalanceById(int account_id)
    {
        double cash=getTotalCashByAccountId(account_id)-getTotalPaymentsByAccountId(account_id);
        
        return cash;
    }
    
    public double getTotalCash()
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash... ");
            //get total cash collected
            String sqlStmt="SELECT customer_id,sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='DR' and account ='Cash'";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
    
    
    /*
     * Get cash from the Bank collection Account
     */
    
    
    public double getTotalBank()
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash... ");
            //get total cash collected
            String sqlStmt="SELECT customer_id,sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='DR' and account ='Bank' and account_tx_id=0";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
    
    public double getTotalMobileMoney()
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash... ");
            //get total cash collected
            String sqlStmt="SELECT customer_id,sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='DR' and account ='Mobile Money'";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
    
    public double getTotalCashByAccountId(int account_id)
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash by account id... ");
            //get total cash collected
            String sqlStmt="SELECT sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='DR' and account_tx_id ="+account_id;
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
    
    public double getTotalCashPayments()
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash... ");
            //get total cash collected
            String sqlStmt="SELECT customer_id,sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='CR' and account ='Cash'";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
    
    /*
     * All bank transfers
     */
    public double getTotalBankPayments()
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cheque collection transfers... ");
            //get total cash collected
            String sqlStmt="SELECT sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='CR' and account ='Bank' and tx_code=7 ";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
    
    /*
     * All bank transfers
     */
    public double getTotalMobileMoneyPayments()
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash... ");
            //get total cash collected
            String sqlStmt="SELECT customer_id,sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='CR' and account ='Mobile Money'";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
    
    public double getTotalPaymentsByAccountId( int account_id)
    {
        double cash=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash... ");
            //get total cash collected
            String sqlStmt="SELECT sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='CR' and account_tx_id ="+account_id;
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                cash=rs.getDouble("total");                              
            }            
           
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return cash;
    }
}
