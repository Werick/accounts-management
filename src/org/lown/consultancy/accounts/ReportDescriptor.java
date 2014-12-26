/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lown.consultancy.accounts;

import java.util.Date;

/**
 *
 * @author LENOVO USER
 */
public class ReportDescriptor {
    private String reportName;
    private Date transactionDate;
    private String description;
    private double crAmount;
    private double drAmount;
    private double balance;
    private String txType;

    
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public double getCrAmount() {
        return crAmount;
    }

    public void setCrAmount(double crAmount) {
        this.crAmount = crAmount;
    }

    public double getDrAmount() {
        return drAmount;
    }

    public void setDrAmount(double drAmount) {
        this.drAmount = drAmount;
    }
    
}
