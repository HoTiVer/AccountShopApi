package gleb.dresher.AccountShopApi.Repository;

import gleb.dresher.AccountShopApi.entity.Account;
import gleb.dresher.AccountShopApi.enums.AccountType;
import gleb.dresher.AccountShopApi.repository.AccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AccountRepositoryTest {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountRepositoryTest(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Test
    public void AccountRepositoryTest_FindByAccountType_Dota() {
        Account account1 = new Account("Dota 2 account TEST",
                52.0,
                AccountType.DOTA);

        Account account2 = new Account("Fortnite account TEST",
                92.0,
                AccountType.FORTNITE);

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findByAccountType("DOTA");

        Assertions.assertThat(accounts.size()).isEqualTo(1);
    }

    @Test
    public void AccountRepositoryTest_FindPriceLessThen100(){
        Account account1 = new Account("Dota 2 account TEST",
                52.0,
                AccountType.DOTA);

        Account account2 = new Account("Fortnite account TEST",
                92.0,
                AccountType.FORTNITE);

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findByPriceLessThan(100);

        Assertions.assertThat(accounts.size()).isEqualTo(2);
    }

    @Test
    public void AccountRepositoryTest_FindAccountById(){
        Account account1 = new Account("Dota 2 account TEST",
                52.0,
                AccountType.DOTA);

        Account account2 = new Account("Fortnite account TEST",
                92.0,
                AccountType.FORTNITE);

        accountRepository.save(account1);
        accountRepository.save(account2);

        Assertions.assertThat(accountRepository.customIdSearch(1)).isEqualTo(account1);
        Assertions.assertThat(accountRepository.customIdSearch(2)).isEqualTo(account2);
    }
}
