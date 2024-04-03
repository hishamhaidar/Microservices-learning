package com.hhaidar.accountsmicroservice.accounts.service;

import com.hhaidar.accountsmicroservice.accounts.dto.CustomerDto;

public interface IAccountsService {
    /**
     *
     * @param customerDto CustomerDto Object
     */

    void createAccount(CustomerDto customerDto);
/**
 *
 * @param mobileNumber - Input Mobile Number
 * @return Accounts Details based on a given mobileNumber
 */

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
