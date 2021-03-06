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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
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
import org.lown.consultancy.accounts.Product;
import org.lown.consultancy.accounts.Purchase;
import org.lown.consultancy.accounts.Supplier;
import org.lown.consultancy.accounts.dao.ProductDAO;
import org.lown.consultancy.accounts.dao.PurchasesDAO;
import org.lown.consultancy.accounts.dialog.PurchasesDialog;

/**
 *
 * @author LENOVO USER
 */
public class PurchasesList extends JPanel{
    private JTable jTable;
    private String[] columnTitle=new String[]{"Qty","Code","Description","Unit Price","Net Price","VAT","Amount"};
    private static DefaultTableModel model ;
    private Object[][] data; 
    private PurchasesDAO ps;
    private List<Product> productList;
    public static Product selectedProduct;
    private List<Purchase> purchasesItemList;
    public static List<Purchase> purchasesItemList2;
    private static DecimalFormat df = new DecimalFormat("#0.00");
    public static double selectedAmount,selectedVat,selectedNet;
    public static String selectedInvoice;
    public static int selectedRowIndex;
    
    private Map<String,Integer>categoryList;    
    private Map<String,Integer>salesRepList;
    private ProductDAO productService; 
    
    public PurchasesList()
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
        
        productService=new ProductDAO();
        categoryList=productService.getCategoryMap();
        
        
        
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
       // column.setCellEditor(new MyComboBoxEditor(categoryList.keySet().toArray()));
       // column.setCellRenderer(new MyComboBoxRenderer(categoryList.keySet().toArray()));
        
        column = jTable.getColumnModel().getColumn(2); //Description column
        column.setPreferredWidth(150);
        
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);//Qty
        jTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);//unit price
        jTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);//net amount
        jTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);//vat
        jTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);//Amount
       
        //Mouse listener to track a double click
        jTable.addMouseListener(new MouseAdapter(){
            @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        if (e.getClickCount() == 2)// use 2 for a double click use 1 for a single click
                        {
                            System.out.println(" Double click" );
                            PurchasesDialog.createAndShowGUI(SupplierList.selectedSupplier);
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
    
    public void displayInvoice(Supplier s, String Invoicenum)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        ps=new PurchasesDAO();
        purchasesItemList2 = null;
        if(ps.getPurchasesBySupplierId(s.getSupplier_id(), Invoicenum) !=null)
        {
            purchasesItemList2=ps.getPurchasesBySupplierId(s.getSupplier_id(), Invoicenum);
        }
        
        if (!purchasesItemList2.isEmpty())
        {
            for(Purchase purchaseItem:purchasesItemList2)
            {
                //Insert Item in the List
                if(purchaseItem!=null)
                {
                    purchaseItem.setProduct(productService.getProductById(purchaseItem.getProduct().getProduct_id()));
                    System.out.println("Invoice Number "+purchaseItem.getInvoiceNumber());            
                    model.insertRow(jTable.getRowCount(),new Object[]{purchaseItem.getQty(),purchaseItem.getProduct().getProductCode(),purchaseItem.getProduct().getProductName(),df.format(purchaseItem.getUnitPrice()) ,df.format(purchaseItem.getNetAmount()),df.format(purchaseItem.getVat()),df.format(purchaseItem.getAmount())});
                    //model.isCellEditable(jTable.getRowCount(), 6)=isCellEditable(jTable.getRowCount(), 6); 
                }
            }
        }
        

    }
    
    public void insertRow(Purchase purchaseItem)
    {
        //remove all rows      
        //model.getDataVector().removeAllElements();
        jTable.repaint();   
        //Insert Item in the List
        if(purchaseItem!=null)
         {
            //System.out.println("Testing Stuff"+p.getCustomerNumber());            
            model.insertRow(jTable.getRowCount(),new Object[]{purchaseItem.getQty(),purchaseItem.getProduct().getProductCode(),purchaseItem.getProduct().getProductName(),df.format(purchaseItem.getUnitPrice()) ,df.format(purchaseItem.getNetAmount()),df.format(purchaseItem.getVat()),df.format(purchaseItem.getAmount())});
            //model.isCellEditable(jTable.getRowCount(), 6)=isCellEditable(jTable.getRowCount(), 6); 
          }

    }
    public void removeAllRows()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();  
    }
    public List<Purchase> getPurchasesItemList()
    {
        purchasesItemList=new ArrayList<Purchase>();
        Purchase purchaseItem;
        System.out.println("Total Rows: " + jTable.getRowCount());//comment if not debugging
        for (int i=0;i<jTable.getRowCount();i++)
        {
            System.out.println("Current Rows  Number : " + i);//comment if not debugging
            Object qty=model.getValueAt(i, 0);//quantity
            Object code=model.getValueAt(i, 1);//product code
            Object unitprice=model.getValueAt(i, 3);// unit price
            Object netAmt=model.getValueAt(i, 4);//net price
            Object vat=model.getValueAt(i, 5);//Vat awarded
            Object amt=model.getValueAt(i, 6);// amount
            
            productService=new ProductDAO();
            Product product=productService.getProductByCode(code.toString());
            purchaseItem=new Purchase();
            purchaseItem.setQty(Integer.parseInt(qty.toString()));
            purchaseItem.setUnitPrice(Double.parseDouble(unitprice.toString()));
            purchaseItem.setAmount(Double.parseDouble(amt.toString()));
            purchaseItem.setNetAmount(Double.parseDouble(netAmt.toString()));
            purchaseItem.setVat(Double.parseDouble(vat.toString()));
            purchaseItem.setProduct(product);
            purchasesItemList.add(purchaseItem);
        }
        
        return purchasesItemList;
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
                            Object invoicenum = model.getValueAt(selectedRow, 0);
                            Object vat = model.getValueAt(selectedRow, 4);
                            Object net = model.getValueAt(selectedRow, 3);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                           
                            selectedAmount=Double.parseDouble(data.toString());     
                            selectedVat=Double.parseDouble(vat.toString()); 
                            selectedNet=Double.parseDouble(net.toString());     
                            selectedInvoice=invoicenum.toString();
                       }
                       else
                       {
                           selectedAmount=0.0;
                           selectedVat=0.0;
                           selectedNet=0.0;
                           
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
    
class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
  public MyComboBoxRenderer(Object[] items) {
    super(items);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setSelectedItem(value);
    return this;
  }
}

class MyComboBoxEditor extends DefaultCellEditor {
  public MyComboBoxEditor(Object[] items) {
    super(new JComboBox(items));
  }
}    
}
