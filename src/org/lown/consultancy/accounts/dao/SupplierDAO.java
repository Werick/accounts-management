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
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class SupplierDAO {
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    
    public SupplierDAO()
    {
        
    }
    
    
    
    public void saveSupplier(Supplier sp) throws SQLException
    {      
        
        try
        {
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO supplier (number,sname,phone,address,contactperson,dateCreated,createdby,pin) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);
            pst.setString(1, sp.getSupplierNumber());
            pst.setString(2, sp.getSupplierName());
            pst.setString(3, sp.getPhone());
            pst.setString(4, sp.getAddress());
            pst.setString(5, sp.getContactPerson());
            pst.setTimestamp(6, Sql.getCurrentTimeStamp());
            pst.setInt(7, MainMenu.gUser.getUserId());   
            pst.setString(8, sp.getPhone());
            pst.execute();
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Supplier Record Added...");
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
    
    public Supplier getSupplierById(int id)
    {
        Supplier sp=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Supplier Details given the internal DB id... ");
            String sqlStmt="Select supplier_id,sname,number,phone,address, contactperson, pin ";
            sqlStmt+=" from supplier WHERE supplier_id =" + id + "";
            sqlStmt+=" order by number asc";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {                
                sp=new Supplier();
                sp.setSupplierName(rs.getString("sname"));  
                sp.setSupplierNumber(rs.getString("number"));
                sp.setContactPerson(rs.getString("contactperson"));
                sp.setAddress(rs.getString("address"));
                sp.setPhone(rs.getString("phone"));               
                sp.setSupplier_id(rs.getInt("supplier_id"));
                sp.setPin(rs.getString("pin"));
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
	
         
        return sp;
    }
    public Supplier getSupplierByNumber(String id)
    {
        Supplier sp=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Customer Details given the internal DB id... ");
            String sqlStmt="Select supplier_id,sname,number,phone,address, contactperson,pin ";
            sqlStmt+=" from supplier WHERE number ='" + id + "'";
            sqlStmt+=" order by number asc";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                sp=new Supplier();
                sp.setSupplierName(rs.getString("sname"));  
                sp.setSupplierNumber(rs.getString("number"));
                sp.setContactPerson(rs.getString("contactperson"));
                sp.setAddress(rs.getString("address"));
                sp.setPhone(rs.getString("phone"));               
                sp.setSupplier_id(rs.getInt("supplier_id"));
                sp.setPin(rs.getString("pin"));
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
	
         
        return sp;
    }
    
    public List<Supplier> getSupplierByName(String search)
    {
        List<Supplier> spList=new ArrayList<Supplier>();
        Supplier sp=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of Customers given the search criteria... ");
            String sqlStmt="Select supplier_id,sname,number,phone,address, contactperson, pin from supplier WHERE voided=0 and (number like '%" + search + "%' or "; //
            sqlStmt=sqlStmt+" sname like '%" + search + "%' or phone like '%" + search + "%' or address like '%" + search + "%'  or contactperson like '%" + search + "%') order by number asc;";
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                sp=new Supplier();
                sp.setSupplierName(rs.getString("sname"));  
                sp.setSupplierNumber(rs.getString("number"));
                sp.setContactPerson(rs.getString("contactperson"));
                sp.setAddress(rs.getString("address"));
                sp.setPhone(rs.getString("phone"));               
                sp.setSupplier_id(rs.getInt("supplier_id"));
                sp.setPin(rs.getString("pin"));
                spList.add(sp);
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
	 
        return spList;
    }
    
    public List<Supplier> getAllSuppliers()
    {
        List<Supplier> spList=new ArrayList<Supplier>();
        Supplier sp=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of of All suppliers... ");
            String sqlStmt="Select supplier_id,sname,number,phone,address, contactperson, pin ";
            sqlStmt+=" from supplier WHERE voided=0 "; //
            sqlStmt+="  order by number asc;";
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                sp=new Supplier();
                sp.setSupplierName(rs.getString("sname"));  
                sp.setSupplierNumber(rs.getString("number"));
                sp.setContactPerson(rs.getString("contactperson"));
                sp.setAddress(rs.getString("address"));
                sp.setPhone(rs.getString("phone"));               
                sp.setSupplier_id(rs.getInt("supplier_id"));
                sp.setPin(rs.getString("pin"));
                spList.add(sp);
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
	 
        return spList;
    }
    /**
     * Check if Identifier already exists
     * @param identifier
     * @return identifier count
     */
    public int checkDuplicateSupplierNumber(String identifier) 
    {
        int p=0;
        try
        {
            //log info
            AccountsManagement.logger.info("Validating Supplier number... ");
            String sqlStmt="Select count(supplier_id) as idcount from supplier WHERE number ='" + identifier + "'";
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
     * @param Supplier 
     */
    public void updateSupplier(Supplier sp) throws SQLException 
     {
          
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Supplier Details... ");
             //create a prepared statement
             preppedStmtUpdate="update supplier set sname=?,number=?,phone=?,address=?,contactperson=?,dateChanged=?, changedBy=?, pin=? WHERE supplier_id=?";
            //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
            //using Transactions to ensure successfull update
            Sql.getConnection().setAutoCommit(false) ;
            PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
            pst.setString(1, sp.getSupplierName());
            pst.setString(2, sp.getSupplierNumber());
            pst.setString(3, sp.getPhone());
            pst.setString(4, sp.getAddress());
            pst.setString(5, sp.getContactPerson());            
            pst.setTimestamp(6, Sql.getCurrentTimeStamp()); 
            pst.setInt(7, MainMenu.gUser.getUserId());
            pst.setString(8, sp.getPin());            
            pst.setInt(9, sp.getSupplier_id());
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
