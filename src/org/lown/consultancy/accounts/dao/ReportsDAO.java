/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.lown.consultancy.accounts.AccountsManagement;
import org.lown.consultancy.accounts.Customer;
import org.lown.consultancy.accounts.ReportDescriptor;
import org.lown.consultancy.accounts.Sql;

/**
 *
 * @author LENOVO USER
 */
public class ReportsDAO {
    
    
//public List<ReportDescriptor> getSupplierStatement(Supplier s) 
//{
//    select *
//from
//(
//select x.pdate, x.supplier_id,x.invoicenum,x.dramount, 0 as cramount , 'Sales'
//from(
//select pdate, supplier_id,invoicenum,sum(amount) as dramount
//from purchases p
//WHERE voided=0
//group by invoicenum)x
//union
//select c.tx_date, x.supplier_id,x.invoicenum,0,c.amount+c.prepayment as dramount,'payment'
//from(
//select pdate, supplier_id ,invoicenum,0,sum(amount) as cramount
//from purchases p
//where voided=0
//group by invoicenum)x
//join cash_bank c on c.invoicenum=x.invoicenum and c.voided=0)y
//order by pdate
//}

public List<ReportDescriptor> getPettyCashReport() 
    {
        List<ReportDescriptor> pettyCashList=new ArrayList<ReportDescriptor>();
        try
        {
            //log info
            AccountsManagement.logger.info("Create Petty Cash Report... ");
            String sqlStmt="SELECT tx_date,tx_type, description,if(tx_type='CR',amount,null) as cramount, ";
                    sqlStmt+="if(tx_type='DR',amount,null) as dramount ";
                    sqlStmt+="FROM cash_bank c ";
                    sqlStmt+="where account_tx_id=6 and voided=0; ";
                    
                    
           // Open();
           // Statement statement = c.createStatement();
            double curBalance=0.0;
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                ReportDescriptor report=new ReportDescriptor();
                report.setTransactionDate(rs.getDate("tx_date"));
                report.setDescription(rs.getString("description"));
                report.setTxType(rs.getString("tx_type"));
                report.setCrAmount(rs.getDouble("cramount"));
                report.setDrAmount(rs.getDouble("dramount"));
                curBalance=curBalance+report.getDrAmount()-report.getCrAmount();
                report.setBalance(curBalance);                
                pettyCashList.add(report);              
            }
            
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.fillInStackTrace());
         }  
        return  pettyCashList;
    }

    public List<ReportDescriptor> getCustomerReport(Customer c) 
    {
        List<ReportDescriptor> pettyCashList=new ArrayList<ReportDescriptor>();
        try
        {
            //log info
            AccountsManagement.logger.info("Create Customer Report... ");
            String sqlStmt=" select * ";
                    sqlStmt+="from( ";
                    sqlStmt+="select t.tx_summary_id,if(t.txtype='Cash','Cash Sales','Credit Sales') as descr,t.txdate as salesdate,t.txamount as dramount, 0 as cramount, ";
                    sqlStmt+="t.paid, t.datecreated ";
                    sqlStmt+="from transactionsummary t ";
                    sqlStmt+="left outer join cash_bank c on t.tx_summary_id=c.sales_tx_id and c.voided=0 ";
                    sqlStmt+="where t.voided=0 and t.customer_id="+c.getCustomer_id();
                    sqlStmt+=" union ";
                    sqlStmt+="select c.cash_tx_id,concat(c.account,' Received') as descr,c.tx_date as paydate,0 as cramount,c.amount+c.prepayment as dramount, ";
                    sqlStmt+="t.paid,c.datecreated ";
                    sqlStmt+="from transactionsummary t ";
                    sqlStmt+="join cash_bank c on t.tx_summary_id=c.sales_tx_id and c.voided=0 ";
                    sqlStmt+="where t.voided=0 and t.customer_id="+c.getCustomer_id()+ "  )x ";
                    sqlStmt+="order by x.tx_summary_id,x.salesdate ";
                    
            double curBalance=0.0;
            ResultSet rs=Sql.executeQuery(sqlStmt);
            while (rs.next())
            {
                ReportDescriptor report=new ReportDescriptor();
                report.setTransactionDate(rs.getDate("salesdate"));
                if(!rs.getString("descr").contains("Received"))
                {
                    report.setDescription(c.getCustomerNumber()+"-"+rs.getString("tx_summary_id")+"  "+rs.getString("descr"));
                }
                else
                {
                   report.setDescription(rs.getString("descr")); 
                }
                        
                
               
                //report.setTxType(rs.getString("tx_type"));
                report.setCrAmount(rs.getDouble("cramount"));
                report.setDrAmount(rs.getDouble("dramount"));
                curBalance=curBalance+report.getDrAmount()-report.getCrAmount();
                report.setBalance(curBalance);                
                pettyCashList.add(report);              
            }
            
        }
        catch (SQLException e) 
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Logger.getLogger(Sql.class.getName()).log(Level.SEVERE, null, e);
            JOptionPane.showMessageDialog(null, e);
            AccountsManagement.logger.log(Level.SEVERE, "ERROR", e.fillInStackTrace());
         }  
        return  pettyCashList;
    }
}
