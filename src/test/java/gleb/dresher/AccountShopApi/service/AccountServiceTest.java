package gleb.dresher.AccountShopApi.service;

import gleb.dresher.AccountShopApi.dto.AccountDTO;
import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.entity.Seller;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import gleb.dresher.AccountShopApi.repository.SellerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account1;
    private Account account2;

    @BeforeEach
    public void setup() {
        account1 = new Account(
                "Dota 2 account TEST",
                152.0,
                AccountType.DOTA);

        account2 = new Account(
                "Fortnite account TEST",
                192.0,
                AccountType.FORTNITE);

    }

    @Test
    public void AccountServiceGetExpensiveAccountsGreaterThen100(){

        List<Account> accounts = Arrays.asList(account1 ,account2);
        when(accountRepository.findByPriceGreaterThan(Mockito.anyDouble())).thenReturn(accounts);

        List<Account> expensiveAccounts = accountService.getExpensiveAccounts(100.0);
        Assertions.assertThat(expensiveAccounts.size()).isEqualTo(2);

    }

    @Test
    public void AccountServiceGetDotaAccounts(){

        List<Account> accounts = List.of(account1);
        when(accountRepository.findByAccountType(Mockito.anyString())).thenReturn(accounts);

        List<Account> dotaAccounts = accountService.getAccountsByType("dota");
        Assertions.assertThat(dotaAccounts.size()).isEqualTo(1);
    }

    @Test
    public void AccountServiceGetAllAccounts() {
        List<Account> accounts = Arrays.asList(account1 ,account2);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> allAccounts = accountService.getAllAccounts();
        Assertions.assertThat(allAccounts.size()).isEqualTo(2);

    }

    @Test
    public void AccountServiceGetAccountById() {
        account1.setId(1);
        account2.setId(2);

        when(accountRepository.customIdSearch(1)).thenReturn(account1);
        when(accountRepository.customIdSearch(2)).thenReturn(account2);

        ResponseEntity<Account> getFirstAccount = accountService.getAccountById(1);
        ResponseEntity<Account> getSecondAccount = accountService.getAccountById(2);

        Assertions.assertThat(Objects.requireNonNull(getFirstAccount.getBody()).getId()).isEqualTo(1);
        Assertions.assertThat(Objects.requireNonNull(getSecondAccount.getBody()).getId()).isEqualTo(2);
    }


    @Test
    public void AccountServiceCreateAccount() {
        AccountDTO accountDTO = new AccountDTO(
                "Dota 2 account TEST",
                152.0,
                AccountType.DOTA,
                1
        );

        Seller seller = new Seller("Adolf", "adolf@gmail.com");
        seller.setId(1);

        when(sellerRepository.findById(1)).thenReturn(Optional.of(seller));
        account1.setSeller(seller);
        when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account1);

        Account savedAccount = accountService.addAccount(accountDTO);

        Assertions.assertThat(savedAccount.getName()).isEqualTo(account1.getName());
        Assertions.assertThat(savedAccount).isNotNull();
        Assertions.assertThat(savedAccount.getSeller()).isNotNull();
    }

    @Test
    public void AccountServiceUpdateAccount() {
        int accountId = 1;
        AccountDTO accountDTO = new AccountDTO(
                "Dota 2 account 10 mmr",
                1000,
                AccountType.DOTA,
                1
        );

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account1));
        when(accountRepository.save(Mockito.any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));


        String updatingResult = accountService.updateAccount(accountId, accountDTO);

        Assertions.assertThat(updatingResult).isEqualTo("Account with id " + accountId + " updated successfully");

        Assertions.assertThat(account1.getName()).isEqualTo(accountDTO.getName());
        Assertions.assertThat(account1.getPrice()).isEqualTo(accountDTO.getPrice());
        Assertions.assertThat(account1.getAccountType()).isEqualTo(accountDTO.getAccountType());

        verify(accountRepository).save(account1);
    }

    @Test
    public void testUpdateAccount_NotFound() {
        int accountId = 999;
        AccountDTO accountDTO = new AccountDTO("Test Name", 100, AccountType.DOTA, 1);

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        String updatingResult = accountService.updateAccount(accountId, accountDTO);

        Assertions.assertThat(updatingResult).isEqualTo("Account with id " + accountId + " not found");

        verify(accountRepository, never()).save(Mockito.any());
    }

    @Test
    public void AccountServiceDeleteAccount() {
        int accountId = 1;

        when(accountRepository.existsById(accountId)).thenReturn(true);

        String result = accountService.deleteAccount(accountId);

        Assertions.assertThat(result).isEqualTo("Account with id " + accountId + " deleted");


        verify(accountRepository, times(1)).deleteById(accountId);
    }

    @Test
    public void testDeleteAccount_NotFound() {
        int accountId = 52;

        when(accountRepository.existsById(accountId)).thenReturn(false);

        String result = accountService.deleteAccount(accountId);

        Assertions.assertThat(result).isEqualTo("Account not found");

        verify(accountRepository, never()).deleteById(Mockito.anyInt());
    }
}