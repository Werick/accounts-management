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
import java.util.Date;
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
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.api.PurchasesService;
import org.lown.consultancy.accounts.api.SalesService;

/**
 *
 * @author LENOVO USER
 */
public class PurchasesTransactions extends JPanel{
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    JTable jTable;
    String[] columnTitle=new String[]{"Invoice Id","Date","Due Date","Amount","Paid"};
    private static DefaultTableModel model ;
    private Object[][] data;
    private List<Purchase> transactions;    
    private SalesService ss;
    private PurchasesService ps;
    public static Purchase selectedTx;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private JCheckBox checkBox = new JCheckBox();
    
    public PurchasesTransactions()
    {
        AccountsManagement.logger.info("Creating Purchases Transactions Table UI...");
        ss=new SalesService();
        ps=new PurchasesService();
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false; //Disallow the editing of any cell
            }
        };
        jTable.setSize(700, 300); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(400, 200));       
        jTable.setPreferredScrollableViewportSize(new Dimension(500, 250));
        
       
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //Identifier column
        column.setPreferredWidth(100);
                       
        
        column = jTable.getColumnModel().getColumn(1); //Name column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(2); //County column
        column.setPreferredWidth(100);
        
        
        column = jTable.getColumnModel().getColumn(4); //County column
        column.setPreferredWidth(50);        
        column.setCellRenderer(new PurchasesTransactions.CheckBoxEditorRenderer());
        column.setCellEditor(new DefaultCellEditor(checkBox));
        
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);//date
        jTable.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);//due date
        jTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);//Invoice id
        jTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);//Amount
        
        jTable.addMouseListener(new MouseAdapter(){
            @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (e.getClickCount() == 1)// use 2 for a double click use 1 for a single click
                        {
                            System.out.println(" Single click" );
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
                                System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                                System.out.println(String.format("Selected Row in view:  " + data.toString()));
                            
                                //selectedSupplier=ps.getCustomerByNumber(data.toString()); 
                                
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
     
    public void insertRow(Supplier s)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        transactions = null;
        if(ps.getPurchasesBySupplierId(s.getSupplier_id())!=null)
        {
            transactions=ps.getPurchasesBySupplierId(s.getSupplier_id());
        }
            
            if(!transactions.isEmpty())
            {
                for (Purchase tx:transactions)
                {
                    System.out.println("Testing Stuff"+s.getSupplierNumber());
                    Boolean paid=new Boolean(tx.isPaid());
                    model.insertRow(jTable.getRowCount(),new Object[]{tx.getInvoiceNumber(),dateFormat.format(tx.getDate()),dateFormat.format(tx.getDueDate()),df.format(tx.getAmount()),paid});
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
    
    public void insertRow(Supplier s, Date startDate,Date endDate)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        transactions = null;
        if(ps.getPurchasesByDates(s,startDate,endDate)!=null)
        {
            transactions=ps.getPurchasesByDates(s,startDate,endDate);
        }
            
            if(!transactions.isEmpty())
            {
                for (Purchase tx:transactions)
                {
                    System.out.println("Testing Stuff"+s.getSupplierNumber());
                    Boolean paid=new Boolean(tx.isPaid());
                    model.insertRow(jTable.getRowCount(),new Object[]{tx.getInvoiceNumber(),dateFormat.format(tx.getDate()),dateFormat.format(tx.getDueDate()),df.format(tx.getAmount()),paid});
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
