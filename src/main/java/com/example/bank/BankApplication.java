package com.example.bank;

import com.example.bank.entity.Account;
import com.example.bank.enums.AccountType;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;
import com.example.bank.service.impl.AccountServiceImpl;
import com.example.bank.service.impl.TransactionServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class BankApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BankApplication.class, args);


        AccountService accountService = context.getBean(AccountServiceImpl.class);
        TransactionService transactionService = context.getBean(TransactionServiceImpl.class);

        Account receiver = accountService.createNewAccount( BigDecimal.TEN, new Date(), AccountType.CHECKINGS, 1L);
        Account sender =  accountService.createNewAccount(new BigDecimal(70), new Date(), AccountType.CHECKINGS, 1L);

        accountService.listAllAccounts().forEach(System.out::println);

        transactionService.makeTransfer(BigDecimal.TEN, new Date(), sender, receiver, "transfer no:1");

        System.out.println(transactionService.findAll().get(0));
        accountService.listAllAccounts().forEach(System.out::println);

        transactionService.makeTransfer(new BigDecimal(25), new Date(), sender, receiver, "transfer no:2");

        System.out.println(transactionService.findAll().get(1));
        accountService.listAllAccounts().forEach(System.out::println);
    }

}
