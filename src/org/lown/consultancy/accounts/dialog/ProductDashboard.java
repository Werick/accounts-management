/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Category;
import org.lown.consultancy.accounts.dao.CategoryService;
import org.lown.consultancy.accounts.tables.CategoryListTable;
import org.lown.consultancy.accounts.tables.CustomerListTable;
import org.lown.consultancy.accounts.tables.ProductListTable;

/**
 *
 * @author LENOVO USER
 */
public class ProductDashboard extends JPanel implements ActionListener{
    
    private static final String ACT_NEW="new_category";
    private static final String ACT_PAY="receive_pay";
    private static final String ACT_CREATE="add_product";
    private static final String ACT_EDIT="update_product";
    private static final String ACT_DELETE="Print_Statement";
    private static final String ACT_BACK="close";
    private static final String ACT_VIEW="view_product";
     private static final String ACT_SAVECATEGORY="save_add";
    private static final String ACT_UPDATECATEGORY="update_cancel";
    private static final String ACT_DELETECATEGORY="delete";
    private static final String ACT_BACKCATEGORY="close";
    
    public static final Font title2Font = new Font("Times New Roman", Font.BOLD, 16);
    public static final Font title3Font = new Font("Times New Roman", Font.BOLD, 14);
    
    private TitledBorder titled = new TitledBorder("Find/Create Product (s)");
    private TitledBorder titled1 = new TitledBorder("Find/Create Product Category (s)");
    private TitledBorder titled2 = new TitledBorder("Product Information");
    private TitledBorder titled3 = new TitledBorder("Product Category");
    
    
    
    private JPanel pProduct;
    private JPanel pProductCategory;
    private JPanel pTransactions;
    private DecimalFormat df = new DecimalFormat("#0.00");
    
    private static JDialog dlgProductDashboard;
    
    private JLabel lbl_productName;
    private JLabel lbl_search;
    private JLabel lbl_sellingPrice;
    private JLabel lbl_productCode;
    private JTextField txt_productName;
    private JTextField txt_search;
    private JTextField txt_sellingPrice;
    private JTextField txt_productCode;
    
    private JButton btnCreateCategory;
    private JButton btnUpdate;
    private JButton btnClose;
    private JButton btnView;
    private JButton btnEditProduct;
    private JButton btnEditCategory;
    private JButton btnCreateProduct;
    private ProductListTable productListTable;
    
    //product categories
     private JLabel lbl_categoryName;
    private JLabel lbl_categoryCode;
    private JLabel lbl_title;
    private JLabel lbl_title2;
    
    private JTextField txt_categoryName;
    private JTextField txt_categoryCode;
    
    private JButton btnSaveCategory;
    private JButton btnUpdateCategory;
    private JButton btnCloseCategory;
    private JButton btnDeleteCategory;
    
    private CategoryListTable categoryListTable;
    private CategoryService categoryService;
    private Category productCategory;
    
    public ProductDashboard()
    {
        dlgProductDashboard= new JDialog((JDialog)null, "Product Center", true);
        dlgProductDashboard.setLayout(null);
        dlgProductDashboard.setSize(1200, 600);//Width size, Height size
        dlgProductDashboard.setLocationRelativeTo(null);//center the invoice on the screen
        
        titled2.setTitleFont(title2Font);
        titled3.setTitleFont(title2Font);
        titled1.setTitleFont(title2Font);
        
        pProductCategory=new JPanel();
        pProductCategory.setBounds(610, 20, 550, 500);
        pProductCategory.setBorder(titled1);  
        pProductCategory.setLayout(null);
        dlgProductDashboard.add(pProductCategory);
        
        lbl_title=new JLabel();
        lbl_title.setBounds(20, 20, 200, 25);         
        lbl_title.setText("Product Category Details");        
        pProductCategory.add(lbl_title); 
        
        lbl_categoryCode=new JLabel();
        lbl_categoryCode.setBounds(20, 50, 150, 25);         
        lbl_categoryCode.setText("Category Code:");        
        pProductCategory.add(lbl_categoryCode);
        
        txt_categoryCode=new JTextField();
        txt_categoryCode.setBounds(150, 50, 150, 25);         
        txt_categoryCode.setText("");        
        pProductCategory.add(txt_categoryCode);
        
        lbl_categoryName=new JLabel();
        lbl_categoryName.setBounds(20, 80, 150, 25);         
        lbl_categoryName.setText("Category Name:");        
        pProductCategory.add(lbl_categoryName);
        
        txt_categoryName=new JTextField();
        txt_categoryName.setBounds(150, 80, 200, 25);         
        txt_categoryName.setText("");        
        pProductCategory.add(txt_categoryName);
        
        
        btnSaveCategory=new JButton("Add Category");
        btnSaveCategory.setBounds(400, 50, 120, 25);
        btnSaveCategory.setActionCommand(ACT_SAVECATEGORY);
        btnSaveCategory.addActionListener(this);
        pProductCategory.add(btnSaveCategory);
        
        btnUpdateCategory=new JButton("Update");
        btnUpdateCategory.setBounds(400, 80, 120, 25);
        btnUpdateCategory.setActionCommand(ACT_UPDATECATEGORY);
        btnUpdateCategory.addActionListener(this);
        pProductCategory.add(btnUpdateCategory);
        
        btnDeleteCategory=new JButton("Delete");
        btnDeleteCategory.setBounds(400, 110, 120, 25);
        btnDeleteCategory.setActionCommand(ACT_DELETECATEGORY);
        btnDeleteCategory.addActionListener(this);
        btnDeleteCategory.setEnabled(false);
        pProductCategory.add(btnDeleteCategory);
        
        btnClose=new JButton("Back");
        btnClose.setBounds(200, 430, 150, 35);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        pProductCategory.add(btnClose);
        
        lbl_title2=new JLabel();
        lbl_title2.setBounds(20, 150, 200, 25);         
        lbl_title2.setText("Available Categories: ");        
        pProductCategory.add(lbl_title2); 
        
        //Category List Table
        categoryListTable=new CategoryListTable();
        categoryListTable.setBounds(50,180,400, 200);
        pProductCategory.add(categoryListTable);
        categoryListTable.insertRow();
        
        pProduct=new JPanel();
        pProduct.setBounds(20, 20, 550, 500);
        pProduct.setBorder(titled);  
        pProduct.setLayout(null);
        dlgProductDashboard.add(pProduct);
        
        btnView=new JButton("View Product");
        btnView.setBounds(200, 430, 150, 35); 
        btnView.setActionCommand(ACT_VIEW);
        btnView.addActionListener(this); 
        btnView.setToolTipText("Click to View Product.");
        pProduct.add(btnView);
       
        
        btnCreateProduct=new JButton("Create Product");
        btnCreateProduct.setBounds(395, 40, 135, 25); 
        btnCreateProduct.setActionCommand(ACT_CREATE);
        btnCreateProduct.addActionListener(this);  
        btnCreateProduct.setToolTipText("Click to Create/Add a new Product.");
        pProduct.add(btnCreateProduct);
        
        lbl_search=new JLabel();
        lbl_search.setBounds(10, 40, 200, 20);         
        lbl_search.setText("Enter Name or Code:");
        lbl_search.setFont(title3Font);     
        pProduct.add(lbl_search);
        
        txt_search=new JTextField();
        txt_search.setBounds(180, 40, 200, 25);         
        txt_search.setText("");         
        txt_search.setToolTipText("Enter atleast three characters to Search.");
        txt_search.setFont(title3Font);
        /*
         * Add a document Listener method to track changes in the text box and fire th search event
         */
        txt_search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                doSearch();
            }
            
