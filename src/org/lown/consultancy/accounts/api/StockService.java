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
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.Sql;
import org.lown.consultancy.accounts.Stock;
import org.lown.consultancy.accounts.dialog.MainMenu;

/**
 *
 * @author LENOVO USER
 */
public class StockService {
    private String preppedStmtInsert=null;
    private String preppedStmtUpdate=null;
    //private Sql Sql;
    private ProductService ps;
    
    public StockService()
    {
        //Sql=new Sql();
    }
    
    public void saveStockItem(Stock stock) throws SQLException
    {
        try
        {
           
            
            Sql.getConnection().setAutoCommit(false) ;
            preppedStmtInsert="INSERT INTO stock (product_id,sellprice,dateCreated,createdby) VALUES(?,?,?,?)";
            PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtInsert,Sql.createStatement().RETURN_GENERATED_KEYS);            
            pst.setInt(1, stock.getProduct().getProduct_id());
            pst.setDouble(2, stock.getSellingPrice());            
            pst.setTimestamp(3, Sql.getCurrentTimeStamp());
            pst.setInt(4, MainMenu.gUser.getUserId());   
            pst.execute();
            Sql.getConnection().commit();
            
            //JOptionPane.showMessageDialog(null, "Product Category Record Added...");
             //
    
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
         
    }
    
    /*
     * Get the product's or stock Item's selling price
     * @param id
     * the product id must be specified
     */
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
    
    public List<Stock> getAllStockItems()
    {
        List<Stock>stockList=new ArrayList<Stock>();
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select stock_id,product_id,buyprice,sellprice,quantity,reorderlevel,if(vat=1,true,false) as vat from stock WHERE voided=0";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               Stock stock=new Stock();
               stock.setBuyingPrice(rs.getDouble("buyprice"));
               stock.setHasVat(rs.getBoolean("vat"));
               stock.setQuantity(rs.getInt("quantity"));
               stock.setSellingPrice(rs.getDouble("sellprice"));
               stock.setStock_id(rs.getInt("stock_id"));
               stock.setReorderLevel(rs.getInt("reorderlevel"));               
               stockList.add(stock);                              
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
        
        return stockList;
    }
    
     /*
     * Get a list of stock items given that require reordering
     * @Param String search
     */
    public List<Stock> getStockItemsByReorderLevel()
    {
        List<Stock>stockList=new ArrayList<Stock>();
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select stock_id,s.product_id,buyprice,sellprice,quantity,reorderlevel,if(vat=1,true,false) as vat ";
            sqlStmt+=" from stock s ";
            sqlStmt+=" join product p on p.product_id=s.product_id and p.voided=0 ";
            sqlStmt+=" WHERE s.voided=0 and s.reorderlevel<=0 or s.reorderlevel<s.quantity ";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               Stock stock=new Stock();
               stock.setBuyingPrice(rs.getDouble("buyprice"));
               stock.setHasVat(rs.getBoolean("vat"));
               stock.setQuantity(rs.getInt("quantity"));
               stock.setSellingPrice(rs.getDouble("sellprice"));
               stock.setStock_id(rs.getInt("stock_id"));
               stock.setReorderLevel(rs.getInt("reorderlevel"));               
               stockList.add(stock);                              
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
        
        return stockList;
    }
    /*
     * Get a list of stock items given the priduct name or code
     * @Param String search
     */
    public List<Stock> getStockItemsByName(String search)
    {
        List<Stock>stockList=new ArrayList<Stock>();
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select stock_id,s.product_id,buyprice,sellprice,quantity,reorderlevel,if(vat=1,true,false) as vat ";
            sqlStmt+=" from stock s ";
            sqlStmt+=" join product p on p.product_id=s.product_id and p.voided=0 ";
            sqlStmt+=" WHERE s.voided=0 and (p.productcode like '%"+ search+ "%' or p.productname like '%"+ search+ "%')";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               Stock stock=new Stock();
               stock.setBuyingPrice(rs.getDouble("buyprice"));
               stock.setHasVat(rs.getBoolean("vat"));
               stock.setQuantity(rs.getInt("quantity"));
               stock.setSellingPrice(rs.getDouble("sellprice"));
               stock.setStock_id(rs.getInt("stock_id"));
               stock.setReorderLevel(rs.getInt("reorderlevel"));               
               stockList.add(stock);                              
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
        
        return stockList;
    }
    
