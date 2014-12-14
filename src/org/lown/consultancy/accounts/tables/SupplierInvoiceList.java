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
import org.lown.consultancy.accounts.Purchase;
import org.lown.consultancy.accounts.SalesTransaction;
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.dao.PurchasesDAO;
import org.lown.consultancy.accounts.dao.SalesDAO;

/**
 *
 * @author LENOVO USER
 */
public class SupplierInvoiceList extends JPanel{
    
    String[] columnTitle=new String[]{"Paid","Invoice Num","Date","Due Date","Amount","Balance","Allocation"};
    static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    static JTable jTable;
    private static DefaultTableModel model ;
    private static Object[][] data;
    public static List<Purchase> transactions;  
    private static SalesDAO ss;
    private PurchasesDAO ps;
    public static Purchase selectedTx;    
    private static DecimalFormat df = new DecimalFormat("#0.00");    
    private JCheckBox checkBox = new JCheckBox();
    
    public SupplierInvoiceList()
    {
        AccountsManagement.logger.info("Creating Transactions Table UI...");
        ps=new PurchasesDAO();
        
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
        column.setCellRenderer(new SupplierInvoiceList.CheckBoxEditorRenderer());
        column.setCellEditor(new DefaultCellEditor(checkBox));               
        
        column = jTable.getColumnModel().getColumn(1); //invoice number
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(2); //date
        column.setPreferredWidth(150);
        
        column = jTable.getColumnModel().getColumn(3); //due Date
        column.setPreferredWidth(150); 
       
        
        column = jTable.getColumnModel().getColumn(4); //Amount
        column.setPreferredWidth(130);
        
        column = jTable.getColumnModel().getColumn(5); //Balance
        column.setPreferredWidth(130);
        
        column = jTable.getColumnModel().getColumn(6); //allocation
        column.setPreferredWidth(130);
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        //jTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);//Invoice num
        jTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);//date
        jTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);//due date
        jTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);//amount
        jTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);//balance
        jTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);//allocation
        
        
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
    
    public Purchase getSelectedTransaction()
    {
        return selectedTx;
    }
    
    public static void getAllocatedList()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();           
                        
        if(!transactions.isEmpty())
         {
            for (Purchase tx:transactions)
            {
                System.out.println("Testing Stuff"+tx.getSupplier().getSupplier_id());
                Boolean paid=new Boolean(tx.isPaid());                
                model.insertRow(jTable.getRowCount(),new Object[]{paid,tx.getInvoiceNumber(),dateFormat.format(tx.getDate()),dateFormat.format(tx.getDueDate()),df.format(tx.getAmount()),df.format(tx.getBalance()),df.format(tx.getAllocation())});
             }
          }

    }
    public void insertRow(Supplier s)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        transactions = null;
        if(ps.getPurchasesBySupplierId(s.getSupplier_id())!=null)
        {
            transactions=ps.getInvoiceSummaryBySupplierId(s.getSupplier_id());
        }
            
        if(!transactions.isEmpty())
        {
            for (Purchase tx:transactions)
            {
                System.out.println("Testing Stuff"+s.getSupplierNumber());
                Boolean paid=new Boolean(tx.isPaid());
                tx.setBalance(ps.getInvoiceBalance(tx));//calling with the service failed
                model.insertRow(jTable.getRowCount(),new Object[]{paid,tx.getInvoiceNumber(),dateFormat.format(tx.getDate()),dateFormat.format(tx.getDueDate()),df.format(tx.getAmount()),df.format(tx.getBalance())});
            }
        }
        try
        {
            
        }
        catch (Exception ex)
        {
            //ignore error
        }

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
