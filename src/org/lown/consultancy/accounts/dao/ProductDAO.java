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
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Category;
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.Stock;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class ProductDAO {
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    //private Sql Sql;
    
    public ProductDAO()
    {
       // Sql=new Sql();
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
    public void saveProduct(Product product) throws SQLException
    {
        try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO product (productcode,productname,category_id,measureunit,dateCreated,createdby) VALUES(?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            pst.setString(1, product.getProductCode());
            pst.setString(2, product.getProductName()); 
            pst.setInt(3,  product.getCategory().getCategory_id());
            pst.setString(4, product.getMeasureUnit()); 
            pst.setTimestamp(5, Sql.getCurrentTimeStamp());
            pst.setInt(6, MainMenu.gUser.getUserId());   
            pst.execute();
            int productid=getLastProductId();
            product.setProduct_id(productid);
            
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
//         finally
//         {
//             Sql.Close();
//         }
    }
    
    public void saveProduct(Product product, Stock stock) throws SQLException
    {
        try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO product (productcode,productname,category_id,measureunit,dateCreated,createdby) VALUES(?,?,?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);            
            pst.setString(1, product.getProductCode());
            pst.setString(2, product.getProductName()); 
            pst.setInt(3,  product.getCategory().getCategory_id());
            pst.setString(4, product.getMeasureUnit()); 
            pst.setTimestamp(5, Sql.getCurrentTimeStamp());
            pst.setInt(6, MainMenu.gUser.getUserId());   
            pst.execute();
            int productid=getLastProductId();
            product.setProduct_id(productid);
            
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
                System.out.println("Last Inserted Id:"   + productid);
                
                product.setProduct_id(autoIncKeyFromApi);
            
            //Insert the stock Item
            preppedStmtInsert="INSERT INTO stock (product_id,sellprice,dateCreated,createdby,buyprice) VALUES(?,?,?,?,?)";
            pst=Sql.getConnection().prepareStatement(preppedStmtInsert);            
            pst.setInt(1, stock.getProduct().getProduct_id());
            pst.setDouble(2, stock.getSellingPrice());            
            pst.setTimestamp(3, Sql.getCurrentTimeStamp());
            pst.setInt(4, MainMenu.gUser.getUserId());  
            pst.setDouble(5, stock.getBuyingPrice()); 
            pst.execute();
            Sql.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Product Record Added...");
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
    
    /**
     * Check if Identifier already exists
     * @param productcode
     * @return productcode count
     */
    public int checkDuplicateProductCode(String productcode) 
    {
        int c=0;
        try
        {
            //log info
            AccountsManagement.logger.info("Validating Product Code... ");
            String sqlStmt="Select count(product_id) as idcount from product WHERE productcode ='" + productcode + "'";
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
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
//        finally
//        {
//            Sql.Close();
//        }
        
        return c;
    }
    
    public int getLastProductId() 
    {
        int c=0;
        try
        {
            //log info
            AccountsManagement.logger.info("Validating Product Code... ");
            String sqlStmt="Select max(product_id) as last_id from product";
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                c=rs.getInt("last_id");
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
        
        
        return c;
    }
    
    public void updateProduct(Product product) throws SQLException
    {
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Product Details... ");
             //create a prepared statement
             preppedStmtUpdate="update product set productname=?,productcode=?,category_id=?, measureunit=?, dateChanged=?, changedBy=? WHERE product_id=?";
            //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
            //using Transactions to ensure successfull update
            Sql.getConnection().setAutoCommit(false) ;
            PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
            pst.setString(1, product.getProductName());
            pst.setString(2, product.getProductCode());                      
            pst.setInt(3,  product.getCategory().getCategory_id());
            pst.setString(4, product.getMeasureUnit()); 
            pst.setTimestamp(5, Sql.getCurrentTimeStamp());
            pst.setInt(6, MainMenu.gUser.getUserId());
            pst.setInt(7, product.getProduct_id());
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
    public void updateProduct(Product product, Stock stock) throws SQLException
    {
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Product Details... ");
             //create a prepared statement
             preppedStmtUpdate="update product set productname=?,productcode=?,category_id=?, measureunit=?, datechanged=?, changedby=? WHERE product_id=?";
            //System.out.println("Check if db print has data: "+ p.getlMiddleFmd());
            //using Transactions to ensure successfull update
            Sql.getConnection().setAutoCommit(false) ;
            PreparedStatement pst= Sql.getConnection().prepareStatement(preppedStmtUpdate);
            pst.setString(1, product.getProductName());
            pst.setString(2, product.getProductCode());                      
            pst.setInt(3,  product.getCategory().getCategory_id());
            pst.setString(4, product.getMeasureUnit()); 
            pst.setTimestamp(5, Sql.getCurrentTimeStamp());
            pst.setInt(6, MainMenu.gUser.getUserId());
            pst.setInt(7, product.getProduct_id());
            pst.executeUpdate(); 
            
            //Update the stock Item
            preppedStmtUpdate="UPDATE stock set sellprice=?, datechanged=?, changedby=?, buyprice=?  WHERE product_id=? ";
            pst=Sql.getConnection().prepareStatement(preppedStmtUpdate);            
            
            pst.setDouble(1, stock.getSellingPrice());            
            pst.setTimestamp(2, Sql.getCurrentTimeStamp());
            pst.setInt(3, MainMenu.gUser.getUserId());  
            pst.setDouble(4, stock.getBuyingPrice()); 
            pst.setInt(5, stock.getProduct().getProduct_id());
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
    public Product getProductById(int id)
    {
        Product product=null;
        Map<Integer,Category> categoryMap=getProductCategoryMap();
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select product_id,productname,productcode, measureunit, category_id from product WHERE voided=0 and product_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                product=new Product();
                product.setProductName(rs.getString("productname"));  
                product.setProductCode(rs.getString("productcode"));                          
                product.setCategory_id(rs.getInt("category_id"));
                product.setMeasureUnit(rs.getString("measureunit"));
                product.setProduct_id(rs.getInt("product_id"));
                product.setCategory(categoryMap.get((Integer)rs.getInt("category_id")));
            }
            rs.close();
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
	
         
        return product;
    }
    
    public double getProductSellingPrice(int id)
    {
        double sp=0.0;      
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select product_id,sellprice from stock WHERE voided=0 and product_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               sp=rs.getDouble("sellprice");               
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
	
         
        return sp;
    }
    public double getProductBuyingPrice(int id)
    {
        double bp=0.0;      
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select product_id,buyprice from stock WHERE voided=0 and product_id =" + id + "";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               bp=rs.getDouble("buyprice");               
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
	
         
        return bp;
    }
    
    public Product getProductByCode(String search)
    {
        Product p=null;
        Map<Integer,Category> categoryMap=getProductCategoryMap();
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Product Details given the  category code... ");
            String sqlStmt="Select product_id,productname,productcode, measureunit, category_id from product WHERE voided=0 and productcode ='" + search + "'";
            //AccountsManagement.logger.info("Test Product sql "+sqlStmt);
            //System.out.println(sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                p = new Product();
                p.setProductCode(rs.getString("productcode"));  
                p.setProductName(rs.getString("productname"));                                
                p.setCategory_id(rs.getInt("category_id"));
                p.setMeasureUnit(rs.getString("measureunit"));
                p.setProduct_id(rs.getInt("product_id"));  
                p.setCategory(categoryMap.get((Integer)rs.getInt("category_id")));
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
	
         
        return p;
    }
    
    public List<Product> getProductsByName(String search)
    {
        List<Product> productList=new ArrayList<Product>();
        Map<Integer,Category> categoryMap=getProductCategoryMap();
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of All products... ");
            String sqlStmt="Select product_id,productname,productcode, measureunit, category_id from product WHERE voided=0 "; //
            sqlStmt+="and (productcode like '%"+ search+ "%' or productname like '%"+ search+ "%')";
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Product p = new Product();
                p.setProductCode(rs.getString("productcode"));  
                p.setProductName(rs.getString("productname")); 
                p.setMeasureUnit(rs.getString("measureunit"));
                p.setCategory_id(rs.getInt("category_id"));
                p.setProduct_id(rs.getInt("product_id"));
                p.setCategory(categoryMap.get((Integer)rs.getInt("category_id")));
                productList.add(p);
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
	 
        return productList;
    }
    
    public List<Product> getAllProducts()
    {
        List<Product> productList=new ArrayList<Product>();
        
        Map<Integer,Category> categoryMap=getProductCategoryMap();
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of All products... ");
            String sqlStmt="Select product_id,productname,productcode, measureunit, category_id from product WHERE voided=0 "; //
            
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Product p = new Product();
                p.setProductCode(rs.getString("productcode"));  
                p.setProductName(rs.getString("productname")); 
                p.setMeasureUnit(rs.getString("measureunit"));
                p.setCategory_id(rs.getInt("category_id"));
                p.setProduct_id(rs.getInt("product_id"));
                p.setCategory(categoryMap.get((Integer)rs.getInt("category_id")));
                productList.add(p);
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
	 
        return productList;
    }
    
    public Map<String, Integer> getCategoryMap()
    {
        Map<String, Integer> categoryMap=new HashMap<String,Integer>();
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of all Product Category and put them in a hash map... ");
            String sqlStmt="Select category_id,categoryname,categorycode from productcategory WHERE voided=0 "; //            
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Category c = new Category();
                c.setCategoryCode(rs.getString("categorycode"));  
                c.setCategoryName(rs.getString("categoryname"));                                
                c.setCategory_id(rs.getInt("category_id"));              
                categoryMap.put(c.getCategoryName(), (Integer)c.getCategory_id());
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
	 
        
        return categoryMap;
    }
    
    public Map<String, Integer> getProductMap(int category_id)
    {
        Map<String, Integer> productMap=new HashMap<String,Integer>();
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of all Product given the category and put them in a hash map... ");
            String sqlStmt="Select productname,productcode,product_id from product WHERE voided=0 "; // 
            sqlStmt+=" and category_id="+category_id;
            sqlStmt+=" order by productname asc";
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Product p = new Product();
                p.setProductCode(rs.getString("productcode"));  
                p.setProductName(rs.getString("productname"));                                
                p.setProduct_id(rs.getInt("product_id"));              
                productMap.put(p.getProductName(), (Integer)p.getProduct_id());
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
	 
        
        return productMap;
    }
    
    public Map<Integer, Category> getProductCategoryMap()
    {
        Map<Integer, Category> categoryMap=new HashMap<Integer,Category>();
        try
        {
            //log info
            AccountsManagement.logger.info("Get List of all Product Category and put them in a hash map... ");
            String sqlStmt="Select category_id,categoryname,categorycode from productcategory WHERE voided=0 "; //            
          
            AccountsManagement.logger.info("Executing Query: " + sqlStmt);
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                Category c = new Category();
                c.setCategoryCode(rs.getString("categorycode"));  
                c.setCategoryName(rs.getString("categoryname"));                                
                c.setCategory_id(rs.getInt("category_id"));              
                categoryMap.put((Integer)c.getCategory_id(),c);
            }
            rs.close();
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
	 
        
        return categoryMap;
    }
    
}