    /*
     * Get a list of stock items given that require reordering
     * @Param String search
     */
    public int getPendingStockItems()
    {
        int c=0;
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="select count(*) as total ";
            sqlStmt+="from (Select stock_id,s.product_id,buyprice,sellprice,quantity,reorderlevel,if(vat=1,true,false) as vat ";
            sqlStmt+=" from stock s ";
            sqlStmt+=" join product p on p.product_id=s.product_id and p.voided=0 ";
            sqlStmt+=" WHERE s.voided=0 and s.reorderlevel<=0 or s.reorderlevel<s.quantity)st ";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               c=rs.getInt("total")   ;                          
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
        
        return  c;
    }
    /*
     * Get a list of stock items given the priduct name or code
     * @Param String search
     */
    public List<Stock> getStockItemsByCategory(int search)
    {
        List<Stock>stockList=new ArrayList<Stock>();
        
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select stock_id,s.product_id,buyprice,sellprice,quantity,reorderlevel,if(vat=1,true,false) as vat ";
            sqlStmt+=" from stock s ";
            sqlStmt+=" join product p on p.product_id=s.product_id and p.voided=0 and p.category_id = "+search;
            sqlStmt+=" WHERE s.voided=0 ";
           
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
               Stock stock=new Stock();
               stock.setBuyingPrice(rs.getDouble("buyprice"));
               stock.setHasVat(rs.getBoolean("vat"));
               stock.setQuantity(rs.getInt("quantity"));
               stock.setSellingPrice(rs.getDouble("sellprice"));
               stock.setStock_id(rs.getInt("stock_id"));
               stock.setReorderLevel(rs.getInt("reorderlevel"));               
               stockList.add(stock);                              
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
        
        return stockList;
    }
    /*
     * Get Stock Item given the stock item internal db id
     * @param id
     */
    public Product getProductByStockId(int id)
    {
        //Am usng Streaming in anticipation that This database will end up having very large tables
        //For successfull streaming a result set must be closed before another statement can be issued
        // Concurrent result sets are not allowed
        Product product=new Product();
        ps=new ProductService();
        try
        {
            //log info
            AccountsManagement.logger.info("Getting Category Details given the internal DB id... ");
            String sqlStmt="Select product_id, stock_id from stock WHERE voided=0 and stock_id =" + id + "";
           int pid=0;
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                pid=rs.getInt("product_id");
                           
            }
            rs.close();
            product=ps.getProductById(pid);   
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e);
         }
        
        //stock.setProduct(product);
        
        return product;
    }
    
    /*
     * Update a list of stock Items 
     */
    
    public void updateStockItem(List<Stock> stockItems) throws SQLException
    {
         try
         {
             //log info
             AccountsManagement.logger.info("Updating Stock Item Details... ");
             
             Sql.getConnection().setAutoCommit(false) ;            
                //Update the stock Item
             for(Stock stock:stockItems)
             {
                 preppedStmtUpdate="UPDATE stock set sellprice=?, datechanged=?, changedby=?, buyprice=?, reorderlevel=?, vat=?  WHERE product_id=? ";
                 PreparedStatement pst=Sql.getConnection().prepareStatement(preppedStmtUpdate);            
            
                 pst.setDouble(1, stock.getSellingPrice());            
                 pst.setTimestamp(2, Sql.getCurrentTimeStamp());
                 pst.setInt(3, MainMenu.gUser.getUserId());  
                 pst.setDouble(4, stock.getBuyingPrice()); 
                 pst.setInt(5, stock.getReorderLevel());                 
                 pst.setInt(6, (stock.isHasVat()? 1:0));
                 pst.setInt(7, stock.getProduct().getProduct_id());                 
                 pst.executeUpdate();           
             }
             
            Sql.getConnection().commit(); //commit transaction if successful
            JOptionPane.showMessageDialog(null, "Record(s) Updated...");
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
