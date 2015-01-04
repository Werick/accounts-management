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
import org.lown.consultancy.accounts.Account;
import org.lown.consultancy.accounts.dao.CompanyDAO;

/**
 *
 * @author LENOVO USER
 */
public class AccountsTable extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"Acount Number","Account Name","Bank"};
    private static DefaultTableModel model ;
    private Object[][] data; 
    private CompanyDAO companyDAO;
    private List<Account> accountList;
    
    public static Account selectedAccount;
    
    public AccountsTable()
    {
        companyDAO=new CompanyDAO();
        accountList=companyDAO.getAllAccounts();
        
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false; //Disallow the editing of any cell
            }
        };
        jTable.setSize(500, 200); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(500, 200));       
        jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
        
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //Id
        column.setPreferredWidth(25);
        
        column = jTable.getColumnModel().getColumn(1); //Code column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(2); //Name column
        column.setPreferredWidth(100);
       
        add(scrollPane);
        add(header);
        validate();
    }
    
    public void getSelectedRow()
    {
     //  JOptionPane.showMessageDialog(null, "Selected Row is: "+jTable.getSelectedRow());
       jTable.getSelectionModel().addListSelectionListener(
               new ListSelectionListener(){
                   public void valueChanged(ListSelectionEvent e)
                   {
                       int selectedRow=jTable.getSelectedRow();
                       if ((selectedRow>=0)&&(jTable.getRowCount()>selectedRow))
                       {
                            Object data = model.getValueAt(selectedRow, 0);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                            
                            selectedAccount=companyDAO.getAccountByNumber(data.toString())   ;         
                            
                       }
                   }
                   
               });
    }
     
     public void insertRow()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available Contra Expenses from the database
        if(!accountList.isEmpty())
         {
            for (Account c:accountList)
            {
                //System.out.println("Testing Stuff"+c.getCustomerNumber());
                model.insertRow(jTable.getRowCount(),new Object[]{c.getAccount_number(),c.getAccount_name(),c.getBank_name()});
             }
          }

    }
    public void insertRow(Account c)
    {
        //remove all rows      
        //model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available product categories from the database
        if(c!=null)
         {
            //System.out.println("Testing Stuff"+c.getCustomerNumber());
            model.insertRow(jTable.getRowCount(),new Object[]{c.getAccount_number(),c.getAccount_name(),c.getBank_name()});
             
          }

    }
}
