package gleb.dresher.AccountShopApi.service;

import gleb.dresher.AccountShopApi.dto.AccountDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final SellerRepository sellerRepository;

    public AccountService(AccountRepository accountRepository, SellerRepository sellerRepository) {
        this.accountRepository = accountRepository;
        this.sellerRepository = sellerRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public ResponseEntity<Account> getAccountById(int id) {
        Account account = accountRepository.customIdSearch(id);
        if (account != null){
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
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

    public ResponseEntity<Account> addAccount(@NotNull AccountDTO accountDTO) {
        String name = accountDTO.getName();
        double price = accountDTO.getPrice();
        AccountType accountType = accountDTO.getAccountType();
        int sellerId = accountDTO.getSellerId();

        Seller seller;
        try {
            seller = sellerRepository.findById(sellerId)
                    .orElseThrow(() -> new RuntimeException("Seller not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
        Account account = new Account(name, price, accountType);
        account.setSeller(seller);

        URI location = URI.create("/accounts/" + account.getId());
        return ResponseEntity.created(location).body(accountRepository.save(account));
    }

    public ResponseEntity<Void> deleteAccount(int id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<Account> updateAccount(int id, AccountDTO accountDTO) {
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

            return ResponseEntity.ok().body(existingAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
