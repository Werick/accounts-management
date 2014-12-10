/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.tables;

import java.awt.Component;
import java.awt.Dimension;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.lown.consultancy.accounts.Stock;
import org.lown.consultancy.accounts.api.ProductService;
import org.lown.consultancy.accounts.api.StockService;

/**
 *
 * @author LENOVO USER
 */
public class StockItemTable extends JPanel{
    JTable jTable;
    String[] columnTitle=new String[]{"Product Code","Category","Description","Avail. Qty","Re-order Level","Sell Price","Buy Price", "Has VAT"};
    private static DefaultTableModel model ;
    private Object[][] data;
    private List<Stock> stockList; 
    private List<Stock> updateStockList;
    private ProductService ps;
    private StockService ss;
    public static Stock selectedStockItem;
    public static int selectedRowIndex;
    
    
    private static DecimalFormat df = new DecimalFormat("#0.00");
    private JCheckBox checkBox = new JCheckBox();
    public StockItemTable()
    {
        AccountsManagement.logger.info("Creating Stock Item List Table UI...");
        ps=new ProductService();
        ss=new StockService();
        //productList=ps.getAllProducts();
        
        model = new DefaultTableModel(data,columnTitle);
        jTable = new JTable(model)/*{
            public boolean isCellEditable(int rowIndex, int colIndex) {
            return false;//((colIndex==0)&&(colIndex==1)&&(colIndex==2)&&(colIndex==3));
            }
        }*/;
        jTable.setSize(700, 450); 
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable.setRowHeight(25);
        jTable.setBorder(BorderFactory.createTitledBorder(""));
        
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        JTableHeader header = jTable.getTableHeader();
        getSelectedRow();
        setPreferredSize(new Dimension(700, 450));       
        jTable.setPreferredScrollableViewportSize(new Dimension(700, 450));//determines the width and height of your table
        
        //set coulmun size
        TableColumn column = null;
        column = jTable.getColumnModel().getColumn(0); //code column
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(1); //category
        column.setPreferredWidth(100);
        
        column = jTable.getColumnModel().getColumn(2); //Name column
        column.setPreferredWidth(200);
        
        column = jTable.getColumnModel().getColumn(7); //Has VAT column
        column.setPreferredWidth(50);
        column.setCellRenderer(new StockItemTable.CheckBoxEditorRenderer());
        column.setCellEditor(new DefaultCellEditor(checkBox)); 
        
        //set cell alignment
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        //jTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);//Code
        jTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);//Qty
        jTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);//Re-order level
        jTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);//Sell price
        jTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);//Buy price
       
        add(scrollPane);
        add(header);
        validate();
    }
    
    public int getRowCount()
    {
        return jTable.getRowCount();
    }
    
    public void removeAllRows()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();  
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
                            Object sp = model.getValueAt(selectedRow, 6);
                            Object data = model.getValueAt(selectedRow, 6);
                            Object vat = model.getValueAt(selectedRow, 7);
                            Object bp = model.getValueAt(selectedRow, 5);
                            Object rl = model.getValueAt(selectedRow, 4);
                            int modelRow = jTable.convertRowIndexToModel(selectedRow);
                            System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                            System.out.println(String.format("Selected Row in view:  " + data.toString()));
                           
//                            selectedAmount=Double.parseDouble(data.toString());     
//                            selectedVat=Double.parseDouble(vat.toString()); 
//                            selectedNet=Double.parseDouble(net.toString());     
                            
                       }
                   }
                   
               });
    }
    
    public void insertRows()
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        stockList=ss.getAllStockItems();
        //list all available product categories from the database
        if(!stockList.isEmpty())
         {
            for (Stock s:stockList)
            {
                //System.out.println("Testing Stuff"+p.getCustomerNumber());
                s.setProduct(ss.getProductByStockId(s.getStock_id()));
                model.insertRow(jTable.getRowCount(),new Object[]{s.getProduct().getProductCode(),s.getProduct().getCategory().getCategoryName(),s.getProduct().getProductName(),s.getQuantity(),s.getReorderLevel(),df.format(s.getSellingPrice()),df.format(s.getBuyingPrice()),s.isHasVat()});
             }
          }

    }
    
    public void insertRow(String search)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        stockList=ss.getStockItemsByName(search);
        //list all available product categories from the database
        if(!stockList.isEmpty())
         {
            for (Stock s:stockList)
            {
                //System.out.println("Testing Stuff"+p.getCustomerNumber());
                s.setProduct(ss.getProductByStockId(s.getStock_id()));
                model.insertRow(jTable.getRowCount(),new Object[]{s.getProduct().getProductCode(),s.getProduct().getCategory().getCategoryName(),s.getProduct().getProductName(),s.getQuantity(),s.getReorderLevel(),df.format(s.getSellingPrice()),df.format(s.getBuyingPrice()),s.isHasVat()});
             }
          }

    }
    
    public void insertRow(int search)
    {
        //remove all rows      
        model.getDataVector().removeAllElements();
        jTable.repaint();   
        stockList=ss.getStockItemsByCategory(search);
        //list all available product categories from the database
        if(!stockList.isEmpty())
         {
            for (Stock s:stockList)
            {
                //System.out.println("Testing Stuff"+p.getCustomerNumber());
                s.setProduct(ss.getProductByStockId(s.getStock_id()));
                model.insertRow(jTable.getRowCount(),new Object[]{s.getProduct().getProductCode(),s.getProduct().getCategory().getCategoryName(),s.getProduct().getProductName(),s.getQuantity(),s.getReorderLevel(),df.format(s.getSellingPrice()),df.format(s.getBuyingPrice()),s.isHasVat()});
             }
          }

    }
    
    public void updateStock()
    {
        updateStockList=new ArrayList<Stock>();
        for(int i=0;i<getRowCount();i++)
        {
            int selectedRow=i;
            if ((selectedRow>=0)&&(jTable.getRowCount()>selectedRow))
            {
                Stock stock=new Stock();
                stock=stockList.get(i);
                Object sp = model.getValueAt(selectedRow, 5);
                Object data = model.getValueAt(selectedRow, 5);
                Object vat = model.getValueAt(selectedRow, 7);
                Object bp = model.getValueAt(selectedRow, 6);
                Object rl = model.getValueAt(selectedRow, 4);
                int modelRow = jTable.convertRowIndexToModel(selectedRow);
                System.out.println(String.format("Selected Row in view: %d. " + "Selected Row in model: %d.", selectedRow, modelRow));
                System.out.println(String.format("Selected Row in view:  " + data.toString()));

                stock.setBuyingPrice(Double.parseDouble(bp.toString()));
                stock.setSellingPrice(Double.parseDouble(sp.toString()));
                stock.setHasVat(Boolean.parseBoolean(vat.toString()));
                stock.setReorderLevel(Integer.parseInt(rl.toString()));   
                
                updateStockList.add(stock);
            }
        }
        //Persist the updated stock List
        if(!updateStockList.isEmpty())
        {
            try {
                ss.updateStockItem(updateStockList);
            } catch (SQLException ex) {
                Logger.getLogger(StockItemTable.class.getName()).log(Level.SEVERE, null, ex);
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
