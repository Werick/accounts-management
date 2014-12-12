/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.Stock;
import org.lown.consultancy.accounts.dao.ProductService;
import org.lown.consultancy.accounts.tables.StockItemTable;

/**
 *
 * @author LENOVO USER
 */
public class StockItemDialog extends JPanel implements ActionListener{
   
    private static final String ACT_UPDATE="update_stock";    ;
    private static final String ACT_BACK="close";
    private static final String ACT_VIEW="view_product_by_category";
    
    private static JDialog dlgStockItem;
    private static StockItemTable stockListTable;
    private static ProductService productService;   
    private static Product product;
    private static Stock stock;
    private Map<String,Integer>categoryList;
    
    private JLabel lbl_search;
    private JLabel lbl_category;
    
    private JComboBox cbo_category;
    private static JTextField txt_search;
    
    private JButton btnUpdate;
    private JButton btnClose;
    
    public StockItemDialog()
    {
         AccountsManagement.logger.info("Creating Manage Stock UI...");        
         productService=new ProductService();
         categoryList=productService.getCategoryMap();
        
        dlgStockItem= new JDialog((JDialog)null, "Stock Items", true);
        dlgStockItem.setLayout(null);
        dlgStockItem.setSize(850, 650);//Width size, Height size
        dlgStockItem.setLocationRelativeTo(null);//center the invoice on the screen
        
        //Stock List Table
        stockListTable=new StockItemTable();
        stockListTable.setBounds(40,50,750, 500);
        dlgStockItem.add(stockListTable);
        stockListTable.insertRows(); 
        
        lbl_search=new JLabel();
        lbl_search.setBounds(20, 20, 400, 25);         
        lbl_search.setText("[Quick Search (Enter product Code/Name to filter the Stock Items):]"); 
        
        dlgStockItem.add(lbl_search); 
        
        txt_search=new JTextField();
        txt_search.setBounds(400, 20, 85, 25);         
        txt_search.setText(""); 
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
                if (txt_search.getText().length()>=3)
                {
//                    //Call the search method
                    stockListTable.insertRow(txt_search.getText());
                }
            }
           
        });
        dlgStockItem.add(txt_search);
        
        lbl_category=new JLabel();
        lbl_category.setBounds(520, 20, 150, 25);         
        lbl_category.setText("[Filter by Category:]"); 
        dlgStockItem.add(lbl_category); 
        
        cbo_category=new JComboBox(categoryList.keySet().toArray());
        cbo_category.setBounds(650, 20, 115, 25);         
        cbo_category.setActionCommand(ACT_VIEW);
        cbo_category.addActionListener(this);
        dlgStockItem.add(cbo_category);
        
//        cbo_category.addItem("Select");
//        cbo_category.setSelectedItem("Select");
        btnUpdate=new JButton("Update");
        btnUpdate.setBounds(450, 550, 120, 50);
        btnUpdate.setActionCommand(ACT_UPDATE);
        btnUpdate.addActionListener(this);
        dlgStockItem.add(btnUpdate);        
        
        btnClose=new JButton("Back");
        btnClose.setBounds(650, 550, 120, 50);
        btnClose.setActionCommand(ACT_BACK);
        btnClose.addActionListener(this);
        dlgStockItem.add(btnClose);
    }
  
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals(ACT_BACK))
        {
            dlgStockItem.setVisible(false);
            return;
	}
        else if(e.getActionCommand().equals(ACT_UPDATE))
        {
            stockListTable.updateStock();
	}
        else if(e.getActionCommand().equals(ACT_VIEW))
        {
            int selCategory=0;
           
            if(categoryList.get(cbo_category.getSelectedItem())!=null)
            {
                selCategory=categoryList.get(cbo_category.getSelectedItem());
            }
            stockListTable.insertRow(selCategory);
            return;
	}
    }
    
    public static void createAndShowGUI()
     {
         AccountsManagement.logger.info("Loading Stock Item Dialog");
         StockItemDialog stockItemDialog = new StockItemDialog();      
         
         dlgStockItem.setVisible(true);          
         dlgStockItem.dispose(); //close the app once done
     }
}
