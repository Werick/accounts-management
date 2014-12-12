/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.Account;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class CompanyService {
    
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    private Map<String,String> companyDetails;
    public CompanyService()
    {
        
    }
    
    public Map<String,String> getCompanyDetails()
    {
        companyDetails=new HashMap<String,String>();
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Customer Details given the internal DB id... ");
            String sqlStmt="Select * from global_property";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                companyDetails.put(rs.getString("property"), rs.getString("property_value"));                              
            }
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
         }
//        finally{
//            Sql.Close();//close open connection
//        }
	
         
        return companyDetails;
    }
    
    public Account getAccountByNumber(String search)
    {
        Account account=null;
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Account Details given the  account number... ");
            String sqlStmt="Select account_id,account_name,account_number, bank, branch,open_balance, account_balance ";
            sqlStmt+=" from account WHERE voided=0 and account_number ='" + search + "'";
            //AccountsManagement.logger.info("Test Product sql "+sqlStmt);
            //System.out.println(sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                account = new Account();
                account.setAccount_number(rs.getString("account_number"));  
                account.setAccount_name(rs.getString("account_name"));                                
                account.setAccount_id(rs.getInt("account_id"));
                account.setBranch(rs.getString("branch"));
                account.setBank_name(rs.getString("bank"));  
                account.setAccount_balance(rs.getDouble("account_balance"));
                account.setOpen_balance(rs.getDouble("open_balance"));                
            }
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
         }
//        finally
//        {
//          Sql.Close();//close open connection 
//        }
	
         
        return account;
    }
    
    public List<Account> getAccountByCategory(String search)
    {
        List<Account> accountList=new ArrayList<Account>();
        Account account=null;
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Account Details given the  account number... ");
            String sqlStmt="Select account_id,account_name,account_number, bank, branch,open_balance, account_balance ";
            sqlStmt+=" from account WHERE voided=0 and category ='" + search + "'";
            AccountsManagement.logger.info("Test Product sql "+sqlStmt);
            //System.out.println(sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                account = new Account();
                account.setAccount_number(rs.getString("account_number"));  
                account.setAccount_name(rs.getString("account_name"));                                
                account.setAccount_id(rs.getInt("account_id"));
                account.setBranch(rs.getString("branch"));
                account.setBank_name(rs.getString("bank"));  
                account.setAccount_balance(rs.getDouble("account_balance"));
                account.setOpen_balance(rs.getDouble("open_balance"));   
                accountList.add(account);
            }
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
         }
//        finally
//        {
//          Sql.Close();//close open connection 
//        }
	
         
        return accountList;
    }
    
    public Account getAccountById(int id)
    {
        Account account=null;
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Account Details given the  account number... ");
            String sqlStmt="Select account_id,account_name,account_number, bank, branch,open_balance, account_balance ";
            sqlStmt+=" from account WHERE voided=0 and account_id =" + id + "";
            //AccountsManagement.logger.info("Test Product sql "+sqlStmt);
            //System.out.println(sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                account = new Account();
                account.setAccount_number(rs.getString("account_number"));  
                account.setAccount_name(rs.getString("account_name"));                                
                account.setAccount_id(rs.getInt("account_id"));
                account.setBranch(rs.getString("branch"));
                account.setBank_name(rs.getString("bank"));  
                account.setAccount_balance(rs.getDouble("account_balance"));
                account.setOpen_balance(rs.getDouble("open_balance"));                
            }
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
         }
