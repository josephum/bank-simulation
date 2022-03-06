package com.example.bank.service;

import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransactionService {

    Transaction makeTransfer(BigDecimal amount, Date creationDate, Account sender, Account receiver, String  message);

    List<Transaction> findAll();

}
