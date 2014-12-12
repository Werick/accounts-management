/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.SalesTransaction;
import org.lown.consultancy.accounts.dao.SalesService;

/**
 *
 * @author LENOVO USER
 */
public class InvoiceList extends JPanel{
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    static JTable jTable;
    String[] columnTitle=new String[]{"","Tx Id","Date","Due Date","Description","Amount","Balance","Allocation"};
    private static DefaultTableModel model ;
    private static Object[][] data;
    public static List<SalesTransaction> transactions;    
    private static SalesService ss;
    public static SalesTransaction selectedTx;
    private static DecimalFormat df = new DecimalFormat("#0.00");
    private JCheckBox checkBox = new JCheckBox();
    
    public InvoiceList()
    {
        AccountsManagement.logger.info("Creating Transactions Table UI...");
        ss=new SalesService();
        
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false; //Disallow the editing of any cell
            }
        };
        jTable.setSize(700, 300); 
        jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(400, 200));       
        jTable.setPreferredScrollableViewportSize(new Dimension(600, 250));
        
//        col1=new JCheckBox();
//        col1.setSize(20, 20);
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //checkbox
        column.setPreferredWidth(50);
        column.setCellRenderer(new CheckBoxEditorRenderer());
        column.setCellEditor(new DefaultCellEditor(checkBox));               
        
        column = jTable.getColumnModel().getColumn(1); //id
        column.setPreferredWidth(50);
        
        column = jTable.getColumnModel().getColumn(2); //date
        column.setPreferredWidth(150);
        
        column = jTable.getColumnModel().getColumn(3); //due Date
        column.setPreferredWidth(150);
        
        column = jTable.getColumnModel().getColumn(4); //desscr
        column.setPreferredWidth(130);
        
        column = jTable.getColumnModel().getColumn(5); //Amount
        column.setPreferredWidth(130);
        
        column = jTable.getColumnModel().getColumn(6); //Balance
        column.setPreferredWidth(130);
        
        column = jTable.getColumnModel().getColumn(7); //Balance
        column.setPreferredWidth(130);
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);//amount
        jTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);//balance
        jTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);//allocation
        jTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);//due date
        
        
        jTable.addMouseListener(new MouseAdapter(){
            @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (e.getClickCount() == 2)// use 2 for a double click use 1 for a single click
                        {
                            System.out.println(" Double click" );
                        }
                    }
        } );
   
        add(scrollPane);
        add(header);
        validate();          
    }
    public int getRowCount()
    {
        return jTable.getRowCount();
    }
    
    public SalesTransaction getSelectedTransaction()
    {
        return selectedTx;
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
                           try
                           {
                               Object data = model.getValueAt(selectedRow, 0);
                                int modelRow = jTable.convertRowIndexToModel(selectedRow);
                                //System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                                System.out.println(String.format("Selected Row in view:  " + data.toString()));
                                
                                //selectedCustomer=cs.getCustomerByNumber(data.toString()); 
                                
                           }
                           catch(java.lang.ArrayIndexOutOfBoundsException ex)
                           {
                               ex.printStackTrace();
                               AccountsManagement.logger.log(Level.SEVERE, "ERROR", ex);
                           }                                   
                            
                       }
                   }
                   
               });
    }
     
    public void deleteRows()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint(); 
    }
     
    public static void insertRow(Customer c)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        transactions=  ss.getPendingInvoicesByCustomerId(c.getCustomer_id());
                        
        if(!transactions.isEmpty())
         {
            for (SalesTransaction tx:transactions)
            {
                System.out.println("Testing Stuff"+c.getCustomerNumber());
                Boolean paid=new Boolean(tx.isPaid());
                tx.setCustomer(c);
                tx.setBalance(ss.getTransactionBalance(tx));
                model.insertRow(jTable.getRowCount(),new Object[]{paid,tx.getTx_summary_id(),dateFormat.format(tx.getTxSalesDate()),dateFormat.format(tx.getTxSalesDueDate()),tx.getTxType(),df.format(tx.getTxSalesAmount()),df.format(tx.getBalance())});
             }
          }

    }
    
    public static void getAllocatedList()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();           
                        
        if(!transactions.isEmpty())
         {
            for (SalesTransaction tx:transactions)
            {
                System.out.println("Testing Stuff"+tx.getCustomer().getCustomerNumber());
                Boolean paid=new Boolean(tx.isPaid());
                model.insertRow(jTable.getRowCount(),new Object[]{paid,tx.getTx_summary_id(),dateFormat.format(tx.getTxSalesDate()),dateFormat.format(tx.getTxSalesDueDate()),tx.getTxType(),df.format(tx.getTxSalesAmount()),df.format(tx.getBalance()),df.format(tx.getAllocation())});
             }
          }

    }
    
    class CheckBoxEditorRenderer extends AbstractCellEditor implements TableCellRenderer
    {
        private JCheckBox checkbox = new JCheckBox();
        public CheckBoxEditorRenderer() {
            super();
            checkbox.setFocusable(false);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Boolean) {
                checkbox.setSelected((Boolean) value);
            }
            return checkbox;
        }
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Boolean) {
                checkbox.setSelected((Boolean) value);
            }
            return checkbox;
        }
        @Override
        public Object getCellEditorValue() {
            return checkbox.isSelected();
        }
    }
    
}


