package com.hhaidar.accountsmicroservice.accounts.impl;


import com.hhaidar.accountsmicroservice.accounts.constants.AccountsConstants;
import com.hhaidar.accountsmicroservice.accounts.dto.AccountsDto;
import com.hhaidar.accountsmicroservice.accounts.dto.CustomerDto;
import com.hhaidar.accountsmicroservice.accounts.exception.CustomerAlreadyExistsException;
import com.hhaidar.accountsmicroservice.accounts.exception.ResourceNotFoundException;
import com.hhaidar.accountsmicroservice.accounts.mapper.AccountsMapper;
import com.hhaidar.accountsmicroservice.accounts.mapper.CustomerMapper;
import com.hhaidar.accountsmicroservice.accounts.model.Account;
import com.hhaidar.accountsmicroservice.accounts.model.Customer;
import com.hhaidar.accountsmicroservice.accounts.repo.AccountsRepository;
import com.hhaidar.accountsmicroservice.accounts.repo.CustomerRepository;
import com.hhaidar.accountsmicroservice.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountsService {
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    /**
     * @param customerDto CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Optional<Customer> checkCust = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(checkCust.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given number : "+customerDto.getMobileNumber());
        }
        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Customer savedCustomer = customerRepository.save(customer);
        Account newAccount = createNewAccount((customer));
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anonymous");
        accountsRepository.save(newAccount);

    }

    /**
     * @param customer - Customer Object
     * @return the new account details
     *
     */
    private Account createNewAccount(Customer customer){
        Account newAccount =new Account();
        newAccount.setCustomerId((customer.getCustomerId()));
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType((AccountsConstants.SAVINGS));
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return  newAccount;
    }
    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Account accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Account accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }



}
