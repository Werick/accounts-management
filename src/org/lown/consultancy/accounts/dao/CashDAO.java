/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.CashTransfer;
import org.lown.consultancy.accounts.ContraAccount;
import org.lown.consultancy.accounts.ContraExpenses;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class CashDAO {
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    
    public CashDAO()
    {
        
    }
    
    public List<ContraExpenses> getAllExpenses()
    {
        List<ContraExpenses> expenses=new ArrayList<ContraExpenses>();
        
        try
        {
            //log info
            AccountsManagement.logger.info("get available cash... ");
            //get total cash collected
            String sqlStmt="SELECT expense_id,expensedate,description,contra_id,amount,createdby,datecreated,pettycash_id FROM expenses t "; 
            sqlStmt+="where voided=0 ";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                ContraExpenses c=new ContraExpenses();
                ContraAccount acc=new ContraAccount();
                acc.setContra_id(rs.getInt("contra_id"));
                c.setAmount(rs.getDouble("amount"));
                c.setExpenseDate(rs.getDate("expensedate"));
                c.setDescription(rs.getString("description"));
                c.setPcv_id(rs.getInt("pettycash_id"));
                c.setExpense_id(rs.getInt("expense_id"));
                c.setAccount(acc);
                expenses.add(c);
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
        return expenses;
    }
    
     /**
     * Update Money transfer from a  one source account to another destination account
     * @param Cash pettyCash     
     * @param List<ContraExpenses> expensesList
     */
    public void postPettyCashExpense(Cash pettyCash, List<ContraExpenses> expensesList) throws SQLException
    {
         try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Posting the Petty Cash Expenses Total
             */
            
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code,tx_type,amount,createdby,datecreated,account,account_tx_id,description) ";
            preppedStmtInsert+="VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);            
            pst.setDate(1, new java.sql.Date(pettyCash.getDate().getTime()));
            pst.setInt(2,  pettyCash.getTxCode());
            pst.setString(3, pettyCash.getTxType());            
            pst.setDouble(4, pettyCash.getAmount());            
            pst.setInt(5, MainMenu.gUser.getUserId());  
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setString(7, "Petty Cash"); //this needs to be updates to use the Account Object e.g. account.getAccountName()
            pst.setInt(8,  6); //this needs to be updates to use the Account Object e.g. account.getAccount_id()
            pst.setString(9, "Petty Cash Expenses Voucher"); 
            pst.execute();
            
            // Using Statement.getGeneratedKeys()
                // to retrieve the value of an auto-increment
                // value
                //
                int autoIncKeyFromApi = -1; 
                ResultSet rs = pst.getGeneratedKeys(); 
                if (rs.next()) 
                {
                    autoIncKeyFromApi = rs.getInt(1);
                    //System.out.println("Key returned from getGeneratedKeys():" + autoIncKeyFromApi);
                } 
                else 
                {
                    // throw an exception from here
                }
                rs.close(); 
                rs = null; 
                System.out.println("Key returned from getGeneratedKeys():"   + autoIncKeyFromApi);
                int pettyCash_id= autoIncKeyFromApi;
            /*
             * Posting Contra Expenses to the Expenses Account
             */
            
                for(ContraExpenses c: expensesList)
                {
                    preppedStmtInsert="INSERT INTO expenses (expensedate,description,contra_id,amount,createdby,datecreated,pettycash_id) ";
                    preppedStmtInsert+="VALUES(?,?,?,?,?,?,?)";
                    pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
                    pst.setDate(1, new java.sql.Date(c.getExpenseDate().getTime()));
                    pst.setString(2,  c.getDescription());
                    pst.setInt(3, c.getAccount().getContra_id());            
                    pst.setDouble(4, c.getAmount());            
                    pst.setInt(5, MainMenu.gUser.getUserId());  
                    pst.setTimestamp(6, Sql.getCurrentTimeStamp());
                    pst.setInt(7, pettyCash_id);                    
                    pst.execute();
                }
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Contra Expenses Successfully Posted...");
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
    
    /**
     * Update Money transfer from a  one source account to another destination account
     * @param CashTransfer source     
     * @param CashTransfer destination
     */
    public void transferMoney(CashTransfer source, CashTransfer destination) throws SQLException
    {
         try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Transfering from the source account
             */
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code,tx_type,amount,createdby,datecreated,account,account_tx_id,description) ";
            preppedStmtInsert+="VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            pst.setDate(1, new java.sql.Date(source.getTransferDate().getTime()));
            pst.setInt(2,  source.getTransactionCode());
            pst.setString(3, source.getTransactionType());            
            pst.setDouble(4, source.getAmount());            
            pst.setInt(5, MainMenu.gUser.getUserId());  
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setString(7, source.getAccount().getAccount_name()); 
            pst.setInt(8,  source.getAccount().getAccount_id());
            pst.setString(9, source.getDescription()); 
            pst.execute();
            
            
            /*
             * Transfering to the destination account
             */
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code,tx_type,amount,createdby,datecreated,account,account_tx_id,description) ";
            preppedStmtInsert+="VALUES(?,?,?,?,?,?,?,?,?)";
            pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            pst.setDate(1, new java.sql.Date(destination.getTransferDate().getTime()));
            pst.setInt(2,  destination.getTransactionCode());
            pst.setString(3, destination.getTransactionType());            
            pst.setDouble(4, destination.getAmount());            
            pst.setInt(5, MainMenu.gUser.getUserId());  
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setString(7, destination.getAccount().getAccount_name()); 
            pst.setInt(8,  destination.getAccount().getAccount_id());
            pst.setString(9, destination.getDescription()); 
            pst.execute();
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Cash Transfer Successfully Excecuted...");
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
            sqlStmt+="where voided=0 and tx_type='DR' and account ='Cash' and account_tx_id in(0,1) ";
           
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
            sqlStmt+="where voided=0 and tx_type='CR' and account ='Cash' and account_tx_id in(0,1)";
           
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
            sqlStmt+="where voided=0 and tx_type='CR' and account ='Bank' and tx_code in(7) ";
           
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
