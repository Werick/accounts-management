/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Cash;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.Prepayment;
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.Purchase;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class PurchasesService {
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    
    public PurchasesService()
    {
        
    }
    public double getLastPaymentMade(Supplier supplier)
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
            sqlStmt+=" where voided=0 and tx_type='CR' and supplier_id =" + supplier.getSupplier_id() + "";
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
    
    public void postSupplierPayment(List<Cash> cashTx,List<Purchase> tx) throws SQLException
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
                preppedStmtInsert="INSERT INTO cash_bank (tx_date,tx_code, amount,supplier_id,tx_type,account,dateCreated,createdby,account_tx_id,prepayment,invoicenum) ";
                preppedStmtInsert+=" VALUES(?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);               
                pst.setDate(1, new java.sql.Date(cash.getDate().getTime()));   //converting util date to sql date   
                pst.setInt(2, cash.getTxCode());   
                pst.setDouble(3, cash.getAmount());
                pst.setInt(4, cash.getPurchase().getSupplier().getSupplier_id());            
                pst.setString(5, cash.getTxType()); 
                pst.setString(6, cash.getAccount());            
                pst.setTimestamp(7, Sql.getCurrentTimeStamp());
                pst.setInt(8, MainMenu.gUser.getUserId()); 
                if(cash.getPurchase()!=null)
                {
                    pst.setInt(9, cash.getPurchase().getAccount().getAccount_id());                       
                }
                else
                {
                    pst.setInt(9, 0);                    
                }
                
                pst.setDouble(10, cash.getPrepayment());
                if(cash.getPurchase()!=null)
                {
                    pst.setString(11, cash.getPurchase().getInvoiceNumber());    
                }
                else
                {
                   pst.setString(11, null);
                }
               
                pst.execute(); 
            
            }
             
             if(!tx.isEmpty())
             {

                 for(int i=0;i<tx.size();i++)                 {
                    
                    //create a prepared statement
                    preppedStmtUpdate="update purchases set paid=?,dateChanged=?, changedBy=? WHERE invoicenum=?";
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
                     pst.setString(4, tx.get(i).getInvoiceNumber());
                     pst.executeUpdate(); 
                 }
             }              
            
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Supplier Payment successfully Posted...");
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
             * Insert the transaction summary then followed by individual transaction items
             */
            
            preppedStmtInsert="insert into prepayment (supplier_id,amount,prepaydate,datecreated,createdby) VALUES(?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            
            pst.setInt(1, prepayment.getPurchase().getSupplier().getSupplier_id());             
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
    
    public double getTotalPrepaymentBySupplierId(int id)
    {
        //prepayments made to any supplier given the supplier Id
        double totalPrepay=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("getTotalSalesByCustomerId given DB id... ");
            String sqlStmt="SELECT supplier_id,sum(amount) as total FROM prepayment p "; 
            sqlStmt+="where voided=0 and allocated=0 and supplier_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {        
                totalPrepay=rs.getDouble("total");                              
            }
            
            double allocated=0.0;
            sqlStmt="SELECT supplier_id,sum(amount) as total FROM prepayallocation p "; 
            sqlStmt+="where voided=0  and supplier_id =" + id + "";
           
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
    
    
    public void postPrePaymentAllocation(List<Prepayment> ppAllot) throws SQLException
    {
        //prepayments made to suppliers
        try
        {
           
            //Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;            
            
            /*
             * Insert PrePayments Allocations for various invoices to paid to a supplier
             */
            for(Prepayment prepayCash:ppAllot)
            {
                preppedStmtInsert="INSERT INTO prepayallocation (dateallocated,amount,supplier_id,dateCreated,createdby,invoicenum) VALUES(?,?,?,?,?,?)";
                PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);               
                pst.setDate(1, new java.sql.Date(prepayCash.getDate().getTime()));   //converting util date to sql date   
                pst.setDouble(2, prepayCash.getAmountAllocated());
                pst.setInt(3, prepayCash.getPurchase().getSupplier().getSupplier_id());                           
                pst.setTimestamp(4, Sql.getCurrentTimeStamp());
                pst.setInt(5, MainMenu.gUser.getUserId()); 
                pst.setString(6, prepayCash.getPurchase().getInvoiceNumber());                
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
    
    
    public void postPurchases(List<Purchase> purchaseItems) throws SQLException
    {
        try
        {           
            //Sql.Open();
            Sql.getConnection().setAutoCommit(false) ;            
            /*
             * Posting individual transaction items
             */
            for(Purchase purchaseItem:purchaseItems)
            {
                preppedStmtInsert="INSERT INTO purchases (pdate,product_id,supplier_id,quantity,amount,vat,netamount,invoicenum,dateCreated,createdby,duedate,unitprice) ";
                preppedStmtInsert+=" VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);                
                
                pst.setDate(1, new java.sql.Date(purchaseItem.getDate().getTime()));   //converting util date to sql date 
                pst.setInt(2, purchaseItem.getProduct().getProduct_id());                
                pst.setInt(3, purchaseItem.getSupplier().getSupplier_id());
                pst.setInt(4, purchaseItem.getQty());
                pst.setDouble(5, purchaseItem.getAmount());
                pst.setDouble(6, purchaseItem.getVat());
                pst.setDouble(7, purchaseItem.getNetAmount());
                pst.setString(8, purchaseItem.getInvoiceNumber());                               
                pst.setTimestamp(9, Sql.getCurrentTimeStamp());
                pst.setInt(10, MainMenu.gUser.getUserId());
                pst.setDate(11, new java.sql.Date(purchaseItem.getDueDate().getTime()));   //converting util date to sql date 
                pst.setDouble(12, purchaseItem.getUnitPrice());
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
                
                //update the stock movement table
                preppedStmtInsert="INSERT INTO stockmovement (txdate,product_id,curqty,in_purchases_id,dateCreated,createdby) VALUES(?,?,?,?,?,?)";
                pst=Sql.getConnection().prepareStatement(preppedStmtInsert);  
                
                int qty=getAvailableQuantity(purchaseItem.getProduct().getProduct_id());
                pst.setDate(1, new java.sql.Date(purchaseItem.getDate().getTime()));   //converting util date to sql date 
                pst.setInt(2, purchaseItem.getProduct().getProduct_id());
                pst.setInt(3, qty);                //current qty
                pst.setInt(4, autoIncKeyFromApi);                //quantiy purchased foreig key to purchases
                pst.setTimestamp(5, Sql.getCurrentTimeStamp());
                pst.setInt(6, MainMenu.gUser.getUserId());                
                pst.execute();
                
                //update stock table
                //create a prepared statement
                preppedStmtUpdate="update stock set quantity=?, lastchangetype=?, datechanged=?, changedby=? where product_id=? ";
                pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
                    
                pst.setInt(1, qty+ purchaseItem.getQty()); //current + purchases
                pst.setString(2, "DR"); 
                pst.setTimestamp(3, Sql.getCurrentTimeStamp());
                pst.setInt(4, MainMenu.gUser.getUserId()); 
                pst.setInt(5, purchaseItem.getProduct().getProduct_id());
                pst.executeUpdate();
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

public boolean hasVAT(int id)
    {
        boolean vat=false;      
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Product Quantity given the internal DB id... ");
            String sqlStmt="Select product_id,quantity as qty,if(vat=1,true,false) as vat from stock WHERE voided=0 and product_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               vat=rs.getBoolean("vat");               
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
	
         
        return vat;
    }
public List<Purchase> getPurchasesBySupplierId(int id)
    {
        List<Purchase> txList=new ArrayList<Purchase>();
        Purchase purchasesTx;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select purchase_id,pdate,netamount, vat, amount, product_id,duedate, ";
            sqlStmt+="if(paid=0,false,true) as paid, invoicenum, supplier_id "; 
            sqlStmt+="from purchases WHERE voided=0 and supplier_id =" + id + " order by pdate";
            
            System.out.println("Debugging: "+sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                purchasesTx=new Purchase();
                Supplier supplier=new Supplier();
                supplier.setSupplier_id(rs.getInt("supplier_id"));
                purchasesTx.setInvoiceNumber(rs.getString("invoicenum"));
                purchasesTx.setAmount(rs.getDouble("amount"));
                purchasesTx.setDate(rs.getDate("pdate"));
                purchasesTx.setDueDate(rs.getDate("duedate"));               
                purchasesTx.setPaid(rs.getBoolean("paid"));
                purchasesTx.setSupplier(supplier);
                txList.add(purchasesTx);                
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

public List<Purchase> getPurchasesBySupplierId(int id, String invoicenum)
    {
        List<Purchase> txList=new ArrayList<Purchase>();
        Purchase purchasesTx;
        try
        {
            //log info
            ProductService ps=new ProductService();
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select purchase_id,quantity,pdate,netamount, vat, amount, unitprice,product_id,duedate, ";
            sqlStmt+="if(paid=0,false,true) as paid, invoicenum, supplier_id "; 
            sqlStmt+="from purchases WHERE voided=0 and supplier_id =" + id + " and invoicenum='"+invoicenum+"' order by pdate";
            
            System.out.println("Debugging: "+sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                purchasesTx=new Purchase();
                Supplier supplier=new Supplier();
                Product product=new Product();
                
                product.setProduct_id(rs.getInt("product_id"));                
                supplier.setSupplier_id(rs.getInt("supplier_id"));
                purchasesTx.setInvoiceNumber(rs.getString("invoicenum"));
                purchasesTx.setProduct(product);
                purchasesTx.setAmount(rs.getDouble("amount"));
                purchasesTx.setVat(rs.getDouble("vat"));
                purchasesTx.setNetAmount(rs.getDouble("netamount"));
                purchasesTx.setDate(rs.getDate("pdate"));
                purchasesTx.setDueDate(rs.getDate("duedate"));               
                purchasesTx.setPaid(rs.getBoolean("paid"));
                purchasesTx.setSupplier(supplier);
                purchasesTx.setUnitPrice(rs.getDouble("unitprice"));
                purchasesTx.setQty(rs.getInt("quantity"));
                txList.add(purchasesTx);            
                
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


public List<Purchase> getPurchasesByDates(Supplier supplier,Date startDate, Date endDate)
    {
        List<Purchase> txList=new ArrayList<Purchase>();
        Purchase purchasesTx;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select purchase_id,pdate,netamount, vat, amount, product_id,duedate, ";
            sqlStmt+=" if(paid=0,false,true) as paid, invoicenum, supplier_id "; 
            sqlStmt+=" from purchases WHERE voided=0 and supplier_id =" + supplier.getSupplier_id();
            sqlStmt+=" and pdate between '" + new java.sql.Date(startDate.getTime()) + "' and '" + new java.sql.Date(endDate.getTime())+"'";
            sqlStmt+=" group by invoicenum, pdate order by duedate ";
            System.out.println("Debugging: "+sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                purchasesTx=new Purchase();                
                purchasesTx.setInvoiceNumber(rs.getString("invoicenum"));
                purchasesTx.setAmount(rs.getDouble("amount"));
                purchasesTx.setDate(rs.getDate("pdate"));
                purchasesTx.setDueDate(rs.getDate("duedate"));               
                purchasesTx.setPaid(rs.getBoolean("paid"));
                purchasesTx.setSupplier(supplier);
                txList.add(purchasesTx);                
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

public double getInvoiceBalance(Purchase tx)
    {
        double txtotalPayment=0.0;
        double txbalance=0.0;
        try
        {
            //log info
            /*
             * To calculate any outstanding balance for a given invoice transaction. We take all the possible payments (cash/bank/mobile+prepayment if any)
             * that have been made for that tx
             * NB a tx can be paid by several cash/bank/mobile installments
             * The balance is calculated by getting total amount for a transaction - the payments
             */
            AccountsManagement.logger.info("get Invoice Balance... ");
            String sqlStmt="SELECT invoicenum,supplier_id,sum(amount) as amount,sum(prepayment) as preamount FROM cash_bank t "; 
            sqlStmt+="where voided=0 and invoicenum='" + tx.getInvoiceNumber() + "'";
           
            ResultSet rs2=Sql.executeQuery(sqlStmt);
            while (rs2.next())
            {        
                txtotalPayment=rs2.getDouble("amount")+rs2.getDouble("preamount");                              
                txbalance=tx.getAmount()-txtotalPayment;
            }
            rs2.close();
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
    

public List<Purchase> getInvoiceSummaryBySupplierId(int id)
    {
        List<Purchase> txList=new ArrayList<Purchase>();
        Purchase purchasesTx;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="SELECT pdate,duedate,supplier_id,sum(netamount)as netamount,sum(vat) as vat, sum(amount)as amount, "; 
            sqlStmt+=" if(paid=0,false,true) as paid, invoicenum, supplier_id "; 
            sqlStmt+=" from purchases WHERE paid=0 and voided=0 and supplier_id =" + id + "";
            sqlStmt+=" group by invoicenum, pdate order by duedate ";
            
            System.out.println("Debugging: "+sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                purchasesTx=new Purchase();
                Supplier supplier=new Supplier();
                supplier.setSupplier_id(rs.getInt("supplier_id"));
                purchasesTx.setInvoiceNumber(rs.getString("invoicenum"));
                purchasesTx.setAmount(rs.getDouble("amount"));
                purchasesTx.setDate(rs.getDate("pdate"));
                purchasesTx.setDueDate(rs.getDate("duedate"));               
                purchasesTx.setPaid(rs.getBoolean("paid"));
                purchasesTx.setVat(rs.getDouble("vat"));
                purchasesTx.setSupplier(supplier);
//                double bal=getInvoiceBalance(purchasesTx);
//                purchasesTx.setBalance(bal);
                txList.add(purchasesTx);                
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
    
    public double getTotalPurchasesBySupplierId(int id)
    {
        double total=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("getTotalPurchasesBySupplierId given DB id... ");
            String sqlStmt="SELECT supplier_id,sum(amount) as total FROM purchases "; 
            sqlStmt+="where voided=0 and supplier_id =" + id + "";
           
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
    
    public double getTotalPaymentsBySupplierId(int id)
    {
        double total=0.0;
        try
        {
            //log info
            AccountsManagement.logger.info("getTotalPaymentsBySupplierId given DB id... ");
            String sqlStmt="SELECT supplier_id,sum(amount) as total FROM cash_bank t "; 
            sqlStmt+="where tx_type='CR' and voided=0 and supplier_id =" + id + "";
           
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
}
