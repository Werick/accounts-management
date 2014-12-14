/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Category;
import org.lown.consultancy.accounts.dao.CategoryDAO;
import org.lown.consultancy.accounts.tables.CategoryListTable;

/**
 *
 * @author LENOVO USER
 */
public class CategoryDialog extends JPanel implements ActionListener{
    private static final String ACT_SAVE="save_add";
    private static final String ACT_UPDATE="update_cancel";
    private static final String ACT_DELETE="delete";
    private static final String ACT_BACK="close";
    
    private JLabel lbl_categoryName;
    private JLabel lbl_categoryCode;
    private JLabel lbl_title;
    private JLabel lbl_title2;
    
    private JTextField txt_categoryName;
    private JTextField txt_categoryCode;
    
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnClose;
    private JButton btnDelete;
    
    private static JDialog dlgCategory;
    private CategoryListTable categoryListTable;
    private CategoryDAO categoryService;
    private Category productCategory;
    
    public CategoryDialog()
    {
        AccountsManagement.logger.info("Creating Product Category UI...");        
        
        dlgCategory= new JDialog((JDialog)null, "Product Categories", true);
        dlgCategory.setLayout(null);
        dlgCategory.setSize(500, 400);//Width size, Height size
        dlgCategory.setLocationRelativeTo(null);//center the invoice on the screen
        
        
        lbl_title=new JLabel();
        lbl_title.setBounds(10, 20, 200, 25);         
        lbl_title.setText("Product Category Details");        
        dlgCategory.add(lbl_title); 
        
        lbl_categoryCode=new JLabel();
        lbl_categoryCode.setBounds(10, 50, 150, 25);         
        lbl_categoryCode.setText("Category Code:");        
        dlgCategory.add(lbl_categoryCode);
        
        txt_categoryCode=new JTextField();
        txt_categoryCode.setBounds(150, 50, 150, 25);         
        txt_categoryCode.setText("");        
        dlgCategory.add(txt_categoryCode);
        
        lbl_categoryName=new JLabel();
        lbl_categoryName.setBounds(10, 80, 150, 25);         
        lbl_categoryName.setText("Category Name:");        
        dlgCategory.add(lbl_categoryName);
        
        txt_categoryName=new JTextField();
        txt_categoryName.setBounds(150, 80, 200, 25);         
        txt_categoryName.setText("");        
        dlgCategory.add(txt_categoryName);
        
        
        btnSave=new JButton("Add Category");
        btnSave.setBounds(10, 120, 110, 25);
        btnSave.setActionCommand(ACT_SAVE);
        btnSave.addActionListener(this);
        dlgCategory.add(btnSave);
        
        btnUpdate=new JButton("Update");
        btnUpdate.setBounds(130, 120, 90, 25);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        dlgCategory.add(btnUpdate);
        
        btnDelete=new JButton("Delete");
        btnDelete.setBounds(230, 120, 90, 25);
        btnDelete.setActionCommand(ACT_DELETE);
        btnDelete.addActionListener(this);
        btnDelete.setEnabled(false);
        dlgCategory.add(btnDelete);
        
        btnClose=new JButton("Back");
        btnClose.setBounds(330, 120, 90, 25);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        dlgCategory.add(btnClose);
        
        lbl_title2=new JLabel();
        lbl_title2.setBounds(10, 150, 200, 25);         
        lbl_title2.setText("Available Categories: ");        
        dlgCategory.add(lbl_title2); 
        
        //Category List Table
        categoryListTable=new CategoryListTable();
        categoryListTable.setBounds(20,180,450, 150);
        dlgCategory.add(categoryListTable);
        categoryListTable.insertRow();
        
        dlgCategory.setVisible(true);          
        dlgCategory.dispose(); //close the app once done
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgCategory.setVisible(false);
            return;
	}
       
        else if(e.getActionCommand().equals(ACT_SAVE))
        {
            if(btnSave.getText().equalsIgnoreCase("Add Category"))
            {
               //clear the text boxes to allow addtion of new category record
               btnSave.setText("Save Category"); 
               btnUpdate.setText("Cancel");
               btnDelete.setEnabled(false);
               txt_categoryName.setText("");
               txt_categoryCode.setText("");
               return;
            }
            else  if(btnSave.getText().equalsIgnoreCase("Save Category"))
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
                categoryService=new CategoryDAO();
                productCategory.setCategoryCode(txt_categoryCode.getText());
                productCategory.setCategoryName(txt_categoryName.getText());
                try {
                    categoryService.saveCategory(productCategory);
                    categoryListTable.insertRow(productCategory);
                } catch (SQLException ex) {
                    Logger.getLogger(CategoryDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                btnSave.setText("Add Category"); 
                btnUpdate.setText("Update");
                btnDelete.setEnabled(true);
                return;
            }
        }
        else if(e.getActionCommand().equals(ACT_DELETE))
        {
            return;
        }
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            return; 
        }
        
        
    }
    
     public static void createAndShowGUI()
     {
         
         AccountsManagement.logger.info("Loading Category Dialog");
         CategoryDialog categoryDialog = new CategoryDialog();
         
         
     }
    
}
