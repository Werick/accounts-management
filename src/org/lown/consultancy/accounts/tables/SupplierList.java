/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.util.List;
import java.util.logging.Level;
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
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.api.PurchasesService;
import org.lown.consultancy.accounts.api.SupplierService;
import org.lown.consultancy.accounts.dialog.SupplierDashboard;

/**
 *
 * @author LENOVO USER
 */
public class SupplierList extends JPanel{
    
    JTable jTable;
    String[] columnTitle=new String[]{"Supplier ID","Name","Phone","Contact Person"};
    private static DefaultTableModel model ;
    private Object[][] data;
    private List<Supplier> findList;    
    private SupplierService ss;
    private PurchasesService ps;
    public static Supplier selectedSupplier;
    
    public SupplierList()            
    {
        AccountsManagement.logger.info("Creating Supplier Table UI...");
        ss=new SupplierService();
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
        column.setPreferredWidth(45);
        
        column = jTable.getColumnModel().getColumn(1); //Name column
        column.setPreferredWidth(150);
        
        column = jTable.getColumnModel().getColumn(2); //County column
        column.setPreferredWidth(40);
        
        add(scrollPane);
        add(header);
        validate();  
    }
    
    public int getRowCount()
    {
        return jTable.getRowCount();
    }
    
    public Supplier getSelectedSupplier()
    {
        return selectedSupplier;
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
                            
                                selectedSupplier=ss.getSupplierByNumber(data.toString()); 
                                if(selectedSupplier!=null)
                                {
                                    //Should Load the Autofill the supplier Dash Board                    
                                    SupplierDashboard.txt_supplierName.setText(selectedSupplier.getSupplierName());
                                    SupplierDashboard.txt_supplierNumber.setText(SupplierList.selectedSupplier.getSupplierNumber());
                                    SupplierDashboard.txt_phone.setText(selectedSupplier.getPhone());
                                    SupplierDashboard.txt_address.setText(selectedSupplier.getAddress());
                                    SupplierDashboard.txt_contact.setText(selectedSupplier.getContactPerson());
                                    //SalesDialog.createAndShowGUI(CustomerListTable.selectedCustomer); 
                                    SupplierDashboard.txListTable.insertRow(selectedSupplier);
                                    try
                                    {
                                        double totalPurchases=ps.getTotalPurchasesBySupplierId(SupplierList.selectedSupplier.getSupplier_id());
                                        double totalPayments=ps.getTotalPaymentsBySupplierId(SupplierList.selectedSupplier.getSupplier_id());
                                        double balance=totalPurchases-totalPayments;
                                        double lastPayment=ps.getLastPaymentMade(selectedSupplier);
                                        SupplierDashboard.txt_totalPurchases.setText(SupplierDashboard.df.format(totalPurchases));
                                        if (balance>=0)
                                        {
                                            SupplierDashboard.txt_balance.setText(SupplierDashboard.df.format(balance));
                                        }
                                        else
                                        {                        
                                            SupplierDashboard.txt_balance.setText("("+SupplierDashboard.df.format(balance)+")");  
                                        }
                                        SupplierDashboard.txt_lastPayment.setText(SupplierDashboard.df.format(lastPayment));
                                    }
                                    catch(Exception ex)
                                    {
                                        //ignore error
                                    }
                    
                    
                 }
                                
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
     
    public void insertRow(String search)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        findList=  ss.getSupplierByName(search);
                        
        if(!findList.isEmpty())
         {
            for (Supplier c:findList)
            {
                System.out.println("Testing Stuff"+c.getSupplierNumber());
                model.insertRow(jTable.getRowCount(),new Object[]{c.getSupplierNumber(),c.getSupplierName(),c.getPhone(),c.getContactPerson()});
             }
          }

    }
    
    public void deleteRows()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint(); 
    }
    
}
