package gleb.dresher.AccountShopApi.controller;

import gleb.dresher.AccountShopApi.dto.AccountDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import gleb.dresher.AccountShopApi.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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

    @Operation(summary = "Get all accounts",
                description = "Get all accounts from database"
                )
    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountService.getAllAccounts();
    }

    @Operation(summary = "Get account by id unique id",
                description = "Return account by account id and code status 200 if account exists, 404 if not",
                responses = {
                @ApiResponse(responseCode = "200", description = "Account found"),
                @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)}
                )
    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") int id) {
        return accountService.getAccountById(id);
    }

    @Operation(summary = "Get all accounts by specific price.",
    description = "Get all account that have price greater then price in param." +
            " If no account found return empty list")
    @GetMapping("/accounts/priceup/{price}")
    public List<Account> getExpensiveAccounts(@PathVariable double price) {
        return accountService.getExpensiveAccounts(price);
    }

    @Operation(summary = "Get all accounts by specific price.",
            description = "Get all account that have price less then price in param." +
                    " If no account found return empty list")
    @GetMapping("/accounts/pricedown/{price}")
    public List<Account> getCheapAccounts(@PathVariable double price) {
        return accountService.getCheapAccounts(price);
    }

    @Operation(summary = "Get all accounts by specific price diaposone.",
            description = "Get all account that have price between min and max in param." +
                    " If no account found return empty list")
    @GetMapping("/accounts/pricebetween/{min}/{max}")
    public List<Account> getPriceDiaposone
            (@PathVariable("min") double min, @PathVariable("max") double max) {
        return accountService.getPriceDiaposone(min, max);
    }

    @Operation(summary = "Get all accounts by specific type.",
        description = "Returns json list of accounts with specific game type, like dota or cs")
    @GetMapping("/accounts/{type}")
    public List<Account> getAccountsByType(@PathVariable("type") String type) {
        return accountService.getAccountsByType(type);
    }

    //TODO
    @Operation(summary = "Add new account",
            description = "Add new account to database," +
                    " if all fields are valid return 200, if not or " +
                    "seller with id not found then return 400")
    @PostMapping("/accounts")
    public ResponseEntity<Account> addAccount(@RequestBody @Valid AccountDTO accountDTO) {
        return accountService.addAccount(accountDTO);
    }

    @Operation(summary = "Delete account by id",
            description = "Delete account by id and return 204," +
                    " if account not found return 404")
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") int id) {
        return accountService.deleteAccount(id);
    }

    @Operation(summary = "Update account by id",
            description = "Update account by id and return 200," +
                    " if account not found return 404")
    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") int id, @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO);
    }
}
