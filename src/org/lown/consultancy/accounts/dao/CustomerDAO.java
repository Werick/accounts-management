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
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class CustomerDAO {
    
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    //private Sql Sql;
    public CustomerDAO()
    {
        //Sql=new Sql();
    }
    public void saveCustomer(Customer customer) throws SQLException
    {      
        
        try
        {
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO customer (customernumber,customername,phone,address,contactperson,dateCreated,createdby) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);
            pst.setString(1, customer.getCustomerNumber());
            pst.setString(2, customer.getCustomerName());
            pst.setString(3, customer.getPhone());
            pst.setString(4, customer.getAddress());
            pst.setString(5, customer.getContactPerson());
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId());   
            pst.execute();
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Customer Record Added...");
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
    
    public Customer getCustomerById(int id)
    {
        Customer c=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Customer Details given the internal DB id... ");
            String sqlStmt="Select customer_id,customername,customernumber,phone,address, contactperson ";
            sqlStmt+=" from customer WHERE customer_id =" + id + "";
            sqlStmt+=" order by customernumber asc";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                c=new Customer();
                c.setCustomerName(rs.getString("customername"));  
                c.setCustomerNumber(rs.getString("customernumber"));
                c.setContactPerson(rs.getString("contactperson"));
                c.setAddress(rs.getString("address"));
                c.setPhone(rs.getString("phone"));               
                c.setCustomer_id(rs.getInt("customer_id"));
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
	
         
        return c;
    }
    public Customer getCustomerByNumber(String id)
    {
        Customer c=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Customer Details given the internal DB id... ");
            String sqlStmt="Select customer_id,customername,customernumber,phone,address, contactperson ";
            sqlStmt+=" from customer WHERE customernumber ='" + id + "'";
            sqlStmt+=" order by customernumber asc";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                c=new Customer();
                c.setCustomerName(rs.getString("customername"));  
                c.setCustomerNumber(rs.getString("customernumber"));
                c.setContactPerson(rs.getString("contactperson"));
                c.setAddress(rs.getString("address"));
                c.setPhone(rs.getString("phone"));               
                c.setCustomer_id(rs.getInt("customer_id"));
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
//            Sql.Close();//close open connection
//        }
	
         
        return c;
    }
    
    public List<Customer> getCustomerByName(String search)
    {
        List<Customer> custList=new ArrayList<Customer>();
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of Customers given the search criteria... ");
            String sqlStmt="Select customer_id,customername,customernumber,phone,address, contactperson from customer WHERE voided=0 and (customernumber like '%" + search + "%' or "; //
            sqlStmt=sqlStmt+" customername like '%" + search + "%' or phone like '%" + search + "%' or address like '%" + search + "%'  or contactperson like '%" + search + "%') order by customernumber asc;";
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Customer c = new Customer();
                c.setCustomerNumber(rs.getString("customernumber"));  
                c.setCustomerName(rs.getString("customername"));
                c.setAddress(rs.getString("address"));
                c.setPhone(rs.getString("phone"));
                c.setContactPerson(rs.getString("contactperson"));                
                c.setCustomer_id(rs.getInt("customer_id"));              
                custList.add(c);
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
//            Sql.Close();//close open connection
//        }
	 
        return custList;
    }
    
    /**
     * Check if Identifier already exists
     * @param identifier
     * @return identifier count
     */
    public int checkDuplicateCustomerNumber(String identifier) 
    {
        int p=0;
        try
        {
            //log info
            AccountsManagement.logger.info("Validating Customer number... ");
            String sqlStmt="Select count(customer_id) as idcount from customer WHERE customernumber ='" + identifier + "'";
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                p=rs.getInt("idcount");
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
//        finally
//        {
//            Sql.Close();
//        }
        
        return p;
    }
    
    /**
     * Update Participant details
     * @param Customer 
     */
    public void updateCustomer(Customer c) throws SQLException 
     {
          
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Customer Details... ");
             //create a prepared statement
             preppedStmtUpdate="update customer set customername=?,customernumber=?,phone=?,address=?,contactperson=?,dateChanged=?, changedBy=? WHERE customer_id=?";
            //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
            //using Transactions to ensure successfull update
            Sql.getConnection().setAutoCommit(false) ;
            PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
            pst.setString(1, c.getCustomerName());
            pst.setString(2, c.getCustomerNumber());
            pst.setString(3, c.getPhone());
            pst.setString(4, c.getAddress());
            pst.setString(5, c.getContactPerson());            
            pst.setTimestamp(6, Sql.getCurrentTimeStamp()); 
            pst.setInt(7, MainMenu.gUser.getUserId());
            pst.setInt(8, c.getCustomer_id());
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
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.toString());
            Sql.getConnection().rollback();
         }
//         finally
//         {
//             Sql.Close();
//         }
    } 
}
