/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.lown.consultancy.accounts.ContraAccount;
import org.lown.consultancy.accounts.ContraExpenses;
import org.lown.consultancy.accounts.dao.CompanyService;

/**
 *
 * @author LENOVO USER
 */
public class ContraExpensesTable extends JPanel{
    
    private JTable jTable;
    private String[] columnTitle=new String[]{"Description","Contra Account","Amount"};
    private static DefaultTableModel model ;
    private Object[][] data; 
    private CompanyService cs;
    private List<ContraAccount> contraList;
    private List<ContraExpenses> expensesList;
    public static ContraAccount selectedAccount;
    public static ContraExpenses selectedExpense;
    private DecimalFormat df = new DecimalFormat("#0.00");
    
    public ContraExpensesTable()
    {
        
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
        
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);//amount
       
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
                            Object data = model.getValueAt(selectedRow, 1);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                            
                            //selectedAccount=cs.getContraAccountByName(data.toString())   ;         
                            
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
//        if(!contraList.isEmpty())
//         {
//            for (ContraAccount c:contraList)
//            {
//                //System.out.println("Testing Stuff"+c.getCustomerNumber());
//                model.insertRow(jTable.getRowCount(),new Object[]{c.getContra_id(),c.getContraName(),c.getContraDescription()});
//             }
//          }

    }
    public void insertRow(ContraExpenses c)
    {
        //remove all rows      
        //model.getDataVector().removeAllElements();
        jTable.repaint();   
         
        //list all available product categories from the database
        if(c!=null)
         {
            //System.out.println("Testing Stuff"+c.getCustomerNumber());
            model.insertRow(jTable.getRowCount(),new Object[]{c.getDescription(),c.getAccount().getContraName(),df.format(c.getAmount())});
             
          }

    }
   
    
    public List<ContraExpenses> getExpensesList()
    {
        expensesList=new ArrayList<ContraExpenses>();
        ContraExpenses expenseItem;
        System.out.println("Total Rows: " + jTable.getRowCount());//comment if not debugging
        for (int i=0;i<jTable.getRowCount();i++)
        {
            System.out.println("Current Rows  Number : " + i);//comment if not debugging
            Object descr=model.getValueAt(i, 0);//description
            Object acc=model.getValueAt(i, 1);//contra account           
            Object amt=model.getValueAt(i, 2);// amount
            
            cs=new CompanyService();
            ContraAccount account=cs.getContraAccountByName(acc.toString());
            expenseItem=new ContraExpenses();
            expenseItem.setAccount(account);            
            expenseItem.setAmount(Double.parseDouble(amt.toString()));            
            expenseItem.setDescription(descr.toString());
            expensesList.add(expenseItem);
        }
        
        return expensesList;
    }
    public void removeSelectedRow()
    {
        //remove selected row      
        int selectedRow=jTable.getSelectedRow();  
        if (selectedRow!=-1)
        {
            model.removeRow(selectedRow);
            jTable.repaint();
        }
        
    }
}
