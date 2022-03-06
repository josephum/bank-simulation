package com.example.bank.service.impl;

import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.enums.AccountType;
import com.example.bank.exception.AccountOwnerException;
import com.example.bank.exception.BadRequestException;
import com.example.bank.exception.BalanceNotSufficientException;
import com.example.bank.exception.UnderConstructionException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.representer.BaseRepresenter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class TransactionServiceImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean underConstruction;

    AccountRepository accountRepository;
    TransactionRepository transactionRepository;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public Transaction makeTransfer(BigDecimal amount, Date creationDate, Account sender, Account receiver, String message) {
        if (!underConstruction){
        checkAccountOwner(sender,receiver);
        validateAccount(sender,receiver);
        executeBalanceAndUpdateIfRequired(amount,sender,receiver);
        return transactionRepository.save(Transaction.builder()
                .amount(amount)
                .creationDate(creationDate)
                .sender(sender.getId())
                .receiver(receiver.getId())
                .message(message).build());
        } else {
            throw new UnderConstructionException("Make transfer is not possible for now. Please try again later");
        }
    }


    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, Account sender, Account receiver) {
        if(checkSenderBalance(sender,amount)){
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(amount));
        } else {
            throw new BalanceNotSufficientException("Balance is not enough for this transaction");
        }
    }

    private boolean checkSenderBalance(Account sender, BigDecimal amount) {
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) > 0;

    }

    private void validateAccount(Account sender, Account receiver){
        if (sender==null || receiver==null){
            throw new BadRequestException("Sender or receiver can not be null");
        }
        if (sender.getId().equals(receiver.getId())){
            throw new BadRequestException("Sender account needs to be different from receiver account");
        }
        findAccountById(sender.getId());
        findAccountById(receiver.getId());
    }

    private Account findAccountById(UUID accountId) {
        return AccountRepository.findById(accountId);
    }

    private void checkAccountOwner(Account sender, Account receiver)  {
        if ((sender.getAccountType().equals(AccountType.SAVINGS) || receiver.getAccountType().equals(AccountType.SAVINGS))
        && sender.getUserId().equals(receiver.getUserId())){
            throw new AccountOwnerException("When one of the account type is SAVINGS, sender and receiver have to be the same person");
        }

    }

    @Override
    public List<Transaction> findAll() {
        return TransactionRepository.findAll();
    }
}
