package gleb.dresher.AccountShopApi.controller;

import gleb.dresher.AccountShopApi.dto.AccountDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import gleb.dresher.AccountShopApi.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/accountsfindbyid/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") int id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/accounts/priceup/{price}")
    public List<Account> getExpensiveAccounts(@PathVariable double price) {
        return accountService.getExpensiveAccounts(price);
    }

    @GetMapping("/accounts/pricedown/{price}")
    public List<Account> getCheapAccounts(@PathVariable double price) {
        return accountService.getCheapAccounts(price);
    }

    @GetMapping("/accounts/pricebetween/{min}/{max}")
    public List<Account> getPriceDiaposone
            (@PathVariable("min") double min, @PathVariable("max") double max) {
        return accountService.getPriceDiaposone(min, max);
    }

    @GetMapping("/accounts/{type}")
    public List<Account> getAccountsByType(@PathVariable("type") String type) {
        return accountService.getAccountsByType(type);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> addAccount(@RequestBody AccountDTO accountDTO) {
        return accountService.addAccount(accountDTO);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") int id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") int id, @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO);
    }
}
