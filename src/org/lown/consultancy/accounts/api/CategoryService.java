/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Category;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class CategoryService {
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    
    public CategoryService()
    {
       
    }
    
    public void saveCategory(Category category) throws SQLException
    {
        try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO productcategory (categorycode,categoryname,dateCreated,createdby) VALUES(?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            pst.setString(1, category.getCategoryCode());
            pst.setString(2, category.getCategoryName());            
            pst.setTimestamp(3, Sql.getCurrentTimeStamp());
            pst.setInt(4, MainMenu.gUser.getUserId());   
            pst.execute();
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Product Category Record Added...");
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
    /**
     * Check if Identifier already exists
     * @param identifier
     * @return identifier count
     */
    public int checkDuplicateCategoryCode(String identifier) 
    {
        int c=0;
        try
        {
            //log info
            AccountsManagement.logger.info("Validating Category Code... ");
            String sqlStmt="Select count(category_id) as idcount from productcategory WHERE categorycode ='" + identifier + "'";
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                c=rs.getInt("idcount");
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
//            Sql.Close();
//        }
        
        return c;
    }
    
    public void updateCategory(Category category) throws SQLException
    {
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Category Details... ");
             //create a prepared statement
             preppedStmtUpdate="update productcategory set categoryname=?,categorycode=?,dateChanged=?, changedBy=? WHERE category_id=?";
            //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
            //using Transactions to ensure successfull update
            Sql.getConnection().setAutoCommit(false) ;
            PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
            pst.setString(1, category.getCategoryName());
            pst.setString(2, category.getCategoryCode());                      
            pst.setTimestamp(3, Sql.getCurrentTimeStamp()); 
            pst.setInt(4, MainMenu.gUser.getUserId());
            pst.setInt(5, category.getCategory_id());
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
    
    public Category getCategoryById(int id)
    {
        Category c=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select category_id,categoryname,categorycode from productcategory WHERE voided=0 and category_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                c=new Category();
                c.setCategoryName(rs.getString("categoryname"));  
                c.setCategoryCode(rs.getString("categorycode"));                          
                c.setCategory_id(rs.getInt("category_id"));
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
    
    public Category getCategoryByCode(String search)
    {
        Category c=null;
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the  category code... ");
            String sqlStmt="Select category_id,categoryname,categorycode from productcategory WHERE voided=0 and categorycode ='" + search + "'";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                c=new Category();
                c.setCategoryName(rs.getString("categoryname"));  
                c.setCategoryCode(rs.getString("categorycode"));                          
                c.setCategory_id(rs.getInt("category_id"));
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
	
         
        return c;
    }
    
    public List<Category> getAllCategories()
    {
        List<Category> categoryList=new ArrayList<Category>();
         try
        {
            //log info
            AccountsManagement.logger.info("Get List of Customers given the search criteria... ");
            String sqlStmt="Select category_id,categoryname,categorycode from productcategory WHERE voided=0 "; //
            
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Category c = new Category();
                c.setCategoryCode(rs.getString("categorycode"));  
                c.setCategoryName(rs.getString("categoryname"));                                
                c.setCategory_id(rs.getInt("category_id"));              
                categoryList.add(c);
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
	 
        return categoryList;
    }
}