            public void doSearch() 
            {
                if (txt_search.getText().length()>0)
                {                    
                    //Call the search method
                    productListTable.insertRow(txt_search.getText());
                }
                else
                {
                    productListTable.insertRow();
                }
            }
           
        });
        pProduct.add(txt_search);
         
        //Category List Table
        productListTable=new ProductListTable();
        productListTable.setBounds(20,80,500, 350);        
        productListTable.insertRow();
        pProduct.add(productListTable);
        
        
        dlgProductDashboard.setVisible(true);          
        dlgProductDashboard.dispose(); //close the app once done
        
    }
    
     @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgProductDashboard.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_CREATE))
        {
              ProductListTable.selectedProduct=null;       
              
              //load Product form
              ProductDialog.createAndShowGUI();
	}
        else if (e.getActionCommand().equals(ACT_VIEW))
        {
            if(ProductListTable.selectedProduct!=null)
            {
                ProductDialog.createAndShowGUI();
                return;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Select the Product first, then proceed...");
                return;
            }
        }
        else if(e.getActionCommand().equals(ACT_UPDATECATEGORY))
        {
            if(btnUpdateCategory.getText().equalsIgnoreCase("Cancel"))
            {
               //clear the text boxes to allow addtion of new category record
               btnSaveCategory.setText("Add Category"); 
               btnUpdateCategory.setText("Update");
               btnDeleteCategory.setEnabled(false);
               txt_categoryName.setText("");
               txt_categoryCode.setText("");
               return;
            }
            else if(btnUpdateCategory.getText().equalsIgnoreCase("Update"))
            {
                //do some update stuff
            }
	}
        else if(e.getActionCommand().equals(ACT_SAVECATEGORY))
        {
            if(btnSaveCategory.getText().equalsIgnoreCase("Add Category"))
            {
               //clear the text boxes to allow addtion of new category record
               btnSaveCategory.setText("Save Category"); 
               btnUpdateCategory.setText("Cancel");
               btnDeleteCategory.setEnabled(false);
               txt_categoryName.setText("");
               txt_categoryCode.setText("");
               return;
            }
            else  if(btnSaveCategory.getText().equalsIgnoreCase("Save Category"))
            {
                if (txt_categoryCode.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Category code Missing...");
                    return;                    
                }
                if (txt_categoryName.getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "Category Name Missing...");
                    return;                    
                }
                
                productCategory=new Category();
                categoryService=new CategoryService();
                productCategory.setCategoryCode(txt_categoryCode.getText());
                productCategory.setCategoryName(txt_categoryName.getText());
                try {
                    categoryService.saveCategory(productCategory);
                    categoryListTable.insertRow(productCategory);
                } catch (SQLException ex) {
                    Logger.getLogger(CategoryDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                btnSaveCategory.setText("Add Category"); 
                btnUpdateCategory.setText("Update");
                btnDeleteCategory.setEnabled(true);
                return;
            }
        }
    }
    
    public static void createAndShowGUI()
     {         
         AccountsManagement.logger.info("Loading CustomerDashboard Dialog");
         ProductDashboard productDashboard = new ProductDashboard();       
     }
    
}
