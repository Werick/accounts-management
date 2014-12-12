/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Category;
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.Stock;
import org.lown.consultancy.accounts.dao.ProductService;
import org.lown.consultancy.accounts.tables.ProductListTable;

/**
 *
 * @author LENOVO USER
 */
public class ProductDialog extends JPanel implements ActionListener{
    private static final String ACT_SAVE="save_add";
    private static final String ACT_UPDATE="update_cancel";
    private static final String ACT_DELETE="delete";
    private static final String ACT_BACK="close";
    private static final String ACT_VIEW="view_product";
    
    private JLabel lbl_productName;
    private JLabel lbl_search;
    private JLabel lbl_sellingPrice;
    private JLabel lbl_buyingPrice;
    private JLabel lbl_productCode;
    private JLabel lbl_productCategory;
    private JLabel lbl_measureUnit;
    private JLabel lbl_title;
    private JLabel lbl_title2;
    
    private static JTextField txt_productName;
    private static JTextField txt_search;
    private static JTextField txt_sellingPrice;
    private static JTextField txt_buyingPrice;
    private static JTextField txt_productCode;
    private static JComboBox cbo_productCategory;
    private static JTextField txt_measureUnit;
    
    private static JButton btnSave;
    private JButton btnUpdate;
    private JButton btnClose;
    private static JButton btnDelete;
    private JButton btnView;
    
