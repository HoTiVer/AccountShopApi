package gleb.dresher.AccountShopApi.service;

import gleb.dresher.AccountShopApi.dto.AccountDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final SellerRepository sellerRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(int id) {
        return accountRepository.customIdSearch(id);
    }

    public List<Account> getExpensiveAccounts(double price) {
        return accountRepository.findByPriceGreaterThan(price);
    }

    public List<Account> getCheapAccounts(double price) {
        return accountRepository.findByPriceLessThan(price);
    }

    public List<Account> getPriceDiaposone(double min, double max) {
        return accountRepository.findByPriceBetween(min, max);
    }

    public List<Account> getAccountsByType(String type){
        try {
            type = type.toUpperCase();
            return accountRepository.findByAccountType(type);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account type", e);
        }
    }

    public Account addAccount(@NotNull AccountDTO accountDTO) {
        String name = accountDTO.getName();
        double price = accountDTO.getPrice();
        AccountType accountType = accountDTO.getAccountType();
        int sellerId = accountDTO.getSellerId();

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        Account account = new Account(name, price, accountType);
        account.setSeller(seller);

        return accountRepository.save(account);
    }

    public String deleteAccount(int id) {
        if (!accountRepository.existsById(id)) {
            return "Account not found";
        }
        accountRepository.deleteById(id);
        return "Account with id " + id + " deleted";
    }

    public String updateAccount(int id, AccountDTO accountDTO) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account existingAccount = optionalAccount.get();
            if (accountDTO.getName() != null) {
                existingAccount.setName(accountDTO.getName());
            }
            if (accountDTO.getPrice() != 0) {
                existingAccount.setPrice(accountDTO.getPrice());
            }
            if (accountDTO.getAccountType() != null) {
                existingAccount.setAccountType(accountDTO.getAccountType());
            }

            accountRepository.save(existingAccount);

            return "Account with id " + id + " updated successfully";
        } else {
            return "Account with id " + id + " not found";
        }
    }
}
