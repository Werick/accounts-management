/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.dao.CustomerDAO;
import org.lown.consultancy.accounts.dao.SalesDAO;
import org.lown.consultancy.accounts.dialog.CustomerDashboard;


/**
 *
 * @author LENOVO USER
 */
public class CustomerListTable extends JPanel{
    JTable jTable;
    String[] columnTitle=new String[]{"Customer ID","Name","Phone","Contact Person"};
    private static DefaultTableModel model ;
    private Object[][] data;
    private List<Customer> findList;    
    private CustomerDAO cs;
    private SalesDAO ss;
    public static Customer selectedCustomer;
    
    
    public CustomerListTable()
    {
        AccountsManagement.logger.info("Creating Find Table UI...");
        cs=new CustomerDAO();
        ss=new SalesDAO();
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model) {
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
    
    public Customer getSelectedCustomer()
    {
        return selectedCustomer;
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
                            
                                selectedCustomer=cs.getCustomerByNumber(data.toString()); 
                                
                                //auto fill details if not null
                                if (selectedCustomer!=null)
                                {
                                    CustomerDashboard.txt_customerName.setText(selectedCustomer.getCustomerName());
                                    CustomerDashboard.txt_customerNumber.setText(selectedCustomer.getCustomerNumber());
                                    CustomerDashboard.txt_phone.setText(selectedCustomer.getPhone());
                                    CustomerDashboard.txt_address.setText(selectedCustomer.getAddress());
                                    CustomerDashboard.txt_contact.setText(selectedCustomer.getContactPerson()); 
                                    CustomerDashboard.txListTable.insertRow(selectedCustomer);
                    
                                    double totalSales=ss.getTotalSalesByCustomerId(selectedCustomer.getCustomer_id());
                                    double totalCash=ss.getTotalCashByCustomerId(selectedCustomer.getCustomer_id());
                                    double lastPayment=ss.getLastCustomerPayment(selectedCustomer);
                                    double balance=totalSales-totalCash;
                                    CustomerDashboard.txt_totalSales.setText(CustomerDashboard.df.format(totalSales));
                                    if (balance>=0)
                                    {
                                        CustomerDashboard.txt_balance.setText(CustomerDashboard.df.format(balance));
                                    }
                                    else
                                    {                        
                                        CustomerDashboard.txt_balance.setText("("+CustomerDashboard.df.format(balance)+")");  
                                    }
                                    CustomerDashboard.txt_lastPayment.setText(CustomerDashboard.df.format(lastPayment));
                                    System.out.println("Total Sales: "+totalSales);
                                    System.out.println("Total Cash: "+totalCash);
                                    System.out.println("Balance: "+balance);
                                }
                                else
                                {
                                    //clear the text boxes if no customer is selected
                                    CustomerDashboard.txt_customerName.setText("");
                                    CustomerDashboard.txt_customerNumber.setText("");
                                    CustomerDashboard.txt_phone.setText("");
                                    CustomerDashboard.txt_address.setText("");
                                    CustomerDashboard.txt_contact.setText("");  
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
        findList=  cs.getCustomerByName(search);
                        
        if(!findList.isEmpty())
         {
            for (Customer c:findList)
            {
                System.out.println("Testing Stuff"+c.getCustomerNumber());
                model.insertRow(jTable.getRowCount(),new Object[]{c.getCustomerNumber(),c.getCustomerName(),c.getPhone(),c.getContactPerson()});
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
