/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.ReportDescriptor;

/**
 *
 * @author LENOVO USER
 */
public class ExcelExport {
    private static final String filename = "customer_statement.xls";    
    private static final String statementfile = "customer_statement";
    private static final String workingDir = System.getProperty("user.dir");    
    private static final SimpleDateFormat datef = new SimpleDateFormat("dd-MMM-yyyy");  
    private static DecimalFormat df = new DecimalFormat("#0.00");
    
    private String finalfile = "";
    private Customer customer;
    private static Workbook workbook;
    private List<ReportDescriptor> report;
       
    
    public ExcelExport()
    {
        
    }
    
    public void exportCustomer()
    {
        try
        {
            finalfile = workingDir + "\\" + filename; 
            FileInputStream file = new FileInputStream(new File(finalfile));
            workbook = new HSSFWorkbook(file);
            Sheet sheet = workbook.getSheet("statement");
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip the header row 1.
        }
        catch(IOException ex)
        {
            Logger.getLogger(InvoicePrinter.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ReportDescriptor> getReport() {
        return report;
    }

    public void setReport(List<ReportDescriptor> report) {
        this.report = report;
    }
    
}
