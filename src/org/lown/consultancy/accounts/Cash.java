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
public class Cash {
    private Customer customer;
    private Date date;
    private String txType; //Credit or Debit
    private double amount;
    private double prepayment;
    private String account; //Bank Account or cash Account
    private int cash_tx_id;
    private int txCode; //transaction code
    private String chequeNumber;
    private SalesTransaction salesTransaction;
    private Purchase purchase;
    
    
    
    public SalesTransaction getSalesTransaction() {
        return salesTransaction;
    }

    public void setSalesTransaction(SalesTransaction salesTransaction) {
        this.salesTransaction = salesTransaction;
    }
        
    public double getPrepayment() {
        return prepayment;
    }

    public void setPrepayment(double prepayment) {
        this.prepayment = prepayment;
    }    
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCash_tx_id() {
        return cash_tx_id;
    }

    public void setCash_tx_id(int cash_tx_id) {
        this.cash_tx_id = cash_tx_id;
    }

    public int getTxCode() {
        return txCode;
    }

    public void setTxCode(int txCode) {
        this.txCode = txCode;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }
    
    
    
}
