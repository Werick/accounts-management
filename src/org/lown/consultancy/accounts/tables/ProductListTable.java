/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.dao.ProductDAO;

/**
 *
 * @author LENOVO USER
 */
public class ProductListTable extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"Product_id","Code","Name","Category","Unit"};
    private static DefaultTableModel model ;
    private Object[][] data; 
    private ProductDAO ps;
    private List<Product> productList;
    public static Product selectedProduct;
    
    public ProductListTable()
    {
        AccountsManagement.logger.info("Creating Product List Table UI...");
        ps=new ProductDAO();
        productList=ps.getAllProducts();
        selectedProduct=null;
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false; //Disallow the editing of any cell
            }
        };
        jTable.setSize(400, 450); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(400, 450));       
        jTable.setPreferredScrollableViewportSize(new Dimension(480, 300));//determines the width and height of your table
        
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //Id column
        column.setPreferredWidth(50);
        
        column = jTable.getColumnModel().getColumn(1); //Code column
        column.setPreferredWidth(60);
        
        column = jTable.getColumnModel().getColumn(2); //Name column
        column.setPreferredWidth(200);
       
        add(scrollPane);
        add(header);
        validate();
    }
    
    private void getSelectedRow()
    {
     //  JOptionPane.showMessageDialog(null, "Selected Row is: "+jTable.getSelectedRow());
       jTable.getSelectionModel().addListSelectionListener(
               new ListSelectionListener(){
                   public void valueChanged(ListSelectionEvent e)
                   {
                       int selectedRow=jTable.getSelectedRow();
                       if ((selectedRow>=0)&&(jTable.getRowCount()>selectedRow))
                       {
                            Object data = model.getValueAt(selectedRow, 1);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));                            
                            selectedProduct=ps.getProductByCode(data.toString());                            
                       }
                   }
                   
               });
    }
    public void insertRow()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available product categories from the database
        if(!productList.isEmpty())
         {
            for (Product p:productList)
            {
                //System.out.println("Testing Stuff"+p.getCustomerNumber());
                model.insertRow(jTable.getRowCount(),new Object[]{p.getProduct_id(),p.getProductCode(),p.getProductName(),p.getCategory().getCategoryName(),p.getMeasureUnit()});
             }
          }

    }
    public void insertRow(String search)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        productList=ps.getProductsByName(search); 
        //list all available product categories from the database
        if(!productList.isEmpty())
         {
            for (Product p:productList)
            {
                //System.out.println("Testing Stuff"+p.getCustomerNumber());
                model.insertRow(jTable.getRowCount(),new Object[]{p.getProduct_id(),p.getProductCode(),p.getProductName(),p.getCategory().getCategoryName(),p.getMeasureUnit()});
             }
          }

    }
    public void insertRow(Product product)
    {
        //remove all rows      
        //model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available product categories from the database
        if(product!=null)
         {
            //System.out.println("Testing Stuff"+p.getCustomerNumber());            
            model.insertRow(jTable.getRowCount(),new Object[]{product.getProduct_id(),product.getProductCode(),product.getProductName(),product.getCategory().getCategoryName(),product.getMeasureUnit()});
             
          }

    }
    
    public Product getSelectedProduct()
    {
        return selectedProduct;
    }
    
    public int getRowCount()
    {
        return jTable.getRowCount();
    }
}
