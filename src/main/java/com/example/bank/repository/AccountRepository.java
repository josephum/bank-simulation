package com.example.bank.repository;

import com.example.bank.entity.Account;
import com.example.bank.exception.RecordNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AccountRepository {
    public static List<Account> accountList = new ArrayList<>();

    public Account save(Account account){
        accountList.add(account);
        return account;
    }

    public static List<Account> findAll(){
        return accountList;
    }

    public static Account findById(UUID accountId) {
        return accountList.stream().filter(account -> account.getId().equals(accountId)).findAny()
                .orElseThrow(()->new RecordNotFoundException("This account is not is the database"));
    }
}
