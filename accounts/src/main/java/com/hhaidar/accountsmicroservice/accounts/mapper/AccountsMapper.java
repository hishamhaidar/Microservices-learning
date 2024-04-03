package com.hhaidar.accountsmicroservice.accounts.mapper;

import com.hhaidar.accountsmicroservice.accounts.dto.AccountsDto;
import com.hhaidar.accountsmicroservice.accounts.model.Account;

public class AccountsMapper {
    public static AccountsDto mapToAccountsDto(Account accounts, AccountsDto accountsDto) {
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        return accountsDto;
    }

    public static Account mapToAccounts(AccountsDto accountsDto, Account accounts) {
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }
}
