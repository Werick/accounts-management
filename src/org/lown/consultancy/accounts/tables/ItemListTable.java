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
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.SalesItem;
import org.lown.consultancy.accounts.api.ProductService;

/**
 *
 * @author LENOVO USER
 */
public class ItemListTable extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"Qty","Code","Description","Unit Price","Discount","Amount"};
    private static DefaultTableModel model ;
    private Object[][] data;
    private List<SalesItem> SalesItemList;
    private ProductService ps;
    
    public static double selectedAmount;
    public static double selectedDiscount;
    public static int selectedRowIndex;
    private static DecimalFormat df = new DecimalFormat("#0.00");
    
    
    public ItemListTable()
    {
        AccountsManagement.logger.info("Creating Items List Table UI...");
        
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model){
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false; //Disallow the editing of any cell
            }
        };
        jTable.setSize(500, 300); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(600, 250));       
        jTable.setPreferredScrollableViewportSize(new Dimension(600, 250));
        
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //Qty column
        column.setPreferredWidth(25);
        
        column = jTable.getColumnModel().getColumn(1); //Category column
        column.setPreferredWidth(50);
        
        column = jTable.getColumnModel().getColumn(2); //Description column
        column.setPreferredWidth(150);
        
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);//Qty
        jTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);//net amount
        jTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);//net amount
        jTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);//net amount
       
        add(scrollPane);
        add(header);
        validate();
        
    }
    
    public int getRowCount()
    {
        return jTable.getRowCount();
    }
    public void insertRow(SalesItem salesItem)
    {
        //remove all rows      
        //model.getDataVector().removeAllElements();
        jTable.repaint();   
        //Insert Item in the List
        if(salesItem!=null)
         {
            //System.out.println("Testing Stuff"+p.getCustomerNumber());            
            model.insertRow(jTable.getRowCount(),new Object[]{salesItem.getQuantity(),salesItem.getProduct().getProductCode(),salesItem.getProduct().getProductName(),df.format(salesItem.getSellPrice()),df.format(salesItem.getDiscount()),df.format(salesItem.getAmount())});
             
          }

    }
    public void removeAllRows()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();  
    }
    public List<SalesItem> getSalesItemList()
    {
        SalesItemList=new ArrayList<SalesItem>();
        SalesItem salesItem;
        System.out.println("Total Rows: " + jTable.getRowCount());//comment if not debugging
        for (int i=0;i<jTable.getRowCount();i++)
        {
            System.out.println("Current Rows  Number : " + i);//comment if not debugging
            Object qty=model.getValueAt(i, 0);//quantity
            System.out.println("Current Qty : " + qty);//comment if not debugging
            Object code=model.getValueAt(i, 1);//product code
            Object sp=model.getValueAt(i, 3);//Unit price
            Object discount=model.getValueAt(i, 4);//discount awarded
            Object amt=model.getValueAt(i, 5);//transaction amount
            
            ps=new ProductService();
            Product product=ps.getProductByCode(code.toString());
            salesItem=new SalesItem();
            salesItem.setQuantity(Integer.parseInt(qty.toString()));
            salesItem.setAmount(Double.parseDouble(amt.toString()));
            salesItem.setSellPrice(Double.parseDouble(sp.toString()));
            salesItem.setDiscount(Double.parseDouble(discount.toString()));
            salesItem.setProduct(product);
            SalesItemList.add(salesItem);
        }
        
        return SalesItemList;
    }
    private void getSelectedRow()
    {
     //  JOptionPane.showMessageDialog(null, "Selected Row is: "+jTable.getSelectedRow());
       jTable.getSelectionModel().addListSelectionListener(
               new ListSelectionListener(){
                   public void valueChanged(ListSelectionEvent e)
                   {
                       int selectedRow=jTable.getSelectedRow();
                       selectedRowIndex=selectedRow;
                       if ((selectedRow>=0)&&(jTable.getRowCount()>selectedRow))
                       {
                            Object data = model.getValueAt(selectedRow, 5);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                           
                            selectedAmount=Double.parseDouble(data.toString());   
                            
                            data = model.getValueAt(selectedRow, 4);
                            selectedDiscount=Double.parseDouble(data.toString());  
                            
                            
                       }
                   }
                   
               });
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
