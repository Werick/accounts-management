/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.Prepayment;
import org.lown.consultancy.accounts.SalesItem;
import org.lown.consultancy.accounts.SalesTransaction;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class SalesService {
    
   
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    //private Sql Sql;
    public SalesService()
    {
       //Sql=new Sql();
    }
    
    public Map<String, Integer> getSalesRepMap()
    {
        Map<String, Integer> salesRepMap=new HashMap<String,Integer>();
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of all Product Category and put them in a hash map... ");
            String sqlStmt="Select staff_id,staffname from staff WHERE voided=0 "; //            
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               salesRepMap.put(rs.getString("staffname"), (Integer)rs.getInt("staff_id"));
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
	 
        
        return salesRepMap;
    }
    
    public void postTransactionSummary(SalesTransaction salesTx) throws SQLException
    {
        try
        {
           
           // Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Instert the transaction summary then followed by individual transaction items
             */
            
            preppedStmtInsert="INSERT INTO transactionsummary (txdate,txamount,customer_id,txtype,salesrep_id,dateCreated,createdby,paid) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);            
            
            pst.setDate(1, new java.sql.Date(salesTx.getTxSalesDate().getTime()));   //converting util date to sql date         
            pst.setDouble(2, salesTx.getTxSalesAmount());
            pst.setInt(3, salesTx.getCustomer().getCustomer_id());            
            pst.setString(4, salesTx.getTxType()); 
            pst.setInt(5, salesTx.getSalesRep()); 
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId()); 
            if(salesTx.isPaid())
            {
                pst.setInt(9, 1);                        
            }
            else
            {
                pst.setInt(9, 0); 
            }
            pst.execute();
            
                // Example of using Statement.getGeneratedKeys()
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
                int txid=getLastTransactionSummaryId(); 
                System.out.println("Last Inserted Id:"   + txid);
                salesTx.setTx_summary_id(autoIncKeyFromApi);          
            
                Sql.getConnection().commit();
                JOptionPane.showMessageDialog(null, "Transaction successfully Posted...");
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
    
    public void postPrepayment(Prepayment prepayment) throws SQLException
    {
        try
        {
           
           // Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Instert the transaction summary then followed by individual transaction items
             */
            
            preppedStmtInsert="insert into prepayment (customer_id,amount,prepaydate,datecreated,createdby) VALUES(?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            
            pst.setInt(1, prepayment.getCustomer().getCustomer_id());             
            pst.setDouble(2, prepayment.getAmount()); 
            pst.setDate(3, new java.sql.Date(prepayment.getDate().getTime()));   //converting util date to sql date         
            pst.setTimestamp(4, Sql.getCurrentTimeStamp());
            pst.setInt(5, MainMenu.gUser.getUserId());             
            pst.execute();         
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Transaction successfully Posted...");
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
    
    public void postTransaction(SalesTransaction salesTx, List<SalesItem> salesItems) throws SQLException
    {
        try
        {
           
            //Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Instert the transaction summary then followed by individual transaction items
             */
            
            preppedStmtInsert="INSERT INTO transactionsummary (txdate,txamount,customer_id,txtype,salesrep_id,dateCreated,createdby,txduedate,paid) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);            
            
            pst.setDate(1, new java.sql.Date(salesTx.getTxSalesDate().getTime()));   //converting util date to sql date         
            pst.setDouble(2, salesTx.getTxSalesAmount());
            pst.setInt(3, salesTx.getCustomer().getCustomer_id());            
            pst.setString(4, salesTx.getTxType()); 
            pst.setInt(5, salesTx.getSalesRep()); 
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId());  
            pst.setDate(8, new java.sql.Date(salesTx.getTxSalesDueDate().getTime()));   //converting util date to sql date   
            if(salesTx.isPaid())
            {
                pst.setInt(9, 1);                        
            }
            else
            {
                pst.setInt(9, 0); 
            }
            pst.execute();
             // Example of using Statement.getGeneratedKeys()
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
                
            int txid=getLastTransactionSummaryId();
            salesTx.setTx_summary_id(autoIncKeyFromApi);
            
            /*
             * Posting individual transaction items
             */
            for(SalesItem salesItem:salesItems)
            {
                preppedStmtInsert="INSERT INTO transactions (tx_summary_id,txdate,product_id,txqty,txamount,customer_id,discount,dateCreated,createdby,txtype) VALUES(?,?,?,?,?,?,?,?,?,?)";
                pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);
                
                salesItem.setTransaction_summary_id(txid);
                pst.setInt(1, salesItem.getTransaction_summary_id());
                pst.setDate(2, new java.sql.Date(salesItem.getSalesDate().getTime()));   //converting util date to sql date 
                pst.setInt(3, salesItem.getProduct().getProduct_id());
                pst.setInt(4, salesItem.getQuantity());
                pst.setDouble(5, salesItem.getAmount());
                pst.setInt(6, salesTx.getCustomer().getCustomer_id());            
                pst.setDouble(7, salesItem.getDiscount());                 
                pst.setTimestamp(8, Sql.getCurrentTimeStamp());
                pst.setInt(9, MainMenu.gUser.getUserId()); 
                pst.setInt(10, salesItem.getTxType()); 
                pst.execute();
                
                int autoTxId = -1; 
                ResultSet rs2 = pst.getGeneratedKeys(); 
                if (rs2.next()) 
                {
                    autoTxId = rs2.getInt(1);
                    //System.out.println("Key returned from getGeneratedKeys():" + autoIncKeyFromApi);
                } 
                else 
                {
                    // throw an exception from here
                }
                rs2.close(); 
                rs2 = null;
                //update the stock movement table
                preppedStmtInsert="INSERT INTO stockmovement (txdate,product_id,curqty,out_sales_id,dateCreated,createdby) VALUES(?,?,?,?,?,?)";
                PreparedStatement pst2=Sql.getConnection().prepareStatement(preppedStmtInsert);  
                
                int qty=getAvailableQuantity(salesItem.getProduct().getProduct_id());
                pst2.setDate(1, new java.sql.Date(salesItem.getSalesDate().getTime()));   //converting util date to sql date 
                pst2.setInt(2, salesItem.getProduct().getProduct_id());
                pst2.setInt(3, qty);                //current qty
                pst2.setInt(4, autoTxId);                //quantiy purchased foreig key to purchases
                pst2.setTimestamp(5, Sql.getCurrentTimeStamp());
                pst2.setInt(6, MainMenu.gUser.getUserId());                
                pst2.execute();
                
                //update stock table
                //create a prepared statement
                preppedStmtUpdate="update stock set quantity=?, lastchangetype=?, datechanged=?, changedby=? where product_id=? ";
                PreparedStatement pst3= Sql.getConnection().prepareStatement(preppedStmtUpdate);
                    
                pst3.setInt(1, qty - salesItem.getQuantity()); //current + purchases
                pst3.setString(2, "CR"); 
                pst3.setTimestamp(3, Sql.getCurrentTimeStamp());
                pst3.setInt(4, MainMenu.gUser.getUserId()); 
                pst3.setInt(5, salesItem.getProduct().getProduct_id());
                pst3.executeUpdate();
            }
              
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Transaction successfully Posted...");
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
    
    public void postTransaction(SalesTransaction salesTx, List<SalesItem> salesItems, Cash cashTx) throws SQLException
    {
        try
        {
           
            //Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Instert the transaction summary then followed by individual transaction items
             */
            
            preppedStmtInsert="INSERT INTO transactionsummary (txdate,txamount,customer_id,txtype,salesrep_id,txduedate,dateCreated,createdby,paid) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);            
            
            pst.setDate(1, new java.sql.Date(salesTx.getTxSalesDate().getTime()));   //converting util date to sql date         
            pst.setDouble(2, salesTx.getTxSalesAmount());
            pst.setInt(3, salesTx.getCustomer().getCustomer_id());            
            pst.setString(4, salesTx.getTxType()); 
            pst.setInt(5, salesTx.getSalesRep()); 
            pst.setDate(6, new java.sql.Date(salesTx.getTxSalesDueDate().getTime()));   //converting util date to sql date   
            pst.setTimestamp(7, Sql.getCurrentTimeStamp());
            pst.setInt(8, MainMenu.gUser.getUserId());   
            if(salesTx.isPaid())
            {
                pst.setInt(9, 1);                        
            }
            else
            {
                pst.setInt(9, 0); 
            }
            pst.execute();
            // Example of using Statement.getGeneratedKeys()
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
            int txid= autoIncKeyFromApi;
            salesTx.setTx_summary_id(autoIncKeyFromApi);
            
            /*
             * Posting individual transaction items
             */
            for(SalesItem salesItem:salesItems)
            {
                preppedStmtInsert="INSERT INTO transactions (tx_summary_id,txdate,product_id,txqty,txamount,customer_id,discount,dateCreated,createdby,txtype) VALUES(?,?,?,?,?,?,?,?,?,?)";
                pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);
                
                salesItem.setTransaction_summary_id(autoIncKeyFromApi);
                pst.setInt(1, salesItem.getTransaction_summary_id());
                pst.setDate(2, new java.sql.Date(salesItem.getSalesDate().getTime()));   //converting util date to sql date 
                pst.setInt(3, salesItem.getProduct().getProduct_id());
                pst.setInt(4, salesItem.getQuantity());
                pst.setDouble(5, salesItem.getAmount());
                pst.setInt(6, salesTx.getCustomer().getCustomer_id());            
                pst.setDouble(7, salesItem.getDiscount());                 
                pst.setTimestamp(8, Sql.getCurrentTimeStamp());
                pst.setInt(9, MainMenu.gUser.getUserId());     
                pst.setInt(10, salesItem.getTxType()); 
                pst.execute();
                
                int autoTxId = -1; 
                ResultSet rs2 = pst.getGeneratedKeys(); 
                if (rs2.next()) 
                {
                    autoTxId = rs2.getInt(1);
                    System.out.println("Key returned from getGeneratedKeys():" + autoTxId);
                } 
                else 
                {
                    // throw an exception from here
                }
                rs2.close(); 
                rs2 = null;
                //update the stock movement table
                preppedStmtInsert="INSERT INTO stockmovement (txdate,product_id,curqty,out_sales_id,dateCreated,createdby) VALUES(?,?,?,?,?,?)";
                pst=Sql.getConnection().prepareStatement(preppedStmtInsert);  
                
                int qty=getAvailableQuantity(salesItem.getProduct().getProduct_id());
                pst.setDate(1, new java.sql.Date(salesItem.getSalesDate().getTime()));   //converting util date to sql date 
                pst.setInt(2, salesItem.getProduct().getProduct_id());
                pst.setInt(3, qty);                //current qty
                pst.setInt(4, autoTxId);                //quantiy purchased foreig key to purchases
                pst.setTimestamp(5, Sql.getCurrentTimeStamp());
                pst.setInt(6, MainMenu.gUser.getUserId());                
                pst.execute();
                
                //update stock table
                //create a prepared statement
                preppedStmtUpdate="update stock set quantity=?, lastchangetype=?, datechanged=?, changedby=? where product_id=? ";
                pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
                    
                pst.setInt(1, qty - salesItem.getQuantity()); //current + purchases
                pst.setString(2, "CR"); 
                pst.setTimestamp(3, Sql.getCurrentTimeStamp());
                pst.setInt(4, MainMenu.gUser.getUserId()); 
                pst.setInt(5, salesItem.getProduct().getProduct_id());
                pst.executeUpdate();
            }
             
            /*
             * Insert Cash Transaction
             */
            
                       
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code, amount,customer_id,tx_type,account,dateCreated,createdby,sales_tx_id) VALUES(?,?,?,?,?,?,?,?,?)";
            pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            
            pst.setDate(1, new java.sql.Date(cashTx.getDate().getTime()));   //converting util date to sql date   
            pst.setInt(2, cashTx.getTxCode());   
            pst.setDouble(3, cashTx.getAmount());
            pst.setInt(4, cashTx.getCustomer().getCustomer_id());            
            pst.setString(5, cashTx.getTxType()); 
            pst.setString(6, cashTx.getAccount());            
            pst.setTimestamp(7, Sql.getCurrentTimeStamp());
            pst.setInt(8, MainMenu.gUser.getUserId()); 
            pst.setInt(9, txid);
            pst.execute(); 
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Transaction successfully Posted...");
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
    
    public void postPaymentReceipt(List<Cash> cashTx,List<SalesTransaction> tx) throws SQLException
    {
        try
        {
           
            //Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;            
            
            /*
             * Insert Cash Payments for various invoices
             */
            for(Cash cash:cashTx)
            {
                preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code, amount,customer_id,tx_type,account,dateCreated,createdby,sales_tx_id,prepayment,chequeno) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);               
                pst.setDate(1, new java.sql.Date(cash.getDate().getTime()));   //converting util date to sql date   
                pst.setInt(2, cash.getTxCode());   
                pst.setDouble(3, cash.getAmount());
                pst.setInt(4, cash.getCustomer().getCustomer_id());            
                pst.setString(5, cash.getTxType()); 
                pst.setString(6, cash.getAccount());            
                pst.setTimestamp(7, Sql.getCurrentTimeStamp());
                pst.setInt(8, MainMenu.gUser.getUserId()); 
                if(cash.getSalesTransaction()!=null)
                {
                    pst.setInt(9, cash.getSalesTransaction().getTx_summary_id());
                }
                else
                {
                    pst.setInt(9, 0);
                }
                
                pst.setDouble(10, cash.getPrepayment());
                pst.setString(11, cash.getChequeNumber());     
                pst.execute(); 
            
            }
             
             if(!tx.isEmpty())
             {

                 for(int i=0;i<tx.size();i++)                 {
                    
                    //create a prepared statement
                    preppedStmtUpdate="update transactionsummary set paid=?,dateChanged=?, changedBy=? WHERE tx_summary_id=?";
                    PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
                    //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
                    //using Transactions to ensure successfull update
            
                    if(tx.get(i).isPaid())
                    {
                         pst.setInt(1, 1);                        
                    }
                    else
                    {
                        pst.setInt(1, 0); 
                    }
                     pst.setTimestamp(2, Sql.getCurrentTimeStamp()); 
                     pst.setInt(3, MainMenu.gUser.getUserId());
                     pst.setInt(4, tx.get(i).getTx_summary_id());
                     pst.executeUpdate(); 
                 }
             }              
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Payment/Cash Receipt successfully Posted...");
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
    
    public void postPrePaymentAllocation(List<Prepayment> ppAllot) throws SQLException
    {
        try
        {
           
            //Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;            
            
            /*
             * Insert PrePayments Allocations for various invoices
             */
            for(Prepayment prepayCash:ppAllot)
            {
                preppedStmtInsert="INSERT INTO prepayallocation (dateallocated,amount,customer_id,dateCreated,createdby,sales_tx_id) VALUES(?,?,?,?,?,?)";
                PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);               
                pst.setDate(1, new java.sql.Date(prepayCash.getDate().getTime()));   //converting util date to sql date   
                pst.setDouble(2, prepayCash.getAmountAllocated());
                pst.setInt(3, prepayCash.getCustomer().getCustomer_id());                           
                pst.setTimestamp(4, Sql.getCurrentTimeStamp());
                pst.setInt(5, MainMenu.gUser.getUserId()); 
                pst.setInt(6, prepayCash.getSalesTransaction().getTx_summary_id());                
                pst.execute();             
            }
             
                           
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "PrePayment Allocation successfully Posted...");
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
    
    public void postCashSales(Cash salesTx) throws SQLException
    {
        try
        {
           
            Sql.getConnection().setAutoCommit(false) ;
            /*
             * Insert Cash Transaction
             */
            
                       
            preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code, amount,customer_id,tx_type,account,dateCreated,createdby) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            
            pst.setDate(1, new java.sql.Date(salesTx.getDate().getTime()));   //converting util date to sql date   
            pst.setInt(2, salesTx.getTxCode());   
            pst.setDouble(3, salesTx.getAmount());
            pst.setInt(4, salesTx.getCustomer().getCustomer_id());            
            pst.setString(5, salesTx.getTxType()); 
            pst.setString(6, salesTx.getAccount());            
            pst.setTimestamp(7, Sql.getCurrentTimeStamp());
            pst.setInt(8, MainMenu.gUser.getUserId());   
            pst.execute();                      
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Transaction successfully Posted...");
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
    
    public int getLastTransactionSummaryId() 
    {
        int tx_id=0;
        try
        {
            //log info
            AccountsManagement.logger.info("Validating Product Code... ");
            String sqlStmt="Select max(tx_summary_id) as last_id from transactionsummary ";
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                tx_id=rs.getInt("last_id");
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
//            Sql.Close();
//        }
        
        
        return tx_id;
    }
    
    public List<SalesTransaction> getTransactionsByCustomerId(int id)
    {
        List<SalesTransaction> txList=new ArrayList<SalesTransaction>();
        SalesTransaction salesTx;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select tx_summary_id,txdate,txamount, customer_id,txduedate,if(txtype='Credit','Invoice','Cash Sale') as txtype,";
            sqlStmt+="if(paid=0,false,true) as paid "; 
            sqlStmt+="from transactionsummary WHERE voided=0 and customer_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                salesTx=new SalesTransaction();
                salesTx.setTx_summary_id(rs.getInt("tx_summary_id"));
                salesTx.setTxSalesAmount(rs.getDouble("txamount"));
                salesTx.setTxSalesDate(rs.getDate("txdate"));
                salesTx.setTxSalesDueDate(rs.getDate("txduedate"));
                salesTx.setTxType(rs.getString("txtype"));
                salesTx.setPaid(rs.getBoolean("paid"));
                txList.add(salesTx);                
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
        
        return txList;
    }
    
    /*
     * Get Transactions by dates
     */
    public List<SalesTransaction> getTransactionsByDates(Customer customer, Date startDate, Date endDate)
    {
        List<SalesTransaction> txList=new ArrayList<SalesTransaction>();
        SalesTransaction salesTx;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select tx_summary_id,txdate,txamount, customer_id,txduedate,if(txtype='Credit','Invoice','Cash Sale') as txtype,";
            sqlStmt+="if(paid=0,false,true) as paid "; 
            sqlStmt+=" from transactionsummary WHERE voided=0 and customer_id =" + customer.getCustomer_id() ;
            sqlStmt+=" and txdate between '" + new java.sql.Date(startDate.getTime()) + "' and '" + new java.sql.Date(endDate.getTime())+"'";
            
            System.out.println("SQL statment: "+sqlStmt);
            System.out.println("SQL Start Date: "+new java.sql.Date(startDate.getTime())+"\t SQL End Date: "+new java.sql.Date(endDate.getTime()));
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                salesTx=new SalesTransaction();
                salesTx.setTx_summary_id(rs.getInt("tx_summary_id"));
                salesTx.setTxSalesAmount(rs.getDouble("txamount"));
                salesTx.setTxSalesDate(rs.getDate("txdate"));
                salesTx.setTxSalesDueDate(rs.getDate("txduedate"));
                salesTx.setTxType(rs.getString("txtype"));
                salesTx.setPaid(rs.getBoolean("paid"));
                txList.add(salesTx);                
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
        
        return txList;
    }
    
    /*
     * Get Customer's Last Payment
     */
    public double getLastCustomerPayment(Customer customer)
    {
        double amount=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt=" select left(max(concat(tx_date,amount)),19) as txdate, mid(max(concat(tx_date,amount)),20) as amount ";
            sqlStmt+=" from( "; 
            sqlStmt+=" select tx_date,sum(amount)as amount ";
            sqlStmt+=" from cash_bank ";
            sqlStmt+=" where voided=0 and tx_type='DR' and customer_id =" + customer.getCustomer_id() + "";
            sqlStmt+=" group by date(tx_date))x; ";           

            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                 amount=rs.getDouble("amount") ;         
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
        
        return amount;
    }
    public List<SalesTransaction> getPendingInvoicesByCustomerId(int id)
    {
        List<SalesTransaction> txList=new ArrayList<SalesTransaction>();
        SalesTransaction salesTx;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select tx_summary_id,txdate,txamount, customer_id,txduedate,if(txtype='Credit','Invoice','Cash Sale') as txtype,";
            sqlStmt+="if(paid=0,false,true) as paid "; 
            sqlStmt+="from transactionsummary WHERE paid=0 and txtype not in ('Cash') and voided=0 and customer_id =" + id + "";
            sqlStmt+=" order by txduedate asc";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                salesTx=new SalesTransaction();
                salesTx.setTx_summary_id(rs.getInt("tx_summary_id"));
                salesTx.setTxSalesAmount(rs.getDouble("txamount"));
                salesTx.setTxSalesDate(rs.getDate("txdate"));
                salesTx.setTxSalesDueDate(rs.getDate("txduedate"));
                salesTx.setTxType(rs.getString("txtype"));
                salesTx.setPaid(rs.getBoolean("paid"));
                //salesTx.setBalance(getTransactionBalance(salesTx));
                txList.add(salesTx);                
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
        
        return txList;
    }
    
    public double getTotalSalesByCustomerId(int id)
    {
        double total=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("getTotalSalesByCustomerId given DB id... ");
            String sqlStmt="SELECT customer_id,sum(txamount) as total FROM transactionsummary t "; 
            sqlStmt+="where voided=0 and customer_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                total=rs.getDouble("total");                              
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
        return total;
    }
    
    public double getTotalCashByCustomerId(int id)
    {
        double total=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("getTotalSalesByCustomerId given DB id... ");
            String sqlStmt="SELECT customer_id,sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where voided=0 and tx_type='DR' and customer_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                total=rs.getDouble("total");                              
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
        return total;
    }
    
    public double getTotalPrepaymentByCustomerId(int id)
    {
        double totalPrepay=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("getTotalSalesByCustomerId given DB id... ");
            String sqlStmt="SELECT customer_id,sum(amount) as total FROM prepayment p "; 
            sqlStmt+="where voided=0 and allocated=0 and customer_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                totalPrepay=rs.getDouble("total");                              
            }
            
            double allocated=0.0;
            sqlStmt="SELECT customer_id,sum(amount) as total FROM prepayallocation p "; 
            sqlStmt+="where voided=0  and customer_id =" + id + "";
           
            rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                allocated=rs.getDouble("total");                              
            }
            totalPrepay=totalPrepay-allocated;
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        return totalPrepay;
    }
    
    public double getTransactionBalance(SalesTransaction tx)
    {
        double txtotalPayment=0.0;
        double txbalance=0.0;
        try
        {
            //log info
            /*
             * To calculate any outstanding balance for a sales transaction. We take all the possible payments (cash/bank/mobile+prepayment if any)
             * that have been made for that tx
             * NB a tx can be paid by several cash/bank/mobile installments
             * The balance is calculated by getting total amount for a transaction - the payments
             */
            AccountsManagement.logger.info("getTotalSalesByCustomerId given DB id... ");
            String sqlStmt="SELECT sales_tx_id,customer_id,sum(amount) as cashamount,sum(prepayment) as preamount FROM cash_bank t "; 
            sqlStmt+="where voided=0 and sales_tx_id=" + tx.getTx_summary_id() + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                txtotalPayment=rs.getDouble("cashamount")+rs.getDouble("preamount");                              
                txbalance=tx.getTxSalesAmount()-txtotalPayment;
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
        return txbalance;
    }
    
     /**
     * Update Payment Status
     * @param List<SalesTransaction> 
     */
    public void updateTransaction(List<SalesTransaction> tx) throws SQLException 
     {
          
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Trasaction Payment Status... ");
             
             if(!tx.isEmpty())
             {
                 Sql.getConnection().setAutoCommit(false) ;
                 for(int i=0;i<tx.size();i++)
                 {
                    
                    preppedStmtUpdate="update transactionsummary set paid=?,dateChanged=?, changedBy=? WHERE tx_summary_id=?";
                    
                    PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
                    //create a prepared statement
                    //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
                    //using Transactions to ensure successfull update
            
                    if(tx.get(i).isPaid())
                    {
                         pst.setInt(1, 1);                        
                    }
                    else
                    {
                        pst.setInt(1, 0); 
                    }
                     pst.setTimestamp(2, Sql.getCurrentTimeStamp()); 
                     pst.setInt(3, MainMenu.gUser.getUserId());
                     pst.setInt(4, tx.get(i).getTx_summary_id());
                     pst.executeUpdate(); 
                 }
                 Sql.getConnection().commit();
                 //commit transaction if successful
                JOptionPane.showMessageDialog(null, "Record Updated...");
             }
             
             
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
    
    public int getAvailableQuantity(int id)
    {
        int qty=0;      
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Product Quantity given the internal DB id... ");
            String sqlStmt="Select product_id,quantity as qty from stock WHERE voided=0 and product_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               qty=rs.getInt("qty");               
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
//          Sql.Close();//close open connection 
//        }
	
         
        return qty;
    }

}
