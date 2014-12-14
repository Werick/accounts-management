/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.text.DecimalFormat;
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
import org.lown.consultancy.accounts.dao.CashDAO;
import org.lown.consultancy.accounts.dao.CompanyDAO;

/**
 *
 * @author LENOVO USER
 */
public class ExpensesList extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"Date","Ref","Description","Contra Account","Amount"};
    private static DefaultTableModel model ;
    private Object[][] data; 
    private CompanyDAO cs;
    private CashDAO cashDAO;
    private List<ContraAccount> contraList;
    private List<ContraExpenses> expensesList;
    public static ContraAccount selectedAccount;
    public static ContraExpenses selectedExpense;
    private DecimalFormat df = new DecimalFormat("#0.00");
    public static double selectedAmount;
    
    public ExpensesList()
    {
         model = new DefaultTableModel(data,columnTitle);
         cs=new CompanyDAO();
         cashDAO=new CashDAO();
         expensesList=cashDAO.getAllExpenses();
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
        column = jTable.getColumnModel().getColumn(2); //Id
        column.setPreferredWidth(150);
        
        column = jTable.getColumnModel().getColumn(3); //Code column
        column.setPreferredWidth(150);
        
        column = jTable.getColumnModel().getColumn(4); //Name column
        column.setPreferredWidth(100);
        
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);//amount
       
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
                            Object data = model.getValueAt(selectedRow, 4);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                            
                            //selectedAccount=cs.getContraAccountByName(data.toString())   ;  
                            selectedAmount=Double.parseDouble(data.toString());
                            
                       }
                       else
                       {
                           selectedAmount=0.0;
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
        if(!expensesList.isEmpty())
         {
            for (ContraExpenses c:expensesList)
            {
                //System.out.println("Testing Stuff"+c.getCustomerNumber());
                ContraAccount acc=cs.getContraAccountById(c.getAccount().getContra_id());
                c.setAccount(acc);
                String reff="PCV"+c.getPcv_id();
                model.insertRow(jTable.getRowCount(),new Object[]{c.getExpenseDate(),reff, c.getDescription(),c.getAccount().getContraName(),df.format(c.getAmount())});
             }
          }

    }
    
}