    private static JDialog dlgProduct;
    private static ProductListTable productListTable;
    private static ProductService productService;   
    private static Product product;
    private static Stock stock;
    private static Map<Integer,Category> productCategoryMap;
    private Map<String,Integer>categoryList;
    public ProductDialog()
    {
        AccountsManagement.logger.info("Creating Products UI...");        
        
        dlgProduct= new JDialog((JDialog)null, "Product Data Form", true);
        dlgProduct.setLayout(null);
        dlgProduct.setSize(650, 300);//Width size, Height size
        dlgProduct.setLocationRelativeTo(null);//center the invoice on the screen
        
        productService=new ProductService();
        productCategoryMap=productService.getProductCategoryMap();
        categoryList=productService.getCategoryMap();
        
        lbl_title=new JLabel();
        lbl_title.setBounds(10, 20, 200, 25);         
        lbl_title.setText("Product Details");        
        dlgProduct.add(lbl_title); 
        
        lbl_productCode=new JLabel();
        lbl_productCode.setBounds(10, 50, 150, 25);         
        lbl_productCode.setText("Product Code:");        
        dlgProduct.add(lbl_productCode);
        
        txt_productCode=new JTextField();
        txt_productCode.setBounds(150, 50, 150, 25);         
        txt_productCode.setText("");        
        dlgProduct.add(txt_productCode);
        
        lbl_productName=new JLabel();
        lbl_productName.setBounds(10, 80, 150, 25);         
        lbl_productName.setText("Product Description:");        
        dlgProduct.add(lbl_productName);
        
        txt_productName=new JTextField();
        txt_productName.setBounds(150, 80, 250, 25);         
        txt_productName.setText("");        
        dlgProduct.add(txt_productName);
        
        lbl_productCategory=new JLabel();
        lbl_productCategory.setBounds(10, 110, 150, 25);         
        lbl_productCategory.setText("Product Category:");        
        dlgProduct.add(lbl_productCategory);
        
        cbo_productCategory=new JComboBox(categoryList.keySet().toArray());
        cbo_productCategory.setBounds(150, 110, 200, 25);         
        //txt_productName.setText("");        
        dlgProduct.add(cbo_productCategory);
        
        lbl_measureUnit=new JLabel();
        lbl_measureUnit.setBounds(10, 140, 150, 25);         
        lbl_measureUnit.setText("Measurement Unit:");        
        dlgProduct.add(lbl_measureUnit);
        
        txt_measureUnit=new JTextField();
        txt_measureUnit.setBounds(150, 140, 100, 25);         
        txt_measureUnit.setText("");        
        dlgProduct.add(txt_measureUnit);
        
        lbl_sellingPrice=new JLabel();
        lbl_sellingPrice.setBounds(10, 170, 150, 25);         
        lbl_sellingPrice.setText("Selling Price (Ksh):");        
        dlgProduct.add(lbl_sellingPrice);
        
        txt_sellingPrice=new JTextField();
        txt_sellingPrice.setBounds(150, 170, 100, 25);         
        txt_sellingPrice.setText("");        
        dlgProduct.add(txt_sellingPrice);
        
        lbl_buyingPrice=new JLabel();
        lbl_buyingPrice.setBounds(10, 200, 150, 25);         
        lbl_buyingPrice.setText("buying Price (Ksh):");        
        dlgProduct.add(lbl_buyingPrice);
        
        txt_buyingPrice=new JTextField();
        txt_buyingPrice.setBounds(150, 200, 100, 25);         
        txt_buyingPrice.setText("");        
        dlgProduct.add(txt_buyingPrice);
        
        
        btnSave=new JButton("Add Product");
        btnSave.setBounds(500, 50, 110, 25);
        btnSave.setActionCommand(ACT_SAVE);
        btnSave.addActionListener(this);
        dlgProduct.add(btnSave);
        
        btnUpdate=new JButton("Update");
        btnUpdate.setBounds(500, 90, 110, 25);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        dlgProduct.add(btnUpdate);
        
        btnDelete=new JButton("Delete");
        btnDelete.setBounds(500, 130, 110, 25);
        btnDelete.setActionCommand(ACT_DELETE);
        btnDelete.addActionListener(this);
        //btnDelete.setEnabled(false);
        dlgProduct.add(btnDelete);
        
        btnClose=new JButton("Back");
        btnClose.setBounds(500, 170, 110, 25);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        dlgProduct.add(btnClose);
        
//        btnView=new JButton("View");
//        btnView.setBounds(500, 220, 110, 25);
//        btnView.setActionCommand(ACT_VIEW);
//        btnView.addActionListener(this);
//        dlgProduct.add(btnView);
//        
//        lbl_title2=new JLabel();
//        lbl_title2.setBounds(10, 220, 120, 25);         
//        lbl_title2.setText("Available Products: ");        
//        dlgProduct.add(lbl_title2); 
//        
//        lbl_search=new JLabel();
//        lbl_search.setBounds(150, 220, 250, 25);         
//        lbl_search.setText("[Quick Search (Enter product Code/Name):]"); 
//        
//        dlgProduct.add(lbl_search); 
        
//        txt_search=new JTextField();
//        txt_search.setBounds(400, 220, 85, 25);         
//        txt_search.setText(""); 
//        txt_search.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void changedUpdate(DocumentEvent e) 
//            {
//                doSearch();
//            }
//            
//            @Override
//            public void removeUpdate(DocumentEvent e) 
//            {
//                doSearch();
//            }
//            
//            @Override
//            public void insertUpdate(DocumentEvent e) 
//            {
//                doSearch();
//            }
//            
//            public void doSearch() 
//            {
//                if (txt_search.getText().length()>=3)
//                {
////                    //Call the search method
//                    productListTable.insertRow(txt_search.getText());
//                }
//            }
//           
//        });
//        dlgProduct.add(txt_search);
//        
//        //Category List Table
//        productListTable=new ProductListTable();
//        productListTable.setBounds(40,250,550, 550);
//        dlgProduct.add(productListTable);
//        productListTable.insertRow();        
       
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgProduct.setVisible(false);
            return;
	}
       
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(btnSave.getText().equalsIgnoreCase("Add Product"))
            {
               //clear the text boxes to allow addtion of new category record
               btnSave.setText("Save"); 
               btnUpdate.setText("Cancel");
               btnDelete.setEnabled(false);
               txt_productName.setText("");
               txt_productCode.setText("");
               txt_measureUnit.setText("");
               txt_sellingPrice.setText("");
               txt_buyingPrice.setText("");
               return;
            }
            else  if(btnSave.getText().equalsIgnoreCase("Save"))
            {
                if(validateInput())
                {
                    product=new Product();
                    productService=new ProductService();
                    product.setProductCode(txt_productCode.getText());
                    product.setProductName(txt_productName.getText());
                    Integer selCategory=(Integer)categoryList.get(cbo_productCategory.getSelectedItem());
                    product.setCategory(productCategoryMap.get(selCategory));
                    product.setMeasureUnit(txt_measureUnit.getText());
                
                    stock=new Stock();
                    stock.setProduct(product);
                    
                    if (isNumeric(txt_sellingPrice.getText()))
                    {
                        stock.setSellingPrice(Double.parseDouble(txt_sellingPrice.getText()));
                    }
                    
                    if (isNumeric(txt_buyingPrice.getText()))
                    {
                        stock.setBuyingPrice(Double.parseDouble(txt_buyingPrice.getText()));
                    }
                
                    try {
                        productService.saveProduct(product,stock);
                        //productListTable.insertRow(product);
                    } catch (SQLException ex) {
                        Logger.getLogger(CategoryDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                    btnSave.setText("Add Product"); 
                    btnUpdate.setText("Update");
                    btnDelete.setEnabled(true);
                }
                    
                
                return;
            }
        }
        else if(e.getActionCommand().equals(ACT_DELETE))
        {
            return;
        }
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            if(btnUpdate.getText().equalsIgnoreCase("Cancel"))
            {
               //clear the text boxes to allow addtion of new category record
               btnSave.setText("Add Product"); 
               btnUpdate.setText("Update");
               btnDelete.setEnabled(true);
               txt_productName.setText("");
               txt_productCode.setText("");
               txt_measureUnit.setText("");
               txt_sellingPrice.setText("");
               return;
            }
            else  if(btnUpdate.getText().equalsIgnoreCase("Update"))
            {
                if (validateInput())
                {
                    if (product!=null)
                    {
                        productService=new ProductService();
                        product.setProductCode(txt_productCode.getText());
                        product.setProductName(txt_productName.getText());
                        Integer selCategory=(Integer)categoryList.get(cbo_productCategory.getSelectedItem());
                        product.setCategory(productCategoryMap.get(selCategory));
                        product.setMeasureUnit(txt_measureUnit.getText());
                        
                        stock=new Stock();
                    stock.setProduct(product);
                    
                    if (isNumeric(txt_sellingPrice.getText()))
                    {
                        stock.setSellingPrice(Double.parseDouble(txt_sellingPrice.getText()));
                    }
                    
                    if (isNumeric(txt_buyingPrice.getText()))
                    {
                        stock.setBuyingPrice(Double.parseDouble(txt_buyingPrice.getText()));
                    }
                         try {
                             productService.updateProduct(product,stock);                            
                    } catch (SQLException ex) {
                        Logger.getLogger(CategoryDialog.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                }
            }
            return; 
        }
        else if(e.getActionCommand().equals(ACT_VIEW))
        {
            System.out.println("Testing view product..");
            if (productListTable.getRowCount()==0)
            {
                    return;
            }
            else if (ProductListTable.selectedProduct!=null)
            {
                System.out.println("Testing Selected view product..");
                product=new Product();
                product=ProductListTable.selectedProduct;
                txt_productName.setText(product.getProductName());
                txt_productCode.setText(product.getProductCode());
                txt_measureUnit.setText(product.getMeasureUnit());
                txt_sellingPrice.setText(Double.toString(productService.getProductSellingPrice(product.getProduct_id())));
                txt_buyingPrice.setText(Double.toString(productService.getProductBuyingPrice(product.getProduct_id())));
                Category selCategory=productCategoryMap.get(product.getCategory_id());
                cbo_productCategory.setSelectedItem(selCategory.getCategoryCode());
            }
            return;
        }
        
        
        
    }
    
     public static void createAndShowGUI()
     {
         AccountsManagement.logger.info("Loading Category Dialog");
         ProductDialog productDialog = new ProductDialog();      
         if (ProductListTable.selectedProduct!=null)
            {
                System.out.println("Testing Selected view product..");
                product=new Product();
                product=ProductListTable.selectedProduct;
                txt_productName.setText(product.getProductName());
                txt_productCode.setText(product.getProductCode());
                txt_measureUnit.setText(product.getMeasureUnit());
                txt_sellingPrice.setText(Double.toString(productService.getProductSellingPrice(product.getProduct_id())));
                txt_buyingPrice.setText(Double.toString(productService.getProductBuyingPrice(product.getProduct_id())));
                Category selCategory=productCategoryMap.get(product.getCategory_id());
                cbo_productCategory.setSelectedItem(selCategory.getCategoryCode());
                btnSave.setEnabled(false);
            }
            else
            {
                btnDelete.setEnabled(false);
                
            }
         dlgProduct.setVisible(true);          
         dlgProduct.dispose(); //close the app once done
     }
     
     private boolean validateInput()
     {
          if (txt_productCode.getText().isEmpty())
          {
              JOptionPane.showMessageDialog(null, "Product code Missing...");
              return false;                    
          }
          if (txt_productName.getText().isEmpty())
          {
              JOptionPane.showMessageDialog(null, "Product Name Missing...");
              return false;                    
          }
                
          if (txt_measureUnit.getText().isEmpty())
          {
             JOptionPane.showMessageDialog(null, "Product Measurement unit Missing...");
             return false;                    
          }
          
          if (txt_sellingPrice.getText().isEmpty())
          {
              JOptionPane.showMessageDialog(null, "Product Selling Price Missing...");
              return false;                    
            }
                
         return true;
     }
     
    private boolean isNumeric(String str)  
    {  
        try 
            {  
                double d = Double.parseDouble(str);  
            }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
        return true;  
    }
    
    
}