//        finally
//        {
//          Sql.Close();//close open connection 
//        }
	
         
        return account;
    }
    
     public List<Account> getAccountByName(String search)
    {
        List<Account> accountList=new ArrayList<Account>();
        
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of All products... ");
            String sqlStmt="Select account_id,account_name,account_number, bank, branch,open_balance, account_balance ";
            sqlStmt+=" from account WHERE voided=0 and account_number like '%" + search + "%'";//
            sqlStmt+=" or account_name like '%"+ search+ "%')";
            sqlStmt+=" or branch like '%"+ search+ "%')";
            sqlStmt+=" or bank like '%"+ search+ "%')";
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Account account = new Account();
                account.setAccount_number(rs.getString("account_number"));  
                account.setAccount_name(rs.getString("account_name"));                                
                account.setAccount_id(rs.getInt("account_id"));
                account.setBranch(rs.getString("branch"));
                account.setBank_name(rs.getString("bank"));  
                account.setAccount_balance(rs.getDouble("account_balance"));
                account.setOpen_balance(rs.getDouble("open_balance")); 
                accountList.add(account);
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
//        finally
//        {
//            Sql.Close();//close open connection
//        }
	 
        return accountList;
    }
    
    public List<Account> getAllAccounts()
    {
        List<Account> accountList=new ArrayList<Account>();        
        
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of All products... ");
            String sqlStmt="Select account_id,account_name,account_number, bank, branch,open_balance, account_balance ";
            sqlStmt+=" from account WHERE voided=0;";
            
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Account account = new Account();
                account.setAccount_number(rs.getString("account_number"));  
                account.setAccount_name(rs.getString("account_name"));                                
                account.setAccount_id(rs.getInt("account_id"));
                account.setBranch(rs.getString("branch"));
                account.setBank_name(rs.getString("bank"));  
                account.setAccount_balance(rs.getDouble("account_balance"));
                account.setOpen_balance(rs.getDouble("open_balance")); 
                accountList.add(account);
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
//        finally
//        {
//            Sql.Close();//close open connection
//        }
	 
        return accountList;
    }
    
    public void saveAccount(Account account, Cash cashTx) throws SQLException
    {
        try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO account (account_number,account_name,branch,bank,open_balance,dateCreated,createdby, category) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);
            pst.setString(1, account.getAccount_number());
            pst.setString(2, account.getAccount_name());            
            pst.setString(3, account.getBranch()); 
            pst.setString(4, account.getBank_name()); 
            pst.setDouble(5, account.getOpen_balance());
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId()); 
            pst.setString(8, account.getCategory()); 
            pst.execute();
            
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
            account.setAccount_id(autoIncKeyFromApi);    
            
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code, amount,tx_type,account,dateCreated,createdby,account_tx_id) VALUES(?,?,?,?,?,?,?,?)";
            pst=Sql.getConnection().prepareStatement(preppedStmtInsert);  
            
            //cashTx.setAccount(Integer.toString(autoIncKeyFromApi));
            cashTx.setTxCode(8);//opening balance
            
            pst.setDate(1, new java.sql.Date(cashTx.getDate().getTime()));   //converting util date to sql date   
            pst.setInt(2, cashTx.getTxCode());   
            pst.setDouble(3, cashTx.getAmount());                    
            pst.setString(4, cashTx.getTxType()); 
            pst.setString(5, cashTx.getAccount());            
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId());            
            pst.setInt(8, autoIncKeyFromApi);     
            pst.execute(); 
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "New Account Record Added...");
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
//         finally
//         {
//             Sql.Close();
//         }
    }
    
     public void saveOpenBalance(Cash cashTx) throws SQLException
    {
        try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code, amount,tx_type,account,dateCreated,createdby) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);  
            
            //cashTx.setAccount("Cash");
            cashTx.setTxCode(8);//opening balance
            
            pst.setDate(1, new java.sql.Date(cashTx.getDate().getTime()));   //converting util date to sql date   
            pst.setInt(2, cashTx.getTxCode());   
            pst.setDouble(3, cashTx.getAmount());                    
            pst.setString(4, cashTx.getTxType()); 
            pst.setString(5, cashTx.getAccount());            
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId());            
            pst.execute(); 
            
            Sql.getConnection().commit();
            
            JOptionPane.showMessageDialog(null, "New Account Record Added...");
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
//         finally
//         {
//             Sql.Close();
//         }
    }
    
    public void updateAccount(Account account) throws SQLException
    {
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Account Details... ");
             //create a prepared statement
             preppedStmtUpdate="update account set account_name=?,account_number=?,branch=?, bank=?, open_balance=?, datechanged=?, changedBy=? category=? WHERE account_id=?";
            //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
            //using Transactions to ensure successfull update
            Sql.getConnection().setAutoCommit(false) ;
            PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
           
            pst.setString(1, account.getAccount_name());   
            pst.setString(2, account.getAccount_number());
            pst.setString(3, account.getBranch()); 
            pst.setString(4, account.getBank_name()); 
            pst.setDouble(5, account.getOpen_balance()); 
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId());
            pst.setString(8, account.getCategory()); 
            pst.setInt(9, account.getAccount_id());
            pst.executeUpdate(); 
            Sql.getConnection().commit(); //commit transaction if successful
            JOptionPane.showMessageDialog(null, "Record Updated...");
         }
         catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            //Log error
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
            Sql.getConnection().rollback();
         }
//         finally
//         {
//             Sql.Close();
//         }
    }
}
